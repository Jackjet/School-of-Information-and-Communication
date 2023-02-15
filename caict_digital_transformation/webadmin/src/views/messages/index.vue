<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="标题">
          <el-input clearable size="medium" placeholder="请输入消息标题" v-model="searchItem.title"></el-input>
        </el-form-item>

        <el-form-item label="创建时间">
          <el-date-picker v-model="time" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </el-form-item>

        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="search" style="color: #5677DF" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" plain size='medium' @click="handleAdd" icon="el-icon-plus" style="color: #5677DF">新增</el-button>
      </div>
      <TableList :table-data='tableData' v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction" @onHandleRowClick="rowClick"></TableList>
    </el-col>

    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>

    <el-dialog title="查看接收方" :visible.sync="receiverUsersDialogVisible" width="850px">
      <el-main class="dialog-main">
        <el-col :span="24">
          <el-form :inline="true">
            <el-form-item label="客户名称" class="font-color">
              <el-input clearable size="medium" placeholder="请输入客户名称" v-model="receiverUsersSearchItem.name"/>
            </el-form-item>

            <el-form-item label="联系人" class="font-color">
              <el-input clearable size="medium" placeholder="请输入联系人姓名" v-model="receiverUsersSearchItem.linkmanName"/>
            </el-form-item>

            <el-form-item style='margin-left: 15px;'>
              <el-button size='medium' @click="receiverUsersSearch" style="color: #5677DF" icon="el-icon-search">查询</el-button>
              <el-button size='medium' @click="receiverUsersReset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24">
          <TableList :table-data='receiverUsers'
                     v-loading="receiverUsersIsSubmitLoading"
                     :table-selection="receiverUsersTableSelection"
                     :table-label="receiverUsersTableHeader"
                     @onHandleRowClick="receiverUsersRowClick"
                     :table-option="receiverUsersTableOperation">
          </TableList>
        </el-col>
        <el-col :span='24'>
          <pagination ref="receiverUsersPage" :total="receiverUsersTotal" @pageChange="receiverUsersPageChange"></pagination>
        </el-col>
      </el-main>
    </el-dialog>

  </el-main>
</template>
<script>
import {findAll, findAllReceiver,} from "@/api/messages.js"
import {findAllNoPage,} from "@/api/industries.js"
import {mapGetters} from 'vuex'
import TableList from '@/components/table/tableListJiLu.vue'
import Pagination from '@/components/table/Pagination.vue'

export default {
  components: {TableList, Pagination},
  computed: {
    ...mapGetters([
      'permissions'
    ]),
  },
  data() {
    return {
      industriesData: {},
      drawer: false,
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        {id: false, type: 'color', label: '消息标题', list: 'title'},
        {id: false, type: 'color', label: '内容', list: 'text'},
        {id: false, type: 'color', label: '创建时间', list: 'createTime'}
      ],
      tableOpction: {
        label: '操作',
        width: '180px',
        value: 0,
        options: [
          {
            label: '查看接收方',
            key: 0,
            type: 'text',
            icon: 'el-icon-view',
            state: true,
            method: (row) => {
              this.receiverUsersDialogVisible = true;
              this.receiverUsersSearchItem.id = row.id;
              this.getReceiverUsers();
            },
            show: (row) => {
              return true;
            }
          }
        ]
      },
      time: [],
      searchItem: {
        title: '',
        startTime: '',
        endTime: '',
      },
      lastItem: {
        title: '',
        startTime: '',
        endTime: '',
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',

      receiverUsersDialogVisible: false,
      receiverUsers: [],

      receiverUsersTableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      receiverUsersTableOperation: {},
      receiverUsersIsSubmitLoading: false,
      receiverUsersTableHeader: [
        {id: false, type: 'color', label: '客户名称', list: 'name'},
        {id: false, type: 'color', label: '联系人', list: 'linkmanName'},
        {id: false, type: 'color', label: '联系电话', list: 'linkmanPhone'},
        {id: false, type: 'color', label: '所属行业', list: 'industries'},
      ],
      receiverUsersSearchItem: {
        id: '',
        name: '',
        linkmanName: '',
      },
      receiverUsersLastItem: {
        id: '',
        name: '',
        linkmanName: '',
      },
      receiverUsersTotal: 0,
      receiverUsersPageSize: '20',
      receiverUsersCurrentPage: '1',
    }
  },
  created() {
    this.findPermission()
    this.fetchIndustry()
    this.fetchData()
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
      that.searchItem.title = '';
      that.searchItem.startTime = '';
      that.searchItem.endTime = '';
      that.time = [];
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]]
      })
    },
    // 获取行业列表
    async fetchIndustry(type) {
      const that = this;
      try {
        that.isSubmitLoading = true
        const res = await findAllNoPage()
        if (res.data.code === 1) {
          this.industriesData = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.searchItem;
      data.page = this.currentPage;
      data.size = this.pageSize;
      if (type !== 'page') {
        if (that.time !== null && that.time !== '' && that.time.length > 0) {
          data.startTime = this.time[0];
          data.endTime = this.time[1]
        } else {
          data.startTime = '';
          data.endTime = ''
        }
      }
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await findAll(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          res.data.data.content.map(function (v, k) {
            if (v.text.length > 11) {
              v.text = v.text.substr(0, 11) + '...'
            }
          })
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //全选
    handleSelectionChange(vals) {
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
      this.pageSize = item.limit
      this.currentPage = item.page
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      Object.entries(that.searchItem).map((item, index) => {
        that.searchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData('page')
    },
    async handleAdd(data) {
      const that = this;
      that.$router.push({
        name: '消息添加',
        query: {
          type: "add"
        },
        params: {
          industriesData: that.industriesData
        }
      })
    },
    // 详情
    handleDetail(data) {
      const that = this
      that.$router.push({
        path: '/messages/info',
        query: {
          id: data.id,
          type: 'info'
        }
      })
    },

    async getReceiverUsers(id) {
      const that = this;

      let data = {};
      data = this.receiverUsersSearchItem;
      data.page = this.receiverUsersCurrentPage;
      data.size = this.receiverUsersPageSize;

      that.receiverUsersDialogVisible = true;

      Object.entries(that.receiverUsersSearchItem).map((item, index) => {
        that.receiverUsersLastItem[item[0]] = that.receiverUsersSearchItem[item[0]];
      });

      try {
        that.receiverUsersIsSubmitLoading = true;
        const res = await findAllReceiver(data);
        that.receiverUsersIsSubmitLoading = false;
        if (res.data.code === 1) {
          this.receiverUsers = res.data.data.content;
          for (let i = 0; i < that.receiverUsers.length; i++) {
            let industriesTemp = that.receiverUsers[i].industries;
            if (industriesTemp === null) {
              continue
            }
            let industryArray = industriesTemp.split(",")
            let industries = "";
            industryArray.map(function (value, key) {
              let item = that.industriesData[value]
              if (item !== undefined) {
                industries += "、" + item
              }
            });
            that.receiverUsers[i].industries = industries.substr(1)
          }

          this.receiverUsersTotal = res.data.data.totalElements;
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }

    },
    // 搜索
    receiverUsersSearch() {
      this.receiverUsersCurrentPage = 1;
      this.$refs.receiverUsersPage.Page(1);
      this.getReceiverUsers();

    },
    // 翻页
    receiverUsersPageChange(item) {
      let that = this;
      this.receiverUsersPageSize = item.limit;
      this.receiverUsersCurrentPage = item.page;
      Object.entries(that.receiverUsersSearchItem).map((item, index) => {
        that.receiverUsersSearchItem[item[0]] = that.receiverUsersLastItem[item[0]];
      });
      this.getReceiverUsers();
    },
    // 重置
    receiverUsersReset() {
      const that = this;
      that.receiverUsersSearchItem.name = '';
      that.receiverUsersSearchItem.linkmanName = '';
      Object.entries(that.receiverUsersSearchItem).map((item, index) => {
        that.receiverUsersLastItem[item[0]] = that.receiverUsersSearchItem[item[0]];
      })
    },
    receiverUsersRowClick(row) {
      const that = this;
      let routeData = that.$router.resolve({
        name: '客户详情',
        query: {
          id: row.id
        },
        params: {
          industriesData: that.industriesData
        }
      });
      window.open(routeData.href, '_blank');
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

.dialog-main {
  display: flex;
  flex-direction: column;
  background: #fff;
  height: 100%;
  width: 100%;
  margin: 0 auto;
  min-height: 400px;
  min-width: 800px;
  border-radius: 5px;
  padding: 10px 20px;
}

/deep/ .font-color .el-form-item__label {
  color: #606266 !important;
}

</style>
