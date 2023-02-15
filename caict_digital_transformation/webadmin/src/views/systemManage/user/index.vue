<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="姓名">
          <el-input clearable
                    size="medium"
                    placeholder="请输入姓名"
                    v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item label="账号">
          <el-input clearable
                    size="medium"
                    placeholder="请输入账号"
                    v-model="SearchItem.account">
          </el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input clearable
                    size="medium"
                    placeholder="请输入手机号"
                    v-model="SearchItem.phone">
          </el-input>
        </el-form-item>
        <el-form-item label="组织机构">
          <el-input clearable
                    size="medium"
                    placeholder="请输入组织机构"
                    v-model="SearchItem.organizationName">
          </el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select clearable v-model="SearchItem.sex" placeholder="请选择性别">
            <el-option
              key="0"
              label="女"
              value="0">
            </el-option>
            <el-option
              key="1"
              label="男"
              value="1">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="SearchNoteList" style="color: #5677DF" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" v-permission="add" plain size='medium' @click="handleAdd" icon="el-icon-plus" style="color: #5677DF">新增</el-button>
        <el-button type="primary" v-permission="templateDownload" plain size='medium' @click="outModel" icon="el-icon-bottom" style="color: #5677DF">模板下载</el-button>
        <el-upload
          class="upload"
          ref="upload"
          :headers='headers'
          :action="action"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          :before-remove="beforeRemove"
          :on-error="hadleError"
          limit="1"
          :on-exceed="handleExceed"
          :file-list="fileList">
          <el-button v-permission="userImport" type="primary" plain size='medium' @click="add" icon="el-icon-bottom-left" style="color: #5677DF">用户导入</el-button>
        </el-upload>
        <el-button type="primary" plain size='medium' v-permission="userExport" @click="outUser" icon="el-icon-top-right" style="color: #5677DF">用户导出</el-button>
        <el-button type="primary" plain size='medium' v-permission="batchDelete" @click="handleDelete" icon="el-icon-delete" style="color: #5677DF">批量删除</el-button>
        <el-button type="primary" plain size='medium' v-permission="batchRetPassword" @click="handleResetPasswd" icon="el-icon-setting" style="color: #5677DF">批量重置密码</el-button>
        <el-button type="primary" plain size='medium' v-permission="batchEnable" @click="handleEnable(true)" icon="el-icon-circle-check" style="color: #5677DF">批量启用</el-button>
        <el-button type="primary" plain size='medium' v-permission="batchBan" @click="handleEnable(false)" icon="el-icon-remove-outline" style="color: #5677DF">批量禁用</el-button>
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
    webAdminUserFindall,
    webAdminUserDeleteAll,
    webAdminUserUpdatePasswordReset,
    webAdminUserUpdateAllEnable,
    webAdminUserUpdateEnable
  } from "@/api/userMen.js";
  import {mapGetters} from 'vuex'
  import TableList from '@/components/table/tableListJiLu.vue'
  import Pagination from '@/components/table/Pagination.vue'
  import {getToken} from '@/utils/auth'
  import {getJsonTree} from '@/utils/tool'
  import {outExcel} from '@/utils/export'

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
        add: ['none'],
        batchDelete: ['none'],
        batchRetPassword: ['none'],
        templateDownload: ['none'],
        userExport: ['none'],
        userImport: ['none'],
        batchEnable: ['none'],
        batchBan: ['none'],
        action: process.env.VUE_APP_BASE_API + '/webadmin/system/webAdminUser/import',
        headers: {
          Authorization: 'token ' + getToken(),
        },
        fileList: [],
        tableSelection: {
          key: true,
          type: 'selection',
          detaile: false
        },
        isSubmitLoading: false,
        DeletelistiD: [],
        enbleList: [],
        tableData: [],
        tableHeader: [
          {id: false, type: '', label: '姓名', list: 'name'},
          {id: false, type: '', label: '账号', list: 'account'},
          {id: false, type: '', label: '手机号', list: 'phone'},
          {id: false, type: '', label: '性别', list: 'sex'},
          {id: false, type: '', label: '组织机构', list: 'organizationName'},
          {id: false, type: '', label: '角色权限', list: 'roleName'},
          {id: false, type: '', label: '创建时间', list: 'createTime'},
        ],
        tableOpction: {
          label: '操作',
          width: '230px',
          value: 'user',
          options: [{
            label: '详情',
            key: 0,
            type: 'text',
            permission: ['none'],
            icon: 'el-icon-tickets',
            State: false,
            method: (row) => {
              this.handleDetail(row)
            },
            show: (row) => {
              return true;
            }
          }, {
            label: '编辑',
            key: 0,
            type: 'text',
            permission: ['none'],
            icon: 'el-icon-edit-outline',
            State: false,
            method: (row) => {
              this.handleEdit(row)
            }
          },
            {
              label: '',
              key: 0,
              type: 'text',
              icon: 'el-icon-circle-check',
              State: false,
              permission: ['none'],
              method: (row) => {
                this.enable(row)
              },
              show: (row) => {
                return true;
              }
            }]
        },
        SearchItem: {
          name: '',
          account: '',
          phone: '',
          organizationName: '',
          sex: ''
        },
        lastItem: {
          name: '',
          account: '',
          phone: '',
          organizationName: '',
          sex: ''
        },
        total: 0,
        pageSize: '20',
        currentPage: '1',
        RootUrl: '',
        modalObjj: '',
      }
    },
    created() {
      this.fetchData()
      this.findPermission()
    },
    methods: {
      // 查询按钮权限
      findPermission() {
        const that = this
        let props = {
          detail: function (value) {
            that.tableOpction.options[0].permission = JSON.parse(JSON.stringify(value))
          },
          edit: function (value) {
            that.tableOpction.options[1].permission = JSON.parse(JSON.stringify(value))
          },
          add: function (value) {
            that.add = JSON.parse(JSON.stringify(value))
          },
          batchDelete: function (value) {
            that.batchDelete = JSON.parse(JSON.stringify(value))
          },
          batchRetPassword: function (value) {
            that.batchRetPassword = JSON.parse(JSON.stringify(value))
          },
          templateDownload: function (value) {
            that.templateDownload = JSON.parse(JSON.stringify(value))
          },
          userExport: function (value) {
            that.userExport = JSON.parse(JSON.stringify(value))
          },
          userImport: function (value) {
            that.userImport = JSON.parse(JSON.stringify(value))
          },
          batchEnable: function (value) {
            that.batchEnable = JSON.parse(JSON.stringify(value))
          },
          batchBan: function (value) {
            that.batchBan = JSON.parse(JSON.stringify(value))
          },
          enable: function (value) {
            that.tableOpction.options[2].permission = JSON.parse(JSON.stringify(value))
          }
        }
        if (this.permissions.length > 0) {
          this.permissions.map(function (v, k) {
            if (v['user']) {
              for (let i in v['user']) {
                if (props.hasOwnProperty(i)) {
                  props[i](v['user'][i])
                }
              }
            }
          })
        }
      },
      // 重置
      reset() {
        const that = this
        that.SearchItem.name = '',
          that.SearchItem.account = '',
          that.SearchItem.phone = '',
          that.SearchItem.organizationName = '',
          that.SearchItem.sex = ''
        Object.entries(that.SearchItem).map((item, index) => {
          that.lastItem[item[0]] = that.SearchItem[item[0]]
        })
      },
      // 获取列表
      async fetchData() {
        const that = this
        let datas = ''
        datas = this.SearchItem,
          datas.page = this.currentPage,
          datas.size = this.pageSize
        Object.entries(that.SearchItem).map((item, index) => {
          that.lastItem[item[0]] = that.SearchItem[item[0]]
        })
        try {
          that.isSubmitLoading = true
          const res = await webAdminUserFindall(datas)
          that.isSubmitLoading = false
          if (res.data.code === 1) {
            res.data.data.content.map(function (v, k) {
              if (v.sex === 0) {
                v.sex = '女'
              } else if (v.sex === 1) {
                v.sex = '男'
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
        const that = this
        that.DeletelistiD = []
        that.enbleList = []
        vals.map(function (v, k) {
          that.DeletelistiD.push(v.id)
          that.enbleList.push({id: v.id, enable: v.enable})
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
      // 详情
      handleDetail(data) {
        const that = this
        that.$router.push({
          path: '/systemManage/user/userAdd',
          query: {
            id: data.id,
            type: '详情'
          }
        })
      },
      // 编辑
      handleEdit(data) {
        const that = this
        that.$router.push({
          path: '/systemManage/user/userAdd',
          query: {
            id: data.id,
            type: '编辑'
          }
        })
      },
      // 添加
      handleAdd(data) {
        const that = this
        that.$router.push({
          path: '/systemManage/user/userAdd',
          query: {
            type: '新增'
          }
        })
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
      // 删除当前数据 重载列表
      async handleDelete(data) {
        const that = this
        if (that.DeletelistiD.length === 0) {
          return that.$message.warning('请先选择用户')
        }
        that.$confirm('删除会清空数据库中除日志相关的其他全部用户信息, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.isSubmitLoading = true
          const response = await webAdminUserDeleteAll(that.DeletelistiD + '')
          that.isSubmitLoading = false
          if (response.data.code === 1) {
            that.$message.success('批量删除成功')
            this.fetchData()
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          return false
        })
      },
      // 启用 重载列表
      async enable(row) {
        const that = this
        let tips = ''
        let data = {
          enable: !row.enable,
          id: row.id
        }
        if (row.enable) {
          tips = '禁用'
        } else {
          tips = '启用'
        }
        that.$confirm('请确认是否' + tips + '?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.isSubmitLoading = true
          const response = await webAdminUserUpdateEnable(data)
          that.isSubmitLoading = false
          if (response.data.code === 1) {
            if (row.enable) {
              that.$message.success('禁用成功')
              this.fetchData()
            } else {
              that.$message.success('启用成功')
              this.fetchData()
            }
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          return false
        })
      },
      // 重置密码 重载列表
      async handleResetPasswd() {
        const that = this
        if (that.DeletelistiD.length === 0) {
          return that.$message.warning('请先选择用户')
        }
        that.$confirm('重置密码为123456, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.isSubmitLoading = true
          const response = await webAdminUserUpdatePasswordReset(that.DeletelistiD)
          that.isSubmitLoading = false
          if (response.data.code === 1) {
            that.$message.success('批量重置密码成功')
            this.fetchData()
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          return false
        })
      },
      // 批量启用 重载列表
      async handleEnable(type) {
        const that = this
        if (that.DeletelistiD.length === 0) {
          return that.$message.warning('请先选择用户')
        }
        let num = 0
        that.enbleList.map(function (v, k) {
          if (type === true && v.enable === false) {
            num++
          } else if (type === false && v.enable === true) {
            num++
          }
        })
        if (num === 0) {
          return type === true ? that.$message.warning('所选账号已经是启用状态') : that.$message.warning('所选账号已经是禁用状态')
        }
        let data = {
          enable: type,
          ids: that.DeletelistiD
        }
        let tips = type === true ? '此操作将批量启用账号' : '此操作将批量禁用账号'
        that.$confirm(tips + ', 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.isSubmitLoading = true
          const response = await webAdminUserUpdateAllEnable(data)
          that.isSubmitLoading = false
          if (response.data.code === 1) {
            if (type) {
              that.$message.success('批量启用账号成功')
            } else {
              that.$message.success('批量禁用账号成功')
            }
            this.fetchData()
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          return false
        })
      },
      // 上传成功
      handleSuccess(response, file, fileList) {
        this.$refs.upload.clearFiles();
        if (fileList[0].response.code === 1) {
          this.fileList = []
          this.$message.success('导入成功')
          this.fetchData()
        } else {
          this.fileList = []
          this.$message.error(fileList[0].response.msg)
        }
      },
      // 上传失败
      hadleError(error, file, fileList) {
        this.$refs.upload.clearFiles();
        this.fileList = []
        const result = JSON.parse(error.message);
        this.$message.error(result.msg)
      },
      // 导出模板
      outModel() {
        const that = this
        outExcel('请确认是否下载模板?', "/webadmin/system/webAdminUser/exportModel", {}, '用户信息模板')
      },
      // 导出模板
      outUser() {
        const that = this
        let data = {}
        data = this.SearchItem,
          data.page = this.currentPage,
          data.size = this.pageSize
        outExcel('请确认是否导出用户?', "/webadmin/system/webAdminUser/export", data, '用户信息')
      }
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

  .upload {
    display: inline-block;
    margin: 0 10px;
    max-height: 30px;
    // overflow: hidden;
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
      background: url('../../../assets/image/filter.png');
      background-size: 100% 100%;
      padding: 15px 0 0 20px;
      min-height: 100px;

    }
  }

  .add {
    width: 25px;
    height: 25px;
    background: url('../../../assets/image/add.png');
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
</style>
