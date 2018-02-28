package com.annotation.mvp.annotationmvp.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rahul.abrol on 12/02/18.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Core {
}
