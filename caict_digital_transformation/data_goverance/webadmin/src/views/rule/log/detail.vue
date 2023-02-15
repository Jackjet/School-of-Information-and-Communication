<template>
  <el-main>
  <el-drawer
    :title="modalObjj"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer">
  <el-row>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="ruleName"
        label="规则名称:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='true' autocomplete="off" v-model.trim="ruleForm.ruleName" placeholder="请输入规则名称"></el-input>
      </el-form-item>
        <el-form-item
        prop="taskName"
        label="任务名称:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='true' autocomplete="off" v-model.trim="ruleForm.taskName" placeholder="请输入任务名称"></el-input>
      </el-form-item>
      <el-form-item
        prop="startTime"
        label="作业开始时间:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
         <el-input clearable :disabled='true' autocomplete="off" v-model.trim="ruleForm.startTime" placeholder="请输入作业开始时间"></el-input>
      </el-form-item>
      <el-form-item 
        prop="title"
        label="标题:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model.trim="ruleForm.title" placeholder="请输入标题"></el-input>
      </el-form-item>
      <el-form-item  prop="userName"  :label-width="this.formLabelWidth" class="InpitWidth" label="抄送人:">
        <el-input clearable autocomplete="off" disabled v-model="ruleForm.userName" placeholder="请选择抄送人">
          <el-button slot="append" icon="el-icon-circle-plus" @click="roleClick">选择</el-button>
        </el-input>
      </el-form-item>
      <el-form-item
        prop="level"
        label="级别:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-radio-group v-model="ruleForm.level">
          <el-radio  label="严重警告">严重警告</el-radio>
          <el-radio  label="警告">警告</el-radio>
          <el-radio  label="关注">关注</el-radio>
          <el-radio  label="一般">一般</el-radio>
          <el-radio label="正常">正常</el-radio>
        </el-radio-group>
      </el-form-item>
       <el-form-item
        prop="eventDesc"
        label="事件描述:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.eventDesc" placeholder="请输入事件描述"></el-input>
      </el-form-item>
    </el-form>
   </el-row> 
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
    <el-dialog title="用户列表" :before-close='cancel' append-to-body :visible.sync="isShow">
      <el-table
        ref="multipleTable"
        v-loading="loading"
        :data='table.list.data'
        empty-text='暂无数据'
        class='el_tab_alage'
        :row-key='getRowKeys'
        @selection-change="handleSelectionChange">
        <!-- <el-table-column type="selection" :reserve-selection="true" width="50" align="center"></el-table-column> -->
         <el-table-column label="序号" min-width="30" align="center">
          <template slot-scope="scope">
            <el-radio v-model="radio" :label="scope.$index" :key="scope.$index" class="no-show-label" @change.native="getCurrentRow(scope.row)">&nbsp;</el-radio>
          </template>
        </el-table-column>
        <template v-for="(item, index) in table.list.header">
          <el-table-column fit
            align='center'
            :key='index'
            :label="item.label"
            :width="item.width"
            :prop="item.field">
            <template slot-scope="scope">
              {{scope.row[item.field]}}
            </template>
          </el-table-column>
        </template>
      </el-table>
      <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      </el-col>
      <div style="text-align: center;">
        <el-button type="primary" @click="cancel">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </div>
    </el-dialog>
  </el-drawer>

   </el-main>
</template>
<script>

import { insert, findById } from '@/api/rule/log'
import { getList } from '@/api/omManger/user'
import Pagination from '@/components/table/Pagination.vue'	
export default {
  components: {
    // 注册组件
    // editor: editor
    Pagination
  },

  props: {
    modalObjj: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      ruleForm: {
        level: '',   
        ruleId: '',
        ruleName: '',
        taskId: '',
        taskName: '',
        startTime: '',
        eventDesc: '',
        title: '',
        userName: ''
      },
      selectId:'',
      radio: '',
      obj: {},
      objj: {},
      objjj: {},
      rule: {},
      options: [],
      optionss: [{value: '完整性非空约束', label:'完整性非空约束'},
      {value: '实体唯一性约束', label:'实体唯一性约束'},
      {value: '有效性长度约束', label:'有效性长度约束'},
      {value: '有效性标志取值约束 ', label:'有效性标志取值约束'},
      {value: '有效性代码值域约束 ', label:'有效性代码值域约束'},
      {value: '有效性内容规范约束', label:'有效性内容规范约束'},
      {value: '存在一致性依赖约束', label:'存在一致性依赖约束'},
      {value: '等值一致性依赖约束', label:'等值一致性依赖约束'},
      {value: '等值一致性依赖约束', label:'等值一致性依赖约束'},
      {value: '等值一致性依赖约束', label:'等值一致性依赖约束'},
      {value: '逻辑一致性依赖约束', label:'逻辑一致性依赖约束'},
      {value: '取值准确性约束', label:'取值准确性约束'},
      {value: '时效性约束', label:'时效性约束'},
      ],
      optionsss: [],
      rules: {
        ruleName: [{
          required: true,
          message: '请输入规则名称',
          trigger: 'blur'
        }],
        startTime: [{
          required: true,
          message: '请输入作业开始时间',
          trigger: ['change', 'blur']
        }],
        taskName: [{
          required: true,
          message: '请输入任务名称',
          trigger: ['change', 'blur']
        }],
        level: [{
          required: true,
          message: '请选择级别',
          trigger: ['change', 'blur']
        }],
        title: [{
          required: true,
          message: '请输入标题',
          trigger: ['change', 'blur']
        }],
        itcode: [{
          required: true,
          message: '请选择抄送人',
          trigger: ['change', 'blur']
        }],
        userName: [{
          required: true,
          message: '请选择抄送人',
          trigger: ['change', 'blur']
        }],
        eventDesc: [{
          required: true,
          message: '请选择事件描述',
          trigger: ['change', 'blur']
        }],
      },
      isShow: false,
      // 勾选数据
      tableArr: [],
      table: {
        list: {
          header: [
            {
              label: '用户名称',
              field: 'name',
              width: '100'
            },
            {
              label: '描述',
              field: 'remark',
              width: '110'
            },
            {
              label: '角色',
              field: 'roleName',
              width: '110'
            },
            {
              label: '创建时间',
              field: 'createTime'
            }
          ],
          data: []
        }
      },
      // 是否触发禁用表单项
      ProhibitFrom: false,
      ProhibitFromm: false,
      dialogFormVisible: this.show,
      formLabelWidth: '120px',
      lock: false,
      // Http: 'http://apptest.liangxin.net.cn/',
      dialogVisible: true,
      loading: false,
      total: 0,
      pageSize: '20',
      currentPage: '1',
     
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  created() {

  },
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    initial: function (value) {
      const that = this
      that.dialogFormVisible = true
      // this.findNodes()
      // this.findList()
      if (value) {
        that.nowId = value
        that.initFormDetail(value)
      } else {
        // let year = new Date().getYear()
        that.ProhibitFrom = false
      }
    },
     pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.commandTypeFindRoles()
    },
    getRowKeys (row) {
      return row.id
    },
    getCurrentRow(row){
      this.ruleForm.itcode = row.id
      this.ruleForm.userName = row.name
    },
    // handleSelectionChange (row) {
    //   // 只要id
    //   console.log(row)
    //   let arr = []
    //   row.forEach(item => {
    //     arr.push(item.id)
    //   })
    //   this.tableArr = arr
    // },
    roleClick () {
      this.isShow = true
      this.commandTypeFindRoles()
    },
    // 获取权限
    async commandTypeFindRoles () {
      let data = {
        page: this.currentPage,
        size: this.pageSize
      }
      const res = await getList(data)
      this.table.list.data = res.data.data.content
      this.total = res.data.data.totalElements
      // let newArr = this.id
      // // 获取勾选id
      // res.data.data.forEach(item => {
      //   arr.forEach(row => {
      //     if (item.id === row) {
      //       newArr.push(item)
      //     }
      //   })
      // })
      // // 设置选中
      // console.log(newArr)
      // newArr.forEach(row => {
      //   this.$refs.multipleTable.toggleRowSelection(row, true)
      // })
    },
    selectGet(vId){
      const that = this 
      switch(that.ruleForm.constraintType){
        case '完整性非空约束': 
          that.ruleForm.sql = 'select * from A.table1 where field1 is null';
          break;
        case '实体唯一性约束': 
          that.ruleForm.sql = 'select filed1,filed2,field3,count(*) from A.table1 group by field1,field2,field3 having count(*)>1';
          break;
        case '有效性长度约束': 
          that.ruleForm.sql = 'length from A.table1 where length <>10';
          break;
        case '有效性标志取值约束': 
          that.ruleForm.sql = "select * from A.table1 where field1 not in('0','1')";
          break;
        case '有效性代码值域约束': 
          that.ruleForm.sql = 'select * from A.table1 where field1 not in(select code from 代码表)';
          break;
        case '有效性内容规范约束': 
          that.ruleForm.sql = "from A.table1 where not regexp like (核检字段，'^1[3456789]\d{9}$')";
          break;
        // case '存在一致性依赖约束': that.ruleForm.sql =  'length from A.table1 where length <>10'; break;
        // case '等值一致性依赖约束': that.ruleForm.sql =  'select * from A.table1 where field1 not in('0','1')'; break;
        // case '逻辑一致性依赖约': that.ruleForm.sql =  'length from A.table1 where length <>10'; break;
        // case '取值准确性约束': that.ruleForm.sql =  'select * from A.table1 where field1 not in('0','1')'; break;
      }
    },
    cancel () {
      this.currentPage = 1
      this.$refs.multipleTable.clearSelection()
      this.isShow = false
    },
    submit () {
      console.log(this.radio)
      // 每次选择清空上一次  
      this.isShow = false
      // 确定权限后清空勾选状态
      this.$refs.multipleTable.clearSelection()
    },
    selectGett(vId){
      const that = this 
      that.objjj = this.optionsss.find((item)=>{//this.ruleForm
        return item.value === vId;//筛选出匹配数据
      });
    },
    addB (row) {
      this.$refs.call.popup('添加', 'increase', '')
    },
    /**
     * 初始化获取当前需要编辑 查看的详细数据
     * @param value 当前需要操作的唯一iD
     */
    async initFormDetail (value) {
      const that = this
      try {
        let parameter = {id: value}
        that.loading = true
        that.tableData = []
        const response = await findById(parameter)
        that.loading = false
        that.ruleForm.ruleId = response.data.data.ruleId
        that.ruleForm.ruleName = response.data.data.ruleName
        that.ruleForm.taskId = response.data.data.taskId
        that.ruleForm.taskName = response.data.data.taskName
        that.ruleForm.startTime = response.data.data.startTime
        if (this.modalObjj === '详情') {
          that.ProhibitFrom = true
          that.ProhibitFromm = true
          that.isShow = false;
          // 禁用编辑器
        } else if (this.modalObjj === '编辑') {
          that.ProhibitFromm = true
          that.lock = true
        } else {
          that.lock = false
          that.ProhibitFrom = false
          that.ProhibitFromm = false
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },

    // 关闭模态框 清空所有表单项 为编辑器初始化
    CloseModal () {
      const that = this
      that.ClearTextConeten()
      that.ProhibitFrom = false
      // if (!that.rules.name) {
      //   that.rules = that.rule
      // }
    },

    // 初始化表单
    ClearTextConeten () {
      const that = this
      that.$refs['ruleForm'].resetFields()
      that.ruleForm.name = '',   
      that.ruleForm.businessTechnicalType = '1',
      that.ruleForm.result = '1',
      that.ruleForm.sql = '',
      that.ruleForm.params = '',
      that.ruleForm.constraintType = '',
      that.ruleForm.dataQualityType = '',
      that.tableData = []
      that.radio = '',
      that.isShow = false
      that.ProhibitFrom = false
      that.ProhibitFromm = false
      that.dialogFormVisible = false
      if (that.ruleForm.id) {
        delete that.ruleForm.id
      }
    },

    SuretoAddClick (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }
        Object.entries(that.ruleForm).map((a, b) => {
          if (a[0] === 'id') {
            delete that.ruleForm[a[0]]
          }
        })
        if (this.modalObjj === '添加') {
          that.add(that.ruleForm)
          return false
        } else if (this.modalObjj === '编辑') {
          that.ruleForm.id = that.nowId
          that.edit(that.ruleForm)
          return false
        }
      })
    },

    // 添加方法
    async add(data) {
      const that = this
      // POST请求添加轮播图数据
      let da = data
      let array = []
      da.params = JSON.stringify(this.tableData)
      array.push({context: da.eventDesc, updateTime: '', userName: '', operation: ''})
      da.eventDesc = JSON.stringify(array)
      that.loading = true
      const response = await insert(da)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextConeten()
        // 重新触发父组件加载loading
        that.$emit('Reload')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    // 修改方法
    async edit(data) {
      const that = this
      // PUT 请求修改数据
      let da = data
      let array = []
      array.push({context: da.eventDesc, updateTime: '', userName: '', operation: ''})
      da.eventDesc = JSON.stringify(array)
      that.loading = true
      const response = await updateTemplate(da)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextConeten()
        // 重新触发父组件加载loading
        that.$emit('Reload')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    async findNodes() {
      const that = this
      that.loading = true
      const response = await getDataQuality()
      that.loading = false
      if (response.data.code === 1) {
        that.optionsss = []
        response.data.data.content.map(function(v){
          that.optionsss.push({value: v.name, label:v.name})
        })
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    delete(row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async() => {
        that.Loading = true
        let data = {
          id: row.id
        }
        that.tableData.map(function(v, k){
          if(v.id === row.id){
            that.tableData.splice(k,1)
            that.$message.success('删除成功')
          }else{
              that.$message.success('删除失败')
          }
        })
        that.Loading = false
      }).catch(() => {
        return false
      })
    },

    increase(row) {
      const that = this
      let id = that.genID(10)
      that.tableData.push({id:id, name:row.data.name, meaning: row.data.meaning, value: ''})
    },

    genID(length){
      return Number(Math.random().toString().substr(3,length) + Date.now()).toString(36);
    }
  }
}
</script>

<style lang="scss" scoped>

/deep/ .el-drawer__wrapper{
  overflow-y: scroll;
}
/deep/ .el-avatar, 
 /deep/ .el-drawer{
  overflow-y: scroll!important;
}
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
  margin-bottom: 35px;
}
:focus {
  outline:0;
}
.add{
  margin-left:10px;
}

</style>
