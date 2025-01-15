package com.app.domain.order.service;

import com.app.domain.base.AbstractService;
import com.app.domain.order.enmus.OrderState;
import com.app.domain.order.entity.OrderEntity;
import com.app.domain.order.mapper.OrderMapper;
import com.app.domain.order.param.OrderParam;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.service.ProductDetailsService;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.user.enums.Role;
import com.app.domain.user.service.UserService;
import com.app.domain.wallet.service.WalletService;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.app.domain.order.enmus.OrderState.MAKE_PAYMENT;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Service
@RequiredArgsConstructor
public class OrderService extends AbstractService<OrderMapper, OrderEntity> {

    private final ProductDetailsService productDetailsService;

    private final OrderAction orderAction;

    private final WalletService walletService;

    private final UserService userService;

    /**
     * 查看用户是否买了这个商品中
     *
     * @param userId 用户ID
     */
    public Boolean hasOrder(String userId,String orderId) {
        //查询买过(确认收货)的订单
        return this.lambdaQuery().
                //用户ID
                eq(OrderEntity::getUserId, userId).
                //订单ID
                eq(OrderEntity::getId, orderId).
                //订单状态
                eq(OrderEntity::getState, OrderState.CONFIRM_RECEIPT.name()).exists();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public synchronized List<OrderEntity> createOrder(List<OrderParam> orderParam, String userId) {
        List<OrderEntity> list = orderParam.stream().map(t -> {
            ProductDetailsEntity product = productDetailsService.getById(t.getProductId());
            productDetailsService.checkStock(t.getNumber(),product.getId());
            //扣减库存
            productDetailsService.addOrSubStock(t.getNumber(),product.getId(),false);
            //创建订单
            return OrderEntity.create(OrderState.PLACE_ORDER,
                    userId,
                    t.getDeliveryAddress(),
                    product,
                    t.getNumber(),
                    t.getTotalPrice(),
                    t.getRemark());
        }).toList();
        return this.saveBatch(list) ? list : new ArrayList<>();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean closeOrder(String orderId, UserEntity user) {
        OrderState closeOrder = OrderState.CLOSE_ORDER;
        OrderEntity order = Role.ADMIN.equals(user.getRole()) ? getOneByAdmin(orderId, closeOrder, user) : getOne(orderId, closeOrder, user);
        //如果允许关闭订单 , 1.订单中的商品如果存在数量加回来 2.订单中的商品不存在则忽略
        ProductDetailsEntity product = productDetailsService.getById(order.getProductId());
        if (!Objects.isNull(product)) {
            //存在
            productDetailsService.addOrSubStock(order.getNumber(), product.getId(), true);
        }
        order.setState(closeOrder);
        return this.updateById(order);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean payOrder(String orderId, UserEntity user) {
        OrderState makePayment = MAKE_PAYMENT;
        OrderEntity one = getOne(orderId,makePayment , user);
        //获取账单总余额
        BigDecimal price = one.getTotalPrice();
        boolean expenditure = walletService.expenditure(price, user);
        //设置状态
        one.setState(makePayment);
        return this.updateById(one) && expenditure;
    }

    public Boolean applyRefundOrder(String orderId, UserEntity loginUser) {
        OrderState refund = OrderState.APPLY_FOR_REFUND;
        OrderEntity one = getOne(orderId, refund,loginUser);
        one.setState(refund);
        return this.updateById(one);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean refundOrder(String orderId,UserEntity user) {
        OrderState refund = OrderState.REFUND;
        //管理员做的事不需要买家
        OrderEntity one = getOneByAdmin(orderId, refund,user);
        one.setState(refund);
        ProductDetailsEntity product = productDetailsService.getById(one.getProductId(), false);
        //如果商品存在则返回库存否则不返回
        if (Objects.nonNull(product)) {
            productDetailsService.addOrSubStock(product.getStock() + one.getNumber(), product.getId(), true);
        }
        //获取账单总余额0
        BigDecimal price = one.getTotalPrice();
        //余额 + 商品总价钱
        Boolean recharge = walletService.recharge(price, userService.getById(one.getUserId()));
        //更新订单 && 钱包 && 更新商品
        return this.updateById(one) && recharge;
    }
    public Boolean sendOrder(String orderId, UserEntity loginUser) {
        OrderState send = OrderState.SHIP_ORDER;
        //不需要获取管理员用户
        OrderEntity one = getOneByAdmin(orderId, send, loginUser);
        one.setState(send);
        return this.updateById(one);
    }

    public Boolean receiveOrder(String orderId, UserEntity loginUser) {
        OrderState receive = OrderState.CONFIRM_RECEIPT;
        OrderEntity one = getOne(orderId, receive, loginUser);
        one.setState(receive);
        //更新未未评价
        one.setIsEvaluate(OrderEntity.UN_EVALUATE);
        return this.updateById(one);
    }


    public Boolean deleteOrder(String orderId, UserEntity loginUser) {
        OrderState delete = OrderState.DELETE_ORDER;
        OrderEntity one = getOne(orderId, delete, loginUser);
        return this.removeById(one.getId());
    }

    public Page<OrderEntity> getOrderByUser(UserEntity loginUser) {
        //用户获取自己的,管理员获取所有的
        Page<OrderEntity> page;
        if (Role.ADMIN.equals(loginUser.getRole())) {
            page = this.lambdaQuery().page(CommonPageRequestUtils.defaultPage());
        }else {
            page = this.lambdaQuery().eq(OrderEntity::getUserId, loginUser.getId()).page(CommonPageRequestUtils.defaultPage());
        }
        return page;
    }

    public OrderEntity getOrder(String orderId) {
        return this.lambdaQuery().eq(OrderEntity::getId, orderId).one();
    }


    public Page<OrderEntity> getWaitPay(UserEntity user) {
        return getOrderByUserId(user,OrderEntity::getState,OrderState.PLACE_ORDER.name());
    }

    public Page<OrderEntity> getWaitReceive(UserEntity user) {
        return getOrderByUserId(user,OrderEntity::getState,OrderState.SHIP_ORDER.name(), MAKE_PAYMENT.name());
    }

    public Page<OrderEntity> getWaitEvaluate(UserEntity user) {
        //查询出当前用户已经收货的商品
        Page<OrderEntity> page = getOrderByUserId(user, OrderEntity::getState, OrderState.CONFIRM_RECEIPT.name());
        List<OrderEntity> list = page.getRecords().
                stream().
                //把筛选未评价的商品
                filter(t -> OrderEntity.UN_EVALUATE.equals(t.getIsEvaluate())).toList();
        page.setTotal(list.size());
        page.setRecords(list);
        return page;
    }

    public List<OrderEntity> getDetailsByProductId(String productId) {
        return this.lambdaQuery().eq(OrderEntity::getProductId, productId).list();
    }

    private OrderEntity getOne(String orderId,OrderState nextState,UserEntity user) {
        OrderEntity entity = this.lambdaQuery().eq(OrderEntity::getId, orderId).eq(OrderEntity::getUserId, user.getId()).one();
        AssertUtils.notNull(entity, "订单不存在");
        orderAction.doAction(entity.getState(), nextState, user.getRole());
        return entity;
    }

    private OrderEntity getOneByAdmin(String orderId,OrderState nextState,UserEntity user) {
        OrderEntity entity = this.lambdaQuery().eq(OrderEntity::getId, orderId).one();
        AssertUtils.notNull(entity, "订单不存在");
        orderAction.doAction(entity.getState(), nextState, user.getRole());
        return entity;
    }

    private Page<OrderEntity> getOrderByUserId(UserEntity user, SFunction<OrderEntity,?> sFunction, Object... val) {
        Page<OrderEntity> page;
        //管理员获取所有的
        if (Role.ADMIN.equals(user.getRole())) {
            page = this.lambdaQuery().in(sFunction, val).page(CommonPageRequestUtils.defaultPage());
        }else {
            page = this.lambdaQuery().eq(OrderEntity::getUserId, user.getId()).in(sFunction, val).page(CommonPageRequestUtils.defaultPage());
        }
        return page;
    }

    public Page<OrderEntity> getMyEvaluateOrder(String loginUserId) {
        return this.lambdaQuery().eq(OrderEntity::getUserId, loginUserId).
                eq(OrderEntity::getIsEvaluate, OrderEntity.EVALUATE).
                page(CommonPageRequestUtils.defaultPage());
    }

    public Page<OrderEntity> getOrderByType(OrderState type, UserEntity loginUser) {
        if (Role.ADMIN.equals(loginUser.getRole())) {
            return this.lambdaQuery().eq(OrderEntity::getState, type).page(CommonPageRequestUtils.defaultPage());
        }
        return new Page<>();
    }
}
