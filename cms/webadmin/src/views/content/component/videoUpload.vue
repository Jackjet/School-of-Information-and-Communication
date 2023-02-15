<template>
  <el-upload
    :ref="uploadRef"
    :action="action"
    :file-list="fileList"
    :fieldName="fieldName"
    :on-success="handleSuccess"
  >
    <el-button size="small" type="primary">点击上传</el-button>
  </el-upload>
</template>
<script>
import Template from '../../template/template';
export default {
  components: {Template},
  data() {
    return {
      fileList: [],
      action: '',
      uploadRef:'',
      fieldName:''
    }
  },
  methods: {
    handleSuccess(response, file, fileList){
      let data  = {
        fileName: fileList[fileList.length - 1].response.fileName,
        fileDownloadUri: fileList[fileList.length - 1].response.fileDownloadUri,
        fileType: fileList[fileList.length - 1].response.fileType,
        size: fileList[fileList.length - 1].response.size
      }

      this.$emit('uploadClick',this.uploadRef,data,false,this.fieldName)
    },
    clearFile(){
      const that = this
      that.fileList = []
    }
  },
  props:{
    uploadRef: String,
    fileList: Array,
    action: String,
    fieldName: String
  },
  watch:{
    uploadRef (newValue ,oldValue){
      oldValue = newValue
    },
    fileList (newValue ,oldValue){
      oldValue = newValue
    },
    action (newValue ,oldValue){
      oldValue = newValue
    },
    fieldName (newValue ,oldValue) {
      oldValue = newValue
    }
  }
}
</script>
