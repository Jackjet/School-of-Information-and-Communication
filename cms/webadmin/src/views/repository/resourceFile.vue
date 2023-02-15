<template>
  <el-main v-show="show" :v-loading="load">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
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
          <el-button type="primary"  size='medium' @click="upload" icon="el-icon-upload2">上传文件</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="addFolder" icon="el-icon-plus">添加文件夹</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" style="display: flex;flex-wrap:wrap;min-height: 400px;">
      <div class="block" v-for="item in tableData" :key="item">
        <div style="display: flex;flex-direction: column;margin:10px 10px;">
          <template v-if="item.type === '0'">
            <div @click="detail(item)" class="folder">
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
          <el-button type="text" style="width:120px;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;" @click="deleteConfirm(item)">删除</el-button>
        </div>
      </div>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <Upload ref="upload" @Reload='fetchData'></Upload>
    <AddFolder ref="addFolder" @Reload='fetchData'></AddFolder>
  </el-main>
</template>

<script>
import {fileFindAll,fileInsert,fileDelete} from '@/api/repository'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import Upload from '@/views/repository/upload.vue'
import AddFolder from '@/views/repository/addFolder.vue'
export default {
  components: { TableList, Pagination, Upload,AddFolder },
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
      load:false
    }
  },
  mounted() {
    this.show = true
    let node = {
      repositoryId: this.$route.query.repositoryId,
      parentId: this.$route.query.parentId
    }
    this.parentId = node.parentId
    this.nodes.push(node)

    this.fetchData();
  },

  methods: {
    async fetchData() {
      this.load = true
      let node = this.nodes[this.nodes.length - 1]
      const data = {
        repositoryId: node.repositoryId,
        parentId: node.parentId,
        page: this.currentPage,
        size: this.pageSize
      }
      try {
        let res = await fileFindAll(data)
        this.tableData = res.data.data.content
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
        this.$router.push({
          path: '/repository/index'
        })
      } else {
        nodes = nodes.splice(nodes.length - 2, 1)
        this.nodes = nodes
        let node = this.nodes[this.nodes.length-1]
        this.parentId = node.parentId
        this.fetchData()
      }
    },

    upload() {
      this.$refs.upload.initial(this.nodes[this.nodes.length - 1]);
    },

    deleteConfirm(row){
      this.$confirm('确认删除？')
        .then(_ => {
          this.delete(row);
        })
        .catch(_ => {});
    },
    async delete(row){
      const that = this
      let response = await fileDelete({id:row.id});

      if (response.data.code === 1) {
        // 删除成功后 回调效果
        that.$message.success(response.data.msg)
        that.fetchData()
      } else {
        // 删除失败后 回调失败信息
        that.$message.error(response.data.msg)
      }
    },

    addFolder(){
      this.$refs.addFolder.initial(this.nodes[this.nodes.length - 1]);
    },

    detail(row){
      let node = {
        repositoryId: row.repositoryId,
        parentId: row.id
      }
      this.parentId = node.parentId
      this.nodes.push(node)

      this.fetchData();
    }
  }
}
</script>
<style>
  .folder:hover{
    cursor:pointer;
  }
</style>
