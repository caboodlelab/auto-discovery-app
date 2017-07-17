package com.caboodle.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author harishchauhan
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface UrlPath {

	String name() default "";

	String consumes() default "";

	String produces() default "";

	String method() default "";
}