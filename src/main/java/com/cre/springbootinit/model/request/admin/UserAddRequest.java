package com.cre.springbootinit.model.request.admin;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
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
     * 用户简介
     */
    private String description;

    /**
     * 用户角色
     */
    private String userRole;


}
