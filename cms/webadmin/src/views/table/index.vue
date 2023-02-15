<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入访客"
            prefix-icon="el-icon-search"
            v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入访客手机号"
            prefix-icon="el-icon-search"
            v-model="SearchItem.phone">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable
            size="medium"
            width='50'
            placeholder="请输入被访人"
            prefix-icon="el-icon-search"
            v-model="SearchItem.userName">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="SearchItem.Timeframe"
            size="medium"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading"
        :table-label="tableHeader"
        :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>

<script>
import { getList } from '@/api/table'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'

export default {
  components: { TableList, Pagination },
  data() {
      return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '访客', list: 'salesMan'},
        {id: false, type: '', label: '访客手机号', list: 'salesManLoginNme'},
        {id: false, type: '', label: '被访人', list: 'standDisc'},
        {id: false, type: '', label: '拜访日期', list: 'startDate'},
        {id: false, type: '', label: '申请时间', list: 'totalDisc'}
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [{
          label: '详情',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            // this.detail(row)
          }
        },
        {
          label: '编辑',
          key: 0,
          type: 'success',
          State: false,
          method: (row) => {
            // this.detail(row)
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'danger',
          State: false,
          method: (row) => {
            // this.detail(row)
          }
        }
        ]
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
      RootUrl: ''
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
   async fetchData() {
      const data = {
        page: this.currentPage,
        size: this.pageSize
      }
      try {
        let res = await getList(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    }
  }
}
</script>
