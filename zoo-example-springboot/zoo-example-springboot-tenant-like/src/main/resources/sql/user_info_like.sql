DROP TABLE IF EXISTS `user_info_like`;
CREATE TABLE `user_info_like`
(
    `id`      int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `tenant`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '租户值',
    `name`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
    `gender`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别（1-男；2-女; 0-未知）',
    `deleted` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;


INSERT INTO `user_info_like`
VALUES (1, '1,', '张三', 1, 0);
INSERT INTO `user_info_like`
VALUES (2, '1,2,', '李四', 2, 0);
INSERT INTO `user_info_like`
VALUES (3, '1,2,21,', '王五', 1, 0);
INSERT INTO `user_info_like`
VALUES (4, '1,3,', '赵六', 0, 0);
INSERT INTO `user_info_like`
VALUES (5, '1,3,31,', '田七', 1, 0);
