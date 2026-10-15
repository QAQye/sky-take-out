package com.sky.service.impl;

import com.aliyun.oss.internal.OSSUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class SetmealImpl implements SetmealService {
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Override
    public PageResult setmealQuery(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmeals= setmealMapper.pageQuery(setmealPageQueryDTO);
//        返回page结果的总页数以及返会对应的结果数组
        return new PageResult(setmeals.getTotal(),setmeals.getResult());
    }

    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal =new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.save(setmeal);
        List <SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
        setmealDishes.forEach(item->item.setSetmealId(setmeal.getId()));
        setmealDishMapper.save(setmealDishes);
    }
}
