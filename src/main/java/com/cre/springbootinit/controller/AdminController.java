package com.cre.springbootinit.controller;

import com.cre.springbootinit.annotation.AuthCheck;
import com.cre.springbootinit.common.BaseResponse;
import com.cre.springbootinit.common.DeleteRequest;
import com.cre.springbootinit.common.ErrorCode;
import com.cre.springbootinit.common.PageBean;
import com.cre.springbootinit.constant.UserConstant;
import com.cre.springbootinit.exception.BusinessException;
import com.cre.springbootinit.model.request.admin.*;
import com.cre.springbootinit.model.vo.UserVo;
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
     * @param userRoleUpdateRequest userRoleUpdateRequest
     * @return
     */
    @PutMapping("/updateUserRole")
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
    public BaseResponse<PageBean<UserVo>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        PageBean<UserVo> userList = userService.listUserByPage(userQueryRequest);
        return BaseResponse.success(userList);
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
