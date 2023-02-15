<template>
  <el-main class="main">
    <div style="width: 90%">
      <h4>{{ title }}</h4>
    </div>
    <el-col class="main-center">
      <el-form
        :model="ruleForm"
        ref="ruleForm"
        label-width="150px"
        class="demo-ruleForm"
      >
        <el-form-item
          class="span-center"
          label="活动标题:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.title }}</span>
        </el-form-item>

        <el-form-item
          class="span-center"
          label="活动时间:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{
            ruleForm.startTime + "至" + ruleForm.endTime
          }}</span>
        </el-form-item>

        <el-form-item
          class="span-center"
          label="联系人:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.linkmanName }}</span>
        </el-form-item>

        <el-form-item
          class="span-center"
          label="联系电话:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.linkmanPhone }}</span>
        </el-form-item>

        <el-form-item
          class="span-center"
          label="主办方:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.sponsor }}</span>
        </el-form-item>

        <el-form-item
          class="span-center"
          label="承办方:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.organizer }}</span>
        </el-form-item>

        <el-form-item
          class="span-center"
          label="活动内容:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.content }}</span>
        </el-form-item>

        <el-form-item label="活动主图:" :label-width="this.formLabelWidth">
          <el-image
            class="img"
            :src="ruleForm.pic"
            fit="contain"
            @click="handlePicPreview"
          />
        </el-form-item>

        <el-form-item label="活动附件:" :label-width="this.formLabelWidth">
          <span
            style="display: block; margin-top: 10px"
            class="a-tag"
            @click="downloadAttachment(ruleForm.attachmentUrl)"
            >{{ ruleForm.attachmentName }}</span
          >
        </el-form-item>

        <el-form-item
          class="span-center"
          label="活动地址:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.address }}</span>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="resetForm('ruleForm')"
            >返回列表</el-button
          >
        </div>
      </el-form>
    </el-col>

    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
  </el-main>
</template>

<script>
import { find, resetPassword } from "@/api/activities.js";
import { getToken } from "@/utils/auth";

export default {
  components: {},
  data() {
    return {
      title: "活动详情",
      ruleForm: {
        title: "",
        time: "",
        linkmanName: "",
        linkmanPhone: "",
        sponsor: "",
        organizer: "",
        content: "",
        pic: "",
        attachmentName: "",
        attachmentUrl: "",
        address: "",
      },
      organizationOptions: [],
      options: [],
      formLabelWidth: "120px",
      dialogImageUrl: "",
      dialogVisible: false,
    };
  },
  created() {
    this.getDetail();
  },
  methods: {
    handlePicPreview() {
      this.dialogImageUrl = this.ruleForm.pic;
      this.dialogVisible = true;
    },
    downloadAttachment(url) {
      let a = document.createElement("a");
      a.href = url;
      a.click();
    },

    // 详情
    async getDetail() {
      const that = this;
      let data = { id: this.$route.query.id };
      that.loading = true;
      const response = await find(data);
      that.loading = false;
      if (response.data.code === 1) {
        that.ruleForm = response.data.data;
        //替换null值
        for (let key in that.ruleForm) {
          if (that.ruleForm[key] === null) {
            that.ruleForm[key] = "";
          }
        }

        this.ruleForm.pic =
          process.env.VUE_APP_BASE_API + "/" + this.ruleForm.pic;
        this.ruleForm.attachmentUrl =
          process.env.VUE_APP_BASE_API + "/" + this.ruleForm.attachmentUrl;
      } else {
        that.$message.error(response.data.msg);
        return false;
      }
    },

    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.closeModal();
    },
    // 初始化表单
    closeModal() {
      for (let key in this.ruleForm) {
        this.ruleForm[key] = "";
      }

      this.$router.push({
        path: "/activities/list",
      });
    },
  },
};
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

// /deep/ .el-form-item__content {
//   line-height: 20px;
// }

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
    width: 80%;
    margin: 0 0 0 100px;
  }
}
.el-form-item {
  margin-bottom: 0;
}

.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}

// /deep/ .el-form-item {
//   margin-bottom: 0px;
// }

// .span-center {
//   display: flex;
//   align-items: center;
// }

// .span {
//   margin-left: -120px;
// }

.img {
  width: 220px;
  height: auto;
  margin-top: 15px;
  margin-left: 5px;
  cursor: pointer;
}
</style>
