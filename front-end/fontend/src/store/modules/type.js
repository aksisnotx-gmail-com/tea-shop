import { defineStore } from "pinia";

import { getProductTypeApi } from '@/api/home'
export const useTypeStore = defineStore("type", {
    state: () => ({
        typeIconList:  ['hf', 'fan', 'hy'],
        productTypeList: [], // 商品类型列表
        tabbar: [],
        typeId: 0
    }),
    actions: {
        async getProductTypeList () {
            const res = await getProductTypeApi()
            if(res.code !== 200) return

            this.productTypeList = res.data.map((item, index) => ({ ...item, icon: this.typeIconList[index]}))
            this.tabbar = res.data.map(item => ({ ...item, proList: [] }))
        },
        setTypeId(id) {
            console.log("=>(type.js:24) id", id);
            this.typeId = id
        }
    }
})
