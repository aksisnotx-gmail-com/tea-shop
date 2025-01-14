package com.app.domain.product.service;

import com.app.domain.base.AbstractService;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductTypeEntity;
import com.app.domain.product.mapper.ProductDetailsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean deleteProductById(List<String> ids) {
        return this.removeBatchByIds(ids);
    }

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
}
