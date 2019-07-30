package com.miaoshaproject.redis;

/**
 * @author cyx
 * @data 2019/3/15 12:30
 */
public class GoodsKey extends BasePrefix {
    public GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }
    public static GoodsKey getgoodsList=new GoodsKey(60,"gl");

    public static GoodsKey getgoodsStock=new GoodsKey(0,"goodstock");
}
