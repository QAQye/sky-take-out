package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
    @AutoFill(value = OperationType.UPDATE)
    void updateCategory(Category category);
    @Insert("insert into category(name,type,sort,status,create_time,update_time,create_user,update_user) " +
            "value " +
            "(#{name},#{type},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void addCategory(Category category);
    @Delete("delete from category where id=#{id}")
    void delete(Long id);
    @Select("select * from category where type=#{type}")
    List<Category> catgoryList(Integer type);
}
