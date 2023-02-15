<template>
  <!-- 调用表格 -->
  <el-main v-loading="Loading">
    <el-form :inline="true" class='el-InputForm'>
      <el-form-item>
        <el-button
          type="primary"
          size='medium'
          icon="el-icon-plus"
          style="margin-left: 20px;"
          @click="onAddClicked">添加
        </el-button>
      </el-form-item>
    </el-form>
    <tableForm
      :table-data='table.data'
      :table-selection="table.selection"
      :table-label="table.header"
      :table-option="table.option">
    </tableForm>
    <!-- 调用操作弹层 -->
    <theDialog
      :modal-obj='modalObj'
      ref="callDetail"
      @Reload='searchList'>
    </theDialog>
  </el-main>
</template>
<script>
import tableForm from '../../integration/tableList'
import theDialog from './edit.vue'
export default {
  components: { tableForm, theDialog },
  data () {
    return {
      Loading: false,
      modalObj: '',
      /**
       * 列表配置
       * data: 数据
       * selection: 选择框
       * header: 表头
       * total: 总数
       * currentPage: 当前页面
       * pageSize: 每页展示数
       */
      table: {
        data: [],
        selection: {
          key: false,
          type: 'selection'
        },
        header: [{
          id: false,
          type: '',
          label: '分组名称',
          list: 'name'
        }, {
          id: false,
          type: '',
          label: '题目数量', // 需要字典赋值
          list: 'questionNumber'
        }],
        option: {
          label: '操作',
          value: '考试分组',
          options: [{
            label: '编辑',
            key: 0,
            type: 'text',
            icon: 'el-icon-edit',
            State: false,
            method: (row) => {
              this.doEditRow(row)
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
        }
      }
    }
  },

  methods: {
    init () {
      this.searchList()
    },
    /**
     * 获取列表方法
     */
    async searchList () {
      const that = this
      try {
        const url = that.Interface.courseManage.questiongroup.find
        that.Loading = true
        const response = await this.request.dataGet(that, url, {})
        that.Loading = false
        if (response.data.code !== 0) {
          that.table.data = response.data.data
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
    doEditRow (row) {
      this.modalObj = '编辑'
      this.$refs.callDetail.init(true, row)
    },

    /**
     * 添加点击事件
     */
    onAddClicked () {
      this.modalObj = '添加'
      this.$refs.callDetail.init(false, {})
    },

    /**
     * 删除点击事件
     */
    doDeleteRow (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        // if (row.relatedContent > 0) {
        //   that.$message.error('存在关联内容，不允许删除！')
        //   return false
        // }
        let url = this.Interface.courseManage.questiongroup.delete + '?id=' + row.id
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
    }
  }
}
</script>

<style lang="scss">
  .myCertSelect{
    margin-left: 20px;
  }
</style>
