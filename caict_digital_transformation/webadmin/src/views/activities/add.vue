<template>
  <el-main class="main">
    <div><h4>{{ title }}</h4></div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">

        <el-form-item class="span-center" label="活动标题:" prop="title" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' placeholder="请输入活动标题" size="medium" v-model="ruleForm.title"/>
        </el-form-item>

        <el-form-item class="span-center" label="活动开始时间:" prop="startTime" :label-width="this.formLabelWidth">
          <el-date-picker value-format="yyyy-MM-dd HH:mm:ss" v-model="ruleForm.startTime" type="datetime" placeholder="请输入活动开始时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item class="span-center" label="活动结束时间:" prop="endTime" :label-width="this.formLabelWidth">
          <el-date-picker value-format="yyyy-MM-dd HH:mm:ss" v-model="ruleForm.endTime" type="datetime" placeholder="请输入活动结束时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item class="span-center" label="联系人:" prop="linkmanName" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="请输入活动联系人名称" clearable size="medium" v-model="ruleForm.linkmanName"/>
        </el-form-item>

        <el-form-item class="span-center" label="联系电话:" prop="linkmanPhone" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="请输入活动联系人电话" clearable size="medium" v-model="ruleForm.linkmanPhone"/>
        </el-form-item>

        <el-form-item class="span-center" label="主办方:" prop="sponsor" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="请输入活动主办方名称" clearable size="medium" v-model="ruleForm.sponsor"/>
        </el-form-item>

        <el-form-item class="span-center" label="承办方:" prop="organizer" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="请输入活动承办方名称" clearable size="medium" v-model="ruleForm.organizer"/>
        </el-form-item>

        <el-form-item class="span-center" label="活动内容:" prop="content" :label-width="this.formLabelWidth">
          <el-input :rows='4' type="textarea" placeholder="请描述下活动内容" clearable size="medium" v-model="ruleForm.content"></el-input>
        </el-form-item>

        <el-form-item label="活动主图:" prop="pic" :label-width="this.formLabelWidth">
          <el-upload :action="uploadPicUrl"
                     :headers='headers'
                     ref="uploadPic"
                     list-type="picture-card"
                     :limit="1"
                     :file-list="picFileList"
                     :on-exceed="handleExceed"
                     :on-error="handlePicError"
                     :on-success="handlePicSuccess">

            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}">
              <img class="el-upload-list__item-thumbnail" :src="file.url" alt="活动主图">

              <span class="el-upload-list__item-actions">
                <span class="el-upload-list__item-preview" @click="handlePicPreview(file)">
                  <i class="el-icon-zoom-in"></i>
                </span>
                <span class="el-upload-list__item-delete" @click="handlePicRemove(file)">
                  <i class="el-icon-delete"></i>
                </span>
             </span>

            </div>
          </el-upload>
        </el-form-item>

        <el-form-item label="活动附件:" :label-width="this.formLabelWidth">
          <el-upload
            :action="uploadAttachmentUrl"
            :headers='headers'
            ref="uploadAttachment"
            :on-preview="handleAttachmentPreview"
            :on-remove="handleAttachmentRemove"
            :on-error="handleAttachmentError"
            :on-success="handleAttachmentSuccess"
            :limit="1"
            :on-exceed="handleExceed"
            :file-list="attachmentFileList">
            <el-button size="small" type="primary">上传附件</el-button>
          </el-upload>
        </el-form-item>

        <el-form-item class="span-center" label="活动地址:" prop="address" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="请输入活动地址" clearable size="medium" v-model="ruleForm.address"/>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="resetForm('ruleForm')">返回列表</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm')">确 定</el-button>
        </div>
      </el-form>
    </el-col>


    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>

  </el-main>

</template>

<script>
import {
  add,
  find,
  update,
} from "@/api/activities.js";
import {getToken} from '@/utils/auth'

export default {
  components: {},
  data() {
    return {
      title: '添加活动',
      props: {checkStrictly: true},
      ruleForm: {
        title: '',
        startTime: '',
        endTime: '',
        linkmanName: '',
        linkmanPhone: '',
        sponsor: '',
        organizer: '',
        content: '',
        pic: '',
        attachmentName: '',
        attachmentUrl: '',
        address: '',
      },
      organizationOptions: [],
      options: [],
      formLabelWidth: "120px",

      dialogImageUrl: '',
      dialogVisible: false,
      uploadPicUrl: process.env.VUE_APP_BASE_API + '/webadmin/activities/upload/activities/pic',
      headers: {
        Authorization: 'token ' + getToken(),
      },
      picFileList: [],

      uploadAttachmentUrl: process.env.VUE_APP_BASE_API + '/webadmin/activities/upload/activities/attachment',
      attachmentFileList: [],

      rules: {
        title: [
          {required: true, message: '请输入活动标题', trigger: 'blur'},
        ],
        startTime: [
          {required: true, message: '请输入活动开始时间', trigger: 'blur'},
        ],
        endTime: [
          {required: true, message: '请输入活动结束时间', trigger: 'blur'},
        ],
        linkmanName: [
          {required: true, message: '请输入活动联系人名称', trigger: 'blur'},
        ],
        linkmanPhone: [
          {required: true, message: '请输入活动联系人电话', trigger: 'blur'},
        ],
        sponsor: [
          {required: true, message: '请输入活动主办方名称', trigger: 'blur'},
        ],
        organizer: [
          {required: true, message: '请输入活动承办方名称', trigger: 'blur'},
        ],
        content: [
          {required: true, message: '请描述下活动内容', trigger: 'blur'},
        ],
        pic: [
          {required: true, message: '请添加活动主图', trigger: 'blur'},
        ],
        address: [
          {required: true, message: '请输入活动地址', trigger: 'blur'},
        ],
      },
    }
  },
  created() {
    if (this.$route.query.type === 'edit') {
      this.title = "活动编辑";
      this.getDetail()
    }
  },
  methods: {
    //活动主图相关的
    handlePicRemove(file) {
      this.picFileList = [];
      this.ruleForm.pic = "";
    },
    handlePicPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handlePicSuccess(file) {
      this.ruleForm.pic = file.fileDownloadUri;
    },
    handlePicError(error, file, fileList) {
      this.$refs.uploadPic.clearFiles();
      this.picFileList = [];
      this.$message.error(JSON.parse(error.message).msg)
    },

    handleAttachmentRemove() {
      this.attachmentFileList = [];
      this.ruleForm.attachmentName = "";
      this.ruleForm.attachmentUrl = "";

    },
    handleAttachmentPreview(file) {
    },
    handleAttachmentSuccess(file) {
      this.ruleForm.attachmentName = file.fileName;
      this.ruleForm.attachmentUrl = file.fileDownloadUri;

      let json = {};
      json["name"] = file.fileName;
      json["url"] = process.env.VUE_APP_BASE_API + "/" + file.fileDownloadUri;
      this.attachmentFileList.push(json)
    },
    handleAttachmentError(error, file, fileList) {
      this.$refs.uploadAttachment.clearFiles();
      this.attachmentFileList = [];
      this.$message.error(JSON.parse(error.message).msg)
    },

    handleExceed(files, fileList) {
      this.$message.warning(`当前只允许上传一个文件，超过文件上传数据量`);
    },

    // 详情
    async getDetail() {
      const that = this;
      let data = {id: this.$route.query.id}
      that.loading = true;
      const response = await find(data);
      that.loading = false;
      if (response.data.code === 1) {
        that.ruleForm = response.data.data;
        that.picFileList.push({
          "url": process.env.VUE_APP_BASE_API + "/" + that.ruleForm.pic
        });

        that.attachmentFileList.push({
          "name": that.ruleForm.attachmentName,
          "url": process.env.VUE_APP_BASE_API + "/" + that.ruleForm.attachmentUrl
        })

      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg);
        return false
      }
    },

    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.loading = true;
          // 取值
          const that = this;
          let data = this.ruleForm;
          if (this.$route.query.type === 'edit') {
            // 编辑
            update(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('编辑成功');
                this.resetForm('ruleForm');
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            // 新增
            add(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('添加成功');
                this.resetForm('ruleForm');
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.closeModal();
    },
    // 初始化表单
    closeModal() {
      //清空表单数据
      for (let key in this.ruleForm) {
        this.ruleForm[key] = "";
      }

      this.$router.push({
        path: '/activities/list'
      })
    },
  }
}
</script>
<style lang="scss" scoped>
/deep/ .el-input {
  width: 100% !important;
}

/deep/ .el-cascader {
  width: 100% !important;
}

/deep/ .el-select {
  width: 100% !important;
}

/deep/ .el-form-item__content {
  line-height: 20px;
}

.main {
  display: flex;
  flex-direction: column;
  background: #fff;
  height: 100%;
  width: 95%;
  margin: 0 auto;
  min-height: 600px;
  margin-top: -240px;
  border-radius: 5px;
  padding: 10px 20px;

  .main-center {
    width: 45%;
    margin: 0 0 0 100px;
  }
}

.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}

</style>
