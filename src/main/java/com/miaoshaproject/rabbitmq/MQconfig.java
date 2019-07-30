package com.miaoshaproject.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cyx
 * @data 2019/5/6 19:30
 */
@Configuration
public class MQconfig {
    public static  final String QUEUE="queue";
    public static  final String MIAOSHA_QUEUE="miaosha_queue";
    public static  final String TOPIC_QUEUE1="topicqueue1";
    public static  final String TOPIC_QUEUE2="topicqueue2";
    public static  final String TOPIC_EXCHANGE="topicexchange";
    public static  final String FANOUT_EXCHANGE="fanoutchange";

    /**
     * direct模式 交换机 Exchange
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(MIAOSHA_QUEUE,true);
    }
    /**
     * Topic模式 交换机Exchange
     */
    @Bean
    public Queue Topicqueue1(){
        return new Queue(TOPIC_QUEUE1,true);
    }

    @Bean
    public Queue Topicqueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding TopicBinding1(){
        return BindingBuilder.bind(Topicqueue1()).to(topicExchange()).with("topic.key1");
    }

    @Bean
    public Binding TopicBinding2(){
        return BindingBuilder.bind(Topicqueue2()).to(topicExchange()).with("topic.#");
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding Fanoutbinding1(){
        return BindingBuilder.bind(Topicqueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding Fanoutbinding2(){
        return BindingBuilder.bind(Topicqueue2()).to(fanoutExchange());
    }
}
