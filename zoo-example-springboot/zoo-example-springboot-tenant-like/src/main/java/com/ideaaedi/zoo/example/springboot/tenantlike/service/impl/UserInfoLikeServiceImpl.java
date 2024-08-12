package com.ideaaedi.zoo.example.springboot.tenantlike.service.impl;

import com.ideaaedi.zoo.diy.artifact.mybatisplus.service.ServiceImplExt;
import com.ideaaedi.zoo.example.springboot.tenantlike.mapper.UserInfoLikeMapper;
import com.ideaaedi.zoo.example.springboot.tenantlike.po.UserInfoLikePO;
import com.ideaaedi.zoo.example.springboot.tenantlike.service.UserInfoLikeService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoLikeServiceImpl extends ServiceImplExt<UserInfoLikeMapper, UserInfoLikePO> implements UserInfoLikeService {

}
