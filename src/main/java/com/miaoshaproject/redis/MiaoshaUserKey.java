package com.miaoshaproject.redis;


/**
 * @author cyx
 * @data 2019/3/15 12:31
 */
public class MiaoshaUserKey extends BasePrefix {
    public static final int TOKEN_EXPIRE=3600*24*2;
    private MiaoshaUserKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }
    public static   MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE,"tk");
    public static   MiaoshaUserKey getByid = new MiaoshaUserKey(0,"id");
}
