-- 创建数据库
create database my_database;

-- 使用数据库
use my_oj;

-- 用户表
create table user
(
    id            BIGINT primary key comment 'ID',
    user_account  varchar(20)            not null unique comment '账号',
    user_password varchar(32) comment '密码',
    username      varchar(10)  default '' comment '用户名',
    user_role     varchar(10)  default '' comment '用户权限',
    user_email    varchar(128) default '' comment '邮箱',
    user_pic      varchar(128) default '' comment '头像',
    description   text         default '' comment '个人简介',
    create_time   datetime               not null comment '创建时间',
    update_time   datetime               not null comment '修改时间',
    isDelete      tinyint      default 0 not null comment '是否删除'
) comment '用户表';

-- 题目表
create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)      null comment '标题',
    content     text              null comment '内容',
    tags        varchar(1024)     null comment '标签列表（json 数组）',
    answer      text              null comment '题目答案',
    submitNum   int     default 0 not null comment '题目提交数',
    acceptedNum int     default 0 not null comment '题目通过数',
    judgeCase   text              null comment '判题用例（json 数组）',
    judgeConfig text              null comment '判题配置（json 对象）',
    thumbNum    int     default 0 not null comment '点赞数',
    favourNum   int     default 0 not null comment '收藏数',
    userId      bigint            not null comment '创建用户 id',
    createTime  datetime          not null comment '创建时间',
    updateTime  datetime          not null comment '更新时间',
    isDelete    tinyint default 0 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目';

-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)      not null comment '编程语言',
    code       text              not null comment '用户代码',
    judgeInfo  text              null comment '判题信息（json 对象）',
    status     int     default 0 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    questionId bigint            not null comment '题目 id',
    userId     bigint            not null comment '创建用户 id',
    createTime datetime          not null comment '创建时间',
    updateTime datetime          not null comment '更新时间',
    isDelete   tinyint default 0 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交';


