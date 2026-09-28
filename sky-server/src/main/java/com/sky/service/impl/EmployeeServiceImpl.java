package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeePageQueryVO;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
//        对前端传过来密码进行MD5加密后的进行比对
        password=DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

//    新增员工，这里需要调用我们的数据存储的持久层把数据传输过来
    @Override
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("当前线程的id是"+Thread.currentThread().getId());
        Employee employee=new Employee();
//        这里本来可以通过一个一个写来完成开发的需求,但是因为这两个对象的字段都是相同的,所以可以直接使用对象属性拷贝的方法进行
//        employee.setName(employeeDTO.getName());
//        从employeeDTO中的属性拷贝到employee中,前提是这个属性必须要是一致的
        BeanUtils.copyProperties(employeeDTO,employee);

        //这里因为有些属性中实体类中有但是我们传递过来的数据没有,所以需要做一个自己的设置
//        这里面中1表示可以使用0表示锁定
        employee.setStatus(StatusConstant.ENABLE);
        //设置密码,默认的密码为123456,密码在数据库中需要加密进行存储,这里使用了工具类对密码进行了贾母
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes(StandardCharsets.UTF_8)));
//        之后来设置创建时间以及更改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置这条记录的创建人id以及修改人id
        //TODO 后期需要改为当前登录用户的id（这里已经完成了开发）
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.insert(employee);


    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        System.out.println("进行用户的查询");
        //        从这里面得到当前的页数以及每一页的大小
//        如果不用这些库使用mysql框架那么需要使用limit关键字来实现，select * from employee limit 0,10
//        通过pagehelper可以简化分页代码的编写
//        这个的原理是会动态拼接后面的mysql语句
        //开始分页查询
        PageHelper.startPage(
                employeePageQueryDTO.getPage(),
                employeePageQueryDTO.getPageSize()
        );
// 调用 Mapper 查询员工数据
// 前提：查询前已经调用 PageHelper.startPage(...) 开启分页
// employees 中包含当前页的员工数据，以及符合条件的总记录数
        Page<Employee> employees = employeeMapper.querry(employeePageQueryDTO);

// 将当前页的 Employee 列表转换为 EmployeePageQueryVO 列表
        List<EmployeePageQueryVO> records = employees.getResult() // 获取当前页的员工列表
                .stream() // 将列表转成流，方便逐个转换员工对象
                .map(employee -> {
                    // map：把每个 Employee 转换成一个 EmployeePageQueryVO
                    // employee 表示当前正在处理的员工对象

                    // 为当前员工创建一个新的 VO，用于存放返回给前端的数据
                    EmployeePageQueryVO vo = new EmployeePageQueryVO();

                    // 将 employee 中名称相同、类型兼容的属性复制到 vo
                    // 第一个参数是源对象，第二个参数是目标对象
                    // VO 中没有的属性不会被复制，例如 VO 不定义 password 就不会复制密码
                    BeanUtils.copyProperties(employee, vo);

                    // 将 vo 作为当前员工的转换结果
                    return vo;
                })
                .collect(Collectors.toList()); // 将转换后的所有 VO 收集成 List，赋值给 records
        PageResult pageResult=new PageResult(employees.getTotal(),records);
        return pageResult;
    }

}
