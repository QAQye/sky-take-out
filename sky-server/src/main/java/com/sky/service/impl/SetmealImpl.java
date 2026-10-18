package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.save(setmeal);
        List <SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
        setmealDishes.forEach(item->item.setSetmealId(setmeal.getId()));
        setmealDishMapper.saveBatch(setmealDishes);
    }

    @Override
    public SetmealVO getByid(Long id) {
        Setmeal setmeal= setmealMapper.getById(id);
        List<SetmealDish> setmealDishes= setmealDishMapper.getBySetmealId(id);
        SetmealVO setmealVO =new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }
    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
//        可以采用先删除后新增的方式来进行对于这个列表的修改
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        setmealDishes.forEach(e->e.setSetmealId(setmealDTO.getId()));
        setmealDishMapper.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        if (setmealMapper.getByIdWithStatus(ids) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        setmealMapper.deleteBatch(ids);
        ids.forEach(e->setmealDishMapper.deleteBySetmealId(e));
    }


    @Override
    public void updateStatus(Integer status, Long id) {
        if (status==0&&setmealDishMapper.selectBySetmealWithDish(id)>StatusConstant.ENABLE){
            throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
        }
        Setmeal setmeal =new Setmeal().builder().status(status).id(id).build();
        setmealMapper.update(setmeal);
    }
}
