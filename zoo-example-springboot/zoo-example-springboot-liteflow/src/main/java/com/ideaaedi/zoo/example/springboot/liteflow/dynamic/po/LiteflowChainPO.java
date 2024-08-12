package com.ideaaedi.zoo.example.springboot.liteflow.dynamic.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ideaaedi.zoo.diy.artifact.liteflow.parser.SQLXmlELParserExt;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 规则
 * <pre>
 *  对应数据库脚本见{@link SQLXmlELParserExt}类注释 或 src/main/resources/sql/liteflow.sql
 * </pre>
 */
@Data
@TableName("liteflow_chain")
public class LiteflowChainPO {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属应用
     */
    @TableField("application_name")
    private String applicationName;

    /**
     * 规则名称（定位chain的标识）
     */
    @TableField("chain_name")
    private String chainName;

    /**
     * 规则描述
     */
    @TableField("chain_desc")
    private String chainDesc;

    /**
     * 规则
     */
    @TableField("el_data")
    private String elData;

    /**
     * 路由
     */
    @TableField("route")
    private String route;

    /**
     * 路由所属命名空间
     */
    @TableField("namespace")
    private String namespace;

    /**
     * 是否启用（1-启用；0-不启用）
     */
    @TableField("enable")
    private Integer enable;

    /**
     * 是否已删除(0-未删除；1-已删除)
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 修改人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 修改时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}