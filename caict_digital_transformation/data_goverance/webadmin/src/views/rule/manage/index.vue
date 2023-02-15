<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入规则名称" prefix-icon="el-icon-search" v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size='medium' @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="primary" size='medium' @click="add" icon="el-icon-add">添加</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <drawer :modal-objjj='modalObjjj' ref="callDetail" @Reload="submitLoading"></drawer>
  </el-main>
</template>

<script>
  import {getRule, deleteRule, Subscribe} from '@/api/rule'
  import TableList from '@/components/table/TableList.vue'
  import Pagination from '@/components/table/Pagination.vue'
  import drawer from './detail'

  export default {
    components: {TableList, Pagination, drawer},
    data() {
      return {
        drawer: false,
        tableData: [],
        tableHeader: [{
          id: false, type: '', label: '规则名称', list: 'name'
        }, {
          id: false, type: 'sjmb', label: '数据模板', list: 'templateName'
        }, {
          id: false, type: '', label: '创建日期', list: 'createTime'
        }],
        tableOpction: {
          label: '操作',
          width: '400px',
          value: 0,
          options: [{
            label: '变更历史',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.upHistory(row)
            }
          }, {
            label: '编辑',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.edit(row)
            }
          }, {
            label: '详情',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.detail(row)
            }
          }, {
            label: '删除',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.delete(row)
            }
          }]
        },
        options: [],
        optionss: [],
        isSubmitLoading: false,
        DeletelistiD: [],
        modalObjjj: '',
        SearchItem: {
          name: '',
          itcode: ''
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
      if (this.$route.query.itcode) {
        this.SearchItem.itcode = this.$route.query.itcode
      }
      this.fetchData()
    },
    methods: {
      async fetchData() {
        const data = {
          page: this.currentPage,
          size: this.pageSize,
          name: this.SearchItem.name,
          itcode: this.SearchItem.itcode
        }
        try {
          const res = await getRule(data)
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
          if (this.$route.query.id) {
            this.modalObjjj = '详情'
            this.$refs.callDetail.initial(this.$route.query.id)
          }

        } catch (even) {
          this.$message.error(even.msg)
        }
      },

      edit: function (row) {
        this.modalObjjj = '编辑'
        this.$refs.callDetail.initial(row.id)
      },

      detail: function (row) {
        this.modalObjjj = '详情'
        this.$refs.callDetail.initial(row.id)
      },

      add: function () {
        this.modalObjjj = '添加'
        this.$refs.callDetail.initial(false)
      },

      search() {
        this.currentPage = 1;
        this.$refs.page.Page(1);
        this.fetchData()
      },
      submitLoading(row) {
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
          const response = await deleteRule(row.id)
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


      // async findNodes() {
      //   const that = this
      //   that.loading = true
      //   const response = await findAllNodes()
      //   that.loading = false
      //   if (response.data.code === 1) {
      //     that.options = []
      //     response.data.data.map(function(v){
      //       that.options.push({value: v.id, label:v.name})
      //     })
      //   } else {
      //     // 添加上传失败后 回调失败信息
      //     that.$message.error(response.data.msg)
      //     return false
      //   }
      // },
      upHistory(row) {
        this.$router.push({
          name: 'update',
          params: {
            code: row.id
          }
        })
      },
      // async findDatabases(vId) {
      //   const that = this
      //   const data = {
      //     nodeId: that.SearchItem.nodeId
      //   }
      //   that.loading = true
      //   const response = await findAllDatabases(data)
      //   that.loading = false
      //   if (response.data.code === 1) {
      //     that.optionss = []
      //     response.data.data.map(function(v){
      //       that.optionss.push({value: v.id, label:v.name})
      //     })
      //   } else {
      //     // 添加上传失败后 回调失败信息
      //     that.$message.error(response.data.msg)
      //     return false
      //   }
      // }

    }
  }
</script>
