package com.miaoshaproject.dao;

import com.miaoshaproject.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author cyx
 * @data 2019/3/14 23:25
 */
@Mapper
public interface UserDao {

    @Select("select * from user where id =#{id}")
    User getById(@Param("id") int id);

    @Insert("insert into user(id,name) values(#{id},#{name})")
    int  insertUser(User user);
}
