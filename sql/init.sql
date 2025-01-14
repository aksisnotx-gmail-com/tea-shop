drop database if exists tea_shop_database;

create database if not exists tea_shop_database;

use tea_shop_database;

create table sys_banner
(
    banner_url  varchar(255) null comment '轮播图',
    create_time timestamp    null,
    update_time timestamp    null,
    id          varchar(255) not null
        primary key
)
    comment '轮播图';

create table if not exists sys_file
(
    id          varchar(255) not null
        primary key,
    path        varchar(255) null comment '保存路径',
    create_time timestamp    null comment '创建时间',
    update_time timestamp    null comment '修改时间',
    url         varchar(255) null comment '下载url'
)
    comment '文件';

create table sys_order
(
    id               varchar(255) not null
        primary key,
    create_time      timestamp    null,
    update_time      timestamp    null,
    state            varchar(255) null comment '订单状态',
    order_number     varchar(255) null comment '下单编号',
    user_id          varchar(255) null,
    delivery_address varchar(255) null comment '配送地址',
    products   text         null comment '本次订单所有的商品信息',
    is_evaluate      int          null comment '是否评价了 1 已评价 -1 未处理商品 0 未评价 ',
    number       int          null comment 'sku数量',
    total_price      decimal      null comment '总价格',
    remark           varchar(500) null comment '备注'
)
    comment '订单列表有外键';

create table sys_product_comment
(
    id              varchar(255) not null comment 'id'
        primary key,
    star_rating     int          null comment '星级',
    user_id         varchar(255) null comment '用户ID',
    comment_content text         null comment '评论内容',
    comment_img_url varchar(255) null comment '评论图片json形式为多张图片',
    create_time     timestamp    null comment '创建时间',
    update_time     timestamp    null comment '更新时间',
    order_id        varchar(255) null comment '订单ID'
)
    comment '商品评论';

create table sys_product_details(
    id               varchar(255) not null comment 'id' primary key,
    carousel         varchar(255) null comment '轮播图来自于sku',
    product_name     varchar(255) null,
    price           decimal      null comment '价格',
    special_price     decimal          null comment '特惠价格',
    stock           int          null comment '库存',
    is_popular    int          null comment '是否热销，1 是 0 不是',
    is_special    int          null comment '是否特惠，1 是 0 不是',
    create_time      timestamp    null comment '创建时间',
    update_time      timestamp    null,
    product_type_ids text null comment '商品类型ID'
)
    comment '商品详情有外键';

create table sys_product_type
(
    type        varchar(255) null comment '类型',
    img_url     varchar(255) null comment '图片',
    id          varchar(255) not null comment 'id'
        primary key,
    update_time timestamp    null comment '更新时间',
    create_time timestamp    null comment '创建时间'
)
    comment '商品分类类型';

create table sys_shopping_cart
(
    id             varchar(255) not null primary key,
    create_time    timestamp    null,
    update_time    timestamp    null,
    product_id varchar(255) null comment '商品id',
    number         int          null comment '数量',
    user_id        varchar(255) null comment '用户ID'
)
    comment '购物车';

create table sys_user
(
    id               varchar(255) not null comment 'id' primary key,
    role             varchar(255) null comment '角色',
    pwd              varchar(255) null comment '密码',
    nickname         varchar(255) null comment '昵称',
    avatar           varchar(255) null comment '头像',
    username         varchar(255) null comment '用户名',
    create_time      timestamp    null,
    update_time      timestamp    null,
    shipping_address text         null comment '收货地址(可能是多个)',
    phone_number     varchar(255) null comment '手机号码',
    gender           int          null comment '0 是男 1是女'
)
    comment '用户表';

create table sys_wallet
(
    id          varchar(255) not null
        primary key,
    balance     decimal      null comment '余额',
    user_id     varchar(255) null comment '用户ID',
    create_time timestamp    null comment '创建时间',
    update_time timestamp    null comment '更新时间'
)
    comment '钱包有外键';

create index sys_wallet_fk
    on sys_wallet (user_id);

create table sys_wallet_record
(
    expenditure decimal      null comment '支出',
    income      decimal      null comment '收入',
    user_id     varchar(255) null comment '用户ID',
    id          varchar(255) not null comment 'id'
        primary key,
    create_time timestamp    null comment '创建时间',
    update_time timestamp    null comment '修改时间'
)
    comment '钱包流水记录';



# 初始分类
insert into sys_product_type(type, id, create_time) values ('花茶','1',NOW()),
                                                           ('红茶','2',NOW()),
                                                           ('白茶','3',NOW()),
                                                           ('生茶','4',NOW()),
                                                           ('熟茶','5',NOW());

# 初始用户
insert into sys_user (id, phone_number,role, pwd, nickname, username, create_time,gender)
values ('1','15156246001','ADMIN','e10adc3949ba59abbe56e057f20f883e','admin','admin',NOW(),1),
       ('2','15156246002','BUYER','e10adc3949ba59abbe56e057f20f883e','test-buyer','test-buyer',NOW(),1);

# 初始化钱包
insert into sys_wallet (id, balance, user_id, create_time, update_time) values ('1', 0, '2', NOW(), NOW());

# 初始化化商品
insert into sys_product_details(carousel,id, product_name, price, special_price, stock, is_popular, is_special, create_time, product_type_ids)
values ('[]','1', '红茶', 250.00, 25, 100, 1, 1, NOW(), '["1","2"]'),
('[]','2', '绿茶',  100.00, 0, 10, 0, 0, NOW(), '["1","2","3"]'),
('[]','3', '西湖龙井',   10000.00, 9999.99, 1, 1, 1, NOW(), '["5"]');

