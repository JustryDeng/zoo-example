package com.ideaaedi.zoo.example.springboot.knife4j.entity.enums;

import com.ideaaedi.zoo.commonbase.support.EnumDescriptor;
import lombok.Getter;

@Getter
public enum DemoTypeEnum implements EnumDescriptor {
    
    DEPT("部门"),
    
    FACTORY("工厂"),
    ;
    
    private final String desc;
    
    DemoTypeEnum(String desc) {
        this.desc = desc;
    }
    
    @Override
    public String obtainDescription() {
        return this.desc;
    }
}
