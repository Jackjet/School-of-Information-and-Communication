<template>
  <el-drawer
    :title="title"
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
        label="选择新增:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select @change="changeType" v-model="ruleForm.selectData">
        <el-option label="文件" value="0"></el-option>
        <el-option label="文件夹" value="1"></el-option>
      </el-select>
      </el-form-item>
    </el-form>

    <div v-show="file_div_isShow" id="file_div" class="demo-drawer__footer drawer_footer">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="name"
        label="所在目录:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input disabled autocomplete="off" v-model="ruleForm.currentDir" :placeholder="'请输入' + namee"></el-input>
      </el-form-item>
      <!-- <el-form-item
        prop="name"
        label="文件名称:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.name" :placeholder="'请输入' + namee"></el-input>
      </el-form-item> -->
      <el-form-item
          ref="uploadImg"
          prop="fileName"
          label="选择文件:"
          style="text-align:left"
          :label-width="this.formLabelWidth"
          class='InpitWidth'> 
          <!-- <el-input  clearable :disabled='true' autocomplete="off" v-model="ruleForm.fileName" placeholder="请输入文件名"></el-input> -->
          <el-upload
          class="upload-demo"
          :headers='headers'
          :action="action"
          :data="data"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          :before-remove="beforeRemove"
          :on-error="herror"  
          limit="1"
          :on-exceed="handleExceed"
          :file-list="fileList">
          <el-button size="small" type="primary">上传文件</el-button>
          <!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
        </el-upload>
        </el-form-item>
      </el-form>
    </div>
    <div id="dir_div" v-show="dir_div_isShow" class="demo-drawer__footer drawer_footer">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="name"
        label="所在目录:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input disabled autocomplete="off"  v-model="ruleForm.currentDir" :placeholder="'请输入'"></el-input>
      </el-form-item>
      <el-form-item
        prop="name"
        label="文件夹名称:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.dirName" :placeholder="'请输入'"></el-input>
      </el-form-item>
      </el-form>
    </div>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>

// import { insertKnowledge, roleList, findById, deleteFile, updateKnowledge} from '@/api/laws'
import { isMPStrict } from '@/utils/validate'
import { getToken } from '@/utils/auth'
import { createDir,deleteFile } from '@/api/website'
export default {
  components: {
    // 注册组件
    // editor: editor
  },

  props: {
    title: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      file_div_isShow:true,
      dir_div_isShow:false,
      ruleForm: {
        fileName: '0',
        description: '',   
        file: '',
        name: '',
        type: '',
        currentDir: '',
        dirName: '',
        selectData: '文件',
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
      action: process.env.VUE_APP_BASE_API  + 'webadmin/website/upload',
      data: {},
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
    initial: function (dir) {
      const that = this
      that.title = '添加'
      that.ruleForm.currentDir = dir
      that.data = {
        storagePath : dir
      }
      that.dialogFormVisible = true
    },

    selectGet(vId){
      const that = this 
      that.obj = this.options.find((item)=>{//this.ruleForm
        return item.value === vId;//筛选出匹配数据
      });
    },
    // 关闭模态框 清空所有表单项 为编辑器初始化
    CloseModal () {
      const that = this
      that.deleteFile()
      that.ClearTextConeten()
      that.ProhibitFrom = false
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
      that.ruleForm.currentDir = ''
      that.ruleForm.dirName = ''
      that.ruleForm.selectData = '文件'
      that.fileList = []
      that.ProhibitFrom = false
      that.ProhibitFromm = false
      that.dialogFormVisible = false
      if (that.ruleForm.id) {
        delete that.ruleForm.id
      }
      this.file_div_isShow = true;
      this.dir_div_isShow = false;
    },
    async SuretoAddClick (ruleForm) {
      const that = this
      if(this.file_div_isShow==true){
        that.ClearTextConeten()
        that.$emit('Reload')
      }else{
        const data = {
          dir : that.ruleForm.currentDir + "/" + that.ruleForm.dirName
        }
        try {
          const res = await createDir(data)
          console.log("res",res)
          that.ClearTextConeten()
          that.$emit('Reload')
        } catch (even) {
          this.$message.error(even.msg)
        }
      }
    },
    async deleteFile(){
      if(this.ruleForm.file.length > 0){
          const data = {
            path : this.ruleForm.file
          }
          try {
            const res = await deleteFile(data)
          } catch (even) {
            this.$message.error(even.msg)
          }
      }
    },
    handleSuccess (response, file, fileList) {
      this.ruleForm.fileName  = fileList[0].response.fileName
      this.ruleForm.file =  fileList[0].response.fileDownloadUri
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
    changeType(data) {
        const that = this
        if(data == 0){
            //选择文件
            this.file_div_isShow = true;
            this.dir_div_isShow = false;
            that.ruleForm.selectData = '文件'
        }else{
            //选择文件夹
            this.file_div_isShow = false;
            this.dir_div_isShow = true;
            that.ruleForm.selectData = '文件夹'
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
