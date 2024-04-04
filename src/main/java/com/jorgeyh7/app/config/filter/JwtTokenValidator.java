package com.jorgeyh7.app.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jorgeyh7.app.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {


        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(jwtToken!=null){
            jwtToken=jwtToken.substring(7);//se extrae el string a partir la posicion 7
            DecodedJWT decodedJWT=jwtUtils.validateToken(jwtToken);
            String username=jwtUtils.extractUsername(decodedJWT);
            String stringAutorities= jwtUtils.getSpecificClaim(decodedJWT,"authorities").asString();
            Collection<? extends GrantedAuthority> authorities= AuthorityUtils.commaSeparatedStringToAuthorityList(stringAutorities);

            //set security context
            SecurityContext context= SecurityContextHolder.getContext();
            Authentication authenticationToken=new UsernamePasswordAuthenticationToken(username,null,authorities);
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);

        }
        filterChain.doFilter(request,response);

    }
}
