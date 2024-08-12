package com.ideaaedi.zoo.example.springboot.knife4j.entity.enums;

import com.ideaaedi.zoo.commonbase.support.EnumDescriptor;
import lombok.Getter;

@Getter
public enum DemoCertTypeEnum implements EnumDescriptor {
    
    ID_CARD("身份证"),
    
    PASSPORT("护照");
    
    private final String desc;
    
    DemoCertTypeEnum(String desc) {
        this.desc = desc;
    }
    
    @Override
    public String obtainDescription() {
        return this.desc;
    }
}
