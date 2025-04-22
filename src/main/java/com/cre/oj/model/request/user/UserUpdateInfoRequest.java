package com.cre.oj.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户信息更新请求体
 */
@Data
public class UserUpdateInfoRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotEmpty
    @Pattern(regexp = "\\S{1,10}$", message = "用户名长度为1-10个非空字符")
    private String username;

    @Email
    @NotEmpty //非空字符串
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
     * 个人简介
     */
    private String description;


}
