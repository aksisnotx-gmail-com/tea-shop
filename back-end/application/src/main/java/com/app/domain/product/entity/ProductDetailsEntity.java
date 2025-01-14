package com.app.domain.product.entity;

import com.app.domain.base.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Serial
    private static final long serialVersionUID = 5910473738974612741L;

    //轮播图来自于sku json形式 List<String>
    @Schema(description = "轮播图")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> carousel;

    //商品名称
    @Schema(description = "商品名称")
    private String productName;

    //商品价格
    @Schema(description = "商品价格")
    private BigDecimal price;

    @Schema(description = "库存")
    private int stock;

    //是否为推荐商品
    @Schema(description = "是否推荐，1 是 0 不是")
    private int isRecommend;

    //商品类型：汉服、首饰  List<ProductType>
    @Schema(description = "商品类型ID，一个商品可以在多个分类")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> productTypeIds;
}

