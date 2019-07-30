package com.miaoshaproject.controller;

import com.miaoshaproject.Vo.GoodsVo;
import com.miaoshaproject.domain.Goods;
import com.miaoshaproject.domain.MiaoshaOrder;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.domain.OrderInfo;
import com.miaoshaproject.rabbitmq.MQsender;
import com.miaoshaproject.rabbitmq.MiaoshaMessage;
import com.miaoshaproject.redis.GoodsKey;
import com.miaoshaproject.redis.MiaoshaUserKey;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author cyx
 * @data 2019/3/15 13:25
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
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
    @Autowired
    MQsender mQsender;

    private static Logger log = LoggerFactory.getLogger(MiaoshaController.class);

    HashMap<Long,Boolean> LocalOverhashmap = new HashMap<>() ;

    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doMiaosha(Model model,
                                     MiaoshaUser user,
                         @RequestParam("goodsId")long goodsId){
        if(user==null){
           return Result.error(CodeMsg.SESSION_ERROR);
        }
        if(LocalOverhashmap.get(goodsId)==true){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //判断是否已经秒杀到了
        MiaoshaOrder miaoshaOrder=orderService.getOrderByGoodsIdUserId(user.getId(),goodsId);
        if(miaoshaOrder!=null){
            log.info(""+miaoshaOrder.getOrderId());
            return Result.error(CodeMsg.REAPEAT_MIAOSHA);
        }

        //预减库存
        long stock = redisService.get(GoodsKey.getgoodsStock,""+goodsId,long.class);
        if(stock < 0){
            LocalOverhashmap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        redisService.decr(GoodsKey.getgoodsStock,""+goodsId);
        //判断库存
//        GoodsVo goods=goodsService.getGoodsVoBygoodsId(goodsId);
////        int stock = goods.getStockCount();
////        if(stock<=0){
////            return Result.error(CodeMsg.MIAO_SHA_OVER);
////        }

        //减库存 生成订单 下订单
//        OrderInfo orderInfo= miaoshaService.miaosha(user,goods);
//        log.info("秒杀成功了");
//        return Result.success(orderInfo);
        //入队
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setGoodsId(goodsId);
        miaoshaMessage.setMiaoshaUser(user);
        mQsender.sendMiaoshaMessage(miaoshaMessage);
        return Result.success(0);
    }

    /**
     * 加载商品数量到redis中，减少数据库访问
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goods= goodsService.listGoods();
        if(goods==null){
            return;
        }
        for(GoodsVo g:goods){
            redisService.set(GoodsKey.getgoodsStock,""+g.getId(),g.getStockCount());
            LocalOverhashmap.put(g.getId(),false);
        }
    }
    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId) {
        log.info("goodsid:"+goodsId);
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
        log.info("result:"+result);
        return Result.success(result);
    }
}
