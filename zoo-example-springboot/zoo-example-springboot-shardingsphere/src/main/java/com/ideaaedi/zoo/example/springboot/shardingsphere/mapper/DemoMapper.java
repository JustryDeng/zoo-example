package com.ideaaedi.zoo.example.springboot.shardingsphere.mapper;

import com.ideaaedi.zoo.example.springboot.shardingsphere.entity.po.DemoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DemoMapper {

    /**
     * 有分表 join 没分表
     */
    List<DemoPO> listOne();
    
    /**
     * 有分表 join 没分表 , 同时有参数
     */
    List<DemoPO> listTwo(@Param("from") String from, @Param("to") String to);
    
    /**
     * 有分表 join 没分表，测试使用 非分表列，看能不能再笛卡尔积前过滤一些表
     */
    List<DemoPO> listTwo2(@Param("remark") String remark);
    
    /**
     * 有分表 join 有分表
     */
    List<DemoPO> listThree();
    
    /**
     * 有分表 join 有分表
     */
    List<DemoPO> listFour(@Param("transRecordFrom") String transRecordFrom, @Param("transRecordTo") String transRecordTo,
                          @Param("payRecordFrom") String payRecordFrom, @Param("payRecordTo") String payRecordTo);
    
    /**
     * 有分表 join 有分表 join 没分表
     */
    List<DemoPO> listFive();
    
    /**
     * 有分表 join 有分表 join 没分表
     */
    List<DemoPO> listSix(@Param("transRecordFrom") String transRecordFrom, @Param("transRecordTo") String transRecordTo,
                          @Param("payRecordFrom") String payRecordFrom, @Param("payRecordTo") String payRecordTo);
}
