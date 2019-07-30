package com.miaoshaproject.validator;


import com.miaoshaproject.Util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author cyx
 * @data 2019/4/2 11:23
 */
public class ismobileValidator implements ConstraintValidator<ismobile,String> {
    private boolean required=false;
    @Override
    public void initialize(ismobile constraintAnnotation) {
        required=constraintAnnotation.isrequired();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required){
            return ValidatorUtil.isMobile(value);
        }else{
            if(StringUtils.isEmpty(value)){
                return true;
            }else{
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
