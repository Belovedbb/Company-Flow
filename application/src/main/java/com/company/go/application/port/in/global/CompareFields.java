package com.company.go.application.port.in.global;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompareFieldsValidator.class)
public @interface CompareFields {
    String message() default "{com.company.compareFields}";
    Class< ? > [] groups() default {};
    Class< ? > [] payload() default {};

    String [] args();
}
