package com.company.go.application.port.in.inventory;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotExceedProductSizeValidator.class)
public @interface NotExceedProductSize {
    String message() default "{com.company.notExceedProductSize}";
    Class< ? > [] groups() default {};
    Class< ? > [] payload() default {};

}
