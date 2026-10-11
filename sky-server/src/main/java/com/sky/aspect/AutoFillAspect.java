package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
//    切入点这一行相当于拦截mapper下面所有的方法，但是实际情况我们并不需要拦截mapper下所有的方法，所以只是拦截在加入@annotation注解的类
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}
//    这个相当于一个前置通知，需要指定一个切入点，会执行切入点的表达式匹配
    @Before("autoFillPointCut()")
//    之后传入一个连接点，链接点知道当前拦截的方法是怎么样的，要在sql执行之前对其进行赋值不能执行完之后才赋值
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的填充.....");
        //首先需要获取到拦截的方法上当前数据库操作类型
//        获取方法签名对象
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType= autoFill.value();//数据库的操作类型

        //获取到当前拦截的实体对象
//        要实现自动填充要将实体对象放在第一个就可以了，获取当前注解所在方法的所有参数
        Object[] args=joinPoint.getArgs();
//        如果这里面没有参数的话那么就直接返回即可
        if (args==null||args.length==0){
            return;
        }
//        获取相应的实体对象
        Object entity=args[0];

        //准备赋值数据
        LocalDateTime now=LocalDateTime.now();
        Long currentId= BaseContext.getCurrentId();

        //根据当前不同的操作类型为对应的属性通过反射来赋值
        if (operationType==OperationType.INSERT){
            //为四个公共字段进行赋值，使用反射的方式来
            try {
                Method setCreateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setCreateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }else if(operationType==operationType.UPDATE){
            try {
                Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
