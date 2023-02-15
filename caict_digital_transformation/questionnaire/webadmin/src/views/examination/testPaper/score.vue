<template>
  <el-dialog
    title="分值设置"
    width='35%'
    class="yulanti"
    :close-on-click-modal='false'
    :show-close='true'
    :before-close="CloseModal"
    :visible.sync="isShow">
    <div style="text-align: center;line-height: 1.5;">
      <div class="allNum">批量设置分值，共{{ Number(danxuanTi) + Number(duoxuanTi) }}题，共{{ Number(danxuanFen) + Number(duoxuanFen) }}分</div>
      <el-row>
        <el-col :span="4">题目类型</el-col>
        <el-col :span="12">分值设置</el-col>
        <el-col :span="4">题目数量</el-col>
        <el-col :span="4">总分值</el-col>
      </el-row>
      <el-row>
        <el-col :span="4">单选题</el-col>
        <el-col :span="12">每题得
          <el-input @change="fen1change" v-model="fen1"></el-input>分
        </el-col>
        <el-col :span="4">{{ danxuanTi }}</el-col>
        <el-col :span="4">{{ danxuanFen }}</el-col>
      </el-row>
      <el-row>
        <el-col :span="4">多选题</el-col>
        <el-col :span="12">每题得
          <el-input @change="fen2change" v-model="fen2"></el-input>分
        </el-col>
        <el-col :span="4">{{ duoxuanTi }}</el-col>
        <el-col :span="4">{{ duoxuanFen }}</el-col>
      </el-row>
    </div>
    <div slot='footer' class='dialog-footer'>
      <el-button size='medium'
        @click="CloseModal()">取 消
      </el-button>
      <el-button size='medium'
         type='primary'
         @click="Save('ruleForm')">确 定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>

export default {
  props: {
    danxuanFen: {
      type: Number,
      default: () => {}
    },
    danxuanTi: {
      type: Number,
      default: () => {}
    },
    duoxuanFen: {
      type: Number,
      default: () => {}
    },
    duoxuanTi: {
      type: Number,
      default: () => {}
    }
  },
  data () {
    return {
      fen1: '',
      fen2: '',
      isShow: false
    }
  },
  methods: {
    init () {
      this.isShow = true
    },
    fen1change () {
      this.danxuanFen = Number(this.fen1) * Number(this.danxuanTi)
    },
    fen2change () {
      this.duoxuanFen = Number(this.fen2) * Number(this.duoxuanTi)
    },
    Save () {
      this.$emit('Reload', this.fen1, this.fen2)
      this.CloseModal()
    },
    CloseModal () {
      this.fen1 = ''
      this.fen2 = ''
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

      .allNum {
        position: relative;
        top: -20px;
        text-align: left;
      }
      .el-input {
        width: auto;

        .el-input__inner {
          width: 60px;
          height: 20px;
          text-align: center;
          display: inline-block;
        }
      }
    }
  }
</style>
