<template>
  <el-drawer
    :title="modalObjj"
    direction="rtl"
    v-loading="loading"
     :scrollable='true'
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer"
    >
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="name"
        label="任务名称:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrommmm' autocomplete="off" v-model="ruleForm.name" placeholder="请输入任务名称"></el-input>
      </el-form-item>
       <el-form-item
        prop="groupTaskId"
        label="所属任务组:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select v-model="ruleForm.groupTaskId" clearable :disabled='ProhibitFromm'   @change="selecSource" placeholder="请选择所属任务组">
          <el-option
            v-for="item in Sourceoptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item> 
      <el-form-item
        prop="dataSourceSourceName"
        label="数据来源:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable disabled  autocomplete="off"  @change="dataSource" v-model="ruleForm.dataSourceSourceName" placeholder="请输入数据来源"></el-input>
      </el-form-item> 
       <el-form-item
        prop="sourceTable"
        label="源:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select  v-model="ruleForm.sourceTable"  clearable  :disabled='ProhibitFromm'  placeholder="请选择源">
          <el-option 
            v-for="item in sourceTables"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="dataSourceTargetName"
        label="目标源:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable disabled  autocomplete="off"  @change="SourceTarget" v-model="ruleForm.dataSourceTargetName" placeholder="请输入目标源"></el-input>
      </el-form-item> 
       <el-form-item
        prop="targetTable"
        label="目标:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select  v-model="ruleForm.targetTable"   clearable :disabled='ProhibitFromm'  placeholder="请选择目标">
          <el-option
            v-for="item in targetTables"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="规则名称:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select v-model="ruleForm.ruleId" clearable  @change="rulename" :disabled='ProhibitFromm'  placeholder="请选择规则">
          <el-option
            v-for="item in rulenameoption"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="增全量策略："
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
       <el-radioGroup v-model="ruleForm.strategy">
        <el-radio label="增量"></el-radio>
        <el-radio label="全量"></el-radio>
   	   </el-radioGroup>
      </el-form-item>
      <el-form-item
        prop="tel"
        label="执行器API:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.api" placeholder="请输入执行器API"></el-input>
      </el-form-item>
      <el-form-item
        prop=""
        label="请求方法:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select v-model="ruleForm.method" clearable  @change="chan" :disabled='ProhibitFromm'  placeholder="请选择请求方法">
          <el-option
            v-for="item in methodes"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
       <el-form-item
        prop="tel"
        label="header:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable type="textarea"  :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.header" placeholder="请输入{'name':'value'}格式的header参数"></el-input>
      </el-form-item>     
       <el-form-item
        prop="tel"
        label="URL参数:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable type="textarea"  :disabled='ProhibitFrommm' autocomplete="off" v-model="ruleForm.urlParams" placeholder="请输入'?variableName=variableValue'格式的URL参数"></el-input>
      </el-form-item>
       <el-form-item
        prop="tel"
        label="Body:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.body" placeholder="请输入请输入{'name':'value'}格式的body参数"></el-input>
      </el-form-item>
       <el-form-item
        prop="tel"
        label="备注:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea"  clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.remark" placeholder="请输入备注"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>
import { getGroupTask, metaDataTableFindAll} from '@/api/groupTask'
import { getRule, } from '@/api/rule'
import { insert, update,  roleList, findById ,SourcefindAll,groupTaskfa} from '@/api/task/manag'
import { getDataSource } from '@/api/dataSource'
import { isMPStrict } from '@/utils/validate'
export default {
  components: {
    // 注册组件
    // editor: editor
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
			    api: "",
				  body: "",
				  dataSourceSourceId: "",
				  dataSourceTargetName: '',
       	  dataSourceSourceName: '',
				  dataSourceTargetId: "",
				  groupTaskId: "",
				  groupTaskName: "",
				  header: "",
				  name: "",
				  remark: "",
				  sourceTable: "",
				  status: "",
				  strategy: "增量",
				  targetTable: "",
				  urlParams: "",
				  ruleName:"",
          ruleId:"",
          method: '',
          
      },
      sourceTables:[],
      targetTables:[],
      obj: {},
      objj: {},
      objjj: {},
      objjjj: {},
      rule: {},
      ProhibitFrommm: false,
      ProhibitFrommmm: false,
      Sourceoptions: [],
      rulenameoption: [],
       options: [{value: '关系数据源', lable:'关系数据源'},{value: '大数据数据源', lable:'大数据数据源'},{value: '文件数据源', lable:'文件数据源'}],
       methodes:[{value: 'GET', lable:'GET'},{value: 'POST', lable:'POST'},{value: 'PUT', lable:'PUT'},{value: 'DELETE', lable:'DELETE'}],
      rules: {
        name: [{
          required: true,
          message: '请输入任务名称',
          trigger: 'blur'
        }],
        groupTaskId: [{
          required: true,
          message: '请选择所属任务组',
          trigger: ['change', 'blur']
        }],
        dataSourceSourceName: [{
          required: true,
          message: '请选择源数据来源',
          trigger: ['change', 'blur']
        }],
        sourceTable: [{
          required: true,
          message: '请选择源',
          trigger: ['change', 'blur']
        }],
        dataSourceTargetName: [{
          required: true,
          message: '请选择目标源',
          trigger: ['change', 'blur']
        }],
        targetTable: [{
          required: true,
          message: '请选择目标',
          trigger: ['change', 'blur']
        }],
        // ruleId: [{
        //   required: true,
        //   message: '请选择规则名称',
        //   trigger: ['change', 'blur']
        // }],
      },
       dataSources: [],
     	 dataTargets: [],
			 Sourcedata:{
			 	 name: '',
         sourceCatalog: '',
         dataSourceSourceId: '',
         targetCatalog: '',
         dataSourceTargetId: '',
         status: '',
			 },
			 groupTaskfaItem: {
        name: '',
        sourceCatalog: '',
        dataSourceSourceId: '',
        targetCatalog: '',
        dataSourceTargetId: '',
        status: '',
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
      SearchItem: {
        name: '',
        sourceCatalog: '',
        targetCatalog: '',
        status: '',
        dataSourceSourceId: '',
        dataSourceTargetId: '',
      },
      objDataSource: {},
      objSourceTarget: {}
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  created() {
    // alert(1)
    this.fetchDatas()
    this.findNodess()
  },
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    async fetchData(val) {
      this.SearchItem.name = val.label
       const data = this.SearchItem
       data.page = this.currentPage
       data.size = this.pageSize  
      try {
        const res = await getGroupTask(data)
        if(res.data.code === 1 && res.data.data.content.length > 0){
          this.ruleForm.dataSourceSourceName = res.data.data.content[0].dataSourceSourceName;
          this.ruleForm.dataSourceTargetName =  res.data.data.content[0].dataSourceTargetName;
          this.ruleForm.dataSourceSourceId = res.data.data.content[0].dataSourceSourceId;
          this.ruleForm.dataSourceTargetId =  res.data.data.content[0].dataSourceTargetId;
          this.source(res.data.data.content[0].dataSourceSourceId, 1)
          this.source(res.data.data.content[0].dataSourceTargetId, 2)
        }else {
          this.$message.error(even.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    async source(id, type) {	
     	const that = this
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        dataSourceId: id,
      }
      try {
        const res = await metaDataTableFindAll(data)
        if(res.data.code === 1 && res.data.data.content.length > 0){
          if(type === 1){
            that.sourceTables = []
            res.data.data.content.map(function(v, k){
              that.sourceTables.push({value:v.tableName, label:v.tableName})
            })
          }else if(type === 2){
             that.targetTables = []
            res.data.data.content.map(function(v, k){
              that.targetTables.push({value:v.tableName, label:v.tableName})
            })
          }
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    async fetchDatas() {	
     	const that = this
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        name: this.ruleForm.ruleName,
      }
      try {
        const res = await getRule(data)
        res.data.data.content.map(function(v, k){
        that.rulenameoption.push({value: v.id, label:v.name})
        })
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    initial: function (value, id) {
      if(id !== ''){
        this.ruleForm.groupTaskId = id
        this.selecSource(id)
      }
     const that = this
      that.dialogFormVisible = true
      if (value) {
        that.nowId = value
        that.initFormDetail(value)
        // that.getTarget(2, value)
      } else {
        // let year = new Date().getYear()
        that.ProhibitFrom = false
      }
    },
    selecSource(vId){
      const that = this 
      that.objjj = this.Sourceoptions.find((item)=>{//this.ruleForm
        return item.value === vId;//筛选出匹配数据
      });
      this.ruleForm.groupTaskName = that.objjj.label;
  	  this.fetchData(that.objjj)
    },
    rulename(vId){
      const that = this 
      that.objjjj = this.rulenameoption.find((item)=>{//this.ruleForm
        return item.value === vId;//筛选出匹配数据
      });
      this.ruleForm.ruleName = that.objjjj.label;
      this.ruleForm.ruleId =  that.objjjj.value;
    },
    dataSource(vId){
      const that = this 
      that.objDataSource = this.sourceTables.find((item)=>{//this.ruleForm
        return item.value === vId;//筛选出匹配数据
      });
      this.ruleForm.dataSourceSourceId = that.objDataSource.label;
      this.ruleForm.dataSourceSourceName =  that.objDataSource.value;
    },
    SourceTarget(vId){
      const that = this 
      that.objSourceTarget = this.targetTables.find((item)=>{//this.ruleForm
        return item.value === vId;//筛选出匹配数据
      });
      this.ruleForm.dataSourceTargetName = that.objSourceTarget.label;
      this.ruleForm.dataSourceTargetId =  that.objSourceTarget.value;
    },
    chan(){
      if(this.ruleForm.method === 'POST' || this.ruleForm.method === 'PUT'){
        this.ProhibitFrommm = true 
      }else{
         this.ProhibitFrommm = false
      }
    },
// 	selectGet(vId){
// 		console.log(vId)
//    const that = this 
//    that.obj = this.dataSource.find((item)=>{//this.ruleForm
//      return item.value === vId;//筛选出匹配数据
//    });
//  },
//  selectGett(vId){
//  	console.log(vId)
//    const that = this 
//    that.objj = this.dataTarget.find((item)=>{//this.ruleForm
//      return item.value === vId;//筛选出匹配数据
//    });
//  },
    /**
     * 初始化获取当前需要编辑 查看的详细数据
     * @param value 当前需要操作的唯一iD
     */
    async initFormDetail (value) {
      const that = this;
      try {
        let parameter = {id: value}
        that.loading = true
        const response = await findById(parameter)
        that.loading = false
        that.ruleForm = response.data.data
        that.source(response.data.data.dataSourceSourceId, 1)
         that.source(response.data.data.dataSourceTargetId, 2)
        if (this.modalObjj === '详情') {
            that.ProhibitFrom = true
            that.ProhibitFromm = true
            that.ProhibitFrommm = true
            that.ProhibitFrommmm = true
          // 禁用编辑器
        } else if (this.modalObjj === '编辑') {
          if(value === 'admin'){
            that.ProhibitFrom = false
            that.ProhibitFromm = true
            that.ProhibitFrommmm = true
          } else {
            that.ProhibitFrom = false
            that.ProhibitFromm = false
            that.ProhibitFrommmm = true
          }
          that.lock = true
        }  else {
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
      that.ruleForm.api= '';
      that.ruleForm.body = '';
      that.ruleForm.groupTaskId = '';
      that.ruleForm.groupTaskName = '';
      that.ruleForm.dataSourceSourceId = ''
      that.ruleForm.dataSourceSourceName = ''
      that.ruleForm.sourceCatalog = ''
      that.ruleForm.targetCatalog = ''
      that.ruleForm.dataSourceTargetId = ''
      that.ruleForm.dataSourceTargetName = ''
      that.ruleForm.header = '';
      that.ruleForm.name = '';
      that.ruleForm.remark = '';
      that.ruleForm.ruleId = '';
      that.ruleForm.sourceTable = '';
      that.ruleForm.sourceType = '';
      that.ruleForm.status = '';
      that.ruleForm.strategy = '增量';
      that.ruleForm.targetTable = '';
      that.ruleForm.targetType = '';
      that.ruleForm.urlParams= '';
      that.ProhibitFrom = false
      that.ProhibitFromm = false
      that.ProhibitFrommm = false
      that.ProhibitFrommmm = false
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
      that.loading = true
      const response = await update(data)
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
      
      const data = this.Sourcedata
      data.page = this.currentPage
      data.size = this.pageSize
      const response = await SourcefindAll(data)
      if (response.data.code === 1) {
        that.options = [] 
        response.data.data.content.map(function(v, k){
          that.options.push({value: v.id, label:v.name})
        })
        console.log(that.options)
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async getTarget(type, value) {
//    const that = this
//    // that.loading = true
//    let data  = {}
//    data.catalog = type === 0 ? that.ruleForm.dataSourceSourceName : that.ruleForm.dataSourceTargetName
//    const response = await getDataSource(data)
//    // that.loading = false
//    if(response.data.code === 1) {
//      if(type === 0) {
//          that.dataSource = []
//          response.data.data.content.map(function(v, k){
//          that.dataSource.push({value: v.id, label:v.name})
//        })
//      } else if(type === 1) {
//        that.dataTarget = []
//        response.data.data.content.map(function(v, k){
//          that.dataTarget.push({value: v.id, label:v.name})
//        })
//      } else if(type === 2){
//        response.data.data.content.map(function(v, k){
//          that.dataTarget.push({value: v.id, label:v.name})
//          that.dataSource.push({value: v.id, label:v.name})
//          that.initFormDetail(value)
//        })
//        
//      }
//    } else {
//      // 添加上传失败后 回调失败信息
//      that.$message.error(response.data.msg)
//      return false
//    }
    },
    
    
     async findNodess() {
      const that = this
      const data = this.groupTaskfaItem
      data.page = this.currentPage
      data.size = this.pageSize
      const response = await groupTaskfa(data)
      if (response.data.code === 1) {
        that.Sourceoptions = [] 
        response.data.data.content.map(function(v, k){
          that.Sourceoptions.push({value: v.id, label:v.name})
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

<style lang="scss">
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