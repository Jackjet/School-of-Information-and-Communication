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
            label="模板名称:"
            :label-width="this.formLabelWidth"
            class='InpitWidth'>
            <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model.trim="ruleForm.name" placeholder="请输入模板名称"></el-input>
          </el-form-item>
          <el-form-item
            prop="constraintType"
            label="约束分类:"
            :label-width="this.formLabelWidth"
            class='InpitWidth'>
            <el-select clearable v-model="ruleForm.constraintType" :disabled='ProhibitFrom' @change="selectGet" placeholder="请选择约束分类">
              <el-option
                v-for="item in optionss"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="结果集:"
                        :label-width="this.formLabelWidth"
                        class='InpitWidth'>
            <el-radio v-model="ruleForm.result" :disabled="isChoose" label="1">单行</el-radio>
            <el-radio v-model="ruleForm.result" :disabled="isChoose" label="2">多行</el-radio>
          </el-form-item>
          <el-form-item
            prop="sql"
            label="检查SQL:"
            :label-width="this.formLabelWidth"
            class='InpitWidth'>
            <el-input type="textarea" :rows="4" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.sql" placeholder="请输入检查SQL"></el-input>
            <div style="position: absolute; display: inline-block;line-height: 0%; padding: 6px;">
              <el-tooltip content="变量引用使用'${name}'形式" placement="top">
                <div style="top:0!important;" class="el-icon-warning"></div>
              </el-tooltip>
            </div>
          </el-form-item>
          <el-form-item
            label="备注:"
            :label-width="this.formLabelWidth"
            class='InpitWidth'>
            <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.remark" placeholder="请输入备注"></el-input>
          </el-form-item>
        </el-form>
      </el-row>
      <el-row>
        <el-button v-if="isShow" class="add" type="primary" size='small' @click="addB">添加变量</el-button>
        <el-col :span="24">
          <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
        </el-col>
      </el-row>
      <div class="demo-drawer__footer drawer_footer">
        <el-button v-if='!ProhibitFrom' @click="CloseModal">取 消</el-button>
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

  import {getTemplate, insertTemplate, updateTemplate, findByIdTemplate} from '@/api/dataBase'
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
          businessTechnicalType: '1',
          result: '1',
          sql: '',
          params: '',
          constraintType: '',
          dataQualityType: '',
          remark: '',
        },
        obj: {},
        objj: {},
        objjj: {},
        rule: {},
        isChoose: false,
        isShow: true,
        options: [],
        optionss: [{value: '完整性非空约束', label: '完整性非空约束'},
          {value: '实体唯一性约束', label: '实体唯一性约束'},
          {value: '有效性长度约束', label: '有效性长度约束'},
          {value: '有效性标志取值约束', label: '有效性标志取值约束'},
          {value: '有效性代码值域约束', label: '有效性代码值域约束'},
          {value: '有效性内容规范约束', label: '有效性内容规范约束'},
          {value: '存在一致性依赖约束', label: '存在一致性依赖约束'},
          {value: '等值一致性依赖约束', label: '等值一致性依赖约束'},
          {value: '逻辑一致性依赖约束', label: '逻辑一致性依赖约束'},
          {value: '取值准确性约束', label: '取值准确性约束'},
          {value: '时效性约束', label: '时效性约束'},
        ],
        optionsss: [],
        rules: {
          name: [{
            required: true,
            message: '请输入模板名称',
            trigger: 'blur'
          }],
          sql: [{
            required: true,
            message: '请输入检查sql',
            trigger: 'blur'
          }],
          constraintType: [{
            required: true,
            message: '请选择约束分类',
            trigger: ['change', 'blur']
          }],
          dataQualityType: [{
            required: true,
            message: '请选择数据问题分类',
            trigger: ['change', 'blur']
          }]
        },
        tableData: [],
        tableHeader: [{
          id: false, type: '', label: '变量名', list: 'name'
        }, {
          id: false, type: '', label: '变量含义', list: 'meaning'
        }],
        tableOpction: {
          label: '操作',
          width: '300px',
          value: 0,
          options: [{
            label: '删除',
            key: 0,
            type: 'danger',
            State: false,
            method: (row) => {
              this.delete(row)
            }
          }
          ]
        },
        mode: {
          type: 'dialog',
          form: [{
            type: 'input',
            label: '变量名:',
            placeholder: '请输入变量名',
            model: 'name',
            state: ['detail', 'new', 'modify'],
            rules: [{
              required: true, message: '请输入变量名', trigger: 'blur'
            }]
          },
            {
              type: 'input',
              label: '变量含义:',
              placeholder: '请输入变量含义',
              model: 'meaning',
              state: ['detail', 'new', 'modify'],
              // rules: [{
              //   required: true, message: '请输入变量含义', trigger: 'blur'
              // }]
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
        loading: false,

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
        that.tableOpction = {
          label: '操作',
          width: '300px',
          value: 0,
          options: [{
            label: '删除',
            key: 0,
            type: 'danger',
            State: false,
            method: (row) => {
              this.delete(row)
            }
          }
          ]
        }
        that.dialogFormVisible = true
        // this.findList()
        if (value) {
          that.nowId = value
          that.initFormDetail(value)
        } else {
          // let year = new Date().getYear()
          that.ProhibitFrom = false
        }
      },

      selectGet(vId) {
        const that = this
        if (vId === '') {
          that.ruleForm.sql = ''
          return
        }
        switch (that.ruleForm.constraintType) {
          case '完整性非空约束':
            that.ruleForm.sql = 'select * from A.table1 where field1 is null';
            break;
          case '实体唯一性约束':
            that.ruleForm.sql = 'select filed1,filed2,field3,count(*) from A.table1 group by field1,field2,field3 having count(*)>1';
            break;
          case '有效性长度约束':
            that.ruleForm.sql = 'select 主键字段 length from A.table1 where length <>10';
            break;
          case '有效性标志取值约束':
            that.ruleForm.sql = "select * from A.table1 where field1 not in('0','1')";
            break;
          case '有效性代码值域约束':
            that.ruleForm.sql = "select * from A.table1 where field1 not in(select code from 代码表)";
            break;
          case '有效性内容规范约束':
            that.ruleForm.sql = "from A.table1 where not regexp like (核检字段，'^1[3456789]\d{9}$')";
            break;
          case '存在一致性依赖约束':
            that.ruleForm.sql = "(1)表间的存在一致性依赖约束，示例 1、如客户表和账户表间，账户表中的客户号应在存在一致性依赖约束 2、客户主表和客户附表间，客户附表中的客户号在客户主表中存在。(2)字段间的存在一致性依赖约束，示例 A系统获得的客户学历应推送给各系统，故同一客户的学历信息应一致";
            break;
          case '等值一致性依赖约束':
            that.ruleForm.sql = "同一字段的等值致性依赖约束，示例 A系统获得的客户学历应推送给各系统，故同一客户的学历信息应一致";
            break;
          case '逻辑一致性依赖约束':
            that.ruleForm.sql = "一个核检对象的值必须在、与另外一个核检对象的值满足某种逻辑关系（如大于小于等）。示例：人口出生日期小于入学日期。SELECT主键字段、出生日期、入学日期逻辑致性依赖约束FROM A table 1   WHERE出生日期<入学日期";
            break;
          case '取值准确性约束':
            that.ruleForm.sql = '描述核检对象是否与其对应的客观实体的特征相一致。不仅数据的格式和内容满足有效性的要求，其值也是客观实体的数据';
            break;
          case '时效性约束':
            that.ruleForm.sql = '描述核检对象能否及时反应其对应的实际业务的时点状态。比如判断业务日期范围是否符合范围，是否只有一天的数据 示例： select count(distinct业务日期 as num from A table1）where num 1';
            break;
        }
      },
      selectGett(vId) {
        const that = this
        that.objjj = this.optionsss.find((item) => {//this.ruleForm
          return item.value === vId;//筛选出匹配数据
        });
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
          const response = await findByIdTemplate(parameter)
          that.loading = false
          this.tableData = JSON.parse(response.data.data.params)
          response.data.data.params = JSON.parse(response.data.data.params)
          that.ruleForm = response.data.data

          if (this.modalObjjj === '详情') {
            that.tableOpction = {}
            that.ProhibitFrom = true
            that.ProhibitFromm = true
            that.isChoose = true
            that.isShow = false;
            // 禁用编辑器
          } else if (this.modalObjjj === '编辑') {
            that.ProhibitFromm = true
            that.lock = true
            that.isChoose = false
          } else {
            that.tableOpction =
              that.lock = false
            that.isChoose = false
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
        that.ruleForm.name = '',
          that.ruleForm.businessTechnicalType = '1',
          that.ruleForm.result = '1',
          that.ruleForm.sql = '',
          that.ruleForm.params = '',
          that.ruleForm.constraintType = '',
          that.ruleForm.dataQualityType = '',
          that.ruleForm.remark = '',
          that.tableData = []
        that.isChoose = false
        that.isShow = true
        that.ProhibitFrom = false
        that.ProhibitFromm = false
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
        let number = 0
        let index = that.ruleForm.sql.indexOf('$')
        while (index !== -1) {
          number++; // 每出现一次 次数加一
          index = that.ruleForm.sql.indexOf('$', index + 1); // 从字符串出现的位置的下一位置开始继续查找
        }
        if (this.tableData.length > number) {
          return that.$message.warning('请确认添加的变量是否已全部引用！')
        }
        if (this.tableData.length > 0 && that.ruleForm.sql.indexOf('$') === -1 && that.ruleForm.sql.indexOf('{') === -1 && that.ruleForm.sql.indexOf('}') === -1) {
          return that.$message.warning('检查SQL中未含有变量，请确认！')
        }
        da.params = JSON.stringify(this.tableData)
        that.loading = true
        const response = await insertTemplate(da)
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
        let number = 0
        let index = that.ruleForm.sql.indexOf('$')
        while (index !== -1) {
          number++; // 每出现一次 次数加一
          index = that.ruleForm.sql.indexOf('$', index + 1); // 从字符串出现的位置的下一位置开始继续查找
        }
        if (this.tableData.length > number) {
          return that.$message.warning('请确认添加的变量是否已全部引用！')
        }
        if (this.tableData.length > 0 && that.ruleForm.sql.indexOf('$') === -1 && that.ruleForm.sql.indexOf('{') === -1 && that.ruleForm.sql.indexOf('}') === -1) {
          return that.$message.warning('检查SQL中未含有变量，请确认！')
        }
        da.params = JSON.stringify(this.tableData)
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
              let flag = that.tableData.splice(k, 1)
              that.$message.success('删除成功')
            }
          })
          that.Loading = false
        }).catch(() => {
          return false
        })
      },

      increase(row) {
        const that = this
        let i = 0;
        let id = that.genID(10)
        that.tableData.map(function (k, v) {
          if (k.name === row.data.name) {
            i = 1
            that.$message.warning('不可添加同名变量!')
            return false;
          }
        })
        if (i === 1) {
          return false;
        }
        that.tableData.push({id: id, name: row.data.name, meaning: row.data.meaning, value: ''})
        this.$refs.call.closeDialog()
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
