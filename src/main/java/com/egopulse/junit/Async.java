package com.egopulse.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Async {

    Long DEFAULT_TIMEOUT = 5L;
    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    long value() default 5L;
    TimeUnit unit() default TimeUnit.SECONDS;

}
