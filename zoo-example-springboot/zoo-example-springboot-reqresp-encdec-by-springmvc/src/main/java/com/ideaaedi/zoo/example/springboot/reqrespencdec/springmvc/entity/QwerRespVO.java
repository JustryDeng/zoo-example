package com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QwerRespVO {
    
    private String qwerName;
    
    private Integer qwerAge;
}
