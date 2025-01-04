package com.app.domain.user.controller;

import com.app.controller.Controller;
import com.app.domain.base.Entity;
import com.app.domain.user.entity.LoginUser;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.user.param.WeChatLoginParam;
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

@Tag(name = "我的")
@RequestMapping("/user")
@RestController
@Validated
public class UserController extends Controller {

    @PostMapping("/auth/login/wechat")
    @Operation(summary = "微信小程序授权登录")
    public RespEntity<UserEntity> wechatLogin(@RequestBody WeChatLoginParam param) {
        return RespEntity.success(userLoginService.loginWithWechat(param));
    }

    @PostMapping("/auth/register")
    @Operation(summary = "后台注册")
    public RespEntity<Boolean> register(@RequestBody @JsonView({Entity.INSERT.class}) @Validated(Entity.INSERT.class) UserEntity param) {
        return RespEntity.success(userLoginService.register(param,false));
    }


    @PostMapping("/auth/login")
    @Operation(summary = "后台登录")
    public RespEntity<UserEntity> login(@RequestBody @JsonView({Entity.LOGIN.class}) @Validated(Entity.LOGIN.class) UserEntity param) {
        return RespEntity.success(userLoginService.login(param.getPhoneNumber(),param.getPwd(),false));
    }

    @PostMapping("/modifyUserInfo")
    @Operation(summary = "修改个人信息")
    public RespEntity<UserEntity> modifyUserInfo(@RequestBody @JsonView({Entity.UPDATE.class}) @Validated(Entity.UPDATE.class) UserEntity param) {
        return RespEntity.success(userLoginService.modifyUserInfo(param));
    }

    @GetMapping("/get")
    @Operation(summary = "获取个人信息")
    public RespEntity<UserEntity> get() {
        return RespEntity.success(LoginUser.getLoginUser());
    }


    //查看用户列表
    @GetMapping("/list")
    @Operation(summary = "查看用户列表 - [新增]")
    public RespEntity<Page<UserEntity>> list() {
        return RespEntity.success(userLoginService.lambdaQuery().page(CommonPageRequestUtils.defaultPage()));
    }

    //查看用户详情信息
    @GetMapping("/info/{id}")
    @Operation(summary = "查看用户信息 - [新增]")
    public RespEntity<UserEntity> info(@PathVariable String id) {
        return RespEntity.success(userLoginService.getById(id));
    }

    //删除用户
    @GetMapping("/delete/{id}")
    @Operation(summary = "删除用户 - [新增]")
    public RespEntity<Boolean> delete(@PathVariable String id) {
        return RespEntity.success(userLoginService.deleteUserById(id));
    }

}
