<template>
  <el-scrollbar class='sidebar'>
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.id">
            <template slot="title">
              <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
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
            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </el-scrollbar>
</template>

<script>
  import transmit from '../../../utils/transmit'
  // import { mapGetters } from 'vuex'
  export default {
    name: 'SideBar',
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
        items: [
          //   {
          //   id: '1',
          //   icon: 'iconfont icon-tuanduicankaoxian-',
          //   index: '/dashboard',
          //   title: '首页'
          // }, {
          //   id: '2',
          //   icon: 'iconfont icon-zhinengsuo',
          //   index: '2',
          //   title: '数据源管理',
          //   subs: [
          //     {
          //       id: '2-1',
          //       index: '/dataSource/index',
          //       title: '数据源管理'
          //     }]
          // }, {
          //   id: '3',
          //   icon: 'iconfont icon-yunweiguanli',
          //   index: '3',
          //   title: '接入任务管理',
          //   subs: [
          //     {
          //       id: '3-1',
          //       index: '/taskManage/index',
          //       title: '任务组管理'
          //     }, {
          //       id: '3-2',
          //       index: '/taskManage/manag',
          //       title: '任务管理'
          //     }, {
          //       id: '3-3',
          //       index: '/taskManage/executionlog',
          //       title: '任务执行日志'
          //     }, {
          //       id: '3-4',
          //       index: '/taskManage/grouplog',
          //       title: '任务组执行日志'
          //     }]
          // }, {
          //   id: '5',
          //   icon: 'iconfont icon-yonghushezhi-',
          //   index: '5',
          //   title: '用户管理',
          //   subs: [
          //     {
          //       id: '5-1',
          //       index: '/omMager/user',
          //       title: '用户管理'
          //     },
          //     {
          //       id: '5-2',
          //       index: '/omMager/comments',
          //       title: '角色管理'
          //     }
          //   ]
          // }, {
          //   id: '7',
          //   icon: 'iconfont icon-zuoce-zonghejiankong',
          //   index: '7',
          //   title: '质量检查规则',
          //   subs: [
          //     {
          //       id: '7-1',
          //       index: '/rule/manage',
          //       title: '规则管理'
          //     },
          //     {
          //       id: '7-2',
          //       index: '/rule/log',
          //       title: '规则日志'
          //     }]
          // }, {
          //   id: '8',
          //   icon: 'iconfont icon-yunshujukuRedis',
          //   index: '8',
          //   title: '数据模板',
          //   subs: [
          //     {
          //       id: '8-2',
          //       index: '/dataBase/examples',
          //       title: '模板管理'
          //     }
          //   ]
          // }, {
          //   id: '9',
          //   icon: 'iconfont icon-wulianwang- ',
          //   index: '9',
          //   title: '法规与知识',
          //   subs: [
          //     {
          //       id: '9-1',
          //       index: '/Laws/Knowledge',
          //       title: '法规政策'
          //     },
          //     {
          //       id: '9-2',
          //       index: '/Laws/Regulations',
          //       title: '知识广场'
          //     }
          //   ]
          // }, {
          //   id: '10',
          //   icon: 'iconfont icon-Icon-yuanchengxiezuo',
          //   index: '10',
          //   title: '配置管理',
          //   subs: [
          //     {
          //       id: '10-1',
          //       index: '/Configuration/fore',
          //       title: '评分体系'
          //     },
          //     {
          //       id: '10-2',
          //       index: '/Configuration/dataClassification',
          //       title: '数据问题分类'
          //     }
          //   ]
          // }, {
          //   id: '11',
          //   icon: 'iconfont icon-zhishizhongxin',
          //   index: '11',
          //   title: '质量报告',
          //   subs: [
          //     {
          //       id: '11-1',
          //       index: '/report/reIndex',
          //       title: '质量报告'
          //     }
          //   ]
          // },{
          //   id: '12',
          //   icon: 'iconfont icon-25',
          //   index: '12',
          //   title: '数据质量工单',
          //   subs: [
          //     {
          //       id: '12-1',
          //       index: '/Dqwt/womanagement',
          //       title: '工单管理'
          //     },

          //   ]
          // }
        ]
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
          //  that.items.push(key)
          that.items = key
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
    bottom: 0;
    overflow-y: hidden;
    z-index: 999;
    text-align: left;
    background: #15253a;
    width: 100%;
    // padding: 5px;

    ul {
      height: 100%;
      background: #15253a;
    }

    .Admin {
      font-size: 22px;
      margin-right: 10px;
    }
  }

  .el-submenu {
    color: rgb(191, 203, 217) !important;
    background: #15253a;
    .el-submenu__title {
      color: rgb(191, 203, 217) !important;
      background: #15253a;
    }
  }

  .is-opened {
    background: #1f2d3d
  }

  .el-submenu__title,
  .el-submenu__title span {
    color: rgb(191, 203, 217) !important;
  }

  .sidebar-el-menu {
    background: #15253a;
    vertical-align: middle;
  }

  .sidebar-el-menu span {
    padding-left: 15px;
  }

  .sidebar-el-menu .iconfont {
    font-size: 20px;
    font-weight: bold;
  }

  .el-menu-item:focus, .el-menu-item:hover {
    background: #1f2d3d;
  }

  .el-menu-item {
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
