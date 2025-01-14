<script setup>
import { getProductByTypeApi } from '@/api/home'
import { useGoodsStore } from '@/store/modules/goods'
import { useTypeStore } from '@/store/modules/type'

const goodsStore = useGoodsStore()
const typeStore = useTypeStore()
const { tabbar } = storeToRefs(typeStore)

const pageInfo = reactive({
  current: 1,
  size: 20,
  total: 0
})

const viewInfo = reactive({
  scrollTop: 0,
  current: 0,
  menuHeight: 0,
  menuItemHeight: 0
})

const tabbarList = [
  {
    id: 1,
    title: '茶艺起源',
    imageUrl: 'teaArtOrigin',
    text: '茶艺起源于中国，最早可以追溯到汉唐时期。当时人们不仅注重茶叶的饮用价值，还将其与文化礼仪相结合，逐步形成了以"和、敬、清、静"为核心的茶艺理念。唐代陆羽《茶经》的问世标志着茶艺理论的初步确立，宋代点茶之风和文人雅集更推动了茶艺的文化深化，最终形成了以艺术性、仪式感和文化内涵为核心的传统技艺。'
  },
  {
    id: 2,
    title: '制茶起源',
    imageUrl: 'teaMakingOrigin',
    text: '制茶的起源可追溯至中国古代神农时期，传说神农尝百草时发现茶叶有解毒功效。最早的制茶工艺较为简单，多为鲜叶煮饮或晒干保存。至唐代，蒸青团茶成为主流；宋代发展为磨粉点茶；明代朱元璋提倡散茶，炒青工艺兴起，奠定了现代制茶的基础。'
  },
  {
    id: 3,
    title: '茶器起源',
    imageUrl: 'teaUtensilsOrigin',
    text: '茶器的起源与茶文化发展密不可分，最早的茶器多为陶器，用于煮茶或盛茶。唐代开始出现专门的茶碗，宋代因点茶流行，建盏成为代表性茶器。明清时期，随着散茶和功夫茶的普及，瓷器茶具逐渐成为主流，形式更加丰富，工艺更加精致，形成了多元化的茶器文化。'
  }
]

const switchMenu = (index) => {
  if(index === viewInfo.current) return
  viewInfo.current = index
}

const dataVal = ref(0)

const getElRect = (elClass) => {
  new Promise((resolve, reject) => {
    const query = uni.createSelectorQuery()
    query.select('.' + elClass).fields({size: true},res => {
      // 如果节点尚未生成，res值为null，循环调用执行
      if(!res) {
        setTimeout(() => {
          getElRect(elClass);
        }, 10);
        return ;
      }
      dataVal.value = res.height;
      resolve(res.height)
    }).exec();
  })
}

onLoad((option) => {
  const { typeId } = option

  const index = tabbar.value.findIndex(item => item.id === typeId)
  viewInfo.current = index

  getProductByType(typeId)
})


const onJumpDetail = (id) => {
  goodsStore.productId = id
  uni.navigateTo({
    url: '/pagesA/pages/goodsItem/index'
  })
}

async function getProductByType (typeId, currentPage = 1) {
  if(!typeId) return

  const proLen = tabbar.value[viewInfo.current].proList.length
  tabbar.value[viewInfo.current].proList.splice(0, proLen)

  const res = await getProductByTypeApi(typeId, currentPage)
  const { records, current, size, total } = res.data
  pageInfo.current = current
  pageInfo.size = size
  pageInfo.total = total
  const len = records.length
  if(len) {
    tabbar.value[viewInfo.current].proList = [ ...records ]
  }
}

onReachBottom(async () => {
  uni.showLoading({
    title: '加载中'
  });

  const currentTotal = pageInfo.current * pageInfo.size

  if(currentTotal < pageInfo.total) {
    pageInfo.current++
    await getProductByType(pageInfo.current)
    uni.hideLoading()
  } else {
    uni.hideLoading()
    uni.showToast({
      title: '没有更多了',
      icon: 'error',
      mask: true,
      duration: 1000
    })
  }
})
</script>


<template>
  <view class="tea-culture">
    <view class="content">
      <!-- 左侧菜单 -->
      <scroll-view
        scroll-y
        class="menu-scroll-view"
        :scroll-top="viewInfo.scrollTop"
      >
        <view
          v-for="(item, index) in tabbarList"
          :key="item.id"
          class="menu-item"
          :class="{'menu-item-active': viewInfo.current === index}"
          @click="switchMenu(index)"
        >
          <text class="menu-text">{{ item.title }}</text>
        </view>
      </scroll-view>

      <!-- 右侧内容 -->
      <scroll-view
        scroll-y
        class="content-scroll-view"
      >
        <view class="content-inner">
          <!-- 图片展示区域 -->
          <view class="image-section">
            <image
              :src="`/pagesA/static/${tabbarList[viewInfo.current].imageUrl}.webp`"
              mode="aspectFill"
              class="content-image"
            />
          </view>

          <!-- 标题 -->
          <view class="content-title">
            {{ tabbarList[viewInfo.current].title }}
          </view>

          <!-- 内容文字 -->
          <view class="content-text">
            {{ tabbarList[viewInfo.current].text }}
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<style scoped>
.tea-culture {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.header {
  height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #1E4F23;
}

.title {
  color: #fff;
  font-size: 36rpx;
  font-weight: 500;
}

.content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.menu-scroll-view {
  width: 200rpx;
  height: calc(100vh - 88rpx);
  background: #fff;
}

.menu-item {
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: #f6f6f6;
  padding: 0 20rpx;
}

.menu-item-active {
  background: #fff;
  font-weight: bold;
}

.menu-item-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 8rpx;
  height: 40rpx;
  background: #1E4F23;
  border-radius: 0 4rpx 4rpx 0;
}

.menu-text {
  font-size: 28rpx;
  color: #333;
  text-align: center;
}

.content-scroll-view {
  flex: 1;
  height: calc(100vh - 88rpx);
  background: #f5f5f5;
  padding: 20rpx 0;
}

.image-section {
  width: 100%;
  height: 360rpx;
  margin-bottom: 30rpx;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  position: relative;
}

.content-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.image-section:hover .content-image {
  transform: scale(1.05);
}

.content-inner {
  padding: 30rpx;
  background: #fff;
  border-radius: 20rpx;
  margin: 20rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.content-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin: 30rpx 0;
  padding-left: 20rpx;
  border-left: 8rpx solid #1E4F23;
  line-height: 1.4;
}

.content-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
  text-align: justify;
  padding: 30rpx;
  background: #f8f8f8;
  border-radius: 12rpx;
  margin-top: 20rpx;
}

/* 优化滚动条样式 */
.content-scroll-view ::-webkit-scrollbar {
  width: 6rpx;
  background: transparent;
}

.content-scroll-view ::-webkit-scrollbar-thumb {
  background: rgba(30, 79, 35, 0.2);
  border-radius: 3rpx;
}

/* 添加内容切换动画 */
.content-inner {
  animation: fadeIn 0.3s ease;
}

.image-section {
  animation: slideIn 0.5s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(30rpx);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 优化右侧内容区域的整体样式 */
.content-scroll-view {
  flex: 1;
  height: calc(100vh - 88rpx);
  background: #f5f5f5;
  padding: 20rpx 0;
}

/* 适配暗色模式 */
@media (prefers-color-scheme: dark) {
  .content-text {
    background: #2a2a2a;
    color: #bbb;
  }

  .content-inner {
    background: #333;
  }

  .content-title {
    color: #fff;
  }
}
</style>

