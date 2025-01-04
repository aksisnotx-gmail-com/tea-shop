package com.app.domain.comment.controller;

import com.app.controller.Controller;
import com.app.domain.base.Entity;
import com.app.domain.comment.entity.ProductCommentEntity;
import com.app.domain.comment.vo.CommentVO;
import com.app.domain.order.entity.OrderEntity;
import com.app.domain.user.entity.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxl
 * @since 2024/3/20
 */
@Tag(name = "首页 - 评价")
@RequestMapping("/product/comment")
@RestController
@Validated
public class ProductCommentController extends Controller {

    //发布评论,需要查看当前用户是否有这个商品
    @PostMapping("/publish")
    @Operation(summary = "评价")
    public RespEntity<Boolean> publishComment(@RequestBody @Validated(Entity.INSERT.class) @JsonView(Entity.INSERT.class) ProductCommentEntity param) {
        return RespEntity.success(commentService.publishComment(param, LoginUser.getLoginUserId()));
    }

    //查询所有评论
    @GetMapping("/queryAll/{productId}")
    @Operation(summary = "查询所有评价")
    public RespEntity<Page<ProductCommentEntity>> queryAllComment(@PathVariable String productId) {
        return RespEntity.success(commentService.queryAllComment(productId));
    }

    //删除评论，如果角色是买家只能删除自己的评论
    @GetMapping("/delete/")
    @Operation(summary = "删除评价")
    public RespEntity<Boolean> deleteComment(@RequestParam String commentId, @RequestParam String orderId) {
        return RespEntity.success(commentService.deleteComment(commentId,LoginUser.getLoginUser(),orderId));
    }

    @GetMapping("/getMyEvaluate")
    @Operation(summary = "我的评价")
    public RespEntity<Page<CommentVO>> getMyEvaluate() {
        return RespEntity.success(commentService.getMyEvaluate(LoginUser.getLoginUserId()));
    }
}
