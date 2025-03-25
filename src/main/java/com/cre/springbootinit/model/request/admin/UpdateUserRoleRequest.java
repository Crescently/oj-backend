package com.cre.springbootinit.model.request.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UpdateUserRoleRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    @NotBlank(message = "用户身份不能为空")
    private String newUserRole;
}
