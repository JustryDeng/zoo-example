package com.ideaaedi.zoo.example.springboot.knife4j.entity.vo;

import com.ideaaedi.zoo.commonbase.entity.BasePageDTO;
import com.ideaaedi.zoo.commonbase.enums.sys.CertTypeEnum;
import com.ideaaedi.zoo.commonbase.enums.sys.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户 list req")
public class DemoUserListReqVO extends BasePageDTO {
    
    @Schema(description = "id集合", hidden = true)
    private Collection<Long> idColl;
    
    @Schema(description = "租户")
    @NotBlank(message = "tenant不能为空")
    private String tenant;
    
    @Schema(description = "用户类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotNull(message = "用户类型不能为空")
    private UserTypeEnum type;
    
    @Schema(description = "用户账号")
    private String accountNo;
    
    @Schema(description = "用户姓名")
    private String name;
    
    @Schema(description = "用户性别（1-男；2-女; 0-未知）")
    private Integer gender;
    
    @Schema(description = "用户手机号")
    private String phone;
    
    @Schema(description = "用户邮箱")
    private String email;
    
    @Schema(description = "用户证件类型")
    private CertTypeEnum certType;
    
    @Schema(description = "用户证件号")
    private String certNo;
    
    @Schema(description = "账号状态(1-正常；2-禁用)")
    private Integer state;
    
    @Schema(description = "部门id")
    private Long deptId;
    
    @Schema(description = "部门名称")
    private String deptName;
    
    @Schema(description = "职位id")
    private Long postId;
    
    @Schema(description = "职位名称")
    private String postName;
}