<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="客户名称">
          <el-input clearable
                    size="medium"
                    placeholder="请输入客户名称"
                    v-model="searchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item label="联系人">
          <el-input clearable
                    size="medium"
                    placeholder="请输入联系人姓名"
                    v-model="searchItem.linkmanName">
          </el-input>
        </el-form-item>
        <el-form-item label="客户来源">
          <el-input clearable
                    size="medium"
                    placeholder="请输入客户来源"
                    v-model="searchItem.source">
          </el-input>
        </el-form-item>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="search" style="color: #5677DF" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
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

    <el-dialog title="重置密码" :visible.sync="resetPasswordDialogVisible" width="40%">
      <el-form :model="resetPasswordForm" :rules="resetPasswordFormRules" ref="resetPasswordForm" label-width="150px">
        <el-form-item class="span-center" label="客户名称:" :label-width="this.formLabelWidth" style="color: #606266;">
          <span class="span">{{ resetPasswordForm.name }}</span>
        </el-form-item>

        <el-form-item class="font-color" label="新密码:" prop="password" :label-width="this.formLabelWidth">
          <el-input v-model="resetPasswordForm.password" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetPasswordDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="resetPwd">确 定</el-button>
      </div>
    </el-dialog>

  </el-main>
</template>
<script>
import {
  findAll,
  deleteUser,
  deleteAll,
  resetPassword,
} from "@/api/customer.js"
import {
  findAllNoPage,
} from "@/api/industries.js"
import {mapGetters} from 'vuex'
import TableList from '@/components/table/tableListJiLu.vue'
import Pagination from '@/components/table/Pagination.vue'
import {getToken} from '@/utils/auth'
import {getJsonTree} from '@/utils/tool'
import md5 from 'js-md5'

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
      deleteIds: [],
      tableData: [],
      tableHeader: [
        {id: false, type: 'color', label: '客户名称', list: 'name'},
        {id: false, type: 'color', label: '联系人', list: 'linkmanName'},
        {id: false, type: 'color', label: '联系电话', list: 'linkmanPhone'},
        {id: false, type: 'color', label: '所属行业', list: 'industries'},
        {id: false, type: 'color', label: '客户来源', list: 'source'}
      ],
      tableOperation: {
        label: '操作',
        width: '180px',
        value: 0,
        options: [
          {
            label: '重置密码',
            key: 0,
            type: 'text',
            icon: 'el-icon-edit-outline',
            state: true,
            method: (row) => {
              this.resetPasswordDialogVisible = true;
              this.resetPasswordForm.id = row.id;
              this.resetPasswordForm.name = row.name;
            },
            show: (row) => {
              return true;
            }
          },
          {
            label: '查看问卷',
            key: 0,
            type: 'text',
            icon: 'el-icon-view',
            state: true,
            method: (row) => {
              this.queryQuestionnaire(row)
            },
            show: (row) => {
              return true;
            }
          }
        ]
      },
      time: '',
      searchItem: {
        name: '',
        linkmanName: '',
        source: ''
      },
      lastItem: {
        name: '',
        linkmanName: '',
        source: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
      formLabelWidth: "120px",

      resetPasswordForm: {
        id: "",
        name: '',
        password: '',
      },
      resetPasswordDialogVisible: false,
      resetPasswordFormRules: {
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ]
      },
    }
  },
  created() {
    this.findPermission();
    this.fetchIndustry();
    this.fetchData();
  },
  methods: {
    rowClick(row) {
      this.handleDetail(row);
    },
    // 查询按钮权限
    findPermission() {
    },
    // 重置
    reset() {
      const that = this;
      that.searchItem.name = '';
      that.searchItem.linkmanName = '';
      that.searchItem.source = '';
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]];
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
    async fetchData() {
      const that = this;
      let data = {};
      data = this.searchItem;
      data.page = this.currentPage;
      data.size = this.pageSize;
      if (that.time.length > 0) {
        data.startTime = this.time[0];
        data.endTime = this.time[1];
      }
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]];
      });
      try {
        that.isSubmitLoading = true
        const res = await findAll(data);
        that.isSubmitLoading = false;

        if (res.data.code === 1) {
          that.tableData = res.data.data.content;
          for (let i = 0; i < that.tableData.length; i++) {
            let industriesTemp = that.tableData[i].industries;
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
            that.tableData[i].industries = industries.substr(1)
          }

          this.total = res.data.data.totalElements;
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    queryQuestionnaire(row) {
      window.open(process.env.VUE_APP_BASE_API + "/questionnaire/index.html?id=" + row.id + "&token=" + getToken(), '_blank');
    },
    //全选
    handleSelectionChange(vals) {
      const that = this;
      that.deleteIds = [];
      vals.map(function (v, k) {
        that.deleteIds.push(v.id);
      })
    },
    // 搜索
    search() {
      this.currentPage = 1;
      this.$refs.page.Page(1);
      this.fetchData();
    },
    // 翻页
    pageChange(item) {
      let that = this;
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(that.searchItem).map((item, index) => {
        that.searchItem[item[0]] = that.lastItem[item[0]];
      });
      this.fetchData();
    },
    // 详情
    handleDetail(data) {
      const that = this;
      that.$router.push({
        name: '客户详情',
        query: {
          id: data.id
        },
        params: {
          industriesData: that.industriesData
        }
      })
    },

    resetPwd() {
      this.$refs["resetPasswordForm"].validate((valid) => {
        if (!valid) {
          return false;
        }
        this.loading = true;
        // 取值
        const that = this;

        let data = {};
        data.id = this.resetPasswordForm.id;
        data.plaintext = this.resetPasswordForm.password;
        data.password = md5(this.resetPasswordForm.password);

        // 重置密码
        resetPassword(data).then((res) => {
          if (res.data.code === 1) {
            that.$message.success('密码重置成功');
            that.resetResetPasswordForm('resetPasswordForm');
          } else {
            that.$message.error(res.data.msg);
          }
          that.loading = false;
          that.resetPasswordDialogVisible = false;
        });
      });
    },
    resetResetPasswordForm(resetPasswordForm) {
      this.$refs["resetPasswordForm"].resetFields();
      this.resetPasswordForm.password = '';
    },

    async delete(row) {
      const that = this;
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true;
        const response = await deleteUser(row.id);
        that.isSubmitLoading = false;
        if (response.data.code === 1) {
          that.$message.success('删除成功');
          this.fetchData();
        } else {
          that.$message.error(response.data.msg);
        }
      }).catch(() => {
        return false
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

.center {
  background: #fff;
  padding: 0px 15px;
  margin-top: 20px;
  border-radius: 5px;
  min-height: 380px;
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
    min-height: 70px;
  }
}

.add {
  width: 25px;
  height: 25px;
  background: url('../../assets/image/add.png');
}

.main-top {

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

.span {
  margin-left: -120px;
}

.span-center {
  display: flex;
  align-items: center;
}

/deep/ .span-center .el-form-item__label {
  color: #606266 !important;
}

/deep/ .font-color .el-form-item__label {
  color: #606266 !important;
}

</style>
