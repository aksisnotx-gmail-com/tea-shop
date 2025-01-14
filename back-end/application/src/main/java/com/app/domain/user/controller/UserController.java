package com.app.domain.user.controller;

import com.app.controller.Controller;
import com.app.domain.base.Entity;
import com.app.domain.user.entity.LoginUser;
import com.app.domain.user.entity.UserEntity;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxl
 * @since 2024/3/16
 */

@Tag(name = "[我的 & 登录] - 2025/01/14")
@RequestMapping("/user")
@RestController
@Validated
public class UserController extends Controller {

    @PostMapping("/auth/login/wechat")
    @Operation(summary = "微信小程序登录 - [小程序]")
    public RespEntity<UserEntity> wechatLogin(@RequestBody @JsonView({Entity.LOGIN.class}) UserEntity param) {
        return RespEntity.success(userLoginService.loginWithWechat(param));
    }

    @PostMapping("/auth/register/wechat")
    @Operation(summary = "微信小程序注册 - [小程序]")
    public RespEntity<?> wechatRegister(@RequestBody @JsonView({Entity.INSERT.class}) @Validated(Entity.INSERT.class) UserEntity param) {
        return RespEntity.success(userLoginService.registerWithWechat(param));
    }

    @PostMapping("/auth/login")
    @Operation(summary = "后台登录 - [后台]")
    public RespEntity<UserEntity> login(@RequestBody @JsonView({Entity.ADMIN_LOGIN.class}) @Validated(Entity.ADMIN_LOGIN.class) UserEntity param) {
        return RespEntity.success(userLoginService.login(param));
    }

    @PostMapping("/modifyUserInfo")
    @Operation(summary = "修改个人信息 - [后台 & 小程序]")
    public RespEntity<UserEntity> modifyUserInfo(@RequestBody @JsonView({Entity.UPDATE.class}) @Validated(Entity.UPDATE.class) UserEntity param) {
        return RespEntity.success(userLoginService.modifyUserInfo(param));
    }

    @GetMapping("/get")
    @Operation(summary = "获取个人信息 - [后台 & 小程序]")
    public RespEntity<UserEntity> get() {
        return RespEntity.success(LoginUser.getLoginUser());
    }


    //查看用户列表
    @GetMapping("/list")
    @Operation(summary = "查看用户列表 - [后台]")
    public RespEntity<Page<UserEntity>> list() {
        return RespEntity.success(userLoginService.lambdaQuery().page(CommonPageRequestUtils.defaultPage()));
    }

    //查看用户详情信息
    @GetMapping("/info/{id}")
    @Operation(summary = "查看用户信息 - [后台 & 小程序]")
    public RespEntity<UserEntity> info(@PathVariable String id) {
        return RespEntity.success(userLoginService.getById(id));
    }

    //删除用户
    @GetMapping("/delete/{id}")
    @Operation(summary = "删除用户 - [后台]")
    public RespEntity<Boolean> delete(@PathVariable String id) {
        return RespEntity.success(userLoginService.deleteUserById(id));
    }

}
