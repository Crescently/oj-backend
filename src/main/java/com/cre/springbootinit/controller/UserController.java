package com.cre.springbootinit.controller;

import com.cre.springbootinit.common.BaseResponse;
import com.cre.springbootinit.common.ErrorCode;
import com.cre.springbootinit.exception.BusinessException;
import com.cre.springbootinit.model.enums.UserRoleEnum;
import com.cre.springbootinit.model.request.user.UserLoginRequest;
import com.cre.springbootinit.model.request.user.UserRegisterRequest;
import com.cre.springbootinit.model.request.user.UserUpdateInfoRequest;
import com.cre.springbootinit.model.request.user.UserUpdatePwdRequest;
import com.cre.springbootinit.model.response.user.UserInfoResponse;
import com.cre.springbootinit.model.response.user.UserLoginResponse;
import com.cre.springbootinit.service.UserService;
import com.cre.springbootinit.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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

        log.info("用户注册，用户名：{}，密码：{}", userRegisterRequest.getUserAccount(), userRegisterRequest.getUserPassword());
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
        UserInfoResponse userinfo = userService.getUserInfoByAccount(userAccount);
        if (Objects.equals(userinfo.getUserRole(), UserRoleEnum.BAN.getValue())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        log.info("用户登录，用户账号：{}，密码：{}", userAccount, userPassword);
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
    public BaseResponse<Boolean> userLogout(HttpServletRequest request, @RequestHeader("Authorization") String token) {
        if (request == null || Objects.equals(token, "")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 删除redis中的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

        boolean result = userService.userLogout(request);
        return BaseResponse.success(result);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/userInfo")
    public BaseResponse<UserInfoResponse> getUserInfo() {
        // 使用ThreadLocal获取用户名
        Map<String, Object> map = ThreadLocalUtil.get();
        String userAccount = (String) map.get("userAccount");
        log.info("当前登录的用户账号：{}", userAccount);
        // 2.查询数据库
        UserInfoResponse userInfoResponse = userService.getUserInfoByAccount(userAccount);
        return BaseResponse.success(userInfoResponse);
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateInfoRequest 用户信息更新请求体
     * @return BaseResponse
     */
    @PutMapping("/update")
    public BaseResponse updateUserInfo(@RequestBody @Validated UserUpdateInfoRequest userUpdateInfoRequest) {
        log.info("用户信息更新");
        userService.updateUserInfo(userUpdateInfoRequest);
        return BaseResponse.success();
    }

    /**
     * 更新头像
     *
     * @param avatarUrl 头像url地址
     */
    @PatchMapping("/updateAvatar")
    public BaseResponse updateAvatar(@RequestParam @URL String avatarUrl) {
        log.info("更新头像");
        userService.updateAvatar(avatarUrl);
        return BaseResponse.success();
    }

    /**
     * 更新密码
     *
     * @param userUpdatePwdRequest 用户密码更新请求体
     * @return BaseResponse
     */
    @PatchMapping("/updatePwd")
    public BaseResponse updatePassword(@RequestBody UserUpdatePwdRequest userUpdatePwdRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("更新密码");
        userService.updatePassword(userUpdatePwdRequest);
        // 删除redis中的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        // 执行退出操作
        userService.userLogout(request);

        return BaseResponse.success();
    }
}
