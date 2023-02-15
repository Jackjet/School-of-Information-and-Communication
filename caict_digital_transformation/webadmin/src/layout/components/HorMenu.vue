<template>
  <el-scrollbar class='sidebar'>
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" mode="horizontal" :collapse="collapse" router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.id">
            <template slot="title">
              <span slot="title">{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.id">
                <template slot="title"><span slot="title">{{ subItem.title }}</span></template>
                <el-menu-item v-for="(threeItem, i) in subItem.subs"  @click="clickMenu(threeItem)"   :key="i" :index="threeItem.index">
                {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.index" :key="subItem.id"  @click="clickMenu(subItem)">
                 <span slot="title">{{ subItem.title }}</span>
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" :key="item.id"  @click="clickMenu(item)">
             <span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </el-scrollbar>
</template>

<script>
import { sessionStorageSet } from '@/utils/storage'
import transmit from '@/utils/transmit'
import { mapGetters } from 'vuex'
export default {
  name: 'sidebar',
  props: {
    message: Boolean
  },
  // computed: {
  //    ...mapGetters([
  //     'sidebar'
  //   ]),
  //   isCollapse() {
  //     return !this.sidebar.opened
  //   }
  // },
  data () {
    return {
      collapse: false,
      items: [],
    }
  },

  computed: {
    onRoutes () {
      return this.$route.path
    }
  },
  created () {
    transmit.$on('collapse', msg => {
      this.collapse = msg
    })
  },
   methods: {
    // SetParameters (key) {
    //   const that = this
    //   if (key != null) {
    //     //  that.items.push(key)
    //     that.items = key
    //     that.items.unshift({
    //       id: '1',
    //       icon: 'iconfont icon-shouye',
    //       index: '/dashboard',
    //       title: '首页',
    //    })
    //   }
    // },
    clickMenu(item) {
      sessionStorageSet('expand')
      sessionStorageSet('parent_expand')
      this.$store.commit('selectMenu', item)
    }
  }
}
</script>

<style scoped lang="scss">
  /deep/ .el-menu-item, .el-submenu__title{
    height: 90px!important;
    line-height: 90px!important;
  }
  /deep/ .el-submenu__title{
    height: 90px!important;
    line-height: 90px!important;
  }
  /deep/ .el-submenu .el-menu-item{
    height: 90px!important;
    line-height: 90px!important;
  }
  /deep/ .el-menu--inline .is-active{
    // background-image:url('../../../assets/image/search.png');
    background-size: 100% 42px;
    color: #5677df;
  }
  /deep/  .el-scrollbar__wrap{
    overflow: hidden;
    margin: 0px!important
  }
  /deep/ .el-menu--horizontal > .el-submenu .el-submenu__title:hover{
    background: #ffffff40!important;
  }
  /deep/ .el-menu--horizontal > .el-menu-item.is-active{
    color: #fff;
  }
  /deep/ .el-menu--horizontal>.el-menu-item{
    color: #fff;
  }
 /deep/ .el-menu--horizontal .el-menu-item:not(.is-disabled):hover, .el-menu--horizontal .el-menu-item:not(.is-disabled):focus{
    color: #fff;
  }
  .sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 60px;
    bottom:0;
    overflow-y: hidden;
    z-index: 999;
    text-align: left;
    // background: #15253a;
    width: 100%;
    // padding: 5px;
    ul {
      height:100%;
      // background:#15253a;
    }
    .Admin {
      font-size: 22px;
      margin-right: 10px;
    }
  }
  .el-submenu{
    color:rgb(191, 203, 217)!important;
    // background:#15253a;
    .el-submenu__title{
      color:rgb(191, 203, 217)!important;
      height: 50px!important;
      // background: #15253a;
    }
  }
  .is-opened{
    // background: #001f28
  }
  .el-submenu__title,
  .el-submenu__title span{
    color:#fff!important;
    height: 50px!important;
    line-height: 50px!important;
    font-size: 16px;
    font-weight: 600;
   }
  .sidebar-el-menu{
    // background: #15253a;
    vertical-align: middle;
  }
  .sidebar-el-menu span{
      padding-left: 0px;
  }
  .sidebar-el-menu .iconfont{
    font-size: 20px;
    font-weight: bold;
  }
  .el-menu-item:focus, .el-menu-item:hover{
     background: #ffffff40!important;
     color: #fff;
  }
  .el-menu-item{
    color: #fff;
  }
  .el-scrollbar {
    height: calc(100% - 43px);
  }
 .el-menu--horizontal .el-menu-item{
    font-size: 16px;
    font-weight: 600;
 }
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }

  .sidebar::-webkit-scrollbar {
    width: 0;
  }

  .sidebar-el-menu:not(.el-menu--collapse) {
    width: 100;
  }
</style>
