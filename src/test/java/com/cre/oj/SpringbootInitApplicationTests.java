package com.cre.oj;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cre.oj.mapper.UserMapper;
import com.cre.oj.model.entity.User;
import com.cre.oj.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class SpringbootInitApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void TestMP() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = 1L;
        User user = User.builder().id(id).username("刘震").userEmail("2376355613@qq.com").build();

//        userMapper.updateUserInfo(user);
        userMapper.update(user, new QueryWrapper<User>().eq("id", id));
    }

}
