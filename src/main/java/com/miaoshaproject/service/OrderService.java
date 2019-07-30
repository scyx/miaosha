package com.miaoshaproject.service;

import com.miaoshaproject.Vo.GoodsVo;
import com.miaoshaproject.dao.OrderDao;
import com.miaoshaproject.domain.MiaoshaGoods;
import com.miaoshaproject.domain.MiaoshaOrder;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.domain.OrderInfo;
import com.miaoshaproject.redis.OrderKey;
import com.miaoshaproject.redis.RedisService;
import org.omg.CORBA.ORB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Queue;

/**
 * @author cyx
 * @data 2019/4/3 17:04
 */
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisService redisService;

    public MiaoshaOrder getOrderByGoodsIdUserId(long userId,long goodsId) {
        return redisService.get(OrderKey.order,""+userId+"_"+goodsId,MiaoshaOrder.class);
    }
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        MiaoshaOrder order=new MiaoshaOrder();
        order.setGoodsId(goods.getId());
        order.setOrderId(orderInfo.getId());
        order.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(order);
        redisService.set(OrderKey.order,""+user.getId()+"_"+goods.getId(),order);
        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
