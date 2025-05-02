package com.cre.oj.controller;

import com.cre.oj.common.BaseResponse;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.enums.UserRoleEnum;
import com.cre.oj.model.request.user.*;
import com.cre.oj.model.vo.LoginUserVO;
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
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@Validated @RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 判断用户是否处于封禁状态
        User user = userService.getLoginUser(request);
        if (Objects.equals(user.getUserRole(), UserRoleEnum.BAN.getValue())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        LoginUserVO loginUserVO = userService.login(userAccount, userPassword, request);
        return BaseResponse.success(loginUserVO);
    }

    /**
     * 用户注销
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
     */
    @GetMapping("/userInfo")
    public BaseResponse<UserVO> getUserInfo(HttpServletRequest request) {
        // 2.查询数据库
        User user = userService.getLoginUser(request);
        return BaseResponse.success(userService.getUserVO(user));
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public BaseResponse updateUserInfo(@RequestBody @Validated UserUpdateInfoRequest userUpdateInfoRequest) {
        userService.updateUserInfo(userUpdateInfoRequest);
        return BaseResponse.success();
    }

    /**
     * 更新头像
     */
    @PatchMapping("/update/avatar")
    public BaseResponse updateAvatar(@RequestBody @Validated UserUpdateAvatarRequest userUpdateAvatarRequest) {
        userService.updateAvatar(userUpdateAvatarRequest);
        return BaseResponse.success();
    }

    /**
     * 更新密码
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
