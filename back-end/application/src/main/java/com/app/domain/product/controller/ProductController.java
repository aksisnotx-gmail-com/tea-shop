package com.app.domain.product.controller;

import com.app.controller.Controller;
import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductTypeEntity;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxl
 * @since 2024/3/19
 */

@Tag(name = "[首页 - 商品] - 2025/01/14")
@RequestMapping("/product")
@RestController
@Validated
public class ProductController extends Controller {

    /*
     * ======================================
     * ============== 商品类别 ===============
     * ======================================
     */

    //增加商品类型
    @PostMapping("/type/add")
    @Operation(summary = "增加商品类型 - [后台]")
    public RespEntity<Boolean> addType(@RequestBody @Validated(Entity.INSERT.class) @JsonView(Entity.INSERT.class) ProductTypeEntity params) {
        return RespEntity.success(typeService.save(params));
    }


    //获取所有商品类型
    @GetMapping("/type")
    @Operation(summary = "获取商品类型有ID查询具体,没有ID查询所有的 - [小程序]")
    public RespEntity<Page<ProductTypeEntity>> getAllType(@RequestParam(required = false) String typeId) {
        return RespEntity.success(typeService.getAllType(typeId));
    }


    //删除商品类型
    @GetMapping("/type/delete/{typeId}")
    @Operation(summary = "删除商品类型 - [后台]")
    public RespEntity<Boolean> deleteType(@PathVariable("typeId") String typeId) {
        return RespEntity.success(productDetailsService.deleteType(typeId));
    }

    //修改商品类型
    @PostMapping("/type/modify")
    @Operation(summary = "修改商品类型 - [后台]")
    public RespEntity<Boolean> modifyType(@RequestBody @Validated(Entity.UPDATE.class) @JsonView(Entity.UPDATE.class) ProductTypeEntity param) {
        return RespEntity.success(typeService.updateById(param));
    }

    /*
     * ======================================
     * ============== 商品详情 ===============
     * ======================================
     */

    @GetMapping("/detail/type")
    @Operation(summary = "根据类型获取所有商品 - [小程序 & 后台]")
    public RespEntity<Page<ProductDetailsEntity>> getDetailByType(@RequestParam String typeId) {
        return RespEntity.success(productDetailsService.lambdaQuery().
                like(ProductDetailsEntity::getProductTypeIds, typeId).
                page(CommonPageRequestUtils.defaultPage()));
    }

    //发布商品详情
    @PostMapping("/detail/publish")
    @Operation(summary = "发布商品 - [后台]")
    public RespEntity<Boolean> publishDetail(@RequestBody @JsonView(Entity.INSERT.class) @Validated(Entity.INSERT.class) ProductDetailsEntity param) {
        return RespEntity.success(productDetailsService.save(param));
    }

    //删除商品
    @PostMapping("/detail/delete")
    @Operation(summary = "删除商品 - [后台]")
    public RespEntity<Boolean> delete(@RequestBody List<String> ids) {
        return RespEntity.success(productDetailsService.removeBatchByIds(ids));
    }

    //获取商品详情
    @GetMapping("/detail/{productId}")
    @Operation(summary = "获取商品详情 - [后台 & 小程序]")
    public RespEntity<ProductDetailsEntity> getDetail(@PathVariable("productId") String productId) {
        return RespEntity.success(productDetailsService.getById(productId));
    }

    //获取所有商品详情
    @GetMapping("/detail/all")
    @Operation(summary = "获取所有商品 - [后台 & 小程序]")
    public RespEntity<Page<ProductDetailsEntity>> getAllDetail() {
        return RespEntity.success(productDetailsService.page(CommonPageRequestUtils.defaultPage()));
    }

    //修改商品信息
    @PostMapping("/detail/modify")
    @Operation(summary = "修改商品 - [后台]")
    public RespEntity<Boolean> modifyDetail(@RequestBody @Validated(Entity.UPDATE.class) @JsonView(Entity.UPDATE.class) ProductDetailsEntity param) {
        return RespEntity.success(productDetailsService.updateById(param));
    }

    @GetMapping("/detail/search")
    @Operation(summary = "根据商品名字搜索商品 - [小程序 & 后台]")
    public RespEntity<Page<ProductDetailsEntity>> search(@RequestParam String productName) {
        return RespEntity.success(productDetailsService.
                lambdaQuery().
                like(ProductDetailsEntity::getProductName, productName).
                page(CommonPageRequestUtils.defaultPage()));
    }

    @GetMapping("/specialProducts")
    @Operation(summary = "特惠商品 - [小程序 & 后台]")
    public RespEntity<Page<ProductDetailsEntity>> getSpecialProducts() {
        return RespEntity.success(productDetailsService.
                lambdaQuery().eq(ProductDetailsEntity::getIsSpecial, ProductDetailsEntity.IS_SPECIAL).
                page(CommonPageRequestUtils.defaultPage()));
    }

    @GetMapping("/recommendProducts")
    @Operation(summary = "热销商品 - [小程序 & 后台]")
    public RespEntity<Page<ProductDetailsEntity>> getPopularProducts() {
        return RespEntity.success(productDetailsService.
                lambdaQuery().isNotNull(ProductDetailsEntity::getSalesQuantity).
                //销量不为0
                gt(ProductDetailsEntity::getSalesQuantity, 0).
                page(CommonPageRequestUtils.defaultPage()));
    }
}
