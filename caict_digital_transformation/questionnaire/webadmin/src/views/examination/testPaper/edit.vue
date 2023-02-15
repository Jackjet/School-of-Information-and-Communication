<template>
  <el-main>
    <el-form class="shijuan-top">
      <el-form-item label="考试名称" label-width="70px" class='InpitWidth'>
        <el-input autocomplete="off" maxlength="20" placeholder='请输入考试名称，限制20字以内' v-model="name"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button style="width: 100%; height: 50px; border-width: 2px; border-style: dashed;" size='medium' icon="el-icon-add" @click="choose">从题库中选择题目添加
        </el-button>
      </el-form-item>
    </el-form>
    <div class="shijuan-box">
      <template v-for="(ti, index) in tiArr">
        <div class="shijuan-ti" :key="index">
          <span class="ti-type">{{ theType[ti.type] }}</span>
          <div>{{index+1}} 、<span class="neirongqu" v-html="ti.topic"></span></div>
          <template v-for="item in ti.optionList">
            <div class="xuanx" :key="item.id"><i v-if="item.isAnswer === '1'" style="color: #00d6d6;" class="el-icon-success"></i>{{ optionLabel[item.number-1] }} 、 <span class="neirongqu" v-html="item.detail"></span></div>
          </template>
          <div class="fen-box">分值<el-input style="width: auto;" v-model="ti.score" @change="fenzhichange"></el-input>分</div>
          <div class="btn-box">
            <el-button v-if="index !== 0" size='medium' type='text' @click="moveUp(index)">上移</el-button>
            <el-button size='medium' type='text' @click="editQuestion(index)">编辑</el-button>
            <el-button size='medium' type='text' @click="deteleThis(index)">删除</el-button>
            <el-button v-if="(index+1) !== tiArr.length" size='medium' type='text' @click="moveDown(index)">下移</el-button>
          </div>
        </div>
      </template>
    </div>
    <div class="setNum">
      <h3>试卷设置</h3>
      <p class="allNum">共{{ tiArr.length }}题，共计{{ Number(danxuanFen) + Number(duoxuanFen) }}分</p>
      <p>单选题&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共{{ danxuanTi }}题，共计{{ danxuanFen }}分</p>
      <p>多选题&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共{{ duoxuanTi }}题，共计{{ duoxuanFen }}分</p>
      <el-button size='medium'  @click="toSetScore">批量修改分数</el-button>
    </div>
    <!-- 调用操作弹层 -->
    <theDialog
      ref="callDetail"
      @Reload='addTi'>
    </theDialog>
    <!-- 调用操作弹层 -->
    <theChange ref="callChange" @Reload='changeQuestion'></theChange>
    <!-- 调用操作弹层 -->
    <theScore
      ref="callScore"
      :danxuan-fen='danxuanFen'
      :danxuan-ti='danxuanTi'
      :duoxuan-fen='duoxuanFen'
      :duoxuan-ti='duoxuanTi'
      @Reload='changeFen'>
    </theScore>
    <div style="padding-bottom: 20px; width: calc(100% - 420px);">
      <el-button
        size='medium'
        @click="back">取 消
      </el-button>
      <el-button type="primary"
        size='medium'
        @click="save">确 定
      </el-button>
    </div>
  </el-main>
</template>
<script>
import theDialog from './choose.vue'
import theScore from './score.vue'
import theChange from './questionEdit.vue'
export default {
  components: { theDialog, theScore, theChange },
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
    choose () {
      this.$refs.callDetail.init()
    },
    moveUp (num) {
      let theId = this.tiIdArr[num]
      let theData = this.tiArr[num]
      this.tiArr.splice(num, 1)
      this.tiIdArr.splice(num, 1)
      this.tiArr.splice(num - 1, 0, theData)
      this.tiIdArr.splice(num - 1, 0, theId)
    },
    deteleThis (num) {
      const that = this
      that.$confirm('请再次确认是否删除该题?', '提示', {
        type: 'warning'
      }).then(() => {
        this.tiArr.splice(num, 1)
        this.tiIdArr.splice(num, 1)
        this.fenzhichange()
      }).catch(() => {
        return false
      })
    },
    editQuestion (index) {
      this.$refs.callChange.init(this.tiIdArr[index], index)
    },
    async changeQuestion (index) {
      let data = await this.findQuestion(this.tiIdArr[index])
      data.score = this.tiArr[index].score
      this.tiArr.splice(index, 1)
      this.tiArr.splice(index, 0, data)
    },
    moveDown (num) {
      let theId = this.tiIdArr[num]
      let theData = this.tiArr[num]
      this.tiArr.splice(num, 1)
      this.tiIdArr.splice(num, 1)
      this.tiArr.splice(num + 1, 0, theData)
      this.tiIdArr.splice(num + 1, 0, theId)
    },
    async findQuestion (id) {
      const that = this
      const url = that.Interface.courseManage.question.findById
      const response = await this.request.dataGet(that, url, {id: id})
      if (response.data.code !== 0) {
        let Data = response.data.data
        let ti = {
          questionId: Data.question.id,
          type: Data.question.type,
          score: '0',
          topic: Data.question.topic,
          optionList: []
        }
        Data.optionList.forEach(item => {
          ti.optionList.push({
            id: item.id,
            detail: item.detail,
            number: item.number,
            isAnswer: item.isAnswer
          })
        })
        return ti
      }
    },
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
          this.fenzhichange()
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    toSetScore () {
      this.$refs.callScore.init()
    },
    changeFen (num1, num2) {
      this.tiArr.forEach(item => {
        if (item.type === '0' && num1 !== '') {
          item.score = num1
        } else if (item.type === '1' && num2 !== '') {
          item.score = num2
        }
      })
      this.fenzhichange()
    },
    fenzhichange () {
      let danxuan = 0
      let duoxuan = 0
      let danxuanTi = 0
      let duoxuanTi = 0
      this.tiArr.forEach(item => {
        if (item.type === '0') {
          danxuan += Number(item.score)
          danxuanTi++
        } else {
          duoxuan += Number(item.score)
          duoxuanTi++
        }
      })
      this.danxuanFen = danxuan
      this.danxuanTi = danxuanTi
      this.duoxuanFen = duoxuan
      this.duoxuanTi = duoxuanTi
    },
    back () {
      const that = this
      that.$router.push({path: 'examination'})
    },
    save () {
      const that = this
      if (!that.name || that.name === '') {
        that.$message.warning('请输入考试名称，建议20字以内')
        return false
      }
      if (that.tiArr.length < 1) {
        that.$message.warning('请选择题目')
        return false
      }
      let data = {
        name: that.name,
        totalSorce: Number(that.danxuanFen) + Number(that.duoxuanFen),
        question: []
      }
      that.tiArr.forEach((item, i) => {
        data.question.push({
          questionId: item.questionId,
          number: i + 1,
          score: Number(item.score),
          type: item.type
        })
      })
      if (this.theId === '') {
        that.add(data)
      } else {
        that.edit(data)
      }
    },
    // 添加方法
    async add (data) {
      const that = this
      let url = that.Interface.courseManage.examination.insert
      that.loading = true
      const response = await that.request.dataPost(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success('添加成功')
        that.back()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.data)
        return false
      }
    },

    // 修改方法
    async edit (data) {
      const that = this
      data.id = that.theId
      let url = that.Interface.courseManage.examination.update
      // PUT 请求修改数据
      that.loading = true
      const response = await that.request.dataPut(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success('编辑成功')
        that.back()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.data)
        return false
      }
    },
    addTi (data) {
      data.forEach(async item => {
        let data = await this.findQuestion(item)
        this.tiIdArr.push(item)
        this.tiArr.push(data)
      })
    }
  }
}
</script>
<style lang="scss">
  .shijuan-top {
    width: calc(100% - 400px);
  }
  .shijuan-box {
    padding: 10px;
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
          margin: 0 10px;
          height: 20px;
          width: 80px;
          text-align: center;
          background-color: #ffffff00;
        }
      }
      .btn-box {
        position: absolute;
        left: 101%;
        top: 0;
        .el-button {
          margin-left: 0;
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
