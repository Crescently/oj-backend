package com.cre.oj.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录响应
 */
@Data
public class LoginUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Long id;

    private String userAccount;

    private String userRole;

    private String username;

    private String userEmail;

    private String userPic;

}
