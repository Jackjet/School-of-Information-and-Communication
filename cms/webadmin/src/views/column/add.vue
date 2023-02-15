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
        v-if="ruleForm.parentName !== undefined && ruleForm.parentName !== ''"
        label="父级栏目"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input disabled v-model="ruleForm.parentName"></el-input>
      </el-form-item>

      <el-form-item
        prop="columnName"
        label="栏目名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.columnName" placeholder="请输入栏目名称"></el-input>
      </el-form-item>
      <el-form-item
        prop="templateId"
        label="模板名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select v-model="ruleForm.templateId" placeholder="请选择模板">
          <el-option
            v-for="item in templates"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        ref="uploadImg"
        prop="fileName"
        label="栏目图片:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-upload
          class="upload-demo"
          :headers='headers'
          :action="action"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          :before-remove="beforeRemove"
          :on-error="herror"
          :file-list="fileList">
          <el-button size="small" type="primary">选择文件</el-button>
        </el-upload>
        <el-button size="small" @click.native="addPicture()" type="primary" >资源库上传</el-button>
          <br>
          <template>
            <el-table
              :data="tableList"
              style="width: 100%">
              <el-table-column
                prop="fileName"
                label="文件名"
                width="180">
              </el-table-column>
              <el-table-column
                label="操作">
                <template slot-scope="scope">
                  <el-button type="text" @click="deleteRow(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </template>
      </el-form-item>
      <el-form-item
        prop="name"
        label="SEO标题"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.seoTitle" placeholder="请输入SEO标题"></el-input>
      </el-form-item>
      <el-form-item
        prop="name"
        label="SEO关键字"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.seoKeyWord" placeholder="请输入SEO关键字"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
    <RepositoryLib ref="repositoryLib" @sure="sure"></RepositoryLib>
  </el-drawer>
</template>

<script>

import { findById,insert,update,templateFindAll } from '@/api/column'
import { isMPStrict } from '@/utils/validate'
import { getToken } from '@/utils/auth'
import RepositoryLib from '@/views/column/repository/index.vue';
export default {
  components: {
    // 注册组件
    // editor: editor
    RepositoryLib
  },

  props: {
    title: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      ruleForm: {
        parentName:'',
        columnName:'',
        templateId:'',
        seoTitle:'',
        seoKeyWord:'',
        parentId:'',
        attachment:[]
      },
      rules: {
        columnName: [{
          required: true,
          message: '请输入栏目名称',
          trigger: ['change', 'blur']
        }],
        templateId: [{
          required: true,
          message: '请选择模板',
          trigger: ['change', 'blur']
        }]
      },
      headers:{
        Authorization : 'token ' + getToken(),
      },
      // 是否触发禁用表单项
      ProhibitFrom: false,
      ProhibitFromm: false,
      dialogFormVisible: this.show,
      formLabelWidth: '120px',
      lock: false,
      dialogVisible: true,
      loading: false,
      fileList: [],
      action: process.env.VUE_APP_BASE_API  + '/service/file/column/pictureUpload?file=',
      file:{},
      templates:[],
      uploadFile: new Array(),
      tableList: new Array()
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
    async initial (title,id,columnName) {
      const that = this

      await this.getTemplates()
      that.dialogFormVisible = true
      that.title = title
      if (id && title){
        if(title === '增加子栏目'){
          that.ruleForm.parentId = id
          that.ruleForm.parentName = columnName
          that.ProhibitFrom = false

        } else {
          that.nowId = id
          that.initFormDetail(id)
        }
      } else {
        that.ProhibitFrom = false
      }
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
        that.loading = false

        if (response.data.code === 1) {
          // 查询成功后 回调效果
          that.ruleForm = response.data.data

          this.tableList = that.ruleForm.attachment
        } else {
          // 查询失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
        if (this.title === '详情') {
          that.ProhibitFrom = true
          // 禁用编辑器
        } else if (this.title === '编辑') {
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
    },

    // 初始化表单
    ClearTextConeten () {
      const that = this
      that.$refs['ruleForm'].resetFields()
      that.ruleForm.columnName = ''
      that.ruleForm.templateId = ''
      that.ruleForm.seoTitle = ''
      that.ruleForm.seoKeyWord = ''
      that.ruleForm.parentId = ''
      that.ruleForm.attachment = []
      that.ProhibitFrom = false
      that.ProhibitFromm = false
      that.dialogFormVisible = false
      that.fileList=[]
      that.uploadFile = []
      that.tableList = []
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

        console.log('this.uploadFile')
        console.log(this.uploadFile)

        this.uploadFile.forEach(item => {
          let data = {
            fileName:item.fileName,
            fileDownloadUri:item.fileDownloadUri,
            fileType:item.fileType,
            size:item.size,
          }
          that.tableList.push(data)
        })
        console.log(that.tableList)

        that.ruleForm.attachment = that.tableList
        if (that.title === '添加' || that.title === '增加子栏目') {
          that.add(that.ruleForm)
          return false
        } else if (this.title === '编辑') {
          that.ruleForm.id = that.nowId
          that.edit(that.ruleForm)
          return false
        }
      })
    },

    // 添加方法
    async add(data) {
      const that = this

      that.loading = true
      const response = await insert(data)
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

    // 修改方法
    async edit(data) {
      const that = this
      // PUT 请求修改数据
      that.loading = true
      const response = await update(data)
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
      let tmp = []
      fileList.forEach(item => {
        let data  = {
          fileName:fileList[0].response.fileName,
          fileDownloadUri:fileList[0].response.fileDownloadUri,
          fileType:fileList[0].response.fileType,
          size:fileList[0].response.size,
        }

        tmp.push(data)
      })

      this.uploadFile = tmp
    },
    herror(error, file, fileList){
      const result = JSON.parse(error.message);
      this.$message.error('上传失败:' + result.msg)
    },
    handlePreview (file) {
      console.log(file)
    },

    beforeRemove (file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    async handleRemove(file, fileList){

    },

    async getTemplates() {
      const that = this
      // PUT 请求修改数据
      that.loading = true
      const response = await templateFindAll()
      that.loading = false
      if (response.data.code === 1) {
        that.templates = response.data.data
      } else {
        that.$message.error(response.data.msg)
      }
    },

    addPicture(){
      this.$refs.repositoryLib.init()
    },
    sure(data){
      console.log('sure data')
      console.log(data)
      data.forEach((item,i) => {
        this.tableList.forEach((item1,j) => {
          console.log('item.id')
          console.log(item.id)
          console.log('item1.id')
          console.log(item1.id)
          if(item.id === item1.id) {
            this.tableList = this.tableList.splice(j,1)
          }
        })
      })

      data.concat(this.tableList)
      this.tableList = data
      console.log('this.tableList')
      console.log(this.tableList)
    },
    //图片操作
    deleteRow(row){
      const that = this

      let data = that.tableList
      data.forEach((item,i) => {
        if(item.id === row.id){
          that.tableList.splice(i,1)
          return true
        }
      })
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
