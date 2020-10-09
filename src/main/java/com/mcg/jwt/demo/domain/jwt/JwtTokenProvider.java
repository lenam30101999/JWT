package com.mcg.jwt.demo.domain.jwt;

import com.mcg.jwt.demo.domain.caches.CacheManager;
import com.mcg.jwt.demo.domain.constants.Constants;
import com.mcg.jwt.demo.domain.entity.CustomUserDetails;
import com.mcg.jwt.demo.domain.entity.User;
import com.mcg.jwt.demo.domain.utils.GenKey;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    @Autowired protected CacheManager cacheManager;

    @Value("${app.jwtSecret.accessToken}")
    private String JWT_SECRET_TOKEN;

    @Value("${app.jwtExpirationInMs.accessToken}")
    private long JWT_EXPIRATION_TOKEN;

    public String generateAccessToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_TOKEN);
        return Jwts.builder()
                .setHeader(header())
                .setClaims(getClaims(userDetails.getUser()))
                .setSubject(Long.toString(userDetails.getUser().getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_TOKEN)
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails userDetails){
        String id = Long.toString(userDetails.getUser().getId());
        String token = Jwts.builder()
                .setClaims(stringRandomGenerator())
                .setSubject(id)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_TOKEN)
                .compact();
        cacheManager.setValue(GenKey.genRefreshKey(token), id, Constants.TIME_OUT);
        return token;
    }

    public int getIdFromSubjectJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_TOKEN)
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_TOKEN).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private Map<String, Object> getClaims(User user){
        Map<String, Object> mClaims = new HashMap<>();
        mClaims.put("email", user.getEmail());
        mClaims.put("role", user.getRole());
        mClaims.put("state", user.getState());
        return mClaims;
    }

    private Map<String, Object> header(){
        Map<String, Object> map = new HashMap<>();
        map.put("typ", "JWT");
        return map;
    }

    private Map<String, Object> stringRandomGenerator(){
        Map<String, Object> mClaims = new HashMap<>();
        String randomString = RandomStringUtils.randomAlphabetic(60);
        mClaims.put("info", randomString);
        return mClaims;
    }
}
