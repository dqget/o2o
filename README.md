[TOC]

# 鲜花销售微信公众号

## 一、描述

基于springboot框架的鲜花销售微信公众号

## 二、项目运行环境及配置

项目开发时IDE使用的是IDEA，MySQL管理和设计工具使用的是Navicat，使用Redis Desktop Manager管理Redis内存数据库，使用Xshell管理远程服务器。下面是项目开发和运行时所需要的环境或配置：

JDK 1.8

MySQL 8.0

Redis 5.0.0

Maven 3.5.4

远程服务器（若不使用远程服务器启动项目，微信用户、支付等功能将不能使用）

微信公众号 appid

微信公众号 appsecret

蚂蚁金服 app_id

蚂蚁金服用户私钥 merchant_private_key

蚂蚁金服支付宝公钥 alipay_public_key

## 三、项目启动和源码地址

项目使用Spring Boot自带的Tomcat启动，无需下载新的Tomcat，Spring Boot启动类是com.lovesickness.o2o.O2oApplication

使用git进行项目的版本控制，现已开源，github地址：https://github.com/dqget/o2o

## 四、项目API文档

项目使用Swagger生成在线API控制台。

项目启动后，API文档地址为：http://localhost:8080/o2o/swagger-ui.html