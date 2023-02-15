<template>
  <el-drawer
    title="资源库上传"
    direction="rtl"
    v-loading="load"
    :visible.sync="show"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer"
    :append-to-body="true">
    <el-scrollbar style="margin-left: 20px;width: 90%">
      <el-col :span="24">
        <el-form :inline="true" class='el-InputForm' style="margin-bottom: 45px;">
          <template v-if="parentId === ''">
            <el-form-item>
              <el-button type="primary"  size='medium' @click="back" icon="el-icon-back">返回</el-button>
            </el-form-item>
          </template>
          <template v-else>
            <el-form-item>
              <el-button type="primary"  size='medium' @click="back" icon="el-icon-top">返回上级</el-button>
            </el-form-item>
          </template>
          <el-form-item>
            <el-button type="primary"  size='medium' @click="sure" icon="el-icon-check">确定使用</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" style="display: flex;flex-wrap:wrap;min-height: 400px;">
        <div class="block" v-for="item in tableData" :key="item">
          <div style="display: flex;flex-direction: column;margin:10px 10px;" :ref="item.id" @click="choose(item)" class="black_border">
            <template v-if="item.type === '0'">
              <div class="folder" @click="detail(item)">
                <el-image style="width: 120px; height: 120px" :src="require('@/icons/repository/folder.png')" fit="contain">
                </el-image>
                <span style="width:120px;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;text-align:center;">{{ item.fileName }}</span>
              </div>
            </template>
            <template v-else-if="item.type === '2'">
              <el-image style="width: 120px; height: 120px" :src="require('@/icons/repository/zip.png')" fit="contain">
              </el-image>
              <span style="width:120px;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;text-align:center;">{{ item.fileName }}</span>
            </template>
            <template v-else-if="item.type === '3'">
              <el-image style="width: 120px; height: 120px" :src="require('@/icons/repository/pdf.png')" fit="contain">
              </el-image>
              <span style="width:120px;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;text-align:center;">{{ item.fileName }}</span>
            </template>
            <template v-else>
              <el-image style="width: 120px; height: 120px" :src="item.fileDownloadUri" fit="contain">
              </el-image>
              <span style="width:120px;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;text-align:center;">{{ item.fileName }}</span>
            </template>
          </div>
        </div>
      </el-col>
      <el-col :span='24' style="margin-bottom: 45px;">
        <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      </el-col>
    </el-scrollbar>
  </el-drawer>
</template>

<script>
import {findAllRepository,fileFindAll} from '@/api/column'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '文件名', list: 'fileName'},
        {id: false, type: '', label: '创建时间', list: 'createTime'}
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [{
          label: '详情',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            this.detail(row)
          }
        },{
          label: '删除',
          key: 0,
          type: 'danger',
          State: false,
          method: (row) => {
            this.deleteConfirm(row)
          }
        }]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        repositoryId: '',
        parentId: ''
      },
      lastItem: {
        name: '',
        phone: '',
        userName: '',
        Timeframe: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      nodes:[],
      parentId:'',
      show:false,
      load:false,
      chosen:[],
      borderClass:'black_border',
      isSingle:false
    }
  },

  methods: {
    initial(id,isSingle) {
      this.show = true
      let node = {
        repositoryId: id,
        parentId: ''
      }
      this.parentId = node.parentId
      this.nodes.push(node)
      this.isSingle = isSingle
      this.fetchData();
    },
    async fetchData() {
      this.load = true
      let node = this.nodes[this.nodes.length - 1]
      this.currentPage=1
      const data = {
        repositoryId: node.repositoryId,
        parentId: node.parentId,
        page: this.currentPage,
        size: this.pageSize
      }
      try {
        let res = await fileFindAll(data)
        let tableData = res.data.data.content
        tableData.forEach(item => {
          item.chosen = false
        })
        this.tableData = tableData
        this.total = res.data.data.totalElements
        this.load = false
      } catch (even) {
        this.load = false
        this.$message.error(even.msg)
      }
    },

    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData( this.nodes[this.nodes.length] )
    },

    back(){
      let nodes = this.nodes

      if(nodes.length === 1){
        nodes = []
        this.nodes = nodes
        this.show = false
        this.parentId = ''
      } else {
        nodes = nodes.splice(nodes.length - 2, 1)
        this.nodes = nodes
        let node = this.nodes[this.nodes.length-1]
        this.parentId = node.parentId
        this.fetchData()
      }
    },

    detail(row){
      let node = {
        repositoryId: row.repositoryId,
        parentId: row.id
      }
      this.parentId = node.parentId
      this.nodes.push(node)

      this.fetchData();
    },
    async ClearTextConeten (done) {
      const that = this
      that.show = false
    },
    choose(box){
      const that = this
      box.chosen = !box.chosen
      if(box.chosen){
        that.$refs[box.id][0].style.borderColor='#20a0ff'
        that.$refs[box.id][0].style.borderStyle='solid'
      }else{
        that.$refs[box.id][0].style.borderColor='#001F28'
        that.$refs[box.id][0].style.borderStyle=''
      }
    },
    sure(){
      let checkData = new Array()

      this.tableData.forEach(item => {
        if(item.chosen){
          checkData.push(item)
        }
      })

      this.show = false

      this.$emit('sure', checkData)
    }
  }
}
</script>
<style lang="scss">
  .blue_border{
    border-color: #20a0ff;
    border-style: solid;
  }

  .black_border{
    border-color: #001F28;
  }

  /*1.显示滚动条：当内容超出容器的时候，可以拖动：*/
  .el-drawer__body {
    overflow: auto;
    /* overflow-x: auto; */
  }

  /*2.隐藏滚动条，太丑了*/
  .el-drawer__container ::-webkit-scrollbar{
    display: none;
  }

  .folder:hover{
   cursor:pointer;
  }
</style>
