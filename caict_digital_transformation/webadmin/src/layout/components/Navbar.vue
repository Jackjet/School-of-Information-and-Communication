<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb class="breadcrumb-container" />
    <themepicker />
    <div class="right-menu">
      <el-dropdown   placement="bottom-start" class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <div>
            <svg t="1600311663297" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5329" width="35" height="40"><path d="M512 148.5c49.1 0 96.7 9.6 141.5 28.5 43.3 18.3 82.2 44.5 115.6 77.9 33.4 33.4 59.6 72.3 77.9 115.6 18.9 44.8 28.5 92.4 28.5 141.5s-9.6 96.7-28.5 141.5c-18.3 43.3-44.5 82.2-77.9 115.6-33.4 33.4-72.3 59.6-115.6 77.9-44.8 18.9-92.4 28.5-141.5 28.5s-96.7-9.6-141.5-28.5c-43.3-18.3-82.2-44.5-115.6-77.9-33.4-33.4-59.6-72.3-77.9-115.6-18.9-44.8-28.5-92.4-28.5-141.5s9.6-96.7 28.5-141.5c18.3-43.3 44.5-82.2 77.9-115.6s72.3-59.6 115.6-77.9c44.8-18.9 92.4-28.5 141.5-28.5m0-50C283.6 98.5 98.5 283.6 98.5 512S283.6 925.5 512 925.5 925.5 740.4 925.5 512 740.4 98.5 512 98.5z" p-id="5330" fill="#ffffff"></path><path d="M512 401m-106 0a106 106 0 1 0 212 0 106 106 0 1 0-212 0Z" p-id="5331" fill="#ffffff"></path><path d="M514.5 540.8c-105.1 0-195 68.8-231.6 166.2 28.2 27.9 60.4 50.1 95.9 65.9 40.2 18 83 27.1 127.1 27.1s86.9-9.1 127.1-27.1c38.9-17.4 73.8-42.3 103.8-74.1 1.4-1.5 2.7-3 4.1-4.4-39.2-90.6-125.8-153.6-226.4-153.6z" p-id="5332" fill="#ffffff"></path></svg>
            <span class="na">{{name}}</span>
          </div>
        </div>
        <el-dropdown-menu style="margin-top: 40px;"slot="dropdown" class="user-dropdown">
          <el-dropdown-item divided @click.native="add">
            <span style="display:block;">修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
       <!-- <theme-picker></theme-picker> -->
    </div>
    <drawer
      ref="call"
      :drawer-dialog='mode'
      @Reload='initial'>
    </drawer>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {resetPassword} from '@/api/user'
import Breadcrumb from '@/components/Breadcrumb'
import ThemePicker from '@/components/themepicker/ThemePicker.vue'
import Hamburger from '@/components/Hamburger'
import transmit from '../../utils/transmit'
import drawer from '@/components/dialog/dialog.vue'
import md5 from 'js-md5'
import {sessionStorageGet, sessionStorageRemove} from "@/utils/storage";
import store from "@/store";

export default {
  components: {
    Breadcrumb,
    Hamburger,
    drawer,
    ThemePicker
  },
data () {
    return {
      collapse: false,
      name: '',
      mode: {
        type: 'dialog',
        form: [{
          type: 'input',
          label: '原密码:',
          placeholder: '请输入原密码',
          model: 'oldPassword',
          state: ['detail', 'new', 'modify'],
          rules: [{
            required: true, message: '请输入原密码', trigger: 'blur'
          }]
        },{
          type: 'input',
          label: '新密码:',
          placeholder: '请输入新密码',
          model: 'newPassword',
          state: ['detail', 'new', 'modify'],
          rules: [{
            required: true, message: '请输入新密码', trigger: 'blur'
          }, {
          pattern:  /^[0-9A-Za-z]{6,12}$/, //正则
          message: '请输入6-12位新密码,(字母开头,允许字母数字下划线组合)'
        }]
        },{
          type: 'input',
          label: '确认密码:',
          placeholder: '请输入确认密码',
          model: 'quePassword',
          state: ['detail', 'new', 'modify'],
          rules: [{
            required: true, message: '请输入确认密码', trigger: 'blur'
          }, {
          pattern:  /^[0-9A-Za-z]{6,12}$/, //正则
          message: '请输入6-12位新密码,(字母开头,允许字母数字下划线组合)'
         }]
        }],
        event: [{
          bind: 'increase',
          method: (row) => {
            this.increase(row)
          }
        },
        {
          bind: 'modify',
          method: (row) => {
            this.bindModify(row)
          }
        }]
      }
    }
  },
  mounted () {
    const that = this
    that.name = sessionStorageGet('name') == null ? store.getters.name : sessionStorageGet('name');
    transmit.$on('logout', function(){
      that.logoutToken()
    })
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'id'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
      this.collapse = !this.sidebar.opened
      transmit.$emit('collapse', this.collapse)
    },
    logout() {
      this.$confirm("是否退出登录?", "提示", {
        type: "warning"
      })
        .then(async () => {
          this.logoutFn();
        })
        .catch(() => {
          return false;
        });
    },
    //退出
    async logoutFn() {
      this.$store.commit('SET_STATE', '')
      sessionStorageRemove('state')
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    // 失效401
    async logoutToken() {
      await this.$store.dispatch('user/resetToken')
      this.$router.push('/login')
    },
    async increase(row) {
      if(row.data.newPassword!==row.data.quePassword){
        return this.$message.error('新密码和确认密码输入不一致!')
      }
      const data = {
        id: sessionStorageGet('id'),
        newPassword: md5(row.data.newPassword),
        oldPassword: md5(row.data.oldPassword),
      }
      try {
        let res = await resetPassword(data)
        if(res.data.code === 1){
          this.$message.success('修改密码成功')
          this.$refs.call.closeDialog()
          this.logoutFn()
        }else {
          this.$message.error(res.data.msg)
        }

       } catch (even) {
        this.$message.error(even.msg)
      }

    },
    add() {
      this.$refs.call.popup('修改密码', 'increase', '')
    },
  }
}
</script>

<style lang="scss" scoped>
  /deep/ .el-dropdown-menu{
    top: 60px!important;
  }
.navbar {
  height: 90px;
  overflow: hidden;
  position: relative;
  background: url('../../assets/image/top.png');
  // background-image: linear-gradient(to right, #4977FC 0%, #4977FC 100%);
  // border-bottom: 2px solid #ffffff50;
  background-size: 100% 100%;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  .na{
    display: inline-block;
    top: -5px;
    position: absolute;
    width: 100px;
    height: 20px;
    cursor: pointer;
    font-size: 17px;
    color: #fff;
  }
  .hamburger-container {
    line-height: 86px;
    height: 100%;
    float: left;
    cursor: pointer;
    padding: 5px 15px!important;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }
  .el-dropdown-menu el-popper user-dropdown
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
      margin-right: 100px;
      width: 35px;
      height: 35px;
      .avatar-wrapper {
        margin-top: 25px;
        position: relative;
        // .user-avatar {
        //   cursor: pointer;
        //   width: 40px;
        //   height: 40px;
        //   border-radius: 10px;
        // }
        // .el-icon-caret-bottom {
        //   cursor: pointer;
        //   position: absolute;
        //   right: -20px;
        //   top: 25px;
        //   font-size: 12px;
        // }
      }
    }
  }
}
</style>
