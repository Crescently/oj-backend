package com.cre.oj.controller;

import com.cre.oj.common.BaseResponse;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.enums.UserRoleEnum;
import com.cre.oj.model.request.user.*;
import com.cre.oj.model.response.user.UserLoginResponse;
import com.cre.oj.model.vo.UserVO;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return BaseResponse
     */
    @PostMapping("/register")
    public BaseResponse userRegister(@Validated @RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        userService.register(userRegisterRequest);
        return BaseResponse.success();
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @return BaseResponse
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> userLogin(@Validated @RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 判断用户是否处于封禁状态
        User user = userService.getUser(userAccount);
        if (Objects.equals(user.getUserRole(), UserRoleEnum.BAN.getValue())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        UserLoginResponse userLoginResponse = userService.login(userAccount, userPassword, request);
        return BaseResponse.success(userLoginResponse);
    }

    /**
     * 用户注销
     *
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        boolean result = userService.userLogout(request);
        User loginUser = userService.getLoginUser(request);
        String redisKey = "login:user:" + loginUser.getId();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(redisKey);

        return BaseResponse.success(result);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/userInfo")
    public BaseResponse<UserVO> getUserInfo(HttpServletRequest request) {
        // 2.查询数据库
        User user = userService.getLoginUser(request);
        return BaseResponse.success(userService.getUserVO(user));
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateInfoRequest 用户信息更新请求体
     * @return BaseResponse
     */
    @PutMapping("/update")
    public BaseResponse updateUserInfo(@RequestBody @Validated UserUpdateInfoRequest userUpdateInfoRequest) {
        userService.updateUserInfo(userUpdateInfoRequest);
        return BaseResponse.success();
    }

    /**
     * 更新头像
     *
     * @param userUpdateAvatarRequest
     * @return
     */
    @PatchMapping("/update/avatar")
    public BaseResponse updateAvatar(@RequestBody @Validated UserUpdateAvatarRequest userUpdateAvatarRequest) {
        userService.updateAvatar(userUpdateAvatarRequest);
        return BaseResponse.success();
    }

    /**
     * 更新密码
     *
     * @param userUpdatePwdRequest 用户密码更新请求体
     * @return BaseResponse
     */
    @PatchMapping("/update/pwd")
    public BaseResponse updatePassword(@RequestBody UserUpdatePwdRequest userUpdatePwdRequest, HttpServletRequest request) {
        userService.updatePassword(userUpdatePwdRequest);
        String redisKey = "login:user:" + userUpdatePwdRequest.getUserId();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(redisKey);
        // 执行退出操作
        userService.userLogout(request);

        return BaseResponse.success();
    }
}
