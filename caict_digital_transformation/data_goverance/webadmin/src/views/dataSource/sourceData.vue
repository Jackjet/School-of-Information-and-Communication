<template>
  <el-main>
    <el-row>
      <el-button type="primary" size='medium' @click="reTu" icon="el-icon-add">返回</el-button>
    </el-row>
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
    <drawer
      :modal-objj='modalObjj'
      ref="callDetaill"
      @Reload='submitLoadingg'>
    </drawer>
  </el-main>

</template>

<script>
  import {metaDataTablFindall, updateFields} from '@/api/dataSource'
  import TableList from '@/components/table/TableList.vue'
  import Pagination from '@/components/table/Pagination.vue'
  import drawer from './add'
  import {getToken} from '@/utils/auth'

  export default {
    components: {TableList, Pagination, drawer},

    data() {
      return {
        drawer: false,
        tableData: [],
        tableHeader: [{id: false, type: '', label: '表名', list: 'tableName'},
          {id: false, type: '', label: '数据源名称', list: 'dataSourceName'},
          {id: false, type: '', label: '描述', list: 'tableDesc'},
          {id: false, type: '', label: '元数据状态', list: 'status'},
          {id: false, type: '', label: '元数据更新日期', list: 'updateTime'},
        ],
        tableOpction: {
          label: '操作',
          width: '300px',
          value: 0,
          options: [{
            label: '显示字段',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.seeSource(row)
            }
          },
            {
              label: '更新字段',
              key: 0,
              type: 'primary',
              State: true,
              method: (row) => {
                this.updateSource(row)
              }
            },
            {
              label: '查看数据',
              key: 0,
              type: 'primary',
              State: true,
              method: (row) => {
                this.query(row)
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
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        const data = {
          page: this.currentPage,
          size: this.pageSize,
          dataSourceId: this.$route.query.id
        }
        try {
          const res = await metaDataTablFindall(data)
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } catch (even) {
          this.$message.error(even.msg)
        }
      },
      seeSource(row) {
        this.$router.push({name: "source", query: {id: row.id, dataSourceId: this.$route.query.id}})
      },
      reTu: function (value) {
        this.$router.push({name: "index"})
      },
      async updateSource(row) {
        const that = this
        that.loading = true
        let da = {
          id: row.id
        }
        const response = await updateFields(da)
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
      add: function () {
        let that = this
        this.modalObjj = '添加'
        this.$refs.callDetaill.initial(false, this.$route.meta.title)
      },
      submitLoadingg() {
        this.fetchData()
      },
      pageChange(item) {
        this.pageSize = item.limit
        this.currentPage = item.page
        this.fetchData()
      },
      // 删除当前单条数据 重载列表
      delete(row) {
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
      query(row) {
        this.$router.push({name: "queryTableData", query: {id: row.id, dataSourceId: this.$route.query.id}})
      }
    }
  }
</script>
