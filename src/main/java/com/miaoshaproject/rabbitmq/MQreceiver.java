package com.miaoshaproject.rabbitmq;

import com.miaoshaproject.Vo.GoodsVo;
import com.miaoshaproject.domain.MiaoshaOrder;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;

/**
 * @author cyx
 * @data 2019/5/6 19:30
 */
@Service
public class MQreceiver {
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserService userService;
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;

    private Logger logger= LoggerFactory.getLogger(MQreceiver.class);

    /**
     *
     * @param message
     */
    @RabbitListener(queues = MQconfig.MIAOSHA_QUEUE)
    public void receiveMiaoshaMessage(String message){
        MiaoshaMessage miaoshaMessage = RedisService.StringToBean(message,MiaoshaMessage.class);
        MiaoshaUser user=miaoshaMessage.getMiaoshaUser();
        long goodsId=miaoshaMessage.getGoodsId();

        GoodsVo goods= goodsService.getGoodsVoBygoodsId(goodsId);
        int stockcount=goods.getStockCount();
        if(stockcount<=0){
            return;
        }
        miaoshaService.miaosha(user,goods);
    }

//           @RabbitListener(queues = MQconfig.TOPIC_QUEUE1)
//        public void receiveTopic1(String message){
//            logger.info("topic queue1 receive message:"+message);
//
//        }
//
//
//        @RabbitListener(queues = MQconfig.TOPIC_QUEUE2)
//        public void receiveTopic2(String message){
//            logger.info("topic queue2  receive message:"+message);
//
//        }
}
