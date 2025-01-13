package com.app.domain.shippingcart.controller;

import com.app.controller.Controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxl
 * @since 2024/3/22
 */
@Tag(name = "购物车 - 详情")
@RequestMapping("/shoppingCart")
@RestController
@Validated
public class ShoppingCartController extends Controller {

    /*//商品加入购物车
    @PostMapping("/add")
    @Operation(summary = "商品加入购物车")
    public RespEntity<Boolean> addSku(@RequestBody @JsonView(Entity.INSERT.class) @Validated(Entity.INSERT.class) ShoppingCartEntity entity) {
        return RespEntity.success(shoppingCartService.addSku(entity,LoginUser.getLoginUserId()));
    }

    //加/减购物车商品
    @Operation(summary = "加/减购物车商品")
    @GetMapping("/addOrReduce")
    @Parameter(name = "number",description = "数量,如果是加则是正数,如果是减则是负数")
    @Parameter(name = "itemId",description = "购物项ID")
    public RespEntity<Boolean> addOrReduce(@RequestParam String itemId,@RequestParam Integer number) {
        return RespEntity.success(shoppingCartService.addOrReduce(itemId,number));
    }

    //查询购物车商品
    @Operation(summary = "查询购物车商品")
    @GetMapping("/getAll")
    public RespEntity<Page<ShoppingCartEntity>> getAll() {
        return RespEntity.success(shoppingCartService.getAll(LoginUser.getLoginUserId()));
    }*/
}
