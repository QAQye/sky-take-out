package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sky.exception.DeletionNotAllowedException;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
//    加入事务注解，保证数据的一致性，保证方法是个原子性的
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //向菜品表插入数据，每次插入一条
        //向口味表插入n条数据
        Dish dish=new Dish();
//        将dishdto的属性拷贝到dish中来
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setStatus(StatusConstant.DISABLE);
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

    @Override
    @Transactional
    public void deleteDish(List<Long> ids) {
        //判断是否能够被删除是否存在起售中的菜品
        if(dishMapper.getById(ids)>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
//            是否被套餐关联
        List<Long>setmealDishIds = setmealDishMapper.getByDishId(ids);
        if (setmealDishIds.size()>0&&setmealDishIds!=null){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
//        删除菜品数据
        dishMapper.deleteByID(ids);
//        删除口味数据
        dishFlavorMapper.deleteByBatchDishID(ids);
    }

    @Override
    public DishVO showDish(Long id) {
        DishVO dishVO= dishMapper.showDish(id);
        List <DishFlavor> dishFlavors=dishFlavorMapper.getByDishId(id);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);
        //    实现方式：先删除原来有的口味信息，然后再重新插入口味数据
        dishFlavorMapper.deleteByDishID(dishDTO.getId());
        List<DishFlavor> dishFlavors=dishDTO.getFlavors();
        if(dishFlavors!=null&&dishFlavors.size()>0){
            dishFlavors.forEach(dishFlavor->dishFlavor.setDishId(dishDTO.getId()));
//            向口味表中插入n条数据
//            采用批量插入
            dishFlavorMapper.insertBatch(dishFlavors);
        }
    }

    @Override
    @Transactional
    public void startOrStop(Long id, Integer status) {
        Dish dish = Dish.builder().id(id).status(status).build();
//        dish.setId(id);
//        dish.setStatus(status);
        dishMapper.updateDish(dish);
//        菜品如果停售了那么相应的套餐也要停售
        if (status==StatusConstant.DISABLE){
            List<Long> ids=new ArrayList<>();
            ids.add(id);
//            List<SetmealDishMapper> setmealDishMappers= setmealDishMapper.getByDishId(ids);
            List<Long> setmealIds=setmealDishMapper.getByDishId(ids);
            setmealIds.forEach(e->{
                Setmeal s=Setmeal.builder().id(e).status(StatusConstant.DISABLE).build();
                setmealMapper.update(s);
            });
        }

    }

//    这样来写dish的实现可以让其更好的适配可以根据多个公共字段进行查询
    @Override
    public List<Dish> getByCategory(Long id) {
        Dish dish = Dish.builder()
                .categoryId(id)
                .status(StatusConstant.ENABLE)
                .build();
        List<Dish> dishes= dishMapper.list(dish);
        return dishes;
    }
}
