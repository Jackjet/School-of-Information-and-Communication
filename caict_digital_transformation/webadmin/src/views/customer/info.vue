<template>
  <el-main class="main">
    <div style="width: 90%">
      <h4>{{ title }}</h4>
    </div>
    <el-col class="main-center">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="150px"
        class="demo-ruleForm"
      >
        <el-form-item label="公司LOGO:" :label-width="this.formLabelWidth">
          <el-image
            class="img"
            style="width: 50px; height: 50px"
            :src="ruleForm.logo"
            fit="contain"
            @click="handlePicPreview(ruleForm.logo)"
          />
        </el-form-item>

        <el-form-item
          class="span-center"
          label="客户名称:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.name }}</span>
        </el-form-item>

        <el-form-item
          class="span-center"
          label="客户来源:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.source }}</span>
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
          label="地址:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ getDetailAddress() }}</span>
        </el-form-item>

        <el-form-item
          class="span-center"
          label="所属行业:"
          :label-width="this.formLabelWidth"
        >
          <span class="span">{{ ruleForm.industries }}</span>
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
import { find } from "@/api/customer.js";
import { getToken } from "@/utils/auth";

export default {
  components: {},
  data() {
    return {
      title: "客户详情",
      isShow: false,
      industriesData: {},
      ruleForm: {
        logo: "",
        name: "",
        linkmanName: "",
        linkmanPhone: "",
        province: "",
        city: "",
        county: "",
        address: "",
        industries: "",
      },
      organizationOptions: [],
      options: [],
      formLabelWidth: "120px",

      dialogImageUrl: "",
      dialogVisible: false,
    };
  },
  created() {
    this.title = "客户详情";
    this.userDetail();
    this.isShow = false;

    this.industriesData = this.$route.params.industriesData;
    if (this.industriesData === undefined) {
      this.industriesData = JSON.parse(
        sessionStorage.getItem("industries_data")
      );
    } else {
      sessionStorage.setItem(
        "industries_data",
        JSON.stringify(this.industriesData)
      );
    }
  },
  methods: {
    handlePicPreview(file) {
      this.dialogImageUrl = file;
      this.dialogVisible = true;
    },

    // 详情
    async userDetail() {
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

        let industriesTemp = that.ruleForm.industries;
        if (industriesTemp !== undefined && industriesTemp !== null) {
          let industryArray = industriesTemp.split(",");
          let industries = "";
          industryArray.map(function (value, key) {
            let item = that.industriesData[value];
            if (item !== undefined) {
              industries += "、" + item;
            }
          });
          that.ruleForm.industries = industries.substr(1);
        }
      } else {
        that.$message.error(response.data.msg);
        return false;
      }
    },
    getDetailAddress() {
      return (
        this.ruleForm.province +
        this.ruleForm.city +
        this.ruleForm.county +
        this.ruleForm.address
      );
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.closeModal();
    },
    // 初始化表单
    closeModal() {
      this.isShow = false;

      for (let key in this.ruleForm) {
        this.ruleForm[key] = "";
      }

      this.$router.push({
        path: "/customer/list",
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
    width: 90%;
    margin: 0 0 0 100px;
  }
}

.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}

/deep/ .el-form-item {
  margin-bottom: 0px;
}

.span {
  margin-left: -120px;
}

.span-center {
  display: flex;
  align-items: center;
}

.img {
  width: 80px;
  height: 100px;
  margin-top: 15px;
  margin-left: 5px;
  cursor: pointer;
}
</style>
