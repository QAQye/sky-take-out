package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐的接口")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @GetMapping("/page")
    @ApiOperation(value = "套餐分页查询" )
    public Result<PageResult> setmealQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult =setmealService.setmealQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    @PostMapping
    @ApiOperation(value = "新增套餐" )
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.save(setmealDTO);
        return Result.success();
    }
}
