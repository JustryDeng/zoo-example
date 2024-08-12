package com.ideaaedi.zoo.example.springboot.dynamic.datasource.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 交易记录表
 * </p>
 *
 * @author mybatis-plus generator
 * @since 2022-10-06 21:18:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("trans_record")
public class TransRecordPO {

    /**
     * id
     */
    @TableId("id")
    private Long id;
    
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 交易日期（yyyy-MM-dd）
     */
    @TableField("trans_date")
    private String transDate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}
