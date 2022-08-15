/*
 Navicat Premium Data Transfer

 Source Server         : Dbcon
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : car

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 25/11/2021 23:10:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customerbooking
-- ----------------------------
DROP TABLE IF EXISTS `customerbooking`;
CREATE TABLE `customerbooking`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `common_status` int(11) NULL DEFAULT NULL,
  `booking_id` bigint(20) NULL DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK7v8jclt83e7yhlivuc56ppmd6`(`booking_id`) USING BTREE,
  INDEX `FKjlx5t10icf9enfcejld9qbhn8`(`customer_id`) USING BTREE,
  CONSTRAINT `FK7v8jclt83e7yhlivuc56ppmd6` FOREIGN KEY (`booking_id`) REFERENCES `slotbooking` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKjlx5t10icf9enfcejld9qbhn8` FOREIGN KEY (`customer_id`) REFERENCES `customeregistration` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customeregistration
-- ----------------------------
DROP TABLE IF EXISTS `customeregistration`;
CREATE TABLE `customeregistration`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `common_status` int(11) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `first_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `last_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `mobile` int(11) NULL DEFAULT NULL,
  `nic` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `vehicle_no` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customeregistration
-- ----------------------------
INSERT INTO `customeregistration` VALUES (4, 'no,132/A,watinapaha rd,Dewalapola', 2, 'niro123@gmail.com', 'sudu', 'niro', 741580006, '953559185V', 'KH-7209');
INSERT INTO `customeregistration` VALUES (5, 'string', 0, 'abcd@gmail.com', 'lakkkkkkkkk', 'string', 703943671, '973550181V', 'string');
INSERT INTO `customeregistration` VALUES (6, 'string', 0, 'abcd@gmail.com', 'lakkkkkkkkk', 'string', 703943671, '973550151V', 'string');

-- ----------------------------
-- Table structure for customermaintenance
-- ----------------------------
DROP TABLE IF EXISTS `customermaintenance`;
CREATE TABLE `customermaintenance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `common_status` int(11) NULL DEFAULT NULL,
  `customer_id` bigint(20) NULL DEFAULT NULL,
  `maintenance_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKahdvpfe35rm83l2k40r1l5vso`(`customer_id`) USING BTREE,
  INDEX `FK605b4s3fmkk8q2f3779nf0ikg`(`maintenance_id`) USING BTREE,
  CONSTRAINT `FK605b4s3fmkk8q2f3779nf0ikg` FOREIGN KEY (`maintenance_id`) REFERENCES `maintenancedetails` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKahdvpfe35rm83l2k40r1l5vso` FOREIGN KEY (`customer_id`) REFERENCES `customeregistration` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for maintenancedetails
-- ----------------------------
DROP TABLE IF EXISTS `maintenancedetails`;
CREATE TABLE `maintenancedetails`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `common_status` int(11) NULL DEFAULT NULL,
  `maintenance_type` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `other_description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `vehicle_no` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of maintenancedetails
-- ----------------------------
INSERT INTO `maintenancedetails` VALUES (1, 2, NULL, 'on others', 'KH-7209');
INSERT INTO `maintenancedetails` VALUES (2, 2, NULL, 'on others', 'KH-7209');
INSERT INTO `maintenancedetails` VALUES (3, 2, NULL, 'on others', 'KH-7209');
INSERT INTO `maintenancedetails` VALUES (4, 2, NULL, 'on others', 'KH-7209');
INSERT INTO `maintenancedetails` VALUES (5, 2, NULL, 'on others', 'KH-7209');
INSERT INTO `maintenancedetails` VALUES (6, 2, NULL, 'on others', 'KH-7209');
INSERT INTO `maintenancedetails` VALUES (7, 2, 'Full', 'on others', 'KH-7209');

-- ----------------------------
-- Table structure for maintenancetype
-- ----------------------------
DROP TABLE IF EXISTS `maintenancetype`;
CREATE TABLE `maintenancetype`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `common_status` int(11) NULL DEFAULT NULL,
  `maintenacne_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of maintenancetype
-- ----------------------------
INSERT INTO `maintenancetype` VALUES (1, 2, 'Full');

-- ----------------------------
-- Table structure for repair
-- ----------------------------
DROP TABLE IF EXISTS `repair`;
CREATE TABLE `repair`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `common_status` int(11) NULL DEFAULT NULL,
  `repair_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `maintenance_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK1ysbpey8l9uvsve13giqfgtwa`(`maintenance_id`) USING BTREE,
  CONSTRAINT `FK1ysbpey8l9uvsve13giqfgtwa` FOREIGN KEY (`maintenance_id`) REFERENCES `maintenancedetails` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of repair
-- ----------------------------
INSERT INTO `repair` VALUES (1, 1, 'oil and filters', NULL);

-- ----------------------------
-- Table structure for slot
-- ----------------------------
DROP TABLE IF EXISTS `slot`;
CREATE TABLE `slot`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `common_status` int(11) NULL DEFAULT NULL,
  `slotname` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `time_period_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK1gku87lucokg48cpxg84x3578`(`time_period_id`) USING BTREE,
  CONSTRAINT `FK1gku87lucokg48cpxg84x3578` FOREIGN KEY (`time_period_id`) REFERENCES `timeperiod` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of slot
-- ----------------------------
INSERT INTO `slot` VALUES (1, 0, 'oil and filters', '1-3', NULL);

-- ----------------------------
-- Table structure for slotbooking
-- ----------------------------
DROP TABLE IF EXISTS `slotbooking`;
CREATE TABLE `slotbooking`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `booking_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `common_status` int(11) NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `vehicle_no` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `slot_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKs40f95h5t6rptoxvca6xhb4a0`(`slot_id`) USING BTREE,
  CONSTRAINT `FKs40f95h5t6rptoxvca6xhb4a0` FOREIGN KEY (`slot_id`) REFERENCES `slot` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of slotbooking
-- ----------------------------
INSERT INTO `slotbooking` VALUES (8, '20', 0, '2021-02-19', 'KH-7206', NULL);

-- ----------------------------
-- Table structure for timeperiod
-- ----------------------------
DROP TABLE IF EXISTS `timeperiod`;
CREATE TABLE `timeperiod`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `common_status` int(11) NULL DEFAULT NULL,
  `time_code` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `time_from` time(0) NULL DEFAULT NULL,
  `time_to` time(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for userlogin
-- ----------------------------
DROP TABLE IF EXISTS `userlogin`;
CREATE TABLE `userlogin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `common_status` int(11) NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `user_role` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userlogin
-- ----------------------------
INSERT INTO `userlogin` VALUES (13, 0, '$2a$10$Q7V3pNBBpRrB5IZxk76BBeK9bdG1JUmyIJ2aPBHLh3Bpjhpcrm4eS', 'chathura', 0);
INSERT INTO `userlogin` VALUES (14, 1, '$2a$10$kWtQ9npu4XgpXWQSJai8C.deplIg/5pn.RMNeyj73EIZBd1QC330i', 'sachitha', 1);
INSERT INTO `userlogin` VALUES (15, 0, '$2a$10$IROlrMmcdz48dOi2a1TRo.3s94YpT3D7/VqtP2ZMjWIVB.CcG99Ha', 'hasanka', 0);
INSERT INTO `userlogin` VALUES (16, 0, '$2a$10$SfG5klv.rB1gTLM/lBe3x.P4Fv0Tss7Uygbo0pxopG6OVr2lEqwby', 'lakshan', 2);
INSERT INTO `userlogin` VALUES (17, 0, '$2a$10$ZS1mTUSMt/YOXPtCE7jgHujg//OvfZRGlHiPjcvGgil.L2HD3/I02', 'hasitha', 2);

SET FOREIGN_KEY_CHECKS = 1;
