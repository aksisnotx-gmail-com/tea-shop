package com.app.domain.order.controller;

import com.app.controller.Controller;
import com.app.domain.base.Entity;
import com.app.domain.order.enmus.OrderState;
import com.app.domain.order.entity.OrderEntity;
import com.app.domain.order.param.OrderParam;
import com.app.domain.user.entity.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxl
 * @since 2024/3/23
 */
@Tag(name = "[我的 - 订单] - 2025/01/15")
@RequestMapping("/shoppingCart/order")
@RestController
@Validated
public class OrderController extends Controller {

    //下单
    @PostMapping("/create")
    @Operation(summary = "创建订单 - [小程序]")
    public RespEntity<List<OrderEntity>> createOrder(@RequestBody List<OrderParam> orderParam) {
        return RespEntity.success(orderService.createOrder(orderParam, LoginUser.getLoginUserId()));
    }

    //关闭订单
    @GetMapping("/close/{orderId}")
    @Operation(summary = "关闭订单 - [后台 & 小程序]")
    public RespEntity<Boolean> closeOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.closeOrder(orderId,LoginUser.getLoginUser()));
    }

    //付款
    @GetMapping("/pay/{orderId}")
    @Operation(summary = "付款 - [小程序]")
    public RespEntity<Boolean> payOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.payOrder(orderId,LoginUser.getLoginUser()));
    }

    //申请退款
    @GetMapping("/applyRefund/{orderId}")
    @Operation(summary = "申请退款 - [小程序]")
    public RespEntity<Boolean> applyRefundOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.applyRefundOrder(orderId,LoginUser.getLoginUser()));
    }

    //退款
    @GetMapping("/refund/{orderId}")
    @Operation(summary = "退款 - [小程序]")
    public RespEntity<Boolean> refundOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.refundOrder(orderId,LoginUser.getLoginUser()));
    }

    //发货
    @GetMapping("/send/{orderId}")
    @Operation(summary = "发货 - [后台]")
    public RespEntity<Boolean> sendOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.sendOrder(orderId,LoginUser.getLoginUser()));
    }

    //确认收货
    @GetMapping("/receive/{orderId}")
    @Operation(summary = "确认收货 - [小程序]")
    public RespEntity<Boolean> receiveOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.receiveOrder(orderId,LoginUser.getLoginUser()));
    }

    //删除订单
    @GetMapping("/delete/{orderId}")
    @Operation(summary = "删除订单 - [后台 & 小程序]")
    public RespEntity<Boolean> deleteOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.deleteOrder(orderId,LoginUser.getLoginUser()));
    }

    //获取订单
    @GetMapping("/get")
    @Operation(summary = "获取订单[前台获取自己的,后台获取所有] - [后台 & 小程序]")
    public RespEntity<Page<OrderEntity>> getOrder() {
        return RespEntity.success(orderService.getOrderByUser(LoginUser.getLoginUser()));
    }

    //获取订单详情
    @GetMapping("/get/{orderId}")
    @Operation(summary = "获取订单详情 - [后台 & 小程序]")
    public RespEntity<OrderEntity> getOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.getById(orderId));
    }

    //待评价
    @GetMapping("/getWaitEvaluate")
    @Operation(summary = "获取待评价的商品 - [后台 & 小程序]")
    public RespEntity<Page<OrderEntity>> getWaitEvaluate() {
        return RespEntity.success(orderService.getWaitEvaluate(LoginUser.getLoginUser()));
    }

    @GetMapping("/search")
    @Operation(summary = "订单搜索，可以获取待发货、待收货... - [后台 & 小程序]")
    @Parameter(name = "type",description = "只传 type 能获取【代收货、待发货】& 不能为空")
    @Parameter(name = "productName",description = "商品名字，有商品名字表示在某个 type 中搜索订单 & 可选")
    public RespEntity<Page<OrderEntity>> search(@RequestParam OrderState type,@RequestParam(required = false) String productName) {
        return RespEntity.success(orderService.search(type,LoginUser.getLoginUser(),productName));
    }

    @PostMapping("/modify/address")
    @Operation(summary = "修改地址 - [小程序]")
    public RespEntity<Boolean> modifyOrderAddress(@RequestBody @JsonView(Entity.UPDATE.class) @Validated(Entity.UPDATE.class) OrderEntity order) {
        return RespEntity.success(orderService.modifyOrderAddress(order));
    }
}

