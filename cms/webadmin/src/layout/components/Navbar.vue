<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />
    <div @click="exchangeUrl">
      <img src="@/icons/image/web.png" style="width: 37px; height: 37px; position:absolute;left: 76%;top: 6px;cursor: pointer" >
      <a style="position: absolute;left: 79%; top: 17px;font-size: 14px; ">网站首页</a>
    </div>
    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <p style="display: inline-block;position: relative;left:-10px;top: -14px;font-size: 14px;cursor: pointer;">{{this.userName}}</p>
          <img src="../../assets/image/user.png" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link to="/">
            <el-dropdown-item>
              主页
            </el-dropdown-item>
          </router-link>
          <!-- <a target="_blank" href="https://github.com/PanJiaChen/vue-admin-template/">
            <el-dropdown-item>Github</el-dropdown-item>
          </a>
          <a target="_blank" href="https://panjiachen.github.io/vue-element-admin-site/#/">
            <el-dropdown-item>Docs</el-dropdown-item>
          </a> -->
          <router-link to="/updatePassword">
            <el-dropdown-item>
              修改密码
            </el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">注销登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import transmit from '../../utils/transmit'
import {getInfo} from '@/api/user'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  data () {
    return {
      collapse: false,
      baseUrl : process.env.VUE_APP_BASE_API+'web/view/index.html',
      userName: ''
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar'
    ])
  },

  async mounted() {
    const response = await getInfo();
    this.userName = response.data.data.realName
  },

  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
      this.collapse = !this.sidebar.opened
      transmit.$emit('collapse', this.collapse)
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      console.log(this.$route)
      console.log(this.$route.fullPath)
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    exchangeUrl(){
      window.open(this.baseUrl,'_blank');
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;
        top: -5px;

        .user-avatar {
          display: inline-block;
          cursor: pointer;
          width: 37px;
          height: 37px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
