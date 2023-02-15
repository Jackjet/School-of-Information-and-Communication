<template>
  <el-main :v-loading="load">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="back" icon="el-icon-back">返回</el-button>
        </el-form-item>
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
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <Add ref="add" @Reload='fetchData'></Add>
  </el-main>
</template>

<script>
import {findData,deleteData} from '@/api/template'
import {getToken} from '@/utils/auth'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import Add from '@/views/template/dataFieldAdd.vue'
export default {
  components: { TableList, Pagination, Add },
  data() {
    return {
      tableData: [],
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
      lastItem: {
        name: '',
        phone: '',
        userName: '',
        Timeframe: ''
      },
      header: {
        'Authorization': 'token ' + getToken()
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      load: false,
      parentId: '',
      action: '',
      fileList: []
    }
  },
  mounted() {
    this.show = true
    this.parentId = this.$route.query.parentId
    this.action = process.env.VUE_APP_BASE_API + '/webadmin/template/uploadJson?templateId='+this.parentId+'&file='
    this.fetchData();
  },
  methods: {
    async fetchData() {
      this.load = true
      const data = {
        templateId: this.parentId,
        type: this.SearchItem.type,
        page: this.currentPage,
        size: this.pageSize
      }
      try {
        let res = await findData(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
        this.load = false
      } catch (even) {
        this.load = false
        this.$message.error(even.msg)
      }
    },

    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },

    add() {
      this.$refs.add.initial('添加',this.parentId);
    },
    edit(row) {
      this.$refs.add.initial('编辑',this.parentId,row.id);
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
      const response = await deleteData({id:row.id});
      that.loading = false

      if (response.data.code === 1) {
        // 删除成功后 回调效果
        that.$message.success('删除成功')
        await that.fetchData()
      } else {
        // 删除失败后 回调失败信息
        that.$message.error(response.data.msg)
      }
    },

    handleClick(tab) {
      this.SearchItem.type = tab.name
      this.fetchData()
    },

    back(){
      this.$router.push({
        path: '/template/template'
      })
    },

    async resourceFile(row){
      this.$router.push({
        path: '/repository/resourceFile',
        query: {
          repositoryId: row.id,
          parentId:''
        },
      });
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
    },

    handleSuccess (data) {
      let code = data.code
      if (code === 1){
        this.$message.success('上传成功')
        this.$refs['uploadFile'].clearValidate()
        this.fetchData();
      }else {
        this.$message.error('上传失败:' + data.msg)
      }
      this.isSubmitLoading = false
    },
    hError(error){
      const result = JSON.parse(error.message);
      this.$message.error('上传失败:' + result.msg)
    },
  }
}
</script>
