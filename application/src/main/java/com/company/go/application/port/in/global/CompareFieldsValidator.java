package com.company.go.application.port.in.global;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompareFieldsValidator implements ConstraintValidator<CompareFields, Object> {
    private String args[];

    @Override
    public void initialize(CompareFields constraintAnnotation) {
        this.args = constraintAnnotation.args();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if(args == null || args.length == 0){
            return false;
        }
        if(args.length < 2){
            args = args[0].split("\\s*,\\s*");
        }
        for (String arg1 : args) {
            Object basePropertyValue = new BeanWrapperImpl(object).getPropertyValue(arg1);
            for (String arg : args) {
                Object propertyValue = new BeanWrapperImpl(object).getPropertyValue(arg);
                if (basePropertyValue == null || propertyValue == null || !basePropertyValue.equals(propertyValue)) {
                    return false;
                }
            }
        }
        return true;
    }
}
