<template>
  <el-scrollbar class='sidebar'>
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse"
     router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.id">
            <template slot="title">
              <i style="width: 20px;display: inline-block;" v-html="item.icon"></i><span slot="title">{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.id">
                <template slot="title"><span slot="title">{{ subItem.title }}</span></template>
                <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.index" :key="subItem.id">
                 <span slot="title">{{ subItem.title }}</span>
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" :key="item.id">
            <i style="width: 20px;display: inline-block;" v-html="item.icon"></i></i><span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </el-scrollbar>
</template>

<script>
import transmit from '../../../utils/transmit'
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
      // items: [{
      //   id: '1',
      //   icon: 'iconfont icon-shouye',
      //   index: '/dashboard',
      //   title: '首页'
      // },{
      //   id: '2',
      //   icon: 'iconfont icon-shouye',
      //   index: '/template',
      //   title: '模板管理',
      //   subs:[
      //     {
      //       id: '2-1',
      //       icon: 'iconfont icon-shouye',
      //       index: '/template/templateFile',
      //       title: '模板文件'
      //     },
      //     {
      //       id: '2-2',
      //       icon: 'iconfont icon-shouye',
      //       index: '/template/template',
      //       title: '模板管理'
      //     }
      //   ]
      // }, {
      //   id: '4',
      //   icon: 'iconfont icon-shouye',
      //   index: '/column',
      //   title: '栏目管理',
      //   subs: [{
      //     id: '4-1',
      //     icon: 'iconfont icon-shouye',
      //     index: '/column/index',
      //     title: '栏目列表'
      //   }]
      // }, {
      //   id: '2',
      //   icon: 'iconfont icon-shouye',
      //   index: '/website',
      //   title: '网站文件',
      //   subs: [{
      //     id: '2-1',
      //     icon: 'iconfont icon-shouye',
      //     index: '/website/index',
      //     title: '网站文件管理'
      //   }]
      // },{
      //   id: '6',
      //   icon: 'iconfont icon-shouye',
      //   index: '/repository',
      //   title: '资源库管理',
      //   subs: [{
      //     id: '6-1',
      //     icon: 'iconfont icon-shouye',
      //     index: '/repository/index',
      //     title: '资源库管理'
      //   }]
      // }, {
      //   id: '7',
      //   icon: 'iconfont icon-shouye',
      //   index: '/websiteManage',
      //   title: '网站管理',
      //   subs: [{
      //     id: '7-3',
      //     icon: 'iconfont icon-shouye',
      //     index: '/websiteManage/role/index',
      //     title: '角色管理'
      //   }]
      // }]
      items: []
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
    SetParameters (key) {
      const that = this
      if (key != null) {
        //  that.items.push(key)
        that.items = key
        console.log(that.items)
      }
    }
  }
}
</script>

<style scoped lang="scss">
  .sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 60px;
    bottom:0;
    overflow-y: hidden;
    z-index: 999;
    text-align: left;
    background: #15253a;
    width: 100%;
    // padding: 5px;

    ul {
      height:100%;
      background:#15253a;
    }

    .Admin {
      font-size: 22px;
      margin-right: 10px;
    }
  }
  .el-submenu{
    color:rgb(191, 203, 217)!important;
    background:#15253a;
    .el-submenu__title{
      color:rgb(191, 203, 217)!important;
      background: #15253a;
    }
  }
  .is-opened{
    background: #001f28
  }
  .el-submenu__title,
  .el-submenu__title span{
    color:rgb(191, 203, 217)!important;
   }
  .sidebar-el-menu{
    background: #15253a;
    vertical-align: middle;
  }
  .sidebar-el-menu span{
      padding-left: 15px;
  }
  .sidebar-el-menu .iconfont{
    font-size: 20px;
    font-weight: bold;
  }
  .el-menu-item:focus, .el-menu-item:hover{
    background: #001F28
  }
  .el-menu-item{
     color: rgb(191, 203, 217)
  }
  .el-scrollbar {
    height: calc(100% - 43px);
  }

  .el-scrollbar__wrap {
    overflow-x: hidden;
  }

  .sidebar::-webkit-scrollbar {
    width: 0;
  }

  .sidebar-el-menu:not(.el-menu--collapse) {
    width: 230px;
  }
</style>
