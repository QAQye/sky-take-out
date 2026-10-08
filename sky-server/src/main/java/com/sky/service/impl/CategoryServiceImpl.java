package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.exception.DeletionNotAllowedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


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
       Page<Category> categories= categoryMapper.pageQuery(categoryPageQueryDTO);
       PageResult pageResult = new PageResult(categories.getTotal(),categories.getResult());
       return pageResult;
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category=new Category();
        category.setId(categoryDTO.getId());
        category.setType(categoryDTO.getType());
        category.setName(categoryDTO.getName());
        category.setSort(categoryDTO.getSort());
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.updateCategory(category);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
//        if (dishMapper.getByCategoryId(id)!=null){
//            Dish dish=new Dish();
//            dish.setStatus(status);
//            dish.setCategoryId(id);
//            dish.setUpdateTime(LocalDateTime.now());
//            dish.setUpdateUser(BaseContext.getCurrentId());
//            dishMapper.updateDishByCategoryId(dish);
//        }
//        dishMapper.startOrStop(status,id);
//        setmealMapper.startOrStop(status,id);
        Category category=new Category();
        category.setId(id);
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.updateCategory(category);
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setStatus(StatusConstant.ENABLE);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.addCategory(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (dishMapper.getByCategoryId(id)>0){
           throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        else if (setmealMapper.getByCategoryId(id)>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        else {
            categoryMapper.delete(id);
        }
    }

    @Override
    public List<Category> getCategoryList(Integer type) {
        List<Category> categories=categoryMapper.catgoryList(type);
        return categories;
    }
}
