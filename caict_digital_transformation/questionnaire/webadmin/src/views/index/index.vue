<template>
  <el-row class='container'>
    <headers v-bind:index="showDown"></headers>
    <sidebar></sidebar>
    <!-- 主页面 -->
    <el-scrollbar class='content-box' :class="{'content-collapse':collapse}">
        <div class="content">
          <div class='header_title'>
            <!-- 导航控制柄 -->
            <div class="collapse-btn" @click="collapseChage">
              <i v-if="!collapse" class="el-icon-s-fold"></i>
              <i v-else class="el-icon-s-unfold"></i>
            </div>
            <!-- 页面索引路径最多支持三级导航 -->
            <el-breadcrumb separator='/'>
              <el-breadcrumb-item>{{ title }}</el-breadcrumb-item>
              <el-breadcrumb-item v-if="this.name !== ''">
                {{ name }}
              </el-breadcrumb-item>
              <el-breadcrumb-item v-if="this.slots !== '' && this.name !== ''">
                {{ slots }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <router-view @reroute="reloadRote"></router-view>
        </div>
    </el-scrollbar>
  </el-row>
</template>

<script>
import headers from '../../components/template/header.vue'
import sidebar from '../../components/template/navMenu.vue'
import transmit from '../../components/common/transmit'

export default {
  components: { sidebar, headers },

  data () {
    return {
      collapse: false,
      title: this.$route.meta.title,
      name: this.$route.meta.name,
      slots: this.$route.meta.slots,
      showDown: true
    }
  },

  mounted () {
    window.onresize = () => {
      return (() => {
        if (document.body.clientWidth <= 800) {
          this.collapse = true
        }

        if (document.body.clientWidth <= 500) {
          this.showDown = false
        } else {
          this.showDown = true
        }

        transmit.$emit('collapse', this.collapse)
      })()
    }
  },

  methods: {
    collapseChage () {
      this.collapse = !this.collapse
      transmit.$emit('collapse', this.collapse)
    },
    reloadRote () {
      this.title = this.$route.meta.title
      this.name = this.$route.meta.name
      this.slots = this.$route.meta.slots
    }
  },

  watch: {
    $route (route) {
      this.title = route.meta.title
      this.name = route.meta.name
      this.slots = route.meta.slots
    }
  }
}
</script>

<style lang="scss" scoped>
  @import '~./index.scss';
</style>
