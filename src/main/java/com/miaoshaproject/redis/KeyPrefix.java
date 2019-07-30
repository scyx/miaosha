package com.miaoshaproject.redis;

/**
 * @author cyx
 * @data 2019/3/15 12:03
 */
public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();
}
