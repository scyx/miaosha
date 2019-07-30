package com.miaoshaproject.rabbitmq;

import com.miaoshaproject.domain.MiaoshaUser;

/**
 * @author cyx
 * @data 2019/7/24 15:38
 */
public class MiaoshaMessage {
    private MiaoshaUser miaoshaUser;
    private long goodsId;

    public MiaoshaUser getMiaoshaUser() {
        return miaoshaUser;
    }

    public void setMiaoshaUser(MiaoshaUser miaoshaUser) {
        this.miaoshaUser = miaoshaUser;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
