package com.app.domain.shippingcart.service;

import com.app.domain.base.AbstractService;
import com.app.domain.product.service.ProductDetailsService;
import com.app.domain.shippingcart.entity.ShoppingCartEntity;
import com.app.domain.shippingcart.mapper.ShoppingCartMapper;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.user.enums.Role;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author xxl
 * @since 2024/3/22
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartService extends AbstractService<ShoppingCartMapper, ShoppingCartEntity> {

    private final ProductDetailsService productService;

    public synchronized Boolean addProduct(ShoppingCartEntity entity, UserEntity user) {
        AssertUtils.assertTrue(Role.BUYER.equals(user.getRole()), "操作异常");
        //检查库存
        productService.checkStock(entity.getNumber(), entity.getProductId());
        entity.setUserId(user.getId());
        return this.save(entity);
    }

    public synchronized Boolean addOrReduce(String itemId, Integer number) {
        ShoppingCartEntity entity = this.getById(itemId);
        int sumNumber = entity.getNumber() + number;

        //总数量为0则购物车没有这个购物项
        if (sumNumber <= 0) {
            return this.removeById(entity.getId());
        }
        //库存检查
        productService.checkStock(sumNumber, entity.getProductId());
        //更新
        entity.setNumber(sumNumber);
        return this.updateById(entity);
    }

    public Page<ShoppingCartEntity> getAll(String loginUserId) {
        Page<ShoppingCartEntity> page = this.lambdaQuery().eq(ShoppingCartEntity::getUserId, loginUserId).page(CommonPageRequestUtils.defaultPage());
        page.getRecords().forEach(t -> {
            t.setProduct(productService.getById(t.getProductId()));
        });
        return page;
    }
}
