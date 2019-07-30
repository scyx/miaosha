package com.miaoshaproject.service;

import com.miaoshaproject.Vo.GoodsVo;
import com.miaoshaproject.dao.GoodsDao;
import com.miaoshaproject.domain.Goods;
import com.miaoshaproject.domain.MiaoshaGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cyx
 * @data 2019/4/3 15:37
 */
@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoods(){
        return goodsDao.getgoodsVoList();
    }

    public GoodsVo getGoodsVoBygoodsId(long goodsId) {
        return goodsDao.getgoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g=new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        long re = goodsDao.reduceStock(g);
        return re>0;
    }
}
