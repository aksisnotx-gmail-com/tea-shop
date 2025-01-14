package com.app.domain.product.service;

import cn.hutool.core.util.StrUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.product.entity.ProductTypeEntity;
import com.app.domain.product.mapper.ProductTypeMapper;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

/**
 * @author xxl
 * @since 2024/4/1
 */
@Service
public class ProductTypeService extends AbstractService<ProductTypeMapper, ProductTypeEntity> {

    public Page<ProductTypeEntity> getAllType(String typeId) {
        return StrUtil.isBlank(typeId) ? this.lambdaQuery().page(CommonPageRequestUtils.defaultPage()) :
                this.lambdaQuery().eq(ProductTypeEntity::getId, typeId).page(CommonPageRequestUtils.defaultPage());
    }
}
