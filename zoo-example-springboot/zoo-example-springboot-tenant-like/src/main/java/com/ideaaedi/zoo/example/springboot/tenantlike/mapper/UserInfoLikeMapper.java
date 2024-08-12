package com.ideaaedi.zoo.example.springboot.tenantlike.mapper;

import com.ideaaedi.zoo.diy.artifact.mybatisplus.mapper.BaseMapperExt;
import com.ideaaedi.zoo.example.springboot.tenantlike.po.UserInfoLikePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoLikeMapper extends BaseMapperExt<UserInfoLikePO> {

}
