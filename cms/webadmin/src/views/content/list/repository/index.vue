<template>
  <el-drawer
    title="资源库上传"
    direction="rtl"
    v-loading="load"
    :visible.sync="isShow"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer"
    :append-to-body="true">
    <el-col :span="24" style="margin-left: 20px;width: 90%">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
                    size="medium"
                    placeholder="请输入资源库名称"
                    prefix-icon="el-icon-search"
                    v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click.native="fetchData" icon="el-icon-search">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" style="margin-left: 20px;width: 90%">
      <TableList :table-data='tableData' v-loading="isSubmitLoading"
                 :table-label="tableHeader"
                 :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24' style="margin-left: 20px;width: 90%">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <ResourceFile ref="resourceFile" @sure="sure"></ResourceFile>
  </el-drawer>
</template>

<script>
import {findAllRepository,fileFindAll} from '@/api/content'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import ResourceFile from '@/views/content/list/repository/resourceFile.vue';
export default {
  components: { TableList, Pagination,ResourceFile},
  data() {
    return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '资源库名称', list: 'name'},
        {id: false, type: '', label: '创建时间', list: 'createTime'}
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [{
          label: '资源库内容',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.resourceFile(row)
          }
        }]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        name: '',
        phone: '',
        userName: '',
        Timeframe: ''
      },
      lastItem: {
        name: '',
        phone: '',
        userName: '',
        Timeframe: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      load: false,
      isShow:false,
      field:'',
      isSingle:false,
      fieldName:''
    }
  },

  methods: {
    init(field,isSingle,fieldName){
      this.isShow = true
      this.field = field
      this.isSingle = isSingle
      this.fieldName = fieldName
      this.fetchData()
    },
    async fetchData() {
      this.load = true
      const data = {
        name: this.SearchItem.name,
        page: this.currentPage,
        size: this.pageSize
      }
      try {
        let res = await findAllRepository(data)
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

    async resourceFile(row){
      this.$refs.resourceFile.initial(row.id,this.isSingle)
    },
    async ClearTextConeten () {
      const that = this
      that.isShow = false
    },
    sure(data){
      this.$emit('sure', this.field,data,this.fieldName,this.isSingle)
      this.ClearTextConeten()
    }
  }
}
</script>
