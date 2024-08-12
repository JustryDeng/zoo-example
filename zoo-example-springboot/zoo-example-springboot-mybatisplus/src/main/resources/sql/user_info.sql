DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`
(
    `id`      int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
    `gender`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别（1-男；2-女; 0-未知）',
    `deleted` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;


INSERT INTO `user_info`
VALUES (1, '张三', 1, 0);
INSERT INTO `user_info`
VALUES (2, '李四', 2, 0);
INSERT INTO `user_info`
VALUES (3, '王五', 0, 1);
