package com.miaoshaproject.redis;

/**
 * @author cyx
 * @data 2019/3/15 12:10
 */
public class UserKey extends BasePrefix {

    private UserKey (String prefix){
        super(prefix);
    }
    public static UserKey getByid=new UserKey("id");
    public static UserKey getByname=new UserKey("name");
}
