package com.cre.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.springbootinit.common.DeleteRequest;
import com.cre.springbootinit.common.PageBean;
import com.cre.springbootinit.model.entity.User;
import com.cre.springbootinit.model.request.admin.*;
import com.cre.springbootinit.model.request.user.UserRegisterRequest;
import com.cre.springbootinit.model.request.user.UserUpdateInfoRequest;
import com.cre.springbootinit.model.request.user.UserUpdatePwdRequest;
import com.cre.springbootinit.model.response.user.UserLoginResponse;
import com.cre.springbootinit.model.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {
    /**
     * 根据用户账号获取用户信息
     *
     * @param userAccount 用户账号
     * @return UserInfoResponse
     */
    UserVo getUserInfoByAccount(String userAccount);

    /**
     * 用户注册
     *
     * @param userRegisterRequest userRegisterRequest
     */
    void register(UserRegisterRequest userRegisterRequest);

    /**
     * 更新用户信息
     *
     * @param userUpdateInfoRequest userUpdateInfoRequest
     */
    void updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest);

    /**
     * 更新用户头像
     *
     * @param avatarUrl 头像URL
     */
    void updateAvatar(String avatarUrl);

    /**
     * 更新用户密码
     *
     * @param userUpdatePwdRequest userUpdatePwdRequest
     */
    void updatePassword(UserUpdatePwdRequest userUpdatePwdRequest);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 密码
     * @param request      request
     * @return UserLoginResponse
     */
    UserLoginResponse login(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户登出
     *
     * @param request request
     * @return 成功与否
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 更新用户角色
     *
     * @param userRoleUpdateRequest userRoleUpdateRequest
     */
    void updateUserRole(UserRoleUpdateRequest userRoleUpdateRequest);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */

    /**
     * 获取用户列表
     *
     * @param userQueryRequest
     * @return
     */
    PageBean<UserVo> listUserByPage(UserQueryRequest userQueryRequest);

    /**
     * 删除用户
     *
     * @param deleteRequest
     */
    void deleteUser(DeleteRequest deleteRequest);

    /**
     * 添加用户
     *
     * @param userAddRequest
     */
    void addUser(UserAddRequest userAddRequest);

    /**
     * 管理员更新用户
     *
     * @param userInfoUpdateRequest
     */
    void updateUser(UserInfoUpdateRequest userInfoUpdateRequest);
}
