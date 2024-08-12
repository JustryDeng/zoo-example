package com.ideaaedi.zoo.example.springboot.tenantlike.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("user_info_like")
public class UserInfoLikePO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    @TableField("tenant")
    private String tenant;
    
    @TableField("name")
    private String name;
    
    @TableField("gender")
    private Integer gender;
    
    /*
     * 是否已删除
     */
    @TableLogic(delval = "1", value = "0")
    private Integer deleted;
    
}
