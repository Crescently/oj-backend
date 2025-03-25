package com.cre.springbootinit.controller;

import com.cre.springbootinit.annotation.AuthCheck;
import com.cre.springbootinit.common.BaseResponse;
import com.cre.springbootinit.common.DeleteRequest;
import com.cre.springbootinit.common.ErrorCode;
import com.cre.springbootinit.common.PageBean;
import com.cre.springbootinit.constant.UserConstant;
import com.cre.springbootinit.exception.BusinessException;
import com.cre.springbootinit.model.request.admin.*;
import com.cre.springbootinit.model.response.admin.ListUserInfoResponse;
import com.cre.springbootinit.service.UserService;
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
     * @param updateUserRoleRequest updateUserRoleRequest
     * @return
     */
    @PutMapping("/updateUserRole")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse updateUserRole(@RequestBody UpdateUserRoleRequest updateUserRoleRequest) {
        if (updateUserRoleRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.updateUserRole(updateUserRoleRequest);
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
    public BaseResponse<PageBean<ListUserInfoResponse>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        PageBean<ListUserInfoResponse> userPageBean = userService.listUserByPage(userQueryRequest);
        return BaseResponse.success(userPageBean);
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
     * @param updateUserInfoRequest
     * @return
     */
    @PutMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse updateUser(@RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        if (updateUserInfoRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.updateUser(updateUserInfoRequest);
        return BaseResponse.success();
    }

}
