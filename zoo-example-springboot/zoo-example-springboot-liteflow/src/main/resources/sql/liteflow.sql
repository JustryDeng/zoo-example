CREATE TABLE `liteflow_chain`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `application_name` varchar(32) NOT NULL COMMENT '所属应用',
  `chain_name` varchar(32) NOT NULL COMMENT '规则名称（定位chain的标识）',
  `chain_desc` varchar(64) NULL DEFAULT NULL COMMENT '规则描述',
  `el_data` text NOT NULL COMMENT '规则',
  `route` text NULL COMMENT '路由',
  `namespace` varchar(32) NULL DEFAULT NULL COMMENT '路由所属命名空间',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用（1-启用；0-不启用）',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已删除(0-未删除；1-已删除)',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_by` bigint(20) NOT NULL COMMENT '修改人',
  `updated_at` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `application_name_idx`(`application_name` ASC) USING BTREE,
  INDEX `chain_name_idx`(`chain_name` ASC) USING BTREE,
  INDEX `enable_idx`(`enable` ASC) USING BTREE,
  INDEX `namespace_idx`(`namespace` ASC) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;


CREATE TABLE `liteflow_script`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `application_name` varchar(32) NOT NULL COMMENT '所属应用',
  `script_id` varchar(32) NOT NULL COMMENT '脚本id（定位node的标识）',
  `script_name` varchar(64) NULL DEFAULT NULL COMMENT '脚本名称',
  `script_data` text NOT NULL COMMENT '脚本',
  -- 支持哪些脚本类型，详见源码 https://github.com/dromara/liteflow/blob/master/liteflow-core/src/main/resources/dtd/liteflow.dtd
  `script_type` enum('script','boolean_script','switch_script','for_script') NOT NULL COMMENT '脚本类型（script|boolean_script|switch_script|for_script）',
  -- 支持哪些脚本语言，详见源码 https://github.com/dromara/liteflow/blob/master/liteflow-core/src/main/resources/dtd/liteflow.dtd
  `script_language` enum('qlexpress','groovy','js','python','lua','aviator','java','kotlin') NOT NULL COMMENT '脚本语言（qlexpress|groovy|js|python|lua|aviator|java|kotlin）',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用（1-启用；0-不启用）',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已删除(0-未删除；1-已删除)',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_by` bigint(20) NOT NULL COMMENT '修改人',
  `updated_at` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `application_name_idx`(`application_name` ASC) USING BTREE,
  INDEX `script_id_idx`(`script_id` ASC) USING BTREE,
  INDEX `script_type_idx`(`script_type` ASC) USING BTREE,
  INDEX `script_language_idx`(`script_language` ASC) USING BTREE,
  INDEX `enable_idx`(`enable` ASC) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

