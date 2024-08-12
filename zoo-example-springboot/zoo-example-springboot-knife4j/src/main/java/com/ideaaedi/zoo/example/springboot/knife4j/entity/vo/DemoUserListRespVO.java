package com.ideaaedi.zoo.example.springboot.knife4j.entity.vo;

import com.ideaaedi.zoo.commonbase.enums.sys.CertTypeEnum;
import com.ideaaedi.zoo.commonbase.enums.sys.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户 list resp")
public class DemoUserListRespVO {

    @Schema(description = "id")
    private Long id;
    
    @Schema(description = "租户编码")
    private String tenant;
    
    @Schema(description = "所属业务租户的名称")
    private String tenantName;
    
    @Schema(description = "用户类型")
    private UserTypeEnum type;

    @Schema(description = "用户账号")
    private String accountNo;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "用户生日")
    private String birthday;

    @Schema(description = "用户性别（1-男；2-女; 0-未知）")
    private Integer gender;

    @Schema(description = "头像(附件id)")
    private Long avatarId;
    
    @Schema(description = "头像url")
    private String avatarUrl;
    
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

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;
    
    @Schema(description = "部门id")
    private Integer deptId;
    
    @Schema(description = "部门名称")
    private String deptName;
    
    @Schema(description = "职位id")
    private Long postId;
    
    @Schema(description = "职位名称")
    private String postName;

}