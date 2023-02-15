<template>
  <el-main class="main">
    <div> <h4></h4></div>
    <el-col class="main-center">
      <el-form
        v-loading="loading"
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="150px"
        class="demo-ruleForm"
      >
        <el-row class="bottom">
          <el-form-item label="系统类型:" :label-width="this.formLabelWidth" prop='name'>
            <el-input
              clearable
              maxlength = '100'
              size="medium"
              :disabled="disabled"
              placeholder="请输入系统类型"
              v-model="ruleForm.osName"
            ></el-input>
          </el-form-item>
          <el-form-item label="硬盘大小:" :label-width="this.formLabelWidth" prop='name'>
            <el-input
              clearable
              maxlength = '100'
              size="medium"
              :disabled="disabled"
              placeholder="请输入硬盘大小"
              v-model="ruleForm.total"
            ></el-input>
          </el-form-item>
          <el-form-item label="已用硬盘大小:" :label-width="this.formLabelWidth" prop='name'>
            <el-input
              clearable
              maxlength = '100'
              size="medium"
              :disabled="disabled"
              placeholder="请输入已用硬盘大小"
              v-model="ruleForm.used"
            ></el-input>
          </el-form-item>
          <el-form-item label="内存大小:" :label-width="this.formLabelWidth" prop='name'>
            <el-input
              clearable
              maxlength = '100'
              size="medium"
              :disabled="disabled"
              placeholder="请输入内存大小"
              v-model="ruleForm.memTotal"
            ></el-input>
          </el-form-item>
          <el-form-item label="已用内存大小:" :label-width="this.formLabelWidth" prop='name'>
            <el-input
              clearable
              maxlength = '100'
              size="medium"
              :disabled="disabled"
              placeholder="请输入已用内存大小"
              v-model="ruleForm.memRam"
            ></el-input>
          </el-form-item>
          <el-form-item label="CPU已用百分比:" :label-width="this.formLabelWidth" prop='name'>
            <el-input
              clearable
              maxlength = '100'
              size="medium"
              :disabled="disabled"
              placeholder="请输入CPU已用百分比"
              v-model="ruleForm.combined"
            ></el-input>
          </el-form-item>
        </el-row>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
  import { timedTaskFindOsInfo, findDatabaseInfo, timedTaskInsert, timedTask } from "@/api/info.js";
  import { getToken } from '@/utils/auth'
  export default {
    components: { },
    data() {
      return {
        title: '新增',
        disabled: true,
        disabledo: true,
        loading: false,
        isActive: true,
        props: { checkStrictly: true },
        ruleForm: {
          osName: "",
          total: '',
          used: "",
          memTotal: '',
          memRam: "",
          combined: ''
        },
        type:'',
        organizationOptions: [],
        formLabelWidth: "120px",
        rules: {
          phone: [{
            required: false,
            trigger: ['change', 'blur']
          },
            {
              pattern:  /^\/(?:[^\/]+\/)*$/,  //正则
              message: '请输入正确的文件夹路径'
            }
          ],
        },
      }
    },
    created () {
      this.osDetail()
    },
    methods: {
      // 服务器详情
      async osDetail() {
        const that = this
        let data = {}
        that.loading = true
        const response = await timedTaskFindOsInfo(data)
        that.loading = false
        if (response.data.code === 1) {
          let datas =  response.data.data
          that.ruleForm.osName = datas.osBasicInfo.osName
          that.ruleForm.total = datas.hardDiskInfo.total
          that.ruleForm.used = datas.hardDiskInfo.used
          that.ruleForm.memTotal = datas.ramInfo.memTotal
          that.ruleForm.memRam = datas.ramInfo.memRam
          that.ruleForm.combined = datas.cpuInfo.combined
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
    }
  }
</script>
<style lang="scss" scoped>
  /deep/ .el-input-number__increase .el-input{
    width: 100%!important;
  }
  /deep/ .el-input{
    //  width: 50%!important;
  }
  /deep/ .el-cascader{
    width: 50%!important;
  }
  //  /deep/ .el-select{
  //     width: 50%!important;
  //  }
  /deep/.el-form-item__content{
    line-height: 20px;
  }
  .top{
    position: relative;
    padding-left: 0%;
    padding-top: 2%;
    padding-right: 5%;
    margin-top: 10px;
  }
  .bottom{
    position: relative;
    padding: 20px 10px 20px 10px;
    padding-left: 0%;
    padding-top: 2%;
    padding-right: 15%;
    margin-top: 10px;
  }
  .title{
    position:absolute;
    left: 3%;
    top: -20px;
    width: 120px;
    height: 35px;
    line-height: 35px;
    background: #409EFF;
    color: #fff;
    text-align: center;
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
      width: 90%;
      margin: 0 auto;
    }
  }
  .demo-drawer__footer{
    margin-top: 80px;
    text-align: center;
  }
  .center{
    display: flex;
    align-items: center;     /* 垂直居中 */
  }
  /deep/ .center .el-form-item__content{
    margin-left: 5px!important;
  }
</style>
