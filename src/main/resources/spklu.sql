/*
 Navicat Premium Data Transfer

 Source Server         : local mysql
 Source Server Type    : MySQL
 Source Server Version : 100137
 Source Host           : localhost:3306
 Source Schema         : spklu

 Target Server Type    : MySQL
 Target Server Version : 100137
 File Encoding         : 65001

 Date: 24/10/2020 19:08:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for main_form_value
-- ----------------------------
DROP TABLE IF EXISTS `main_form_value`;
CREATE TABLE `main_form_value`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of main_form_value
-- ----------------------------
INSERT INTO `main_form_value` VALUES (1, NULL, NULL, 'pph', '25', NULL);
INSERT INTO `main_form_value` VALUES (2, NULL, NULL, 'inflasi', '3.5', NULL);
INSERT INTO `main_form_value` VALUES (3, NULL, NULL, 'discount_rate', '13', NULL);
INSERT INTO `main_form_value` VALUES (4, NULL, NULL, 'jumlah_kendaraan', '600', NULL);
INSERT INTO `main_form_value` VALUES (5, NULL, NULL, 'biaya_sipil', '150000000', NULL);
INSERT INTO `main_form_value` VALUES (6, NULL, NULL, 'biaya_kelistrikan', '400000000', NULL);
INSERT INTO `main_form_value` VALUES (7, NULL, NULL, 'harga_evse', '800000000', NULL);
INSERT INTO `main_form_value` VALUES (8, NULL, NULL, 'harga_jual_pln', '1.2', NULL);
INSERT INTO `main_form_value` VALUES (9, NULL, NULL, 'harga_jual_konsumen', '1.5', NULL);
INSERT INTO `main_form_value` VALUES (10, NULL, NULL, 'pertumbuhan_kbl', '23', NULL);
INSERT INTO `main_form_value` VALUES (11, NULL, NULL, 'durasi_penggunaan_evse', '20', NULL);
INSERT INTO `main_form_value` VALUES (12, NULL, NULL, 'biaya_sewa_lahan', '0', NULL);
INSERT INTO `main_form_value` VALUES (13, NULL, NULL, 'jumlah_konektor', NULL, NULL);
INSERT INTO `main_form_value` VALUES (14, NULL, NULL, 'daya_maksimum_konektor', NULL, NULL);
INSERT INTO `main_form_value` VALUES (15, NULL, NULL, 'kapasitas_kbl', '25', NULL);
INSERT INTO `main_form_value` VALUES (16, NULL, NULL, 'rugi_dan_daya_pendukung', '10', NULL);
INSERT INTO `main_form_value` VALUES (17, NULL, NULL, 'rasio_spklu', '38', NULL);
INSERT INTO `main_form_value` VALUES (18, NULL, NULL, 'durasi_penggunaan_evse', '20', NULL);
INSERT INTO `main_form_value` VALUES (19, NULL, NULL, 'biaya_sewa_lahan', '0', NULL);

-- ----------------------------
-- Table structure for main_parameter
-- ----------------------------
DROP TABLE IF EXISTS `main_parameter`;
CREATE TABLE `main_parameter`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `key` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of main_parameter
-- ----------------------------
INSERT INTO `main_parameter` VALUES (1, NULL, NULL, 'pkl', '1.0', 'double', 'param ? Populasi Kendaraan Listrik');
INSERT INTO `main_parameter` VALUES (2, NULL, NULL, 'ieikAdd', '550000.0', 'double', 'param Infrastructure Expense (in kIDR)');
INSERT INTO `main_parameter` VALUES (3, NULL, NULL, 'ieikDiv', '1000.0', 'double', 'param Infrastructure Expense (in kIDR)');
INSERT INTO `main_parameter` VALUES (4, NULL, NULL, 'bo', '0.02', 'double', 'param Biaya Operasional *)');
INSERT INTO `main_parameter` VALUES (5, NULL, NULL, 'bp', '0.02', 'double', 'param Biaya Pemasaran');
INSERT INTO `main_parameter` VALUES (6, NULL, NULL, 'btt', '100000.0', 'double', 'param Biaya Tak Terduga');
INSERT INTO `main_parameter` VALUES (7, NULL, NULL, 'keh', '1.0', 'double', 'param Kebutuhan Energi harian (KWH)');
INSERT INTO `main_parameter` VALUES (8, NULL, NULL, 'heMtp', '0.707', 'double', 'param ? Harga Energi (Rp/KwH)');
INSERT INTO `main_parameter` VALUES (9, NULL, NULL, 'heMin', '1.0', 'double', 'param ? Harga Energi (Rp/KwH)');
INSERT INTO `main_parameter` VALUES (10, NULL, NULL, 'heMtpN', '0.035', 'double', 'param ? Harga Energi (Rp/KwH)');
INSERT INTO `main_parameter` VALUES (11, NULL, NULL, 'bet', '365.0', 'double', 'param Biaya Energy Tahunan');
INSERT INTO `main_parameter` VALUES (12, NULL, NULL, 'bos', '57600.0', 'double', 'param Biaya Operasi SPKLU, Gaji Pegawai Pertahun');
INSERT INTO `main_parameter` VALUES (13, NULL, NULL, 'pkhg', '1.0', 'double', 'param ? Populasi Kendaraan Hybrid (Gaikindo)');
INSERT INTO `main_parameter` VALUES (14, NULL, NULL, 'bc', '1.65', 'double', 'param ? Biaya Charging');
INSERT INTO `main_parameter` VALUES (15, NULL, NULL, 'tei', '1.0', 'double', 'param Total Expense (inflated)');
INSERT INTO `main_parameter` VALUES (16, NULL, NULL, 'pl', '365.0', 'double', 'param Pendapatan Layanan');
INSERT INTO `main_parameter` VALUES (17, NULL, NULL, 'ri', '1.0', 'double', 'param Revenue (inflated)');
INSERT INTO `main_parameter` VALUES (18, NULL, NULL, 'dcs', '1.0', 'double', 'param Discounted Cash Flow');
INSERT INTO `main_parameter` VALUES (19, NULL, NULL, 'pi', '0.05', 'double', 'param Profitability Index');
INSERT INTO `main_parameter` VALUES (20, NULL, NULL, 'bep', '1.0', 'double', 'param Break Event Point (BEP)');
INSERT INTO `main_parameter` VALUES (21, NULL, NULL, 'dirTemp', '/tmp/com.bppt.spklu/', 'String', 'location save temp file');

-- ----------------------------
-- Table structure for main_status
-- ----------------------------
DROP TABLE IF EXISTS `main_status`;
CREATE TABLE `main_status`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for main_user
-- ----------------------------
DROP TABLE IF EXISTS `main_user`;
CREATE TABLE `main_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `status_id` int(11) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `bod` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `username`(`username`, `status_id`) USING BTREE,
  INDEX `status_id`(`status_id`) USING BTREE,
  CONSTRAINT `main_user_ibfk_1` FOREIGN KEY (`status_id`) REFERENCES `main_status` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tran_tracking
-- ----------------------------
DROP TABLE IF EXISTS `tran_tracking`;
CREATE TABLE `tran_tracking`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `ip_address` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `request_uri` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `request_body` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  `user` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
