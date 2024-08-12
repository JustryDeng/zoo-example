package com.ideaaedi.zoo.example.springboot.dynamic.datasource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ideaaedi.zoo.example.springboot.dynamic.datasource.entity.po.TransRecordPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransRecordMapper extends BaseMapper<TransRecordPO> {

}
