package com.ideaaedi.zoo.example.springboot.mybatisplus.mapper;

import com.ideaaedi.zoo.diy.artifact.mybatisplus.mapper.BaseMapperExt;
import com.ideaaedi.zoo.example.springboot.mybatisplus.po.UserInfoPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapperExt<UserInfoPO> {

}
