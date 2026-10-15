package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理相关的接口")
public class DishController {
    @Autowired
    DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> querryDish(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品{}",dishPageQueryDTO);
        PageResult pageResult =dishService.querryDish(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    @DeleteMapping
    @ApiOperation("批量删除菜品")
//    对于springboot中可以自动解析封装为一个list集合，加入一个注解既可以启用
    public Result deleteDishes(@RequestParam List<Long> ids){
        log.info("删除菜品{}",ids);
        dishService.deleteDish(ids);
        return Result.success();
    }
    @ApiOperation("修改菜品回显")
    @GetMapping("/{id}")
    public Result<DishVO> showDish(@PathVariable ("id") Long id){
        log.info("修改菜品回显功能");
        DishVO dishVO= dishService.showDish(id);
        return Result.success(dishVO);
    }
    @ApiOperation("修改菜品")
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品");
        dishService.updateDish(dishDTO);
        return Result.success();
    }
    @PostMapping("status/{status}")
    @ApiOperation("修改菜品状态")
    public Result startOrStop(@PathVariable("status") Integer status,  @RequestParam("id") Long id){
        log.info("修改菜品状态");
        dishService.startOrStop(id,status);
        return Result.success();
    }
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    //这里的参数必须要匹配否则不能传递进来数据
    public Result<List<Dish>> getCategoryDish( @RequestParam("categoryId") Long categoryId){
        log.info("根据分类id来进行查询菜品");
        List<Dish> dishes=dishService.getByCategory(categoryId);
        return Result.success(dishes);
    }
}
