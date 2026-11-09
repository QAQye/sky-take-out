package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@Api(tags= "用户分类相关接口")
@Slf4j
@RequestMapping("/user")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/category/list")
    public Result<List<Category>> queryCatgory(@RequestParam(value = "type", required = false) Integer type){
        List<Category> categories=categoryService.list(type);
        return Result.success(categories);
    }
}
