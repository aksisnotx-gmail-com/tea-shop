import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        userInfo: {
            appId: "wx6979ebcb29e15873",
            avatar: '',
            code: "",
            secret: "9719b9cd67ba4a478795e1f85e99acbf",
            nickname: '',
            phoneNumber: ''
        }
    }),
    actions: {
        setUserInfo () {
            const avatar = uni.getStorageSync('avatar')
            const nickname = uni.getStorageSync('nickname')

            this.userInfo.avatar = avatar
            this.userInfo.nickname = nickname
            console.log(this.userInfo, 'userInfo');
        }
    }
})