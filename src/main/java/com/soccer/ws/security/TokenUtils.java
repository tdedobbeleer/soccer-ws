package com.soccer.ws.security;

import com.soccer.ws.persistence.UserDetailsAdapter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

  private final Logger logger = Logger.getLogger(this.getClass());

  private final String AUDIENCE_UNKNOWN   = "unknown";
  private final String AUDIENCE_WEB       = "web";
  private final String AUDIENCE_MOBILE    = "mobile";
  private final String AUDIENCE_TABLET    = "tablet";

  @Value("${jwt.token.secret}")
  private String secret;

  @Value("${jwt.token.expiration}")
  private String expiration;

    public Boolean validateToken(String token, UserDetails userDetails) {
        UserDetailsAdapter user = (UserDetailsAdapter) userDetails;
        Claims claims = getClaimsFromToken(token);
        final String username = this.getUsername(claims);
        final DateTime created = this.getCreatedDate(claims);
        return (username.equals(user.getUsername()) && !(this.isCreatedBeforeLastPasswordReset(created, user.getPasswordLastSet())));
    }

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
        created = new DateTime((long) claims.get("created"));
    } catch (Exception e) {
      created = null;
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

    private long generateCurrentDate() {
        return DateTime.now().getMillis();
  }

  private Date generateExpirationDate() {
      Duration duration = Duration.parse(expiration);
      return new DateTime().plusSeconds(Math.toIntExact(duration.getSeconds())).toDate();
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

  public String generateToken(UserDetails userDetails, Device device) {
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("sub", userDetails.getUsername());
    claims.put("audience", this.generateAudience(device));
    claims.put("created", this.generateCurrentDate());
    return this.generateToken(claims);
  }

  private String generateToken(Map<String, Object> claims) {
    return Jwts.builder()
      .setClaims(claims)
      .setExpiration(this.generateExpirationDate())
      .signWith(SignatureAlgorithm.HS512, this.secret)
      .compact();
  }

  public Boolean canTokenBeRefreshed(String token, DateTime lastPasswordReset) {
      Claims claims = getClaimsFromToken(token);
      final DateTime created = this.getCreatedDate(claims);
      return (!(this.isCreatedBeforeLastPasswordReset(created, lastPasswordReset)) && this.ignoreTokenExpiration(claims));
  }

  public String refreshToken(String token) {
    String refreshedToken;
    try {
      final Claims claims = this.getClaimsFromToken(token);
      claims.put("created", this.generateCurrentDate());
      refreshedToken = this.generateToken(claims);
    } catch (Exception e) {
      refreshedToken = null;
    }
    return refreshedToken;
  }

}
