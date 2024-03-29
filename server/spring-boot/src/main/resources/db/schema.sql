DROP TABLE IF EXISTS `ysh_rfid`;
CREATE TABLE `ysh_rfid` (
  `uid` VARCHAR(32)NOT NULL  PRIMARY KEY COMMENT '主键',
  `name` VARCHAR(32) NOT NULL UNIQUE COMMENT '用户名',
  `kind` VARCHAR(4) NOT NULL  COMMENT '类别',
  `create_time` DATETIME NOT NULL DEFAULT NOW() COMMENT '创建时间',
  `last_update_time` DATETIME NOT NULL DEFAULT NOW() COMMENT '上次更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Spring Boot Demo Orm 系列示例表';
