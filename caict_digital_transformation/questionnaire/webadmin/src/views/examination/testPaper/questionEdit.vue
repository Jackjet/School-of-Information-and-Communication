<template>
  <el-dialog
    title="编辑题目"
    width='80%'
    class="chooseTi"
    :close-on-click-modal='false'
    :show-close='true'
    :before-close="CloseModal"
    :visible.sync="isShow">
    <el-col :span="24">
      <el-form class="editor-box">
        <el-form-item
          label='题目'
          label-width='120px'
          style='width:95%'>
          <editor :isClear='isClear'
                  :editor-id="{str: 'editor'}"
                  ref="ChildData">
          </editor>
        </el-form-item>
        <template v-for="(option, i) in optionArr">
          <el-form-item
            :key='i'
            :label='"选项 " + optionLabel[i]'
            label-width='120px'
            style='width:95%'>
            <el-button type="text" v-show="optionArr.length > 2" icon="el-icon-close" @click="doDelete(i)"></el-button>
            <editor :isClear='isClear'
                    :editor-id="{str: 'editor' + i}"
                    :ref="'xuanxiang' + i">
            </editor>
            <el-checkbox v-model="option.choose">设为正确选项</el-checkbox>
          </el-form-item>
        </template>
        <el-form-item
          label=''
          label-width='120px'>
          <div class="add-btn" @click="doAdd">新增选项</div>
        </el-form-item>
        <el-form-item
          label='问题解析'
          label-width='120px'
          style='width:95%'>
          <editor :isClear='isClear'
                  :editor-id="{str: 'parsing'}"
                  ref="parsing">
          </editor>
        </el-form-item>
      </el-form>
    </el-col>
    <div slot='footer' class='dialog-footer'>
      <el-button size='medium'
        @click="CloseModal">取 消
      </el-button>
      <el-button size='medium'
         type='primary'
         @click="Save">确 定
      </el-button>
    </div>
  </el-dialog>
</template>
<script>
import editor from '../../integration/wangEditorNew'
export default {
  components: { editor },
  data () {
    return {
      theId: '',
      theIndex: '',
      method: '',
      optionLabel: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'k'],
      isClear: false,
      optionArr: [{
        detail: '',
        number: '',
        choose: false
      }, {
        detail: '',
        number: '',
        choose: false
      }],
      isShow: false,
      examinationGroupId: ''
    }
  },
  methods: {
    init (id, index) {
      this.theId = id
      this.theIndex = index
      this.findQuestion()
      this.isShow = true
    },
    async findQuestion () {
      const that = this
      try {
        const url = that.Interface.courseManage.question.findById
        that.Loading = true
        const response = await this.request.dataGet(that, url, {id: that.theId})
        that.Loading = false
        if (response.data.code !== 0) {
          let Data = response.data.data
          that.examinationGroupId = Data.question.examinationGroupId
          that.$refs.ChildData.write(Data.question.topic)
          that.$refs.parsing.write(Data.question.problemAnalysis)
          Data.optionList.forEach(element => {
            if (element.isAnswer === '1') {
              element.choose = true
            } else {
              element.choose = false
            }
          })
          that.optionArr = Data.optionList
          setTimeout(() => {
            Data.optionList.forEach((element, index) => {
              that.$refs['xuanxiang' + index][0].write(element.detail)
            })
          }, 300)
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    clear (str) {
      let returnStr = ''
      let isBr = str.substring(str.length - 11)
      if (isBr === '<p><br></p>') {
        returnStr = str.substring(0, str.length - 11)
      } else {
        returnStr = str
      }
      return returnStr
    },
    Save () {
      const that = this
      let type = 0
      let timu = that.$refs.ChildData.fetch()
      if (timu === '<p><br></p>') {
        this.$message.warning('题目不能为空')
        return false
      } else {
        timu = that.clear(timu)
      }
      let flag = false
      that.optionArr.forEach((item, index) => {
        if (that.$refs['xuanxiang' + index][0].fetch() === '<p><br></p>') {
          flag = true
        }
        if (item.choose) {
          type++
          item.isAnswer = '1'
        } else {
          item.isAnswer = '0'
        }
        item.detail = that.clear(that.$refs['xuanxiang' + index][0].fetch())
        item.number = index + 1
      })
      if (flag) {
        this.$message.warning('选项不能为空')
        return false
      }
      let jiexi = that.clear(that.$refs.parsing.fetch())
      let theType = 0
      if (type === 0) {
        this.$message.warning('请设置正确选项')
        return false
      } else if (type === 1) {
        theType = 0
      } else {
        theType = 1
      }
      let data = {
        type: theType,
        topic: timu,
        problemAnalysis: jiexi,
        option: that.optionArr,
        examinationGroupId: that.examinationGroupId
      }
      that.edit(data)
    },

    // 修改方法
    async edit (data) {
      const that = this
      data.id = that.theId
      let url = that.Interface.courseManage.question.update
      // PUT 请求修改数据
      that.loading = true
      const response = await that.request.dataPut(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success('编辑成功')
        that.CloseModal()
        // 重新触发父组件加载loading
        that.$emit('Reload', that.theIndex)
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.data)
        return false
      }
    },
    doDelete (num) {
      const that = this
      that.$confirm('是否确定删除该选项?', '提示', {
        type: 'warning'
      }).then(() => {
        that.optionArr.splice(num, 1)
        that.$message.success('删除成功')
      }).catch(() => {
        return false
      })
    },
    doAdd () {
      if (this.optionArr.length < 10) {
        this.optionArr.push({detail: '', number: '', choose: false})
      } else {
        this.$message.warning('最多不能超过10个选项')
      }
    },
    CloseModal () {
      this.optionArr.splice(0)
      this.optionArr = [{
        detail: '',
        number: '',
        choose: false
      }, {
        detail: '',
        number: '',
        choose: false
      }]
      this.isShow = false
    }
  }
}
</script>
<style lang="scss">
  .chooseTi {
    .menuSeach {
      width: 300px;
      .el-input__suffix-inner {
        height: 100%;
      }
    }
    .el-dialog {
      max-height: 80%;
      overflow-y: auto;

      .editor-box {
        .el-checkbox {
          display: block;
          text-align: left;
        }
        .add-btn {
          cursor: pointer;
          width: 200px;
          height: 50px;
          text-align: center;
          line-height: 50px;
          border: 1px dashed #a2a2a2;

          &:hover {
            border: 1px dashed #90d9ea;
          }
        }
        .el-button {
          position: absolute;
          left: 101%;
          top: 0;
          font-size: 20px;
        }
      }
    }
  }
</style>
