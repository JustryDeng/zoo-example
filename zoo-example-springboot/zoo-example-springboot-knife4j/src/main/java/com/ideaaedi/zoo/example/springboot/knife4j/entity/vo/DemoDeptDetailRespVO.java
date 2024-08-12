package com.ideaaedi.zoo.example.springboot.knife4j.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ideaaedi.zoo.commonbase.enums.sys.DeptTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Schema(description = "部门 detail resp")
public class DemoDeptDetailRespVO {

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "父部门id（无父部们则填0）")
    private Integer pid;

    @Schema(description = "父部门")
    @ExcelProperty("父部门")
    private String pName;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "类型")
    private DeptTypeEnum type;

    @Schema(description = "部门路径")
    private String path;

    @Schema(description = "部门路径")
    private String namePath;

    @Schema(description = "排序（同级部门，sort越小越排前面）")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;
    
}