package com.app.domain.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.user.entity.LoginUser;
import com.app.domain.user.enums.Role;
import com.app.domain.user.mapper.UserMapper;
import com.app.domain.user.param.WeChatLoginParam;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.wallet.service.WalletService;
import com.app.toolkit.redis.RedisUtils;
import com.sdk.exception.GlobalException;
import com.sdk.resp.RespEntity;
import com.sdk.util.asserts.AssertUtils;
import com.sdk.util.jwt.JWTUtils;
import com.sdk.util.md5.MD5Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 微信服务
 *
 * @author xxl
 * @since 2024/3/16
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService extends AbstractService<UserMapper,UserEntity> {

    private final WalletService walletService;

    public UserEntity login(UserEntity entity,boolean isWechat) {
        UserEntity user = getUserByPhoneNumber(entity.getPhoneNumber());
        AssertUtils.notNull(user, "用户不存在");
        AssertUtils.assertTrue((Role.BUYER.equals(user.getRole()) && isWechat) ||
                Role.ADMIN.equals(user.getRole()) && !isWechat ,"登录错误");
        AssertUtils.assertTrue(MD5Utils.decrypt(user.getPwd(),user.getPwd()), "密码错误");
        return LoginUser.store(user);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean register(UserEntity param,boolean isWeChat) {
        UserEntity user = getUserByPhoneNumber(param.getPhoneNumber());
        AssertUtils.isNull(user, "用户已经存在");
        param.setRole(isWeChat ? Role.BUYER : Role.ADMIN);
        param.setPwd(MD5Utils.encrypt(param.getPwd()));
        //保存用户
        boolean save = this.save(param);
        //前台角色初始化钱包
        if (isWeChat && save) {
            walletService.initWallet(param.getId());
        }
        return save;
    }

    public UserEntity modifyUserInfo(UserEntity param) {
        UserEntity user = getById(param.getId());
        user.setNickname(param.getNickname());
        user.setAvatar(param.getAvatar());
        user.setUsername(param.getUsername());
        user.setShippingAddress(param.getShippingAddress());
        user.setGender(param.getGender());
        //更新用户
        boolean update = this.updateById(param);
        if (update) {
            UserEntity entity = this.getById(param.getId());
            entity.setToken(LoginUser.getToken());
            return LoginUser.update(entity);
        }
        throw new GlobalException("更新用户信息失败");
    }

    public Boolean deleteUserById(String id) {
        return this.removeById(id) && LoginUser.remove(id);
    }

    private UserEntity getUserByPhoneNumber(String phoneNumber) {
        return this.lambdaQuery().eq(UserEntity::getPhoneNumber, phoneNumber).one();
    }
}
