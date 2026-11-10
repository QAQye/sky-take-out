package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    @Transactional
    public void add(ShoppingCartDTO shoppingCartDTO) {
//        首先处理逻辑：相同的商品只用给数量加一即可，所以在添加前先判断是否商品是否存在，并且不同用户由不同的购物车
//        如果不存在的情况才将当前的数据存入数据库中
        ShoppingCart shoppingCart =new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
//        在JWT中已经做了threadlocal将userId存储在threadlocal中
        Long userId= BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
//        说明在这里已经查询到了对饮改的数据，直接对数量进行修改即可
        if (list !=null&&list.size()>0){
//            因为查询只可能查询到唯一的数据
            ShoppingCart cart = list.get(0);
            Integer number = cart.getNumber()+1;
            cart.setNumber(number);
            shoppingCartMapper.updateNumber(cart);
        }
//        如果不存在那么就插入一条数据
        else{
//            判断本次添加到购物车的菜品还是套餐
            if (shoppingCartDTO.getSetmealId()!=null){
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }
            //本次添加来的是套餐
            else {
                Dish dish =dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.add(shoppingCart);
        }

    }
}
