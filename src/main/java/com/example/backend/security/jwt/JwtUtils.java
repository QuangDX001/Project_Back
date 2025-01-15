package com.example.backend.security.jwt;

import com.example.backend.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


/**
 * Created by Admin on 10/9/2023
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    //private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${app.jwtCookieName}")
    private String jwtCookie;

    public String generateJwtToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(String.format("%s,%s", userPrincipal.getId(), userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

//    public String getUsernameFromJwtToken(String token){
//        return Jwts.parserBuilder().setSigningKey(key()).build()
//                .parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public long getIdFromJwtToken(String token) {
//        String subject = getUsernameFromJwtToken(token);
//
//        // Split the subject to get ID and username
//        String[] idAndUsername = subject.split(",");
//
//        // Check if there are at least two parts (ID and username)
//        if (idAndUsername.length >= 2) {
//            return Long.parseLong(idAndUsername[1].trim());
//        } else {
//            // Handle the case where the subject does not contain the expected format
//            throw new IllegalArgumentException("Invalid subject format in JWT token");
//        }
//    }

    public String getJwtFromCookie(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null){
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(Authentication au){
        String jwt = generateJwtToken(au);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public long getIdFromJwtToken(String token){
        String subject = getClaimFromToken(token,Claims::getSubject);
        String[] idAndUsername = subject.split(",");
        long id = Integer.parseInt(idAndUsername[0]);
        return id;
    }

    public String getUsernameFromJwtToken(String token) {
        String subject = getClaimFromToken(token,Claims::getSubject);
        String[] idAndUsername = subject.split(",");
        String username = idAndUsername[1];
        return username;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            //httpServletRequest.setAttribute("invalidSignature", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            //httpServletRequest.setAttribute("invalidToken", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            //httpServletRequest.setAttribute("expired", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            //httpServletRequest.setAttribute("unsupported", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            //httpServletRequest.setAttribute("empty", e.getMessage());
        }
        return false;
    }

}
