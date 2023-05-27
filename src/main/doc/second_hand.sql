/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : second_hand

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 10/04/2023 20:33:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_cart
-- ----------------------------
DROP TABLE IF EXISTS `t_cart`;
CREATE TABLE `t_cart`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NULL DEFAULT NULL,
  `amount` int(11) UNSIGNED NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `is_checked` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_cart
-- ----------------------------
INSERT INTO `t_cart` VALUES (2, 1, 5, 2, 0);
INSERT INTO `t_cart` VALUES (14, 2, 2, 2, NULL);

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` bigint(255) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `pay_status` int(11) NULL DEFAULT NULL COMMENT '0 Not paid，1 Paid',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `product_id` int(11) NULL DEFAULT NULL,
  `amount` int(11) NULL DEFAULT NULL,
  `total_price` decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '总价',
  `delivery_company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流公司(配送方式)',
  `delivery_sn` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `receiver_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人地址',
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `confirm_status` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '确认收货状态：0->未确认；1->已确认',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (20210120001316603, 2, 1, '2021-01-20 00:13:17', '2021-01-20 00:13:17', 2, 1, 3998.00, NULL, NULL, '2', '1', '2', NULL, 1);
INSERT INTO `t_order` VALUES (20210120001502815, 2, 1, '2021-01-20 00:15:03', '2021-01-20 00:15:03', 2, 1, 3998.00, NULL, NULL, '2', '221', '2', NULL, 1);
INSERT INTO `t_order` VALUES (20210120001502849, 2, 1, '2021-01-20 00:15:03', '2021-01-20 00:15:03', 1, 1, 805900.00, NULL, NULL, '2', '221', '2', NULL, 1);
INSERT INTO `t_order` VALUES (20210120002417875, 2, 1, '2021-01-20 00:24:17', '2021-01-20 00:24:17', 2, 1, 3998.00, NULL, NULL, '2', '22115', '2', NULL, 1);
INSERT INTO `t_order` VALUES (20210120002949019, 2, 1, '2021-01-20 00:29:50', '2021-01-20 00:29:50', 2, 1, 3998.00, NULL, NULL, '2', '2', '2', NULL, 0);
INSERT INTO `t_order` VALUES (20210120145924066, 2, 0, '2021-01-20 14:59:25', '2021-01-20 14:59:25', 2, 1, 3998.00, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_order` VALUES (20210122002658134, 2, 0, '2021-01-22 00:26:59', '2021-01-22 00:26:59', 2, 2, 7996.00, NULL, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `type_id` int(11) NULL DEFAULT NULL,
  `type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `amount` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '库存',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES (1, '暗影精灵5', 805900.00, 4, '电子产品', '游戏本电脑1', '/uploadFile/timg.jpg', 2, '2021-02-13 17:05:56', 2);
INSERT INTO `t_product` VALUES (2, '华硕笔记本', 3998.00, 4, '电子产品', '没怎么用过，99新', '/uploadFile/79f0f736afc37931418010e8e0c4b74542a91102.jpg', 0, '2021-01-22 22:56:48', 2);

-- ----------------------------
-- Table structure for t_type
-- ----------------------------
DROP TABLE IF EXISTS `t_type`;
CREATE TABLE `t_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_type
-- ----------------------------
INSERT INTO `t_type` VALUES (1, '学习工具', NULL);
INSERT INTO `t_type` VALUES (2, '生活用品', NULL);
INSERT INTO `t_type` VALUES (3, '体育器材', NULL);
INSERT INTO `t_type` VALUES (4, '电子产品', NULL);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nickname` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `role_type` int(11) NULL DEFAULT NULL COMMENT '用户类别',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birthday` date NULL DEFAULT NULL,
  `gender` int(11) NULL DEFAULT NULL,
  `hpic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (5, 'ceshu1223', 'e10adc3949ba59abbe56e057f20f883e', '', NULL, 1, '', NULL, 1, '/uploadFile/psb.jpg');
INSERT INTO `t_user` VALUES (6, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 21, 0, '', NULL, 1, '');
INSERT INTO `t_user` VALUES (7, 'admin2', '123456', '2', NULL, 1, '2', NULL, 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
