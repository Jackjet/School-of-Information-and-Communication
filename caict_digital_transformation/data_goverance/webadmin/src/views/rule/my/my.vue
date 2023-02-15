<template>
  <el-main>
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <!-- <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入名称"
            prefix-icon="el-icon-search"
            v-model="SearchItem.name">
          </el-input>
        </el-form-item> -->
        <!-- <el-form-item>
          <el-button type="primary"  size='medium' @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="primary"  size='medium' @click="add" icon="">添加</el-button>
        </el-form-item> -->
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading"
        :table-label="tableHeader"
        :table-option="tableOpction"
        @loadd='submitLoadingg'>
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <!-- <drawer
      :modal-objj='modalObjj'
      ref="callDetaill"
      @Reload='submitLoadingg'>
    </drawer> -->
  </el-main>

</template>

<script>
import { findByCurrentUser } from '@/api/user'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
// import drawer from './add'
import { getToken } from '@/utils/auth'

export default {
  components: { TableList, Pagination },

  data() {
      return {
      drawer: false,
      tableData: [],
      tableHeader: [{id: false, type: '', label: '名称', list: 'ruleName'},
        {id: false, type: '', label: '创建时间', list: 'createTime'},
      ],
      tableOpction: {

      },
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        name: '',
        type: ''
      },
      lastItem: {
        name: '',
        type: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
    }
  },
  created () {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
      }
      try {
        const res = await findByCurrentUser(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
   add: function () {
      let that = this
      this.modalObjj = '添加'
      this.$refs.callDetaill.initial(false, this.$route.meta.title)
    },
    edit: function (row) {
      let that = this
      this.modalObjj = '编辑'
      this.$refs.callDetaill.initial(row.id, this.$route.meta.title)
    },
    submitLoadingg(){
      this.fetchData()
    },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },
    search(){
      this.currentPage = 1;
      this.$refs.page.Page(1);
      this.fetchData()
    },
    // 删除当前单条数据 重载列表
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.Loading = true
        const response = await deleteKnowledge(row.id)
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
  }
}
</script>
