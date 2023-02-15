<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="行业名称">
          <el-input clearable size="medium" placeholder="请输入行业名称" v-model="searchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="time"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </el-form-item>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="search" style="color: #5677DF" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" v-permission="add" plain size='medium' @click="handleAdd" icon="el-icon-plus" style="color: #5677DF">添加行业</el-button>
      </div>
      <TableList :table-data='tableData'
                 v-loading="isSubmitLoading"
                 @onHandleSelectionChange="handleSelectionChange"
                 :table-selection="tableSelection"
                 :table-label="tableHeader"
                 @onHandleRowClick="rowClick"
                 :table-option="tableOperation">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
  import {
    findAll,
    add,
    deleteIndustry,
    deleteAll,
  } from "@/api/industries.js"
  import {mapGetters} from 'vuex'
  import TableList from '@/components/table/tableListJiLu.vue'
  import Pagination from '@/components/table/Pagination.vue'
  import {getToken} from '@/utils/auth'
  import {getJsonTree} from '@/utils/tool'

  export default {
    components: {TableList, Pagination},
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
        batchDelete: ['none'],
        isSubmitLoading: false,
        deleteIds: [],
        tableData: [],
        tableHeader: [
          {id: false, type: 'color', label: '行业名称', list: 'name'},
          {id: false, type: 'color', label: '创建人', list: 'createById'},
          {id: false, type: 'color', label: '创建时间', list: 'createTime'}
        ],
        tableOperation: {
          label: '操作',
          width: '180px',
          value: 0,
          options: [
            {
              label: '编辑',
              key: 3,
              type: 'text',
              icon: 'el-icon-edit',
              state: true,
              method: (row) => {
                this.edit(row)
              },
              show: (row) => {
                return true;
              }
            },
            {
              label: '删除',
              key: 3,
              type: 'text',
              icon: 'el-icon-delete',
              state: true,
              method: (row) => {
                this.delete(row)
              },
              show: (row) => {
                return true;
              }
            }
          ]
        },
        time: [],
        searchItem: {
          name: ''
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
    created() {
      this.findPermission();
      this.fetchData();
    },
    methods: {
      rowClick(row) {
        this.handleDetail(row)
      },
      // 查询按钮权限
      findPermission() {
      },
      // 重置
      reset() {
        const that = this
        that.searchItem.name = '';
        that.searchItem.startTime = '';
        that.searchItem.endTime = '';
        that.time = [];
        Object.entries(that.searchItem).map((item, index) => {
          that.lastItem[item[0]] = that.searchItem[item[0]]
        })
      },
      // 获取列表
      async fetchData(type) {
        const that = this;
        let data = {};
        data = this.searchItem;
        data.page = this.currentPage;
        data.size = this.pageSize;
        if (type !== 'page') {
          if (that.time !== null && that.time !== '' && that.time.length > 0) {
            data.startTime = this.time[0];
            data.endTime = this.time[1];
          } else {
            data.startTime = '';
            data.endTime = '';
          }
        }
        Object.entries(that.searchItem).map((item, index) => {
          that.lastItem[item[0]] = that.searchItem[item[0]]
        });
        try {
          that.isSubmitLoading = true
          const res = await findAll(data)
          that.isSubmitLoading = false
          if (res.data.code === 1) {
            this.tableData = res.data.data.content;
            this.total = res.data.data.totalElements
          } else {
            this.$message.error(even.msg)
          }
        } catch (even) {
          this.$message.error(even.msg)
        }
      },
      //全选
      handleSelectionChange(vals) {
        const that = this
        that.deleteIds = []
        vals.map(function (v, k) {
          that.deleteIds.push(v.id)
        })
      },
      // 搜索
      search() {
        this.currentPage = 1
        this.$refs.page.Page(1)
        this.fetchData()
      },
      // 翻页
      pageChange(item) {
        let that = this
        this.pageSize = item.limit;
        this.currentPage = item.page;
        if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
          that.time = []
        }
        Object.entries(that.searchItem).map((item, index) => {
          that.searchItem[item[0]] = that.lastItem[item[0]]
        });
        this.fetchData('page');
      },
      // 详情
      handleDetail(data) {
        // const that = this;
        // that.$router.push({
        //   path: '/industries/info',
        //   query: {
        //     id: data.id,
        //     type: "info"
        //   }
        // })
      },
      // 行高
      cellStyle() {
        return {
          'font-size': '14px',
          height: '40px',
          padding: '0'
        }
      },
      // 头行高
      headerStyle() {
        return {
          'font-size': '14px',
          height: '40px',
          padding: '0',
          background: '#F5F7FA'
        }
      },

      async handleAdd(data) {
        const that = this;
        that.$router.push({
          path: '/industries/add',
          query: {
            id: data.id,
            type: "add"
          }
        })
      },

      async delete(row) {
        const that = this;
        that.$confirm('请确认是否删除?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.isSubmitLoading = true
          const response = await deleteIndustry(row.id)
          that.isSubmitLoading = false
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

      // 编辑
      edit(data) {
        const that = this;
        that.$router.push({
          path: '/industries/add',
          query: {
            id: data.id,
            type: "edit"
          }
        })
      },
    }
  }
</script>
<style lang="scss" scoped>
  /deep/ .el-button--primary.is-plain:focus, .el-button--primary.is-plain:hover {
    background: #ffffffff
  }

  /deep/ .el-form-item__label {
    color: #fff;
  }

  /deep/ .el-input--medium .el-input__inner {
    height: 30px;
    line-height: 30px;
  }

  /deep/ .el-date-editor .el-range__close-icon {
    line-height: 24px !important;
  }

  /deep/ .el-input__inner {
    height: 30px;
    line-height: 30px;
  }

  /deep/ .el-button--medium {
    padding: 7px 15px;
  }

  /deep/ .el-form-item {
    margin-bottom: 5px;
  }

  /deep/ .el-InputForm .el-range__icon {
    line-height: 25px !important;
  }

  /deep/ .el-input__icon .el-range__icon {
    line-height: 24px !important;
  }

  /deep/ .el-date-editor .el-range-separator {
    line-height: 24px !important;
  }

  .center {
    background: #fff;
    padding: 0px 15px;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 350px;
  }

  .main {
    background: #00000000;
    height: 100%;
    width: 100%;
    padding: 0 30px;
    min-height: 600px;
    margin-top: -240px;
    .el-InputForm {
      background: url('../../assets/image/filter.png');
      background-size: 100% 100%;
      padding: 15px 0 0 20px;
      min-height: 100px;
    }
  }

  .add {
    width: 25px;
    height: 25px;
    background: url('../../assets/image/add.png');
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
