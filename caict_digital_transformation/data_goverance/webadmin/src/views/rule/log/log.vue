<template>
  <el-main>
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
       <el-form-item>
           <el-form-item>
         	 <el-input clearable size="medium" placeholder="请输入规则名称" prefix-icon="el-icon-search" v-model="SearchItem.ruleName">
         	 </el-input>
           </el-form-item>
            <!--<el-datePicker  type="daterange" @change="shijianxuanz" :value="datevalue"  v-model="datevalue"  show-week-numbers placement="bottom-end"  placeholder="Select date" style="width: 220px"></el-datePicker >-->
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="search" icon="el-icon-search">查询</el-button>
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
    <drawerr
      :modal-obj='modalObj'
      ref="callDetail"
      @Reload='submitLoading'>
    </drawerr>
  </el-main>
</template>

<script>
import { getList, findById} from '@/api/rule/log'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import md5 from 'js-md5'
import drawer from './detail'
import drawerr from './add'
export default {
  components: { TableList, Pagination, drawer, drawerr },
  data() {
    return {
      drawer: false,
        dialogFormVisible: false,
         formLabelWidth: '120px',
      tableData: [],
      tableHeader: [{
        id: false, type: '', label: '规则名称', list: 'ruleName'
      }, {
        id: false, type: '', label: '作业开始时间', list: 'startTime'
      }, {
        id: false, type: '', label: '执行结果', list: 'status'
      }, {
        id: false, type: '', label: '检查条数', list: 'checkCount'
      }, {
        id: false, type: '', label: '错误条数', list: 'errorCount'
      },
      // {
      //   id: false, type: '', label: '日志详情', list: 'logDesc'
      // },

      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 2,
        options: [{
          label: '详情',
          key: 0,
          type: 'primary',
          State: true,
          method: (row) => {
            this.Logdetails(row)
          }
        },{
          label: '新建工单',
          key: 0,
         type: 'primary',
          State: true,
          method: (row) => {
            this.add(row)
          }
        },]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      ruleForm:[],
      modalObj: '',
      modalObjj: '',
      datevalue:'',
      SearchItem: {
        ruleName: '',
        // startTimeThan: '2015-03-05 17:59:00',
        // startTimeLess: '2020-07-09 17:59:00',
      },

      options: [],
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: ''
    }
  },
  created() {
    this.fetchData()
    if(this.$route.query.id != null){
    	this.ruleForm.groupTaskName = this.$route.query.id;
    }
  },
  methods: {
    async fetchData() {
      const data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      try {
        const res = await getList(data)
        if(res.data.code === 1){
          res.data.data.content.map(function(v, k){
            if(v.status === '无效'){
              v.status = '执行失败'

            }else{
              v.status = '执行成功'

            }
          })
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        }else {
//         this.$message.error(even.msg)
        }

      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    shijianxuanz(val){
    	this.SearchItem.startTimeThan = this.datevalue[0];
    	this.SearchItem.startTimeLess = this.datevalue[1];

    },
    search(){
      this.currentPage = 1;
      this.$refs.page.Page(1);
      this.fetchData()
    },
    submitLoadingg(){
      this.fetchData()
    },
    add: function (row) {
      let that = this
      this.modalObjj = '添加'
      this.$refs.callDetaill.initial(row.id)
    },
    Logdetails(row) {
      let that = this
      this.modalObj = '详情'
      this.$refs.callDetail.initial(row.id)
    },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },
  }
}
</script>

<style type="text/css">
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
  .avatar-border-red .el-upload{
    border:1px dashed #f56c6c !important;
    border-radius: 6px;
    cursor: pointer;
    float: left;
    position: relative;
    overflow: hidden;
    &:hover {
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
    &:hover {
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
  border:1px solid #e4e7ed;
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
  &:hover {
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
.a{
  width: 6vw!important;
}
.drawer_footer{
  text-align: center;
}
:focus {
  outline:0;
}
.el-drawer__open .el-drawer.rtl{
	overflow: scroll;
}
</style>


