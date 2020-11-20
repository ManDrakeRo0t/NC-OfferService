package ru.bogatov.offerservice.configiration;

import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import ru.bogatov.offerservice.entity.Role;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
@Log
public class JwtProvider {
    //@Value("${jwt.secret}")
    private String JwtSecret = "amps";

    public String generateM2mToken(){
        Date date = Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("M2M","valid");
        return "Bearer " +Jwts.builder()
                .setClaims(claims)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512,JwtSecret)
                .compact();
    }


    public boolean validateToken(String token){
        try{
            Claims claims = Jwts.parser().setSigningKey(JwtSecret).parseClaimsJws(token).getBody();
            if(isM2mToken(token) || ("valid".equals(claims.get("gateway").toString()))) return true;
        }catch (ExpiredJwtException expEx) {
            log.severe("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.severe("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.severe("Malformed jwt");
        } catch (SignatureException sEx) {
            log.severe("Invalid signature");
        } catch (Exception e) {
            log.severe("invalid token");
        }
        return false;
    }

    public boolean isM2mToken(String token){
        Claims claims = Jwts.parser().setSigningKey(JwtSecret).parseClaimsJws(token).getBody();
        if(claims.get("M2M") != null){
            return "valid".equals(claims.get("M2M").toString());
        }
        return false;
    }

    public Set<Role> getRolesFromToken(String token){
        Set<Role> roles = new HashSet<>();
        List<String> rolesString = ((ArrayList) Jwts.parser().setSigningKey(JwtSecret).parseClaimsJws(token).getBody().get("roles"));
        for(String s : rolesString) roles.add(Role.valueOf(s));
        return roles;
    }

    public String getLoginFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(JwtSecret).parseClaimsJws(token).getBody();
        return claims.get("email").toString();
    }

}