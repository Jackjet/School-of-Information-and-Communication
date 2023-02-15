<template>
  <el-main>
    <el-col :span='24'>
      <tableForm
        ref="trigger"
        :table-form='table'
        @initial='initial'
        @pageChange='pageChange'>
      </tableForm>
    </el-col>
    <drawer
      ref="call"
      :drawer-dialog='mode'
      @Reload='initial'>
    </drawer>
  </el-main>
</template>

<script>

import drawer from '@/components/dialog/dialog.vue'
import tableForm from '@/components/table/SearchTable.vue'

export default {
  components: {
    tableForm, drawer
  },

  data () {
    return {
      table: {
        search: [{
          type: 'input',
          placeholder: '请输入标题1',
          model: 'input'
        }],

        item: [{
          type: 'new',
          method: (row) => {
            this.bindModify(row)
          }
        }],

        list: {
          detaile: true,
          header: [{
            label: '商品名称',
            field: 'title'
          },
          {
            label: '下单时间',
            field: 'createTime'
          },
          {
            label: '付款时间',
            field: 'endTime'
          },
          {
            label: '状态',
            field: 'status'
          },
          {
            label: '商品简介',
            field: 'detaile',
            expand: true
          },
          {
            label: '订单编号',
            field: 'number',
            expand: true
          }],

          data: [{
            title: '20kg',
            createTime: '2018-10-10',
            endTime: '2019-09-09',
            status: '0',
            detaile: '精制',
            number: '46423432',
            value: 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2278742460,3800967477&fm=26&gp=0.jpg'
          }],

          operation: {
            label: '操作',
            event: [{
              label: '详情',
              icon: 'el-icon-document',
              method: (row) => {
                this.detail(row)
              }
            },
            {
              label: '编辑',
              icon: 'el-icon-edit',
              method: (row) => {
                this.modify(row)
              }
            },
            {
              label: '删除',
              icon: 'el-icon-delete',
              method: (row) => {
                this.delete(row)
              }
            }]
          }
        },

        paging: {
          total: 0,
          currentPage: 1,
          limit: 20
        }
      },

      mode: {
        type: 'dialog',
        form: [{
          type: 'input',
          label: '商品标题',
          placeholder: '请输入商品标题',
          model: 'value',
          state: ['detail', 'new', 'modify'],
          action: 'http://upload.qiniup.com',
          token: 'vHg2e7nOh7Jsucv2Azr5FH6omPgX22zoJRWa0FN5:joyy0LLq8Kgf3JWtBI5OF337Kks=:eyJkZWxldGVBZnRlckRheXMiOjEsInNjb3BlIjoianNzZGsiLCJkZWFkbGluZSI6MTU3NTUzOTAyN30=',
          rules: [{
            required: true, message: '请输入商品标题', trigger: 'blur'
          }]
        },{
          type: 'uploadImage',
          label: '商品标题',
          placeholder: '请输入商品标题',
          model: 'value',
          state: ['detail', 'new', 'modify'],
          action: process.env.VUE_APP_BASE_API  + '/webadmin/service/file/uploadRegulationKnowledgeFile',
          token: 'vHg2e7nOh7Jsucv2Azr5FH6omPgX22zoJRWa0FN5:joyy0LLq8Kgf3JWtBI5OF337Kks=:eyJkZWxldGVBZnRlckRheXMiOjEsInNjb3BlIjoianNzZGsiLCJkZWFkbGluZSI6MTU3NTUzOTAyN30=',
          rules: [{
            required: true, message: '请输入商品标题', trigger: 'blur'
          }]
        },{
          type: 'textarea',
          label: '商品标题',
          placeholder: '请输入商品标题',
          model: 'value',
          state: ['detail', 'new', 'modify'],
          action: 'http://upload.qiniup.com',
          token: 'vHg2e7nOh7Jsucv2Azr5FH6omPgX22zoJRWa0FN5:joyy0LLq8Kgf3JWtBI5OF337Kks=:eyJkZWxldGVBZnRlckRheXMiOjEsInNjb3BlIjoianNzZGsiLCJkZWFkbGluZSI6MTU3NTUzOTAyN30=',
          rules: [{
            required: true, message: '请输入商品标题', trigger: 'blur'
          }]
        }],

        event: [{
          bind: 'increase',
          method: (row) => {
            this.increase(row)
          }
        },
        {
          bind: 'modify',
          method: (row) => {
            this.bindModify(row)
          }
        }]
      }
    }
  },

  mounted () {
    this.initial()
  },

  methods: {
    async initial (target = {}) {
      const that = this
      try {
        that.$refs.trigger.loading = false
      } catch (even) {
        that.$message.error(even.msg)
      }
    },

    detail (row) {
      this.$refs.call.popup('商品详情', 'details', row)
    },

    modify (row) {
      this.$refs.call.popup('商品编辑', 'modify', row)
    },

    bindModify (row) {
      this.$refs.call.popup('商品编辑', 'increase', row)
    },

    increase (row) {
      console.log(row)
    },

    delete (row) {
      this.$message.warning('删除')
    },

    pageChange (val) {
      this.table.paging.limit = val.limit
      this.table.paging.currentPage = val.page
    }
  }
}
</script>
