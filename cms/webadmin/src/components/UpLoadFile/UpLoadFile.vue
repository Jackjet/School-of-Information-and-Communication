<template>
  <el-tabs type="border-card" class='el-tabs-tab' @tab-click='RunFalse'
    v-loading="isSubmitLoading"
    element-loading-text="数据加载中">
    <template v-for="Upname in Upload">
      <el-tab-pane v-if="Upname.flag" :label='Upname.TitleName' :key='Upname.id'>
        <!-- 上传按钮 -->
        <el-row v-if="typeAccess == '0'">
          <el-col :span="24">
            <el-upload class="upload-demo"
              :accept='Upname.accept'
              :action='fileUrl'
              :headers='header'
              :show-file-list='false'
              v-show='true'
              :before-upload='handleContentChange'
              :on-success='handleAvatarSuccess'>
              <el-button size="medium" type="primary" @click="judgetype(Upname.id)">{{Upname.UploadName}}</el-button>
              <div slot="tip" class="el-upload__tip">{{Upname.loadTips}}</div>
            </el-upload>
          </el-col>
        </el-row>
        <el-row :gutter="20" class='lagin'>
          <el-col :span="24" class="uploadmain">
            <template v-if="Upname.content.length === 0">
              <i class="el-icon-info"></i>&nbsp;{{No_data}}
            </template>
            <el-col :span="6" v-for="file in Upname.content" :key='file.type'>
              <div class="grid-content bg-purple">
                <!-- 图片 -->
                <template v-if="typeAccess === '0'">
                  <el-image
                    v-if="file.resourceType === '0'"
                    style="height: 160px;"
                    :src="file.resourceAbsolutePath"
                    :preview-src-list="[file.resourceAbsolutePath]"
                    @click="Selection(file.resourcePath)">
                  </el-image>
                </template>
                <template v-else>
                  <img
                    v-if="file.resourceType === '0'"
                    :src="file.resourceAbsolutePath"
                    style="width: 100%;height: 160px;"
                    class="gesture"
                    @click="Selection(file.resourcePath)" />
                </template>
                  <!-- 压缩包 -->
                <div class='border' v-if="file.resourceType !== '0'">
                  <img src="../../assets/image/zip.png" class='zipImage'/>
                </div>
                <!-- 删除按钮 -->
                <div class='file_name'>
                  <span>{{file.resourceName}}</span>
                  <el-button
                    type="danger"
                    class='delete'
                    icon="el-icon-delete"
                    circle
                    v-if="typeAccess == '0'"
                    @click="AvatarDelete(file.id)"
                  ></el-button>
                </div>
              </div>
            </el-col>
          </el-col>
        </el-row>
        <div class="Page" :key='Upname.id' style="height: 5%; float:right; padding: 1% 30px 0px 0px">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :page-sizes="[20, 50, 100, 200]"
            :current-page="Upname.currentPage"
            :page-size="Upname.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="Upname.total">
          </el-pagination>
        </div>
      </el-tab-pane>
    </template>
  </el-tabs>
</template>

<script>
export default {
  data () {
    return {
      isSubmitLoading: true,
      dialogVisible: false,
      dialogImageUrl: '',
      No_data: '',
      Flug: '',
      Upload: [{
        id: 0,
        UploadName: '点击上传图片',
        TitleName: '图片',
        loadTips: '仅支持上传JPG JPEG PNG GIF文件',
        accept: '.jpg,.jpeg,.png,.gif,.JPG,.JPEG,.GIF',
        content: [],
        total: 0,
        pageSize: 20,
        currentPage: 1
      },
      {
        id: 1,
        UploadName: '点击上传压缩包',
        TitleName: 'ZIP压缩包',
        loadTips: '仅支持上传ZIP格式压缩包',
        accept: '.rar,.zip,.RAR,.ZIP',
        content: [],
        total: 0,
        pageSize: 20,
        currentPage: 1
      },
      {
        id: 2,
        UploadName: '点击上传PDF',
        TitleName: 'PDF',
        loadTips: '仅支持上传PDF格式文件',
        accept: '.pdf,.PDF',
        content: [],
        total: 0,
        pageSize: 20,
        currentPage: 1
      }],
      fileList: [],
      pageSize: 20,
      currentPage: 1,
      type: 0,
      // fileUrl: process.env.VUE_APP_URL + this.api.resourceLib.upload,
      // header: {
      //   'Authorization': 'token ' + this.common.session('currentUser').token
      // }
    }
  },
  /**
   *  @folderId 文件夹id
   *  @typeAccess 进入入口类型 ---> 0：文件夹  1：缩略图
   */
  props: ['folderId', 'typeAccess'],
  mounted () {
    let that = this
    console.log(this.typeAccess)
    this.Upload.forEach(item => {
      if (this.typeAccess === '0') {
        item.flag = true
      } else {
        if (item.TitleName === '图片') {
          item.flag = true
        }
      }
    })
    that.getList()
  },
  methods: {
    Selection: function (even) {
      if (this.typeAccess === '0') {
        return false
      }
      if (this.typeAccess === '1') {
        this.$emit('formChild', even)
        return false
      }
    },
    // 上传本地表单数据
    judgetype: function (index) {
      this.Flug = index
    },
    handleContentChange (file) {
      let that = this
      let FileSize = file.size / 1024 / 1024 < 500
      if (!FileSize) {
        that.$message.error('上传超出文件限制大小')
        return false
      }
      this.isSubmitLoading = true
    },
    handleAvatarSuccess (response, file, fileList) {
      let that = this
      if (response.code === 1) {
        that.isSubmitLoading = false
        that.UploadFile(response.data)
        return false
      }
      this.dialogVisible = true
      this.result = response.msg
      that.isSubmitLoading = false
    },

    async UploadFile (res) {
      const that = this
      try {
        let that = this
        let url = that.api.resourceLib.insert
        let obj = {
          folderId: that.folderId,
          resourceType: that.Flug,
          resourceFormat: res.fileLongPath,
          resourceName: res.fileName,
          resourcePath: res.filePath
        }
        const response = await this.request.dataPost(that, url, obj)
        if (response.code === 1) {
          that.$message({
            message: response.msg,
            type: 'success'
          })
          that.isSubmitLoading = false
          for (let i in that.Upload) {
            if (that.Upload[i].id === that.Flug) {
              that.Upload[i].content = []
            }
          }
          that.getList()
          return false
        }
        that.isSubmitLoading = false
        that.$message.error(response.msg)
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },

    handleSizeChange (val, even) {
      let id = 0
      let that = this
      that.Upload.forEach(function (index, item) {
        if (parseInt(index.id) === parseInt(that.type)) {
          index.content.splice(0, index.content.length)
        }
      })
      this.getList(val, id)
    },

    handleCurrentChange (size) {
      let id = 1
      let that = this
      that.Upload.forEach(function (index, item) {
        if (parseInt(index.id) === parseInt(that.type)) {
          index.content.splice(0, index.content.length)
        }
      })

      this.getList(size, id)
    },

    RunFalse (tab, event) {
      let that = this
      that.type = tab.index
      // that.isSubmitLoading = true
      // 遍历数组 是否请求重载当前数据
      that.Upload.forEach(function (index, item) {
        if (index.id === parseInt(that.type)) {
          if (index.content.length === 0) {
            that.getList()
          } else {
            that.isSubmitLoading = false
            return false
          }
        }
      })
    },
    // 初始化请求数据文件列表
    async getList (value, Contents) {
      const that = this
      try {
        let that = this
        // let parameters = {
        //   resourceType: that.type,
        //   folderId: that.folderId
        // }
        // if (value !== undefined && value !== null) {
        //   if (Contents === 0) {
        //     that.pageSize = value
        //   } else {
        //     that.currentPage = value
        //   }
        // } else {
        //   that.pageSize = 20
        //   that.currentPage = 1
        // }
        // let url = that.api.resourceLib.findAll + '?size=' + that.pageSize + '&page=' + that.currentPage
        // const response = await this.request.dataGet(that, url, parameters)
        // if (response.code === 1) {
          that.isSubmitLoading = false
          let Data = [{"id":"1f506049cc8c4ebbaf197355e43ac6c7","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591345348067.png","resourcePath":"/yanglao/data/20200605/1591345348067.png","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345348067.png","createById":"admin","createByName":"管理员","createTime":"2020-06-05 16:22:28","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345348067.png"},{"id":"c53ee5b8a0044bb1b6781cc784070f43","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591345344714.png","resourcePath":"/yanglao/data/20200605/1591345344714.png","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345344714.png","createById":"admin","createByName":"管理员","createTime":"2020-06-05 16:22:25","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345344714.png"},{"id":"4ecb50788cec43d295f8974253932d34","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591345341494.png","resourcePath":"/yanglao/data/20200605/1591345341494.png","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345341494.png","createById":"admin","createByName":"管理员","createTime":"2020-06-05 16:22:22","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345341494.png"},{"id":"eb87891875a049beb7cf5a3732c9bd5e","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591345337904.png","resourcePath":"/yanglao/data/20200605/1591345337904.png","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345337904.png","createById":"admin","createByName":"管理员","createTime":"2020-06-05 16:22:18","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345337904.png"},{"id":"c249134bdafb4826a9dd245c1f94312b","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591345333676.png","resourcePath":"/yanglao/data/20200605/1591345333676.png","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345333676.png","createById":"admin","createByName":"管理员","createTime":"2020-06-05 16:22:14","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345333676.png"},{"id":"2d1672dd9e9344acbfcfde1483a6c7c4","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591345330416.png","resourcePath":"/yanglao/data/20200605/1591345330416.png","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345330416.png","createById":"admin","createByName":"管理员","createTime":"2020-06-05 16:22:10","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345330416.png"},{"id":"973b08a07dc84fb186bb3f54ef7fdeae","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591345327074.png","resourcePath":"/yanglao/data/20200605/1591345327074.png","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345327074.png","createById":"admin","createByName":"管理员","createTime":"2020-06-05 16:22:07","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345327074.png"},{"id":"6e05dbabe3f24091a356a83924f3902f","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591345323228.png","resourcePath":"/yanglao/data/20200605/1591345323228.png","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345323228.png","createById":"admin","createByName":"管理员","createTime":"2020-06-05 16:22:03","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345323228.png"},{"id":"44f63b9855cf4208a668272c0c304136","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591345316592.png","resourcePath":"/yanglao/data/20200605/1591345316592.png","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345316592.png","createById":"admin","createByName":"管理员","createTime":"2020-06-05 16:21:57","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200605/1591345316592.png"},{"id":"3aa9d1be73314534afe7197225283df5","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591243475883.jpg","resourcePath":"/yanglao/data/20200604/1591243475883.jpg","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200604/1591243475883.jpg","createById":"admin","createByName":"管理员","createTime":"2020-06-04 12:04:36","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200604/1591243475883.jpg"},{"id":"8fa9084481124d74b7bcef9f3943e7a4","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591243470426.jpg","resourcePath":"/yanglao/data/20200604/1591243470426.jpg","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200604/1591243470426.jpg","createById":"admin","createByName":"管理员","createTime":"2020-06-04 12:04:30","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200604/1591243470426.jpg"},{"id":"210211ace8444f88b5ad0ca74fa099d3","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591243465731.jpg","resourcePath":"/yanglao/data/20200604/1591243465731.jpg","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200604/1591243465731.jpg","createById":"admin","createByName":"管理员","createTime":"2020-06-04 12:04:26","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200604/1591243465731.jpg"},{"id":"bb18846f141a4bcc8f010a5fbfe2c00d","folderId":"d641b538689a45a08d925c444c80547c","resourceName":"1591243455429.jpg","resourcePath":"/yanglao/data/20200604/1591243455429.jpg","resourceType":"0","resourceFormat":"http://47.105.160.248/resourceLib/yanglao/data/20200604/1591243455429.jpg","createById":"admin","createByName":"管理员","createTime":"2020-06-04 12:04:16","updateById":null,"updateByName":null,"updateTime":null,"resourceAbsolutePath":"http://47.105.160.248/resourceLib/yanglao/data/20200604/1591243455429.jpg"}]
          // if (Data.length === 0) {
          //   that.No_data = '暂无数据'
          // }
          // 按时间进行数据排序
          Data.sort(function (arr, brr) {
            let x = arr['createTime']
            let y = brr['createTime']
            return ((x > y) ? -1 : ((x < y) ? 1 : 0))
          })
          that.Upload.map((itemUpload) => {
            Data.filter((itemData) => {
              if (parseInt(itemData.resourceType) === itemUpload.id) {
                itemUpload.content.push(itemData)
                itemUpload.total = response.data.totalElements
              }
            })
          })
        // } else {
        //   that.isSubmitLoading = false
        //   that.$message.error('文件夹下没有资源')
        // }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },

    // 移除当前选中文件
    AvatarDelete (even) {
      const that = this
      that.$confirm('您确定要删除么?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = that.api.resourceLib.deleteById + '?id=' + even
        const response = await this.request.dataDelete(that, url, {})
        that.$message.success(response.msg)
        that.isSubmitLoading = false
        that.Upload.forEach(function (index, item) {
          if (parseInt(index.id) === parseInt(that.type)) {
            index.content.splice(0, index.content.length)
          }
        })
        that.getList()
      }).catch((response) => {
        that.$message.error(response.msg)
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
  .gesture{
    cursor: pointer;
  }
  .el-main {
    flex-basis: 0% !important;
  }

  .el-tabs-tab{
    width:90% !important;
    margin-left:5%;
  }

  .el_tab_alage {
    width: 92%;
    border:1px solid #f0f0f0;
    /* margin-left:4%; */
    margin-bottom: 10px;
  }

  .el-tabs-tab .el-tabs__item {
    padding:0 30px !important;
    height: 50px;
    line-height: 50px;
  }

  .upload-demo {
    float: left;
  }

  .upload-demo .el-upload__tip {
    display: inline-block;
    margin-left: 20px;
  }

  .uploadmain {
    margin-top: 20px;
    color: #b4b4b4;
    font-size: 14px;
  }

  .uploadmain .grid-content {
    margin-bottom: 20px;
  }

  .border img {
    width: 100%;
    display: inline-block;
    vertical-align: top;
    height: 160px;
  }

  .file_name {
    width: 100%;
    height: 30px;
  }

  .file_name > span {
    display: inline-block;
    font-size: 14px;
    vertical-align:middle;
    line-height: 30px;
    overflow:hidden;
    text-overflow:ellipsis;
    white-space:nowrap;
    text-align: left;
  }

  .file_name > span:nth-child(1) {
    float: left;
    width: 75%;
    color: #606266;
  }

  .file_name > span:nth-child(2) {
    float: right;
    width: 20%;
    text-align: right;
  }

  .delete {
    font-size: 14px;
    float: right;
    cursor: pointer;
    border-radius: 3px !important;
    padding: 3px 10px !important;
    margin-top: 3px !important;
  }

  audio {
    width: 100% !important;
  }

  video {
    width: 100% !important;
    height: 150px;
    border: 1px solid rgb(228, 228, 228);
  }

  .border {
    height: 150px;
    border: 1px solid rgb(228, 228, 228);
  }

  .zipImage {
    margin-top: 20px;
    height: 120px !important;
    width: 100px !important;
  }

  @media screen and (max-width: 1030px) {
   .file_name > span:nth-child(1) {
      float: left;
      width: 50%;
      color: #606266;
    }
  }

  @media screen and (max-width: 750px) {
   .file_name > span:nth-child(1) {
      float: left;
      width: 20%;
      color: #606266;
    }
  }

  .lagin {
    width: 100% !important;
  }
</style>
