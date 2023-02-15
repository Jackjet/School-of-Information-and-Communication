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
            <el-button type="primary"  size='medium' @click="search" icon="el-icon-search">搜索</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"  size='medium' @click="add" icon="el-icon-plus">添加</el-button>
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
            label="审核状态"
            align='center'>
            <template slot-scope="scope">
              <template v-if="scope.row.status === '2'">
                <el-button type="text" @click="rejectedReason(scope.row)"><span style="color: red">{{status[scope.row.status].value}}</span></el-button>
              </template>
              <template v-else>
                {{status[scope.row.status].value}}
              </template>
            </template>
          </el-table-column>
          <el-table-column
            prop="visitorVolume"
            label="浏览量"
            align='center'>
          </el-table-column>
          <el-table-column
            prop="power"
            label="置顶"
            align='center'>
            <template slot-scope="scope">
              <el-input v-model="scope.row.power" style="width: 40px;" @blur="updatePower(scope.row)"></el-input>
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
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
      <Add ref="add" @Reload='fetchData'></Add>
      <Copy ref="copy" @Reload='fetchData'></Copy>
      <Rejected ref="rejected" @Reload='fetchData'></Rejected>
    </el-main>
  </el-container>
</template>

<script>
import {findAll,updatePower,deleteById} from '@/api/content'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import Add from '@/views/content/list/add.vue'
import Copy from '@/views/content/list/copy.vue'
import Template from '../../template/template';
import Rejected from '@/views/content/list/rejected.vue'
export default {
  components: {Template, TableList, Pagination,Add,Copy,Rejected },
  data() {
    return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '文章标题', list: 'title'},
        {id: false, type: '', label: '所属栏目', list: 'columnName'},
        {id: false, type: '', label: '审核状态', list: 'status'},
        {id: false, type: '', label: '浏览量', list: 'visitorVolume'},
        {id: false, type: '', label: '置顶', list: 'power'},
        {id: false, type: '', label: '创建时间', list: 'createTime'}
      ],
      tableOption: {
        label: '操作',
        width: '300px',
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
          label: '复制',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.copy(row)
          }
        },{
          label: '预览',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.preview(row)
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
    async isList (newValue ,oldValue){
      oldValue = newValue
      await this.fetchData()
    },
    async pcListUrl (newValue ,oldValue){
      console.log('pcListUrl')
      console.log(newValue)
      oldValue = newValue
    },
    async pcDocumentUrl (newValue ,oldValue){
      console.log('pcDocumentUrl')
      console.log(newValue)
      oldValue = newValue
    },
    async appListUrl (newValue ,oldValue){
      console.log('appListUrl')
      console.log(newValue)
      oldValue = newValue
    },
    async appDocumentUrl (newValue ,oldValue){
      console.log('appDocumentUrl')
      console.log(newValue)
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
    async search(){
      this.currentPage = 1
      this.fetchData()
    },
    async fetchData() {
      this.load = true
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

    add() {
      this.$refs.add.initial('添加',this.templateId,this.columnId);
    },
    edit(row) {
      this.$refs.add.initial('编辑',this.templateId, this.columnId,row.id);
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
      let response = await deleteById({id: row.id})
      that.loading = false
      if (response.data.code === 1) {
        that.$message.success(response.data.msg)
        that.fetchData()
      } else {
        that.$message.error(response.data.msg)
      }

    },
    async updatePower(row){
      const that = this
      let data = {
        id: row.id,
        power: row.power
      }
      let response = await updatePower(data)
      if (response.data.code === 0) {
        that.$message.error(response.data.msg)
      }
      that.fetchData()
    },
    copy(row){
      this.$refs.copy.initial('复制',row.id);
    },
    preview(row){
      const that = this

      let link = process.env.VUE_APP_BASE_API + 'web/view/' + that.pcDocumentUrl + '?id=' + row.id
      window.open(link, '_blank')
    },
    rejectedReason(row){
      this.$refs.rejected.initial(row.reasonForRejection);
    }
  }
}
</script>
