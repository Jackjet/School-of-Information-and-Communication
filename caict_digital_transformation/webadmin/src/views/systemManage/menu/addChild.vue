<template>
  <el-main class="main">
     <div> <h4>{{title}}</h4></div>
     <el-col class="main-center">
      <el-form
      v-loading="isSubmitLoading"
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
     >
      <el-form-item label="名称:" :label-width="this.formLabelWidth" prop='name'>
        <el-input
          clearable
          maxlength="100"
          size="medium"
          :disabled="disabled"
          placeholder="请输入名称"
          v-model="ruleForm.name"
        ></el-input>
      </el-form-item>
      <el-form-item
        prop="parentName"
        label="父级菜单:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          disabled="true"
          placeholder="请输入父级菜单"
          v-model="ruleForm.parentName"
        ></el-input>
      </el-form-item>
      <el-form-item label="类型:" prop='type' :label-width="this.formLabelWidth">
        <el-select v-model="ruleForm.type" clearable  :disabled='disabled' placeholder="请选择类型">
          <el-option
            key="0"
            label="菜单"
            value="0">
          </el-option>
          <el-option
            key="1"
            label="功能按钮"
            value="1">
          </el-option>
          <el-option
            key="2"
            label="链接新窗口"
            value="2">
          </el-option>
          <el-option
            key="3"
            label="链接内页"
            value="3">
          </el-option>
          <el-option
            key="4"
            label="内置功能页"
            value="4">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="路由地址:"  prop='path' :label-width="this.formLabelWidth">
        <el-input  maxlength = '100'   placeholder="请输入路由地址例如: /path" clearable size="medium" :disabled="disabled" v-model="ruleForm.path"></el-input>
      </el-form-item>
      <el-form-item label="页面地址:"  prop='component' :label-width="this.formLabelWidth">
        <el-input  maxlength = '100'   placeholder="请输入页面地址例如: @/views/login/index" clearable size="medium" :disabled="disabled" v-model="ruleForm.component"></el-input>
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
import { menuTreeInsert, menuTreeUpdate } from "@/api/menu.js";
import { getToken } from '@/utils/auth'
import { icons } from '@/utils/icon'

export default {
  components: { },
  data() { 
      return {
        title: '新增',
        disabled: false,
        disabledo: false,
        props: { checkStrictly: true },
        ruleForm: {
          path: '',
          component: '',
          redirect: '',
          name: '',
          parentName: '',
          meta: {title: '', type: '',icon:''},
          parentId: '',
          child: '',
          type: ''      
        },
        isSubmitLoading: false,
        organizationOptions: [],
        options: [],
        formLabelWidth: "120px",
        rules: {
          name: [
            {
              required: true,
              message: "请输入名称",
              trigger: "change",
            },
          ],
          parentName: [
            {
              required: true,
              message: "请输入父级菜单",
              trigger: "change",
            },
          ],
          type: [{
            required: true,
            message: '请选择类型',
            trigger: ['change', 'blur']
            }
          ],
          path: [{
            required: true,
            message: '请输入路由地址',
            trigger: ['change', 'blur']
            }
          ],
          component: [{
            required: true,
            message: '请输入页面地址',
            trigger: ['change', 'blur']
            }
          ],
        },
    }
  },
  created () {
    this.options = icons
    if (this.$route.query.type === '新增') {
      this.title = this.$route.query.type
      this.ruleForm.parentId = this.$route.query.id
      this.ruleForm.parentName = this.$route.query.name
    }else if(this.$route.query.type === '编辑'){
      this.title = this.$route.query.type
      this.ruleForm = this.$route.query
      this.ruleForm.type = this.$route.query.types + ""
    }
  },  
  methods: {
    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.isSubmitLoading = true;
          // 取值
          const that = this
          let data = JSON.parse(JSON.stringify(that.ruleForm))
          data.meta.title = data.name
          data.meta = JSON.stringify(this.ruleForm.meta)
          if(this.title === '编辑') {
            // 编辑
            menuTreeUpdate(data).then((res) => {
              if (res.data.code === 1) {
                  this.$message.success('编辑成功');
                  this.resetForm('ruleForm')
                this.isSubmitLoading = false;
              } else {
                that.isSubmitLoading = false
                this.$message.error(res.data.msg);
              }
            });
          } else {
            // 新增
            menuTreeInsert(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('新增成功');
                this.resetForm('ruleForm')
                this.isSubmitLoading = false;
              } else {
                that.isSubmitLoading = false
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
      this.ruleForm.path = "";
      this.ruleForm.component = "";
      this.ruleForm.redirect = "";
      this.ruleForm.name = "";
      this.ruleForm.parentName = "";
      this.ruleForm.meta.title =  '';
      this.ruleForm.meta.type = '';
      this.ruleForm.meta.icon = '';
      this.ruleForm.parentId = "";
      this.ruleForm.child = "";
      this.ruleForm.type = "";
      this.$router.push({ 
        path: '/systemManage/menu/menus'
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