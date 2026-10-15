package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult querryDish(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(List<Long> ids);

    DishVO showDish(Long id);

    void updateDish(DishDTO dishDTO);

    void startOrStop(Long id, Integer status);

    List<Dish> getByCategory(Long id);
}
