<template>
  <el-dialog
    :title="modalObj"
    width='35%'
    :close-on-click-modal='false'
    :show-close='true'
    :before-close="CloseModal"
    :visible.sync="isShow">
    <el-form class="fengmian" :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="name"
        label="分组名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input autocomplete="off" maxlength="20" placeholder='请输入分组名称，限制20字以内' v-model="ruleForm.name"></el-input>
      </el-form-item>
    </el-form>
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
    modalObj: {
      type: String,
      default: () => {}
    }
  },
  data () {
    return {
      theId: '',
      ruleForm: {
        name: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入分组名称',
          trigger: ['blur', 'change']
        }]
      },
      isShow: false,
      formLabelWidth: '100px',
      loading: false,
      nowId: ''
    }
  },
  methods: {
    init (flag, data) {
      if (flag) {
        this.ruleForm.name = data.name
        this.theId = data.id
      } else {
        this.ruleForm.name = ''
        this.theId = ''
      }
      this.isShow = true
    },
    Save (ruleForm) {
      const that = this
      // 判断当前表单是否符合提交规则
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }
        if (this.modalObj === '添加') {
          that.add(that.ruleForm)
        } else if (this.modalObj === '编辑') {
          that.edit(that.ruleForm)
        }
      })
    },
    // 添加方法
    async add (data) {
      const that = this
      let url = that.Interface.courseManage.questiongroup.insert
      that.loading = true
      const response = await that.request.dataPost(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success('添加成功')
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

    // 修改方法
    async edit (data) {
      const that = this
      data.id = that.theId
      let url = that.Interface.courseManage.questiongroup.update
      // PUT 请求修改数据
      that.loading = true
      const response = await that.request.dataPut(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success('编辑成功')
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
      this.$refs['ruleForm'].resetFields()
      this.ruleForm.name = ''
      this.theId = ''
      this.isShow = false
    }
  }
}
</script>
