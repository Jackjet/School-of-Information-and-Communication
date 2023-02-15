<template>
  <el-main class="main">
    <div><h4>{{title}}</h4></div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">

        <el-form-item class="span-center" label="行业名称:" prop="name" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' placeholder="请输入行业名称" size="medium" v-model="ruleForm.name"/>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="resetForm('ruleForm')">返回列表</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm')">确 定</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
  import {
    add,
    find,
    update,
  } from "@/api/industries.js";
  import {getToken} from '@/utils/auth'

  export default {
    components: {},
    data() {
      return {
        title: '添加行业',
        props: {checkStrictly: true},
        ruleForm: {
          name: '',
        },
        formLabelWidth: "120px",

        rules: {
          name: [
            {required: true, message: '请输入行业名称', trigger: 'blur'},
          ]
        },
      }
    },
    created() {
      if (this.$route.query.type === 'edit') {
        this.title = "行业编辑";
        this.getDetail()
      }
    },
    methods: {
      // 详情
      async getDetail() {
        const that = this;
        let data = {id: this.$route.query.id}
        that.loading = true;
        const response = await find(data);
        that.loading = false;
        if (response.data.code === 1) {
          that.ruleForm = response.data.data;
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg);
          return false
        }
      },

      submitForm(ruleForm) {
        this.$refs["ruleForm"].validate((valid) => {
          if (valid) {
            this.loading = true;
            // 取值
            const that = this;
            let data = this.ruleForm;
            if (this.$route.query.type === 'edit') {
              // 编辑
              update(data).then((res) => {
                if (res.data.code === 1) {
                  this.$message.success('编辑成功');
                  this.resetForm('ruleForm');
                  this.loading = false;
                } else {
                  this.$message.error(res.data.msg);
                }
              });
            } else {
              // 新增
              add(data).then((res) => {
                if (res.data.code === 1) {
                  this.$message.success('添加成功');
                  this.resetForm('ruleForm');
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
        this.closeModal();
      },
      // 初始化表单
      closeModal() {
        //清空表单数据
        for (let key in this.ruleForm) {
          this.ruleForm[key] = "";
        }

        this.$router.push({
          path: '/industries/list'
        })
      },
    }
  }
</script>
<style lang="scss" scoped>
  /deep/ .el-input {
    width: 100% !important;
  }

  /deep/ .el-cascader {
    width: 100% !important;
  }

  /deep/ .el-select {
    width: 100% !important;
  }

  /deep/ .el-form-item__content {
    line-height: 20px;
  }

  .main {
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
    .main-center {
      width: 45%;
      margin: 0 0 0 100px;
    }
  }

  .demo-drawer__footer {
    margin-top: 80px;
    text-align: center;
  }

</style>
