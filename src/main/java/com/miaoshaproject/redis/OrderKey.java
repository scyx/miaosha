package com.miaoshaproject.redis;

/**
 * @author cyx
 * @data 2019/3/15 12:30
 */
public class OrderKey extends BasePrefix {
    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey order = new OrderKey(0,"order");
}
