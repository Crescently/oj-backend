package com.cre.oj.model.request.admin;

import com.cre.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 获取用户信息列表请求
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
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
     * 用户角色
     */
    private String userRole;

    /**
     * 用户昵称
     */
    private String username;


    /**
     * 手机号
     */
    private String telephone;


}
