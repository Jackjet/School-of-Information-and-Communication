<template>
  <el-drawer
    :title="title"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextContent"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer">
    <el-scrollbar  class="el-scrollbar-01">
      <el-form :model="templateFile" ref="templateFile"  v-loading='loading' style="margin-bottom: 45px">
        <el-form-item
          :rules="rules.type"
          prop="type"
          label="模板数据类型"
          :label-width="this.formLabelWidth"
          class='InputWidth'>
          <el-select v-model="templateFile.type" placeholder="请选择">
            <el-option
              v-for="item in selectValue"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          :rules="rules.field"
          prop="field"
          label="字段"
          :label-width="this.formLabelWidth"
          class='InputWidth'>
          <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="templateFile.field" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item
          :rules="rules.name"
          prop="fieldName"
          label="字段名称"
          :label-width="this.formLabelWidth"
          class='InputWidth'>
          <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="templateFile.fieldName" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item
          prop="dataType"
          label="数据类型"
          :label-width="this.formLabelWidth">
          <el-radio v-model="templateFile.dataType" label="单文本">单文本</el-radio>
          <el-radio v-model="templateFile.dataType" label="多文本">多文本</el-radio>
          <el-radio v-model="templateFile.dataType" label="富文本">富文本</el-radio><br/>
          <el-radio v-model="templateFile.dataType" label="下拉框">下拉框</el-radio>
          <el-radio v-model="templateFile.dataType" label="单张图">单张图</el-radio>
          <el-radio v-model="templateFile.dataType" label="多张图">多张图</el-radio>
          <el-radio v-model="templateFile.dataType" label="多媒体">多媒体</el-radio>
        </el-form-item>
        <el-form-item
          prop="defaultValue"
          label="默认值"
          :label-width="this.formLabelWidth"
          class='InputWidth'>
          <el-input
            type="textarea"
            :rows="4"
            clearable
            :disabled='ProhibitFromm'
            autocomplete="off"
            v-model="templateFile.defaultValue"
            placeholder="如果定义字段类型为下拉框，此处填写被选择的项目（用','分开，如'男,女'）"></el-input>
        </el-form-item>
        <el-form-item
          :rules="rules.type"
          prop="isNull"
          label="是否允许为空"
          :label-width="this.formLabelWidth"
          class='InputWidth'>
          <el-select v-model="templateFile.isNull" placeholder="请选择">
            <el-option
              v-for="item in selectValue2"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
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

import {findDataById, insertData, updateData} from '@/api/template'
import { getToken } from '@/utils/auth'
import {sessionStorageGet, sessionStorageSet} from '@/utils/storage';
export default {
  'components': {
    // 注册组件
    // editor: editor
  },

  'props': {
    'title': {
      'type': String,
      'default': () => {}
    }
  },

  'data'() {
    return {
      'selectValue':[
        {
          'value': '1',
          'label': '文档数据模型'
        },
        {
          'value': '0',
          'label': '列表数据模型'
        }
      ],
      'selectValue2':[
        {
          'value': 'NO',
          'label': 'NO'
        },
        {
          'value': 'YES',
          'label': 'YES'
        }
      ],
      'templateFile': {
        'id': '',
        'field':'',
        'type': '',
        'dataType': '单文本',
        'fieldName': '',
        'defaultValue': '',
        'isNull': '',
        'templateId': ''
      },
      'rules': {
        'name': [{
          'required': true,
          'message': '请输入字段名称',
          'trigger': ['change', 'blur']
        }],
        'type': [{
          'required': true,
          'message': '请选择类型',
          'trigger': 'change'
        }],
        'field': [{
          'required': true,
          'message': '请输入字段',
          'trigger': ['change', 'blur']
        }]
      },
      'headers':{
        'Authorization': 'token ' + getToken(),
      },
      // 是否触发禁用表单项
      'ProhibitFrom': false,
      'ProhibitFromm': false,
      'dialogFormVisible': this.show,
      'formLabelWidth': '120px',
      'lock': false,
      'dialogVisible': true,
      'loading': false,
      'searchType':''
    }
  },
  'watch': {
    'show'() { this.dialogFormVisible = this.show }
  },
  'created'() {

  },
  'methods': {
    /**
     * @param templateId 模板ID
     * @param title 根据不同状态 展示不同操作
     * @param rowId 数据ID
     * @param type 1 文档 0 列表 模板数据类型
     */
    'initial': function (title,templateId,rowId,type) {
      const that = this
      that.dialogFormVisible = true
      that.title = title
      this.searchType = type
      this.templateFile.templateId = templateId
      if (title && rowId) {
        that.title = title
        that.initFormDetail(rowId)
      } else {
        that.title = title
        that.ProhibitFrom = false
      }
    },
    /**
     * 初始化获取当前需要编辑 查看的详细数据
     * @param dataId 数据ID
     */
    async 'initFormDetail'(dataId) {
      const that = this
      try {
        that.loading = true
        console.log(this.templateFile.templateId)
        if (this.templateFile.templateId === '' || this.templateFile.templateId === null ){
          let dataField = []
          if (this.searchType === '1'){
            dataField = JSON.parse(localStorage.getItem('dataField01'));
          }else {
            dataField = JSON.parse(localStorage.getItem('dataField02'));
          }
          for (let dataFieldElement of dataField) {
            let id = dataFieldElement.id
            if (id === dataId){
              this.templateFile = dataFieldElement
            }
          }
          that.loading = false
        }else {
          let params = {
            'id': dataId
          }
          const response = await findDataById(params)
          that.loading = false
          let code = response.data.code
          if (code === 1) {
            // 查询成功后 回调效果
            this.templateFile = response.data.data
          } else {
            // 查询失败后 回调失败信息
            that.dialogFormVisible = false
            that.$message.error(response.data.msg)
            return false
          }
        }
        if (this.title === '详情') {
          that.ProhibitFrom = true
          // 禁用编辑器
        } else if (this.title === '编辑') {
          that.ProhibitFrom = false
          that.ProhibitFromm = false
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
    'CloseModal'() {
      const that = this
      that.ClearTextContent()
      that.ProhibitFrom = false
    },

    // 初始化表单
    'ClearTextContent'() {
      const that = this
      that.$refs['templateFile'].resetFields()
      that.templateFile.name = ''
      that.templateFile.type = ''
      that.templateFile.content = ''
      that.ProhibitFrom = false
      that.ProhibitFromm = false
      that.dialogFormVisible = false
    },

    'SureToAddClick'(templateFile) {
      const that = this
      that.$refs[templateFile].validate((index) => {
        if (index === false) {
          return false
        }
        if (this.templateFile.templateId === ''){
          if (this.title === '添加') {
            let dataField = []
            if (this.templateFile.type === '1'){
              dataField = JSON.parse(localStorage.getItem('dataField01'));
            }else {
              dataField = JSON.parse(localStorage.getItem('dataField02'));
            }
            that.templateFile.id = this.guid()
            if (dataField === null || dataField === ''){
              dataField = []
              dataField.push(that.templateFile)
            }else {
              for (let dl of dataField) {
                let dataField = dl.field
                let tempField = that.templateFile.field
                if (dataField === tempField){
                  that.$message.error('字段'+dl.field+'已存在')
                  return false
                }
              }
              dataField.push(that.templateFile)
            }
            if (this.templateFile.type === '1'){
              localStorage.setItem('dataField01',JSON.stringify(dataField))
            }else {
              localStorage.setItem('dataField02',JSON.stringify(dataField))
            }
            that.$message.success('添加成功')
            that.ClearTextContent()
            that.$emit('Reload')
          }else if (this.title === '编辑'){
            let dataField = []
            if (this.templateFile.type === '1'){
              dataField = JSON.parse(localStorage.getItem('dataField01'));
            }else {
              dataField = JSON.parse(localStorage.getItem('dataField02'));
            }
            for (let dataFieldElement of dataField) {
              let id = dataField.id
              if (id === that.templateFile.id) {
                dataField = that.templateFile
              }
            }
            dataField.forEach((item,index) => {
              if (item && item.id === that.templateFile.id) {
                dataField.splice(index, 1,that.templateFile);
              }
            })
            console.log(dataField)
            if (this.templateFile.type === '1'){
              localStorage.setItem('dataField01',JSON.stringify(dataField))
            }else {
              localStorage.setItem('dataField02',JSON.stringify(dataField))
            }
            that.$message.success('修改成功')
            that.ClearTextContent()
            that.$emit('Reload')
          }
        }else {
          if (this.title === '添加') {
            this.add(that.templateFile)
            return false
          } else if (this.title === '编辑') {
            this.edit(that.templateFile)
            return false
          }
        }
      })
    },

    // 添加方法
    async 'add'(data) {
      const that = this
      that.loading = true
      const response = await insertData(data)
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
    async 'edit'(data) {
      const that = this
      // PUT 请求修改数据
      that.loading = true
      const response = await updateData(data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
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

    'guid'() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      })
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
