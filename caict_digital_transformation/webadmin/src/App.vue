<!--
 * @Author: your name
 * @Date: 2020-09-10 19:00:16
 * @LastEditTime: 2020-09-28 12:29:56
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\App.vue
-->
<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import {sessionStorageSet, sessionStorageGet} from '@/utils/storage'

export default {
  name: "App",
  data() {
  },
  created() {
    //在页面加载时读取sessionStorage里的状态信息
    if (sessionStorage.getItem('store')) {
      this.$store.replaceState(
        Object.assign(
          {},
          this.$store.state,
          JSON.parse(sessionStorage.getItem('store'))
        )
      )
    }
    //在页面刷新时将vuex里的信息保存到sessionStorage里
    //beforeunload事件在页面刷新时先触发
    window.addEventListener('beforeunload', () => {
      sessionStorage.setItem('store', JSON.stringify(this.$store.state))
    })
  },

};
</script>
<style lang="scss">
.el-message {
  min-width: 270px !important;
}

</style>
