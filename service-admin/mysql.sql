CREATE DATABASE `bank1` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

CREATE TABLE `account_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '户主姓名',
  `account_no` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '银行卡号',
  `account_password` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '帐户密码',
  `account_balance` double DEFAULT NULL COMMENT '帐户余额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;
INSERT INTO `account_info` VALUES (2, '张三的账户', '1', '', 10000);