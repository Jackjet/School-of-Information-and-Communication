<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="活动主题">
          <el-input clearable
                    size="medium"
                    placeholder="请输入活动主题名称"
                    v-model="searchItem.title">
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
        <el-form-item label="活动状态">
          <el-select clearable v-model="searchItem.status" placeholder="请选择">
            <el-option key="-1" label="请选择" value=""/>
            <el-option key="0" label="未开始" value="0"/>
            <el-option key="1" label="进行中" value="1"/>
            <el-option key="2" label="已结束" value="2"/>
          </el-select>
        </el-form-item>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="search" style="color: #5677DF" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" plain size='medium' @click="handleAdd" icon="el-icon-plus" style="color: #5677DF">添加活动</el-button>
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

    <el-dialog title="参与的客户" :visible.sync="joinUsersDialogVisible" width="850px">
      <el-main class="dialog-main">
        <el-col :span="24">
          <el-form :inline="true">
            <el-form-item label="客户名称" class="font-color">
              <el-input clearable size="medium" placeholder="请输入客户名称" v-model="joinUsersSearchItem.name"/>
            </el-form-item>

            <el-form-item label="联系人" class="font-color">
              <el-input clearable size="medium" placeholder="请输入联系人姓名" v-model="joinUsersSearchItem.linkmanName"/>
            </el-form-item>

            <el-form-item style='margin-left: 15px;'>
              <el-button size='medium' @click="joinUsersSearch" style="color: #5677DF" icon="el-icon-search">查询</el-button>
              <el-button size='medium' @click="joinUsersReset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24">
          <TableList :table-data='joinUsers'
                     v-loading="joinUsersIsSubmitLoading"
                     :table-selection="joinUsersTableSelection"
                     :table-label="joinUsersTableHeader"
                     @onHandleRowClick="joinUsersRowClick"
                     :table-option="joinUsersTableOperation">
          </TableList>
        </el-col>
        <el-col :span='24'>
          <pagination ref="joinUsersPage" :total="joinUsersTotal" @pageChange="joinUsersPageChange"></pagination>
        </el-col>
      </el-main>
    </el-dialog>

  </el-main>
</template>
<script>
import {
  findAll,
  add,
  deleteActivity,
  deleteAll,
  findUsersByActivityId,
} from "@/api/activities.js"
import {
  findAllNoPage,
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
      // 'permissions'
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
      deleteIds: [],
      tableData: [],
      tableHeader: [
        {id: false, type: 'color', label: '活动主题', list: 'title'},
        {id: false, type: 'color', label: '活动时间', list: 'time'},
        {id: false, type: 'color', label: '活动状态', list: 'status'},
        {id: false, type: 'color', label: '创建时间', list: 'createTime'}
      ],
      tableOperation: {
        label: '操作',
        width: '180px',
        value: 0,
        options: [
          {
            label: '查看参与客户',
            key: 0,
            type: 'text',
            icon: 'el-icon-view',
            state: true,
            method: (row) => {
              this.joinUsersDialogVisible = true;
              this.joinUsersSearchItem.id = row.id;
              this.getJoinUsers();
            },
            show: (row) => {
              return row.isJoinOpt;
            }
          },
          {
            label: '编辑',
            key: 1,
            type: 'text',
            icon: 'el-icon-edit',
            state: true,
            method: (row) => {
              this.edit(row)
            },
            show: (row) => {
              return row.isEditOpt;
            }
          },
          {
            label: '删除',
            key: 2,
            type: 'text',
            icon: 'el-icon-delete',
            state: true,
            method: (row) => {
              this.delete(row)
            },
            show: (row) => {
              return row.isEditOpt;
            }
          }
        ]
      },
      time: [],
      searchItem: {
        title: '',
        status: ''
      },
      lastItem: {
        title: '',
        status: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',

      joinUsersDialogVisible: false,
      joinUsers: [],

      joinUsersTableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      joinUsersTableOperation: {},
      joinUsersIsSubmitLoading: false,
      joinUsersTableHeader: [
        {id: false, type: 'color', label: '客户名称', list: 'name'},
        {id: false, type: 'color', label: '联系人', list: 'linkmanName'},
        {id: false, type: 'color', label: '联系电话', list: 'linkmanPhone'},
        {id: false, type: 'color', label: '所属行业', list: 'industries'},
      ],
      joinUsersSearchItem: {
        id: '',
        name: '',
        linkmanName: '',
      },
      joinUsersLastItem: {
        id: '',
        name: '',
        linkmanName: '',
      },
      joinUsersTotal: 0,
      joinUsersPageSize: '20',
      joinUsersCurrentPage: '1',
    }
  },
  created() {
    this.findPermission();
    this.fetchIndustry();
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
      that.searchItem.title = '';
      that.searchItem.status = '';

      that.searchItem.approveStatus = '';
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
          res.data.data.content.map(function (v, k) {
            //活动状态：0:未开始、1:进行中、2:已结束
            let startTime = new Date(v.startTime);
            let endTime = new Date(v.endTime);
            let nowTime = new Date();
            if (nowTime < startTime) {
              v.status = "未开始";
              v.isJoinOpt = false;
              v.isEditOpt = true;
              v.isDelOpt = true;

            } else if (nowTime > endTime) {
              v.status = "已结束";
              v.isJoinOpt = true;
              v.isEditOpt = false;
              v.isDelOpt = false;

            } else {
              v.status = "进行中";
              v.isJoinOpt = true;
              v.isEditOpt = false;
              v.isDelOpt = false;

            }

            v.time = v.startTime + "至" + v.endTime;
          });
          this.tableData = res.data.data.content;
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
      const that = this;
      that.$router.push({
        path: '/activities/info',
        query: {
          id: data.id,
          type: "info"
        }
      })
    },

    async handleAdd(data) {
      const that = this;
      that.$router.push({
        path: '/activities/add',
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
        const response = await deleteActivity(row.id)
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
        path: '/activities/add',
        query: {
          id: data.id,
          type: "edit"
        }
      })
    },

    async getJoinUsers() {
      const that = this;

      let data = {};
      data = this.joinUsersSearchItem;
      data.page = this.joinUsersCurrentPage;
      data.size = this.joinUsersPageSize;

      that.joinUsersDialogVisible = true;

      Object.entries(that.joinUsersSearchItem).map((item, index) => {
        that.joinUsersLastItem[item[0]] = that.joinUsersSearchItem[item[0]];
      });

      try {
        that.joinUsersIsSubmitLoading = true;
        const res = await findUsersByActivityId(data);
        that.joinUsersIsSubmitLoading = false;
        if (res.data.code === 1) {
          this.joinUsers = res.data.data.content;
          for (let i = 0; i < that.joinUsers.length; i++) {
            let industriesTemp = that.joinUsers[i].industries;
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
            that.joinUsers[i].industries = industries.substr(1)
          }

          this.joinUsersTotal = res.data.data.totalElements;
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }

    },
    // 搜索
    joinUsersSearch() {
      this.joinUsersCurrentPage = 1;
      this.$refs.joinUsersPage.Page(1);
      this.getJoinUsers();

    },
    // 翻页
    joinUsersPageChange(item) {
      let that = this;
      this.joinUsersPageSize = item.limit;
      this.joinUsersCurrentPage = item.page;
      Object.entries(that.joinUsersSearchItem).map((item, index) => {
        that.joinUsersSearchItem[item[0]] = that.joinUsersLastItem[item[0]];
      });
      this.getJoinUsers();
    },
    // 重置
    joinUsersReset() {
      const that = this;
      that.joinUsersSearchItem.name = '';
      that.joinUsersSearchItem.linkmanName = '';
      Object.entries(that.joinUsersSearchItem).map((item, index) => {
        that.joinUsersLastItem[item[0]] = that.joinUsersSearchItem[item[0]];
      })
    },
    joinUsersRowClick(row) {
      const that = this;
      let routeData = that.$router.resolve({
        name: '客户详情',
        query: {
          id: row.userId
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
