package com.cre.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("user")
@Builder
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户角色
     */
    @TableField("user_role")
    private String userRole;

    /**
     * 用户昵称
     */
    @NotEmpty
    @Pattern(regexp = "\\S{1,10}$", message = "用户名长度为1-10个非空字符")
    @TableField("username")
    private String username;

    /**
     * 用户邮箱
     */
    @Email
    @NotEmpty //非空字符串
    private String userEmail;

    /**
     * 用户头像
     */
    private String userPic;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    /**
     * 是否删除
     */
    @TableLogic
    @TableField("isDelete")
    private Integer isDelete;

    /**
     * 用户简介
     */
    private String description;

}
