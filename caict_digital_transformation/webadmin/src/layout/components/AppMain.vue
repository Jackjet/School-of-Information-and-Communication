<!--
 * @Author: your name
 * @Date: 2020-09-10 19:00:18
 * @LastEditTime: 2020-09-27 14:04:29
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\layout\components\AppMain.vue
-->
<template>
  <section class="app-main">
    <div class="main-top">
    </div>
    <transition name="fade-transform" mode="out-in">
      <router-view :key="key" />
    </transition>
  </section>
</template>
<script>
export default {
  name: 'AppMain',
  computed: {
    key() {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.app-main {
  /*50 = navbar  */
  min-height: calc(100vh - 0px);
  width: 100%;
  position: relative;
  background: #f8f8f8;
  /* overflow: hidden; */
}
.main-top{
  height: 287px;
  margin-top: 38px;
  /* position: relative; */
  background: url('../../assets/image/bottom.png');
  background-size: 100% 100%;
}
.fixed-header+.app-main {
  padding-top: 50px;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>
