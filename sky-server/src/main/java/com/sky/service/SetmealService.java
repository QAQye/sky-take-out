package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

public interface SetmealService {
    PageResult setmealQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void save(SetmealDTO setmealDTO);
}
