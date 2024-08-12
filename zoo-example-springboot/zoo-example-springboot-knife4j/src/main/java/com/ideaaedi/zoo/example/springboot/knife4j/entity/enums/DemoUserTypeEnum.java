package com.ideaaedi.zoo.example.springboot.knife4j.entity.enums;

import com.ideaaedi.zoo.commonbase.support.EnumDescriptor;
import lombok.Getter;

@Getter
public enum DemoUserTypeEnum implements EnumDescriptor {
    
    TEMP_T("短期临时用户"),
    
    TEMP_E("长期临时用户"),
    
    NORMAL("普通用户"),
    ;
    
    private final String desc;
    
    DemoUserTypeEnum(String desc) {
        this.desc = desc;
    }
    
    @Override
    public String obtainDescription() {
        return this.desc;
    }
}
