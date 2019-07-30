package com.miaoshaproject.Vo;

import com.miaoshaproject.domain.Goods;
import com.miaoshaproject.domain.OrderInfo;

/**
 * @author cyx
 * @data 2019/5/4 20:47
 */
public class OrderDetailVo {
    private Goods goods;
    private OrderInfo order;

    public Goods getGoods() {
        return goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }


}
