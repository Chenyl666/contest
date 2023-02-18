package com.contest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.mapper.ContestDetailMapper;
import com.contest.service.ContestDetailService;
import org.springframework.stereotype.Service;

@Service
public class ContestDetailServiceImpl extends ServiceImpl<ContestDetailMapper, ContestDetailEntity> implements ContestDetailService {
}
