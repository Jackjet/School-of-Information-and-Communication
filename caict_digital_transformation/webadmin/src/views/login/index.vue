<template>
  <div class="login-container">
    <div class="center">
      <div class="right">
        <el-form
          ref="loginForm"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          auto-complete="on"
          label-position="left"
        >
          <div class="title-container">
            <h3 class="title">运营管理系统</h3>
          </div>
          <el-form-item prop="username" class="item user_name">
            <span class="svg-container">
              <i class="el-icon-user" />
            </span>
            <el-input
              maxlength="100"
              ref="username"
              v-model="loginForm.username"
              placeholder="请输入账号"
              name="username"
              type="text"
              tabindex="1"
              autocomplete="off"
            />
          </el-form-item>
          <el-form-item prop="password" class="item user_name">
            <span class="svg-container">
              <i class="el-icon-lock" />
            </span>
            <el-input
              show-password
              maxlength="100"
              :key="passwordType"
              ref="password"
              v-model="loginForm.password"
              :type="passwordType"
              placeholder="请输入密码"
              name="password"
              tabindex="2"
              autocomplete="off"
              @keyup.enter.native="handleLogin"
            />
          </el-form-item>
          <el-dialog
            append-to-body
            :visible.sync="dialogVisible"
            :show-close="false"
            width="450px"
          >
            <slideverify
              :l="42"
              ref="dialogopen"
              :r="10"
              :w="410"
              :h="200"
              :block_y="block_y"
              :imgurl="imgurl"
              :miniimgurl="miniimgurl"
              @success="handleLogin"
              @fail="onFail"
              @refresh="onRefresh"
              :slider-text="text"
            ></slideverify>
          </el-dialog>
          <el-button
            :loading="loading"
            type="primary"
            style="
              width: 100%;
              margin-bottom: 5px;
              height: 45px;
              margin-left: 0px;
            "
            @click.native.prevent="verift"
            >登录</el-button
          >
          <div class="tips wid">
            <el-checkbox
              v-model="sumberPasswd"
              style="float: right; margin-right: 5px; margin-top: 10px"
              label="记住密码"
              name="type"
            ></el-checkbox>
            <span></span>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { SmsSend, forgetPassword, getImageVerifyCode } from "@/api/user";
import { getScrollPosition } from "@/utils/browser";
import router from "@/router";
import dragVerify from "vue-drag-verify";
import slideverify from "@/components/SlideVerify/slide-verify.vue";
import { throttle } from "@/utils/tool";
import { setCookies, getCookies, removeCookies } from "@/utils/auth";
import { isEmpty } from "@/utils/tool";
import { sessionStorageSet } from "@/utils/storage";
import CryptoJS from "crypto-js";

export default {
  name: "Login",
  components: {
    dragVerify,
    slideverify,
  },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error("请输入超过6位数密码"));
      } else {
        callback();
      }
    };
    const validateOldPassword = (rule, value, callback) => {
      if (value.length <= 0) {
        callback(new Error("请输入确认密码"));
      } else if (this.passwdForm.oldPasswd !== this.passwdForm.newPasswd) {
        callback(new Error("新密码与确认密码输入不一致"));
      } else {
        callback();
      }
    };
    return {
      loginForm: {
        username: "",
        password: "",
        left: "",
      },
      sumberPasswd: false,
      lock: true,
      block_y: "",
      imgurl: "",
      miniimgurl: "",
      chenckMoveid: "",
      dialogVisible: false,
      circle: false,
      count: 60,
      timer: null,
      formLabelWidth: "90px",
      loginRules: {
        username: [
          {
            required: true,
            message: "请输入账号",
            trigger: ["change", "blur"],
          },
          {
            pattern: /^[0-9A-Za-z]{1,20}$/, //正则
            message: "请输入1-20位账号,(字母开头,允许字母数字下划线组合)",
          },
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: ["change", "blur"],
          },
          {
            pattern: /^[0-9A-Za-z]{6,12}$/, //正则
            message: "请输入6-12位密码,(字母开头,允许字母数字下划线组合)",
          },
        ],
      },
      passwordCheckValidate: {
        errorText: "",
      },
      sends: "获取验证码",
      loading: false,
      passwordType: "password",
      redirect: undefined,
      isShow: 1,
      disabled: false,
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
  },
  mounted() {
    this.getCooike();
  },
  methods: {
    //点击验证
    verift() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          // if (this.lock) {
          //   this.getImageVerifyCodes()
          //   this.lock = false
          // }
          // this.dialogVisible = true

          this.handleLogin();
        } else {
          return false;
        }
      });
    },
    getCooike() {
      let that = this;
      if (!isEmpty(getCookies("username"))) {
        that.loginForm.username = getCookies("username");
        let password = CryptoJS.AES.decrypt(
          getCookies("password").toString(),
          "secret key 123"
        );
        that.loginForm.password = password.toString(CryptoJS.enc.Utf8);
        that.sumberPasswd = true;
      } else {
        that.sumberPasswd = false;
      }
    },
    async getImageVerifyCodes() {
      const that = this;
      that.imgurl = "";
      that.miniimgurl = "";
      let data = await getImageVerifyCode();
      let imgobj = data.data.data;
      that.block_y = imgobj.yHeight;
      that.imgurl = "data:image/png;base64," + imgobj.bigImage;
      that.miniimgurl = "data:image/png;base64," + imgobj.smallImage;
      that.chenckMoveid = imgobj.chenckMoveId;
      if (that.$refs.dialogopen) {
        that.$refs.dialogopen.reset(imgobj.yHeight);
      }
    },
    onSuccess(left) {
      this.handleLogin();
    },
    onRefresh: throttle(function () {
      this.getImageVerifyCodes();
    }, 1000),
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    handl() {
      this.$refs.passwdForm.validate((valid) => {
        if (valid) {
          this.updateP();
        } else {
          return false;
        }
      });
    },
    handleLogin(left) {
      let that = this;
      this.loginForm.x_index = left;
      this.loginForm.chenckMoveid = this.chenckMoveid;
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$store
            .dispatch("user/login", this.loginForm)
            .then((data) => {
              if (data.code === 1) {
                this.$store
                  .dispatch("permission/findCurrentUserButton")
                  .then((data) => {
                    if (data.code === 1) {
                      if (that.sumberPasswd === true) {
                        let password = CryptoJS.AES.encrypt(
                          that.loginForm.password,
                          "secret key 123"
                        );
                        setCookies("username", that.loginForm.username, 7);
                        setCookies("password", password, 7);
                      } else {
                        removeCookies("username");
                        removeCookies("password");
                      }
                      this.$router.push({ path: "/" });
                    } else {
                      this.$message.error(data.msg);
                    }
                  })
                  .catch((data) => {
                    this.onRefresh();
                    this.$message.error(data.msg);
                  });
              } else {
                this.onRefresh();
                this.$message.error(data.msg);
              }
            })
            .catch((data) => {
              this.onRefresh();
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>
<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #fff;
$light_gray: #000;
$cursor: #000;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 35px;
    width: 85%;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 2px 5px 2px 15px;
      color: $light_gray;
      height: 35px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }
  .center {
    display: -webkit-flex; /* Safari */
    display: flex;
    flex-direction: row;
    width: 73%;
    height: 50%;
    min-width: 800px;
    // margin: 0 auto;
    position: relative;
    top: 42%;
    left: 50%;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    // .left{
    //   width: 67%;
    //   height: 100%;
    //   float: left;
    //   background: url('../../assets/image/left.png');
    //   background-size: 100% 100%;
    //   border-radius: 8px 0px 0px 8px;
    //   min-height: 370px;
    //   flex-shrink: 1;
    // }
    .right {
      float: right;
      min-width: 420px;
      width: 53%;
      height: 100%;
      // background: #fff;
      border-radius: 0px 8px 8px 0px;
      min-height: 370px;
      flex-shrink: 0;
    }
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: rgb(255, 255, 255);
.el-button--primary {
  // background: rgb(83, 190, 119);
  border: none;
}

/deep/ .el-button + .el-button {
  margin-left: 0px !important;
}

.dv_handler {
  height: 40px !important;
}

.InpitWidth {
  width: 80%;
  min-width: 402px;
}

.item {
  height: 44px;
  line-height: 44px;
}

/deep/ .el-form-item__content {
  line-height: 35px;
}

.login-container {
  height: 100%;
  min-height: 550px;
  min-width: 1100px;
  width: 100%;
  // background-color: $bg;
  // background: #005C97;  /* fallback for old browsers */
  // background: -webkit-linear-gradient(to right, #363795, #005C97);  /* Chrome 10-25, Safari 5.1-6 */
  // background: linear-gradient(to right, #363795, #005C97); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: url("../../assets/image/bg.png");
  background-size: 100% 100%;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 500px;
    max-width: 100%;
    padding: 40px 40px 0 30px;
    // margin: 0 auto;
    overflow: hidden;
    .el-form-item {
      border: 1px solid rgb(192, 189, 189);
      background: #fff;
      border-radius: 5px;
      color: #000;
    }
  }
  .passwd-form {
    position: relative;
    width: 450px;
    max-width: 100%;
    padding: 10px 30px 0;
    margin: 0 auto;
    overflow: hidden;
    .el-form-item {
      background: #fff;
      border-radius: 5px;
      color: #000;
    }
    /deep/ .el-input__inner {
      border: 1px solid rgb(192, 189, 189);
      background-color: #fff;
      background-image: none;
      border-radius: 4px;
      line-height: 40px;
      height: 40px;
      box-sizing: border-box;
      color: #606266;
      display: inline-block;
      font-size: inherit;
      padding: 0 15px;
    }
    /deep/ .el-form-item__label {
      text-align: right;
      vertical-align: middle;
      float: left;
      font-size: 14px;
      color: #606266;
      line-height: 40px;
      padding: 0 12px 0 0;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
    }
  }
  .tips {
    font-size: 14px;
    color: #000;
    margin-bottom: 10px;
    height: 30px;
    line-height: 30px;
    cursor: pointer;
    span {
      &:first-of-type {
        margin-right: 1px;
        float: right;
      }
    }
    :hover {
      color: blue;
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;
    max-width: 360px !important;
    .title {
      font-size: 28px;
      color: #222222;
      margin: 0px auto 30px auto;
      text-align: center;
      font-weight: 548;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
  .wid,
  .el-button,
  .user_name {
    max-width: 360px !important;
  }
}
</style>
