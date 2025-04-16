package com.cre.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cre.oj.annotation.AuthCheck;
import com.cre.oj.common.BaseResponse;
import com.cre.oj.common.DeleteRequest;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.constant.UserConstant;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.admin.UserAddRequest;
import com.cre.oj.model.request.admin.UserInfoUpdateRequest;
import com.cre.oj.model.request.admin.UserQueryRequest;
import com.cre.oj.model.request.admin.UserRoleUpdateRequest;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Resource
    private UserService userService;

    /**
     * 更新用户角色
     *
     * @param userRoleUpdateRequest userRoleUpdateRequest
     * @return
     */
    @PutMapping("/update/role")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse updateUserRole(@RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {
        if (userRoleUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.updateUserRole(userRoleUpdateRequest);
        return BaseResponse.success();

    }

    /**
     * 获取用户列表
     *
     * @param userQueryRequest listUserRequest
     * @return
     */
    @PostMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size), userService.getQueryWrapper(userQueryRequest));
        return BaseResponse.success(userPage);
    }

    /**
     * 删除用户
     *
     * @param deleteRequest deleteUserRequest
     * @return
     */
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.deleteUser(deleteRequest);
        return BaseResponse.success();
    }

    /**
     * 添加用户
     *
     * @param userAddRequest
     * @return
     */
    @PutMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse addUser(@RequestBody UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.addUser(userAddRequest);
        return BaseResponse.success();
    }

    /**
     * 更新用户
     *
     * @param userInfoUpdateRequest
     * @return
     */
    @PutMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse updateUser(@RequestBody UserInfoUpdateRequest userInfoUpdateRequest) {
        if (userInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.updateUser(userInfoUpdateRequest);
        return BaseResponse.success();
    }

}
