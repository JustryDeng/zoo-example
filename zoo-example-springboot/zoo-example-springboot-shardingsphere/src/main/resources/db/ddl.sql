DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
                              `id` bigint(20) UNSIGNED NOT NULL COMMENT 'id',
                              `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- 相关真实表需要提前创建好才行
-- 注：ShardingSphere-JDBC不提供动态管理节点的能力；需要的话，官方推荐使用 ShardingSphere-PROXY的DistSQL动态管理节点
CREATE TABLE IF NOT EXISTS `trans_record_202401` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202401` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202402` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202402` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202403` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202403` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202404` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202404` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202405` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202405` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202406` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202406` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202407` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202407` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202408` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202408` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202409` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202409` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202410` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202410` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202411` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202411` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `trans_record_202412` (
                                                     `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                     `user_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '用户id',
                                                     `trans_date` VARCHAR ( 10 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易日期（yyyy-MM-dd）',
                                                     `remark` VARCHAR ( 100 ) CHARACTER
                                                         SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易记录表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `pay_record_202412` (
                                                   `id` BIGINT ( 20 ) UNSIGNED NOT NULL COMMENT 'id',
                                                   `pay_date` VARCHAR ( 10 ) CHARACTER
                                                       SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付日期（yyyy-MM-dd）',
                                                   `trans_id` BIGINT ( 20 ) NULL DEFAULT NULL COMMENT '交易id',
                                                   `amount` DECIMAL ( 10, 2 ) NULL DEFAULT NULL COMMENT '支付金额',
                                                   PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

