package com.ideaaedi.zoo.example.springboot.knife4j.controller.b;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.ideaaedi.zoo.commonbase.entity.PageInfo;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.diy.artifact.apidoc.knife4j.annotation.ApiTag;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.vo.DemoDeptDetailRespVO;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.vo.DemoDeptListReqVO;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.vo.DemoDeptListRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@RequestMapping("/dept-b")
@ApiTag(name = "部门B", order = 4)
public class DemoBDeptController {
    
    @PostMapping({"/list", "/pages"})
    @Operation(summary = "列表")
    @ApiOperationSupport(order = 2)
    Result<PageInfo<DemoDeptListRespVO>> list(@Validated @RequestBody DemoDeptListReqVO req) {
        return Result.success();
    }
    
    
    @Operation(summary = "详情")
    @RequestMapping("/detail/{deptId}")
    @ApiOperationSupport(order = 1)
    Result<DemoDeptDetailRespVO> detail(@Parameter(description = "部门id")
                                        @NotNull(message = "deptId不能为空") @PathVariable("deptId") Integer deptId) {
        return Result.success();
    }
    
}
