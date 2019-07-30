package com.miaoshaproject.redis;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * @author cyx
 * @data 2019/3/15 12:05
 */
public abstract class BasePrefix implements KeyPrefix {
    private int expireSeconds;

    private String prefix;
    public BasePrefix(String prefix){ //0代表永不过期
      this(0,prefix);
    }
    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds=expireSeconds;
        this.prefix=prefix;
    }
    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
      String classname=this.getClass().getSimpleName();
      return classname+":"+prefix;
    }
}
