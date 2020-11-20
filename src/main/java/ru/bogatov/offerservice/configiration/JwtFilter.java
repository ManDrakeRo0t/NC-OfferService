package ru.bogatov.offerservice.configiration;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Log
public class JwtFilter extends GenericFilterBean {

    public static final String AUTH = "Authorization";
    private JwtProvider jwtProvider;

    public JwtFilter(@Autowired JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if(token != null && jwtProvider.validateToken(token)){
            CustomUserDetails customUserDetails;
            if(jwtProvider.isM2mToken(token)){
                customUserDetails = CustomUserDetails.fromM2tToken();
            }else{
                customUserDetails = CustomUserDetails.fromGateWayToken(jwtProvider.getLoginFromToken(token),jwtProvider.getRolesFromToken(token));
            }
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails,
                    null,
                    customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String bearer = request.getHeader(AUTH);
        if(bearer != null && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }
}
