package com.soccer.ws.security;

import com.soccer.ws.persistence.UserDetailsAdapter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenUtils implements TokenUtils {

    @Value("${jwt.token.secret}")
    private String secret;

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

        return (username.equals(user.getUsername())
                && !(this.isCreatedBeforeLastPasswordReset(created, user.getPasswordLastSet())));
    }

    @Override
    public String generateToken(UserDetails userDetails, String fingerPrint, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<String, Object>();
        DateTime now = DateTime.now();
        claims.put("sub", userDetails.getUsername());
        claims.put("created", now.toDate());
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
}
