package com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应类
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmpResult<T>   {
    
    /** 标识代码 */
    private String code;
    
    /** 提示信息 */
    private String msg;
    
    /** 返回的数据 */
    private T data;
    
    public static <T> TmpResult<T> success(T data) {
        return new TmpResult<>("000000", "成功", data);
    }
    
    public static <T> TmpResult<T> failure(T data) {
        return new TmpResult<>("100000", "失败", data);
    }
    
}
