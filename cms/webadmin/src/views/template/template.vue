<template>
  <el-main :v-loading="load">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
                    size="medium"
                    placeholder="请输入模板名称"
                    prefix-icon="el-icon-search"
                    v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="fetchData" icon="el-icon-search">搜索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="add" icon="el-icon-plus">添加</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading"
                 :table-label="tableHeader"
                 :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <Add ref="add" @Reload='fetchData'></Add>
  </el-main>
</template>

<script>
import {find,deleteById} from '@/api/template'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import Add from '@/views/repository/add.vue'
export default {
  components: { TableList, Pagination, Add },
  data() {
    return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '模板名称', list: 'name'},
        {id: false, type: '', label: '创建时间', list: 'createTime'}
      ],
      tableOpction: {
        label: '操作',
        width: '400px',
        value: 0,
        options: [
          {
            label: '数据模型',
            key: 0,
            type: 'primary',
            State: false,
            method: (row) => {
              this.dataField(row)
            }
          },
          {
            label: '编辑',
            key: 0,
            type: 'primary',
            State: false,
            method: (row) => {
              this.edit(row)
            }
          },{
            label: '删除',
            key: 0,
            type: 'danger',
            State: false,
            method: (row) => {
              this.deleteConfirm(row)
            }
          }]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        name: '',
        phone: '',
        userName: '',
        Timeframe: ''
      },
      lastItem: {
        name: '',
        phone: '',
        userName: '',
        Timeframe: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      load: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.load = true
      const data = {
        name: this.SearchItem.name,
        page: this.currentPage,
        size: this.pageSize
      }
      try {
        let res = await find(data)
        this.currentPage = '1'
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
        this.load = false
      } catch (even) {
        this.load = false
        this.$message.error(even.msg)
      }
    },

    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },

    add() {
      this.$router.push({
        path: '/template/templateExchange',
        query: {
          typeId:'1'
        },
      });
    },
    edit(row) {
      this.$router.push({
        path: '/template/templateExchange',
        query: {
          typeId:'2',
          templateId: row.id
        },
      });
    },

    deleteConfirm(row){
      this.$confirm('确认删除？')
        .then(_ => {
          this.delete(row);
        })
        .catch(_ => {});
    },
    async delete(row){
      const that = this
      that.loading = true
      const response = await deleteById({id:row.id});
      that.loading = false

      if (response.data.code === 1) {
        // 删除成功后 回调效果
        that.$message.success('删除成功')
        await that.fetchData()
      } else {
        // 删除失败后 回调失败信息
        that.$message.error(response.data.msg)
      }
    },

    async dataField(row){
      this.$router.push({
        path: '/template/dataField',
        query: {
          parentId:row.id
        },
      });
    }
  }
}
</script>
