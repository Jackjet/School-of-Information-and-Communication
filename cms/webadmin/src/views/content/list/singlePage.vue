<template>
  <el-main>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <input type="hidden" v-model="hidden">
      <el-form-item v-for="(value,i) in dataFields"
                    :label="value.fieldName"
                    :label-width="formLabelWidth"
                    class='InpitWidth'
                    :key="value">
        <template v-if="value.dataType === '单文本'">
          <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="contentValues[value.field]" :placeholder="'请输入' + value.fieldName" @input="change"></el-input>
        </template>
        <template v-if="value.dataType === '多文本'">
          <el-input type="textarea" v-model="contentValues[value.field]" :placeholder="'请输入' + value.fieldName" @input="change"></el-input>
        </template>
        <template v-if="value.dataType === '富文本'">
          <Editor  @monitor="monitor(value.field)" :ref="value.field" @input="change"></Editor>
        </template>
        <template v-if="value.dataType === '下拉框'">
          <el-select v-model="contentValues[value.field]" :placeholder="'请输入' + value.fieldName" @input="change">
            <el-option v-for="item in selectOptions[value.field]" :label="item" :value="item"></el-option>
          </el-select>
        </template>
        <template v-if="value.dataType === '单张图'">
          <SingleUpload :ref="value.field" :action="action" :uploadRef="value.field" @uploadClick="setFileData" :fieldName="value.fieldName"></SingleUpload>
          <el-button size="small" @click.native="addPicture(value.field,true,value.fieldName)" type="primary">资源库上传</el-button>
          <template>
            <el-table
              :data="contentValues[value.field]"
              style="width: 100%">
              <el-table-column
                prop="fileName"
                label="文件名"
                width="180">
              </el-table-column>
              <el-table-column
                label="操作">
                <template slot-scope="scope">
                  <el-button type="text" @click="deleteRow(value.field,scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </template>
        <template v-if="value.dataType === '多张图'">
          <MultiUpload :ref="value.field" :action="action" :uploadRef="value.field" @uploadClick="setFileData"></MultiUpload>
          <el-button size="small" @click.native="addPicture(value.field,false,value.fieldName)" type="primary">资源库上传</el-button>
          <template>
            <el-table
              :data="contentValues[value.field]"
              style="width: 100%">
              <el-table-column
                prop="fileName"
                label="文件名"
                width="180">
              </el-table-column>
              <el-table-column
                label="操作">
                <template slot-scope="scope">
                  <el-button type="text" @click="deleteRow(value.field,scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </template>
        <template v-if="value.dataType === '多媒体'">
          <VideoUpload :ref="value.field" :action="videoUpload" :uploadRef="value.field" @uploadClick="setFileData"></VideoUpload>
          <template>
            <el-table
              :data="contentValues[value.field]"
              style="width: 100%">
              <el-table-column
                prop="fileName"
                label="文件名"
                width="180">
              </el-table-column>
              <el-table-column
                label="操作">
                <template slot-scope="scope">
                  <el-button type="text" @click="deleteRow(value.field,scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </template>
      </el-form-item>

    </el-form>
    <div class="demo-drawer__footer drawer_footer" v-if="dataFields !== []">
      <el-button v-if='!ProhibitFrom' type="primary" @click="save('ruleForm')">确 定</el-button>
    </div>
    <div v-else>
      <span>请确认模板是否存在，模板异常！</span>
    </div>
    <RepositoryLib ref="repositoryLib" @sure="sure"></RepositoryLib>
  </el-main>
</template>

<script>

import { findById,insert,update,fieldFindAll,findAll } from '@/api/content'
import Template from '../../template/template';
import Editor from '@/components/editor/wangEditor.vue';
import SingleUpload from '@/views/content/component/singleUpload.vue';
import MultiUpload from '@/views/content/component/multiUpload.vue';
import TableList from '@/components/table/TableList.vue'
import RepositoryLib from '@/views/content/list/repository/index.vue'
import {uploadFile} from '../../../api/templateFile';
import VideoUpload from '@/views/content/component/videoUpload.vue';
export default {
  components: {
    Template,
    // 注册组件
    // editor: editor
    Editor,
    MultiUpload,
    SingleUpload,
    TableList,
    RepositoryLib,
    VideoUpload
  },

  watch:{
    async columnId (newValue ,oldValue){
      oldValue = newValue
      await this.initial()
    },
    async templateId (newValue ,oldValue){
      oldValue = newValue
      await this.initial()
    },
    async isList (newValue ,oldValue){
      oldValue = newValue
      await this.initial()
    }
  },
  props:{
    columnId: String,
    templateId: String,
    isList: Boolean
  },

  data () {
    return {
      formLabelWidth:'20%',
      columnId: '',
      templateId: '',
      isList: false,
      dataFields:[],
      isAdd:true,
      ruleForm:{
        id:'',
        title:'',
        columnId:'',
        content:'',
        type:'1'
      },
      contentValues:{},
      contentTypes:{},
      selectOptions:{},
      action: process.env.VUE_APP_BASE_API  + '/service/file/content/pictureUpload?file=',
      videoUpload: process.env.VUE_APP_BASE_API  + '/service/file/content/videoUpload?file=',
      hidden: ''
    }
  },
  async created() {
    const that = this
    await that.initial()
    that.hidden = 'hidden'
  },
  methods: {
    //初始化
    async initial () {
      const that = this
      //获取数据模型
      await that.fieldFindAll()
      //获取数据
      let params = {
        columnId: that.columnId
      }
      let response = await findAll(params);
      if (response.data.code === 1) {
        let data = response.data.data.content
        if (data.length > 0) {
          that.ruleForm.id = data[0].id
          that.ruleForm.title = data[0].title
          that.ruleForm.columnId = data[0].columnId
          that.ruleForm.content = data[0].content
          that.isAdd = false

          await that.getContent(data[0].content)
        } else {
          that.ruleForm.columnId = that.columnId
        }
      } else {
        that.$message.error(response.data.msg)
      }
    },
    async fieldFindAll(){
      const that = this
      let params = {
        templateId: that.templateId
      }

      let response = await fieldFindAll(params)

      if (response.data.code === 1) {
        that.dataFields = response.data.data

        that.dataFields.forEach(item => {
          that.contentTypes[item.field] = item.dataType
          if(item.dataType === '下拉框'){
            let options = item.defaultValue.split(',')
            that.selectOptions[item.field] = options
          }

          if(item.dataType === '单张图' || item.dataType === '多张图' || item.dataType === '多媒体'){
            that.contentValues[item.field] = []
          }
        })

        console.log('that.contentTypes')
        console.log(that.contentTypes)
      } else {
        that.$message.error(response.data.msg)
      }
    },

    getContent(content) {
      const that = this
      let json = JSON.parse(content)

      that.dataFields.forEach(item => {
        let value = json.values[item.field]
        let type = json.types[item.field]
        if(value !== undefined && value !== ''){
          that.contentValues[item.field] = value
        }
        if(type !== undefined && type !== ''){
          that.contentTypes[item.field] = type
        }

        if(item.dataType === '富文本'){
          that.$refs[item.field][0].setContent(that.contentValues[item.field])
        }
      })
    },


    //图片操作
    deleteRow(field,row){
      const that = this
      let data = that.contentValues[field]
      data.forEach((item,i) => {
        if(item.name === row.name){
          data.splice(i,1)
          return true
        }
      })
    },

    //富文本
    monitor(ref) {
      const that = this
      that.contentValues[ref] = that.$refs[ref][0].htmlContent().html
    },


    save(ruleForm){
      const that = this
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }

        that.doSave()
      })
    },
    async doSave(){
      const that = this
      that.uploadFile()

      if(!that.setContent()){
        return false
      }
      if(that.isAdd){
        await that.add()
      } else {
        await that.edit()
      }
    },

    async add(){
      const that = this
      let response = await insert(that.ruleForm)
      if (response.data.code === 1) {
        that.$message.success(response.data.msg)
        that.initial()
      } else {
        that.$message.error(response.data.msg)
      }
    },
    async edit(){
      const that = this
      let response = await update(that.ruleForm)
      if (response.data.code === 1) {
        that.$message.success(response.data.msg)
        that.initial()
      } else {
        that.$message.error(response.data.msg)
      }
    },
    //上传图片
    uploadFile(){
      const that = this
      that.dataFields.forEach(item => {
        if(item.dataType === '单张图' || item.dataType === '多张图' || item.dataType === '多媒体') {
          that.$refs[item.field][0]['$refs'][item.field].submit()
        }
      })
    },
    //保存文件信息
    setFileData(field,file,isSingle,fieldName){
      console.log('setFileData')
      const that = this

      let temp = that.contentValues[field]
      if(temp === undefined || temp === null) {
        temp = []
      }

      if(isSingle && temp.length > 0) {
        that.$message.error(fieldName + '只能上传一张图片')
        return false
      }
      temp.push(file)
      that.contentValues[field]=temp

      console.log(that.contentValues)
    },
    //content数据转json字符串
    setContent(){
      const that = this

      let dataFields = that.dataFields
      for(var i = 0 ;i< dataFields.length; i++ ){
        //校验非空
        if(dataFields[i].isNull === 'NO' && (that.contentValues[dataFields[i].field] === '' || that.contentValues[dataFields[i].field] === null || that.contentValues[dataFields[i].field] === undefined)) {
          that.$message.error(dataFields[i].fieldName + '不能为空');
          return false
          break
        }
      }

      let content = {
        values: {},
        types: {}
      }
      content.values = that.contentValues
      content.types = that.contentTypes
      that.ruleForm.content = JSON.stringify(content)
      return true
    },
    addPicture(field,isSingle,fieldName){
      this.$refs.repositoryLib.init(field,isSingle,fieldName)
    },
    sure(field,data,fieldName,isSingle){
      const that = this
      let temp = that.contentValues[field]

      console.log('checkData')
      console.log(data)
      if(isSingle && temp.length > 0) {
        that.$message.error(fieldName + '只能上传一张图片')
      } else {
        data.forEach(item => {
          temp.push(item)
        })

        that.contentValues[field] = temp
      }
    },
    change() {
      this.$forceUpdate()  // 强制刷新
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
