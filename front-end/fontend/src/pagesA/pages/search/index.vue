<script setup>
import {delSearchHistoryApi, getSearchHistoryApi, searchProductApi} from "@/api/tabbar/my";
import {addCarApi} from "@/api/tabbar/car";

const searchVal = ref('')
const showResult = ref(false) // 控制显示搜索结果还是历史记录

// 搜索历史数据
const historyList = ref([])
const recentList = ref([])
const getSearchHistory = async ()  => {
  const res = await getSearchHistoryApi()
  const { recentSearchHistory, searchHistory } = res.data
  recentList.value = recentSearchHistory
  historyList.value = searchHistory
}

const deleteHistory = async (type) => {
  const typeMenu = {
    0: '确定删除历史记录',
    1: '确定删除最近记录'
  }
  uni.showModal({
    title: '提示',
    content: typeMenu[type],
    success: function (res) {
      if (res.confirm) {
        delSearchHistoryApi(type).then(res => {
          if(res.data) {
            getSearchHistory()
            return uni.showToast({
              title: '删除成功',
              icon: 'success'
            })
          }
          uni.showToast({
            title: '删除失败',
            icon: 'fail'
          })
        })
      }
    }
  });
}

// 搜索结果数据
const searchResults = ref([])
const loading = ref(false)
const finished = ref(false)

// 添加筛选标签相关数据和方法
const tabList = [
  { name: '综合', index: 0 },
  { name: '销量', index: 1 },
  { name: '价格', index: 2 }
]
const curIndex = ref(0)

const switchTab = (index) => {
  if (curIndex.value === index) return
  curIndex.value = index
  // TODO: 根据筛选条件重新加载数据
  loadData()
}

// 搜索处理
const onSearch = async () => {
  const val = searchVal.value.toString().trim()
  if (!val) {
    uni.showToast({
      title: '请输入搜索内容',
      icon: 'none'
    })
    return
  }
  showResult.value = true
  loadData()
}

const onClickActionText = () => {
  if(showResult.value) {
    onClear()
  } else {
    onSearch()
  }
}

const onClear = async () => {
  searchVal.value = ''
  showResult.value = false
  await getSearchHistory()
}

// 点击历史记录
const onClickHistory = (keyword) => {
  const val = keyword.toString().trim()
  if (!val) {
    uni.showToast({
      title: '请输入搜索内容',
      icon: 'none'
    })
    return
  }
  searchVal.value = val
  showResult.value = true
  loadData()
}

// 加载搜索结果数据
const loadData = async () => {
  const val = searchVal.value.toString().trim()
  loading.value = true
  const res = await searchProductApi(val)
  const { records } = res.data
  searchResults.value = records
  loading.value = false
}

const addCar = async (item) => {
  console.log("=>(index.vue:119) item", item);
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

async function apiInit ()  {
  await uni.showLoading({
    title: '加载中...',
    mask: true
  })
  await getSearchHistory()
  uni.hideLoading()
}

onMounted(()   =>  {
  apiInit()
})
</script>

<template>
  <view class="search-container">
    <!-- 搜索框 -->
    <view class="search-header">
      <up-search
        v-model="searchVal"
        :actionText="showResult ? '取消' : '搜索'"
        :showAction="true"
        @search="onSearch"
        @custom="onClickActionText"
        @clear="onClear"
      ></up-search>
    </view>

    <!-- 搜索历史页面 -->
    <template v-if="!showResult">
      <view class="history-container">
        <!-- 最近搜索 -->
        <view class="section">
          <view class="flex justify-between items-center">
            <view class="search_title">最近搜索</view>
            <template v-if="recentList.length">
              <up-icon name="trash" size="20" @click="deleteHistory(1)"></up-icon>
            </template>
          </view>
          <view class="tag-list">
            <text
              v-for="item in recentList"
              :key="item"
              class="search_item"
              @click="onClickHistory(item)"
            >{{ item }}</text>
          </view>
        </view>

        <!-- 历史记录 -->
        <view class="section">
          <view class="flex justify-between items-center">
            <view class="search_title">历史记录</view>
            <template v-if="historyList.length">
              <up-icon name="trash" size="20" @click="deleteHistory(0)"></up-icon>
            </template>
          </view>
          <view class="tag-list">
            <text
              v-for="item in historyList"
              :key="item"
              class="search_item"
              @click="onClickHistory(item)"
            >{{ item }}</text>
          </view>
        </view>
      </view>
    </template>

    <!-- 搜索结果页面 -->
    <template v-else>
      <!-- 添加筛选标签 -->
      <view class="filter-tabs">
        <view
          v-for="item in tabList"
          :key="item.index"
          class="tab-item"
          :class="{ 'tab-active': curIndex === item.index }"
          @click="switchTab(item.index)"
        >
          <text>{{ item.name }}</text>
        </view>
      </view>

      <!-- 修改商品列表布局 -->
      <template v-if="!searchResults.length">
        <u-empty text="暂无商品数据" mode="data"></u-empty>
      </template>
      <template v-else>
        <scroll-view
            scroll-y
            class="result-list"
            @scrolltolower="loadData"
        >
          <view class="goods-grid">
            <view
                v-for="item in searchResults"
                :key="item.id"
                class="goods-item"
            >
              <template v-if="!item.carousel.length">
                <up-image
                    src="/static/load-error.jpg"
                    width="100rpx"
                    height="125rpx"
                    shape="circle"
                >
                </up-image>
              </template>
              <template v-else>
                <image
                    :src="item.carousel[0]"
                    mode="aspectFill"
                    class="goods-image"
                />
              </template>
              <view class="goods-info">
                <text class="goods-name">{{ item.productName }}</text>
                <view class="goods-price-row">
                  <view class="price-group">
                    <text class="price-symbol">¥</text>
                    <text class="price-value">{{ item.price }}</text>
                    <template v-if="item.isSpecial">
                      <text class="original-price">¥{{ item.specialPrice }}</text>
                    </template>
                  </view>
                  <view class="cart-btn" @click="addCar(item)">
                    <up-icon name="shopping-cart" size="40rpx" color="#fff"></up-icon>
                  </view>
                </view>
              </view>
            </view>
          </view>

          <!-- 加载状态 -->
          <view v-if="loading" class="loading-text">正在加载...</view>
          <view v-if="finished" class="loading-text">没有更多了</view>
        </scroll-view>
      </template>
    </template>
  </view>
</template>

<style scoped>
.search-container {
  min-height: 100vh;
  background: #fff;
}

.search-header {
  padding: 20rpx 30rpx;
  background: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
}

.history-container {
  padding: 0 30rpx;
}

.section {
  margin-bottom: 40rpx;
}

.search_title {
  font-size: 30rpx;
  color: #232326;
  margin-bottom: 20rpx;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.search_item {
  padding: 12rpx 40rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #686868;
  background-color: #f0f2f5;
}

.result-list {
  height: calc(100vh - 88rpx - 82rpx);
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
  margin-right: 10rpx;
}

.original-price {
  font-size: 24rpx;
  color: #999;
  text-decoration: line-through;
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

.loading-text {
  text-align: center;
  color: #999;
  padding: 20rpx 0;
  font-size: 24rpx;
}

/* 筛选标签样式 */
.filter-tabs {
  display: flex;
  padding: 20rpx 30rpx;
  background: #fff;
  border-bottom: 1rpx solid #eee;
}

.tab-item {
  flex: 1;
  text-align: center;
  font-size: 28rpx;
  color: #666;
  position: relative;
  padding: 10rpx 0;
}

.tab-active {
  color: #1E4F23;
  font-weight: 500;
}

.tab-active::after {
  content: '';
  position: absolute;
  bottom: -10rpx;
  left: 50%;
  transform: translateX(-50%);
  width: 40rpx;
  height: 4rpx;
  background: #1E4F23;
  border-radius: 2rpx;
}

/* 商品网格布局样式 */
.goods-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  padding: 20rpx;
}

.goods-item {
  background: #fff;
  border-radius: 12rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.goods-image {
  width: 100%;
  height: 340rpx;
  background: #f5f5f5;
}

.goods-info {
  padding: 20rpx;
}

.goods-name {
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

.goods-price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 修改原有的价格和购物车按钮样式 */
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
  margin-right: 10rpx;
}

.original-price {
  font-size: 24rpx;
  color: #999;
  text-decoration: line-through;
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

/* 调整结果列表的高度，考虑筛选标签的高度 */
.result-list {
  height: calc(100vh - 88rpx - 82rpx);
}
</style>
