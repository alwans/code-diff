/*
 Navicat Premium Data Transfer

 Source Server         : 158
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 127.0.0.1:3306
 Source Schema         : test_platform

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 24/09/2021 10:56:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for coverage_app
-- ----------------------------
DROP TABLE IF EXISTS `coverage_app`;
CREATE TABLE `coverage_app`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `project_id` int(10) NOT NULL COMMENT '项目id',
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '收集状态：收集中：未收集',
  `host` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务器ip',
  `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'jacoco端口',
  `is_disable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用',
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coverage_report
-- ----------------------------
DROP TABLE IF EXISTS `coverage_report`;
CREATE TABLE `coverage_report`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `project_id` int(5) NOT NULL COMMENT '项目id',
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '自动生成的uuid',
  `report_type` tinyint(1) NULL DEFAULT NULL COMMENT '报告类型：0：全量，1：增量',
  `diff_type` int(11) NULL DEFAULT NULL COMMENT 'diff类型：0：分支diff 1：commit diff',
  `is_used` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否正被启用：0：未被使用，1：被使用',
  `old_branch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基线分支',
  `new_branch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前分支',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `add_time` datetime(0) NOT NULL COMMENT '创建时间',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for diff_project_info
-- ----------------------------
DROP TABLE IF EXISTS `diff_project_info`;
CREATE TABLE `diff_project_info`  (
  `id` bigint(5) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `repo_id` int(5) NOT NULL COMMENT '仓库id',
  `env` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '环境名',
  `project_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属项目组名',
  `project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名',
  `project_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目仓库地址',
  `collect_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '收集状态：0：未收集，1：收集中，2：暂停收集',
  `report_status` tinyint(1) NOT NULL COMMENT '报告状态：0：未生成，1：生成中，2：生成成功，3：生成失败',
  `is_disable` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否启用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `add_time` datetime(0) NOT NULL COMMENT '添加时间',
  `last_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for diff_repo_info
-- ----------------------------
DROP TABLE IF EXISTS `diff_repo_info`;
CREATE TABLE `diff_repo_info`  (
  `id` bigint(5) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `depot_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '仓库名',
  `depot_type` int(2) NOT NULL COMMENT '仓库类型：0:SVN, 1:GIT',
  `depot_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '仓库地址：例127.0.0.1:888',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
  `passwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `is_disable` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否启用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `add_time` datetime(0) NOT NULL COMMENT '创建时间',
  `last_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for diff_result_info
-- ----------------------------
DROP TABLE IF EXISTS `diff_result_info`;
CREATE TABLE `diff_result_info`  (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `project_id` int(5) NOT NULL COMMENT '项目id',
  `diff_type` int(1) NOT NULL COMMENT 'diff类型：0:分支diff，1:commit diff',
  `commit_branch` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'commit对应的分支名',
  `old_branch` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对比分支名',
  `new_branch` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标分支名',
  `old_commit_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对比commitId',
  `new_commit_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标commitId',
  `diff_result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'diff结果',
  `add_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
