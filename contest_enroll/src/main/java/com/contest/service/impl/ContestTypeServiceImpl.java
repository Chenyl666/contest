package com.contest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.entity.contest.ContestType;
import com.contest.mapper.ContestTypeMapper;
import com.contest.service.ContestTypeService;
import org.springframework.stereotype.Service;

@Service
public class ContestTypeServiceImpl extends ServiceImpl<ContestTypeMapper, ContestType> implements ContestTypeService {
}
