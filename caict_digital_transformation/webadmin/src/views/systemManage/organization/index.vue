<template>
  <el-main class="main">
    <el-col :span="24" class="center">
      <el-table
        v-loading="isSubmitLoading"
        class="table-list"
        :cell-style="cellStyle"
        :header-cell-style="headerStyle"
        :data="tableData"
        style="width: 100%"
        row-key="id"
        stripe="true"
        default-expand-all='true'
        lazy
        :load="load"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column
          prop="name"
          align="center"
          label="组织机构名称"
          >
        </el-table-column>
        <el-table-column
          prop="levelMsg"
          label="层级"
          align="center"
         >
        </el-table-column>
        <el-table-column
          prop="seq"
          align="center"
          label="排序">
          <template slot-scope="scope">
            <template v-if="scope.row.seq === -1">
              <el-button type="text" size="medium" icon="el-icon-top" @click="organizationSeq(0, scope.row)"></el-button>
            </template>
            <template v-else-if="scope.row.seq === 0 && scope.row.name!== '总部' ">
              <el-button type="text" size="medium" icon="el-icon-bottom" @click="organizationSeq(1, scope.row)"></el-button>
            </template>
            <template v-else-if="scope.row.seq !== -1 && scope.row.seq !== 0 && scope.row.seq !== -2">
              <el-button type="text" size="medium" icon="el-icon-top" @click="organizationSeq(0, scope.row)"></el-button>
              <el-button type="text" size="medium" icon="el-icon-bottom" @click="organizationSeq(1, scope.row)"></el-button>
            </template> 
            <!-- <span v-if="scope.row.level !==0">{{上移}}</span> -->
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="medium"
              icon="el-icon-plus"
              v-permission="add"
              @click="handleAdd(scope.row)">新增</el-button>
            <el-button
              type="text"
              size="medium"
              icon="el-icon-edit-outline"
              v-permission="edit"
              @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              size="medium"
              type="text"
              icon="el-icon-delete"
              v-permission="del"
               v-if="scope.row.name!== '总部'"
              @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-main>
</template>

<script>
import {
  organizationFindAll,
  organizationUpdateSeq,
  organizationDelete
} from "@/api/organization.js";
import { mapGetters } from 'vuex'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import { getToken } from '@/utils/auth'
import { getJsonTree } from '@/utils/tool'
import { sessionStorageSet, sessionStorageGet } from '@/utils/storage' 
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
      add: ['none'],
      edit: ['none'],
      del: ['none'],
      tableData: [],
      expand: [],
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        name: '',
        type: ''
      },
      lastItem: {
        name: '',
        type: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
    }
  },
  created () {
    this.expand.push(sessionStorageGet('expand')) 
    this.expand.push(sessionStorageGet('parent_expand'))
    this.fetchData()
    this.findPermission()
  },
  methods: {
    // 查询按钮权限
    findPermission() {
      const that = this
      let props = {
        edit: function (value) {
          that.edit = JSON.parse(JSON.stringify(value))
        },
        add: function (value) {
          that.add = JSON.parse(JSON.stringify(value))
        },
        delete: function (value) {
          that.del = JSON.parse(JSON.stringify(value))
        }
      }
      if(this.permissions.length > 0){
        this.permissions.map(function(v, k){
          if(v['organization']){
            for (let i in v['organization']) {
              props[i](v['organization'][i])
            }
          }
        })
      }
    },
    async fetchData() {
      const data = {}
      const that = this
      try {
        that.isSubmitLoading = true  
        const res = await organizationFindAll(data)
        that.isSubmitLoading = false  
        if(res.data.code === 1){
          this.tableData = []
          let datas = []
          datas.push(res.data.data)
          this.tableData = getJsonTree(datas, '')    
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 上下移动
    async organizationSeq(type, row) {
      const that = this
      const data = {
        action: type,
        id: row.id
      }
      try {
        that.isSubmitLoading = true  
        const res = await organizationUpdateSeq(data)
        that.isSubmitLoading = false  
        if(res.data.code === 1){
          this.fetchData()
        } else {
          that.isSubmitLoading = false 
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 添加
    handleAdd(data){
      const that = this
      that.$router.push({ 
        path: '/systemManage/organization/organizationAdd',
        query: {
          id: data.id,
          name: data.name,
          level: data.level,
          levelMsg: data.levelMsg,
          type:'新增'
        }
      })
      sessionStorageSet('parent_expand', data.parentId)
      sessionStorageSet('expand', data.id)
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
  
    handleEdit: function (data) {
      const that = this
      that.$router.push({ 
        path: '/systemManage/organization/organizationAdd',
        query: {
          id: data.id,
          parentId: data.parentId,
          parentName: data.parentName,
          name: data.name,
          level: data.level,
          levelMsg: data.levelMsg,
          type:'编辑'
        }
      })
      sessionStorageSet('parent_expand', data.parentId)
      sessionStorageSet('expand', data.id)
    },
    // 删除当前单条数据 重载列表
    handleDelete (data) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.Loading = true
        const response = await organizationDelete(data.id)
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
/deep/ .el-button+.el-button{
  margin-left: 5px;
}
.main{
  background: #00000000;
  height: 100%;
  width: 100%;
  padding: 0 30px;
  min-height: 600px;
  margin-top: -260px;
  .el-InputForm{
    background:url('../../../assets/image/filter.png');
    background-size: 100% 100%;
    padding:15px 0 0 20px;
    min-height: 100px;
  }
}
.add{
  width: 25px;
  height: 25px;
  background:url('../../../assets/image/add.png');
}
.main-top{

}
.work-query {
  display: flex;
  align-items: center;
  padding: 20px;
}
.table-list{
  margin-top: 15px;
}
.center{
  background: #fff;
  padding: 20px 15px;
  margin-top: 40px;
  border-radius: 5px;
  min-height: 550px;
 }
.table-pagination {
  display: flex;
  justify-content: flex-end;
  padding: 20px;
}
</style>