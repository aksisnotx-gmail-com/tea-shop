package com.app.domain.order.entity;

import cn.hutool.core.util.IdUtil;
import com.app.domain.base.Entity;
import com.app.domain.order.enmus.OrderState;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 订单列表(SysOrder)表实体类
 *
 * @author makejava
 * @since 2024-03-20 22:45:03
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "订单信息")
@TableName(value = "sys_order",autoResultMap = true)
public class OrderEntity extends Entity {

    public static final Integer UN_EVALUATE = 0;

    public static final Integer EVALUATE = 1;

    public static final Integer UN_HANDLER = -1;

    public static final Integer DELETE = 2;

    @Serial
    private static final long serialVersionUID = 6509027597663570020L;

    //订单状态
    @Schema(description = "订单状态")
    private OrderState state;

    //下单编号
    @Schema(description = "下单编号")
    private String orderNumber;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "收货地址")
    private String deliveryAddress;

    @Schema(description = "商品id")
    private String productId;

    @Schema(description = "本次订单所有的商品信息，防止删除/修改出现问题商品,解决处在订单中的商品需要被冻结问题")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProductDetailsEntity product;

    //是否评价(1 已评价 0 未评价 -1 未购买)
    @Schema(description = "是否评价(1 已评价 0 未评价 -1 表示未处理商品评价 2 已删除 )")
    private Integer isEvaluate;

    @Schema(description = " 本次订单的商品数量")
    private Integer number;

    @Schema(description = "总价")
    private BigDecimal totalPrice;

    @Schema(description = "备注")
    private String remark;

    public static OrderEntity create(OrderState state,
                                     String userId,
                                     String deliveryAddress,
                                     ProductDetailsEntity product,
                                     Integer number,
                                     BigDecimal totalPrice,
                                     String remark) {
        OrderEntity order = new OrderEntity();
        order.setState(state);
        order.setOrderNumber(IdUtil.simpleUUID());
        order.setUserId(userId);
        order.setProductId(product.getId());
        order.setDeliveryAddress(deliveryAddress);
        order.setProduct(product);
        order.setIsEvaluate(UN_HANDLER);
        order.setNumber(number);
        order.setTotalPrice(totalPrice);
        order.setRemark(remark);
        return order;
    }
}

