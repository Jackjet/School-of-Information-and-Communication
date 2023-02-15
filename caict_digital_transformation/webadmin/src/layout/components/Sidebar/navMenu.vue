<template>
  <el-scrollbar class='sidebar'>
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.id">
            <template slot="title">
              <svg class="icon" aria-hidden="true">
                <use :xlink:href="item.icon"></use>
              </svg>
              <span style="font-size: 15px; padding-left: 10px;" slot="title">{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.id">
                <template slot="title"><span slot="title">{{ subItem.title }}</span></template>
                <el-menu-item v-for="(threeItem, i) in subItem.subs" @click="clickMenu(threeItem)" :key="i" :index="threeItem.index">
                  {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.index" :key="subItem.id" @click="clickMenu(subItem)">
                <span slot="title">{{ subItem.title }}</span>
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" :key="item.id" @click="clickMenu(item)">
            <svg class="icon" aria-hidden="true">
              <use :xlink:href="item.icon"></use>
            </svg>
            <span style="font-size: 15px; padding-left: 10px;" slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </el-scrollbar>
</template>

<script>
import {sessionStorageSet} from '@/utils/storage'
import transmit from '../../../utils/transmit'

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
    data() {
      return {
        collapse: false,
        items: [],
      }
    },

    computed: {
      onRoutes() {
        return this.$route.path
      }
    },
    created() {
      transmit.$on('collapse', msg => {
        this.collapse = msg
      })
    },
    methods: {
      SetParameters(key) {
        const that = this
        if (key != null) {
          //that.items.push(key)
          that.items = key
          that.items.unshift({
            id: '1',
            icon: '#icon-weibiaoti-',
            index: '/dashboard',
            title: '首页',
          })
        }
      },
      clickMenu(item) {
        //type: 0菜单，1按钮，2新窗口，3内页，4内部功能页面
        if (item.type === 2) {
          window.open(item.url, '_blank');
        } else {
          sessionStorageSet('expand')
          sessionStorageSet('parent_expand')
          this.$store.commit('selectMenu', item)
        }
      }
    }
  }
</script>

<style scoped lang="scss">
  .icon {
    width: 1.4em;
    height: 1.4em;
    vertical-align: -0.45em;
    fill: currentColor;
    overflow: hidden;
  }

  /deep/ .el-menu-item, .el-submenu__title {
    height: 42px !important;
    line-height: 42px !important;
  }

  /deep/ .el-submenu__title {
    height: 42px !important;
    line-height: 42px !important;
  }

  /deep/ .el-submenu .el-menu-item {
    height: 42px !important;
    line-height: 42px !important;
    padding-left: 50px !important;
  }

  /deep/ .el-menu--inline .is-active {
    background-image: url('../../../assets/image/search.png');
    background-size: 100% 42px;
    color: #5677df;
  }

  .sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 60px;
    bottom: 0;
    overflow-y: hidden;
    z-index: 999;
    text-align: left;
    // background: #15253a;
    width: 100%;
    // padding: 5px;
    ul {
      height: 100%;
      // background:#15253a;
    }
    .Admin {
      font-size: 22px;
      margin-right: 10px;
    }
  }

  .el-submenu {
    color: rgb(191, 203, 217) !important;
    // background:#15253a;
    .el-submenu__title {
      color: rgb(191, 203, 217) !important;
      height: 50px !important;
      // background: #15253a;
    }
  }

  .is-opened {
    // background: #001f28
  }

  .el-submenu__title,
  .el-submenu__title span {
    color: #606266 !important;
    height: 50px !important;
    line-height: 50px !important;
  }

  .sidebar-el-menu {
    // background: #15253a;
    vertical-align: middle;
  }

  .el-submenu__title span {
    font-size: 16px;
  }

  .el-menu-item span {
    font-size: 15px;
  }

  .sidebar-el-menu .iconfont {
    font-size: 20px;
    font-weight: bold;
  }

  .el-menu-item:focus, .el-menu-item:hover {
    // background: #001F28
  }

  .el-menu-item {
    color: #606266
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
