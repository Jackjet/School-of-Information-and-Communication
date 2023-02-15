<template>
  <el-main style="width: 500px">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="oldPassword"
        label="旧密码"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.oldPassword" placeholder="请输入旧密码" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item
        prop="newPassword"
        label="新密码"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.newPassword" placeholder="请输入新密码" style="width: 300px"></el-input>
      </el-form-item>
    </el-form>
    <div align="center">
      <el-button type="primary" @click="click">确 定</el-button>
    </div>
  </el-main>
</template>

<script>

import { getInfo,updatePassword} from '@/api/user'
export default {
  components: {
    // 注册组件
    // editor: editor
  },

  data () {
    return {
      ruleForm:{
        id:'',
        oldPassword:'',
        newPassword:''
      },
      rules: {
        oldPassword: [{
          required: true,
          message: '请输入旧密码',
          trigger: ['change', 'blur']
        }],
        newPassword: [{
          required: true,
          message: '请输入新密码',
          trigger: ['change', 'blur']
        }]
      },
      formLabelWidth: '120px'
    }
  },
  async created() {
    await this.initial()
  },
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    async initial () {
      const that = this
      let response = await getInfo()
      if (response.data.code === 1) {
        this.ruleForm.id =response.data.data.id
      }else {
        that.$message.error(response.data.msg)
      }
    },
    async click(){
      const that = this
      let params = {
        id: that.ruleForm.id,
        oldPassword: that.$md5(that.ruleForm.oldPassword),
        newPassword: that.$md5(that.ruleForm.newPassword)
      }
      let response = await updatePassword(params);
      if (response.data.code === 1) {
        that.$message.success(response.data.msg)
        await that.$store.dispatch('user/logout')
        this.$router.push(`/login?redirect=${this.$route.fullPath}`)
      }else {
        that.$message.error(response.data.msg)
      }
    }
  }
}
</script>

<style lang="scss">
  .el-drawer {
    .el-drawer {
      .el-drawer__body {
        padding: 10px 20px !important;
      }
    }
    .InpitWidth {
      width: 80%;
      min-width: 302px;
    }
    .avatar-border-red .el-upload{
      border:1px dashed #f56c6c !important;
      border-radius: 6px;
      cursor: pointer;
      float: left;
      position: relative;
      overflow: hidden;
      &:hover {
        border-color: #b6b5b5;
      }
      .el-progress {
        position: absolute;
        z-index: 999;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: white;
        .el-progress-circle {
          margin: 13% auto 10%;
        }
      }
    }
    .avatar-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      float: left;
      position: relative;
      overflow: hidden;
      &:hover {
        border-color: #b6b5b5;
      }
      .el-progress {
        position: absolute;
        z-index: 999;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: white;
        .el-progress-circle {
          margin: 13% auto 10%;
        }
      }
    }
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 200px;
      height: 178px;
      line-height: 178px;
      text-align: center;
      cursor: pointer;
      border-radius: 6px;
    }
    .avatar {
      width: 200px;
      height: 180px;
      display: block;
    }
  }

  .edit_main {
    text-align: left;
    padding: 10px 15px;
    border-radius: 5px;
    min-height: 250px;
    background-color: #f5f7fa;
    border:1px solid #e4e7ed;
    color: #c0c4cc;
    cursor: not-allowed;
    img {
      width: auto;
      height: auto;
    }
  }

  .ClassDisable .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    float: left;
    position: relative;
    overflow: hidden;
    cursor: no-drop;
    &:hover {
      border-color: #b6b5b5;
    }
    .el-progress {
      position: absolute;
      z-index: 999;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: white;
      .el-progress-circle {
        margin: 13% auto 10%;
      }
    }
  }

  .buttonentry {
    float: left;
    text-align: left;
  }
  .a{
    width: 6vw!important;
  }
  .drawer_footer{
    text-align: center;
  }
  :focus {
    outline:0;
  }

</style>
