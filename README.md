# springboot初始化模版

> 基于Spring Boot 3.1.2，整合主流框架，快速开发企业级后端项目
>
> 后端接口文档： http://localhost:8081/doc.html#/home
>
> OPENAPI文档：http://localhost:8081/v3/api-docs

## 主流框架 & 特性

- Spring Boot 3.1.2
- Spring MVC
- MyBatis + MyBatis Plus 数据访问（开启分页）

## 数据存储

- MariaDB 数据库
- Redis 内存数据库
- 阿里云OSS对象存储
- ElasticSearch 搜索引擎(待办)

## 常用工具

- lombok 简化代码
- knife4j 接口文档

## 业务特性

- 全局请求响应拦截器（token校验）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- 全局跨域处理
- 用户权限校验AOP
- 用户登录、登出、刷新token
- 用户角色权限管理
- 文件上传下载
- 阿里云OSS对象存储
- Redis缓存
- 分页查询

## 实现功能
- 用户接口：注册，登录，查询用户信息，修改用户的个人信息
- 管理员接口：查看用户列表（分页），修改用户权限，添加用户，删除用户
- 文件上传下载接口：上传文件到阿里云OSS 对象存储

