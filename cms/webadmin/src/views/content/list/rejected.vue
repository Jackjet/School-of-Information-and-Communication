<template>
  <el-drawer
    title="驳回原因"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer">
    <el-form ref="ruleForm" :model="ruleForm" v-loading='loading'>
      <el-form-item
        label="驳回原因"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea" disabled v-model="ruleForm.reasonForRejection"></el-input>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>

<script>
import {rejected} from '@/api/audit'
export default {
  components: {
    // 注册组件
    // editor: editor
  },

  props: {
    title: {
      type: String,
      default: () => {
      }
    }
  },

  data() {
    return {
      ruleForm: {
        reasonForRejection: ''
      },
      dialogFormVisible: this.show,
      formLabelWidth: '120px',
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
    initial: function (reasonForRejection) {
      const that = this
      that.dialogFormVisible = true
      that.ruleForm.reasonForRejection = reasonForRejection
    },

    // 关闭模态框 清空所有表单项 为编辑器初始化
    CloseModal() {
      const that = this
      that.ClearTextConeten()
      that.ProhibitFrom = false
    },

    // 初始化表单
    ClearTextConeten() {
      const that = this
      that.$refs['ruleForm'].resetFields()
      that.ruleForm.reasonForRejection = ''
      that.dialogFormVisible = false
    }
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

</style>
