<template>
  <el-drawer
    title="上传文件"
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
        ref="uploadImg"
        prop="fileName"
        label="文件名:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <!-- <el-input  clearable :disabled='true' autocomplete="off" v-model="ruleForm.fileName" placeholder="请输入文件名"></el-input> -->
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
          :file-list="fileList"
          :auto-upload="false"
          ref="upload">
          <el-button size="small" type="primary">选择文件</el-button>
          <!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
        </el-upload>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick()">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>

import { fileInsert} from '@/api/repository'
import { isMPStrict } from '@/utils/validate'
import { getToken } from '@/utils/auth'
export default {
  components: {
    // 注册组件
    // editor: editor
  },

  data () {
    return {
      fileList: [],
      // 是否触发禁用表单项
      ProhibitFrom: false,
      ProhibitFromm: false,
      dialogFormVisible: this.show,
      formLabelWidth: '120px',
      lock: false,
      dialogVisible: true,
      loading: false,
      action: process.env.VUE_APP_BASE_API  + '/service/file/resourceFile/upload?file=',
      file:{},
      repositoryId:'',
      parentId:''
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  created() {

  },
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    initial: function (node) {
      const that = this

      that.dialogFormVisible = true
      that.ProhibitFrom = false
      that.repositoryId = node.repositoryId
      that.parentId = node.parentId
    },

    // 关闭模态框 清空所有表单项 为编辑器初始化
    CloseModal () {
      const that = this
      that.ClearTextConeten()
      that.ProhibitFrom = false
    },

    // 初始化表单
    ClearTextConeten () {
      const that = this
      that.fileList = []
      that.dialogFormVisible = false
    },

    async SuretoAddClick () {
      const that = this
      that.$refs.upload.submit();
    },

    async save(){
      const that = this
      let res = await fileInsert(that.file);
      if (res.data.code === 1) {
        that.$message.success(res.data.msg)
        that.CloseModal()
        that.$emit('Reload')
      } else {
        that.$message.error(res.data.msg)
      }
    },

    handleSuccess (response, file, fileList) {
      let fileName = fileList[0].response.fileName.split('.')
      if(fileName.length < 2) {
        this.$message.error('非法文件格式')
        return false
      } else {
        if(fileName[1] !== 'zip' && fileName[1] !== 'pdf' && fileName[1] !== 'jpg' && fileName[1] !== 'png' && fileName[1] !== 'jpeg'){
          this.$message.error('非法文件格式')
          return false
        }
      }

      let type = '1'
      if (fileName[fileName.length - 1] === 'zip') {
        type = '2'
      } else if (fileName[fileName.length - 1] === 'pdf') {
        type = '3'
      }
      let data  = {
        fileName:fileList[0].response.fileName,
        fileDownloadUri:fileList[0].response.fileDownloadUri,
        fileType:fileList[0].response.fileType,
        size:fileList[0].response.size,
        repositoryId:this.repositoryId,
        parentId:this.parentId,
        type: type
      }

      this.file = data
      this.$refs['uploadImg'].clearValidate()

      this.save()
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
