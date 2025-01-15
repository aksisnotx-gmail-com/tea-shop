<script setup>
import {
  getSwiperListApi,
  getTeaTypeApi,
  getRecommendProductsApi
} from '@/api/home'
    import { useGoodsStore } from '@/store/modules/goods'
    import { useTypeStore } from '@/store/modules/type'
    import {onPullDownRefresh} from "@dcloudio/uni-app";

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

    // const teaType = [
    //   {
    //     id: 1,
    //     imageUrl: 'scentedTea',
    //     typeName: '花茶'
    //   },
    //   {
    //     id: 2,
    //     imageUrl: 'blackTea',
    //     typeName: '红茶'
    //   },
    //   {
    //     id: 3,
    //     imageUrl: 'whiteTea',
    //     typeName: '白茶'
    //   },
    //   {
    //     id: 4,
    //     imageUrl: 'rawTea',
    //     typeName: '生茶'
    //   },
    //   {
    //     id: 5,
    //     imageUrl: 'ripeTea',
    //     typeName: '熟茶'
    //   },
    // ]
    const teaType = ref()
    const getTeaType = async () => {
      const res = await getTeaTypeApi()
      teaType.value = [ ...res.data?.records  ]
    }

    const recommendProducts = ref([])
    const getRecommendProducts = async () => {
        const res = await getRecommendProductsApi()
        if(res.data.records.length) {
          recommendProducts.value = [ ...res.data.records ]
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

   async function apiInit () {
      await getSwiperList()
      await getTeaType()
      await getRecommendProducts()
     // await typeStore.getProductTypeList()
     // uni.hideLoading()
    }
    onMounted( () => {
      apiInit()
    })

    // onLoad(() => {
    //     uni.showLoading({
    //         title: '加载中'
    //     });
    // })

    onPullDownRefresh(() => {
      apiInit()
      setTimeout(() => {
        uni.stopPullDownRefresh()
      }, 1000)
    })

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
          >
          </up-image>
        </view>
        <view class="px-4 py-3 flex justify-between flex-wrap">
          <template v-for="item of teaType" :key="item.id">
            <view class="flex flex-col gap-3 items-center">
            <template v-if="!item.imgUrl">
              <up-image
                  src="/static/load-error.jpg"
                  width="100rpx"
                  height="125rpx"
                  shape="circle"
              >
              </up-image>
            </template>
            <template v-else>
              <up-image
                  :src="item.imgUrl"
                  width="100rpx"
                  height="125rpx"
                  shape="circle"
              >
              </up-image>
            </template>
              <text class="text-3.25 color-bluegray">{{ item.type }}</text>
            </view>
          </template>
        </view>

        <view class="px-4 mt-3 pb-4">
          <view class="bg-#fff p-2 rounded-2">
            <view class="py-3">
              <text class="text-5 font-600">热销茶叶</text>
            </view>

            <view class="hot-sales-grid">
              <template v-if="!recommendProducts.length">
                <u-empty text="暂无商品数据" mode="data"></u-empty>
              </template>
              <template v-else>
                <template v-for="item of recommendProducts" :key="item.id">
                    <view
                        class="hot-sales-item"
                    >
                      <template v-if="!item.carousel.length">
                        <image
                            src="/static/load-error.jpg"
                            mode="aspectFill"
                            class="product-image"
                        />
                      </template>
                      <template v-else>
                        <image
                            :src="item.carousel[0]"
                            mode="aspectFill"
                            class="product-image"
                        />
                      </template>
                      <view class="product-info">
                        <text class="product-name text-cut-2">{{ item.productName }}</text>
                        <view class="product-price">
                          <view class="flex items-center gap-2">
                            <template v-if="item?.isSpecial">
                              <view>
                                <text class="price-symbol">¥</text>
                                <text class="price-value">{{ item.specialPrice }}</text>
                              </view>
                              <view class="line-through text-2.5 fw-500 color-#949494">
                                <text>¥</text>
                                <text>{{ item.price }}</text>
                              </view>
                            </template>
                            <template v-else>
                              <view>
                                <text class="price-symbol">¥</text>
                                <text class="price-value">{{ item.price }}</text>
                              </view>
                            </template>
                          </view>
                          <view class="cart-icon">
                            <up-icon name="shopping-cart" size="40rpx" color="#fff"></up-icon>
                          </view>
                        </view>
                      </view>
                    </view>
                </template>
              </template>
            </view>
          </view>
        </view>
    </view>

</template>

<style scoped>
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
</style>

