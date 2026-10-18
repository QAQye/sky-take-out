package com.sky.controller.admin;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.utils.AliOssUtil;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("{id}")
    @ApiOperation(value = "根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable ("id") Long id){
        SetmealVO setmealVO=setmealService.getByid(id);
        return Result.success(setmealVO);
    }
    @PutMapping
    @ApiOperation(value = "修改套餐")
    public Result  updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }
    @DeleteMapping
    @ApiOperation(value = "删除套餐")
    public Result deleteSetmeal(@RequestParam("ids") List<Long> ids){
        setmealService.deleteBatch(ids);
        return Result.success();
    }
    @PostMapping("/status/{status}")
    @ApiOperation(value = "修改套餐状态")
    public Result updateStatus(@PathVariable("status") Integer status,Long id){
        setmealService.updateStatus(status,id);
        return Result.success();
    }
}
