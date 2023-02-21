package com.contest.service.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.entity.user.UserEntity;
import com.contest.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
}
