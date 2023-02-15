<template>
  <el-main>
    <el-row>
      <el-button type="primary" size='medium' @click="reTu" icon="el-icon-add">返回</el-button>
    </el-row>
    <TableList :table-data='tableData' v-loading="isSubmitLoading"
               :table-label="tableHeader"
               :table-option="tableOpction"
               @loadd='submitLoadingg'>
    </TableList>
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
  import {metaDataTableFindById} from '@/api/dataSource'
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
        tableHeader: [{id: false, type: '', label: '字段名', list: 'name'},
          {id: false, type: '', label: '描述', list: 'comment'},
          {id: false, type: '', label: '字段类型', list: 'type'},
          {id: false, type: '', label: '是否为主键', list: 'ifPrimaryKey'},
          {id: false, type: '', label: '允许为空', list: 'isnull'},
        ],
        tableOpction: {},
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
          id: this.$route.query.id
        }
        try {
          const res = await metaDataTableFindById(data)
          if (JSON.parse(res.data.data).length > 0) {
            let dataSourse = JSON.parse(res.data.data)
            dataSourse.map(function (v, k) {
              if (v.isnull === 0) {
                v.isnull = '否'
              } else {
                v.isnull = '是'
              }
              if (v.ifPrimaryKey === 'true') {
                v.ifPrimaryKey = '是'
              } else {
                v.ifPrimaryKey = '是'
              }
            })
            this.tableData = dataSourse
            this.total = dataSourse.length
          }


        } catch (even) {
          this.$message.error(even.msg)
        }
      },
      reTu: function (value) {
        this.$router.push({name: "sourceData", query: {id: this.$route.query.dataSourceId}})
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
    }
  }
</script>
