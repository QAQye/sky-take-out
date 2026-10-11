package com.sky.mapper;
import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
    @Select("select count(id) from dish where  category_id= #{categoryId}")
    Integer getByCategoryId(long categoryId);
    @AutoFill(OperationType.INSERT)
//    插入菜品数据
    void insert(Dish dish);

//    void updateDishByCategoryId(Dish dish);
}
