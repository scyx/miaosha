package com.miaoshaproject.redis;

/**
 * @author cyx
 * @data 2019/7/24 16:32
 */
public class MiaoshaKey extends BasePrefix {
    public MiaoshaKey(int expireseconds,String prefix){
        super(expireseconds,prefix);
    }
    public static MiaoshaKey isGoodsOver=new MiaoshaKey(0,"isover");
}
