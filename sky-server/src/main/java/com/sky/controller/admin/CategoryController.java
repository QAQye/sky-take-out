package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类管理相关的接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/page")
    @ApiOperation(value = "分页查询分类信息" )
    public Result<PageResult> querryCategory(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("查询分类信息{}",categoryPageQueryDTO.getName());
        PageResult pageResult=categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }
    @ApiOperation(value = "修改分类信息")
    @PutMapping
    public  Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类信息{}",categoryDTO.getName());
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }
}
