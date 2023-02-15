<template>
  <el-main :v-loading="load">
    <el-col :span="24">
      <el-form :model="templateInfo" ref="templateInfo"  v-loading='loading'>
        <el-form-item
          :rules="rules.name"
          prop="name"
          label="模板名称"
          :label-width="this.formLabelWidth"
          class='InputWidth'>
          <el-input
            clearable
            :disabled='ProhibitFrom'
            autocomplete="off"
            v-model="templateInfo.name"
            placeholder="请输入名称"
            style="width: 300px"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="模板文件"
          :label-width="this.formLabelWidth"
          class='InputWidth'><br>
          <div>
            <div style="width: 400px;display: inline-block">
              <span>PC端</span><br>
              <el-checkbox class="checkbox-listData" :true-label="'1'" :false-label="'0'" v-model="templateInfo.isPcList" @change="selectFile">支持列表</el-checkbox>
              <el-checkbox class="checkbox-listData" :true-label="'1'" :false-label="'0'" v-model="templateInfo.isPcDocument" @change="selectFile">支持文档</el-checkbox>
            </div>
            <div style="width: 400px;display: inline-block">
              <span>APP端</span><br>
              <el-checkbox class="checkbox-listData" :true-label="'1'" :false-label="'0'" v-model="templateInfo.isAppList" @change="selectFile">支持列表</el-checkbox>
              <el-checkbox class="checkbox-listData" :true-label="'1'" :false-label="'0'" v-model="templateInfo.isAppDocument" @change="selectFile">支持文档</el-checkbox>
            </div>
          </div>
          <el-form-item
            label="PC端列表模板:"
            v-if="templateInfo.isPcList === '1'"
            style="margin-bottom: 5px">
            <el-button
              @click="getFileUrl01"
              style="width: 300px;height: 40px;text-align: left;margin-left: 9.5px"
            >{{pcListUrl}}</el-button>
          </el-form-item>
          <el-form-item
            label="PC端文档模板:" v-if="templateInfo.isPcDocument === '1'"
            style="margin-bottom: 5px">
            <el-button
              @click="getFileUrl02"
              style="width: 300px;height: 40px;text-align: left;margin-left: 9.5px"
            >{{pcDocumentUrl}}</el-button>
          </el-form-item>
          <el-form-item
            label="APP端列表模板:" v-if="templateInfo.isAppList === '1'"
            style="margin-bottom: 5px">
            <el-button
              @click="getFileUrl03"
              style="width: 300px;height: 40px;text-align: left"
            >{{appListUrl}}</el-button>
          </el-form-item>
          <el-form-item
            label="APP端文档模板:" v-if="templateInfo.isAppDocument === '1'"
            style="margin-bottom: 5px">
            <el-button
              @click="getFileUrl04"
              style="width: 300px;height: 40px;text-align: left"
            >{{appDocumentUrl}}</el-button>
          </el-form-item>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <hr>
      <p>数据模型<el-popover
        placement="top-start"
        width="230"
        title="提示:"
        trigger="hover">
        <img
          slot="reference"
          src="@/icons/image/explain.png"
          height="15px"
          width="15px"
          style="position: relative;left:5px;top:-4px;">
        <slot>模板生效需配置相应的数据模型
        </slot>
      </el-popover></p>

      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="add" icon="el-icon-plus">新增</el-button>
        </el-form-item>
        <el-form-item ref="uploadFile">
          <el-upload
            class="upload-demo"
            :auto-upload="true"
            :action="action"
            :headers='header'
            :show-file-list="false"
            :before-upload='handleContentChange'
            :on-success="handleSuccess"
            :on-error="hError"
            :file-list="fileList">
          <el-button type="primary"  size='medium' @click="fetchData" icon="el-icon-upload">导入</el-button>
          <el-popover
            placement="top-start"
            title="JSON示例:"
            width="200"
            trigger="hover">
            <img
              slot="reference"
              src="@/icons/image/explain.png"
              height="15px"
              width="15px"
              style="position: relative;left:10px;top:-10px;">
            <slot>
              [{<br/>
              "id": "1",<br/>
              "dataType": "单文本",<br/>
              "defaultValue": "新闻",<br/>
              "field": "name",<br/>
              "fieldName": "名称",<br/>
              "isNull": "NO",<br/>
              "templateId": "1",<br/>
              "type": "0"},<br/>
              { "id": "2",<br/>
              "ype"": "单文本",<br/>
              "defaultValue": "100",<br/>
              "field": "attention",<br/>
              "fieldName": "关注度",<br/>
              "isNull": "NO",<br/>
              "templateId": "1",<br/>
              "type": "0" <br/>
              }]
            </slot>
          </el-popover>
          </el-upload>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <el-tabs v-model="SearchItem.type" type="card" @tab-click="handleClick">
        <el-tab-pane label="列表数据模型" name="0"></el-tab-pane>
        <el-tab-pane label="文档数据模型" name="1"></el-tab-pane>
      </el-tabs>
      <TableList :table-data='tableData' v-loading="isSubmitLoading"
                 :table-label="tableHeader"
                 :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24' v-if="parentId === '2'">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <div>
      <el-button @click="backToPage">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SureToAddClick">确 定</el-button>
    </div>
    <Add ref="add" @Reload='fetchTableData'></Add>
    <AddFile ref="addFile" @Reload='initData'></AddFile>
  </el-main>
</template>

<script>
import {deleteData, findById, findData, insert, update} from '@/api/template'
import {getToken} from '@/utils/auth'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import Add from '@/views/template/dataFieldAdd.vue'
import AddFile from '@/views/template/templateAddFile.vue'

export default {
  components: { TableList, Pagination, Add ,AddFile},
  data() {
    return {
      ProhibitFrom: false,
      ProhibitFrom01:false,
      tableData: [],
      isIndeterminatePc: true,
      isIndeterminateApp: true,
      tabsValue: '1',
      tableHeader: [
        {id: false, type: '', label: '字段', list: 'field'},
        {id: false, type: '', label: '字段名称', list: 'fieldName'},
        {id: false, type: '', label: '数据类型', list: 'dataType'},
        {id: false, type: '', label: '是否允许为空', list: 'isNull'},
        {id: false, type: '', label: '默认值', list: 'defaultValue'}
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '编辑',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.edit(row)
          }
        },{
          label: '删除',
          key: 0,
          type: 'danger',
          State: false,
          method: (row) => {
            this.deleteConfirm(row)
          }
        }]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        type: '0',
      },
      rules: {
        name: [{
          required: true,
          message: '请输入模板名称',
          trigger: ['change', 'blur']
        }],
        type: [{
          required: true,
          message: '请选择类型',
          trigger: 'change'
        }],
        field: [{
          required: true,
          message: '请输入字段',
          trigger: ['change', 'blur']
        }]
      },
      header: {
        'Authorization': 'token ' + getToken()
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      load: false,
      action: '',
      fileList: [],
      loading: '',
      templateInfo: {
        id : '',
        name : '',
        isPcList:'0',
        pcListUrl: '',
        isPcDocument: '0',
        pcDocumentUrl: '',
        isAppList: '0',
        appListUrl: '',
        isAppDocument: '0',
        appDocumentUrl: '',
        dataList:[]
      },
      pcListUrl :'',
      pcDocumentUrl: '',
      appListUrl: '',
      appDocumentUrl: '',
      parentId: '',
      typeId: ''
    }
  },
  mounted() {
    this.initData()
    this.show = true
    this.typeId = this.$route.query.typeId
    if (this.typeId === '2') {
      this.parentId =  this.$route.query.templateId
    }
    this.action = process.env.VUE_APP_BASE_API + '/webadmin/template/uploadJson?templateId='+this.parentId+'&file='
    this.fetchData()
  },
  methods: {

    initData(){
      let pcList = sessionStorage.getItem('isPcList')
      if (pcList !== null){
        this.templateInfo.pcListUrl = pcList
        this.pcListUrl = pcList.substr(pcList.lastIndexOf('/')+1)
      }
      let pcDocument = sessionStorage.getItem('isPcDocument')
      if (pcDocument !== null){
        this.templateInfo.pcDocumentUrl = pcDocument
        this.pcDocumentUrl = pcDocument.substr(pcDocument.lastIndexOf('/')+1)
      }
      let appList = sessionStorage.getItem('isAppList')
      if (appList !== null){
        this.templateInfo.appListUrl = appList
        this.appListUrl = appList.substr(appList.lastIndexOf('/')+1)
      }
      let appDocument = sessionStorage.getItem('isAppDocument')
      if (appDocument !== null){
        this.templateInfo.appDocumentUrl = appDocument
        this.appDocumentUrl = appDocument.substr(appDocument.lastIndexOf('/')+1)
      }
      sessionStorage.removeItem('isPcList')
      sessionStorage.removeItem('isPcDocument')
      sessionStorage.removeItem('isAppList')
      sessionStorage.removeItem('isAppDocument')
    },

    async fetchData() {
      const that = this
      that.load = true
      try {
        const data = {
          templateId: that.parentId,
          type: that.SearchItem.type,
          page: that.currentPage,
          size: that.pageSize
        }
        if (that.typeId === '2'){
          let res = await findData(data)
          that.tableData = res.data.data.content
          that.total = res.data.data.totalElements

          that.load = false
        } else if (that.typeId === '1'){
          let type = that.SearchItem.type
          //文档数据
          if (type === '1'){
            let dataField = JSON.parse(localStorage.getItem('dataField01'));
            if (dataField !==null){
              that.tableData = dataField
              that.templateInfo.dataList = dataField
            }
          } else {
            let dataField = JSON.parse(localStorage.getItem('dataField02'));
            if (dataField !==null){
              that.tableData = dataField
              that.templateInfo.dataList = dataField
            }
          }
          that.load = false
        }
        const data2 = {
          id: that.parentId,
        }
        let res2 = await findById(data2)
        let info = res2.data.data
        that.templateInfo = info
        that.pcListUrl = info.pcListUrl.substr(info.pcListUrl.lastIndexOf('/')+1)
        that.pcDocumentUrl = info.pcDocumentUrl.substr(info.pcDocumentUrl.lastIndexOf('/')+1)
        that.appListUrl = info.appListUrl.substr(info.appListUrl.lastIndexOf('/')+1)
        that.appDocumentUrl = info.appDocumentUrl.substr(info.appDocumentUrl.lastIndexOf('/')+1)
      } catch (even) {
        that.load = false
        that.$message.error(even.msg)
      }
    },

    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchTableData()
    },

    async fetchTableData() {
      this.load = true
      const data = {
        templateId: this.parentId,
        type: this.SearchItem.type,
        page: this.currentPage,
        size: this.pageSize
      }
      if (this.typeId === '2'){
        try {
          let res = await findData(data)
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
          this.load = false
        } catch (even) {
          this.load = false
          this.$message.error(even.msg)
        }
      } else if (this.typeId === '1'){
        let type = this.SearchItem.type
        //文档数据
        if (type === '1'){
          let dataField = JSON.parse(localStorage.getItem('dataField01'));
          if (dataField !==null){
            this.tableData = dataField
            this.templateInfo.dataList = dataField
          }
        }
        //列表数据
        else {
          let dataField = JSON.parse(localStorage.getItem('dataField02'));
          if (dataField !==null){
            this.tableData = dataField
            this.templateInfo.dataList = dataField
          }
        }
        this.load = false
      }
    },

    add() {
      this.$refs.add.initial('添加',this.parentId);
    },
    edit(row) {
      this.$refs.add.initial('编辑',this.parentId,row.id,this.SearchItem.type);
    },

    deleteConfirm(row){
      this.$confirm('确认删除？')
        .then(_ => {
          this.delete(row);
        })
        .catch(_ => {});
    },
    async delete(row){
      const that = this
      that.loading = true
      let id = row.id
      if (this.parentId === '' || this.parentId === null){
        let type = this.SearchItem.type
        let dataField = []
        if (type === '1'){
          dataField = JSON.parse(localStorage.getItem('dataField01'))
        }else {
          dataField = JSON.parse(localStorage.getItem('dataField02'))
        }
        dataField.forEach((item,index) => {
          if (item && item.id === id) {
            dataField.splice(index, 1);
          }
        })
        that.loading = false
        that.$message.success('删除成功')
        if (type === '1'){
          localStorage.setItem('dataField01',JSON.stringify(dataField))
        }else {
          localStorage.setItem('dataField02',JSON.stringify(dataField))
        }
        await that.fetchTableData()
      }else {
        const response = await deleteData({id:id});
        that.loading = false
        if (response.data.code === 1) {
          // 删除成功后 回调效果
          that.$message.success('删除成功')
          await that.fetchTableData()
        } else {
          // 删除失败后 回调失败信息
          that.$message.error(response.data.msg)
        }
      }
    },

    handleClick(tab) {
      this.SearchItem.type = tab.name
      this.tableData = []
      this.fetchTableData()
    },


    handleContentChange (file) {
      let that = this
      this.isSubmitLoading = true
      let FileSize = file.size / 1024 / 1024 < 20
      if (file.type !== 'application/json') {
        that.$message.error('仅支持.json格式文件上传')
        this.isSubmitLoading = false
        return false
      }
      if (!FileSize) {
        that.$message.error('上传超出文件限制大小')
        this.isSubmitLoading = false
        return false
      }
      let fileName = file.name.split('.')
      if(fileName.length < 2) {
        this.$message.error('非法文件格式')
        return false
      }
      if (that.typeId === '1'){
        const reader = new FileReader();
        reader.readAsText(file);
        reader.onload = function (){
          let tempData = this.result
          let dataField = JSON.parse(tempData.toString())
          for (let df of dataField) {
            let id = that.getGuid()
            that.$set(df,'id',id)
            let type = df.type
            let dataList = []
            if (type === '1'){
              dataList = JSON.parse(localStorage.getItem('dataField01'));
            }else {
              dataList = JSON.parse(localStorage.getItem('dataField02'));
            }
            if (dataList === null || dataList === ''){
              dataList = []
              dataList.push(df)
            }else {
              for (let dl of dataList) {
                let dataField = dl.field
                let tempField = df.field
                if (dataField === tempField){
                  that.$message.error('字段'+df.field+'已存在')
                  that.fetchTableData()
                  that.isSubmitLoading = false
                  return false
                }
              }
              dataList.push(df)
            }
            if (type === '1'){
              localStorage.setItem('dataField01',JSON.stringify(dataList))
            }else {
              localStorage.setItem('dataField02',JSON.stringify(dataList))
            }
          }
          that.fetchTableData()
          that.isSubmitLoading = false
        }
        return false
      }
    },

    handleSuccess (data) {
      let code = data.code
      if (code === 1){
        this.$message.success('上传成功')
        this.$refs['uploadFile'].clearValidate()
        this.fetchTableData();
      }else {
        this.$message.error('上传失败:' + data.msg)
      }
      this.isSubmitLoading = false
    },
    hError(error){
      const result = JSON.parse(error.message);
      this.$message.error('上传失败:' + result.msg)
    },
    getFileUrl01(){
      this.$refs.addFile.initial('添加','isPcList');
    },
    getFileUrl02(){
      this.$refs.addFile.initial('添加','isPcDocument');
    },
    getFileUrl03(){
      this.$refs.addFile.initial('添加','isAppList');
    },
    getFileUrl04(){
      this.$refs.addFile.initial('添加','isAppDocument');
    },
    backToPage () {
      const that = this
      that.ProhibitFrom = false
      localStorage.removeItem('dataField01')
      localStorage.removeItem('dataField02')
      this.$router.push({
        path: '/template/template',
      });
    },
    SureToAddClick () {
      const that = this
      let dataList01 = JSON.parse(localStorage.getItem('dataField01'));
      let dataList02 = JSON.parse(localStorage.getItem('dataField02'));
      if (dataList01 === null || dataList01 === '') {
        dataList01 = []
      }
      if (dataList02 !== null && dataList02 !== ''){
        for (let df of dataList02) {
          if (df){
            dataList01.push(df)
          }
        }
      }
      that.templateInfo.dataList = dataList01
      localStorage.removeItem('dataField01')
      localStorage.removeItem('dataField02')
      if (this.typeId === '1') {
        this.insertTemplate(that.templateInfo)
        return false
      } else if (this.typeId === '2') {
        this.updateTemplate(that.templateInfo)
        return false
      }
    },
    async insertTemplate(data) {
      const that = this
      let name = data.name
      if (name == null || name ===''){
        console.log('asgqrSADFQFQ')
        that.$message.error('请填入模板名称')
        return false
      }
      that.loading = true
      const response = await insert(data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success('添加成功')
        this.$router.push({
          path: '/template/template',
        });
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async updateTemplate(data){
      const that = this
      that.loading = true
      const response = await update(data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success('编辑成功')
        this.$router.push({
          path: '/template/template',
        });
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    getGuid() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      })
    },

    selectFile(){
      const that = this
      if (that.templateInfo.isPcList === '0'){
        that.templateInfo.pcListUrl = ''
        that.pcListUrl = ''
      }
      if(that.templateInfo.isPcDocument === '0'){
        that.templateInfo.pcDocumentUrl = ''
        that.pcDocumentUrl = ''
      }
      if (that.templateInfo.isAppList === '0'){
        that.templateInfo.appListUrl = ''
        that.appListUrl = ''
      }
      if (that.templateInfo.isAppDocument === '0'){
        that.templateInfo.appDocumentUrl = ''
        that.appDocumentUrl = ''
      }
      // that.fetchTemplateData()
    },

    // async fetchTemplateData() {
    //   this.load = true
    //   try {
    //     const data2 = {
    //       id: this.parentId,
    //     }
    //     let res2 = await findById(data2)
    //     let info = res2.data.data
    //     this.templateInfo = info
    //     this.pcListUrl = info.pcListUrl.substr(info.pcListUrl.lastIndexOf('/')+1)
    //     this.pcDocumentUrl = info.pcDocumentUrl.substr(info.pcDocumentUrl.lastIndexOf('/')+1)
    //     this.appListUrl = info.appListUrl.substr(info.appListUrl.lastIndexOf('/')+1)
    //     this.appDocumentUrl = info.appDocumentUrl.substr(info.appDocumentUrl.lastIndexOf('/')+1)
    //     this.load = false
    //   } catch (even) {
    //     this.load = false
    //     this.$message.error(even.msg)
    //   }
    // },

  }
}
</script>
