package com.miaoshaproject.Util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author cyx
 * @data 2019/3/15 13:00
 */
public class MD5util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
    public static final String salt="1a2b3c4d";

    public static String inputPassToformPass(String inputpass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputpass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDbPass(String formpass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+formpass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputpassTodbpass(String inputpass,String saltDb){
        String formpass=inputPassToformPass(inputpass);
        String dbpass=formPassToDbPass(formpass,saltDb);
        return dbpass;

    }
    public static void main(String[] args){
        System.out.println(inputpassTodbpass("123456","1a2b3c4d"));
    }

}
