# Day01苍穹外卖的概述

## 软件开发流程

### 需求分析

+ 需求规格说明书：常见为word文档，包括了导言、软件定义、应用环境、功能规格、性能需求等
+ 产品原型：相当于用静态网页的形式展现各个功能

### 设计

+ UI设计：用户界面的设计
+ 数据库设计：项目所使用的数据库结构、表的字段、结构、关系
+ 接口设计：基本信息、请求的参数、返回的数据

### 编码

+ 编写项目的代码
+ 单元测试

### 测试

+ 测试用例
+ 测试报告

### 上线运维

+ 软件环境的安装以及配置

## 苍穹外卖项目介绍

### 功能架构

主要分为两个端：管理端以及用户端

![image-20260916164127278](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260916164127278.png)

### 技术选型

主要分为四层：主要分为用户层、网关层、应用层、数据层

![image-20260916164741052](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260916164741052.png)

- **Nginx**: 一个高性能的 Web 服务器和反向代理服务器。它负责接收所有的外部请求，把前端的静态页面返回给用户，或者把请求转发给后面的真实后端服务，甚至还能做负载均衡。

+ **Spring Boot**: 整个后端的基础骨架。它极大地简化了 Spring 应用的初始搭建和开发过程，让你不用写一堆繁琐的配置文件。

+ **Spring MVC**: 负责处理 Web 请求的核心框架，处理 HTTP 请求并将结果返回给前端，比如提供 RESTful 接口。

+ **Spring Task**: Spring 自带的定时任务框架。比如项目中用来“定时取消超时未支付的订单”。

+ **HttpClient**: 一个用来发送 HTTP 请求的工具。后端在需要调用外部接口时会用到它，比如调用微信的登录、支付接口。

+ **Spring Cache**: Spring 提供的一套缓存抽象，搭配 Redis 使用，可以很方便地通过加注解的方式把数据存入缓存，加快访问速度。

+ **JWT (JSON Web Token)**: 身份令牌。用户登录后，后端发给前端一个 JWT，之后前端每次请求都带着它，后端借此来认出“你是谁”，进行权限验证。

+ **阿里云 OSS**: 对象存储服务。用来存大文件的，比如商家上传的菜品图片，存到 OSS 上既安全访问速度又快。

+ **Swagger**: 接口文档生成工具。它能根据你的代码自动生成漂亮的在线接口文档，方便前后端联调测试。

+ **POI**: Apache 提供的一个操作 Office 文档的库。在项目中通常用来实现“导出运营数据到 Excel 表格”的功能。

+ **WebSocket**: 实现服务器和客户端双向通信的技术。项目中用来做“来单提醒”和“客户催单”，只要有新订单，后端就能立刻主动推送到商家的页面上。
+ **MySQL**: 关系型数据库。用来存储项目里最核心的持久化数据，比如员工信息、菜品信息、订单记录等。
+ **Redis**: 内存数据库（缓存）。读写速度极快！用来存那些不需要永久保存但需要频繁读取的数据，比如店铺的营业状态、热门菜品缓存，以此减轻 MySQL 的压力。
+ **MyBatis**: 优秀的持久层框架。它帮你把 Java 对象和 MySQL 里的数据表连接起来，能更优雅地写 SQL 语句。
+ **PageHelper**: MyBatis 的一个分页插件。不用你手动去写复杂的 `LIMIT` 语句，调用它就能轻松实现数据分页展示。
+ **Spring Data Redis**: Spring 提供的用来操作 Redis 的工具库，让在 Java 中使用 Redis 变得更简单。

## Maven安装与配置

1.先从Download Apache Maven – Maven这个网页中获取maven的压缩包

2.配置本地仓库，修改conf/settings.xml中的mirrors，为其添加子标签

```
<localRepository>D:\maven\mvn-repo</localRepository>
```

3.配置阿里云私服，修改conf/settings.xml中的mirrors标签

```
<mirror>
    <id>alimaven</id>
    <name>aliyun maven</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public</url>;
    <mirrorof>central</mirrorof>
</mirror>
```

4.配置环境变量:在系统环境变量中加入maven项目目录环境变量

![image-20260916202810081](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260916202810081.png)

## 项目基本结构

苍穹外卖项目主要分为三个大模块：sky-common,sky-pojo,sky-server

### sky-common模块

这个模块主要是存放一些公共类，可以供给其他模块进行使用。常量类constant：鉴权时候使用的常量、公共字段自动填充常量、信息提示常量（用于对于不同状态中进行给用户的信息提示）、密码常量、状态常量，上下文类：主要用于线程，一个用户申请链接后就分配一个线程来进行处理，还有一些自定义异常、工具类、json处理类、所需要用到的属性、以及结果封装的格式

+ 注解**`@Component`：** 它的作用是将这个类交由 Spring 容器统一管理。当 Spring Boot 启动时，会自动扫描到这个注解，并在底层偷偷帮你 new 一个 `JwtProperties` 对象放入 Spring 的“对象池”中。这样在 `sky-server` 模块的其他业务代码里，你就可以直接使用 `@Autowired` 把它注入拿来用，而不需要每次都手动创建对象。
+ **`@ConfigurationProperties(prefix = "sky.jwt")`：** 它的作用是实现配置文件与 Java 类属性的批量绑定。`prefix = "sky.jwt"` 相当于下达了一个指令：“去 `sky-server` 模块下的 `application.yml` 文件中寻找，把所有以 `sky.jwt` 开头的配置项都找出来，并根据名字一一对应，赋值给当前类的成员变量”。  

### sky-pojo模块

实体类模块，主要用于数据的封装以及数据的传递，其中Entity(与数据库表对应的实体类)、DTO（前端传给后端的数据封装对象）和VO(后端返回给前端的视图对象)

| **对象类型** | **数据流向**  | **核心职责**                           | **字段特点**                                                 |
| ------------ | ------------- | -------------------------------------- | ------------------------------------------------------------ |
| **DTO**      | 前端 ➔ 后端   | 负责接收和封装前端传来的请求参数       | 根据前端接口传参需求定制，可能包含多张表的部分字段组合。     |
| **Entity**   | 后端 ⬌ 数据库 | 负责在代码和数据库之间映射完整数据     | 严格对应数据库表的各个字段，包含创建时间、修改人等底层信息。 |
| **VO**       | 后端 ➔ 前端   | 负责将处理后的数据组装并呈现给前端展示 | 根据前端页面的展示需求定制，通常会剔除密码、状态码等敏感或无用信息。 |

例如以登录的场景为例，当前端输入账号密码后点击登录，后端使用EmployeeLoginDTO来进行接受，接受到之后进行数据库查询，查出的数据封装为Employee，之后进行业务逻辑处理密码比对等工作，做完这些工作之后后端单独拼装一个EmployeeLoginVO之后返回给前端用于后续的身份的校验

在实际的代码运转中，标准的流程就是：前端传过来 **DTO**，后端提取数据转成 **Entity** 存进数据库；查询时，后端从数据库查出 **Entity**，再挑出需要的字段拼装成 **VO** 返回给前端。

为什么这些类需要实现Serializable接口？因为为了后续能够减轻mysql数据压力，在后续的开发中需要实现缓存菜品数据的功能因此需要实现这个接口否则在系统尝试像redis写入数据的时候就会抛出异常

## JWT概述

JWT（JSON Web Token）是一种用于在前后端之间安全传递信息的轻量级身份认证方案。

### JWT包括的部分

鉴权体系主要包括以下几个部分：

**颁发手环（登录阶段）：** 员工在 Vue 前端输入账号密码点击登录，后端 Spring Boot 去 MySQL 数据库核对。比对成功后，后端会调用工具类生成一个包含员工信息的 JWT 令牌，并返回给前端。  

**佩戴手环（请求阶段）：** 前端拿到这个 JWT 后会把它保存在本地。以后前端每次向后端发请求（比如新增菜品、查询订单），都会在 HTTP 请求头（Header）中把这个 JWT 携带上。

**验票放行（拦截阶段）：** 后端的拦截器就像大门口的保安。请求到达真实业务代码前，保安会先检查这个 JWT 是否过期、是否被伪造。如果没问题，保安就会从 JWT 里提取出员工 ID，并存入我们上一问提到的 `BaseContext` 线程池里，然后放行请求。

### JWT的内部构造

**Header（头部）：** 记录了令牌的类型和加密算法。

**Payload（载荷）：** 真正存放有效数据的地方。在苍穹外卖中，这里通常会存放员工的 ID（empId）。

**Signature（签名）：** 这是 JWT 防篡改的核心。后端会把前两部分内容，加上只有后端自己知道的 `SecretKey`（也就是你刚才在配置文件里绑定的 `admin-secret-key`），通过特定算法计算出一个签名。如果黑客偷偷把 Payload 里的员工 ID 改了，因为他不知道秘钥，算出来的签名就会和服务器对不上，拦截器会直接报错。

### 代码中如何实现JWT

以登录为例，当登录后在sky-server中核对账号和密码无误后就准备给前端发放通行证，此时后端会调用``Jwt-Util``工具类将刚刚在配置文件中绑定的密钥``adminSecretKey``、过期时间``adminTtl``、以及查出来的员工id混合在一起，通过加密算法生成遗传jwt，最后这个字符串会封装进EmployeeLoginVO中返回给前端页面。

![image-20260917225330716](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260917225330716.png)

前端拿到 JWT 后会保存在本地浏览器，之后不管点击什么菜单（比如新增菜品、查询订单），都会在 HTTP 请求头（Header）里带上这个令牌。在 `sky-server` 模块里，有一个专门的类叫 `JwtTokenAdminInterceptor`。它就像门口的保安，会拦下所有业务请求。保安会从请求头里抠出这串 JWT，掏出后端独有的秘钥尝试解密。如果解密失败或令牌过期，保安直接抛出异常（提示未登录）；如果解密成功，保安就能从里面读取出当初存进去的员工 ID。第一个代码的意思是在过滤handler，相当于静态的请求会直接进行放行，在动态请求中就需要校验token令牌，校验的过程主要是写在公共类中的方法来进行创建以及解码

![image-20260917225915029](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260917225915029.png)

## MD5加密

使用Java中自带的方法可以对密码进行MD5进行加密

```
DigestUtils.md5DigestAsHex(password.getBytes());
```

进入项目的地址：

[工作台](http://localhost/#/dashboard)

## nginx转发

### nginx引言

在前端中登录发送请求的地址为，这里我们可以看到这里的前端的请求地址和我们后端接受的地址并不相同：

![image-20260921164010844](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260921164010844.png)

![image-20260921164128886](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260921164128886.png)

这个是因为nginx做了反向代理，将前端发送过俩的动态请求由nginx转发到后端服务器中，这个还可以作为反向代理服务器

为什么不直接发请求呢？主要是可以提高访问速度，这里可以做缓存，可以将缓存数据给前端。统统是也可以做负载均衡，可以作为一个负载均衡器将大量的请求按照指定的方式均衡的分配给不同的服务器

### nginx反向代理配置方式

+ 第一行需要填写监听的端口号

location /api/这里意思是来处理特定的请求，当有这个/api请求的时候就会进行处理，主要是通过proxy_pass来把请求进行转发，转发到后端的地址

比如上图中前端发送的请求为/api/employee/login，当这个请求过来之后nginx就会将这个请求进行转发，转发到后端对应的地址localhost:8080/admin这个请求里面来，之后动态的路径也会进行拼接到后面employee/login，这个就是到登录接口的后端

![image-20260921183311209](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260921183311209.png)

### nginx负载均衡的配置

这个主要是通过upstream后端的服务以及端口号，必须这里申请了两个服务器，如果使用了这个那么就会将这个请求就转发到堕胎服务器中，负载均衡的测率也有很多，这里使用的是默认的策略就是轮询策略

![image-20260921184112686](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260921184112686.png)

## Swagger导入接口文档

使用方式：

+ 导入knife4j的maven坐标

![image-20260921215443579](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260921215443579.png)

+ 配置类中加入knife4j相关的配置,这里加入了bean注解相当于用sping框架来创建这个对象以及管理这个对象，之后要注意要指定生成接口需要扫描的包

![image-20260921215517365](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260921215517365.png)

+ 设置静态资源映射，否则接口文档页面无法访问，需要做静态资源的映射，如果不做这些映射就请求不到这些文件中

```
protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
```

### 常用注解

使用这些常用的注解可以让文档更具有可读性

![image-20260921220429812](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260921220429812.png)

## 配置时候所遇到的问题

+ jdk版本和lombok版本不匹配，需要在这里修改lombok版本

![image-20260916203003875](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260916203003875.png)

+ 修改项目的jdk版本，不要在单独某一个模块中修改jdk版本这样没用，要在项目中修改全局的jdk版本（文件->项目结构）

![image-20260916203139011](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260916203139011.png)

+ 数据库连接时报错，是因为忘记修改数据库配置中的用户名以及密码，文件放在server的目录文件的resource资源文件夹里面，这里面定义了外部的配置信息比如mysql、redis

![image-20260916203907986](C:\Users\24861\AppData\Roaming\Typora\typora-user-images\image-20260916203907986.png)