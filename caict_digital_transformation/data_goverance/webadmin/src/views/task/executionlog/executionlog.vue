<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-select clearable v-model="SearchItem.groupTaskId" placeholder="请选择所属任务组">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入任务名称" v-model="SearchItem.taskName">
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-select clearable v-model="SearchItem.status" placeholder="请选择任务状态">
            <el-option label="执行成功" value="执行成功"></el-option>
            <el-option label="执行失败" value="执行失败"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-date-picker
            type="datetimerange"
            value-format="yyyy-MM-dd HH:mm:ss"
            range-separator="至"
            start-placeholder="任务开始时间"
            end-placeholder="任务结束时间"
            @change="shijianxuanz"
            :value="datevalue"
            v-model="datevalue">
          </el-date-picker>
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
    <drawer
      :modal-objj='modalObjj'
      ref="callDetaill"
      @Reload='submitLoadingg'>
    </drawer>
  </el-main>
</template>

<script>
import {findById, getList, groupTaskfa} from '@/api/task/executionlog'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import drawer from './add'

export default {
  components: {TableList, Pagination, drawer},
  data() {
    return {
      drawer: false,
      dialogFormVisible: false,
      formLabelWidth: '120px',
      tableData: [],
      tableHeader: [
        {
          id: false, type: '', label: '任务组名称', list: 'groupTaskName'
        },
        {
          id: false, type: '', label: ' 任务名称', list: 'taskName'
        },
        {
          id: false, type: '', label: '源', list: 'sourceTable'
        },
        {
          id: false, type: '', label: '目标', list: 'targetTable'
        },
        {
          id: false, type: '', label: '增全量策略', list: 'strategy'
        },
        {
          id: false, type: '', label: '执行状态', list: 'status'
        },
        {
          id: false, type: '', label: '开始时间', list: 'startTime'
        },
        {
          id: false, type: '', label: '结束时间', list: 'endTime'
        },
        {
          id: false, type: '', label: '执行时长', list: 'duration'
        }
      ],
      tableOpction: {
        label: '操作',
        width: '100px',
        value: 2,
        options: [{
          label: '日志详情',
          key: 0,
          type: 'primary',
          State: true,
          method: (row) => {
            this.detail(row)
          }
        }]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      ruleForm: [],
      modalObjj: '详情',
      datevalue: '',
      SearchItem: {
        strategy: '',
        status: '',
        taskName: '',
        groupTaskId: '',
        name: '',
        startTimeThan: '',
        startTimeLess: '',
        groupTaskLogId: '',
      },
      groupTaskfaItem: {
        name: '',
        taskName: '',
        sourceCatalog: '',
        dataSourceSourceId: '',
        targetCatalog: '',
        dataSourceTargetId: '',
        status: '',
      },
      options: [],
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: ''
    }
  },
  created() {
    this.findNodes()
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
            if (v.status === '失效') {
              v.status = '执行失败'
            } else if (v.status === '有效') {
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
      console.log(this.datevalue)
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
    detail: function (row) {
      let that = this
      that.modalObjj = '详情'
      that.$refs.callDetaill.initial(row.id)
    },
    async Logdetails(row) {
      let that = this;
      this.dialogFormVisible = true;
      var findByIddata = {
        id: row
      }
      const res = await findById(findByIddata)
      var ruleForm = res.data.data;
      this.ruleForm = ruleForm;
    },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },
    async findNodes() {
      const that = this
      const data = this.groupTaskfaItem
      data.page = this.currentPage
      data.size = this.pageSize
      const response = await groupTaskfa(data)
      if (response.data.code === 1) {
        that.options = []
        response.data.data.content.map(function (v, k) {
          that.options.push({value: v.id, label: v.name})
        })
        if (this.$route.query.name != null && this.$route.query.name != undefined && this.$route.query.name != '') {
          this.SearchItem.taskName = this.$route.query.name;
        }
        if (this.$route.query.id !== '' || this.$route.query.id !== null || this.$route.query.id !== undefined || this.$route.query.id !== 'undefined') {
          this.SearchItem.groupTaskLogId = this.$route.query.id
        }
        this.fetchData()
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

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    float: left;
    position: relative;
    overflow: hidden;

    & :hover {
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

  & :hover {
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


