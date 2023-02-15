<!--
 * @Author: your name
 * @Date: 2020-09-10 19:00:17
 * @LastEditTime: 2020-09-27 21:35:33
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\layout\index.vue
-->
<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
      <div v-if="device==='mobile'" class="top" :class="{colo: show}">
        <div class="logo"><logo /></div>
        <div><hor-menu class="sidebar-hcontainer" /></div>
        <div class="logos"><navbah /></div>
    </div>
    <!-- <div class="flex"><tabs  v-if="device ==='mobile'"></tabs></div> -->
    <sidebar v-if="device !=='mobile'"  class="sidebar-container" />
    <div class="main-container">
      <div :class="{'fixed-header':fixedHeader}">
        <navbar v-if="device !=='mobile'"  />
        <div v-if="device !=='mobile' && delivery === true" class="colo" style="height: 40px; border-bottom: 2px solid #ffffff50; padding: 4px 0px;"><tabs v-if="device !=='mobile'"></tabs></div>  
      </div>
      <app-main />
    </div>
  </div>
</template>

<script>
import Logo from './components/Logo'
import { Navbar, Sidebar, AppMain, Tabs, HorMenu, Navbah } from './components'
import ResizeMixin from './mixin/ResizeHandler'
import transmit from '@/utils/transmit'
export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    Tabs,
    HorMenu,
    Logo,
    Navbah,
  },
  mixins: [ResizeMixin],
  created () {
    transmit.$on('delivery', msg => {
      this.delivery = msg
    })
  },
  data() {
    return {
      show: false,
      scroll: 0,
      bgColor: '#7493FF',
      delivery: false
     }
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
      //  return 'mobile'
    },
    fixedHeader() {
      return this.$store.state.settings.fixedHeader
    },
    needTagsView() {
      return this.$store.state.settings.tagsView
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  mounted() {
    window.addEventListener('scroll', this.scrollD, true)
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    },
    scrollD() {
      const that = this
      that.scroll = document.documentElement.scrollTop
      // alert( that.scroll)
      if(that.scroll > 50){
        that.show = true
      } else if(that.scroll <= 50){
        that.show = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  @import "~@/styles/variables.scss";

  .top{
    position: fixed;
    left: 0;
    top: 0;
    display: flex;
    align-items:center;
    width: 100%;
    height: 90px;
    // background-image: linear-gradient(to right, #2575fc 0%, #2575fc 100%);
    background: url('../assets/image/top.png');
    background-size: 100% 100%; 
    border-bottom: 2px solid #ffffff50;
    // transition: .5s linear;
    z-index: 9
  }
  .colo{
    // background-image: linear-gradient(to top, #4481eb 0%, #04befe 100%);
    background-image: linear-gradient(to right, #2575fc 0%, #2575fc 100%)
  }
  .logo{
    position: fixed; 
    z-index: 9; 
    top: 20px; 
    margin-left: 35px;
    height: 65px;
  }
  .logos{
    position: absolute; 
    top: 0;
    right: 5px;
    z-index: 99999; 
  }
  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
    &.mobile.openSidebar{
      position: fixed;
      top: 0;
    }
  }
  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$sideBarWidth});
    transition: width 0.28s;
    padding-right: 0px!important;
  }
  .fixed-headerr {
    position: fixed;
    top: 50px;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$sideBarWidth});
    transition: width 0.28s;
    padding-right: 0px!important;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px)
  }

  .mobile .fixed-header {
    width: 100%;
  }
  // .mobile .fixed-header+.app-main{
  //  padding-top: 0px!important;
  // }
  .flex{
    display: flex;
    align-items:center;
    position: absolute; 
    top: 50px;
    left: 0px;
    width: 100%;
    z-index: 8; 
  }
 
</style>
