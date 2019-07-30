package com.miaoshaproject.rabbitmq;

import com.miaoshaproject.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cyx
 * @data 2019/5/6 19:30
 */
@Service
public class MQsender {
    private Logger logger= LoggerFactory.getLogger(MQsender.class);
    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * direct模式
     * @param miaoshaMessage
     */
    public void sendMiaoshaMessage(MiaoshaMessage miaoshaMessage){
        String msg= RedisService.beanToString(miaoshaMessage);
        logger.info("send miaoshamessage:"+miaoshaMessage);
        amqpTemplate.convertAndSend(MQconfig.MIAOSHA_QUEUE,msg);
    }

    /**
     * topic模式
     * @param message
     */
//    public void sendTopic(Object message){
//        String msg= RedisService.beanToString(message);
//        logger.info("send topic message:"+message);
//        amqpTemplate.convertAndSend(MQconfig.TOPIC_EXCHANGE,"topic.key1",msg+"1");
//        amqpTemplate.convertAndSend(MQconfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");
//    }
//
//    /**
//     * fanout模式 交换机Exchange
//     */
//    public void sendFanout(Object message){
//        String msg= RedisService.beanToString(message);
//        logger.info("send fanout message:"+message);
//        amqpTemplate.convertAndSend(MQconfig.FANOUT_EXCHANGE,"",msg+"1");
//    }
}
