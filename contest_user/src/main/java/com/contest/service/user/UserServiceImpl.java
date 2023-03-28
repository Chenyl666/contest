package com.contest.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.dto.user.UserDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.user.UserEntity;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    /**
     * 获取用户详细信息
     * */
    @Override
    public ResultModel<UserDetailDto> getUserDetailDto(UserDto userDto) {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(getById(userDto.getUserId()));
        if(userEntityOptional.isPresent()){
            return ResultModel.buildSuccessResultModel(null,userEntityOptional.get().entity2DetailDto());
        }
        return ResultModel.buildFailResultModel("not exist",null);
    }

    /**
     * 保存用户信息
     * */
    @Override
    public ResultModel<String> saveUser(UserDetailDto userDetailDto) {
        UserEntity userEntity = userDetailDto.detailDto2Entity();
        updateById(userEntity);
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 通过用户id获取用户详细信息
     * */
    @Override
    public ResultModel<UserDetailDto> getUserDetailById(String userId) {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(getById(userId));
        if(userEntityOptional.isPresent()){
            return ResultModel.buildSuccessResultModel(null,userEntityOptional.get().entity2DetailDto());
        }
        return ResultModel.buildFailResultModel("not exist",null);
    }

    /**
     * 批量用户数据
     * */
    @Override
    public ResultModel<List<UserDto>> getUserDtoListByIdList(List<String> idList) {
        List<UserEntity> userEntityList = list(new QueryWrapper<UserEntity>().in("user_id", idList));
        List<UserDto> userDtoList = userEntityList.stream().map(UserEntity::entity2Dto).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,userDtoList);
    }
}
