<template>
  <el-drawer
    :title="'选择文件'"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="clearParentUrl"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer">
    <el-scrollbar>
      <el-main v-show="show" :v-loading="load" ref="templateFile">
      <el-col :span="24">
        <el-form :inline="true" class='el-InputForm'>
          <template v-if="parentUrl !== ''">
            <el-form-item>
              <el-button type="primary"  size='small' @click="back" icon="el-icon-back">返回上级</el-button>
            </el-form-item>
          </template>
        </el-form>
      </el-col>
      <el-col :span="24" style="display: flex;flex-wrap:wrap;min-height: 400px;">
        <div class="block" v-for="item in tableData" :key="item">
          <div style="display: flex;flex-direction: column;margin:10px 10px;">
            <template v-if="item.type === '1'">
              <el-image style="width: 50px; height: 50px;cursor: pointer" :src="require('@/icons/repository/folder.png')" fit="contain" @click="lastFolder(item.name)">
              </el-image>
              <el-button type="text" style="width:50px;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;" @click="lastFolder(item.name)">{{ item.name }}</el-button>
            </template>
            <template v-else-if="item.type === '0'">
              <el-image style="width: 50px; height: 50px;cursor: pointer" :src="require('@/icons/image/html.jpg')" fit="contain" @click="checkFile(item.name)">
              </el-image>
              <el-button type="text" style="width:100px; text-align: left;word-break:normal;display:block;white-space:pre-wrap;overflow:hidden;color: #000000" @click="checkFile(item.name)">{{ item.name }}</el-button>
            </template>
          </div>
        </div>
      </el-col>
    </el-main>
    </el-scrollbar>
  </el-drawer>
</template>


<script>
import {find} from '@/api/templateFile'
import TableList from '@/components/table/TableList.vue'
export default {
  components: {TableList},
  data() {
    return {
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '文件名', list: 'fileName'},
        {id: false, type: '', label: '创建时间', list: 'createTime'}
      ],
      dialogFormVisible: false,
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
      load:false,
      fileType : '',
      loading: false
    }
  },

  methods: {
    /**
     * @param title 根据不同状态 展示不同操作
     * @param fileType 使用文件类型
     */
    initial: function (title,fileType) {
      this.fileType = fileType
      this.show = true
      const that = this
      that.dialogFormVisible = true
      that.title = title
      this.fetchData()
    },

    async fetchData() {
      this.load = true
      try {
        let res = await find({parentUrl:this.parentUrl})
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

    /**
     * 选中文件将文件路径保存到session
     * @param name 文件名
     */
    checkFile(name){
      sessionStorage.setItem(this.fileType,this.parentUrl+'/'+name)
      this.clearParentUrl()
    },
    lastFolder(row){
      this.parentUrl = this.parentUrl+'/'+row
      this.fetchData();
    },
    clearParentUrl(){
      const that = this
      this.parentUrl = ''
      that.ProhibitFrom = false
      that.ProhibitFromm = false
      that.dialogFormVisible = false
      that.$emit('Reload')
    }
  }
}
</script>

<style lang="scss">
  .el-drawer {
    .el-drawer {
      .el-drawer__body {
        padding: 10px 20px !important;
      }
    }
    .InputWidth {
      width: 80%;
      min-width: 302px;
    }
    .avatar-border-red .el-upload{
      border:1px dashed #f56c6c !important;
      border-radius: 6px;
      cursor: pointer;
      float: left;
      position: relative;
      overflow: hidden;
      &:hover {
        border-color: #b6b5b5;
      }
      .el-progress {
        position: absolute;
        z-index: 999;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: white;
        .el-progress-circle {
          margin: 13% auto 10%;
        }
      }
    }
    .avatar-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      float: left;
      position: relative;
      overflow: hidden;
      &:hover {
        border-color: #b6b5b5;
      }
      .el-progress {
        position: absolute;
        z-index: 999;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: white;
        .el-progress-circle {
          margin: 13% auto 10%;
        }
      }
    }
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 200px;
      height: 178px;
      line-height: 178px;
      text-align: center;
      cursor: pointer;
      border-radius: 6px;
    }
    .avatar {
      width: 200px;
      height: 180px;
      display: block;
    }
  }

  .edit_main {
    text-align: left;
    padding: 10px 15px;
    border-radius: 5px;
    min-height: 250px;
    background-color: #f5f7fa;
    border:1px solid #e4e7ed;
    color: #c0c4cc;
    cursor: not-allowed;
    img {
      width: auto;
      height: auto;
    }
  }

  .ClassDisable .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    float: left;
    position: relative;
    overflow: hidden;
    cursor: no-drop;
    &:hover {
      border-color: #b6b5b5;
    }
    .el-progress {
      position: absolute;
      z-index: 999;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: white;
      .el-progress-circle {
        margin: 13% auto 10%;
      }
    }
  }
  .el-drawer__body{
    overflow: auto;
  }

  .el-drawer__container ::-webkit-scrollbar{
    display: none;
  }

  .buttonentry {
    float: left;
    text-align: left;
  }
  .a{
    width: 6vw!important;
  }
  .drawer_footer{
    text-align: center;
  }
  :focus {
    outline:0;
  }

</style>
