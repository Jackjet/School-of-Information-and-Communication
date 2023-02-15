<template>
  <el-drawer
    :title="modalObjj"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="name"
        :label="namee"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.name" :placeholder="'请输入' + namee"></el-input>
      </el-form-item>
      <el-form-item
        prop="fileName"
        label="文件名:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'> 
        <!-- <el-input clearable :disabled='true' autocomplete="off" v-model="ruleForm.fileName" placeholder="请输入文件名"></el-input> -->
        <el-upload
        class="upload-demo"
        :headers='headers'
        :action="action"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :on-success="handleSuccess"
        :before-remove="beforeRemove"
        :on-error="herror"  
        limit="1"
        :on-exceed="handleExceed"
        :file-list="fileList">
        <el-button size="small" type="primary">选择文件</el-button>
        <!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
      </el-upload>
      </el-form-item>
      <el-form-item
        label="描述:"  
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.description" placeholder="请输入描述"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>

import { insertKnowledge, roleList, findById, deleteFile, updateKnowledge} from '@/api/laws'
import { isMPStrict } from '@/utils/validate'
import { getToken } from '@/utils/auth'
export default {
  components: {
    // 注册组件
    // editor: editor
  },

  props: {
    modalObjj: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      ruleForm: {
        fileName: '',
        description: '',   
        file: '',
        name: '',
        type: ''
      },
      fileList: [],
      obj: {},
      rule: {},
      options: [],
      rules: {
        fileName: [{
          required: true,
          message: '请选择文件',
          trigger: ['change', 'blur']
        }]
      },
      namee: '',
      action: process.env.VUE_APP_BASE_API  + '/webadmin/service/file/uploadRegulationKnowledgeFile?type=',
      headers:{
        Authorization : 'token ' + getToken(),
      },
      // 是否触发禁用表单项
      ProhibitFrom: false,
      ProhibitFromm: false,
      dialogFormVisible: this.show,
      formLabelWidth: '120px',
      lock: false,
      // Http: 'http://apptest.liangxin.net.cn/',
      dialogVisible: true,
      loading: false,
     
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  created() {
    // alert(1)
    // this.findNodes()

  },
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    initial: function (value,name) {
      const that = this
      that.namee = '名称:'
      that.ruleForm.type = name
      that.action = process.env.VUE_APP_BASE_API  + '/webadmin/service/file/uploadRegulationKnowledgeFile?type=' + name + ''
      that.rules.name  =  [{
        required: true,
        message: '请输入名称',
        trigger: 'blur'
      }],
      that.dialogFormVisible = true
      if (value) {
        that.nowId = value
        that.initFormDetail(value)
      } else {
        // let year = new Date().getYear()
        that.ProhibitFrom = false
      }
    },

    selectGet(vId){
      const that = this 
      that.obj = this.options.find((item)=>{//this.ruleForm
        return item.value === vId;//筛选出匹配数据
      });
    },
    /**
     * 初始化获取当前需要编辑 查看的详细数据
     * @param value 当前需要操作的唯一iD
     */
    async initFormDetail (value) {
      const that = this
      try {
        let parameter = {id: value}
        that.loading = true
        const response = await findById(parameter)
        that.fileList.push({name: response.data.data.fileName, url:response.data.data.file })
        that.loading = false
        that.ruleForm = response.data.data
        if (this.modalObjj === '详情') {
          that.ProhibitFrom = true
          // 禁用编辑器
        } else if (this.modalObjj === '编辑') {
          if(value === 'admin'){
            that.ProhibitFrom = false
            that.ProhibitFromm = true
          } else {
            that.ProhibitFrom = false
            that.ProhibitFromm = false
          }
          that.lock = true
        }  else {
          that.lock = false
          that.ProhibitFrom = false
          that.ProhibitFromm = false
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },



    // 关闭模态框 清空所有表单项 为编辑器初始化
    CloseModal () {
      const that = this
      that.ClearTextConeten()
      that.ProhibitFrom = false
      // if (!that.rules.name) {
      //   that.rules = that.rule
      // }
    },

    // 初始化表单
    ClearTextConeten () {
      const that = this
      that.$refs['ruleForm'].resetFields()
      that.ruleForm.fileName = ''
      that.ruleForm.description = ''
      that.ruleForm.file = ''
      that.ruleForm.name = ''
      that.ruleForm.type = ''
      that.fileList = []
      that.ProhibitFrom = false
      that.ProhibitFromm = false
      that.dialogFormVisible = false
      if (that.ruleForm.id) {
        delete that.ruleForm.id
      }
    },

    SuretoAddClick (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }
        Object.entries(that.ruleForm).map((a, b) => {
          if (a[0] === 'id') {
            delete that.ruleForm[a[0]]
          }
        })
        if (this.modalObjj === '添加') {
          that.add(that.ruleForm)
          return false
        } else if (this.modalObjj === '编辑') {
          that.ruleForm.id = that.nowId
          that.edit(that.ruleForm)
          return false
        }
      })
    },

    // 添加方法
    async add(data) {
      const that = this
      // POST请求添加轮播图数据
      let da = data
      // da.roleName = that.obj.label
      that.loading = true
      const response = await insertKnowledge(da)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextConeten()
        // 重新触发父组件加载loading
        that.$emit('Reload')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    handleSuccess (response, file, fileList) {
     if(fileList[0].response.code == 1 ){
      this.ruleForm.fileName  = fileList[0].response.data.fileName
      this.ruleForm.file =  fileList[0].response.data.fileDownloadUri
      this.$message.success(fileList[0].response.msg)
     }else{
       this.$message.error(fileList[0].response.msg)
     }
     
    },
    herror(error, file, fileList){
      const result = JSON.parse(error.message);
      this.$message.error('上传失败:' + result.msg)
    },
    handlePreview (file) {
      console.log(file)
    },
    handleExceed (files, fileList) {
      this.$message.warning(`当前限制只能选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemove (file, fileList) {  
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    async handleRemove(file, fileList){
      const that = this
      that.Loading = true
      // let datas = {
      //   type: that.ruleForm.type
      // }
      const response = await deleteFile(file.name, that.ruleForm.type)
      that.Loading = false
      if (response.data.code === 1) {
        that.$message.success('删除成功')
        that.ruleForm.fileName  = ''
        that.ruleForm.file = ''
        that.fileList = []
        return true 
      } else {
        that.$message.error(response.data.msg)
          return false
      }
    },
    // 修改方法
    async edit(data) {
      const that = this
      // PUT 请求修改数据
      that.loading = true
      const response = await updateKnowledge(data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextConeten()
        // 重新触发父组件加载loading
        that.$emit('Reload')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

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
