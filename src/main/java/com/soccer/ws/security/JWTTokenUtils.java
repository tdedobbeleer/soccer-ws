package com.soccer.ws.security;

import com.google.common.base.Strings;
import com.soccer.ws.persistence.UserDetailsAdapter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.jasypt.util.text.BasicTextEncryptor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenUtils implements TokenUtils {

    private final Logger logger = Logger.getLogger(this.getClass());

    private final String AUDIENCE_UNKNOWN = "unknown";
    private final String AUDIENCE_WEB = "web";
    private final String AUDIENCE_MOBILE = "mobile";
    private final String AUDIENCE_TABLET = "tablet";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.fp.secret}")
    private String fingerPrintsecret;

    @Value("${jwt.token.expiration.short}")
    private String shortLivedExpiration;

    @Value("${jwt.token.expiration.long}")
    private String longLivedExpiration;

    @Override
    public Boolean validateToken(String token, UserDetails userDetails, String fingerPrint) {
        UserDetailsAdapter user = (UserDetailsAdapter) userDetails;
        Claims claims = getClaimsFromToken(token);
        final String username = this.getUsername(claims);
        final DateTime created = this.getCreatedDate(claims);
        final DateTime expirationTime = this.getExpirationDate(claims);
        final long diff = expirationTime.getMillis() - created.getMillis();

        if (diff <= Duration.parse(shortLivedExpiration).toMillis()) {
            return (username.equals(user.getUsername())
                    && !(this.isCreatedBeforeLastPasswordReset(created, user.getPasswordLastSet())));
        } else {
            final String tkFingerPrint = this.getFingerPrint(claims);
            return (username.equals(user.getUsername())
                    && (!Strings.isNullOrEmpty(tkFingerPrint)
                    && tkFingerPrint.equals(fingerPrint))
                    && !(this.isCreatedBeforeLastPasswordReset(created, user.getPasswordLastSet())));
        }

    }

    @Override
    public String generateToken(UserDetails userDetails, Device device, String fingerPrint, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<String, Object>();
        DateTime now = DateTime.now();
        claims.put("sub", userDetails.getUsername());
        claims.put("audience", this.generateAudience(device));
        claims.put("created", now.toDate());
        if (rememberMe) {
            claims.put("fingerprint", encryptFingerPrint(fingerPrint));
        }
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(rememberMe ? this.generateLongLivedExpirationDate(now).toDate() : this.generateShortLivedExpirationDate(now).toDate())
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();
    }

    @Override
    public String getUsernameFromToken(final String token) {
        String username;
        try {
            username = getUsername(getClaimsFromToken(token));
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private String getUsername(final Claims claims) {
        String username;
        try {
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private DateTime getCreatedDate(final Claims claims) {
        DateTime created;
        try {
            created = new DateTime(claims.get("created"));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    private String getFingerPrint(final Claims claims) {
        String created;
        try {
            created = decryptFingerPrint((String) claims.get("fingerprint"));
        } catch (Exception e) {
            created = "";
        }
        return created;
    }

    private DateTime getExpirationDate(final Claims claims) {
        DateTime expiration;
        try {
            expiration = new DateTime(claims.getExpiration());
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private String getAudience(final Claims claims) {
        String audience;
        try {
            audience = (String) claims.get("audience");
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    private DateTime generateShortLivedExpirationDate(DateTime dt) {
        Duration duration = Duration.parse(shortLivedExpiration);
        return dt.plusSeconds(Math.toIntExact(duration.getSeconds()));
    }

    private DateTime generateLongLivedExpirationDate(DateTime dt) {
        Duration duration = Duration.parse(longLivedExpiration);
        return dt.plusSeconds(Math.toIntExact(duration.getSeconds()));
    }

    private Boolean isCreatedBeforeLastPasswordReset(DateTime created, DateTime lastPasswordReset) {
        return (lastPasswordReset != null && created.isBefore(lastPasswordReset));
    }

    private String generateAudience(Device device) {
        String audience = this.AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = this.AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    private Boolean ignoreTokenExpiration(Claims claims) {
        String audience = this.getAudience(claims);
        return (this.AUDIENCE_TABLET.equals(audience) || this.AUDIENCE_MOBILE.equals(audience));
    }

    private String encryptFingerPrint(final String fingerPrint) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(fingerPrintsecret);
        return textEncryptor.encrypt(fingerPrint);
    }

    private String decryptFingerPrint(final String encryptedFingerPrint) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(fingerPrintsecret);
        return textEncryptor.decrypt(encryptedFingerPrint);
    }
}
