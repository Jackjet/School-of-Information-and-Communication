<!--
 * @Author: your name
 * @Date: 2020-09-10 19:00:18
 * @LastEditTime: 2020-09-24 17:52:17
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\layout\components\Sidebar\index.vue
-->

<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse"/>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical">
        <sidebar-item v-for="route in routes" :key="route.path" :item="route" :base-path="route.path"/>
      </el-menu>
    </el-scrollbar>
    <SidebarItem ref="ChildTree"></SidebarItem>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import Logo from './Logo'
import SidebarItem from './navMenu'
// import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'
import {findCurrentUserSidebar} from '@/api/role'
import {sessionStorageGet} from '@/utils/storage'
import {type} from "@/utils/tool";

export default {
  components: {SidebarItem, Logo},
  computed: {
    ...mapGetters([
      'sidebar'
    ]),
    // routes() {
    //   return this.$router.options.routes
    // },
    // activeMenu() {
    //   const route = this.$route
    //   const { meta, path } = route
    //   // if set path, the sidebar will highlight the path you set
    //   if (meta.activeMenu) {
    //     return meta.activeMenu
    //   }
    //   return path
    // },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  mounted() {
    this.getMenuData()
  },
  methods: {
    async getMenuData() {
      let that = this
      let menu = []
      let parameters = {userId: sessionStorageGet('id')}
      const response = await findCurrentUserSidebar(parameters)
      let Data = response.data.data[0].menuTree
      if (response.data.code === 1) {
        Data.map(function (v, k) {
          //只有一级目录并且是根目录
          if (!v.child) {
            //如果类型为窗口链接，就把index值赋空值
            let path;
            if (v.type != 2) {
              path = v.path
            }
            menu.push({id: v.id, icon: JSON.parse(v.meta).icon, index: path, title: v.name, type: v.type, url: v.path})
          } else {
            if (v.type === 2) {
              console.log(k)
              console.log(v)
            }

            let sub = []
            v.children.map(function (v1, k1) {
              if (v1.levelMsg !== '三级') {
                let kMenu = k1 + 1
                sub.push({id: k + 2 + '-' + kMenu, index: v1.redirect, title: v1.name, type: v.type})
              }
            })
            menu.push({id: k + 2, icon: JSON.parse(v.meta).icon, index: k + 2, title: v.name, subs: sub, type: v.type})
          }
        })
        that.$refs.ChildTree.SetParameters(menu)
      }
    }
  },
}
</script>
