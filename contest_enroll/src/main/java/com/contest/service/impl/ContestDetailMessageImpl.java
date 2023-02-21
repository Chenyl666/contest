package com.contest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.entity.contest.ContestDetailMessageEntity;
import com.contest.mapper.ContestDetailMessageMapper;
import com.contest.service.ContestDetailMessageService;
import org.springframework.stereotype.Service;

@Service
public class ContestDetailMessageImpl extends ServiceImpl<ContestDetailMessageMapper, ContestDetailMessageEntity> implements ContestDetailMessageService {
}
