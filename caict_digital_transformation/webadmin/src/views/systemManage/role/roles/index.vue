<template>
  <el-main class="main">
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item  label="角色权限名称">
          <el-input clearable
            size="medium"
            placeholder="请输入角色权限名称"
            v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="SearchNoteList" style="color: #5677DF" icon="el-icon-search">查询</el-button>
          <el-button  size='medium' @click="reset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" v-permission="add" plain size='medium' @click="handleAdd" icon="el-icon-plus" style="color: #5677DF">新增</el-button>
      </div>
      <TableList :table-data='tableData'
        v-loading="isSubmitLoading"
        @onHandleSelectionChange="handleSelectionChange"
        :table-selection="tableSelection"
        :table-label="tableHeader"
        :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import {
  roleFindAll,
  roleDelete
} from "@/api/role.js";
import { mapGetters } from 'vuex'
import TableList from '@/components/table/tableListJiLu.vue'
import Pagination from '@/components/table/Pagination.vue'
import { getToken } from '@/utils/auth'
import { getJsonTree } from '@/utils/tool'
import transmit from '@/utils/transmit'
export default {
  components: { TableList, Pagination},
  computed: {
  ...mapGetters([
    'permissions'
    ]),
  },
  data() {
      return {
      drawer: false,
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      add: ['none'],
      isSubmitLoading: false,
      DeletelistiD: [],
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '角色权限名称', list: 'name'},
        {id: false, type: '', label: '备注', list: 'remark'}
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 'role',
        options: [{
          label: '编辑',
          key: 0,
          type: 'text',
          icon: 'el-icon-edit-outline',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.handleEdit(row)
          },
          show: (row) => {
            return true;
          }
        },
        {
          label: '用户列表',
          key: 0,
          type: 'text',
          icon: 'el-icon-user',
          permission: ['none'],
          State: false,
          method: (row) => {
            this.handleUser(row)
          },
          show: (row) => {
            return true;
          }
        },
        {
          label: '权限分配',
          key: 0,
          type: 'text',
          icon: 'el-icon-guide',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.handleAssign(row)
          },
          show: (row) => {
            return true;
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'text',
          icon: 'el-icon-delete',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.handleDelete(row)
          },
          show: (row) => {
            return true;
          }
        },
      ]
      },
      SearchItem: {
        name: '',
      },
      lastItem: {
        name: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
    }
  },
  created () {
    this.fetchData()
    this.findPermission()
  },
  methods: {
     // 查询按钮权限
    findPermission() {
      const that = this
      let props = {
        edit: function (value) {
          that.tableOpction.options[0].permission = JSON.parse(JSON.stringify(value))
        },
        add: function (value) {
          that.add = JSON.parse(JSON.stringify(value))
        },
        delete: function (value) {
          that.tableOpction.options[3].permission = JSON.parse(JSON.stringify(value))
        },
        permissionAssignment: function (value) {
          that.tableOpction.options[2].permission = JSON.parse(JSON.stringify(value))
        },
        userList: function (value) {
          that.tableOpction.options[1].permission = JSON.parse(JSON.stringify(value))
        }
      }
      if(this.permissions.length > 0){
        this.permissions.map(function(v, k){
          if(v['role']){
            for (let i in v['role']) {
              props[i](v['role'][i])
            }
          }
        })
      }
    },
    // 重置
    reset() {
      const that = this
      that.SearchItem.name = '',
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData() {
      const that = this
      let data = {}
      data = this.SearchItem,
      data.page = this.currentPage,
      data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        const res = await roleFindAll(data)
        if(res.data.code === 1){
          this.tableData = res.data.data.content
          this.total =  res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //全选
    handleSelectionChange(vals) {
      const that = this
      that.DeletelistiD = []
      vals.map(function(v, k){
        that.DeletelistiD.push(v.id)
      })
    },
    // 搜索
    SearchNoteList() {
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData()
    },
    handleEdit(data) {
      const that = this
      that.$router.push({
        path: '/systemManage/role/roleAdd',
        query: {
          id: data.id,
          name: data.name,
          remark: data.remark,
          type:'编辑'
        }
      })
    },
    // 用户列表
    handleUser(data) {
      const that = this
      that.$router.push({
        path: '/systemManage/role/users',
        query: {
          id: data.id
        }
      })
    },
    // 添加
    handleAdd(data){
      const that = this
      that.$router.push({
        path: '/systemManage/role/roleAdd',
        query: {
          type:'新增'
        }
      })
    },
    // 添加
    handleAssign(data){
      const that = this
      that.$router.push({
        path: '/systemManage/role/assignPremission',
        query: {
          id: data.id,
          name: data.name
        }
      })
    },
    // 行高
    cellStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },
    // 头行高
    headerStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },
    // 删除当前数据 重载列表
    async handleDelete (data) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.Loading = true
        const response = await roleDelete(data.id)
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
<style lang="scss" scoped>
  /deep/ .el-button--primary.is-plain:focus, .el-button--primary.is-plain:hover{
    background: #ffffffff
  }
  /deep/ .el-form-item__label{
    color: #fff;
  }
  /deep/ .el-input--medium .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-button--medium{
    padding: 7px 15px;
  }
  /deep/ .el-form-item{
    margin-bottom: 5px;
  }
  .center{
    background: #fff;
    padding: 0px 15px;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 350px;
  }
  .main{
    background: #00000000;
    height: 100%;
    width: 100%;
    padding: 0 30px;
    min-height: 600px;
    margin-top: -240px;
    .el-InputForm{
      background:url('../../../../assets/image/filter.png');
      background-size: 100% 100%;
      padding:15px 0 0 20px;
      min-height: 100px;

    }
  }
  .add{
    width: 25px;
    height: 25px;
    background:url('../../../../assets/image/add.png');
  }
  .main-top{

  }
  .work-query {
    display: flex;
    align-items: center;
    padding: 20px;
  }
  .table-list {
    padding: 20px 20px 0 20px;
    box-sizing: border-box;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 400px;
  }
  .table-pagination {
    display: flex;
    justify-content: flex-end;
    padding: 20px;
  }
</style>
