package com.miaoshaproject.dao;

import com.miaoshaproject.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author cyx
 * @data 2019/3/15 14:34
 */
@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaoshauser where id=#{id}")
    MiaoshaUser getById(@Param("id")long id);

    @Insert("insert into miaoshauser(id,nickname,password,salt,register_date,login_count) " +
            "values(#{id},#{nickname},#{password},#{salt},#{registerDate},#{loginCount})")
    int insertUser(MiaoshaUser newuser);
}
