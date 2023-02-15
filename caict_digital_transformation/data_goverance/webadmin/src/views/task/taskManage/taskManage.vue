<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" v-model="SearchItem.name" placeholder="请输入任务组名称">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.dataSourceSourceId" placeholder="请选择数据来源">
            <el-option
              v-for="item in dataSource"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.dataSourceTargetId" placeholder="请选择目标源">
            <el-option
              v-for="item in dataSource"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
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
  import {getGroupTask, deletegroupTask, updateStatus, manualOperation} from '@/api/groupTask'
  import {getDataSource} from '@/api/dataSource'
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
          id: false, type: '', label: '任务组名称', list: 'name'
        }, {
          id: false, type: '', label: '数据来源', list: 'dataSourceSourceName'
        }, {
          id: false, type: '', label: '目标源', list: 'dataSourceTargetName'
        }, {
          id: false, type: '', label: '调度周期', list: 'operationMode'
        }, {
          id: false, type: '', label: '状态', list: 'status'
        }, {
          id: false, type: '', label: '创建日期', list: 'createTime'
        }, {
          id: false, type: '', label: '更新日期', list: 'updateTime'
        }
        ],
        tableOpction: {
          label: '操作',
          width: '430px',
          value: 3,
          options: [{
            label: '任务列表查看',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.see(row)
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
              this.commit(row)
            }
          }]
        },
        dataSource: [],
        targetSource: [],
        isSubmitLoading: false,
        DeletelistiD: [],
        modalObjj: '',
        SearchItem: {
          name: '',
          sourceCatalog: '',
          targetCatalog: '',
          status: '',
          dataSourceSourceId: '',
          dataSourceTargetId: '',
        },
        lastItem: {
          name: '',
          sourceCatalog: '',
          targetCatalog: '',
          status: '',
          dataSourceSourceId: '',
          dataSourceTargetId: '',
        },
        options: [{value: '关系数据源', lable: '关系数据源'}, {value: '大数据数据源', lable: '大数据数据源'}, {value: '文件数据源', lable: '文件数据源'}],
        total: 0,
        pageSize: '20',
        currentPage: '1',
        RootUrl: ''
      }
    },
    created() {
      this.findNodes()
      this.fetchData()
    },
    methods: {
      async fetchData() {
        const data = this.SearchItem
        data.page = this.currentPage
        data.size = this.pageSize
        try {
          const res = await getGroupTask(data)
          if (res.data.code === 1) {
            // res.data.data.content.map(function(v, k){
            //   if(v.status === '0'){
            //     v.status = '禁用'
            //   }else{
            //     v.status = '启用'
            //   }
            // })
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
      see(row) {
        this.$router.push({
          name: 'manag',
          query: {
            code: row.id
          }
        })
      },
      submitLoadingg() {
        this.fetchData()
      },
      add: function () {
        this.modalObjj = '添加'
        this.$refs.callDetaill.initial(false)
      },
      edit: function (row) {
        this.modalObjj = '编辑'
        this.$refs.callDetaill.initial(row.id)
      },
      pageChange(item) {
        this.pageSize = item.limit
        this.currentPage = item.page
        this.fetchData()
      },

      commit(row) {
        const that = this
        that.$confirm('请确认是否执行？', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          let data = {
            id: row.id,
          }
          const response = await manualOperation(data)
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
      // 删除当前单条数据 重载列表
      delete(row) {
        const that = this
        that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          const response = await deletegroupTask(row.id)
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
        const that = this
        let status = row.status === '已上线' ? '禁用' : '启用'
        that.$confirm('请确定' + status + '吗?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          // let status = ''
          //  if(row.status === '禁用'){
          //       status = 1
          //   } else {
          //       status = 0
          //   }
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

      reset(row) {
        const that = this
        that.$confirm('此操作将重置密码, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          let data = {
            id: row.id,
          }
          const response = await setPasswd(data)
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
      async findNodes(data) {
        const that = this
        that.loading = true
        const response = await getDataSource()
        that.loading = false
        if (response.data.code === 1) {
          that.dataSource = []
          response.data.data.content.map(function (v, k) {
            that.dataSource.push({value: v.id, label: v.name})
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




