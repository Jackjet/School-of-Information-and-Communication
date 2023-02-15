<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入数据源名称" v-model="SearchItem.name">
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-select clearable v-model="SearchItem.type" placeholder="请选择数据源分类">
            <el-option
              v-for="item in dataCatalogs"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-select clearable v-model="SearchItem.status" placeholder="请选择数据源状态">
            <el-option
              v-for="item in dataBases"
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
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <drawer :modal-objj='modalObjj' ref="callDetail" @Reload="submitLoading"></drawer>
  </el-main>
</template>

<script>
  import drawer from './add'
  import {getDataSource, deleteDataSource, updateUpdateStatus, metaDataTableUpdate} from '@/api/dataSource'
  import TableList from '@/components/table/TableList.vue'
  import Pagination from '@/components/table/Pagination.vue'
  // import drawer from './detail'
  export default {
    components: {TableList, Pagination, drawer},
    data() {
      return {
        drawer: false,
        tableData: [],
        modalObjj: '',
        tableHeader: [{
          id: false, type: '', label: '数据源名称', list: 'name'
        }, {
          id: false, type: '', label: '数据源分类', list: 'type'
        }, {
          id: false, type: '', label: '数据源地址', list: 'ipAddress'
        }, {
          id: false, type: '', label: '状态', list: 'status'
        }, {
          id: false, type: '', label: '创建日期', list: 'createTime'
        }, {
          id: false, type: '', label: '更新日期', list: 'updateTime'
        }],
        tableOpction: {
          label: '操作',
          width: '500px',
          value: 1,
          options: [{
            label: '详情',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.detail(row)
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
            label: '删除',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.delete(row)
            }
          }, {
            label: '禁用',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.ban(row)
            }
          }, {
            label: '更新元数据',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.DataTableUpdate(row)
            }
          }, {
            label: '查看元数据',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.LookDataTable(row)
            }
          }]
        },
        dataBases: [{value: '生效', lable: '生效'}, {value: '失效', lable: '失效'}],
        dataOptions: [{value: '关系数据源', lable: '关系数据源'}, {value: '大数据数据源', lable: '大数据数据源'}, {value: '文件数据源', lable: '文件数据源'}],
        dataCatalogs: [{value: 'MySQL', lable: 'MySQL'},
          {value: 'Oracle', lable: 'Oracle'},
          {value: 'SqlServer', lable: 'SqlServer'},
          {value: 'PostgreSQL', lable: 'PostgreSQL'},
          // {value: 'Hive', lable: 'Hive'},
          // {value: 'ElasticSearch', lable: 'ElasticSearch'},
          // {value: 'HBase', lable: 'HBase'},
          // {value: 'MongoDB', lable: 'MongoDB'},
          // {value: '文件目录', lable: '文件目录'},
          // {value: 'FTP', lable: 'HDFS'},
          // {value: 'HTTP', lable: 'HTTP'},
          // {value: 'HDFS', lable: 'HDFS'}
          ],
        isSubmitLoading: false,
        SearchItem: {
          name: '',
          status: '',
          type: '',
          catalog: ''
        },
        lastItem: {
          name: '',
          status: '',
          type: '',
          catalog: ''
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
          name: this.SearchItem.name,
          type: this.SearchItem.type,
          catalog: this.SearchItem.catalog,
          status: this.SearchItem.status,
        }
        try {
          const res = await getDataSource(data)
          res.data.data.content.map(function (k, v) {
            if (k.businessTechnicalType === '1') {
              k.businessTechnicalType = '业务'
            } else {
              k.businessTechnicalType = '技术'
            }
          })
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } catch (even) {
          this.$message.error(even.msg)
        }
      },
      ban(row) {
        const that = this
        let status = row.status === '生效' ? '禁用' : '启用'
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
          const response = await updateUpdateStatus(data)
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
      edit: function (row) {
        this.modalObjj = '编辑'
        let data = {
          value: row.type
        }
        this.$refs.callDetail.initial(row.id, data)
      },

      detail: function (row) {
        this.modalObjj = '详情'
        let data = {
          value: row.type
        }
        this.$refs.callDetail.initial(row.id, data)
      },

      LookDataTable(row) {
        this.$router.push({name: "sourceData", query: {id: row.id}})
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
          const response = await deleteDataSource(row.id)
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
      add() {
        this.$router.push({name: "choose", params: {id: ''}})
      },
      async findNodes() {
        const that = this
        that.loading = true
        const response = await findAllNodes()
        that.loading = false
        if (response.data.code === 1) {
          that.options = []
          response.data.data.map(function (v) {
            that.options.push({value: v.id, label: v.name})
          })
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },

      async DataTableUpdate(row) {
        const that = this
        that.loading = true
        let da = {
          dataSourceId: row.id
        }
        const response = await metaDataTableUpdate(da)
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },

      async findDatabases(vId) {
        const that = this
        const data = {
          nodeId: that.SearchItem.nodeId
        }
        that.loading = true
        const response = await findAllDatabases(data)
        that.loading = false
        if (response.data.code === 1) {
          that.optionss = []
          response.data.data.map(function (v) {
            that.optionss.push({value: v.id, label: v.name})
          })
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      }

    }
  }
</script>
