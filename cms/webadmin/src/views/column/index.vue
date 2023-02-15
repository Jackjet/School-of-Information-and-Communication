<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
                    size="medium"
                    placeholder="请输入栏目名称"
                    prefix-icon="el-icon-search"
                    v-model="SearchItem.columnName">
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
      <el-table
        :data="tableData"
        style="width: 100%"
        row-key="id"
        lazy
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren1'}"
        v-loading="loading"
        indent="64">
        <el-table-column
          prop="columnName"
          label="栏目名称"
          align='left'>
        </el-table-column>
        <el-table-column
          prop="templateName"
          label="所属模板"
          align='center'>
        </el-table-column>
        <el-table-column
          prop="power"
          label="排序"
          align='center'>
          <template slot-scope="scope">
            <el-input v-model="scope.row.power" style="width: 40px;" @blur="updatePower(scope.row)"></el-input>
          </template>
        </el-table-column>

        <el-table-column fit align='center'
                         :width='tableOption.width'
                         :label="tableOption.label">
          <template slot-scope="scope">
            <el-button  size="mini"  v-for="(value, item) in tableOption.options"
                        :key='item'
                        :type="value.type"
                        :plain='value.State'
                        :icon="value.icon"
                        @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <Add ref="add" @Reload='fetchData'></Add>
  </el-main>
</template>

<script>
import { findAll,findById,insert,update,deleteById,updatePower,findAllRepository,fileFindAll } from '@/api/column'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import Add from '@/views/column/add.vue'
export default {
  components: { TableList, Pagination,Add },
  data() {
    return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '栏目名称', list: 'columnName'},
        {id: false, type: '', label: '所属模板', list: 'templateName'},
        {id: false, type: '', label: '排序', list: 'power'},
      ],
      tableOption: {
        label: '操作',
        width: '400px',
        value: 0,
        options: [{
          label: '编辑',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.edit(row)
          }
        },
        {
          label: '增加子栏目',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.addChild(row)
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'danger',
          State: false,
          method: (row) => {
            this.delete(row)
          }
        },
        {
          label: '预览',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.preview(row)
          }
        }]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        columnName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      loading: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      const data = {
        columnName:this.SearchItem.columnName
      }
      try {
        this.loading = true
        let res = await findAll(data)
        this.loading = false
        this.tableData = res.data.data
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    add() {
      this.$refs.add.initial('添加');
    },
    edit(row){
      this.$refs.add.initial('编辑',row.columnId);
    },
    addChild(row){
      this.$refs.add.initial('增加子栏目',row.columnId,row.columnName);
    },
    async delete(row){
      const that = this
      let response = await deleteById({id:row.columnId})
      if (response.data.code === 1) {
        // 删除成功后 回调效果
        that.$message.success(response.data.msg)
        that.fetchData()
      } else {
        // 删除失败后 回调失败信息
        that.$message.error(response.data.msg)
      }
    },
    async updatePower(row){
      const that = this
      let data = {
        id: row.columnId,
        power: row.power
      }
      let response = await updatePower(data)
      if (response.data.code === 0) {
        that.$message.error(response.data.msg)
      }
      that.fetchData()
    },
    preview(row){
      let link = process.env.VUE_APP_BASE_API + 'web/view/index.html'
      window.open(link, '_blank')
    }
  }
}
</script>

<style scoped lang="scss">
  .el_tab_alage {
    width: 98%;
    border:1px solid #f0f0f0;
    margin: {
      left: 1%;
      bottom: 20px;
      top: 10px;
    }
    .el-table__row {
      @media screen and (max-width: 1020px){
        .el-button {
          margin: {
            top: 0px !important;
            left: 10px !important;
            bottom:0px !important;
            right: 0px !important;
          }
        }
      }
      @media screen and (max-width: 1360px){
        .el-button + .el-button{
          margin: {
            top: 0px !important;
            bottom:0px !important;
            right: 0px !important;
          }
        }
      }
    }
  }
</style>
