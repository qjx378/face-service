-- ----------------------------
-- 1.人脸库表
-- ----------------------------
DROP TABLE IF EXISTS `face_set`;
CREATE TABLE `face_set`
(
    `id`                bigint(32)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `face_set_token`    varchar(128) NULL DEFAULT NULL COMMENT '人脸库的标识，系统自动生成',
    `outer_id`          varchar(256) NULL DEFAULT NULL COMMENT '自定义标识',
    `display_name`      varchar(256) NULL DEFAULT NULL COMMENT '人脸库的名字',
    `face_set_capacity` bigint(16)   NULL DEFAULT 10000 COMMENT '人脸库支持的最大库容，默认10000',
    `face_number`       bigint(16)   NULL DEFAULT 0 COMMENT '人脸库当中的人脸数量',
    `del_flag`          int(11)      NULL DEFAULT 0 COMMENT '删除标记（0：正常; 1：删除）',
    `remarks`           varchar(256) NULL DEFAULT NULL COMMENT '人脸库备注',
    `created_time`      datetime     NULL DEFAULT NULL COMMENT '创建时间',
    `updated_time`      timestamp    NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_face_set_token` (`face_set_token`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '人脸库';

-- ----------------------------
-- 2.人脸库资源表
-- ----------------------------
DROP TABLE IF EXISTS `face_set_resource`;
CREATE TABLE `face_set_resource`
(
    `id`                  bigint(16)    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `face_set_id`         bigint(16)    NULL DEFAULT NULL COMMENT '特征库ID',
    `face_token`          varchar(32)   NULL DEFAULT NULL COMMENT '人脸标识',
    `face_feature`        text          NULL DEFAULT NULL COMMENT '人脸特征值，已进行二进制Base64编码',
    `original_image_path` varchar(1024) NULL DEFAULT NULL COMMENT '原图路径',
    `original_image_hash` varchar(64)   NULL DEFAULT NULL COMMENT '原图hash值（图片文件sha256）',
    `face_image_path`     varchar(1024) NULL DEFAULT NULL COMMENT '人脸裁剪图路径',
    `del_flag`            int(11)       NULL DEFAULT 0 COMMENT '删除标记（0：正常; 1：删除）',
    `created_time`        datetime      NULL DEFAULT NULL COMMENT '创建时间',
    `updated_time`        timestamp     NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_face_token` (`face_token`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '人脸库资源';