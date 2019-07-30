package com.miaoshaproject.controller;

import com.miaoshaproject.rabbitmq.MQsender;
import com.miaoshaproject.result.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cyx
 * @data 2019/7/23 13:22
 */
@Controller
public class SampleController {

//    @Autowired
//    MQsender mQsender;
//
//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq(){
//        mQsender.send("hello,world");
//        return Result.success("helloworld");
//    }
}
