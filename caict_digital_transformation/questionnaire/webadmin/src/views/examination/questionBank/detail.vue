<template>
  <el-dialog
    title="预览"
    width='35%'
    class="yulanti"
    :close-on-click-modal='false'
    :show-close='true'
    :before-close="CloseModal"
    :visible.sync="isShow">
    <div style="text-align: left;line-height: 1.5;">
      <div v-html="data.question.topic"></div>
      <div v-for="(item, i) in data.optionList" style="margin-top: 5px;" :key="i">
        <div class="xuanxiangbox"><i v-if="item.isAnswer === '1'" style="color: #00d6d6;" class="el-icon-success"></i><span class="xuanxiangspan">选项 {{ optionLabel[i] }}: </span><span v-html="item.detail"></span></div>
      </div>
      <div v-html="'解析：' + data.question.problemAnalysis" style="margin-top: 5px; padding: 10px; border: 1px dashed #0000004a;"></div>
    </div>
    <div slot='footer' class='dialog-footer'>
      <el-button size='medium'
        @click="CloseModal()">返 回
      </el-button>
    </div>
  </el-dialog>
</template>

<script>

export default {
  data () {
    return {
      optionLabel: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'k'],
      data: {
        question: {
          topic: '',
          problemAnalysis: ''
        },
        optionList: []
      },
      isShow: false
    }
  },
  methods: {
    init (data) {
      this.findQuestion(data)
      this.isShow = true
    },
    async findQuestion (id) {
      const that = this
      try {
        const url = that.Interface.courseManage.question.findById
        that.Loading = true
        const response = await this.request.dataGet(that, url, {id: id})
        that.Loading = false
        if (response.data.code !== 0) {
          that.data = response.data.data
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    CloseModal () {
      this.data = {
        question: {
          topic: '',
          problemAnalysis: ''
        },
        optionList: []
      }
      this.isShow = false
    }
  }
}
</script>
<style lang="scss">
  .yulanti {
    .el-dialog {
      max-height: 80%;
      overflow-y: auto;

      .xuanxiangbox {
        padding-left: 80px;
        position: relative;

        i {
          position: absolute;
          left: 0px;
          top: 5px;
        }
        .xuanxiangspan {
          position: absolute;
          left: 20px;
          top: 0;
        }
      }
    }
  }
</style>
