package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;


    @Override
//    加入事务注解，保证数据的一致性，保证方法是个原子性的
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //向菜品表插入数据，每次插入一条
        //向口味表插入n条数据
        Dish dish=new Dish();
//        将dishdto的属性拷贝到dish中来
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        //获取insert生成的属性值
        Long dishId = dish.getId();
        List<DishFlavor> dishFlavors=dishDTO.getFlavors();
        if(dishFlavors!=null&&dishFlavors.size()>0){
            dishFlavors.forEach(dishFlavor->dishFlavor.setDishId(dishId));
//            向口味表中插入n条数据
//            采用批量插入
            dishFlavorMapper.insertBatch(dishFlavors);
        }
    }

    @Override
    public PageResult querryDish(DishPageQueryDTO dishPageQueryDTO) {
//        获取当前页以及每页多少个数据
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
//        之后开始来写数据库查询语句
        Page<DishVO> dishes=dishMapper.querryDish(dishPageQueryDTO);
        return new PageResult(dishes.getTotal(),dishes.getResult());
    }
}
