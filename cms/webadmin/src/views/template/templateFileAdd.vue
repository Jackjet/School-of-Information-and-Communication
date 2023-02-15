<template>
  <el-drawer
    :title="title"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextContent"
    custom-class="demo-drawer"
    size='60%'
    :wrapperClosable='false'
    ref="drawer">
    <el-scrollbar>
      <el-form :model="templateFile" ref="templateFile"  v-loading='loading' style="margin-bottom: 45px">
        <el-form-item
          :rules="rules.name"
          prop="name"
          label="名称"
          :label-width="this.formLabelWidth"
          class='InputWidth'>
          <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="templateFile.name" placeholder="请输入名称" style="width: 300px;display: inline-block"></el-input>
          <span v-if="templateFile.type === '0'">.html</span>
        </el-form-item>
        <el-form-item
          :rules="rules.type"
          prop="type"
          label="类型"
          :label-width="this.formLabelWidth"
          class='InputWidth'
        >
          <el-select v-model="templateFile.type" placeholder="请选择" :disabled="insertNone">
            <el-option
              v-for="item in selectValue"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="templateFile.type === '0' && inputUrl !== '' && inputUrl !== null"
          prop="name"
          label="文件目录"
          :label-width="this.formLabelWidth"
          class='InputWidth'>
          <el-input clearable :disabled="true" style="width: 300px;display: inline-block" v-model="inputUrl"></el-input>
        </el-form-item>
        <el-form-item
          v-if="templateFile.type === '0'"
          :rules="rules.content"
          prop="content"
          label="内容"
          :label-width="this.formLabelWidth"
          class='InputWidth'>
          <codemirror ref="cmRef" v-model="templateFile.content" :options="cmOptions" style="width: 110%; border: 2px solid #f2f6fc;border-radius: 2px"></codemirror>
        </el-form-item>
      </el-form>
    </el-scrollbar>
    <br>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SureToAddClick('templateFile')">确 定</el-button>
    </div>
    <br>
  </el-drawer>
</template>

<script>
import {content, insert, update} from '@/api/templateFile'
import { getToken } from '@/utils/auth'
import { codemirror } from 'vue-codemirror'
import 'codemirror/theme/idea.css'
import 'codemirror/mode/htmlmixed/htmlmixed'
import 'codemirror/mode/sql/sql.js'
import 'codemirror/mode/javascript/javascript'
import 'codemirror/mode/vue/vue'
import 'codemirror/mode/xml/xml'
import 'codemirror/addon/hint/show-hint.css'
import 'codemirror/addon/hint/show-hint.js'
import 'codemirror/addon/hint/html-hint'
import 'codemirror/addon/hint/javascript-hint'
import 'codemirror/addon/scroll/simplescrollbars.css'
import 'codemirror/addon/scroll/simplescrollbars'
import 'codemirror/addon/scroll/annotatescrollbar.js'
import 'codemirror/addon/search/matchesonscrollbar.js'
import 'codemirror/addon/search/match-highlighter.js'
import 'codemirror/addon/search/jump-to-line.js'
import 'codemirror/addon/dialog/dialog.js'
import 'codemirror/addon/dialog/dialog.css'
import 'codemirror/addon/search/searchcursor.js'
import 'codemirror/addon/search/search.js'

export default {
  components: {
    codemirror
  },

  props: {
    title: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      selectValue:[
        {
          value: '1',
          label: '文件夹'
        },
        {
          value: '0',
          label: '文件'
        }
      ],
      templateFile: {
        name:'',
        type: '',
        content: ''
      },
      insertContent:{
        name:'',
        url: '',
        content: []
      },
      rules: {
        name: [{
          required: true,
          message: '请输入名称',
          trigger: ['change', 'blur']
        }],
        type: [{
          required: true,
          message: '请选择类型',
          trigger: 'change'
        }],
        content: [{
          required: true,
          message: '请输入内容',
          trigger: ['change', 'blur']
        }]
      },
      headers:{
        Authorization : 'token ' + getToken(),
      },
      // 是否触发禁用表单项
      ProhibitFrom: false,
      ProhibitFromm: false,
      insertNone: false,
      dialogFormVisible: this.show,
      formLabelWidth: '120px',
      lock: false,
      dialogVisible: true,
      loading: false,
      parentUrl:'',
      contentData:{
        url:'',
        name:''
      },
      inputUrl:'',
      cmOptions:{
        mode: 'text/html',
        indentWithTabs: true,
        smartIndent: true,
        lineNumbers: true,
        matchBrackets: true,
        styleActiveLine: true,
        cursorHeight:1,
        autoRefresh: true,
        value:'',
        theme: 'idea',
        scrollbarStyle: 'simple'
      }
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
     * @param title 根据不同状态 展示不同操作
     * @param fileName 文件名
     */
    initial: function (title,value,fileName) {
      const that = this
      that.dialogFormVisible = true
      that.title = title
      this.parentUrl = value
      this.inputUrl = value.substr(1)
      if (title && fileName) {
        that.title = title
        that.initFormDetail(value,fileName)
      } else {
        that.insertNone = false
        that.title = title
        that.ProhibitFrom = false
      }
    },
    /**
     * 初始化获取当前需要编辑 查看的详细数据
     * @param url 文件地址
     * @param fileName 文件名
     */
    async initFormDetail (url,fileName) {
      const that = this

      try {
        this.contentData.url = url
        this.contentData.name = fileName
        that.loading = true
        const response = await content(this.contentData)
        that.loading = false
        let code = response.data.code
        if (code === 1) {
          // 查询成功后 回调效果
          let resultContent = response.data.data
          let relName = resultContent.fileName
          relName = relName.substr(0,relName.lastIndexOf('.'))
          this.templateFile.name = relName
          this.templateFile.type = '0'
          this.parentUrl = resultContent.url
          let listContent = resultContent.content
          let relContent = ''
          listContent.forEach((item) => {
            relContent = relContent + item + '\r\n'
          })
          this.templateFile.content = relContent
        } else {
          // 查询失败后 回调失败信息
          that.dialogFormVisible = false
          that.$message.error(response.data.msg)
          return false
        }
        if (this.title === '详情') {
          that.ProhibitFrom = true
          that.insertNone = true
          // 禁用编辑器
        } else if (this.title === '编辑') {
          that.ProhibitFrom = false
          that.ProhibitFromm = true
          that.insertNone = true
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
      that.ClearTextContent()
      that.ProhibitFrom = false
    },

    // 初始化表单
    ClearTextContent () {
      const that = this
      that.$refs['templateFile'].resetFields()
      that.templateFile.name = ''
      that.templateFile.type = ''
      that.templateFile.content = ''
      that.ProhibitFrom = false
      that.ProhibitFromm = false
      that.dialogFormVisible = false
    },

    SureToAddClick (templateFile) {
      const that = this
      that.$refs[templateFile].validate((index) => {
        if (index === false) {
          return false
        }
        that.insertContent.name = that.templateFile.name
        that.insertContent.url = this.parentUrl
        let content = that.templateFile.content
        let type = that.templateFile.type
        if (type ==='0') {
          let code = content.split(/[(\r\n)\r\n]+/);
          code.forEach((item) => {
            if (!item) {
              code.splice(index, 1);
            }
          })
          code = Array.from(new Set(code))
          that.insertContent.content = code
        }
        if (this.title === '添加') {
          this.add(that.insertContent)
          return false
        } else if (this.title === '编辑') {
          this.edit(that.insertContent)
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
        that.$message.success('添加成功')
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextContent()
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
        that.$message.success('编辑成功')
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextContent()
        // 重新触发父组件加载loading
        that.$emit('Reload')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
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
    .InputWidth {
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

  .el-drawer__body{
    overflow: auto;
  }

  .el-drawer__container ::-webkit-scrollbar{
    display: none;
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
