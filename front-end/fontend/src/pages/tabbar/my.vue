<script setup>
    import { useUserStore } from '@/store/modules/user'

    const userStore = useUserStore()

    // const chooseImage = () => {
    //     uni.chooseImage({
    //             count: 1, //最多可以选择的图片张数，默认9
    //             sizeType: ['compressed'], //original 原图，compressed 压缩图，默认二者都有
    //             sourceType: ['album'], //album 从相册选图，camera 使用相机，默认二者都有。如需直接开相机或直接选相册，请只使用一个选项
    //             success: (res) => { //成功返回的函数
    //                 const imageSrc = res.tempFilePaths[0] //将图片的地址赋值给imageSrc
    //                 uni.uploadFile({ //上传图片
    //                     url: env.fileUrl, //开发者服务器 url
    //                     filePath: imageSrc, //要上传文件资源的路径。
    //                     fileType: 'image', //文件类型，image/video/audio
    //                     name: 'file', //文件对应的 key , 开发者在服务器端通过这个 key 可以获取到文件二进制内容
    //                     success: (res) => { //接口调用成功的回调函数
    //                         uni.showToast({ //消息提示框
    //                             title: '上传成功',
    //                             icon: 'success',
    //                             duration: 1000
    //                         }),
    //                         uni.setStorage({
    //                             key:'bgImg',
    //                             data: imageSrc
    //                         })
    //                         imageUrl.value = imageSrc
    //                     },
    //                     fail: (err) => { //接口调用失败的回调函数
    //                         uni.showModal({ //消息提示框
    //                             content: err.errMsg, //错误信息
    //                             showCancel: false
    //                         });
    //                     }
    //                 });
    //             },
    //             fail: (err) => { //图片接口调用失败的回调函数
    //                 console.log('chooseImage fail', err)
    //
    //             // 判断是否允许调用摄像头
    //             uni.getSetting({
    //                 success: (res) => {
    //                     let authStatus = res.authSetting['scope.album'];
    //                     if (!authStatus) {
    //                         uni.showModal({
    //                             title: '授权失败',
    //                             content: 'Hello uni-app需要从您的相册获取图片，请在设置界面打开相关权限',
    //                             success: (res) => {
    //                             if (res.confirm) {
    //                                 uni.openSetting()
    //                             }
    //                     }
    //                 })
    //                     }
    //                 }
    //             })
    //             }
    //     })
    // }


    const orderStatus = [
        {
            id: 1,
            url: 'waitPay.svg',
            name: '待付款'
        },
        {
            id: 2,
            url: 'receiv.svg',
            name: '待收货'
        },
        {
            id: 3,
            url: 'evaluat.svg',
            name: '待评价'
        },
        {
            id: 4,
            url: 'allOrder.svg',
            name: '全部订单'
        }
    ]

    const onClickStatus = (index) => {
        uni.navigateTo({
            url: `/pagesA/pages/my/order?index=${index}`
        })
    }

    const useCenter = [
        {
            id: 1,
            icon: 'hb.svg',
            title: '余额充值'
        },
        {
            id: 2,
            icon: 'evaluate.svg',
            title: '我的评价'
        },
        {
            id: 3,
            icon: 'address.svg',
            title: '收货地址'
        }
    ]

    const onClickFunction = (item) => {
        const { id } = item
        const obj = {
            '1': 'moneny',
            '2': 'reviewed',
        }

        if(id === 3) {
            uni.navigateTo({
                url: `/pagesA/pages/address/receiving`
            })
            return
        }

        const val = obj[id]
        if(!val) return

        uni.navigateTo({
            url: `/pagesA/pages/my/${val}`
        })
    }


    // TODO: 资料编辑 改为 忘记密码页面
    const JumpEdit = () => {
        uni.navigateTo({
            url: '/pagesA/pages/my/dataEditing'
        })
    }

    const onLogout = () => {
        uni.showModal({
            title: '提示',
            content: '确定退出吗',
            success: function (res) {
                if (res.confirm) {
                    uni.clearStorageSync()
                    uni.reLaunch({
                      url: '/pagesA/pages/auth/login'
                    })
                } else if (res.cancel) {
                    console.log('用户点击取消');
                }
            }
        });
    }

    onMounted(() => {
      userStore.getUserInfo()
    })
</script>

<template>
    <view class="w-100vw bg-#1E4F23">
        <view class="bg-#fff px-3 py-3">
          <view class="layout-items-center gap-6">
            <view><up-avatar src="/static/tabbar/avatar.jpg" size="50"></up-avatar></view>
            <view class="flex flex-col gap-3">
              <view>
                <text class="text-3.25 fw-700">昵称: </text>
                <text class="text-3.25 fw-500">{{ userStore.userInfo?.username || '' }}</text>
              </view>
              <view>
                <text class="text-3.25 fw-700">手机号码: </text>
                <text class="text-3.25 fw-500">{{ userStore.userInfo.phoneNumber || ''}}</text>
              </view>
            </view>
          </view>
        </view>
      <up-line></up-line>
        <view class="bg-#fff px-3 py-3">
            <view class="content_botton">
                <view class="fw-700">我的订单</view>
                <!--  -->
                <view class="my_order">
                    <view
                        class="my_order_item"
                        v-for="(item, index) in orderStatus"
                        :key="item.id"
                        @click="onClickStatus(index)"
                    >
                        <view class="my_order_content">
                            <image :src="'/static/tabbar/' + item.url" mode="widthFix"></image>
                            <view class="color-#666 font-600">{{item.name}}</view>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        <view class="my-3 bg-#fff p-3">
            <template
                v-for="item of useCenter"
                :key="item.id"
            >
                <view class="flex items-center" @click="onClickFunction(item)">
                    <image
                        :src="'/static/tabbar/' + item.icon"
                        mode="aspectFit"
                        class="w-8 h-8"
                    >
                    </image>
                    <view
                        :class="[
                            'h-15', 'ml-3', 'flex-1',
                            'flex', 'justify-between',
                            'items-center',
                            'border_b'
                            ]"
                        >
                        <text class="text-3.5 color-#666">{{ item.title }}</text>
                        <u-icon
                        name="arrow-right"
                        color="#FC7DA6"
                        size="28"
                        ></u-icon>
                    </view>
                </view>
            </template>
            <view class="flex items-center" @click="onLogout">
                    <image
                        src="/static/tabbar/logout.svg"
                        mode="widthFix"
                        class="w-8 h-8"
                    >
                    </image>
                    <view
                        :class="[
                            'h-15', 'ml-3', 'flex-1',
                            'flex', 'justify-between',
                            'items-center'
                            ]"
                        >
                        <text class="text-3.5 color-#666">退出登录</text>
                    </view>
                </view>
        </view>
    </view>
</template>

<style lang="scss" scoped>
    .image {
        width: 100%;
        height: 200px;
    }

    .tit {
        font-size: 40px;
        display: flex;
        justify-content: center;
        background-color: antiquewhite;
    }

    /* .bg {
        padding: 20px 12px;
    } */

    .uni-hello-addfile {
        height: 200rpx;
        text-align: center;
        font-size: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px solid #fff;
    }

    .uni-hello-addfile:active {
        background-color: aqua;
    }

    .content_botton{
      display: flex;
      flex-direction: column;
      gap: 20rpx;

	   .my_order{
		   margin-top: 10rpx;
		   border-radius: 20rpx;
           display: flex;
           justify-content: space-around;
		   .my_order_title{
			   border-radius: 20rpx;
			   padding-left: 20rpx;
			   line-height: 80rpx;
			   height: 80rpx;
		   }
		   .my_order_item{
			  .my_order_content{
				  display: flex;
				  flex-direction: column;
				  align-items: center;
				  justify-content: space-between;
                  gap: 5px;
				  image{
				  		width: 80rpx;
				  		height: 50rpx;
				  }
			  }
		   }
	   }
   }

   .border_xy {
    border: 2px solid #FF75A3;
   }

   .border_b {
    border-bottom: 1px solid #ccc;
   }
</style>
