package com.contest;

import com.contest.entity.user.UserEntity;
import com.contest.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class RegisterTest {

    @Resource
    UserMapper userMapper;

    @Test
    public void getAllUsers(){
        List<UserEntity> userEntities = userMapper.selectList(null);
        userEntities.forEach(System.out::println);
    }

}
