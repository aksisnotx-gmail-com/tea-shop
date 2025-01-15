package com.app.domain.product.service;

import com.app.domain.base.AbstractService;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductTypeEntity;
import com.app.domain.product.mapper.ProductDetailsMapper;
import com.app.toolkit.redis.RedisUtils;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDetailsService extends AbstractService<ProductDetailsMapper,ProductDetailsEntity> {

    private final ProductTypeService typeService;

    private final RedisUtils redisUtils;

    private static final String SEARCH_HISTORY = "search:history:";
    private static final String RECENT_SEARCH_HISTORY = "search:recent:history:";

    /**
     * 限制最近搜索记录的数量
     */
    private static final int MAX_RECENT_SEARCHES = 10;


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
    public synchronized ProductDetailsEntity addOrSubStock(int quantity, String productId,boolean isAdd) {
        if (quantity <= 0) {
            throw new GlobalException("数量必须大于0");
        }

        ProductDetailsEntity entity = this.getById(productId, true);
        if (isAdd) {
            entity.setStock(entity.getStock() + quantity);
            this.updateById(entity);
            return entity;
        }

        if (entity.getStock() - quantity < 0) {
            throw new GlobalException("库存不足");
        }

        entity.setStock(entity.getStock() - quantity);
        this.updateById(entity);
        return entity;
    }

    public Page<ProductDetailsEntity> search(String productName, String loginUserId) {
        saveRecentSearch(loginUserId, productName);
        saveSearchHistory(loginUserId, productName);
        return this.lambdaQuery().
                like(ProductDetailsEntity::getProductName, productName).
                page(CommonPageRequestUtils.defaultPage());
    }


    public Map<String,Object> getSearchHistory(String loginUserId) {
        // 获取最近的 10 条搜索记录
        List<Object> range = redisUtils.getRedisTemplate().opsForList().range(RECENT_SEARCH_HISTORY + loginUserId, 0, MAX_RECENT_SEARCHES - 1);
        if (Objects.isNull(range)){
            range = new ArrayList<>();
        }

        //搜索记录
        Set<Object> searchHistory = redisUtils.getRedisTemplate().opsForZSet().reverseRange(SEARCH_HISTORY + loginUserId, 0, -1);
        if (Objects.isNull(searchHistory)){
            searchHistory = new HashSet<>();
        }
        return Map.of("recentSearchHistory",range,"searchHistory",searchHistory);
    }

    /**
     * 保存最近搜索记录
     */
    private void saveRecentSearch(String userId, String productName) {
        String key = RECENT_SEARCH_HISTORY + userId;
        // 检查用户的最近搜索记录是否已存在
        if (!redisUtils.getRedisTemplate().hasKey(key)) {
            // 新搜索记录加到右边
            redisUtils.getRedisTemplate().opsForList().rightPush(key, productName);
        } else {
            // 如果记录已经存在，直接将搜索词推送到左边（保证新搜索记录排在最前面）
            redisUtils.getRedisTemplate().opsForList().leftPush(key, productName);
        }
        // 如果列表超过最大长度，则移除尾部元素
        redisUtils.getRedisTemplate().opsForList().trim(key, 0, MAX_RECENT_SEARCHES - 1);
    }

    /**
     * 保存历史搜索记录
     */
    private void saveSearchHistory(String userId, String productName) {
        String key = SEARCH_HISTORY + userId;
        // 使用 SortedSet 存储历史搜索记录，score 是时间戳
        redisUtils.getRedisTemplate().opsForZSet().add(key, productName, System.currentTimeMillis());
    }

}
