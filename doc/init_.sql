create database if not exists tea_shop_database;

use tea_shop_database;

-- 文件表
create table if not exists sys_file(
    id               varchar(255) primary key not null comment 'id',
    create_time      datetime    null,
    update_time      datetime    null,
    delete_flag      varchar(255) null,
    create_user      varchar(255) null,
    update_user      varchar(255) null,
    path       varchar(255) null comment '保存路径'
) comment '文件';

-- TODO 茶文化图文

-- 轮播图表
CREATE TABLE sys_carousel (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    image_url VARCHAR(255) NULL COMMENT '轮播图图片URL',
    link_url VARCHAR(255) NULL COMMENT '跳转链接URL',
    is_show BOOLEAN NULL COMMENT '是否显示',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    delete_flag VARCHAR(255) NULL COMMENT '删除标识',
    create_user VARCHAR(255) NULL COMMENT '创建用户',
    update_user VARCHAR(255) NULL COMMENT '更新用户'
) COMMENT = 'sys_轮播图表，用于存储首页的轮播图信息';

-- 茶叶分类表
CREATE TABLE sys_category (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    category_name VARCHAR(255) NULL COMMENT '分类名称',
    description VARCHAR(255) NULL COMMENT '分类描述',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    delete_flag VARCHAR(255) NULL COMMENT '删除标识',
    create_user VARCHAR(255) NULL COMMENT '创建用户',
    update_user VARCHAR(255) NULL COMMENT '更新用户'
) COMMENT = 'sys_茶叶分类表，用于存储茶叶分类信息';

-- 商品表
CREATE TABLE sys_product (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    product_name VARCHAR(255) NULL COMMENT '商品名称',
    description TEXT NULL COMMENT '商品描述',
    price DECIMAL(10, 2) NULL COMMENT '商品价格',
    stock INT NULL COMMENT '商品库存',
    category_id VARCHAR(255) NULL COMMENT '商品分类ID',
    image_url VARCHAR(255) NULL COMMENT '商品图片URL',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    delete_flag VARCHAR(255) NULL COMMENT '删除标识',
    create_user VARCHAR(255) NULL COMMENT '创建用户',
    update_user VARCHAR(255) NULL COMMENT '更新用户',
    FOREIGN KEY (category_id) REFERENCES sys_category(id)
) COMMENT = 'sys_商品表，用于存储商品信息';

-- 用户表
CREATE TABLE sys_users (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    username VARCHAR(255) NULL COMMENT '用户名',
    password VARCHAR(255) NULL COMMENT '密码',
    email VARCHAR(255) NULL COMMENT '邮箱',
    phone_number VARCHAR(255) NULL COMMENT '手机号',
    profile_picture_url VARCHAR(255) NULL COMMENT '个人头像URL',
    wallet_balance DECIMAL(10, 2) NULL COMMENT '钱包余额',
    role VARCHAR(255) NULL COMMENT '用户角色',
    points INT NULL COMMENT '积分',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    delete_flag VARCHAR(255) NULL COMMENT '删除标识',
    create_user VARCHAR(255) NULL COMMENT '创建用户',
    update_user VARCHAR(255) NULL COMMENT '更新用户'
) COMMENT = 'sys_用户表，用于存储用户信息';

-- 积分记录表TODO 是否有必要存在
CREATE TABLE sys_points_record (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    user_id VARCHAR(255) NULL COMMENT '用户ID',
    points INT NULL COMMENT '积分数量',
    reason VARCHAR(255) NULL COMMENT '积分来源',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES sys_users(id)
) COMMENT = 'sys_积分表，用于存储积分明细信息';

-- 订单表
CREATE TABLE sys_orders (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    user_id VARCHAR(255) NULL COMMENT '用户ID',
    order_status VARCHAR(255) NULL COMMENT '订单状态（未支付、已支付等）',
    total_amount DECIMAL(10, 2) NULL COMMENT '订单总金额',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    delete_flag VARCHAR(255) NULL COMMENT '删除标识',
    create_user VARCHAR(255) NULL COMMENT '创建用户',
    update_user VARCHAR(255) NULL COMMENT '更新用户',
    FOREIGN KEY (user_id) REFERENCES sys_users(id)
) COMMENT = 'sys_订单表，用于存储订单信息';

-- 订单详情表
CREATE TABLE sys_order_items (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    order_id VARCHAR(255) NULL COMMENT '订单ID',
    product_id VARCHAR(255) NULL COMMENT '商品ID',
    quantity INT NULL COMMENT '商品数量',
    unit_price DECIMAL(10, 2) NULL COMMENT '商品单价',
    total_price DECIMAL(10, 2) NULL COMMENT '商品总价',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    FOREIGN KEY (order_id) REFERENCES sys_orders(id),
    FOREIGN KEY (product_id) REFERENCES sys_product(id)
) COMMENT = 'sys_订单详情表，用于存储订单中的商品详情';

-- 购物车表
CREATE TABLE sys_cart (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    user_id VARCHAR(255) NULL COMMENT '用户ID',
    product_id VARCHAR(255) NULL COMMENT '商品ID',
    quantity INT NULL COMMENT '商品数量',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    delete_flag VARCHAR(255) NULL COMMENT '删除标识',
    create_user VARCHAR(255) NULL COMMENT '创建用户',
    update_user VARCHAR(255) NULL COMMENT '更新用户',
    FOREIGN KEY (user_id) REFERENCES sys_users(id),
    FOREIGN KEY (product_id) REFERENCES sys_product(id)
) COMMENT = 'sys_购物车表，用于存储购物车信息';

-- 收货地址表
CREATE TABLE sys_shipping_address (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    user_id VARCHAR(255) NULL COMMENT '用户ID',
    address VARCHAR(255) NULL COMMENT '收货地址',
    phone_number VARCHAR(255) NULL COMMENT '联系电话',
    is_default BOOLEAN NULL COMMENT '是否为默认地址',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    delete_flag VARCHAR(255) NULL COMMENT '删除标识',
    create_user VARCHAR(255) NULL COMMENT '创建用户',
    update_user VARCHAR(255) NULL COMMENT '更新用户',
    FOREIGN KEY (user_id) REFERENCES sys_users(id)
) COMMENT = 'sys_收货地址表，用于存储用户的收货地址信息';

-- 支付记录表
CREATE TABLE sys_payment (
    id VARCHAR(255) PRIMARY KEY NOT NULL COMMENT 'id',
    order_id VARCHAR(255) NULL COMMENT '订单ID',
    payment_method VARCHAR(255) NULL COMMENT '支付方式（微信支付等）',
    payment_status VARCHAR(255) NULL COMMENT '支付状态（成功、失败等）',
    payment_time DATETIME NULL COMMENT '支付时间',
    create_time DATETIME NULL COMMENT '创建时间',
    update_time DATETIME NULL COMMENT '更新时间',
    FOREIGN KEY (order_id) REFERENCES sys_orders(id)
) COMMENT = 'sys_支付记录表，用于存储支付信息';
