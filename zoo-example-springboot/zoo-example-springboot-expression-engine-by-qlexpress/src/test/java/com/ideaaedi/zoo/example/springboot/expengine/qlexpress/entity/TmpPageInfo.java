package com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页信息模型
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Data
public class TmpPageInfo<T> implements Serializable {
    
    /** 总条数 */
    private long total;
    
    /** 页码 */
    private int pageNum;
    
    /** 每页条数 */
    private int pageSize;
    
    /** 数据集 */
    private List<T> dataList;
    
    /**
     * 快速构造
     */
    public static <T> TmpPageInfo<T> of(long total, int pageNum, int pageSize) {
        return TmpPageInfo.of(total, pageNum, pageSize, null);
    }
    
    /**
     * 快速构造
     */
    public static <T> TmpPageInfo<T> of(long total, int pageNum, int pageSize, List<T> dataList) {
        TmpPageInfo<T> pageInfo = new TmpPageInfo<>();
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setDataList(dataList);
        return pageInfo;
    }
}
