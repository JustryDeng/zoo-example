package com.ideaaedi.zoo.example.springboot.mybatisplus.service.impl;

import com.ideaaedi.zoo.diy.artifact.mybatisplus.service.ServiceImplExt;
import com.ideaaedi.zoo.example.springboot.mybatisplus.mapper.UserInfoMapper;
import com.ideaaedi.zoo.example.springboot.mybatisplus.po.UserInfoPO;
import com.ideaaedi.zoo.example.springboot.mybatisplus.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImplExt<UserInfoMapper, UserInfoPO> implements UserInfoService {

}
