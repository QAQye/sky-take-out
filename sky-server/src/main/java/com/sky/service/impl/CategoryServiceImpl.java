package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.EmployeeMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        System.out.println("进行分类分页查询");
        PageHelper.startPage(
                categoryPageQueryDTO.getPage(),
                categoryPageQueryDTO.getPageSize()
        );
       Page<Category> categories= categoryMapper.pageQuery(categoryPageQueryDTO).setOrderBy("sort");
       PageResult pageResult = new PageResult(categories.getTotal(),categories.getResult());
       return pageResult;
    }
}
