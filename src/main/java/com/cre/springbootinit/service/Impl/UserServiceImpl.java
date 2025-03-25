package com.cre.springbootinit.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.springbootinit.common.DeleteRequest;
import com.cre.springbootinit.common.ErrorCode;
import com.cre.springbootinit.common.PageBean;
import com.cre.springbootinit.constant.CommonConstant;
import com.cre.springbootinit.constant.MessageConstant;
import com.cre.springbootinit.exception.BusinessException;
import com.cre.springbootinit.mapper.UserMapper;
import com.cre.springbootinit.model.entity.User;
import com.cre.springbootinit.model.enums.UserRoleEnum;
import com.cre.springbootinit.model.request.admin.UpdateUserInfoRequest;
import com.cre.springbootinit.model.request.admin.UpdateUserRoleRequest;
import com.cre.springbootinit.model.request.admin.UserAddRequest;
import com.cre.springbootinit.model.request.admin.UserQueryRequest;
import com.cre.springbootinit.model.request.user.UserRegisterRequest;
import com.cre.springbootinit.model.request.user.UserUpdateInfoRequest;
import com.cre.springbootinit.model.request.user.UserUpdatePwdRequest;
import com.cre.springbootinit.model.response.admin.ListUserInfoResponse;
import com.cre.springbootinit.model.response.user.UserInfoResponse;
import com.cre.springbootinit.model.response.user.UserLoginResponse;
import com.cre.springbootinit.service.UserService;
import com.cre.springbootinit.utils.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cre.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserInfoResponse getUserInfoByAccount(String userAccount) {
        // 查询数据库，根据用户名获取用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userAccount);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, MessageConstant.USERNAME_NOT_EXIST);
        }

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        BeanUtils.copyProperties(user, userInfoResponse);
        return userInfoResponse;
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

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public PageBean<ListUserInfoResponse> listUserByPage(UserQueryRequest userQueryRequest) {
        PageBean<ListUserInfoResponse> userPageBean = new PageBean<>();
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
        // 开启分页查询 PageHelper
        PageHelper.startPage(userQueryRequest.getCurrent(), userQueryRequest.getPageSize());
        // 查询数据库
        List<User> userList = userMapper.selectList(queryWrapper);
        Page<User> userPage = (Page<User>) userList;

        List<ListUserInfoResponse> responseList = BeanUtil.copyToList(userList, ListUserInfoResponse.class);

        userPageBean.setTotal(userPage.getTotal());
        userPageBean.setItems(responseList);

        return userPageBean;
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
    public void updateUser(UpdateUserInfoRequest updateUserInfoRequest) {
        Integer id = updateUserInfoRequest.getId();
        User user = User.builder().username(updateUserInfoRequest.getUsername()).userAccount(updateUserInfoRequest.getUserAccount()).userEmail(updateUserInfoRequest.getUserEmail()).description(updateUserInfoRequest.getDescription()).userRole(updateUserInfoRequest.getUserRole()).build();

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        userMapper.update(user, wrapper);

    }


    @Override
    public void updateUserRole(UpdateUserRoleRequest updateUserRoleRequest) {
        String userAccount = updateUserRoleRequest.getUserAccount();
        String newUserRole = updateUserRoleRequest.getNewUserRole();

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userAccount);

        User user = User.builder().userRole(newUserRole).build();
        userMapper.update(user, wrapper);
    }


    @Override
    public void updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
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
        Map<String, Object> map = ThreadLocalUtil.get();
        String userAccount = (String) map.get("userAccount");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("*").eq("user_account", userAccount);
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
