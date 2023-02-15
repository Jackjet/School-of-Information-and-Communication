<template>
  <el-scrollbar class='sidebar'>
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse"
             unique-opened router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.title" :key="item.id">
            <template slot="title">
              <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <template v-if="subItem.subs">
                <el-submenu :index="subItem.title" :key="subItem.id">
                  <template slot="title">{{ subItem.title }}</template>
                  <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                    {{ threeItem.title }}
                  </el-menu-item>
                </el-submenu>
              </template>
              <template v-else>
                <el-menu-item :index="subItem.index" :key="subItem.id">
                  {{ subItem.title }}
                </el-menu-item>
              </template>
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
import transmit from './../common/transmit'

export default {
  name: 'sidebar',
  props: {
    message: Boolean
  },

  data () {
    return {
      collapse: false,
      items: [{
        id: '0',
        icon: 'Admin Admin-biaoge',
        index: '/index/examination',
        title: '考试管理'
        // subs: [{
        //   id: '0-0',
        //   index: '/index/examination',
        //   title: '考试管理'
        // }]
      }]
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

  ul {
    height: 100%;
  }

  .Admin {
    font-size: 22px;
    margin-right: 10px;
  }
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
