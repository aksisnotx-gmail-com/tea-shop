package com.app.domain.product.entity;

import com.app.domain.base.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情(SysProductDetails)表实体类
 *
 * @author makejava
 * @since 2024-03-19 15:31:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "商品详情实体")
@TableName(value = "sys_product_details",autoResultMap = true)
public class ProductDetailsEntity extends Entity {

    public static final int IS_SPECIAL = 1;
    public static final int NOT_SPECIAL = 0;

    @Serial
    private static final long serialVersionUID = 5910473738974612741L;

    //轮播图来自于sku json形式 List<String>
    @Schema(description = "轮播图")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonView({INSERT.class,UPDATE.class})
    private List<String> carousel;

    //商品名称
    @Schema(description = "商品名称")
    @JsonView({INSERT.class,UPDATE.class})
    @NotBlank(message = "商品名称不能为空", groups = {INSERT.class,UPDATE.class})
    private String productName;

    //商品价格
    @Schema(description = "商品价格")
    @JsonView({INSERT.class,UPDATE.class})
    @NotNull(message = "商品价格不能为空", groups = {INSERT.class,UPDATE.class})
    private BigDecimal price;

    @Schema(description = "商品特惠价格")
    @JsonView({INSERT.class,UPDATE.class})
    @Min(value = 0, message = "商品特惠价格错误", groups = {INSERT.class,UPDATE.class})
    private BigDecimal specialPrice;

    @Schema(description = "库存")
    @JsonView({INSERT.class,UPDATE.class})
    @Min(value = 0, message = "库存数量错误", groups = {INSERT.class,UPDATE.class})
    private int stock;

    @Schema(description = "是否为特惠，1 是 0 不是")
    @JsonView({INSERT.class,UPDATE.class})
    @Min(value = 0, message = "异常参数", groups = {INSERT.class,UPDATE.class})
    @Max(value = 1, message = "异常参数", groups = {INSERT.class,UPDATE.class})
    private int isSpecial;

    @Schema(description = "销量")
    @JsonView({IGNORE.class})
    private int salesQuantity;

    @Schema(description = "商品类型ID，一个商品可以在多个分类")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonView({INSERT.class,UPDATE.class})
    @Size(min = 1, message = "商品类型不能为空", groups = {INSERT.class,UPDATE.class})
    private List<String> productTypeIds;
}

