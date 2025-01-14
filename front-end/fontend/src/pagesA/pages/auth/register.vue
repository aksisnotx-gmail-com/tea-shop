<script setup>
import {registerApi} from "@/api/auth";

const form = reactive({
  username: '',
  gender: 0,
  phoneNumber: '',
  pwd: ''
})

// 输入框统一样式
const inputStyle = {
  background: '#f5f5f5',
  padding: '24rpx',
  borderRadius: '8rpx'
}

const isShowPwd = ref(false)
const onCheckoutPwd = () => {
  isShowPwd.value = !isShowPwd.value
}

const onRegister = async () => {
  // 表单验证
  if (!form.username) {
    uni.showToast({
      title: '请输入姓名',
      icon: 'none'
    })
    return
  }
  if (!form.phoneNumber) {
    uni.showToast({
      title: '请输入手机号',
      icon: 'none'
    })
    return
  }
  if (!form.pwd) {
    uni.showToast({
      title: '请输入登录密码',
      icon: 'none'
    })
    return
  }

  try {
    const res = await registerApi(form)
    if(res.data) {
      uni.showToast({
        title: '注册成功',
        icon: 'none'
      })
      onBack()
    } else  {
      uni.showToast({
        title: res.message || '注册失败',
        icon: 'none'
      })
    }
  } catch (e) {

  }
}

const onBack = () => {
  uni.navigateBack()
}
</script>

<template>
  <view class="register-container">
    <!-- 注册表单 -->
    <view class="register-form">
      <!-- 姓名 -->
      <view class="form-item">
        <text class="form-label">姓名</text>
        <up-input
          v-model="form.username"
          placeholder="请输入姓名"
          border="none"
          :customStyle="inputStyle"
        />
      </view>

      <!-- 性别 -->
      <view class="form-item">
        <text class="form-label">性别</text>
        <up-radio-group v-model="form.gender" activeColor="#1E4F23">
          <up-radio shape="circle" label="男" :name="0"></up-radio>
          <up-radio shape="circle" label="女" :name="1"></up-radio>
        </up-radio-group>
      </view>

      <!-- 手机号 -->
      <view class="form-item">
        <text class="form-label">手机号</text>
        <up-input
          v-model="form.phoneNumber"
          placeholder="请输入手机号"
          type="number"
          maxlength="11"
          border="none"
          :customStyle="inputStyle"
        />
      </view>

      <!-- 登录密码 -->
      <view class="form-item">
        <text class="form-label">登录密码</text>
        <up-input
          v-model="form.pwd"
          placeholder="请输入登录密码"
          :type="isShowPwd ? 'text' : 'password'"
          border="none"
          :customStyle="inputStyle"
        >
          <template #suffix>
            <up-icon
              :name="isShowPwd ? 'eye-fill' : 'eye-off'"
              color="#949494"
              size="28"
              @click="onCheckoutPwd"
            ></up-icon>
          </template>
        </up-input>
      </view>
    </view>

    <!-- 按钮组 -->
    <view class="btn-group">
      <button class="register-btn" @click="onRegister">注册</button>
      <button class="cancel-btn" @click="onBack">返回</button>
    </view>
  </view>
</template>

<style scoped>
.register-container {
  min-height: 100vh;
  background: #1E4F23;
  padding: 0 30rpx;
}

.register-form {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  color: #333;
  font-size: 28rpx;
  margin-bottom: 16rpx;
}

.btn-group {
  margin-top: 60rpx;
  display: flex;
  justify-content: space-between;
  gap: 20rpx;
}

.register-btn,
.cancel-btn {
  flex: 1;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  border-radius: 8rpx;
  font-size: 32rpx;
  border: none;
}

.register-btn {
  background: #fff;
  color: #1E4F23;
}

.cancel-btn {
  background: rgba(255,255,255,0.1);
  color: #fff;
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
