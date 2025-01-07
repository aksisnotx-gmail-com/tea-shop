package aks.com.web.domain.common.service;

import aks.com.web.domain.common.entity.Entity;
import aks.com.web.domain.common.mapper.EntityIBaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author jamesaks
 * @since 2025/1/7
 */
public abstract class AbstractService<M extends EntityIBaseMapper<T>,T extends Entity> extends ServiceImpl<M,T> {
}
