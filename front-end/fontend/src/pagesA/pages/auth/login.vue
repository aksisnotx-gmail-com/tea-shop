<script setup>
import { loginApi } from "@/api/auth";
import { setToken } from "@/utils/auth";
import { useUserStore } from '@/store/modules/user'

const userStore = useUserStore()

const form = ref({
  phoneNumber: '',
  pwd: ''
})

const isShowPwd = ref(false)
const onCheckPwd = () => {
  isShowPwd.value = !isShowPwd.value
}

const login = async () => {
  const phoneNumber = form.value.phoneNumber.toString().trim()
  // 这里处理登录逻辑
  if (!phoneNumber || !form.value.pwd) {
    uni.showToast({
      title: '请输入账号和密码',
      icon: 'none'
    })
    return
  }

  try {
    const res = await loginApi({ phoneNumber, pwd: form.value.pwd })
    if(res.code === 200) {
      setToken(res.data.token)
      userStore.setUserInfo(res.data)
      uni.showToast({
        title: '登录成功',
        icon: 'none'
      })
      uni.reLaunch({
        url: '/pages/home/index'
      })
    } else {
      uni.showToast({
        title: res.message || '登录失败',
        icon: 'none'
      })
    }
  }catch (e) {
    uni.showToast({
      title: res.message || '登录失败',
      icon: 'none'
    })
  }

}

const register = () => {
  // 跳转到注册页面
  uni.navigateTo({
    url: '/pagesA/pages/auth/register'
  })
}
</script>

<template>
  <view class="login-container">
    <!-- 背景图 -->
    <view class="login-bg">
      <image
        src="/pagesA/static/tea-bg.webp"
        mode="aspectFill"
        class="bg-image"
      />
    </view>

    <!-- 登录表单 -->
    <view class="login-form">
      <view class="form-item">
        <view class="u-demo-block__content">
          <!-- 注意：由于兼容性差异，如果需要使用前后插槽，nvue下需使用u--input，非nvue下需使用u-input -->
          <!-- #ifndef APP-NVUE -->
          <up-input
              v-model="form.phoneNumber"
              placeholder="请输入账号"
              border="none"
              :customStyle="{
                background: '#fff',
                padding: '24rpx',
                borderRadius: '8rpx'
              }"
          >
            <!-- #endif -->
            <!-- #ifdef APP-NVUE -->
            <up-input
                v-model="form.phoneNumber"
                placeholder="请输入账号"
                border="none"
                :customStyle="{
                  background: '#fff',
                  padding: '24rpx',
                  borderRadius: '8rpx'
                }"
            >
              <!-- #endif -->
              <template #prefix>
                <up-icon name="account-fill" color="#949494" size="28"></up-icon>
              </template>
              <!-- #ifndef APP-NVUE -->
            </up-input>
            <!-- #endif -->
            <!-- #ifdef APP-NVUE -->
          </up-input>
          <!-- #endif -->
        </view>
      </view>

      <view class="form-item">
        <view class="u-demo-block__content">
          <!-- 注意：由于兼容性差异，如果需要使用前后插槽，nvue下需使用u--input，非nvue下需使用u-input -->
          <!-- #ifndef APP-NVUE -->
          <up-input
              v-model="form.pwd"
              :type="isShowPwd ? 'text' : 'password'"
              placeholder="请输入密码"
              border="none"
              :customStyle="{
                background: '#fff',
                padding: '24rpx',
                borderRadius: '8rpx'
              }"
          >
            <!-- #endif -->
            <!-- #ifdef APP-NVUE -->
            <up-input
                v-model="form.pwd"
                :type="isShowPwd ? 'text' : 'password'"
                placeholder="请输入密码"
                border="none"
                :customStyle="{
                  background: '#fff',
                  padding: '24rpx',
                  borderRadius: '8rpx'
                }"
            >
              <!-- #endif -->
              <template #prefix>
                <up-icon :name="isShowPwd ? 'lock-opened-fill' : 'lock-fill'" color="#949494" size="28"></up-icon>
              </template>
              <template #suffix>
                <up-icon :name="isShowPwd ? 'eye-fill' : 'eye-off'" color="#949494" size="28" @click="onCheckPwd"></up-icon>
              </template>
              <!-- #ifndef APP-NVUE -->
            </up-input>
            <!-- #endif -->
            <!-- #ifdef APP-NVUE -->
          </up-input>
          <!-- #endif -->
        </view>
      </view>

      <view class="btn-group">
        <button
          class="login-btn"
          @click="login"
        >登录</button>

        <button
          class="register-btn"
          @click="register"
        >注册</button>
      </view>

      <view class="forget-pwd">
        <text>忘记密码?</text>
      </view>
    </view>
  </view>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  background: #1E4F23;
}

.login-bg {
  padding: 20rpx 32rpx;
  height: 50vh;
  overflow: hidden;
}

.bg-image {
  width: 100%;
  height: 100%;
}

.login-form {
  padding: 40rpx;
  border-radius: 40rpx 40rpx 0 0;
  background: #1E4F23;
}

.form-item {
  margin-bottom: 30rpx;
}

.btn-group {
  display: flex;
  gap: 20rpx;
  margin-top: 60rpx;
}

.login-btn,
.register-btn {
  flex: 1;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  border-radius: 8rpx;
  font-size: 32rpx;
  border: none;
}

.login-btn {
  background: #fff;
  color: #1E4F23;
}

.register-btn {
  background: rgba(255,255,255,0.1);
  color: #fff;
}

.forget-pwd {
  text-align: center;
  margin-top: 40rpx;
  color: rgba(255,255,255,0.6);
  font-size: 28rpx;
}

/* 去除按钮默认样式 */
button::after {
  border: none;
}

button {
  background: none;
  padding: 0;
  margin: 0;
  line-height: inherit;
}
</style>
