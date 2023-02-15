<template>
  <el-main class="main">
     <div> <h4>{{title}}</h4></div>
     <el-col class="main-center">
      <el-form
       v-loading="loading"
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
     >
      <el-form-item label="角色权限名称:" :label-width="this.formLabelWidth" prop='name'>
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          :disabled="disabled"
          placeholder="请输入角色权限名称"
          v-model="ruleForm.name"
        ></el-input>
      </el-form-item>
      <el-form-item label="备注:" :label-width="this.formLabelWidth">
        <el-input  maxlength = '200' type="textarea"  placeholder="请输入备注" clearable size="medium" :disabled="disabled" v-model="ruleForm.remark"></el-input>
      </el-form-item>
       <div class="demo-drawer__footer drawer_footer">
        <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
        <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">保 存</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>
</template>
<script>
import { roleInsert, roleUpdate } from "@/api/role.js";
import { getToken } from '@/utils/auth'
export default {
  components: { },
  data() {
    return {
      title: '新增',
      disabled: false,
      disabledo: false,
      loading: false,
      props: { checkStrictly: true },
      ruleForm: {
        name: "",
        remark: ''
      },
      organizationOptions: [],
      options: [],
      formLabelWidth: "120px",
      rules: {
        name: [
          {
            required: true,
            message: "请输入角色权限名称",
            trigger: "change",
          },
        ]
      }
    }
  },
  created () {
    if (this.$route.query.type === '新增') {
      this.title = this.$route.query.type
    }else if(this.$route.query.type === '编辑'){
      this.title = this.$route.query.type
      this.ruleForm.id = this.$route.query.id
      this.ruleForm.name = this.$route.query.name
      this.ruleForm.remark = this.$route.query.remark
      this.disabledo = true
    }
  },
  methods: {
    handleChange() {
    },
    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.loading = true;
          // 取值
          const that = this
          let data = this.ruleForm
        
          if (this.$route.query.type === '编辑') {
            // 编辑
            roleUpdate(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('编辑成功');
                this.resetForm('ruleForm')
                this.loading = false;
              } else {
                this.loading = false;
                this.$message.error(res.data.msg);
              }
            });
          } else {
            // 新增
            roleInsert(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('新增成功');
                this.resetForm('ruleForm')
                this.loading = false;
              } else {
                this.loading = false;
                this.$message.error(res.data.msg);
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      this.disabled = false;
      this.disabledo = false
      this.ruleForm.name = "";
      this.ruleForm.remark = "";
      this.$router.push({ 
        path: '/systemManage/role/roles'
      })
    }
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
