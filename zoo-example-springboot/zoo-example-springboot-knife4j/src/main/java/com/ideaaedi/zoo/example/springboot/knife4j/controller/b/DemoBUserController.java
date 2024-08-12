package com.ideaaedi.zoo.example.springboot.knife4j.controller.b;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.ideaaedi.zoo.commonbase.entity.PageInfo;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.diy.artifact.apidoc.knife4j.annotation.ApiTag;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.vo.DemoUserDetailRespVO;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.vo.DemoUserListReqVO;
import com.ideaaedi.zoo.example.springboot.knife4j.entity.vo.DemoUserListRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/user-b")
@ApiTag(name = "用户B", order = 3)
public class DemoBUserController {
    
    
    /**
     * 列表
     *
     * @param req 参数
     *
     * @return 用户列表
     */
    @PostMapping("/list")
    @Operation(summary = "列表")
    @ApiOperationSupport(order = 1)
    Result<PageInfo<DemoUserListRespVO>> list(@Validated @RequestBody DemoUserListReqVO req) {
        return Result.success();
    }
    
    /**
     * 详情
     *
     * @param userId 用户id
     *
     * @return 用户详情
     */
    @Operation(summary = "详情")
    @ApiOperationSupport(order = 2)
    @GetMapping("/detail/{userId}")
    Result<DemoUserDetailRespVO> detail(@Parameter(description = "用户id")
                                       @NotNull(message = "userId不能为空") @PathVariable("userId") Long userId) {
        return Result.success();
    }
    
}
