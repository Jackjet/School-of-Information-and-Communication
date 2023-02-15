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
        label="数据源名称:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm' autocomplete="off" v-model="ruleForm.name" placeholder="请输入数据源名称"></el-input>
      </el-form-item>
      <el-form-item v-if="user"
                    prop="userName"
                    label="用户名:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' auto-complete="new-password" v-model="ruleForm.userName" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item v-if="password"
                    prop="password"
                    label="密码:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input clearable type="password" :disabled='ProhibitFrom' show-password auto-complete="new-password" v-model="ruleForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item v-if="ipAddress"
                    :label="namee"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.ipAddress" :placeholder="'请输入'+ tiName"></el-input>
      </el-form-item>
      <el-form-item v-if="zkAddress"
                    prop="zkAddress"
                    label="ZK地址:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.zkAddress" :placeholder="zkAddresss"></el-input>
      </el-form-item>
      <el-form-item v-if="jdbcUrl"
                    prop="jdbcUrl"
                    label="jdbc url:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.jdbcUrl" :placeholder="jdbcUrll"></el-input>
      </el-form-item>
      <el-form-item v-if="jdbcServer"
                    prop="jdbcServer"
                    label="jdbc驱动:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input clearable style="max-width: 450px;" :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.jdbcServer" :placeholder="jdbcServerr"></el-input>
      </el-form-item>
      <el-form-item v-if="address"
                    prop="address"
                    label="地址:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input clearable type="textarea" :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.address" :placeholder="addresss"></el-input>
      </el-form-item>
      <el-form-item v-if="folder"
                    prop="folder"
                    label="目录:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.folder" :placeholder="folderr"></el-input>
      </el-form-item>
      <el-form-item v-if="dbName"
                    prop="dbName"
                    label="数据库名称:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.dbName" placeholder="请输入数据库名称"></el-input>
      </el-form-item>
      <el-form-item v-if="header"
                    prop="header"
                    label="header:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.header" placeholder="请输入header"></el-input>
      </el-form-item>
      <el-form-item v-if="urlParams"
                    prop="urlParams"
                    label="URL参数:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.urlParams" placeholder="请输入URL参数"></el-input>
      </el-form-item>
      <el-form-item v-if="body"
                    prop="body"
                    label="Body:"
                    :label-width="this.formLabelWidth"
                    class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-if="body" v-model="ruleForm.body" placeholder="请输入body"></el-input>
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
      <el-button v-if='isLink && !ProhibitFrom' type="primary" @click="conn">测试连接</el-button>
    </div>
  </el-drawer>
</template>

<script>

  import {insertDataSource, DataSourceUpdate, findByDataSourceId, connect} from '@/api/dataSource'
  import {isMPStrict} from '@/utils/validate'
  import {getToken} from '@/utils/auth'

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
          userName: '',
          password: '',
          ipAddress: '',
          zkAddress: '',
          sql: '',
          jdbcServer: '',
          jdbcUrl: '',
          address: '',
          folder: '',
          dbName: '',
          header: '',
          urlParams: '',
          body: '',
          remark: '',
          type: '',
          catalog: ''
        },
        jdbcUrll: '',
        jdbcServerr: '',
        zkAddresss: '',
        folderr: '',
        addresss: '',
        namee: '',
        user: false,
        password: false,
        jdbcUrl: false,
        jdbcServer: false,
        header: false,
        urlParams: false,
        body: false,
        address: false,
        folder: false,
        remark: false,
        dbName: false,
        ipAddress: false,
        zkAddress: false,
        obj: {},
        rule: {},
        options: [],
        tiName: '',
        rules: {
          name: [{
            required: true,
            message: '请输入数据源名称',
            trigger: ['change', 'blur']
          }],
          userName: [{
            required: true,
            message: '请输入用户名',
            trigger: ['change', 'blur']
          }],
          password: [{
            required: true,
            message: '请输入密码',
            trigger: ['change', 'blur']
          }],
          jdbcUrl: [{
            required: true,
            message: '请输入jdbc url',
            trigger: ['change', 'blur']
          }],
          jdbcServer: [{
            required: true,
            message: '请输入jdbc驱动',
            trigger: ['change', 'blur']
          }],
          zkAddress: [{
            required: true,
            message: '请输入zk地址',
            trigger: ['change', 'blur']
          }],
          dbName: [{
            required: true,
            message: '请输入数据库名称',
            trigger: ['change', 'blur']
          }],
          folder: [{
            required: true,
            message: '请输入目录',
            trigger: ['change', 'blur']
          }],
          address: [{
            required: true,
            message: '请输入地址',
            trigger: ['change', 'blur']
          }]
        },
        action: process.env.VUE_APP_BASE_API + 'webadmin/service/file/uploadRegulationKnowledgeFile',
        headers: {
          Authorization: 'token ' + getToken(),
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
        isLink: true

      }
    },
    watch: {
      show() {
        this.dialogFormVisible = this.show
      }
    },
    created() {
      // alert(1)
      // this.findNodes()

    },
    methods: {
      /**
       * @param value 当前需要操作的数据集
       * @param state 根据不同状态 展示不同操作
       */
      initial: function (value, name) {
        const that = this
        if (name.value === 'MySQL') {
          that.jdbcUrll = 'jdbc:mysql://{host}:{port}/{database}';
          that.jdbcServerr = 'com.mysql.jdbc.Driver';
        } else if (name.value === 'SqlServer') {
          that.jdbcUrll = 'jdbc:SqlServer://{host}:{port}/{database}';
          that.jdbcServerr = 'com.microsoft.sqlserver.jdbc.SQLServerDriver';
        } else if (name.value === 'PostgreSQL') {
          that.jdbcUrll = 'jdbc:postgresql://{host}:{port}/{database}';
          that.jdbcServerr = 'org.postgresql.Driver';
        } else if (name.value === 'Oracle') {
          that.jdbcUrll = 'jdbc:oracle:thin:@//{host}:{port}/{database}';
          that.jdbcServerr = 'oracle.jdbc.OracleDriver';
        } else if (name.value === 'Hive') {
          that.jdbcUrll = 'jdbc:hive2://{host}:{port}/{database}';
          that.jdbcServerr = 'org.apache.hive.jdbc.HiveDriver';
        } else if (name.value === 'HBase') {
          that.zkAddresss = '127.0.0.1:2181';
        } else if (name.value === 'MongoDB') {
          that.addresss = 'mongodb://[username:password@]host1[:port1][,...hostN[:portN]]][/[database][?options]]';
        } else if (name.value === '文件目录') {
          that.folderr = '/folder/subfolder';
        } else if (name.value === 'ElasticSearch') {
          that.addresss = 'http://{host}:{port}/';
        } else if (name.value === 'HTTP') {
          that.addresss = 'http://{host}:{port}/xxx';
        } else if (name.value === 'FTP') {
          that.addresss = 'ftp://{host}:{port}/';
        } else if (name.value === 'HDFS') {
          that.addresss = 'hdfs://{host}:{port}/{path}';
        }
        that.ruleForm.type = name.value
        switch (name.value) {
          case 'MySQL':
          case 'PostgreSQL':
          case 'SqlServer':
          case 'Oracle':
            that.ruleForm.catalog = '关系数据源';
            break;
          case 'Hive':
          case 'ElasticSearch':
          case 'HBase':
          case 'MongoDB':
            that.ruleForm.catalog = '大数据数据源';
            break;
          case '文件目录':
          case 'HTTP':
          case 'HDFS':
          case 'FTP':
            that.ruleForm.catalog = '文件数据源';
            break;
        }
        switch (name.value) {
          case 'PostgreSQL':
          case 'SqlServer':
          case 'Oracle':
          case 'Hive':
            that.namee = 'IP地址:';
            that.tiName = 'IP地址';
            that.user = true;
            that.password = true;
            that.jdbcUrl = true;
            that.jdbcServer = true;
            that.ipAddress = true;
            that.remark = true;
            break;
          // case 'SqlServer':
          // case 'Oracle':
          //   that.namee = 'IP地址:';
          //   that.tiName = 'IP地址';
          //   that.user = true;
          //   that.password = true;
          //   that.jdbcUrl = true;
          //   that.jdbcServer = true;
          //   that.ipAddress = true;
          //   that.remark = true;
          //   break;
          case 'MySQL':
            that.namee = 'Ip地址:';
            that.tiName = 'IP地址';
            that.user = true;
            that.password = true;
            that.jdbcUrl = true;
            that.jdbcServer = true;
            that.ipAddress = true;
            that.remark = true;
            break;
          case 'FTP':
          case 'HDFS':
          case 'ElasticSearch':
            that.namee = 'IP地址:';
            that.tiName = 'IP地址';
            that.user = true;
            that.password = true;
            that.ipAddress = true;
            that.address = true;
            break;
          case 'HBase':
            that.namee = 'IP地址:';
            that.tiName = 'IP地址';
            that.user = true;
            that.password = true;
            that.ipAddress = true;
            that.zkAddress = true;
            break;
          case 'MongoDB':
            that.namee = 'IP地址:';
            that.tiName = 'IP地址';
            that.user = true;
            that.password = true;
            that.ipAddress = true;
            that.dbName = true;
            that.address = true;
            break;
          case '文件目录':
            that.namee = 'IP地址:';
            that.tiName = 'IP地址';
            that.user = true;
            that.password = true;
            that.ipAddress = true;
            that.folder = true;
            that.isLink = false
            break;
          case 'HTTP':
            that.address = true;
            that.header = true;
            that.urlParams = true;
            that.body = true;
            break;
        }
        that.dialogFormVisible = true
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
        that.obj = this.options.find((item) => {//this.ruleForm
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
          const response = await findByDataSourceId(parameter)
          that.loading = false
          that.ruleForm = response.data.data
          if (this.modalObjj === '详情') {
            that.ProhibitFrom = true
            that.ProhibitFromm = true
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
        that.ruleForm.user = ''
        that.ruleForm.password = ''
        that.ruleForm.ipAddress = ''
        that.ruleForm.zkAddress = ''
        that.ruleForm.sql = ''
        that.ruleForm.jdbcServer = ''
        that.ruleForm.address = ''
        that.ruleForm.folder = ''
        that.ruleForm.dbName = ''
        that.ruleForm.header = ''
        that.ruleForm.urlParams = ''
        that.ruleForm.body = ''
        that.ruleForm.remark = ''
        that.ruleForm.type = ''
        that.ruleForm.catalog = ''
        that.ProhibitFrom = false
        that.ProhibitFromm = false
        that.user = false
        that.password = false
        that.jdbcUrl = false
        that.jdbcServer = false
        that.header = false
        that.urlParams = false
        that.body = false
        that.address = false
        that.folder = false
        that.remark = false
        that.dbName = false
        that.ipAddress = false
        that.zkAddress = false
        that.isLink = true
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
        // da.roleName = that.obj.label
        that.loading = true
        const response = await insertDataSource(da)
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
          this.$router.push({path: "/dataSource/index"})
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },

      // 添加方法
      async conn(data) {
        const that = this
        // POST请求添加轮播图数据
        let da = that.ruleForm
        // da.roleName = that.obj.label
        that.loading = true
        const response = await connect(da)
        that.loading = false
        if (response.data.code === 1) {
          // 添加成功后 回调效果
          that.$message.success(response.data.msg)

        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
      handleSuccess(response, file, fileList) {
        this.ruleForm.fileName = response.fileName
        this.ruleForm.file = response.fileDownloadUri
      },
      handlePreview(file) {
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制只能选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${file.name} ？`)
      },
      // 修改方法
      async edit(data) {
        const that = this
        // PUT 请求修改数据
        that.loading = true
        const response = await DataSourceUpdate(data)
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
          this.$router.push({path: "/dataSource/index"})
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },

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
      min-width: 352px;
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
