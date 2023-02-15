<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="go" icon="">返回</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>

<script>
import { findUpdate } from '@/api/rule'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      drawer: false,
      tableData: [],
      tableHeader: [{
        id: false, type: '', label: '变更时间', list: 'updateTime'
      }, {
        id: false, type: '', label: '变更日志', list: 'log'
      }],
      tableOpction: {

      },
      options: [],
      optionss: [],
      isSubmitLoading: false,
      DeletelistiD: [],
      modalObjjj: '',
      SearchItem: {
        name: ''
      },
      lastItem: {
        name: ''
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
        size: this.pageSize,
        ruleId: this.$route.params.code,
      }
      try {
        const res = await findUpdate(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    submitLoading (row) {
      this.fetchData()
    },

    go(row) {
      this.$router.push({
      　name: 'manage',
      })
    },

    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },

  }
}
</script>
