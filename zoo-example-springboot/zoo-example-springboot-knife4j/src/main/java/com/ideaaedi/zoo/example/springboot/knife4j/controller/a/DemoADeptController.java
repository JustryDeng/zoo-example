package com.ideaaedi.zoo.example.springboot.knife4j.controller.a;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.ideaaedi.zoo.commonbase.entity.PageInfo;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.diy.artifact.apidoc.knife4j.annotation.ApiTag;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.enums.DemoCertTypeEnum;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.vo.DemoDeptDetailRespVO;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.vo.DemoDeptListReqVO;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.vo.DemoDeptListRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/dept-a")
@ApiTag(name = "部门A", order = 1)
public class DemoADeptController {
    
    @PostMapping("/list")
    @Operation(summary = "列表")
    @ApiOperationSupport(order = 1)
    Result<PageInfo<DemoDeptListRespVO>> list(@Validated @RequestBody DemoDeptListReqVO req) {
        return Result.success();
    }
    
    
    @Operation(summary = "详情")
    @GetMapping("/detail/{deptId}")
    @ApiOperationSupport(order = 2)
    Result<DemoDeptDetailRespVO> detail(@Parameter(description = "部门id")
                                        @NotNull(message = "deptId不能为空") @PathVariable("deptId") Integer deptId) {
        return Result.success();
    }
    
    @Operation(summary = "大杂烩请求")
    @Parameters({
            @Parameter(name = "id", description = "文件id", in = ParameterIn.PATH),
            @Parameter(name = "token", description = "请求token", in = ParameterIn.HEADER),
            @Parameter(name = "name", description = "文件名称", in = ParameterIn.QUERY)
    })
    @PostMapping("/bodyParamHeaderPath/{id}")
    @ApiOperationSupport(order = 3)
    public Result<DemoDeptListRespVO> bodyParamHeaderPath(@PathVariable(value = "id") String id,
                                                          @RequestHeader(value = "token") String token,
                                                          @RequestParam(value = "name") String name,
                                                          @RequestBody DemoDeptListReqVO req) {
        return Result.success();
    }
    
    
    @Parameters({
            @Parameter(name = "certType", description = "证件类型", in = ParameterIn.QUERY),
            @Parameter(name = "certTypeList", description = "证件类型集合", in = ParameterIn.QUERY),
            @Parameter(name = "certTypeArray", description = "证件类型数组", in = ParameterIn.QUERY)
    })
    @PostMapping("/enum-demo")
    @Operation(summary = "自定义枚举参数说明")
    @ApiOperationSupport(order = 4)
    Result<PageInfo<DemoDeptListRespVO>> enumDemo(@Validated @RequestBody DemoDeptListReqVO req,
                                                  @RequestParam(value = "certType") DemoCertTypeEnum certType,
                                                  @RequestParam(value = "certTypeList") List<DemoCertTypeEnum> certTypeList,
                                                  @RequestParam(value = "certTypeArray") DemoCertTypeEnum[] certTypeArray
    ) {
        return Result.success();
    }
}
