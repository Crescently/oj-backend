-- 创建数据库
create database my_database;

-- 使用数据库
use my_oj;

-- 用户表
create table user
(
    id            BIGINT primary key comment 'ID',
    user_account  varchar(20)            not null comment '账号',
    user_password varchar(32) comment '密码',
    username      varchar(10)  default '' comment '用户名',
    user_role     varchar(10)  default '' comment '用户权限',
    user_email    varchar(128) default '' comment '邮箱',
    signature     text         default '' comment '个性签名',
    telephone     varchar(20)  default '' comment '手机号',
    address       varchar(100) default '' comment '住址',
    user_pic      varchar(128) default '' comment '头像',
    description   text         default '' comment '个人简介',
    create_time   datetime               not null comment '创建时间',
    update_time   datetime               not null comment '修改时间',
    isDelete      tinyint      default 0 not null comment '是否删除'
) comment '用户表';

-- 题目表
create table if not exists question
(
    id           bigint auto_increment comment 'id' primary key,
    title        varchar(512)      null comment '标题',
    content      text              null comment '内容',
    tags         varchar(1024)     null comment '标签列表（json 数组）',
    answer       text              null comment '题目答案',
    submit_num   int     default 0 not null comment '题目提交数',
    accepted_num int     default 0 not null comment '题目通过数',
    judge_case   text              null comment '判题用例（json 数组）',
    judge_config text              null comment '判题配置（json 对象）',
    thumb_num    int     default 0 not null comment '点赞数',
    favour_num   int     default 0 not null comment '收藏数',
    user_id      bigint            not null comment '创建用户 id',
    create_time  datetime          not null comment '创建时间',
    update_time  datetime          not null comment '更新时间',
    isDelete     tinyint default 0 not null comment '是否删除',
    index idx_userId (user_id)
) comment '题目';

-- 题目提交表
create table if not exists question_submit
(
    id          bigint auto_increment comment 'id' primary key,
    language    varchar(128)      not null comment '编程语言',
    code        text              not null comment '用户代码',
    judge_info  text              null comment '判题信息（json 对象）',
    status      int     default 0 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    question_id bigint            not null comment '题目 id',
    user_id     bigint            not null comment '创建用户 id',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '更新时间',
    isDelete    tinyint default 0 not null comment '是否删除',
    index idx_questionId (question_id),
    index idx_userId (user_id)
) comment '题目提交';

create table if not exists question_thumb
(
    id          bigint auto_increment comment 'id' primary key,
    question_id bigint            not null comment ' id',
    user_id     bigint            not null comment '创建用户 id',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    isDelete    tinyint default 0 not null comment '是否删除',
    index idx_articleId (question_id),
    index idx_userId (user_id)
) comment '题目点赞';

create table if not exists question_favour
(
    id          bigint auto_increment comment 'id' primary key,
    question_id bigint            not null comment ' id',
    user_id     bigint            not null comment '创建用户 id',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    isDelete    tinyint default 0 not null comment '是否删除',
    index idx_questionId (question_id),
    index idx_userId (user_id)
) comment '题目收藏';

create table if not exists comments
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    question_id BIGINT            NOT NULL COMMENT '关联的问题ID',
    user_id     BIGINT            NOT NULL COMMENT '评论用户ID',
    content     longtext          NOT NULL COMMENT '评论内容',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    isDelete    tinyint default 0 not null comment '是否删除',
    INDEX idx_article_id (question_id)
) COMMENT = '问题评论表';

create table if not exists user_sign_in
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT            NOT NULL COMMENT '用户ID',
    sign_date   datetime          NOT NULL COMMENT '签到时间',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    isDelete    tinyint default 0 not null comment '是否删除',
    UNIQUE KEY uniq_user_date (user_id, sign_date)
) COMMENT = '用户签到表';

create table if not exists post
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)      null comment '标题',
    content     text              null comment '内容',
    tags        varchar(1024)     null comment '标签列表（json 数组）',
    thumb_num   int     default 0 not null comment '点赞数',
    favour_num  int     default 0 not null comment '收藏数',
    view_count  int     default 0 comment '浏览量',
    user_id     bigint            not null comment '创建用户 id',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    isDelete    tinyint default 0 not null comment '是否删除',
    index idx_userId (user_id)
) comment '帖子';

create table if not exists post_thumb
(
    id          bigint auto_increment comment 'id' primary key,
    post_id     bigint            not null comment '帖子 id',
    user_id     bigint            not null comment '创建用户 id',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    isDelete    tinyint default 0 not null comment '是否删除',
    index idx_postId (post_id),
    index idx_userId (user_id)
) comment '帖子点赞';

create table if not exists post_favour
(
    id          bigint auto_increment comment 'id' primary key,
    post_id     bigint            not null comment '帖子 id',
    user_id     bigint            not null comment '创建用户 id',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    isDelete    tinyint default 0 not null comment '是否删除',
    index idx_postId (post_id),
    index idx_userId (user_id)
) comment '帖子收藏';

create table if not exists post_comment
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    post_id     BIGINT            NOT NULL COMMENT '所属文章ID',
    user_id     BIGINT            NOT NULL COMMENT '评论者ID',
    content     TEXT              NOT NULL COMMENT '评论内容',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    isDelete    tinyint default 0 not null comment '是否删除'
) comment '帖子评论';





