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
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/status/{status}")
    @ApiOperation(value = "修改分类的状态")
    public Result startOrStop(@PathVariable("status") Integer status,@RequestParam("id") Long id){
        log.info("修改分类状态{}",id);
        categoryService.startOrStop(status,id);
        return  Result.success();

    }
    @PostMapping
    @ApiOperation(value = "新增分类")
    public Result addCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("增加分类{}");
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }
    @DeleteMapping
    @ApiOperation(value = "删除分类")
    public Result deleteCategory(@RequestParam("id") Long id){
        log.info("删除分类{}",id);
        categoryService.deleteCategory(id);
        return Result.success();
    }
    @GetMapping("/list")
    @ApiOperation(value = "获取分类列表")
    public Result<List<Category>> getCategoryList(@RequestParam("type") Integer type){
        log.info("获取分类列表");
        List<Category> categories =categoryService.getCategoryList(type);
        return Result.success(categories);
    }

}
