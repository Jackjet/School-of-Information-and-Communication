<template>
  <el-main class="main">
     <div> <h4>{{title}}</h4></div>
     <el-col class="main-center">
      <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
     >
      <el-form-item label="操作人:" :label-width="this.formLabelWidth">
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          :disabled="disabled"
          placeholder="请输入操作人"
          v-model="ruleForm.createByName"
        ></el-input>
      </el-form-item>
      <el-form-item label="操作模块:" :label-width="this.formLabelWidth">
        <el-input  maxlength = '100'  placeholder="请输入操作模块" clearable size="medium" :disabled="disabledo" v-model="ruleForm.module"></el-input>
      </el-form-item>
      <el-form-item label="操作类型:" :label-width="this.formLabelWidth" >
        <el-input  maxlength = '100'  placeholder="请输入操作类型" clearable size="medium" :disabled="disabled" v-model="ruleForm.api"></el-input>
      </el-form-item>
      <el-form-item label="操作内容:" :label-width="this.formLabelWidth">
        <el-input  maxlength = '200' :rows='4' type="textarea"  placeholder="请输入内容" clearable size="medium" :disabled="disabled" v-model="ruleForm.contentMsg"></el-input>
      </el-form-item>
      <el-form-item label="操作结果:" :label-width="this.formLabelWidth" >
        <el-input  maxlength = '100'  placeholder="请输入操作结果" clearable size="medium" :disabled="disabledo" v-model="ruleForm.result"></el-input>
      </el-form-item>
      <el-form-item label="操作时间:" :label-width="this.formLabelWidth" >
        <el-input  maxlength = '100'  placeholder="请输入操作时间" clearable size="medium" :disabled="disabledo" v-model="ruleForm.createTime"></el-input>
      </el-form-item>
       <div class="demo-drawer__footer drawer_footer">
        <el-button size="medium" @click="resetForm('ruleForm')">返回列表</el-button>
        <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">确 定</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>
  
</template>

<script>
import {
  operationLogFind
} from "@/api/log.js"
import { getToken } from '@/utils/auth'
export default {
  components: { },
  data() {
      return {
        title: '新增',
        disabled: true,
        disabledo: true,
        props: { checkStrictly: true },
        ruleForm: {
          createByName: '',
          api: "",
          contentMsg: '',
          result: '',
          createTime: ''
        },
        organizationOptions: [],
        options: [],
        formLabelWidth: "120px"
    }
  },
  created () {
    if(this.$route.query.type === '详情'){
      this.title = this.$route.query.type
      this.userDetail()
      this.disabledo = true
      this.disabled = true
    }
  },
  methods: {
    // 详情
    async userDetail() {
      const that = this
      let data = { id: this.$route.query.id }
      that.loading = true
      const response = await operationLogFind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data
        that.ruleForm.result = that.ruleForm.result === 0 ? '失败': '成功'
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      this.disabled = false;
      this.disabledo = false
      this.ruleForm.module = "";
      this.ruleForm.createByName = "";
      this.ruleForm.api = "";
      this.ruleForm.contentMsg = "";
      this.ruleForm.result = "";
      this.ruleForm.createTime = "";
      this.$router.push({ 
        path: '/logManage/operationLog/operationLogs'
      })
    },
  }
}
</script>
<style lang="scss" scoped>
 /deep/ .el-input{
   width: 100%!important;
 }
 /deep/ .el-cascader{
    width: 100%!important;
 }
 /deep/ .el-select{
    width: 100%!important;
 }
 /deep/.el-form-item__content{
   line-height: 20px;
 }
 .main{
  display: flex;
  flex-direction: column;
  background: #fff;
  height: 100%;
  width: 95%;
  margin: 0 auto;
  min-height: 600px;
  margin-top: -240px;
  border-radius: 5px;
  padding: 10px 20px;
  .main-center{
    width: 45%;
    margin: 0 0 0 100px;
  }
 }
 .demo-drawer__footer{
   margin-top: 80px;
   text-align: center;
 }

</style>