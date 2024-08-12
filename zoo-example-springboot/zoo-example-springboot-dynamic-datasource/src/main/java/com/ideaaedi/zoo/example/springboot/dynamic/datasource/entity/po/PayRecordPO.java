package com.ideaaedi.zoo.example.springboot.dynamic.datasource.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 支付记录表
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 2023-05-06 14:20:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pay_record")
public class PayRecordPO {

    /**
     * id
     */
    @TableId("id")
    private Long id;
    
    /**
     * 交易id
     */
    @TableField("trans_id")
    private Long transId;
    
    /**
     * 支付日期（yyyy-MM-dd）
     */
    @TableField("pay_date")
    private String payDate;

    /**
     * 支付金额
     */
    @TableField("amount")
    private BigDecimal amount;

}
