package com.app.domain.user.service;

import com.app.domain.base.AbstractService;
import com.app.domain.user.entity.LoginUser;
import com.app.domain.user.enums.Role;
import com.app.domain.user.mapper.UserMapper;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.wallet.service.WalletService;
import com.sdk.exception.GlobalException;
import com.sdk.util.asserts.AssertUtils;
import com.sdk.util.md5.MD5Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public UserEntity loginWithWechat(UserEntity entity) {
        UserEntity user = getUserByPhoneNumber(entity.getPhoneNumber());
        AssertUtils.notNull(user, "用户不存在");
        AssertUtils.assertTrue(user.getRole().equals(Role.BUYER), "登录异常");
        AssertUtils.assertTrue(MD5Utils.decrypt(entity.getPwd(),user.getPwd()), "密码错误");
        return LoginUser.store(user);
    }

    public Boolean registerWithWechat(UserEntity param) {
        UserEntity user = getUserByPhoneNumber(param.getPhoneNumber());
        AssertUtils.isNull(user, "用户已经存在");
        param.setRole(Role.BUYER);
        param.setPwd(MD5Utils.encrypt(param.getPwd()));
        //保存用户
        boolean save = this.save(param);
        //小程序用户初始化钱包
        walletService.initWallet(param.getId());
        return save;
    }

    public UserEntity login(UserEntity param) {
        UserEntity one = this.lambdaQuery().eq(UserEntity::getUsername, param.getUsername()).eq(UserEntity::getRole, Role.ADMIN).one();
        AssertUtils.notNull(one, "用户不存在");
        AssertUtils.assertTrue(Role.ADMIN.equals(one.getRole()), "登录异常");
        AssertUtils.assertTrue(MD5Utils.decrypt(param.getPwd(),one.getPwd()), "密码错误");
        return LoginUser.store(one);
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

    public Boolean modifyUserPassword(UserEntity param) {
        UserEntity user = getUserByPhoneNumber(param.getPhoneNumber());
        AssertUtils.assertTrue(Role.BUYER.equals(user.getRole()), "操作异常");
        user.setPwd(MD5Utils.encrypt(param.getPwd()));
        //移除 TOKEN
        LoginUser.remove(user.getId());
        return this.updateById(user);
    }

    public Boolean deleteUserById(String id) {
        return this.removeById(id) && LoginUser.remove(id);
    }

    private UserEntity getUserByPhoneNumber(String phoneNumber) {
        return this.lambdaQuery().eq(UserEntity::getPhoneNumber, phoneNumber).one();
    }
}
