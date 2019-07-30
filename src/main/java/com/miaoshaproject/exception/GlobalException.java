package com.miaoshaproject.exception;

import com.miaoshaproject.result.CodeMsg;
import com.sun.org.apache.bcel.internal.classfile.Code;

/**
 * @author cyx
 * @data 2019/4/2 12:29
 */
public class GlobalException extends  RuntimeException {
    private CodeMsg cm;

    public CodeMsg getCm() {
        return cm;
    }

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm=cm;
    }

}
