<template>
  <el-main class="main">
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item  label="操作时间" >
          <el-date-picker
            v-model="time"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']"
           >
          </el-date-picker>
        </el-form-item>
        <el-form-item  label="操作人">
          <el-input clearable
            size="medium"
            placeholder="请输入操作人"
            v-model="SearchItem.createByName">
          </el-input>
        </el-form-item>
        <el-form-item  label="操作类型">
          <el-input clearable
            size="medium"
            placeholder="请输入操作类型"
            v-model="SearchItem.api">
          </el-input>
        </el-form-item>
        <el-form-item  label="操作结果">
          <el-select clearable v-model="SearchItem.result" placeholder="请选择操作结果">
            <el-option
              key="0"
              label="失败"
              value="0">
            </el-option>
            <el-option
              key="1"
              label="成功"
              value="1">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" style="color: #5677DF" icon="el-icon-search">查询</el-button>
          <el-button  size='medium' @click="reset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" plain size='medium' v-permission="exportUp"  @click="outUser" icon="el-icon-top-right" style="color: #5677DF">导出</el-button>
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
  operationLogFindAll,
  operationLogExport
} from "@/api/log.js"
import { mapGetters } from 'vuex'
import TableList from '@/components/table/tableListJiLu.vue'
import Pagination from '@/components/table/Pagination.vue'
import { getToken } from '@/utils/auth'
import { getJsonTree } from '@/utils/tool'
import { outExcel } from '@/utils/export'
export default {
  components: { TableList, Pagination},
  computed: {
  ...mapGetters([
    'permissions'
    ]),
  },
  data() {
      return {
      drawer: false,
      time: [],
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      detail: ['none'],
      exportUp: ['none'],
      isSubmitLoading: false,
      DeletelistiD: [],
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '操作人', list: 'createByName'},
        {id: false, type: '', label: '操作模块', list: 'module'},
        {id: false, type: '', label: '操作类型', list: 'api'},
        {id: false, type: '', label: '操作内容', list: 'contentMsg'},
        {id: false, type: 'red', label: '操作结果', list: 'result'},
        {id: false, type: '', label: '操作时间', list: 'createTime'}
      ],
      tableOpction: {
        label: '操作',
        width: '180px',
        value: 0,
        options: [{
          label: '详情',
          key: 0,
          type: 'text',
          icon: 'el-icon-tickets',
          State: true,
          permission: ['none'],
          method: (row) => {
            this.handleDetail(row)
          },
          show: (row) => {
            return true;
          }
        }]
      },
      SearchItem: {
        createByName: '',
        api: '',
        result: '',
        startTime: '',
        endTime: '',
      },
      lastItem: {
       createByName: '',
        api: '',
        result: '',
        startTime: '',
        endTime: '',
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
  mounted() {
  },
  methods: {
    // 查询按钮权限
    findPermission() {
      const that = this
      let props = {
        detail: function (value) {
          that.tableOpction.options[0].permission = JSON.parse(JSON.stringify(value))
        },
        export: function (value) {
          that.exportUp = JSON.parse(JSON.stringify(value))
        },
      }
      if(this.permissions.length > 0){
        this.permissions.map(function(v, k){
          if(v['operationLog']){
            for (let i in v['operationLog']) {
              props[i](v['operationLog'][i])
            }
          }
        })
      }
    },
    // 重置
    reset() {
      const that = this
      that.SearchItem.createByName = '',
      that.SearchItem.api = '',
      that.SearchItem.result = '',
      that.SearchItem.startTime = '',
      that.SearchItem.endTime = '',
      that.time = []
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem,
      data.page = this.currentPage,
      data.size = this.pageSize
       if(type !== 'page'){
         if(that.time !== null &&  that.time !== '' && that.time.length > 0){
            data.startTime = this.time[0],
            data.endTime = this.time[1]
          } else {
            data.startTime = '',
            data.endTime = ''
        }
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await operationLogFindAll(data)
        that.isSubmitLoading = false
        if(res.data.code === 1){
          res.data.data.content.map(function(v, k){
           if(v.result === 0){
              v.result = '失败'
            }else if(v.result === 1){
              v.result = '成功'
            }
            if(v.contentMsg.length > 11) { v.contentMsg = v.contentMsg.substr(0, 11) + '...'}
          })
          this.tableData = res.data.data.content
          this.total =  res.data.data.totalElements
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
      vals.map(function(v, k){
        that.DeletelistiD.push(v.id)
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
      if(that.lastItem.startTime === '' || that.lastItem.startTime === null){
        that.time = []
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      if(that.lastItem.startTime === '' || that.lastItem.startTime === null){
        that.time = []
      }
      this.fetchData('page')
    },
    // 详情
    handleDetail(data) {
      const that = this
      that.$router.push({
        path: '/logManage/operationLog/operationLogAdd',
        query: {
          id: data.id,
          type:'详情'
        }
      })
    },
    // 行高
    cellStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },
    // 头行高
    headerStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },
    // 导出模板
    outUser() {
      const that =this
      outExcel('请确认是否导出操作日志?',  "/webadmin/log/operationLog/export", this.SearchItem, '操作日志')
    },

  }
}
</script>
<style lang="scss" scoped>
  /deep/ .el-button--primary.is-plain:focus, .el-button--primary.is-plain:hover{
    background: #ffffffff
  }
  /deep/ .el-form-item__label{
    color: #fff;
  }
  /deep/ .el-input--medium .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-input__icon .el-range__icon {
    line-height: 24px;
  }
  /deep/ .el-date-editor .el-range-separator{
    line-height: 24px;
  }
  /deep/ .el-button--medium{
    padding: 7px 15px;
  }
  /deep/ .el-date-editor .el-range__close-icon{
    line-height: 24px !important;
  }
  /deep/ .el-form-item{
    margin-bottom: 5px;
  }
  /deep/ .el-InputForm .el-range__icon{
    line-height: 25px!important;
  }
  .center{
    background: #fff;
    padding: 0px 15px;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 350px;
  }
  .main{
    background: #00000000;
    height: 100%;
    width: 100%;
    padding: 0 30px;
    min-height: 600px;
    margin-top: -240px;
    .el-InputForm{
      background:url('../../../assets/image/filter.png');
      background-size: 100% 100%;
      padding:15px 0 0 20px;
      min-height: 100px;

    }
  }
  .add{
    width: 25px;
    height: 25px;
    background:url('../../../assets/image/add.png');
  }
  .main-top{

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
