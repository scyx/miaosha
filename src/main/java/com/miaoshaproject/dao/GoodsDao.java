package com.miaoshaproject.dao;

import com.miaoshaproject.Vo.GoodsVo;
import com.miaoshaproject.domain.Goods;
import com.miaoshaproject.domain.MiaoshaGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author cyx
 * @data 2019/3/14 23:25
 */
@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock_count,start_date,end_date,miaosha_price" +
            " from miaosha_goods mg " +
            "left join goods g on mg.goods_id=g.id")
    List<GoodsVo> getgoodsVoList();

    @Select("select g.*,mg.stock_count,start_date,end_date,miaosha_price" +
            " from miaosha_goods mg " +
            "left join goods g on mg.goods_id=g.id where g.id=#{goodsId}")
    GoodsVo getgoodsVoByGoodsId(@Param("goodsId")long goodsId);

    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId} and stock_count>0")
    long reduceStock(MiaoshaGoods g);
}
