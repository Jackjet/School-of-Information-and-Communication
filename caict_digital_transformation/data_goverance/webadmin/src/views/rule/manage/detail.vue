<template>
  <el-main>
    <el-drawer
      :title="modalObjjj"
      direction="rtl"
      v-loading="loading"
      :visible.sync="dialogFormVisible"
      :before-close="ClearTextConeten"
      custom-class="demo-drawer"
      size='45%'
      :wrapperClosable='false'
      ref="drawer">
      <el-row>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" v-loading='loading'>
          <el-form-item
            prop="name"
            label="规则名称:"
            :label-width="this.formLabelWidth"
            class='InpitWidth'>
            <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model.trim="ruleForm.name" placeholder="请输入规则名称"></el-input>
          </el-form-item>
          <el-form-item
            prop="templateId"
            label="数据模板:"
            :label-width="this.formLabelWidth"
            class='InpitWidth'>
            <el-select clearable v-model="ruleForm.templateId" :disabled='ProhibitFrom' @change="selectGett(ruleForm.templateId,$event)" placeholder="请选择数据模板">
              <el-option
                v-for="item in optionsss"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <el-button style="margin-left: 10px;" v-if="this.modalObjjj === '编辑'" type="primary" @click="updateTemplate">更新数据模板</el-button>
          </el-form-item>
          <el-form-item
            label="检查SQL:"
            :label-width="this.formLabelWidth"
            class='InpitWidth'>
            <el-input type="textarea" clearable disabled='true' autocomplete="off" v-model="ruleForm.sql" placeholder="请输入检查SQL"></el-input>
          </el-form-item>
          <el-form-item
            label="变更说明:"
            :label-width="this.formLabelWidth"
            class='InpitWidth'>
            <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.updateMessage" placeholder="请输入变更说明"></el-input>
          </el-form-item>
        </el-form>
      </el-row>
      <el-row>
        <!-- <el-button class="add" type="primary" size='small' @click="addB">添加变量</el-button>  -->
        <el-col :span="24">
          <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
        </el-col>
      </el-row>
      <div class="demo-drawer__footer drawer_footer">
        <el-button v-if="!ProhibitFrom" @click="CloseModal">取 消</el-button>
        <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
      </div>
    </el-drawer>
    <drawerr
      ref="call"
      :drawer-dialog='mode'
      @Reload='initial'>
    </drawerr>
  </el-main>
</template>
<script>

  import {insertRule, updateRule, findById, ruleSubscribe, findByRuleId, deleteRuleId} from '@/api/rule'
  import {getList} from '@/api/omManger/user'
  import {isEmpty} from '@/utils/tool'
  import {getTemplate, findByIdTemplate} from '@/api/dataBase'
  import TableList from '@/components/table/TableList.vue'
  import drawerr from '@/components/dialog/dialog.vue'

  export default {
    components: {
      TableList,
      drawerr,
      // 注册组件
      // editor: editor
    },

    props: {
      modalObjjj: {
        type: String,
        default: () => {
        }
      }
    },

    data() {
      return {
        ruleForm: {
          name: '',
          businessLeader: '',
          // taskId: '4586869696',
          params: '',
          technicalLeader: '',
          updateMessage: '',
          templateId: '',
          dataQualityType: '',
          businessTechnicalType: '',
          result: '',
          constraintType: '',
          sql: '',
          setId: '',
        },
        obj: {},
        objj: {},
        objjj: {},
        rule: {},
        options: [],
        optionsss: [],
        // optionss: [{value: '完整性非空约束', label:'完整性非空约束'},
        // {value: '实体唯一性约束', label:'实体唯一性约束'},
        // {value: '有效性长度约束', label:'有效性长度约束'},
        // {value: '有效性标志取值约束 ', label:'有效性标志取值约束'},
        // {value: '有效性代码值域约束 ', label:'有效性代码值域约束'},
        // {value: '有效性内容规范约束', label:'有效性内容规范约束'},
        // {value: '存在一致性依赖约束', label:'存在一致性依赖约束'},
        // {value: '等值一致性依赖约束', label:'等值一致性依赖约束'},
        // {value: '等值一致性依赖约束', label:'等值一致性依赖约束'},
        // {value: '等值一致性依赖约束', label:'等值一致性依赖约束'},
        // {value: '逻辑一致性依赖约束', label:'逻辑一致性依赖约束'},
        // {value: '取值准确性约束', label:'取值准确性约束'},
        // {value: '时效性约束', label:'时效性约束'},
        // ],
        technicalLeaders: [],
        rules: {
          name: [{
            required: true,
            message: '请输入规则名称',
            trigger: 'blur'
          }],
          // taskId: [{
          //   required: true,
          //   message: '请选择检查任务',
          //   trigger: ['change', 'blur']
          // }],
          templateId: [{
            required: true,
            message: '请选择数据模板',
            trigger: ['change', 'blur']
          }]
        },
        tableData: [],
        tableHeader: [{
          id: false, type: '', label: '变量名', list: 'name'
        }, {
          id: false, type: '', label: '变量含义', list: 'meaning'
        }, {
          id: false, type: '', label: '值', list: 'value'
        }
        ],
        tableOpction: {
          label: '操作',
          width: '300px',
          value: 0,
          options: [{
            label: '设置值',
            key: 0,
            type: 'primary',
            State: true,
            method: (row) => {
              this.setId = row.id
              this.$refs.call.popup('添加', 'increase', '')
            }
          }
          ]
        },
        mode: {
          type: 'dialog',
          form: [{
            type: 'input',
            label: '值:',
            placeholder: '请输入值',
            model: 'value',
            state: ['detail', 'new', 'modify'],
            rules: [{
              required: true, message: '请输入值', trigger: 'blur'
            }]
          }],
          event: [{
            bind: 'increase',
            method: (row) => {
              let that = this
              that.increase(row)
            }
          },
            {
              bind: 'modify',
              method: (row) => {
                this.bindModify(row)
              }
            }]
        },
        // 是否触发禁用表单项
        ProhibitFrom: false,
        ProhibitFromm: false,
        dialogFormVisible: this.show,
        formLabelWidth: '120px',
        lock: false,
        // Http: 'http://apptest.liangxin.net.cn/',
        dialogVisible: true,
        nowId: '',
        loading: false,
        isTrue: false,
        isSub: '',
        subId: ''

      }
    },
    watch: {
      show() {
        this.dialogFormVisible = this.show
      }
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
        this.findUsers()
        this.findGetTemplate()
        if (value) {
          that.nowId = value
          that.initFormDetail(value)
          that.findByRuleId(value)
        } else {
          // let year = new Date().getYear()
          that.ProhibitFrom = false
        }
      },
      updateTemplate() {
        this.templateId(this.ruleForm.templateId)
      },
      selectGet(vId) {
        const that = this
        switch (that.ruleForm.constraintType) {
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
      selectGett(vId, event) {
        if (vId === '') {
          return false
        }
        const that = this
        // console.log(vId)
        that.objjj = this.optionsss.find((item) => {//this.ruleForm
          return item.value === vId;//筛选出匹配数据
        });
        this.templateId(vId)
      },
      addB(row) {
        this.$refs.call.popup('添加', 'increase', '')
      },
      /**
       * 初始化获取当前需要编辑 查看的详细数据
       * @param value 当前需要操作的唯一iD
       */
      async initFormDetail(value) {
        const that = this
        try {
          let parameter = {id: value}
          that.loading = true
          that.tableData = []
          const response = await findById(parameter)
          that.loading = false
          JSON.parse(response.data.data.params).map(function (v, k) {
            that.tableData.push({id: v.id, name: v.name, value: v.value, meaning: v.meaning})
          })
          that.ruleForm = response.data.data
          if (this.modalObjjj === '详情') {
            that.ProhibitFrom = true
            that.ProhibitFromm = true
            that.isTrue = true
            that.tableOpction = {}
            // 禁用编辑器
          } else if (this.modalObjjj === '编辑') {
            that.ruleForm.updateMessage = ''
            that.tableOpction = {
              label: '操作',
              width: '300px',
              value: 0,
              options: [{
                label: '设置值',
                key: 0,
                type: 'primary',
                State: true,
                method: (row) => {
                  this.setId = row.id
                  this.$refs.call.popup('添加', 'increase', '')
                }
              }
              ]
            }
            that.ProhibitFromm = true
            that.lock = true
          } else {
            that.tableOpction = {
              label: '操作',
              width: '300px',
              value: 0,
              options: [{
                label: '设置值',
                key: 0,
                type: 'primary',
                State: true,
                method: (row) => {
                  this.setId = row.id
                  this.$refs.call.popup('添加', 'increase', '')
                }
              }
              ]
            }
            that.lock = false
            that.ProhibitFrom = false
            that.ProhibitFromm = false
          }
        } catch (even) {
          that.$message.error('数据获取失败')
        }
      },

      // 关闭模态框 清空所有表单项 为编辑器初始化
      CloseModal() {
        const that = this
        that.ClearTextConeten()
        that.ProhibitFrom = false
        // if (!that.rules.name) {
        //   that.rules = that.rule
        // }
      },

      // 初始化表单
      ClearTextConeten() {
        const that = this
        that.$refs['ruleForm'].resetFields()
        that.ruleForm.name = ''
        that.ruleForm.businessLeader = ''
        // that.ruleForm.taskId = ''
        that.ruleForm.technicalLeader = ''
        that.ruleForm.params = ''
        that.ruleForm.updateMessage = ''
        that.ruleForm.templateId = ''
        that.ruleForm.sql = ''
        that.ProhibitFrom = false
        that.ProhibitFromm = false
        that.isTrue = false
        that.tableData = []
        that.dialogFormVisible = false
        if (that.ruleForm.id) {
          delete that.ruleForm.id
        }
      },

      SuretoAddClick(ruleForm) {
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
          if (this.modalObjjj === '添加') {
            that.add(that.ruleForm)
            return false
          } else if (this.modalObjjj === '编辑') {
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
        da.templateName = that.objjj.label
        // da.taskName = that.objjj.label
        da.params = JSON.stringify(this.tableData)
        that.loading = true
        const response = await insertRule(da)
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
        if (!isEmpty(that.objjj.label)) {
          da.templateName = that.objjj.label
        }
        da.params = JSON.stringify(this.tableData)
        that.loading = true
        const response = await updateRule(da)
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

      async findByRuleId(value) {
        const that = this
        try {
          let parameter = {ruleId: value}
          that.loading = true
          that.tableData = []
          const response = await findByRuleId(parameter)
          if (response.data.code === 1) {
            that.isSub = '已订阅'
            that.subId = response.data.data.id
          } else {
            that.isSub = '未订阅'
          }
          that.loading = false

        } catch (even) {
          that.$message.error('数据获取失败')
        }
      },
      async findUsers() {
        const that = this
        that.loading = true
        let data = {
          page: this.currentPage,
          size: 1000
        }
        const response = await getList(data)
        that.loading = false
        if (response.data.code === 1) {
          that.technicalLeaders = []
          response.data.data.content.map(function (v) {
            that.technicalLeaders.push({value: v.name, label: v.name})
          })
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
      async findGetTemplate() {
        const that = this
        that.loading = true
        let data = {
          page: 1,
          size: 1000
        }
        const response = await getTemplate(data)
        that.loading = false
        if (response.data.code === 1) {
          that.optionsss = []
          response.data.data.content.map(function (v) {
            that.optionsss.push({value: v.id, label: v.name})
          })
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
      async templateId(vId) {
        const that = this
        let data = {
          id: vId
        }
        that.loading = true
        const response = await findByIdTemplate(data)
        that.loading = false
        if (response.data.code === 1) {
          that.tableData = []
          JSON.parse(response.data.data.params).map(function (v, k) {
            that.tableData.push({id: v.id, name: v.name, value: v.value, meaning: v.meaning})
          })
          that.ruleForm.sql = response.data.data.sql
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
      Subscribe(row) {
        const that = this
        let title = that.isSub === '已订阅' ? '取消订阅' : '订阅'
        that.$confirm('请确定是否' + title + '?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          let data = {
            ruleId: that.nowId,
            ruleName: that.ruleForm.name,
          }
          let response
          if (that.isSub === '已订阅') {
            response = await deleteRuleId(that.subId)
          } else {
            response = await ruleSubscribe(data)
          }
          that.Loading = false
          if (response.data.code === 1) {
            that.$message.success(response.data.msg)
            this.CloseModal()
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          return false
        })
      },
      delete(row) {
        const that = this
        that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          let data = {
            id: row.id
          }
          that.tableData.map(function (v, k) {
            if (v.id === row.id) {
              that.tableData.splice(k, 1)
              hat.$message.success('删除成功')
            } else {
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
        let tab = {}
        let isEmpet = ''
        let newValue = ''
        this.isSubmitLoading = true
        that.tableData.forEach((item, index) => {
          if (item.id === that.setId) {
            isEmpet = item.value
            newValue = row.data.value
            item.value = row.data.value
            tab[item.name] = row.data.value
          }
        })
        if (isEmpet === null || typeof (isEmpet) == 'undefined' || isEmpet === undefined || isEmpet === '' || isEmpet === 'null') {
          that.ruleForm.sql = this.render(that.ruleForm.sql, tab);
        } else {
          that.ruleForm.sql = that.ruleForm.sql.replace(new RegExp(isEmpet, 'g'), newValue);
        }
        this.isSubmitLoading = false
        this.$refs.call.closeDialog()
      },

      render(tpl, data) {
        return tpl.replace(/\$\{([^\}]+)\}/g, function ($1, $2) {
          return data[$2] ? data[$2] : $1;
        });
      },

      renders(tpl, data) {
        return tpl.replace(/\$\{([^\}]+)\}/g, function ($1, $2) {
          return data[$2] ? data[$2] : $1;
        });
      },

      genID(length) {
        return Number(Math.random().toString().substr(3, length) + Date.now()).toString(36);
      }
    }
  }
</script>

<style lang="scss" scoped>

  /deep/ .el-drawer__wrapper {
    overflow-y: scroll;
  }

  /deep/ .el-avatar,
  /deep/ .el-drawer {
    overflow-y: scroll !important;
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
    .avatar-border-red .el-upload {
      border: 1px dashed #f56c6c !important;
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

  .a {
    width: 6vw !important;
  }

  .drawer_footer {
    text-align: center;
    margin-bottom: 35px;
  }

  :focus {
    outline: 0;
  }

  .add {
    margin-left: 10px;
  }

</style>
