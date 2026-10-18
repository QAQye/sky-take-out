package com.sky.mapper;
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
    @Select("select count(id) from dish where  category_id= #{categoryId}")
    Integer getByCategoryId(long categoryId);
    @AutoFill(OperationType.INSERT)
//    插入菜品数据
    void insert(Dish dish);

    Page<DishVO> querryDish(DishPageQueryDTO dishPageQueryDTO);

    Long getById(List<Long> ids);

    void deleteByID(List<Long> ids);

    DishVO showDish(Long id);
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dishDTO);

    List<Dish> list(Dish categoryId);
//    void updateDishByCategoryId(Dish dish);

}
