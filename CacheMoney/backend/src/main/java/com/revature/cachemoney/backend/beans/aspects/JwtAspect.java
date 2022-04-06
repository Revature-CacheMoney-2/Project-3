package com.revature.cachemoney.backend.beans.aspects;

import com.revature.cachemoney.backend.beans.security.JwtUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Aspect to retrieve JWT information from requests.
 * Used alongside @RequireJwt.
 * 
 * @author Cody Gonsowski and Jeffrey Lor
 */
@Aspect
@Component
public class JwtAspect {
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAspect(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Token validator for JWT stuff.
     * 
     * @param jp contains arguments containing the JWT string and the associated User's ID
     * @return an OK to proceed
     * @throws Throwable this is a general throwable exception
     */
    @Around("@annotation(com.revature.cachemoney.backend.beans.annotations.RequireJwt)")
    @SuppressWarnings("unchecked")
    public ResponseEntity<String> validate(ProceedingJoinPoint jp) throws Throwable {
        // retrieve the arguments from the join point
        Object[] args = jp.getArgs();

        // if either the token or userId is invalid
        if (!jwtUtil.validateToken((String) args[0], (Integer) args[1])) {
            return ResponseEntity.badRequest().build();
        }

        // let the function know to keep going
        return (ResponseEntity<String>) jp.proceed();
    }
}
