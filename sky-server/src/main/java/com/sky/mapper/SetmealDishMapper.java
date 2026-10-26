package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    List<Long> getByDishId(List<Long> ids);
    void saveBatch(List<SetmealDish> setmealDishes);

    List<SetmealDish> getBySetmealId(@Param("setmealId") Long id);
    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void deleteBySetmealId(Long setmealId);
    @Select("select count(*) from setmeal_dish as sd LEFT JOIN dish d on d.id = sd.dish_id where d.status=0")
    Long selectBySetmealWithDish(Long id);

}