package com.cre.oj.model.request.admin;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserInfoUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 住址
     */
    private String address;

    /**
     * 邮箱
     */
    private String userEmail;


    /**
     * 用户角色
     */
    private String userRole;


}
