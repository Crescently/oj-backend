package com.cre.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.common.DeleteRequest;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.admin.*;
import com.cre.oj.model.request.user.UserRegisterRequest;
import com.cre.oj.model.request.user.UserUpdateAvatarRequest;
import com.cre.oj.model.request.user.UserUpdateInfoRequest;
import com.cre.oj.model.request.user.UserUpdatePwdRequest;
import com.cre.oj.model.vo.LoginUserVO;
import com.cre.oj.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {
    /**
     * 根据用户账号获取用户信息
     */
    User getUser(String userAccount);

    /**
     * 用户注册
     */
    void register(UserRegisterRequest userRegisterRequest);

    /**
     * 更新用户信息
     */
    void updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest);

    /**
     * 更新用户头像
     */
    void updateAvatar(UserUpdateAvatarRequest userUpdateAvatarRequest);

    /**
     * 更新用户密码
     */
    void updatePassword(UserUpdatePwdRequest userUpdatePwdRequest);

    /**
     * 用户登录
     */
    LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户登出
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 更新用户角色
     */
    void updateUserRole(UserRoleUpdateRequest userRoleUpdateRequest);


    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
    /**
     * 删除用户
     */
    void deleteUser(DeleteRequest deleteRequest);

    /**
     * 添加用户
     */
    void addUser(UserAddRequest userAddRequest);

    /**
     * 管理员更新用户
     */
    void updateUser(UserInfoUpdateRequest userInfoUpdateRequest);

    /**
     * 获取脱敏的用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     */
    boolean isAdmin(User user);
}
