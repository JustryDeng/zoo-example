package com.ideaaedi.zoo.example.springboot.shardingsphere.entity.po;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DemoPO {
    private Long userId;
    private String userName;
    private Long transId;
    private String transDate;
    private String transRemark;
    private Long payId;
    private String payDate;
    private BigDecimal payAmount;
}
