package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    //动态sql
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number=#{number} where id =#{id}")
    void updateNumber(ShoppingCart cart);

    void add(ShoppingCart shoppingCart);
    @Select("select * from shopping_cart where user_id=#{currentId}")
    List <ShoppingCart> getByUserId(Long currentId);
    @Delete("delete from shopping_cart where user_id=#{currentId}")
    void clean(Long currentId);

    void delete(ShoppingCart shoppingCart);
}
