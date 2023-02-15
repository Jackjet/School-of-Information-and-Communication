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
      <el-form-item label="姓名:" :label-width="this.formLabelWidth" prop='name'>
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          :disabled="disabled"
          placeholder="请输入姓名"
          v-model="ruleForm.name"
        ></el-input>
      </el-form-item>
      <el-form-item label="账号:" :label-width="this.formLabelWidth" prop='account'>
        <el-input  maxlength = '100'  placeholder="请输入账号,(字母，数字，字母+数字)" clearable size="medium" :disabled="disabledo" v-model="ruleForm.account"></el-input>
        <p style="color: red; padding: 0; margin: 10px 0px 0px 0px;">注：新用户初始密码是123456可以登录系统之后修改密码</p>
      </el-form-item>
      <el-form-item label="手机号:" :label-width="this.formLabelWidth" prop='phone'>
        <el-input  maxlength = '100'  placeholder="请输入手机号" clearable size="medium" :disabled="disabled" v-model="ruleForm.phone"></el-input>
      </el-form-item>
      <el-form-item label="邮箱:" :label-width="this.formLabelWidth" prop='email'>
        <el-input  maxlength = '100'  placeholder="请输入邮箱" clearable size="medium" :disabled="disabled" v-model="ruleForm.email"></el-input>
      </el-form-item>
      <el-form-item label="组织机构:":label-width="this.formLabelWidth" >
        <el-cascader
          placeholder="请选择组织机构"
          :props="props"
          :disabled="disabled" 
          collapse-tags
          clearable
          v-model="ruleForm.organizationId"
          :options="organizationOptions"
          @change="handleChange">
        </el-cascader>
      </el-form-item>
      <el-form-item
        prop="roleId"
        label="角色权限:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select v-model="ruleForm.roleId" clearable  :disabled='disabled'  placeholder="请选择角色权限">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="性别:" :label-width="this.formLabelWidth">
        <el-select v-model="ruleForm.sex" clearable :disabled='disabled' placeholder="请选择性别">
          <el-option
            key='0'
            label="女"
            value='0'>
          </el-option>
          <el-option
            key='1'
            label="男"
            value='1'>
          </el-option>
        </el-select>
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
import { organizationFindAll } from "@/api/organization.js";
import { roleFindAll } from "@/api/role.js";
import { webAdminUserInsert, webAdminUserUpdate, webAdminUserFind } from "@/api/userMen.js";
import { getToken } from '@/utils/auth'
export default {
  components: { },
  data() {
      return {
        title: '新增',
        disabled: false,
        disabledo: false,
        props: { checkStrictly: true },
        ruleForm: {
          account: '',
          name: "",
          phone: '',
          sex: '',
          organizationId: '',
          email: '',
          remark: '',
          roleId: ''
        },
        organizationOptions: [],
        options: [],
        formLabelWidth: "120px",
        rules: {
          name: [
            {
              required: true,
              message: "请输入姓名",
              trigger: "change",
            },
          ],
          account: [{
            required: true,
            message: "请输入账号",
            trigger: "change",
          }, {
            pattern:  /^[0-9A-Za-z]{1,20}$/, //正则
            message: '请输入账号,(字母，数字，字母+数字)'
         }],  
          phone: [{
            required: false,
            message: '请输入手机号',
            trigger: ['change', 'blur']
            },
            {
              pattern:  /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/g,  //正则
              message: '请输入正确的手机号'
            }
          ],
          email: [{
            required: false,
            message: '请输入手机号',
            trigger: ['change', 'blur']
            },
            {
              pattern: /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,  //正则
              message: '请输入正确的邮箱'
            }
          ],
        },
    }
  },
  created () {
    if (this.$route.query.type === '新增') {
      this.title = this.$route.query.type
      this.findorganizations()
      this.findroles()
    }else if(this.$route.query.type === '编辑'){
      this.title = this.$route.query.type
      this.findorganizations()
      this.findroles()
      this.disabledo = true
    }else if(this.$route.query.type === '详情'){
      this.title = this.$route.query.type
      this.findorganizations()
      this.findroles()
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
      const response = await webAdminUserFind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data
        that.ruleForm.sex = that.ruleForm.sex === 0 ? '0': '1'
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    handleChange() {
    },
    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // this.loading = true;
          // 取值
          const that = this
          let data = this.ruleForm
          if(Array.isArray(data.organizationId) && data.organizationId.length > 1){
            data.organizationId = data.organizationId.pop()
          }else if(Array.isArray(data.organizationId) && data.organizationId.length === 1){
            data.organizationId = data.organizationId[0]
          }
          if (this.$route.query.type === '编辑') {
            // 编辑
            webAdminUserUpdate(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('编辑成功');
                this.resetForm('ruleForm')
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            // 新增
            webAdminUserInsert(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('新增成功');
                this.resetForm('ruleForm')
                this.loading = false;
              } else {
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
      this.ruleForm.account = "";
      this.ruleForm.name = "";
      this.ruleForm.phone = "";
      this.ruleForm.sex = "";
      this.ruleForm.organizationId = "";
      this.ruleForm.email = "";
      this.ruleForm.remark = "";
      this.ruleForm.roleId = "";
      this.$router.push({ 
        path: '/systemManage/user'
      })
    },
    async findorganizations(data) {
      const that = this
      that.loading = true
      const response = await roleFindAll()
      that.loading = false
      if (response.data.code === 1) {
        that.options = []
        response.data.data.content.map(function(v, k){
          that.options.push({value: v.id, label:v.name})
        })
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async findroles(data) {
      const that = this
      that.loading = true
      const response = await organizationFindAll()
      that.loading = false
      if (response.data.code === 1) {
        let arrData = []
        arrData.push(response.data.data) 
        that.organizationOptions =  this.getJsonTree(arrData, '')
        if(this.$route.query.type !== '新增'){
          this.userDetail()
        }   
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    getJsonTree(data, parentId) {
      let itemArr = []
      for (let i = 0; i < data.length; i++) {
        let node = data[i];
        if(node.parentId === parentId) {
          let newNode = {};
          newNode.value = node.id;
          newNode.label = node.name;
          if(node.children.length > 0) {
            newNode.children = this.getJsonTree(node.children, node.id);
          }      
          itemArr.push(newNode);
        }
      }
      return itemArr;
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