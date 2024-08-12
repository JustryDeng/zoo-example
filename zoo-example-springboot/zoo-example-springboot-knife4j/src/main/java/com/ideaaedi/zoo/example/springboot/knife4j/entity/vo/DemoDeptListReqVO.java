package com.ideaaedi.zoo.example.springboot.knife4j.entity.vo;

import com.ideaaedi.zoo.commonbase.enums.sys.DeptTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Data
@Schema(description = "部门 list req")
public class DemoDeptListReqVO {
    
    @Schema(description = "无视组织架构类型，要附加查询的部门id (注：当type不为null且此字段有值时生效)")
    private Collection<Integer> ignoreTypeAdditionalQueryIds;
    
    @Schema(description = "父部门id（无父部们则填0）")
    private Integer pid;
    
    @Schema(description = "编码")
    private String code;
    
    @Schema(description = "部门名称")
    private String name;
    
    @Schema(description = "类型")
    private DeptTypeEnum type;
    
    @Schema(description = "测试LocalTime类型的展示效果")
    private LocalTime localTime;
    
    @Schema(description = "测试localDate类型的展示效果")
    private LocalDate localDate;
    
    @Schema(description = "测试LocalDateTime类型的展示效果")
    private LocalDateTime localDateTime;
    
    @Schema(description = "类型集合")
    private List<DeptTypeEnum> typeList;
    
    @Schema(description = "类型数组")
    private DeptTypeEnum[] typeArray;
    
}