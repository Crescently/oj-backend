package com.cre.oj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.oj.common.DeleteRequest;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.constant.CommonConstant;
import com.cre.oj.constant.MessageConstant;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.mapper.UserMapper;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.enums.UserRoleEnum;
import com.cre.oj.model.request.admin.UserAddRequest;
import com.cre.oj.model.request.admin.UserInfoUpdateRequest;
import com.cre.oj.model.request.admin.UserQueryRequest;
import com.cre.oj.model.request.admin.UserRoleUpdateRequest;
import com.cre.oj.model.request.user.UserRegisterRequest;
import com.cre.oj.model.request.user.UserUpdateInfoRequest;
import com.cre.oj.model.request.user.UserUpdatePwdRequest;
import com.cre.oj.model.response.user.UserLoginResponse;
import com.cre.oj.model.vo.UserVO;
import com.cre.oj.service.UserService;
import com.cre.oj.utils.JwtUtil;
import com.cre.oj.utils.LoginUserInfoUtil;
import com.cre.oj.utils.Md5Util;
import com.cre.oj.utils.SqlUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cre.oj.constant.UserConstant.USER_LOGIN_STATE;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public User getUser(String userAccount) {
        // 查询数据库，根据用户名获取用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userAccount);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, MessageConstant.USERNAME_NOT_EXIST);
        }
        return user;
    }

    @Override
    public void register(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String username = userRegisterRequest.getUsername();
        String userEmail = userRegisterRequest.getUserEmail();
        // 检查两次密码是否一致
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.TWO_PWD_NOT_MATCH);
        }

        synchronized (userAccount.intern()) {
            // 查询数据库是否已有账户名
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.select("*").eq("user_account", userAccount);
            User user = userMapper.selectOne(wrapper);
            if (user != null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.USERNAME_ALREADY_EXIST);
            }
            // 首先对密码进行加密，保证安全性
            String md5Password = Md5Util.getMD5String(userPassword);

            user = User.builder().userAccount(userAccount).userPassword(md5Password).username(username).userEmail(userEmail)
                    // 默认注册用户是普通用户
                    .userRole(UserRoleEnum.USER.getValue()).build();
            // 将用户信息插入数据库
            userMapper.insert(user);
        }
    }

    @Override
    public UserLoginResponse login(String userAccount, String userPassword, HttpServletRequest request) {
        // 根据用户名查询用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userAccount);
        User loginUser = userMapper.selectOne(wrapper);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, MessageConstant.USERNAME_NOT_EXIST);
        }
        // 判断密码正确性
        if (Md5Util.getMD5String(userPassword).equals(loginUser.getUserPassword())) {
            // 登录成功，返回JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("userAccount", loginUser.getUserAccount());
            String token = JwtUtil.genToken(claims);
            // 把token存入redis
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 1, TimeUnit.HOURS);

            // 封装返回值
            UserLoginResponse userLoginResponse = new UserLoginResponse();
            userLoginResponse.setToken(token);
            BeanUtils.copyProperties(loginUser, userLoginResponse);

            request.getSession().setAttribute(USER_LOGIN_STATE, loginUser);
            return userLoginResponse;
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.PASSWORD_ERROR);
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }


    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        String userAccount = userQueryRequest.getUserAccount();
        String username = userQueryRequest.getUsername();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(userAccount), "user_account", userAccount);
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "user_role", userRole);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public void deleteUser(DeleteRequest deleteRequest) {
        userMapper.deleteById(deleteRequest.getId());
    }

    @Override
    public void addUser(UserAddRequest userAddRequest) {
        User user = User.builder().username(userAddRequest.getUsername()).userAccount(userAddRequest.getUserAccount()).userEmail(userAddRequest.getUserEmail()).description(userAddRequest.getDescription()).userRole(userAddRequest.getUserRole()).build();

        userMapper.insert(user);
    }

    @Override
    public void updateUser(UserInfoUpdateRequest userInfoUpdateRequest) {
        Long id = userInfoUpdateRequest.getId();
        User user = User.builder().username(userInfoUpdateRequest.getUsername()).userAccount(userInfoUpdateRequest.getUserAccount()).userEmail(userInfoUpdateRequest.getUserEmail()).description(userInfoUpdateRequest.getDescription()).userRole(userInfoUpdateRequest.getUserRole()).build();

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        userMapper.update(user, wrapper);

    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }


    @Override
    public void updateUserRole(UserRoleUpdateRequest userRoleUpdateRequest) {
        String userAccount = userRoleUpdateRequest.getUserAccount();
        String newUserRole = userRoleUpdateRequest.getNewUserRole();

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userAccount);

        User user = User.builder().userRole(newUserRole).build();
        userMapper.update(user, wrapper);
    }


    @Override
    public void updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest) {
        Long id = LoginUserInfoUtil.getUserId();
        User user = User.builder().id(id).username(userUpdateInfoRequest.getUsername()).userEmail(userUpdateInfoRequest.getUserEmail()).build();
        userMapper.update(user, new QueryWrapper<User>().eq("id", id));
    }

    /*
    更新头像
     */
    @Override
    public void updateAvatar(String avatarUrl) {
        User user = User.builder().userPic(avatarUrl).build();
        userMapper.update(user, new QueryWrapper<User>().eq("id", LoginUserInfoUtil.getUserId()));
    }

    @Override
    public void updatePassword(UserUpdatePwdRequest userUpdatePwdRequest) {
        String oldPassword = userUpdatePwdRequest.getOldPassword();
        String newPassword = userUpdatePwdRequest.getNewPassword();
        String rePassword = userUpdatePwdRequest.getRePassword();
        // 原密码是否正确
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("*").eq("user_account", LoginUserInfoUtil.getAccount());
        User loginUser = userMapper.selectOne(wrapper);
        if (!Md5Util.checkPassword(oldPassword, loginUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.OLD_PWD_ERROR);
        }
        if (!newPassword.equals(rePassword)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, MessageConstant.TWO_PWD_NOT_MATCH);
        }

        String md5String = Md5Util.getMD5String(newPassword);
        // 2.更新数据库
        User user = User.builder().userPassword(md5String).build();
        userMapper.update(user, new QueryWrapper<User>().eq("id", LoginUserInfoUtil.getUserId()));
    }

}
