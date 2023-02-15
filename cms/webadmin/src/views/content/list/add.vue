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
    <el-scrollbar>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
        <el-form-item
          prop="name"
          label="文章标题"
          :label-width="formLabelWidth"
          class='InpitWidth'>
          <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.title" placeholder="请输入文章标题"></el-input>
        </el-form-item>
        <el-form-item
          prop="power"
          label="置顶"
          :label-width="formLabelWidth"
          class='InpitWidth'>
          <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model.number="ruleForm.power" placeholder="请输入置顶值"></el-input>
        </el-form-item>

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
      <div class="demo-drawer__footer drawer_footer" v-if="dataFields !== []" style="margin-bottom: 45px">
        <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
      </div>
      <div v-else style="margin-bottom: 45px">
        <span>请确认模板是否存在，模板异常！</span>
      </div>
      <RepositoryLib ref="repositoryLib" @sure="sure"></RepositoryLib>
    </el-scrollbar>
  </el-drawer>
</template>

<script>

import { findById,insert,update,fieldFindAll } from '@/api/content'
import { isMPStrict } from '@/utils/validate'
import { getToken } from '@/utils/auth'
import Template from '../../template/template';
import Editor from '@/components/editor/wangEditor.vue';
import SingleUpload from '@/views/content/component/singleUpload.vue';
import MultiUpload from '@/views/content/component/multiUpload.vue';
import RepositoryLib from '@/views/content/list/repository/index.vue';
import VideoUpload from '@/views/content/component/videoUpload.vue';
export default {
  components: {
    MultiUpload,
    SingleUpload,
    Template,
    Editor,
    RepositoryLib,
    VideoUpload
  },

  props: {
    title: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      contentValues:{},
      contentTypes:{},
      content: {
        values:{},
        types:{}
      },
      ruleForm: {
        id:'',
        title:'',
        columnId:'',
        content:'',
        type:'0',
        power: 0
      },
      rules: {
        title: [{
          required: true,
          message: '请输入文章标题',
          trigger: ['change', 'blur']
        }],
        power:[{
          type: 'number',
          min: 0,
          message: '请输入数字,置顶值不能小于0',
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
      fields:[],
      isAdd: true,
      selectOptions:{},

      fileList:[],
      isShow:false,
      resourceLibFile:{},
      action: process.env.VUE_APP_BASE_API  + '/service/file/content/pictureUpload?file=',
      videoUpload: process.env.VUE_APP_BASE_API  + '/service/file/content/videoUpload?file='
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
    async initial (title,templateId,columnId,id) {
      const that = this
      await that.fieldFindAll(templateId)
      that.ruleForm.columnId = columnId
      that.dialogFormVisible = true
      that.title = title
      if (id){
        that.nowId = id
        that.initFormDetail(id)
        that.isAdd = false
      } else {
        that.ProhibitFrom = false
        that.isAdd = true
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
          //字符串转json
          this.getContent(response.data.data.content)
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
    async ClearTextConeten () {
      const that = this
      that.$refs['ruleForm'].resetFields()
      that.ruleForm.title = ''
      that.ruleForm.columnId = ''
      that.ruleForm.content = ''
      that.ruleForm.type = ''
      that.dialogFormVisible = false
      if (that.ruleForm.id) {
        delete that.ruleForm.id
      }

      that.contentValues={}
      that.contentTypes={}
      that.content.values = {}
      that.content.types = {}
      that.fields = []
      that.selectOptions = {}
    },

    async SuretoAddClick (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }

        if(!that.setContent()){
          return false
        }

        Object.entries(that.ruleForm).map((a, b) => {
          if (a[0] === 'id') {
            delete that.ruleForm[a[0]]
          }
        })
        if (that.title === '添加') {
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

    async fieldFindAll(templateId) {
      const that = this
      let params = {
        templateId: templateId
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



  /*1.显示滚动条：当内容超出容器的时候，可以拖动：*/
  .el-drawer__body {
    overflow: auto;
    /* overflow-x: auto; */
  }

  /*2.隐藏滚动条，太丑了*/
  .el-drawer__container ::-webkit-scrollbar{
    display: none;
  }

</style>
