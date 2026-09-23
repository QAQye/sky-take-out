# DAY 02 新增员工

## 新增员工需求分析和设计

添加员工需要的工作：

+ 员工账号（唯一）
+ 员工姓名
+ 手机号
+ 性别，进行选择男和女选择
+ 身份证号（需要校验合法的18位）

然后员工增加后默认密码都是123456，后续需要员工自己对密码进行修改

因为提交的数据为表单数据，提交表单数据使用post来进行提交数据，然后使用json来进行传递

项目约定：因为项目分为了两个端一个是admin端一个是user端，因此如果是管理端发来的请求都是用/admin作为前缀，如果是用户端发来的请求那么统一均使用/user来作为前缀

接口设计：

+ 路径：/admin/employee
+ Method:POST
+ 接口描述：用于新增用户的表单

+ 请求参数：
  + 头部：使用json格式来进行传递
  + body部分：
    + 用户id
    + 用户账号
    + 用户名
    + 手机号
    + 性别
    + 身份证号
+ 返回数据类型：
  + 状态码（必须）
  + 数据部分（非必须如果是新增那么就是非必须，如果是其他方面那么可能是必须字段）
  + 返回的信息（非必须）

![image-20260921222642569](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260921222642569.png)

## 数据库设计

这一次操作的数据表主要是员工表，具体的员工表信息是：

| 字段名      | 数据类型    | 说明         | 备注        |
| ----------- | ----------- | ------------ | ----------- |
| id          | bigint      | 主键         | 自增        |
| name        | varchar(32) | 姓名         |             |
| username    | varchar(32) | 用户名       | 唯一        |
| password    | varchar(64) | 密码         |             |
| phone       | varchar(11) | 手机号       |             |
| sex         | varchar(2)  | 性别         |             |
| id_number   | varchar(18) | 身份证号     |             |
| status      | int         | 账号状态     | 1正常 0锁定 |
| create_time | datetime    | 创建时间     |             |
| update_time | datetime    | 最后修改时间 |             |
| create_user | bigint      | 创建人id     |             |
| update_user | bigint      | 最后修改人id |             |

## 代码开发

### 设计对应的DTO

为什么要设置DTO而不是直接用对应的实体类属性进行开发呢？因为在我们设计的实体类中除了要封装要传过来的数据之外还需要自己额外的属性，比如账号状态，账号创建的时间，最后修改时间，修改人id，创建人id等等，这里建议使用DTO来进行封装数据，这样就可以精确的来封装，前端提交了对应的属性那我就对应起来来封装，如果没有那么就不封装，因此使用的Emplyee来进行属性的封装

### 接口文档测试

通过生成的swagger文档可以对对应的接口进行一个测试

http://localhost:8080/doc.html

在开发好这个新增用户接口后我们进行在swaager里面的测试

![image-20260922212011618](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922212011618.png)

之后发现报错的信息为401，这个意思是因为拦截器将这个请求给拦截了，因此在接口文档中可以统一的获取一个令牌之后就通过这个令牌来进行发消息

因此这里的解决方案是我们需要获取一个全局的token需要第一个用户先登录一次

![image-20260922213003817](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922213003817.png)

eyJhbGciOiJIUzI1NiJ9.eyJlbXBJZCI6MSwiZXhwIjoxNzkwMDkwOTY4fQ.VIpXTWfoCfn7yUp2YakmcZlV0vCpuWbAqSWvlG41uFg

之后在全局参数设计中加入这个参数：

![image-20260922213127829](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922213127829.png)



![image-20260922213245986](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922213245986.png)

### 设置全局的异常处理器用于捕获数据库中唯一字段的异常情况

在全局异常类中可以进行异常处理器的配置

![image-20260922214328032](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922214328032.png)

之后在控制台上找到报错信息以及报错异常的类别，之后在全局异常处理器中就可以将这个类别进行捕获

![image-20260922214636442](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922214636442.png)

在报错的信息中提取出来关键词，让这个异常处理器能够精确的定位到相应的错误信息，比如这里的错误信息是用户名已经存在，这里就可以用contains关键字来判断是否捕获到的信息有包含这一个报错信息，之后就对相应的报错信息进行处理

![image-20260922214920042](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922214920042.png)

这里想要动态的获取到某个用户名已经存在的信息，因此就需要判断它所在的位置，这里所在的位置是在第三个词，然后我们可以先将空格将其分割开后获取到第三个部分的内容

![image-20260922215426067](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922215426067.png)

在完成这些步骤后就可以对相应的异常信息进行捕获和显示相应的错误

![image-20260922215726821](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922215726821.png)

## 新增员工的时候能够动态获取当前创建或者修改用户的id

主要流程：前端完成用户认证后将用户名和密码传给后端，后端进行校验，当后端校验通过之后会生成JWT令牌，之后将生成的JWTToken返回给前端，之后在本地中保存JWT Token，之后前端在每一次请求后端接口的时候都需要在请求头中携带JWT Token，之后后端进行拦截这个token来进行验证，如果通过就可以继续执行业务逻辑，返回相应的数据给前端，如果不通过那么就会返回报错的信息

![image-20260922220005671](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922220005671.png)

由于在登录的时候已经将用户id其实封装进了我们的token中，因此如果想要解析也可以进行反向解析将这个用户id从这个token中解析出来

在登录时，我们生成JWT令牌的代码如下：

```java
Map<String, Object> claims = new HashMap<>();
claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
String token = JwtUtil.createJWT(
        jwtProperties.getAdminSecretKey(),
        jwtProperties.getAdminTtl(),
        claims);
```

在这个代码中我们生成令牌的时候需要传进一个map对象，在这个对象中已经包括了用户的id，在接下来我们也可以在这个jwt中解析出我们相应的用户id

```Java
        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工id：", empId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
```

在这个代码中我们现需要从头部获取出令牌，之后使用我们自己编写的工具方法对token进行解析，得到解析后的token后，我们得到一个键值对，之后通过键值对中的键来获取当前用户的id并将其转化为字符串的形式

因此这里有一个新的技术点：在解析出来员工的id后如何将其转递给service的save方法呢？

### ThreadLocal技术

这个并不是一个线程，但是他是一个线程的局部变量，ThreadLocal为每个线程提供一份单独的存储空间，具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外不能获取相应的值

我们在这里在controller类里，Jwt校验类里，以及eployeeserver类中save函数里都加入了打印线程的代码，发现在一个请求中是由有一个线程来进行处理的，之后我重新发送请求，在这里可以看见客户端每一次发送请求都由不同的线程来处理，一个请求由一个线程处理

![image-20260922223343138](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922223343138.png)

![image-20260922223619086](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922223619086.png)

因此如果我们要实现动态的获取当前用户的id，所以可以在拦截器中将这个用户id单独的存在ThreadLocal存储器中，之后执行到要保存当前用户是谁的时候就可以将保存的id放在相应的位置取出来。

![image-20260922223856424](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260922223856424.png)

如果想要在ThreadLocal中进行存储多个字段，那么这里由两个方法可以进行存储

**方案一：封装统一的上下文对象（企业级推荐做法）** 与其频繁创建多个 `ThreadLocal`，更优雅的做法是先定一个包含所有需要传递字段的实体类，然后让 `ThreadLocal` 专门存放这个大对象。这也是绝大多数开源项目和企业开发的首选。

```
// 1. 先定义一个你需要存储的各种信息的实体类
public class UserInfo {
    private Long id;
    private String username;
    private String role;
    // ... 省略 getter/setter/构造方法
}

// 2. 将 ThreadLocal 的泛型改成这个实体类
public class BaseContext {
    public static ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();

    public static void setUserInfo(UserInfo userInfo) {
        threadLocal.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return threadLocal.get();
    }

    public static void removeUserInfo() {
        threadLocal.remove();
    }
}
```

**方案二：创建多个 ThreadLocal 对象（适用于少量独立字段）** 如果你觉得专门写个类太麻烦，且只是临时多加一两个字段（比如只加个 `token`），直接在原类中声明多个 `ThreadLocal` 也是完全合法的。  

```
public class BaseContext {
    public static ThreadLocal<Long> idThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    // 分别编写 set/get/remove 方法...
}
```

## 所遇到的问题

### PostMapping以及RequestMapping的区别

**@RequestMapping("/admin/employee")（定义公共前缀）：** 作用在类级别上，相当于给这个 Controller 里的所有方法设置了一个基础的 URL 目录。它告诉 Spring 框架：“只要请求路径是以 `/admin/employee` 开头的，都统统交给我这个类来处理”。

**@PostMapping("/logout")（定义具体路径与请求方式）：** 作用在具体的方法级别上，它做了两件事：

1. **路径拼接**：将 `/logout` 追加到类的公共前缀之后，得出该方法的完整访问路径为 `/admin/employee/logout`。
2. **限制请求类型**：明确规定该接口只接收 **POST** 类型的 HTTP 请求。如果前端用 GET 或 PUT 等其他方式访问这个路径，系统会直接报错提示不支持。



