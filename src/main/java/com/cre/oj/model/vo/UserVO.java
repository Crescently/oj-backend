package com.cre.oj.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    private Long id;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
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
     * 用户角色
     */
    private String userRole;

    /**
     * 用户头像地址
     */
    private String userPic;

    /**
     * 用户简介
     */
    private String description;


}
