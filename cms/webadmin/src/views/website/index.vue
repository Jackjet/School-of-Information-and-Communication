<template>
  <el-main class="main">
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
            <el-button type="primary"  size='medium' @click="back" icon="el-icon-back" :style="{ display: backVisible }">返回</el-button>
          </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="fetchData"  icon="el-icon-refresh">刷新</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="add" icon="el-icon-plus">添加</el-button>
        </el-form-item>
        <el-form-item>
          <!-- 文件列表(共{{ dataCount }}条数据) -->
          当前目录{{ currentDir }}
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" style="display: flex;flex-wrap:wrap;min-height: 400px;">
      <div class="block" v-for="item in tableData" :key="item">
        <div style="display: flex;flex-direction: column;margin:10px 10px;">
          <template v-if="item.type === 1">
            <el-image @click="detail(item)" class="folder-style" :src="require('@/icons/website/folder.png')" fit="contain">
            </el-image>
            <el-button type="text" style="width:100px;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;" @click="detail(item)">{{ item.fileName }}</el-button>
          </template>
          <template v-else-if="item.type === 2">
            <el-image style="width: 100px; height: 100px" :src="require('@/icons/website/js.png')" fit="contain">
            </el-image>
            <el-button type="text" class="file-style">{{ item.fileName }}</el-button>
          </template>
          <template v-else-if="item.type === 3">
            <el-image style="width: 100px; height: 100px" :src="require('@/icons/website/css.png')" fit="contain">
            </el-image>
            <el-button type="text" class="file-style">{{ item.fileName }}</el-button>
          </template>
          <template v-else-if="item.type === 4">
            <el-image style="width: 100px; height: 100px" :src="require('@/icons/website/html.png')" fit="contain">
            </el-image>
            <el-button type="text" class="file-style">{{ item.fileName }}</el-button>
          </template>
          <template v-else-if="item.type === 5">
            <el-image style="width: 100px; height: 100px" :src="require('@/icons/website/zip.png')" fit="contain">
            </el-image>
            <el-button type="text" class="file-style">{{ item.fileName }}</el-button>
          </template>
          <template v-else-if="item.type === 6">
            <el-image style="width: 100px; height: 100px" :src="require('@/icons/website/json.png')" fit="contain">
            </el-image>
            <el-button type="text" class="file-style">{{ item.fileName }}</el-button>
          </template>
          <template v-else>
            <el-image style="width: 100px; height: 100px" :src="require('@/icons/website/other.png')" fit="contain">
            </el-image>
            <el-button type="text" class="file-style">{{ item.fileName }}</el-button>
          </template>
        </div>
      </div>
    </el-col>
    <!-- <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col> -->
    <drawer
      :modal-objj='modalObjj'
      ref="callDetaill"
      @Reload='submitLoadingg'>
    </drawer>
    <Add ref="add" @Reload="fetchData"></Add>
  </el-main>
  
</template>

<script>

import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import { getToken } from '@/utils/auth'
import { getList,getOS } from '@/api/website'
import Add from '@/views/website/add.vue'

export default {
  components: { TableList, Pagination , Add },

  data() {
      return {
      drawer: false,
      tableData: [],
      tableHeader: [{id: false, type: '', label: '文件名称', list: 'fileName'},
        {id: false, type: '', label: '文件大小(kb)', list: 'size'},
        {id: false, type: '', label: '更新时间', list: 'updateTime'}
      ],
      tableOpction: {},
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        name: '',
        type: ''
      },
      lastItem: {
        name: '',
        type: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
      dataCount :0,
      parentPath: 'static',
      currentDir: 'static/web',
      backVisible:'none',
      os:2
    }
  },
  created () {
    this.fetchData()
    this.getSysOS()
  },
  methods: {
    async fetchData() {
      const data = {
        dir : this.currentDir
      }
      try {
        const res = await getList(data)
        this.tableData = res.data.data
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    async getSysOS(){
      try {
        const res = await getOS()
        this.os = res.data.data
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
   add: function () {
      this.$refs.add.initial(this.currentDir);
    },
    submitLoadingg(){
      this.fetchData()
    },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },
    SearchNoteList(){
      this.fetchData()
    },
    detail(data){
      this.currentDir = data.path
      this.parentPath = data.parentPath
      this.fetchData()
      this.backVisible = 'block'
    },
    back(){
      this.currentDir = this.parentPath
      if(this.os == 1)
      {
        this.parentPath = this.parentPath.substring(0,this.parentPath.lastIndexOf("\\"))
      }else{
        this.parentPath = this.parentPath.substring(0,this.parentPath.lastIndexOf("/"))
      }
      console.log('currentDir',this.currentDir)
      console.log('parentPath',this.parentPath)
      if(this.parentPath === 'static'){
        this.backVisible = 'none'
      }else{
        this.backVisible = 'block'    
      }
      this.fetchData();
    },
  }
}
</script>
<style lang="scss" scoped>
/deep/ .el-form-item__label{
    color: #000;
}
.main{
  background: #00000000;
  height: 100%;
  width: 100%;
  padding: 0 30px;
  min-height: 600px;
  margin-top: 30px;
  .el-InputForm{
    //background:url('../../../assets/image/filter.png');
    background-size: 100% 100%;
    padding:15px 0 0 20px;

  }
}
.main-top{

}
.work-query {
  display: flex;
  align-items: center;
  padding: 20px;
}
.table-list {
  padding: 0 20px;
  box-sizing: border-box;
}
.table-pagination {
  display: flex;
  justify-content: flex-end;
  padding: 20px;
}
.folder-style {
  color: #19d3ea;
  width: 100px; 
  height: 100px;
  cursor: pointer;  /*鼠标悬停变小手*/
}
.file-style {
  width:100px;
  word-break:normal;
  display:block;
  white-space:pre-wrap;
  overflow:hidden;
  cursor: default;  /*鼠标悬停变小手*/
}
</style>