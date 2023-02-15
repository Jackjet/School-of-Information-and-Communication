<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-select clearable v-model="SearchItem.groupTaskId" placeholder="请选择所属任务组">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入任务名称" v-model="SearchItem.name">
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-select clearable v-model="SearchItem.strategy" placeholder="请选择增全量策略">
            <el-option label="增量" value="增量"></el-option>
            <el-option label="全量" value="全量"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-select clearable v-model="SearchItem.status" placeholder="请选择任务状态">
            <el-option label="生效" value="生效"></el-option>
            <el-option label="失效" value="失效"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size='medium' @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="primary" size='medium' @click="add" icon="el-icon-add">添加</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction">
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
  import {getList, deletes, insert, update, findById, groupTaskfa, updateStatus, manualOperation} from '@/api/task/manag'
  import TableList from '@/components/table/TableList.vue'
  import Pagination from '@/components/table/Pagination.vue'
  import drawer from './add'
  import md5 from 'js-md5'

  export default {
    components: {TableList, Pagination, drawer,},
    data() {
      return {
        drawer: false,
        tableData: [],
        tableHeader: [{
          id: false, type: '', label: '任务组名称', list: 'groupTaskName'
        }, {
          id: false, type: '', label: ' 任务名称', list: 'name'
        }, {
          id: false, type: '', label: '源', list: 'dataSourceSourceName'
        }, {
          id: false, type: '', label: '目标', list: 'dataSourceTargetName'
        }, {
          id: false, type: '', label: '增全量策略', list: 'strategy'
        }, {
          id: false, type: '', label: '状态', list: 'status'
        }, {
          id: false, type: '', label: '执行器API', list: 'api'
        }
        ],
        tableOpction: {
          label: '操作',
          width: '430px',
          value: 4,
          options: [{
            label: '任务执行历史',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.mehistory(row)
            }
          }, {
            label: '删除',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.delete(row)
            }
          }, {
            label: '',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.ban(row)
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
            label: '手动执行',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.resets(row)
            }
          }]
        },
        isSubmitLoading: false,
        DeletelistiD: [],
        modalObjj: '',
        SearchItem: {
          strategy: '',
          status: '',
          groupTaskId: '',
          name: '',

        },
        groupTaskfaItem: {
          name: '',
          sourceCatalog: '',
          dataSourceSourceId: '',
          targetCatalog: '',
          dataSourceTargetId: '',
          status: '',
        },
        options: [],
        total: 0,
        pageSize: '20',
        currentPage: '1',
        RootUrl: ''
      }
    },
    created() {
      this.SearchItem.groupTaskId = this.$route.query.code
      this.findNodes()
      this.fetchData()
    },
    methods: {
      async fetchData() {
        const data = this.SearchItem
        data.page = this.currentPage
        data.size = this.pageSize
        try {
          const res = await getList(data)
          if (res.data.code === 1) {
//        res.data.data.content.map(function(v, k){
//          if(v.status === '无效'){
//            v.status = '无效'
////            this.chuangtai = "启用"
//          }else{
//            v.status = '有效'
////             this.chuangtai = "禁用"
//          }
//        })
            this.tableData = res.data.data.content
            this.total = res.data.data.totalElements
          } else {
            this.$message.error(even.msg)
          }

        } catch (even) {
          this.$message.error(even.msg)
        }
      },
      search() {
        this.currentPage = 1;
        this.$refs.page.Page(1);
        this.fetchData()
      },
      submitLoadingg() {
        this.fetchData()
      },
      mehistory: function (row) {
        let that = this
        this.$router.push({
          path: '/taskManage/executionlog',
          query: {
            name: row.name
          }
        })
      },
      add: function () {
        let that = this
        this.modalObjj = '添加'
        if (this.$route.query.code) {
          this.$refs.callDetaill.initial(false, this.$route.query.code)
        } else {
          this.$refs.callDetaill.initial(false, '')
        }
      },
      edit: function (row) {
        let that = this
        that.modalObjj = '编辑'
        that.$refs.callDetaill.initial(row.id, '')

      },
      async resets(row) {

        let that = this
        var data = {
          id: row.id
        }
        if (row.api === '') {
          that.$confirm('该任务没有执行器API，请确认是否执行？', '提示', {
            type: 'warning'
          }).then(async () => {
            const res = await manualOperation(data)
            if (res.data.code === 1) {
              that.$message.success(res.data.msg)
            } else {
              that.$message.error(res.data.msg)
            }
          }).catch(() => {
            return false
          })
        } else {
          const ress = await manualOperation(data)
          if (ress.data.code === 1) {
            that.$message.success(ress.data.msg)
          } else {
            that.$message.error(ress.data.msg)
          }

        }
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
          const response = await deletes(row.id)
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
      // 删除当前单条数据 重载列表
      ban(row) {
        const that = this;
        let status = row.status === '生效' ? '禁用' : '启用'
        that.$confirm('请确定' + status + '吗?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          let data = {
            id: row.id,
            status: status
          }
          const response = await updateStatus(data)
          that.Loading = false
          if (response.data.code === 1) {
            that.$message.success(response.data.msg)
            this.fetchData()
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          return false
        })
      },


      async findNodes() {
        const that = this

        const data = this.groupTaskfaItem
        data.page = this.currentPage
        data.size = this.pageSize
        const response = await groupTaskfa(data)
        if (response.data.code === 1) {
          that.options = []
          response.data.data.content.map(function (v, k) {
            that.options.push({value: v.id, label: v.name})
          })
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
    }
  }
</script>




