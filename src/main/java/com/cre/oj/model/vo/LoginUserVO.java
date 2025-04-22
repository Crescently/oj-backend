package com.cre.oj.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户登录响应
 */
@Data
public class LoginUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 住址
     */
    private String address;

    /**
     * 用户头像
     */
    private String userPic;

    /**
     * 用户简介
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
