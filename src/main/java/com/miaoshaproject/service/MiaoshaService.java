package com.miaoshaproject.service;

import com.miaoshaproject.Vo.GoodsVo;
import com.miaoshaproject.dao.GoodsDao;
import com.miaoshaproject.domain.Goods;
import com.miaoshaproject.domain.MiaoshaOrder;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.domain.OrderInfo;
import com.miaoshaproject.redis.MiaoshaKey;
import com.miaoshaproject.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cyx
 * @data 2019/4/3 17:09
 */
@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;
    Logger logger = LoggerFactory.getLogger(MiaoshaService.class);

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if(success){
            return orderService.createOrder(user,goods);
        }
        return null;
    }

    public long getMiaoshaResult(Long userId, long goodsId) {
       MiaoshaOrder order = orderService.getOrderByGoodsIdUserId(userId, goodsId);
        if(order != null) {//秒杀成功
            logger.info("orderid"+order.getOrderId());
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                logger.info(""+0);
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }
//
//    public void reset(List<GoodsVo> goodsList) {
//        goodsService.resetStock(goodsList);
//        orderService.deleteOrders();
//    }
}
