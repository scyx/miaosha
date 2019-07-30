package com.miaoshaproject.Util;

import java.util.UUID;

/**
 * @author cyx
 * @data 2019/4/2 13:45
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
