<script setup>
    import {
        getSwiperListApi,
        getRecommendProductsApi,
        getSpecialProductsApi
    } from '@/api/home'
    import { useGoodsStore } from '@/store/modules/goods'
    import { useTypeStore } from '@/store/modules/type'

    const onJumpToSearch = () => {
        uni.navigateTo({
            url: '/pagesA/pages/search/index'
        })
    }

    const onJumpToCulture = () => {
        uni.navigateTo({
            url: '/pagesA/pages/teaCulture/index'
        })
    }

    const swiperList = ref([])
    const getSwiperList = async () => {
        const res = await getSwiperListApi()
        if(res.code !== 200) return

        const { records } = res.data
        swiperList.value = records.map(item => item.bannerUrl)
    }

    const teaType = [
      {
        id: 1,
        imageUrl: 'scentedTea',
        typeName: '花茶'
      },
      {
        id: 2,
        imageUrl: 'blackTea',
        typeName: '红茶'
      },
      {
        id: 3,
        imageUrl: 'whiteTea',
        typeName: '白茶'
      },
      {
        id: 4,
        imageUrl: 'rawTea',
        typeName: '生茶'
      },
      {
        id: 5,
        imageUrl: 'ripeTea',
        typeName: '熟茶'
      },
    ]

    const specialProducts = ref([])
    const getSpecialProducts = async () => {
        const res = await getSpecialProductsApi()
        if(res.data.records.length) {
            specialProducts.value = [ ...res.data.records ]
        }
    }

    const goodsStore = useGoodsStore()
    const toDetail = (id) => {
        goodsStore.productId = id

        uni.navigateTo({
            url: '/pagesA/pages/goodsItem/index'
        })
    }

    const JumpClassify = (typeId) => {
        uni.navigateTo({
            url: `/pagesA/pages/classify/index?typeId=${typeId}`
        })
    }

    const JumpAllProduct = () => {
        uni.navigateTo({
            url: `/pagesA/pages/allProduct/index`
        })
    }

    const typeStore = useTypeStore()
    // onMounted(async () => {
    //     await getSwiperList()
    //     await typeStore.getProductTypeList()
    //     await getRecommendProducts()
    //     await getSpecialProducts()
    //     uni.hideLoading()
    // })

    // onLoad(() => {
    //     uni.showLoading({
    //         title: '加载中'
    //     });
    // })


</script>

<template>
    <view class="bg-#1E4F23">
        <view class="px-3">
          <up-search disabled placeholder="搜索" :showAction="false" @click="onJumpToSearch"></up-search>
        </view>
        <view class="px-4 py-3">
            <u-swiper
                :list="swiperList"
                indicator
                circular
                indicatorActiveColor="#E4697B"
                indicatorMode="dot"
                :displayMultipleItems="0"
            ></u-swiper>
        </view>
        <view class="px-4 pb-3">
          <up-image
              show-loading
              src="/pagesA/static/teaArtOrigin.webp"
              width="100%"
              height="360rpx"
              @click="onJumpToCulture"
          ></up-image>
        </view>
        <view class="px-4 py-3 flex justify-between">
          <template v-for="item of teaType" :key="item.id">
            <view class="flex flex-col gap-3 items-center">
              <up-image
                  :src="`/pagesA/static/${item.imageUrl}.webp`"
                  width="100rpx"
                  height="125rpx"
                  shape="circle"
              ></up-image>
              <text class="text-3.25 color-bluegray">{{ item.typeName }}</text>
            </view>
          </template>
        </view>

        <view class="px-4 mt-3 pb-4">
          <view class="bg-#fff p-2 rounded-2">
            <view class="hot-sales-header">
              <text class="text-5 font-600">热销茶叶</text>
              <!--            <view class="more-btn" @click="JumpAllProduct">-->
              <!--              <text class="text-3.25 color-#666">更多</text>-->
              <!--              <up-icon name="arrow-right" size="24" color="#666"></up-icon>-->
              <!--            </view>-->
            </view>

<!--            <view class="hot-sales-grid">-->
<!--              <template v-if="!specialProducts.length">-->
<!--                <u-empty text="暂无商品数据" mode="data"></u-empty>-->
<!--              </template>-->
<!--              <template v-else>-->
<!--                <view-->
<!--                    v-for="item in specialProducts"-->
<!--                    :key="item.productId"-->
<!--                    class="hot-sales-item"-->
<!--                    @click="toDetail(item.productId)"-->
<!--                >-->
<!--                  <image-->
<!--                      :src="item.attribute.carouselUrl"-->
<!--                      mode="aspectFill"-->
<!--                      class="product-image"-->
<!--                  />-->
<!--                  <view class="product-info">-->
<!--                    <text class="product-name text-cut-2">{{ item.attribute.desc }}</text>-->
<!--                    <view class="product-price">-->
<!--                      <text class="price-symbol">¥</text>-->
<!--                      <text class="price-value">{{ item.price }}</text>-->
<!--                      <view class="cart-icon">-->
<!--                        <up-icon name="shopping-cart" size="40rpx" color="#fff"></up-icon>-->
<!--                      </view>-->
<!--                    </view>-->
<!--                  </view>-->
<!--                </view>-->
<!--              </template>-->
<!--            </view>-->
            <view class="hot-sales-grid">
                <template v-for="item in 9" :key="item">
                  <view
                      class="hot-sales-item"
                  >
                    <image
                        src="/pagesA/static/blackTea.webp"
                        mode="aspectFill"
                        class="product-image"
                    />
                    <view class="product-info">
                      <text class="product-name text-cut-2">简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介</text>
                      <view class="product-price">
                        <view class="flex items-center gap-2">
                          <view>
                            <text class="price-symbol">¥</text>
                            <text class="price-value">100</text>
                          </view>
                          <view class="line-through text-2.5 fw-500 color-#949494">
                            <text>¥</text>
                            <text>100</text>
                          </view>
                        </view>
                        <view class="cart-icon">
                          <up-icon name="shopping-cart" size="40rpx" color="#fff"></up-icon>
                        </view>
                      </view>
                    </view>
                  </view>
                </template>
            </view>
          </view>
        </view>
    </view>
</template>

<style scoped>
    .nav {
        min-width: 170px;
        display: flex;
        padding: 10px 18px;
        color: #090909;
        font-size: 18px;
        border-radius: 20px;
        /* background: #e8e8e8; */
        background-color: #fff;
        cursor: pointer;
        border: 1px solid #e8e8e8;
        transition: all 0.3s;
        box-shadow: 6px 6px 12px #c5c5c5, -6px -6px 12px #ffffff;
    }

    .goods_item {
        margin-left: 20px;
        background: white;
        border-radius: 10px;
        transition: border-radius 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        box-shadow: inset 0 -3em 3em rgba(0,0,0,0.1),
             0 0  0 2px rgb(190, 190, 190),
             0.3em 0.3em 1em rgba(0,0,0,0.3);
    }

    .goods_item:first-child {
        margin-left: 0;
    }

    .old_price {
        text-decoration: line-through;
        font-size: 14px;
        font-weight: 500;
    }

    .hot_card {
        padding: 10px;
        border-radius: 20px;
        border: 2rpx solid #1E4F23;
        background: #e0e0e0;
        box-shadow: 20px 20px 60px #bebebe,
                -20px -20px 60px #ffffff;
    }

    .content {
        font-size: 14px;
        /* 设置最大高度为300px */
        max-height: 80px;
        /* 隐藏超出的内容 */
        overflow: hidden;
        /* 显示省略号来表示被截断的文本，需要设置文本的装饰方式 */
        display: -webkit-box;
        -webkit-line-clamp: 4; /* 用来限制在一个块元素显示的文本的行数 */
        -webkit-box-orient: vertical;
        text-overflow: ellipsis;
        /* 需要设置这个才能使得省略号生效 */
        white-space: normal;
    }

    .hot-sales-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 30rpx 0;
    }

    .more-btn {
      display: flex;
      align-items: center;
      gap: 4rpx;
    }

    .hot-sales-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 20rpx;
    }

    .hot-sales-item {
      background: #fff;
      border-radius: 16rpx;
      overflow: hidden;
      box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
      transition: transform 0.3s ease;
    }

    .hot-sales-item:active {
      transform: scale(0.98);
    }

    .product-image {
      width: 100%;
      height: 300rpx;
      background: #f5f5f5;
    }

    .product-info {
      padding: 20rpx;
    }

    .product-name {
      font-size: 28rpx;
      color: #333;
      line-height: 1.4;
      height: 80rpx;
      overflow: hidden;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      margin-bottom: 16rpx;
    }

    .product-price {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .price-symbol {
      font-size: 24rpx;
      color: #ff4d4f;
    }

    .price-value {
      font-size: 32rpx;
      font-weight: bold;
      color: #ff4d4f;
      margin-right: auto;
    }

    .cart-icon {
      width: 60rpx;
      height: 60rpx;
      background: #1E4F23;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4rpx 8rpx rgba(30, 79, 35, 0.2);
    }

    /* 添加文本截断工具类 */
    .text-cut-2 {
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    /* 优化原有热门推荐卡片样式 */
    .hot_card {
      padding: 20rpx;
      border-radius: 16rpx;
      border: 2rpx solid #1E4F23;
      background: #fff;
      box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
      margin-bottom: 20rpx;
      transition: transform 0.3s ease;
    }

    .hot_card:active {
      transform: scale(0.98);
    }
</style>

