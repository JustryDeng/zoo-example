package com.ideaaedi.zoo.example.springboot.shardingsphere.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息表
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 2023-05-06 14:18:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_info")
public class UserInfoPO {

    /**
     * id
     */
    @TableId("id")
    private Long id;
    
    /**
     * 姓名
     */
    @TableField("`name`")
    private String name;

}
