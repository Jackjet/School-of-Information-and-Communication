<template>
  <el-main class="main">
    <div><h4>{{ title }}</h4></div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">

        <el-form-item class="span-center" label="消息标题:" prop="title" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' placeholder="请输入消息标题" size="medium" v-model="ruleForm.title"/>
        </el-form-item>

        <el-form-item label="消息内容:" prop="text" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="请输入消息内容" clearable size="medium" v-model="ruleForm.text"/>
        </el-form-item>

        <el-form-item label="接收方:" prop="selectedUserIds" :label-width="this.formLabelWidth">
          <el-button size="medium" type="primary" @click="selectUser()">{{ selectUsersBtn }}</el-button>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="resetForm('ruleForm')">返回列表</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm')">确 定</el-button>
        </div>
      </el-form>
    </el-col>

    <el-dialog title="选择接收方" :visible.sync="selectUsersDialogVisible" width="850px" @close='closeDialog'>
      <el-main class="dialog-main">
        <el-col :span="24">
          <el-form :inline="true">
            <el-form-item label="客户名称" class="font-color">
              <el-input clearable size="medium" placeholder="请输入客户名称" v-model="selectUsersSearchItem.name"/>
            </el-form-item>

            <el-form-item label="联系人" class="font-color">
              <el-input clearable size="medium" placeholder="请输入联系人姓名" v-model="selectUsersSearchItem.linkmanName"/>
            </el-form-item>

            <el-form-item style='margin-left: 15px;'>
              <el-button size='medium' @click="selectUsersSearch" style="color: #5677DF" icon="el-icon-search">查询</el-button>
              <el-button size='medium' @click="selectUsersReset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>

        <el-col :span="24">
          <TableList :table-data='selectUsers'
                     v-loading="selectUsersIsSubmitLoading"
                     :table-selection="selectUsersTableSelection"
                     :table-label="selectUsersTableHeader"
                     @onHandleRowClick="selectUsersRowClick"
                     @onHandleSelectionChange="handleSelectionChange"
                     :table-option="selectUsersTableOperation"/>
        </el-col>

        <el-col :span='24'>
          <pagination ref="selectUsersPage" :total="selectUsersTotal" @pageChange="selectUsersPageChange"></pagination>
        </el-col>
      </el-main>
    </el-dialog>

  </el-main>

</template>

<script>
import {
  add,
} from "@/api/messages.js";
import {
  findAll,
} from "@/api/customer.js"
import {getToken} from '@/utils/auth'
import TableList from '@/components/table/tableListJiLu.vue'
import Pagination from '@/components/table/Pagination.vue'
import {sessionStorageGet} from '@/utils/storage'

export default {
  components: {TableList, Pagination},
  data() {
    let checkSelectUserIds = (rule, value, callback) => {
      if (this.selectedUserIds.length === 0) {
        callback(new Error('请选择消息接收方'))
      } else {
        callback()
      }
    };

    return {
      title: '添加消息',
      industriesData: {},
      ruleForm: {
        title: '',
        text: ''
      },
      formLabelWidth: "120px",

      rules: {
        title: [
          {required: true, message: '请输入消息标题', trigger: 'blur'},
        ],
        text: [
          {required: true, message: '请输入消息内容', trigger: 'blur'},
        ],
        selectedUserIds: [
          {required: true, trigger: 'blur', validator: checkSelectUserIds},
        ]
      },
      selectedUsers: [],
      selectedUserIds: [],

      selectUsersDialogVisible: false,
      selectUsers: [],

      selectUsersTableSelection: {
        key: true,
        type: 'selection',
        detaile: false
      },
      selectUsersTableOperation: {},
      selectUsersIsSubmitLoading: false,
      selectUsersTableHeader: [
        {id: false, type: 'color', label: '客户名称', list: 'name'},
        {id: false, type: 'color', label: '联系人', list: 'linkmanName'},
        {id: false, type: 'color', label: '联系电话', list: 'linkmanPhone'},
        {id: false, type: 'color', label: '所属行业', list: 'industries'},
      ],
      selectUsersSearchItem: {
        id: '',
        name: '',
        linkmanName: '',
      },
      selectUsersLastItem: {
        id: '',
        name: '',
        linkmanName: '',
      },
      selectUsersTotal: 0,
      selectUsersPageSize: '20',
      selectUsersCurrentPage: '1',

      selectUsersBtn: "选择接收方"
    }
  },
  created() {
    this.industriesData = this.$route.params.industriesData;
    if (this.industriesData === undefined) {
      this.industriesData = JSON.parse(sessionStorage.getItem("industries_data"))
    } else {
      sessionStorage.setItem("industries_data", JSON.stringify(this.industriesData))
    }
  },
  methods: {
    selectUser() {
      this.getCustomers();
    },

    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.loading = true;
          // 取值
          const that = this;
          let data = {};
          data.senderId = sessionStorageGet('id');
          data.title = that.ruleForm.title;
          data.text = that.ruleForm.text;
          data.userIds = that.selectedUserIds;

          // 新增
          add(data).then((res) => {
            if (res.data.code === 1) {
              this.$message.success('添加成功');
              this.resetForm('ruleForm');
              this.loading = false;
            } else {
              this.$message.error(res.data.msg);
            }
          });
        } else {
          return false;
        }
      });
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.closeModal();
    },
    // 初始化表单
    closeModal() {
      //清空表单数据
      for (let key in this.ruleForm) {
        this.ruleForm[key] = "";
      }

      this.$router.push({
        path: '/messages/list'
      })
    },

    async getCustomers() {
      const that = this;

      let data = {};
      data = this.selectUsersSearchItem;
      data.page = this.selectUsersCurrentPage;
      data.size = this.selectUsersPageSize;

      that.selectUsersDialogVisible = true;

      Object.entries(that.selectUsersSearchItem).map((item, index) => {
        that.selectUsersLastItem[item[0]] = that.selectUsersSearchItem[item[0]];
      });

      try {
        that.selectUsersIsSubmitLoading = true;
        const res = await findAll(data);
        that.selectUsersIsSubmitLoading = false;
        if (res.data.code === 1) {
          that.selectUsers = res.data.data.content;
          for (let i = 0; i < that.selectUsers.length; i++) {
            let industriesTemp = that.selectUsers[i].industries;
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
            that.selectUsers[i].industries = industries.substr(1)
          }

          this.selectUsersTotal = res.data.data.totalElements;
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }

    },
    // 搜索
    selectUsersSearch() {
      this.selectUsersCurrentPage = 1;
      this.$refs.selectUsersPage.Page(1);
      this.getCustomers();

    },
    // 翻页
    selectUsersPageChange(item) {
      let that = this;
      this.selectUsersPageSize = item.limit;
      this.selectUsersCurrentPage = item.page;
      Object.entries(that.selectUsersSearchItem).map((item, index) => {
        that.selectUsersSearchItem[item[0]] = that.selectUsersLastItem[item[0]];
      });
      this.getCustomers();
    },
    // 重置
    selectUsersReset() {
      const that = this;
      that.selectUsersSearchItem.name = '';
      that.selectUsersSearchItem.linkmanName = '';
      Object.entries(that.selectUsersSearchItem).map((item, index) => {
        that.selectUsersLastItem[item[0]] = that.selectUsersSearchItem[item[0]];
      })
    },
    selectUsersRowClick(row) {
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

    //全选
    handleSelectionChange(vals) {
      this.selectedUsers = vals
    },

    closeDialog() {
      const that = this
      that.selectedUserIds = []
      that.selectedUsers.map(function (v, k) {
        that.selectedUserIds.push(v.id)
      })

      const len = that.selectedUserIds.length;
      if (len > 0) {
        this.selectUsersBtn = "选择了" + len + "个接收方";
      } else {
        this.selectUsersBtn = "选择接收方";
      }
    }
  }
}
</script>
<style lang="scss" scoped>
/deep/ .el-input {
  width: 100% !important;
}

/deep/ .el-cascader {
  width: 100% !important;
}

/deep/ .el-select {
  width: 100% !important;
}

/deep/ .el-form-item__content {
  line-height: 20px;
}

.main {
  display: flex;
  flex-direction: column;
  background: #fff;
  height: 100%;
  width: 95%;
  margin: 0 auto;
  min-height: 600px;
  margin-top: -240px;
  border-radius: 5px;
  padding: 10px 20px;

  .main-center {
    width: 45%;
    margin: 0 0 0 100px;
  }
}

.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}

</style>
