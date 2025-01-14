<script setup>
const onSearch = () => {
  uni.navigateTo({
    url: '/pagesA/pages/search/index'
  })
}
// 左侧分类菜单数据
const categories = ref([
  { id: 1, name: '绿茶' },
  { id: 2, name: '普洱茶' },
  { id: 3, name: '红茶' },
  { id: 4, name: '绿茶' },
  { id: 5, name: '乌龙茶' },
  { id: 6, name: '白茶' },
  { id: 7, name: '花茶' }
])

// 当前选中的分类
const currentCategory = ref(0)

// 商品列表假数据
const productList = ref([
  {
    id: 1,
    name: '【年份】白茶',
    image: '/pagesA/static/tea-bg.webp',
    price: '299.00',
    originalPrice: '399.00',
    sales: 1000,
    desc: '高山云雾茶，清香怡人'
  },
  {
    id: 2,
    name: '【年份】花茶',
    image: '/pagesA/static/tea-bg.webp',
    price: '199.00',
    originalPrice: '259.00',
    sales: 800,
    desc: '花香四溢，回味悠长'
  },
  {
    id: 3,
    name: '【年份】绿茶',
    image: '/pagesA/static/tea-bg.webp',
    price: '159.00',
    originalPrice: '199.00',
    sales: 1200,
    desc: '春茶上市，鲜爽醇厚'
  }
])

// 切换分类
const switchCategory = (index) => {
  if (currentCategory.value === index) return
  currentCategory.value = index
  // TODO: 加载对应分类的商品数据
}

// 跳转到商品详情
const toDetail = (id) => {
  uni.navigateTo({
    url: `/pagesA/pages/goodsItem/index?id=${id}`
  })
}
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
          v-for="(item, index) in categories"
          :key="item.id"
          class="category-item"
          :class="{ 'category-active': currentCategory === index }"
          @click="switchCategory(index)"
        >
          {{ item.name }}
        </view>
      </scroll-view>

      <!-- 右侧商品列表 -->
      <scroll-view scroll-y class="product-list">
        <view class="product-grid">
          <view
            v-for="item in productList"
            :key="item.id"
            class="product-item"
            @click="toDetail(item.id)"
          >
            <image
              :src="item.image"
              mode="aspectFill"
              class="product-image"
            />
            <view class="product-info">
              <text class="product-name">{{ item.name }}</text>
              <text class="product-desc">{{ item.desc }}</text>
              <view class="product-price-row">
                <view class="price-group">
                  <text class="price-symbol">¥</text>
                  <text class="price-value">{{ item.price }}</text>
                </view>
                <view class="cart-btn">
                  <up-icon name="shopping-cart" size="40rpx" color="#fff"></up-icon>
                </view>
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
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
