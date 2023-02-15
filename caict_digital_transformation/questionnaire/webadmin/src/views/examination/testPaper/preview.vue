<template>
  <el-main>
    <el-form class="preview-top">
      <el-form-item label="考试名称" label-width="70px" class='InpitWidth'>
        <el-input autocomplete="off" disabled v-model="name"></el-input>
      </el-form-item>
    </el-form>
    <div class="preview-box">
      <template v-for="(ti, i) in tiArr">
        <div class="shijuan-ti" :key="ti.questionId">
          <span class="ti-type">{{ theType[ti.type] }}</span>
          <div>{{i+1}} 、<span class="neirongqu" v-html="ti.topic"></span></div>
          <template v-for="item in ti.optionList">
            <div class="xuanx" :key="item.id"><i v-if="item.isAnswer === '1'" style="color: #00d6d6;" class="el-icon-success"></i>{{ optionLabel[item.number-1] }} 、 <span class="neirongqu" v-html="item.detail"></span></div>
          </template>
          <div class="fen-box">分值<el-input style="width: auto;" disabled v-model="ti.score" @change="fenzhichange"></el-input>分</div>
        </div>
      </template>
    </div>
    <div style="padding-bottom: 20px;">
      <el-button
        size='medium'
        @click="back">返 回
      </el-button>
    </div>
  </el-main>
</template>
<script>
export default {
  data () {
    return {
      theId: '',
      name: '',
      danxuanFen: 0,
      danxuanTi: 0,
      duoxuanFen: 0,
      duoxuanTi: 0,
      theType: ['单选题', '多选题'],
      optionLabel: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'k'],
      tiIdArr: [],
      tiArr: []
    }
  },
  mounted () {
    if (this.$route.query.id) {
      this.theId = this.$route.query.id
      this.findExamination()
    } else {
      this.theId = ''
    }
  },
  methods: {
    async findExamination () {
      const that = this
      try {
        const url = that.Interface.courseManage.examination.findById
        const response = await this.request.dataGet(that, url, {id: this.theId})
        if (response.data.code !== 0) {
          let Data = response.data.data
          that.name = Data.name
          that.tiArr.splice(0)
          that.tiIdArr.splice(0)
          Data.question.forEach(item => {
            let ti = {
              questionId: item.id,
              type: item.type,
              score: item.score,
              topic: item.topic,
              optionList: []
            }
            item.option.forEach(ele => {
              ti.optionList.push({
                id: ele.id,
                detail: ele.detail,
                number: ele.number,
                isAnswer: ele.isAnswer
              })
            })
            that.tiArr.push(ti)
            that.tiIdArr.push(item.id)
          })
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    back () {
      const that = this
      that.$router.push({path: 'examination'})
    }
  }
}
</script>
<style lang="scss">
  .preview-top {
    margin: 0 auto;
    width: calc(100% - 400px);
  }
  .preview-box {
    padding: 10px;
    margin: 0 auto;
    background-color: #fafafa;
    width: calc(100% - 420px);

    .shijuan-ti {
      text-align: left;
      position: relative;
      border: 1px dashed #0000004d;
      line-height: 1.5;
      padding: 0 10px 60px 80px;
      margin-bottom: 20px;

      img {
        max-width: 100%;
      }

      .xuanx {
        position: relative;
        padding-top: 15px;
        border-top: 1px dashed #0000004a;
        i {
          position: absolute;
          left: -25px;
          top: 20px;
        }
      }
      span {
        display: inline;
        width: auto;
        padding: 0 5px;
        p {
          display: inline;
        }
      }
      .ti-type {
        position: absolute;
        left: 10px;
        top: 3px;
        font-weight: 600;
      }
      .fen-box {
        position: absolute;
        left: 0;
        bottom: 0;
        width: calc(100% - 40px);
        height: 40px;
        line-height: 40px;
        background-color: #0000004d;
        padding-left: 40px;
        .el-input__inner {
          color: #000;
          margin: 0 10px;
          height: 20px;
          width: 80px;
          text-align: center;
          background-color: #ffffff00;
        }
      }
    }
  }
  .setNum {
    background-color: #eee;
    width: 340px;
    text-align: center;
    position: absolute;
    right: 30px;
    top: 80px;
    padding: 20px 0;

    p {
      line-height: 2;
    }

    .allNum {
      width: 80%;
      margin: 0 auto;
      border-bottom: 1px dashed #0a0a0a47;
    }
    .el-button {
      margin-top: 20px;
    }
  }
</style>
