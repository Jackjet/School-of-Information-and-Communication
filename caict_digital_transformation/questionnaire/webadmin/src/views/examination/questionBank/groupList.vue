<template>
  <el-dialog
    title="移动分组"
    width='50%'
    v-loading="Loading"
    :close-on-click-modal='false'
    :show-close='true'
    :before-close="CloseModal"
    :visible.sync="isShow">
    <tableForm ref="tableList" :table-data='navMenus'
               :table-selection="tableSelection"
               @onHandleTemplateRow="change"
               :table-label="tableHeader"
               :table-option="tableOption">
    </tableForm>
    <div slot='footer' class='dialog-footer'>
      <el-button size='medium' @click="CloseModal">取 消</el-button>
      <el-button size='medium' type='primary' @click="Save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import tableForm from '../../integration/tableList'

export default {
  components: { tableForm },
  props: {
    navMenus: {
      type: Array,
      default: () => {}
    }
  },
  data () {
    return {
      theId: [],
      toId: '',
      Loading: false,
      isShow: false,
      tableSelection: {
        key: true,
        type: 'radio'
      },
      tableHeader: [{
        id: false,
        type: '',
        label: '分组名称',
        list: 'name'
      },
      {
        id: false,
        type: '',
        label: '题目数量',
        list: 'questionNumber'
      }],
      tableOption: {
        label: '操作',
        value: 1231231,
        options: []
      }
    }
  },
  methods: {
    init (data) {
      this.theId = data
      console.log(this.navMenus)
      this.isShow = true
    },
    change (ele) {
      this.toId = ele.id
    },
    async Save () {
      const that = this
      if (that.toId === '') {
        this.$message.warning('请先选择需要目标分组')
        return false
      }
      let url = that.Interface.courseManage.question.move
      let data = {
        ids: this.theId,
        examinationGroupId: this.toId
      }
      that.Loading = true
      const response = await that.request.dataPost(that, url, data)
      that.Loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.CloseModal()
        // 重新触发父组件加载loading
        that.$emit('Reload')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.data)
        return false
      }
    },

    CloseModal () {
      this.theId.splice(0)
      this.toId = ''
      this.$refs.tableList.initRadio()
      this.isShow = false
    }
  }
}
</script>
