package com.miaoshaproject.controller;

import com.miaoshaproject.Vo.GoodsVo;
import com.miaoshaproject.Vo.OrderDetailVo;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.domain.OrderInfo;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.GoodsService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cyx
 * @data 2019/5/4 20:46
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;


    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model,
                                      MiaoshaUser user,
                                      @RequestParam("orderId")long orderId){
        log.info("orderId:"+orderId);
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order=orderService.getOrderById(orderId);
        long goodsId=order.getGoodsId();
        GoodsVo goods=goodsService.getGoodsVoBygoodsId(goodsId);
        OrderDetailVo vo=new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
