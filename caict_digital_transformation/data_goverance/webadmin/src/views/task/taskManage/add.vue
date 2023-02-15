<template>
  <el-drawer
    :title="modalObjj"
    direction="rtl"
    v-loading="loading"
    :visible.sync="dialogFormVisible"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" v-loading='loading'>
      <el-form-item
        prop="name"
        label="任务组名称:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='!isEdit' autocomplete="off" v-model="ruleForm.name" placeholder="请输入任务组名称"></el-input>
      </el-form-item>
      <el-form-item
        prop="operationMode"
        label="运行模式:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-radio v-model="ruleForm.operationMode" @change='ali' label="手动模式">手动模式</el-radio>
        <el-radio v-model="ruleForm.operationMode" @change='ali' label="周期调度">周期调度</el-radio>
      </el-form-item>
      <el-form-item v-if='corn'
                    prop="scheduleCycle"
                    label="调度Corn:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.scheduleCycle" placeholder="请输入调度Corn"></el-input>
      </el-form-item>
      <el-form-item
        prop="dataSourceSourceId"
        label="数据来源:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select v-model="ruleForm.dataSourceSourceId" clearable @change="selectGet" placeholder="请选择数据来源">
          <el-option
            v-for="item in dataSource"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="dataSourceTargetId"
        label="目标源:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select v-model="ruleForm.dataSourceTargetId" clearable @change="selectGett" placeholder="请选择数据来源">
          <el-option
            v-for="item in dataTarget"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="备注:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.remark" placeholder="请输入备注"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button v-if='!ProhibitFrom' @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>

  import {insertGroupTask, findByIdTaskUpdate, groupTaskUpdate} from '@/api/groupTask'
  import {getDataSource} from '@/api/dataSource'
  import {isMPStrict} from '@/utils/validate'

  export default {
    components: {
      // 注册组件
      // editor: editor
    },

    props: {
      modalObjj: {
        type: String,
        default: () => {
        }
      }
    },

    data() {
      return {
        ruleForm: {
          name: '',
          remark: '',
          dataSourceSourceId: '',
          sourceCatalog: '',
          targetCatalog: '',
          dataSourceTargetId: '',
          operationMode: '手动模式'
        },
        corn: false,
        obj: {},
        objj: {},
        rule: {},
        options: [{value: '关系数据源', lable: '关系数据源'}, {value: '大数据数据源', lable: '大数据数据源'}, {value: '文件数据源', lable: '文件数据源'}],
        rules: {
          name: [{
            required: true,
            message: '请输入任务组名称',
            trigger: 'blur'
          }],
          sourceCatalog: [{
            required: true,
            message: '请选择源数据来源类型',
            trigger: ['change', 'blur']
          }],
          scheduleCycle: [{
            required: true,
            message: '请输入调度Corn',
            trigger: ['change', 'blur']
          }],
          dataSourceSourceId: [{
            required: true,
            message: '请选择数据来源',
            trigger: ['change', 'blur']
          }],
          targetCatalog: [{
            required: true,
            message: '请选择目标源类型',
            trigger: ['change', 'blur']
          }],
          dataSourceTargetId: [{
            required: true,
            message: '请选择目标源',
            trigger: ['change', 'blur']
          }]
        },
        dataSource: [],
        dataTarget: [],
        // 是否触发禁用表单项
        ProhibitFrom: false,
        isEdit: true,
        dialogFormVisible: this.show,
        formLabelWidth: '150px',
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
        that.dialogFormVisible = true
        if (value) {
          that.nowId = value
          that.getTarget(2, value)
        } else {
          that.ProhibitFrom = false
          that.getTarget(0, '')
          that.getTarget(1, '')
        }
      },
      ali() {
        this.corn = this.ruleForm.operationMode === '手动模式' ? false : true
      },
      selectGet(vId) {
        const that = this
        that.obj = this.dataSource.find((item) => {//this.ruleForm
          return item.value === vId;//筛选出匹配数据
        });
      },
      selectGett(vId) {
        const that = this
        that.objj = this.dataTarget.find((item) => {//this.ruleForm
          return item.value === vId;//筛选出匹配数据
        });
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
          const response = await findByIdTaskUpdate(parameter)
          that.loading = false
          that.ruleForm = response.data.data
          if (this.modalObjj === '详情') {
            that.ProhibitFrom = true
            that.isEdit = false
            // 禁用编辑器
          } else if (this.modalObjj === '编辑') {
            this.corn = that.ruleForm.operationMode === '手动模式' ? false : true
            that.ProhibitFrom = false
            that.isEdit = false
            that.lock = true
          } else {
            that.lock = false
            that.ProhibitFrom = false
            that.isEdit = true
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
        that.ruleForm.tel = ''
        that.ruleForm.remark = ''
        that.ruleForm.name = ''
        that.ruleForm.dataSourceSourceId = ''
        that.ruleForm.sourceCatalog = ''
        that.ruleForm.targetCatalog = ''
        that.ruleForm.dataSourceTargetId = ''
        that.ruleForm.operationMode = ''
        that.ProhibitFrom = false
        that.isEdit = true
        that.corn = false
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
        da.dataSourceSourceName = that.obj.label
        da.dataSourceTargetName = that.objj.label
        that.loading = true
        const response = await insertGroupTask(da)
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
        let da = data
        if (that.obj.label !== '' && that.obj.label !== null && that.obj.label !== undefined) {
          da.dataSourceSourceName = that.obj.label
        }
        if (that.objj.label !== '' && that.objj.label !== null && that.objj.label !== undefined) {
          da.dataSourceTargetName = that.objj.label
        }
        const response = await groupTaskUpdate(da)
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

      async getTarget(type, value) {
        const that = this
        // that.loading = true
        let data = {}
        data.catalog = type === 0 ? that.ruleForm.sourceCatalog : that.ruleForm.targetCatalog
        const response = await getDataSource(data)
        // that.loading = false
        if (response.data.code === 1) {
          if (type === 0) {
            that.ruleForm.dataSourceSourceId = ''
            that.dataSource = []
            response.data.data.content.map(function (v, k) {
              that.dataSource.push({value: v.id, label: v.name})
            })
          } else if (type === 1) {
            that.ruleForm.dataSourceTargetId = ''
            that.dataTarget = []
            response.data.data.content.map(function (v, k) {
              that.dataTarget.push({value: v.id, label: v.name})
            })
          } else if (type === 2) {
            that.dataSource = []
            that.dataTarget = []
            response.data.data.content.map(function (v, k) {
              that.dataTarget.push({value: v.id, label: v.name})
              that.dataSource.push({value: v.id, label: v.name})
            })
            that.initFormDetail(value)
          }
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
  }

  :focus {
    outline: 0;
  }

</style>
