import { defineStore } from 'pinia'
import { getLocalUserInfo, setLocalUserInfo } from "@/utils/auth";

export const useUserStore = defineStore('user', {
    state: () => ({
        userInfo: {}
    }),
    actions: {
        setUserInfo (userInfo) {
            setLocalUserInfo(userInfo)
            this.userInfo = { ...userInfo }
        },
        getUserInfo() {
            this.userInfo = this.userInfo?.id ? this.userInfo : getLocalUserInfo()
        }
    }
})
