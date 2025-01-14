package com.app.domain.shippingcart.entity;

import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 购物车(SysShoppingCart)表实体类
 *
 * @author makejava
 * @since 2024-03-22 10:54:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_shopping_cart")
@Schema(description = "购物车")
public class ShoppingCartEntity extends Entity {

    @Serial
    private static final long serialVersionUID = -1888746983435211863L;

    //商品id
    @Schema(description = "商品id")
    @JsonView({INSERT.class})
    @NotBlank(message = "商品id不能为空", groups = {INSERT.class})
    private String productId;

    //数量
    @Schema(description = "数量")
    @JsonView({INSERT.class})
    @Min(value = 1, message = "数量不能小于1", groups = {INSERT.class})
    private Integer number;

    //用户ID
    @Schema(description = "用户ID")
    @JsonView({IGNORE.class})
    private String userId;

    @JsonView({IGNORE.class})
    @TableField(exist = false)
    private ProductDetailsEntity product;
}

