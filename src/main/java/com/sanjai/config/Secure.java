package com.sanjai.config;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@javax.ws.rs.NameBinding
@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.METHOD})
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})

public @interface Secure {
 
}
  