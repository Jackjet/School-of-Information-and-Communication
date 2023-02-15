<template>
  <!-- 调用表格 -->
  <el-main v-loading="Loading">
    <el-form :inline="true" class='el-InputForm'>
      <el-form-item>
        <el-input
          clearable
          size="medium"
          autocomplete="off"
          v-model="SearchItem.name"
          placeholder="请输入考试名称">
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          size='medium'
          icon="el-icon-search"
          @click="search">搜索
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          size='medium'
          icon="el-icon-plus"
          @click="add">添加
        </el-button>
      </el-form-item>
    </el-form>
    <tableForm
      :table-data='table.data'
      :table-selection="table.selection"
      :table-label="table.header"
      :table-option="table.option">
    </tableForm>
    <el-col :span='24'>
      <paging ref="pager" :total="table.total" @pageChange="onPageChanged"></paging>
    </el-col>
  </el-main>
</template>
<script>
import tableForm from '../../integration/tableList'
import paging from '../../integration/pagination'
export default {
  components: { tableForm, paging },
  data () {
    return {
      Loading: false,
      SearchItem: {
        name: ''
      },
      lastSearch: {
        name: ''
      },
      table: {
        data: [],
        selection: {
          key: false,
          type: 'selection'
        },
        header: [{
          id: false,
          type: '',
          label: '考试名称',
          list: 'name'
        }, {
          id: false,
          type: '',
          label: '创建时间', // 需要字典赋值
          list: 'createTime'
        }, {
          id: false,
          type: '',
          label: '答题人数', // 需要字典赋值
          list: 'answerNumber'
        }, {
          id: false,
          type: '',
          label: '关联训练营', // 需要字典赋值
          list: 'relatedContent'
        }],
        option: {
          label: '操作',
          value: 0,
          options: [{
            label: '编辑',
            key: 0,
            type: 'text',
            icon: 'el-icon-edit',
            State: false,
            method: (row) => {
              this.edit(row)
            }
          }, {
            label: '预览',
            key: 0,
            type: 'text',
            icon: 'el-icon-view',
            State: false,
            method: (row) => {
              this.toSee(row)
            }
          }, {
            label: '删除',
            key: 0,
            type: 'text',
            icon: 'el-icon-delete',
            State: false,
            method: (row) => {
              this.doDeleteRow(row)
            }
          }]
        },
        total: 0,
        currentPage: 1,
        pageSize: 20
      }
    }
  },

  methods: {
    init () {
      this.SearchItem.name = ''
      this.table.total = 0
      this.table.currentPage = 1
      this.table.pageSize = 20
      this.$refs.pager.changePage()
      this.$refs.pager.changePageSize(20)
      this.searchList()
    },
    // 模糊查询
    search: function () {
      const that = this
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastSearch[item[0]] = that.SearchItem[item[0]]
      })
      that.currentPage = 1
      this.$refs.pager.changePage()
      this.searchList()
    },
    /**
     * 获取列表方法
     */
    async searchList () {
      const that = this
      try {
        let findUrl = that.Interface.courseManage.examination.find
        let url = findUrl + '?page=' + that.table.currentPage + '&size=' + that.table.pageSize
        Object.entries(that.SearchItem).map((item, index) => {
          that.SearchItem[item[0]] = that.lastSearch[item[0]]
        })
        that.Loading = true
        const response = await this.request.dataGet(that, url, that.SearchItem)
        that.Loading = false
        if (response.data.code === 1) {
          that.table.data = response.data.data.content
          that.table.total = response.data.data.totalElements
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },

    /**
     * 编辑点击事件
     */
    edit (row) {
      const that = this
      that.$router.push({
        path: 'examinationEdit',
        query: {
          id: row.id
        }
      })
    },
    toSee (row) {
      const that = this
      that.$router.push({
        path: 'examinationDetail',
        query: {
          id: row.id
        }
      })
    },

    /**
     * 添加点击事件
     */
    add () {
      const that = this
      that.$router.push({path: 'examinationAdd'})
    },

    /**
     * 删除点击事件
     */
    doDeleteRow (row) {
      const that = this
      if (row.relatedContent > 0) {
        that.$message.warning('存在关联内容，不允许删除！')
        return
      }
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.courseManage.examination.delete + '?id=' + row.id
        that.Loading = true
        const response = await this.request.dataDelete(that, url, {})
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          that.searchList()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },

    /**
     * 页信息改变事件
     */
    onPageChanged (item) {
      const that = this
      that.table.currentPage = item.page
      that.table.pageSize = item.limit
      that.searchList()
    }
  }
}
</script>

<style lang="scss">
  .myCertSelect{
    margin-left: 20px;
  }
</style>
