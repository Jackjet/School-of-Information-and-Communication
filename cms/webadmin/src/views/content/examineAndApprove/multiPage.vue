<template>
  <el-container>
    <el-main>
      <el-col :span="24">
        <el-form :inline="true" class='el-InputForm'>
          <el-form-item>
            <el-input clearable
                      size="medium"
                      placeholder="请输入文章标题"
                      prefix-icon="el-icon-search"
                      v-model="SearchItem.title">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"  size='medium' @click="fetchData" icon="el-icon-search">搜索</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24">
        <el-table
          class="el_tab_alage"
          :data="tableData"
          style="width: 100%"
          row-key="id"
          lazy
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
          v-loading="loading">
          <el-table-column
            prop="title"
            label="文章标题"
            align='center'>
          </el-table-column>
          <el-table-column
            prop="columnName"
            label="所属栏目"
            align='center'>
          </el-table-column>
          <el-table-column
            prop="status"
            label="审核"
            align='center'>
            <template slot-scope="scope">
              {{status[scope.row.status].value}}
            </template>
          </el-table-column>
          <el-table-column
            prop="updateTime"
            label="更新时间"
            align='center'
            min-width="180px">
          </el-table-column>

          <el-table-column fit align='center'
                           :width='tableOption.width'
                           :label="tableOption.label">
            <template slot-scope="scope">
              <el-button  size="mini"  v-for="(value, item) in tableOption.options"
                          :key='item'
                          :type="value.type"
                          :plain='value.State'
                          :icon="value.icon"
                          @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span='24'>
        <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      </el-col>
      <Rejected ref="rejected" @Reload='fetchData'></Rejected>
    </el-main>
  </el-container>
</template>

<script>
import {findAll,pass,rejected} from '@/api/audit'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import Template from '../../template/template';
import Rejected from '@/views/content/examineAndApprove/rejected.vue'
export default {
  components: {Template, TableList, Pagination,Rejected},
  data() {
    return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '文章标题', list: 'title'},
        {id: false, type: '', label: '所属栏目', list: 'columnName'},
        {id: false, type: '', label: '审核', list: 'status'},
        {id: false, type: '', label: '修改时间', list: 'updateTime'}
      ],
      tableOption: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [{
          label: '预览',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.preview(row)
          }
        },{
          label: '通过',
          key: 0,
          type: 'danger',
          State: false,
          method: (row) => {
            this.pass(row)
          }
        },{
          label: '驳回',
          key: 0,
          type: 'danger',
          State: false,
          method: (row) => {
            this.rejected(row)
          }
        }]
      },
      columnId:'',
      templateId:'',
      total: 0,
      pageSize: '20',
      currentPage: '1',
      load: false,
      SearchItem: {
        title:''
      },
      isList:false,
      status:[{
        key: '0',
        value:'待审核'
      },{
        key: '1',
        value:'已通过'
      },{
        key: '2',
        value:'已驳回'
      }],
      pcListUrl:'',
      pcDocumentUrl:'',
      appListUrl:'',
      appDocumentUrl:''
    }
  },
  watch:{
    async columnId (newValue ,oldValue){
      oldValue = newValue
      await this.fetchData()
    },
    async templateId (newValue ,oldValue){
      oldValue = newValue
      await this.fetchData()
    },
    async isList (newValue,oldValue){
      oldValue = newValue
      await this.fetchData()
    },
    async pcListUrl (newValue ,oldValue){
      oldValue = newValue
    },
    async pcDocumentUrl (newValue ,oldValue){
      oldValue = newValue
    },
    async appListUrl (newValue ,oldValue){
      oldValue = newValue
    },
    async appDocumentUrl (newValue ,oldValue){
      oldValue = newValue
    }
  },
  props:{
    columnId: String,
    templateId: String,
    isList: Boolean,
    pcListUrl:String,
    pcDocumentUrl:String,
    appListUrl:String,
    appDocumentUrl:String
  },
  async created() {
    const that = this
    await that.fetchData()
  },
  methods: {
    async fetchData() {
      this.load = true
      this.currentPage=1
      const data = {
        columnId: this.columnId,
        title: this.SearchItem.title,
        page: this.currentPage,
        size: this.pageSize
      }
      try {
        let res = await findAll(data)
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
    async pass(row){
      const that = this
      let response = await pass({id:row.id});
      if (response.data.code === 1) {
        that.$message.success(response.data.msg)
        that.fetchData()
      }else{
        that.$message.error(response.data.msg)
      }
    },
    async rejected(row){
      this.$refs.rejected.initial(row.id);
    },
    preview(row){
      const that = this

      let link = process.env.VUE_APP_BASE_API + 'web/view/' + that.pcDocumentUrl + '?id=' + row.id
      window.open(link, '_blank')
    },
  }
}
</script>
