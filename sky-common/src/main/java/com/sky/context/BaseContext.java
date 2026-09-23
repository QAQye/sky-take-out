package com.sky.context;

public class BaseContext {

//    这里创建了一个ThreadLocal对象，只能在线程内部访问这一片存储空间，在线程外部是不能访问到这一片存储空间
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
//    在这个线程的存储空间内存储id的值
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
//    在这个线程空间中获取这个id的值
    public static Long getCurrentId() {
        return threadLocal.get();
    }
//    在这个线程的空间中移除销毁id的值
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
