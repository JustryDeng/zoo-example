package com.ideaaedi.zoo.example.springboot.litefeature.cs.entity;

import com.ideaaedi.zoo.commonbase.enums.sys.CertTypeEnum;
import com.ideaaedi.zoo.commonbase.enums.sys.UserTypeEnum;
import lombok.Data;

import java.util.Collection;

@Data
public class DemoUserListReqVO {
    
    private Collection<Long> idColl;
    
    private String tenant;
    
    private UserTypeEnum type;
    
    private String accountNo;
    
    private String name;
    
    private Integer gender;
    
    private String phone;
    
    private String email;
    
    private CertTypeEnum certType;
    
    private String certNo;
    
    private Integer state;
    
    private Long deptId;
    
    private String deptName;
    
    private Long postId;
    
    private String postName;
}