package claudiaburali.capstoneproject.security;

import claudiaburali.capstoneproject.entities.User;
import claudiaburali.capstoneproject.exceptions.UnauthorizedException;
import claudiaburali.capstoneproject.services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); // "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MTk4MjkwNjksImV4cCI6MTcyMDQzMzg2OSwic3ViIjoiOTFkMTg2MGItZjE2Yy00MTYwLWIyYTYtODU2NWY0MzY5MTBiIn0.1hEDloV0FbYnw5U8mwg0CsIWLVos6qZSYPOCJUyhG8o"

        if(authHeader == null || !authHeader.startsWith("Bearer ")) throw new UnauthorizedException("Per favore inserisci correttamente il token nell'header");

        String accessToken = authHeader.substring(7); // "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MTk4MjkwNjksImV4cCI6MTcyMDQzMzg2OSwic3ViIjoiOTFkMTg2MGItZjE2Yy00MTYwLWIyYTYtODU2NWY0MzY5MTBiIn0.1hEDloV0FbYnw5U8mwg0CsIWLVos6qZSYPOCJUyhG8o"

        jwtTools.verifyToken(accessToken);

        String userId = jwtTools.extractIdFromToken(accessToken);
        User currentUser = usersService.findById(UUID.fromString(userId));

        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
