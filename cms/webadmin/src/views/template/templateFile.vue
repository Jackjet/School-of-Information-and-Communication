<template>
  <el-main v-show="show" :v-loading="load">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="upload" icon="el-icon-upload2">上传文件</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="add" icon="el-icon-plus">添加</el-button>
        </el-form-item>
        <template v-if="parentUrl !== ''">
          <el-form-item>
            <el-button type="primary"  size='medium' @click="back" icon="el-icon-back">返回</el-button>
          </el-form-item>
        </template>
      </el-form>
    </el-col>
    <el-col :span="24" style="display: flex;flex-wrap:wrap;min-height: 400px;">
      <div class="block" v-for="item in tableData" :key="item">
        <div style="display: flex;flex-direction: column;margin:10px 10px;">
          <template v-if="item.type === '1'">
            <el-image style="width: 120px; height: 120px; cursor: pointer" :src="require('@/icons/repository/folder.png')" fit="contain" @click="lastFolder(item.name)">
            </el-image>
            <el-button type="text" style="width:110px;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;" @click="lastFolder(item.name)">{{ item.name }}</el-button>
          </template>
          <template v-else-if="item.type === '0'">
            <el-image style="width: 120px; height: 120px; cursor: pointer" :src="require('@/icons/image/html.jpg')" fit="contain" @click="detail(item.name)">
            </el-image>
            <el-button type="text" style="width:110px;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;color: #000000" @click="detail(item.name)">{{ item.name }}</el-button>
          </template>
        </div>
      </div>
    </el-col>
    <Upload ref="upload" @Reload='fetchData'></Upload>
    <TemplateFileAdd ref="templateFileAdd" @Reload='fetchData'></TemplateFileAdd>
  </el-main>
</template>

<script>
import {find} from '@/api/templateFile'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import Upload from '@/views/template/upload.vue'
import TemplateFileAdd from '@/views/template/templateFileAdd.vue'
export default {
  components: { TableList, Pagination, Upload, TemplateFileAdd },
  data() {
    return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '文件名', list: 'fileName'},
        {id: false, type: '', label: '创建时间', list: 'createTime'}
      ],
      isSubmitLoading: false,
      DeletelistiD: [],
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
      parentUrl:'',
      show:false,
      load:false
    }
  },
  mounted() {
    this.show = true
    this.fetchData()
  },

  methods: {
    async fetchData() {
      this.load = true
      try {
        let theRandom = Math.random()
        let res = await find({parentUrl:this.parentUrl, num: theRandom})
        this.tableData = res.data.data
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
      let rootUrl = this.parentUrl
      rootUrl = rootUrl.substr(0,rootUrl.lastIndexOf('/'))
      this.parentUrl = rootUrl
      this.fetchData()
    },

    upload() {
      this.$refs.upload.initial(this.parentUrl);
    },

    deleteConfirm(row){
      this.$confirm('确认删除？')
        .then(_ => {
          this.delete(row);
        })
        .catch(_ => {});
    },

    add(){
      this.$refs.templateFileAdd.initial('添加',this.parentUrl);
    },

    detail(row){
      this.$refs.templateFileAdd.initial('编辑',this.parentUrl,row);
    },
    lastFolder(row){
      this.parentUrl = this.parentUrl+'/'+row
      this.fetchData();
    }
  }
}
</script>
