<template>
  <el-main>
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入名称"
            prefix-icon="el-icon-search"
            v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <el-button type="primary"  size='medium' @click="drawer = true" icon="">添加</el-button> 
        </el-form-item>
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
    <el-drawer
      title="我是标题"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose">
      <span>我来啦!</span>
    </el-drawer>
  </el-main>
  
</template>

<script>
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'

import { getToken } from '@/utils/auth'

export default {
  components: { TableList, Pagination},

  data() {
      return {
      drawer: false,
      tableData: [],
      tableHeader: [{id: false, type: '', label: '名称', list: 'name'},
        {id: false, type: '', label: '描述', list: 'description'},
        {id: false, type: '', label: '下载量', list: 'download'},
        {id: false, type: 'wj', label: '文件名', list: 'fileName'},
        {id: false, type: '', label: '提交时间', list: 'createTime'},
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [{
          label: '编辑',
          key: 0,
          type: 'primary',
          State: true,
          method: (row) => {
            this.edit(row)
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'primary',
          State: true,
          method: (row) => {
            this.delete(row)
          }
        }
      ]
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
        type: this.$route.meta.title,
        name: this.SearchItem.name
      }
      try {
        const res = await getKnowledge(data)
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

  }
}
</script>
