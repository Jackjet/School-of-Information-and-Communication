<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入模板名称" prefix-icon="el-icon-search" v-model="SearchItem.name">
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
  import {getTemplate, deleteTemplate} from '@/api/dataBase'
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
          id: false, type: '', label: '模板名称', list: 'name'
        }, {
          id: false, type: '', label: '约束分类', list: 'constraintType'
        }, {
          id: false, type: '', label: '引用次数', list: 'referencesCount'
        }, {
          id: false, type: '', label: '创建人', list: 'createByName'
        }, {
          id: false, type: '', label: '创建日期', list: 'createTime'
        }, {
          id: false, type: '', label: '备注', list: 'remark'
        }
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
          }, {
            label: '删除',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.delete(row)
            }
          }, {
            label: '详情',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.detail(row)
            }
          }
          ]
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
        RootUrl: '',
        num: 0,
        newId: ''
      }
    },
    created() {
      if (this.$route.query.type === 'rule') {
        this.newId = this.$route.query.code
      }
      this.fetchData()
    },
    methods: {
      async fetchData() {
        const data = {
          page: this.currentPage,
          size: this.pageSize,
          name: this.SearchItem.name,
        }
        try {
          const res = await getTemplate(data)
          res.data.data.content.map(function (k, v) {
            if (k.businessTechnicalType === '1') {
              k.businessTechnicalType = '业务'
            } else {
              k.businessTechnicalType = '技术'
            }
          })
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
          if (this.newId !== '' && this.num === 0) {
            this.modalObjjj = '详情'
            this.$refs.callDetail.initial(this.newId)
            this.num = 1
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
          const response = await deleteTemplate(row.id)
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
