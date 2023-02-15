<template>
  <el-col :span='24' class='header' v-loading="loading">
    <h3 class='logo'>调查问卷后台管理系统</h3>
    <div class='keep_right'>
      <div class='language' v-if="index">
        <el-dropdown trigger="click" @command="openColor">
          <span class="el-dropdown-link userinfo-inner">
            <i class='Admin Admin-huabanzhuti el-zhuti'></i>选择主题
            <i class='el-icon-caret-bottom el-xiangxia'></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <template v-for="(item, index) in swactTheme">
              <el-dropdown-item
                :divided='item.divided'
                :key="index"
                :command='item.value'
                :disabled="themeColor === item.value">
                <div :class="item.value + ' ' + 'color'"></div>{{item.label}}
              </el-dropdown-item>
            </template>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <el-dropdown trigger="click" v-if="index">
        <span class="el-dropdown-link userinfo-inner">
          <img src="../../assets/image/admin1.png" />
          {{UserName}}
          <i class='el-icon-caret-bottom'></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <template v-for='(item, index) in downUp'>
            <el-dropdown-item
              :key='index'
              :divided='item.divided'
              @click.native='UserOption(index)'>
              <template v-if='item.type === "down"'>
                {{item.label}}
              </template>
              <el-badge is-dot v-if="item.type === 'message'"
                :value="0"
                :max="99"
                class="item">
                {{item.label}}
              </el-badge>
            </el-dropdown-item>
          </template>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </el-col>
</template>

<script>
import md5 from 'js-md5'
import { toggleClass } from '../../store/index'

export default {
  props: {
    index: Boolean
  },

  data () {
    return {
      UserName: this.common.session('currentUser').name,
      loading: false,
      downUp: [{
        label: '修改密码',
        divided: false,
        type: 'down'
      },
      {
        label: '退出登录',
        divided: true,
        type: 'down'
      }],

      swactTheme: [{
        label: '典雅绿',
        value: 'green',
        divided: false
      },
      {
        label: '深空蓝',
        value: 'default',
        divided: false
      },
      {
        label: '贵族紫',
        value: 'purple',
        divided: false
      },
      {
        label: '魅力橙',
        value: 'yellow',
        divided: false
      },
      {
        label: '铁锈红',
        value: 'red',
        divided: false
      },
      {
        label: '默认主题',
        value: 'blackGreen',
        divided: true
      }]
    }
  },

  mounted () {
    toggleClass(document.body, 'custom-' + this.themeColor)
  },

  computed: {
    themeColor: {
      get () { return this.$store.state.theme.themeColor }
    }
  },

  watch: {
    themeColor: {
      handler () {
        toggleClass(document.body, 'custom-' + this.themeColor)
      }
    }
  },

  methods: {
    /**
     * PUT 请求修改密码
     * @param url 请求地址
     * @param parameter 请求参数
     */

    async modifyPassWord (url, parameter) {
      const that = this
      try {
        that.loading = true
        const result = await that.request.dataPut(that, url, parameter)
        that.loading = false
        if (result.data.code === 1) {
          that.$message.success('密码修改成功')

          // 修改成功 清除用户信息重新登录
          that.common.session('currentUser', null)
          that.$router.push('/login')
        } else {
          that.$message.error(result.data.msg)
        }
      } catch (even) {
        that.$message.errer('修改失败')
      }
    },

    UserOption (index) {
      let that = this
      switch (index) {
        case 0:
          that.$prompt('修改密码', '', {
            inputType: 'password'
          }).then((value) => {
            /**
             * @param value 重置密码
             * Put 请求修改UserAdmin
             */
            if (value.value === null || value.value === 'null') {
              that.$message.warning('修改密码不能为空')
              return false
            }

            let parameter = {
              newPwd: md5(value.value),
              id: this.common.session('currentUser').id
            }
            const url = that.Interface.UserLogin.PutPassWord
            that.modifyPassWord(url, parameter)
          }).catch(() => {
            return false
          })
          break
        case 1:
          // 退出当前用户登录 调用模态框
          that.$confirm('是否退出登录?', '提示', {
            type: 'warning'
          }).then(() => {
            that.$router.push('/login')
            that.common.session('currentUser', null)
          }).catch(() => {
            return false
          })
          break
      }
    },

    openColor (command) {
      this.cookie.set('theme', command)

      toggleClass(document.body, 'custom-' + command)
      this.$store.state.theme.themeColor = command
    }
  }
}
</script>

<style lang="scss">
  .header {
    height: 60px;
    line-height: 60px;
    font-family: "Comic Sans MS" !important;
    font-size: 15px;
    h3 {
      width: 230px;
      font-size: 20px;
      text-align: center;
      float: left;
    }

    .keep_right {
      float: right;
      margin-right: 30px;
    }

    .language {
      display: inline-block;
      vertical-align: top;
      margin-right: 20px;
      font-size: 14px;
      position: relative;
      cursor: pointer;
      .el-zhuti {
        margin-right: 5px;
        position: absolute;
        font-size: 24px;
        left: -35px;
      }
    }

    .el-dropdown {
      color: white !important;
      cursor: pointer;
      font-size: 15px;
      i {
        margin-left: 5px;
      }
      span {
        vertical-align: top;
        img {
          width: 32px;
          display: inline-block;
          vertical-align: middle;
          border-radius: 50%;
          margin-bottom: 5px;
        }
        display: inline-block;
        vertical-align: top;
      }
    }
  }

  .color {
    display: inline-block;
    width: 15px;
    height: 15px;
    background: #41986a !important;
    border-radius: 2px;
    vertical-align: top;
    margin-right: 5px;
    margin-top: 11px;
  }

  .green {
    background: #41986a !important;
  }

  .default {
    background: #20a0ff !important;
  }

  .purple {
    background: #b539ab !important;
  }

  .yellow {
    background: #e6a23c !important;
  }

  .red {
    background: #dd5c5c !important;
  }

  .blackGreen {
    background: #526379 !important;
  }
</style>
