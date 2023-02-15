<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入任务组名称" v-model="SearchItem.groupTaskName">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.dataSourceSourceId" placeholder="请选择数据来源">
            <el-option
              v-for="item in dataSource"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.dataSourceTargetId" placeholder="请选择目标源">
            <el-option
              v-for="item in dataSource"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.status" placeholder="请选择执行状态">
            <el-option label="执行成功" value="执行成功"></el-option>
            <el-option label="执行失败" value="执行失败"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size='medium' @click="search" icon="el-icon-search">搜索</el-button>

        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>

  </el-main>
</template>

<script>
  import {getList, findById, groupTaskfa} from '@/api/task/grouplog'
  import {getDataSource} from '@/api/dataSource'
  import TableList from '@/components/table/TableList.vue'
  import Pagination from '@/components/table/Pagination.vue'
  import md5 from 'js-md5'

  export default {
    components: {TableList, Pagination,},
    data() {
      return {
        drawer: false,
        tableData: [],
        tableHeader: [{
          id: false, type: '', label: '任务组名称', list: 'groupTaskName'
        }, {
          id: false, type: '', label: '数据来源', list: 'dataSourceTargetName'
        }, {
          id: false, type: '', label: '目标源', list: 'dataSourceTargetName'
        }, {
          id: false, type: '', label: '调度周期', list: 'operationMode'
        }, {
          id: false, type: '', label: '执行状态', list: 'status'
        }, {
          id: false, type: '', label: '开始时间', list: 'startTime'
        }, {
          id: false, type: '', label: '结束时间', list: 'endTime'
        }
        ],
        tableOpction: {
          label: '操作',
          width: '150px',
          value: 2,
          options: [{
            label: '任务日志查看',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.Logdetails(row)
            }
          }]
        },
        isSubmitLoading: false,
        DeletelistiD: [],
        ruleForm: [],
        options: [{value: '关系数据源', lable: '关系数据源'}, {value: '大数据数据源', lable: '大数据数据源'}, {value: '文件数据源', lable: '文件数据源'}],
        datevalue: '',
        dataSource: [],
        SearchItem: {
          groupTaskName: '',
          sourceCatalog: '',
          targetCatalog: '',
          status: '',
          dataSourceSourceId: '',
          dataSourceTargetId: '',
        },
        groupTaskfaItem: {
          name: '',
          sourceCatalog: '',
          dataSourceSourceId: '',
          targetCatalog: '',
          dataSourceTargetId: '',
          status: '',

        },

        total: 0,
        pageSize: '20',
        currentPage: '1',
        RootUrl: ''
      }
    },
    created() {
      this.findNodes()
      this.fetchData()
    },
    methods: {
      async fetchData() {
        const data = this.SearchItem
        data.page = this.currentPage
        data.size = this.pageSize
        try {
          const res = await getList(data)
          if (res.data.code === 1) {
            res.data.data.content.map(function (v, k) {
              if (v.status === '无效') {
                v.status = '执行失败'

              } else {
                v.status = '执行成功'

              }
            })
            this.tableData = res.data.data.content
            this.total = res.data.data.totalElements
          } else {
//         this.$message.error(even.msg)
          }

        } catch (even) {
          this.$message.error(even.msg)
        }
      },
      shijianxuanz(val) {
        this.SearchItem.startTimeThan = this.datevalue[0];
        this.SearchItem.startTimeLess = this.datevalue[1];

      },
      search() {
        this.currentPage = 1;
        this.$refs.page.Page(1);
        this.fetchData()
      },
      submitLoadingg() {
        this.fetchData()
      },
      Logdetails(row) {
        let that = this;
        this.$router.push({
          path: '/taskManage/executionlog',
          query: {
            id: row.id
          }
        })
      },
      pageChange(item) {
        this.pageSize = item.limit
        this.currentPage = item.page
        this.fetchData()
      },
      async findNodes(data) {
        const that = this
        that.loading = true
        const response = await getDataSource()
        that.loading = false
        if (response.data.code === 1) {
          that.dataSource = []
          response.data.data.content.map(function (v, k) {
            that.dataSource.push({value: v.id, label: v.name})
          })
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
    }
  }
</script>

<style type="scss">
  .el-drawer {
    .el-drawer {
      .el-drawer__body {
        padding: 10px 20px !important;
      }

    }
    .InpitWidth {
      width: 80%;
      min-width: 302px;
    }

    .avatar-border-red .el-upload {
      border: 1px dashed #f56c6c !important;
      border-radius: 6px;
      cursor: pointer;
      float: left;
      position: relative;
      overflow: hidden;
      :hover {
        border-color: #b6b5b5;
      }

      .el-progress {
        position: absolute;
        z-index: 999;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: white;
        .el-progress-circle {
          margin: 13% auto 10%;
        }
      }
    }

    .avatar-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      float: left;
      position: relative;
      overflow: hidden;

      &
      :hover {
        border-color: #b6b5b5;
      }

      .el-progress {
        position: absolute;
        z-index: 999;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: white;

        .el-progress-circle {
          margin: 13% auto 10%;
        }
      }
    }
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 200px;
      height: 178px;
      line-height: 178px;
      text-align: center;
      cursor: pointer;
      border-radius: 6px;
    }

    .avatar {
      width: 200px;
      height: 180px;
      display: block;
    }

  }

  .edit_main {
    text-align: left;
    padding: 10px 15px;
    border-radius: 5px;
    min-height: 250px;
    background-color: #f5f7fa;
    border: 1px solid #e4e7ed;
    color: #c0c4cc;
    cursor: not-allowed;

    img {
      width: auto;
      height: auto;
    }

  }

  .ClassDisable .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    float: left;
    position: relative;
    overflow: hidden;
    cursor: no-drop;

    &
    :hover {
      border-color: #b6b5b5;
    }

    .el-progress {
      position: absolute;
      z-index: 999;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: white;

      .el-progress-circle {
        margin: 13% auto 10%;
      }

    }
  }

  .buttonentry {
    float: left;
    text-align: left;
  }

  .a {
    width: 6vw !important;
  }

  .drawer_footer {
    text-align: center;
  }

  :focus {
    outline: 0;
  }

  .el-drawer__open .el-drawer.rtl {
    overflow: scroll;
  }
</style>


