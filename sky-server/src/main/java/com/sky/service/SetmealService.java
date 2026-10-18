package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    PageResult setmealQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void save(SetmealDTO setmealDTO);

    SetmealVO getByid(Long id);

    void updateSetmeal(SetmealDTO setmealDTO);

    void deleteBatch(List<Long> ids);


    void updateStatus(Integer status, Long id);
}
