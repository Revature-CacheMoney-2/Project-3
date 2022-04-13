package com.revature.cachemoney.backend.beans.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for noting which controller methods require a JWT.
 * 
 * @author Cody Gonsowski and Jeffrey Lor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireJwt {

}
