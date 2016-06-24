#创建数据库
CREATE DATABASE tinyurl;

USE tinyurl;
#定义短网址表结构
create table tinyurl(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '短网址ID',
  url VARCHAR(1000) NOT NULL COMMENT '长网址',
  PRIMARY KEY (id),
  KEY idx_url(url),
  UNIQUE u_url(url)
)ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=UTF8 COMMENT='生成短网址ID号';

USE tinyurl;
#定义用户表结构
CREATE TABLE user(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(50) NOT NULL COMMENT '用户名',
  password VARCHAR(50) NOT NULL COMMENT '密码',
  token VARCHAR(50) NOT NULL COMMENT '用户口令',
  PRIMARY KEY (id),
  KEY idx_username(username),
  UNIQUE u_user(username, password),
  KEY idx_token(token)
)ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHAR SET=UTF8 COMMENT='用户表';

