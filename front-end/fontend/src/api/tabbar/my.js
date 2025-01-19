import service from '@/utils/request'

// export const ORDER_STATE = {
//     PLACE_ORDER:{front:'待付款',back:'待付款'},
//     MAKE_PAYMENT:{front:'已付款',back:'待发货'},
//     SHIP_ORDER:{front:'待收货',back:'待收货'},
//     CONFIRM_RECEIPT:{front:'已完成',back:'已完成'},
//     CLOSE_ORDER:{front:'已关闭',back:'已关闭'},
//     APPLY_FOR_REFUND:{front:'待退款',back:'待退款'},
//     REFUND:{front:'已退款',back:'已退款'},
//     DELETE_ORDER:{front:'已删除',back:'已删除'},
// }
export const ORDER_STATUS = {
    PLACE_ORDER: 'PLACE_ORDER', // {front:'待付款',back:'待付款'}
    MAKE_PAYMENT: 'MAKE_PAYMENT', // {front:'已付款',back:'待发货'}
    SHIP_ORDER: 'SHIP_ORDER', // {front:'待收货',back:'待收货'}
    CONFIRM_RECEIPT: 'CONFIRM_RECEIPT' // {front:'已完成',back:'已完成'}
}


// 订单
/**
 * @description 获取自己所有订单
 */
export function getAllOrderApi (current) {
    return service({
        url: `shoppingCart/order/get?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 获取待付款的订单
 */
export function getWaitPayApi (current) {
    return service({
        url: `shoppingCart/order/getWaitPay?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 根据类型获取订单状态
 */
export function getOrderByStatusApi (type, current) {
    return service({
        url: `shoppingCart/order/search?type=${type}&current=${current}`,
        method: 'get'
    })
}

/**
 * @description 获取待收货的订单
 */
export function getWaitReceiveApi (current) {
    return service({
        url: `shoppingCart/order/getWaitReceive?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 获取待评价的订单
 */
export function getWaitEvaluateApi (current) {
    return service({
        url: `shoppingCart/order/getWaitEvaluate?current=${current}`,
        method: 'get'
    })
}

// 和敬币中心
/**
 * @description 查询和敬币余额
 */
export function getWalletApi () {
    return service({
        url: `user/wallet/query`,
        method: 'get'
    })
}

/**
 * @description 充值
 */
export function rechargeWalletApi (decimal) {
    return service({
        url: `user/wallet/recharge?decimal=${decimal}`,
        method: 'get'
    })
}

/**
 * @description 获取我的评价
 */
export function getMyEvaluateApi (current) {
    return service({
        url: `product/comment/getMyEvaluate?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 删除评价
 */
export function delMyEvaluateApi (commentId, orderId) {
    return service({
        url: `product/comment/delete/?commentId=${commentId}&orderId=${orderId}`,
        method: 'get'
    })
}

/**
 * @description 消息中心
 */
export function getMsgApi () {
    return service({
        url: `discovery/getMsg`,
        method: 'get'
    })
}

/**
 * @description 已读信息
 */
export function readMsgApi (data) {
    return service({
        url: `discovery/read`,
        method: 'post',
        data
    })
}

/**
 * @description 根据类型获取发现-评论
 */
export function getByTypeApi (type, id) {
    return service({
        url: `discovery/getByType?type=${type}&id=${id}`,
        method: 'get'
    })
}

/**
 * @description 获取最近搜索记录
 */
export function getSearchHistoryApi () {
    return service({
        url: `product/detail/search/history`,
        method: 'get'
    })
}

/**
 * 根据商品名字搜索商品
 */
export function searchProductApi (productName) {
    return service({
        url: `product/detail/search?productName=${productName}`,
        method: 'get'
    })
}

/**
 * 删除搜索记录
 * @param {number} type type=0删除历史记录，type=1删除最近记录
 */
export function delSearchHistoryApi (type) {
    return service({
        url: `product/detail/search/history/delete?type=${type}`,
        method: 'get'
    })
}
