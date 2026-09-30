package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
//    因为在配置文件中开启了驼峰命名法所以在这里也能够自动进行转换
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);
    @Insert("insert into employee(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values " +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    public void insert(Employee employee);
//    因为这个地方我们需要使用到动态sql，动态sql使用注解方式编译不是很方便，所以就写一下映射文件中
    public Page<Employee> querry(EmployeePageQueryDTO employeePageQueryDTO);
//    虽然说这么写sql没有错误，但是为了之后更有通用性，所以可以修改这个写法
//    @Update("update employee SET status=#{status} WHERE id=#{id}")
    void updateEmployee(Employee e);
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
