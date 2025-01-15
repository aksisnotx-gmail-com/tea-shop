package com.app.domain.product.service;

import com.app.domain.base.AbstractService;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductTypeEntity;
import com.app.domain.product.mapper.ProductDetailsMapper;
import com.sdk.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDetailsService extends AbstractService<ProductDetailsMapper,ProductDetailsEntity> {

    private final ProductTypeService typeService;

    public Boolean deleteType(String typeId) {
        ProductTypeEntity entity = typeService.getById(typeId);
        //找到包含这个ID的
        List<ProductDetailsEntity> list = this.lambdaQuery().like(ProductDetailsEntity::getProductTypeIds, entity.getId()).list();
        //删除这个商品或者更新
        list.stream().peek(t -> {
            List<String> typeIds = t.getProductTypeIds();
            typeIds.remove(entity.getId());
            t.setProductTypeIds(typeIds);
        }).toList().forEach(t -> {
            List<String> typeIds = t.getProductTypeIds();
            if (typeIds.isEmpty()) {
                this.removeById(t);
            }else {
                this.updateById(t);
            }
        });
        return typeService.removeById(typeId);
    }

    /**
     * 检查库存,不足抛出异常
     * @param deductionQuantity 扣减数量
     * @param productId 产品 ID
     */
    public synchronized void checkStock(int deductionQuantity,String productId) {
        if (deductionQuantity <= 0) {
            return;
        }
        int stock = this.getById(productId, true).getStock();

        if ((stock - deductionQuantity) < 0) {
            throw new GlobalException("库存不足");
        }
    }

    /**
     *  增加或者扣减库存
     */
    public synchronized boolean addOrSubStock(int quantity, String productId,boolean isAdd) {
        if (quantity > 0) {
            return false;
        }

        ProductDetailsEntity entity = this.getById(productId, true);
        if (isAdd) {
            entity.setStock(entity.getStock() + quantity);
            return this.updateById(entity);
        }

        if (entity.getStock() - quantity < 0) {
            return false;
        }

        entity.setStock(entity.getStock() - quantity);
        return this.updateById(entity);
    }
}
