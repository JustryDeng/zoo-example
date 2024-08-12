package com.ideaaedi.zoo.example.springboot.fieldperm.openapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "部门 list req")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XyzReqVO {
    
    @Schema(description = "编码", hidden = true)
    private String code;
    
    @Schema(description = "部门名称")
    private String name;
    
    @Schema(description = "部门路径")
    private String path;
    
}