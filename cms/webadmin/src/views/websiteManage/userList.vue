<template>
  <el-main :v-loading="load">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
                    size="medium"
                    placeholder="请输入角色名称"
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
import {findAllUser,deleteById,resetPassword,deleteUser} from '@/api/websiteManage'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import Add from '@/views/websiteManage/userAdd.vue'
export default {
  components: { TableList, Pagination, Add },
  data() {
    return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '用户名称', list: 'realName'},
        {id: false, type: '', label: '性别', list: 'sex'},
        {id: false, type: '', label: '角色', list: 'roleName'},
        {id: false, type: '', label: '邮箱', list: 'email'},
        {id: false, type: '', label: '联系电话', list: 'tel'},
        {id: false, type: '', label: '创建时间', list: 'createTime'}
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [{
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
        },{
          label: '重置密码',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.resetPassword(row)
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
        let res = await findAllUser(data)
        let content =  res.data.data.content
        content.forEach(function(value,i){
          if(value.sex === '0') {
            value.sex = '男'
          }else if(value.sex === '1') {
            value.sex = '女'
          }
        })
        this.tableData = content
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
      this.$refs.add.initial('添加');
    },
    edit(row) {
      this.$refs.add.initial('编辑',row.id);
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
      const response = await deleteUser({id:row.id});
      that.loading = false

      if (response.data.code === 1) {
        // 删除成功后 回调效果
        that.$message.success(response.data.msg)
        that.fetchData()
      } else {
        // 删除失败后 回调失败信息
        that.$message.error(response.data.msg)
      }
    },

    async resourceFile(row){
      this.$router.push({
        path: '/repository/resourceFile',
        query: {
          repositoryId: row.id,
          parentId:''
        },
      });
    },

    async resetPassword(row){
      const that = this
      let response = await resetPassword({id:row.id})
      if (response.data.code === 1) {
        that.$message.success(response.data.msg)
        that.fetchData()
      } else {
        that.$message.error(response.data.msg)
      }
    }
  }
}
</script>
