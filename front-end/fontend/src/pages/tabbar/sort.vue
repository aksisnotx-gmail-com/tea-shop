<script setup>
import {getProductTypeApi} from "@/api/home";
import {getAllProductByTypeApi} from "@/api/tabbar/watch";
import {useTypeStore} from "@/store/modules/type";
import {onShow} from "@dcloudio/uni-app";
import {addCarApi} from "@/api/tabbar/car";

const typeStore = useTypeStore()

const onSearch = () => {
  uni.navigateTo({
    url: '/pagesA/pages/search/index'
  })
}
// 左侧分类菜单数据
const categories = ref([])

const getCategories = async () => {
  const res = await getProductTypeApi()
  if(!res.data?.records.length) return
  categories.value = res.data.records.map(item => ({ id: item.id, name: item.type }))
  currentCategoryId.value = categories.value[0].id
}

// 当前选中的分类
const currentCategoryId = ref(0)

// 商品列表假数据
const productList = ref([])

// 切换分类
const switchCategoryFlag = ref(false)
const switchCategory = async (id) => {
  if (currentCategoryId.value === id && switchCategoryFlag.value) return
  switchCategoryFlag.value = true
  currentCategoryId.value = id
  const res = await getAllProductByTypeApi(id)
  productList.value = res.data.records
}

const addCar = async (item) => {
  const { id } = item
  try {
    const res = await addCarApi({ productId: id, number: 1})
    if(res.data) {
      uni.showToast({
        title: '加入购物车成功',
        icon: 'success',
        duration: 2000
      })
    } else {
      uni.showToast({
        title: '加入购物车失败',
        icon: 'error',
        duration: 2000
      })
    }
  } catch (e) {
    uni.showToast({
      title: '下单失败',
      icon: 'error',
    })
  }
}

// 跳转到商品详情
const toDetail = (id) => {
  uni.navigateTo({
    url: `/pagesA/pages/goodsItem/index?id=${id}`
  })
}

async function apiInit () {
  await uni.showLoading({
    title: '加载中...',
    mask: true
  })
  await getCategories()
  const firstTypeId = typeStore.typeId ?  typeStore.typeId : currentCategoryId.value
  await switchCategory(firstTypeId)
  uni.hideLoading()
}

onShow(() => {
  apiInit()
})
</script>

<template>
  <view class="sort-container">
    <!-- 搜索框 -->
    <view class="search-box">
      <up-search
        placeholder="搜索商品"
        disabled
        @click="onSearch"
      ></up-search>
    </view>

    <view class="content">
      <!-- 左侧分类菜单 -->
      <scroll-view scroll-y class="category-menu">
        <view
          v-for="item in categories"
          :key="item.id"
          class="category-item"
          :class="{ 'category-active': currentCategoryId === item.id }"
          @click="switchCategory(item.id)"
        >
          {{ item.name }}
        </view>
      </scroll-view>

      <!-- 右侧商品列表 -->
      <template v-if="!productList.length">
        <u-empty text="暂无商品数据" mode="data"></u-empty>
      </template>
      <template v-else>
        <scroll-view scroll-y class="product-list">
          <view class="product-grid">
            <view
                v-for="item in productList"
                :key="item.id"
                class="product-item"
                @click="toDetail(item.id)"
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
                <text class="product-name">{{ item.productName }}</text>
                <view class="product-price-row">
                  <view class="flex gap-2">
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
                  <view class="cart-btn" @click.stop="addCar(item)">
                    <up-icon name="shopping-cart" size="40rpx" color="#fff"></up-icon>
                  </view>
                </view>
              </view>
            </view>
          </view>
        </scroll-view>
      </template>
    </view>
  </view>
</template>

<style scoped>
.sort-container {
  min-height: 100vh;
  background: #f5f5f5;
  display: flex;
  flex-direction: column;
}

.search-box {
  padding: 20rpx 30rpx;
  background: #fff;
}

.content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

/* 左侧分类菜单样式 */
.category-menu {
  width: 180rpx;
  height: calc(100vh - 88rpx);
  background: #fff;
}

.category-item {
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  color: #333;
  position: relative;
  background: #f8f8f8;
}

.category-active {
  background: #fff;
  color: #1E4F23;
  font-weight: 500;
}

.category-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 6rpx;
  height: 36rpx;
  background: #1E4F23;
  border-radius: 0 4rpx 4rpx 0;
}

/* 右侧商品列表样式 */
.product-list {
  flex: 1;
  height: calc(100vh - 88rpx);
  background: #fff;
}

.product-grid {
  padding: 20rpx;
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 20rpx;
}

.product-item {
  background: #fff;
  border-radius: 12rpx;
  overflow: hidden;
  display: flex;
  padding: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.product-image {
  width: 200rpx;
  height: 200rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 10rpx;
}

.product-desc {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 20rpx;
}

.product-price-row {
  margin-top: auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price-group {
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 24rpx;
  color: #ff4d4f;
}

.price-value {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.cart-btn {
  width: 60rpx;
  height: 60rpx;
  background: #1E4F23;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
