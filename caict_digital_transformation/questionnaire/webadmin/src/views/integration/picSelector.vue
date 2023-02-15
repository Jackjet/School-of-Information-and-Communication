<template>
  <el-dialog
    title="素材选择"
    class="scrollDialog"
    width="80%"
    top="5vh"
    :append-to-body="true"
    :close-on-click-modal="false"
    :before-close="closeDialog"
    :visible.sync="dialogVisible"
    v-loading="dialogLoading">
    <el-col :span="24">
      <span style="font-size: 18px">本地上传</span>
      <el-upload
        class="upload-demo"
        style="margin: 20px 0 20px 0"
        ref="upload"
        :before-upload='beforeUpload'
        :on-success='onUploadSuccess'
        :action='uploadOpt.url'
        :data='QiniuToken'
        :name='uploadOpt.fileFieldName'>
        <el-button size="small" type="primary">选择文件</el-button>
      </el-upload>
    </el-col>
    <el-col>
      <span style="font-size: 18px">素材库选择</span>
    </el-col>
    <el-col :span="24">
      <el-tabs style="margin-top: 20px" clearable v-model="currentType" type="border-card" @tab-click="tabChanged">
        <el-tab-pane label="图 片" name="0"  v-if="uploadOpt.fileType === 'pic' || uploadOpt.fileType === 'fwb'">
          <el-col :span='4'>
            <div class="groupDiv">图片分组</div>
            <el-tree
              class="group-tree"
              :data="groupData"
              node-key="id"
              default-expand-all
              highlight-current
              @node-click="onGroupClick"
              :expand-on-click-node="false">
              <span class="custom-tree-node" slot-scope="{ node, data }">
                <span class="myTreeNum">({{ data.num }})</span>
                <div  class="myTreeLabel">
                  <span>{{ data.name }}</span>
                </div>
              </span>
            </el-tree>
          </el-col>
          <el-col :span='20'>
            <el-form :inline="true" class='el-InputForm'>
              <el-form-item>
                <el-input
                  clearable
                  size="medium"
                  autocomplete="off"
                  v-model="lastSearch.name"
                  placeholder="请输入图片名称">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  size='medium'
                  icon="el-icon-search"
                  @click="search">搜索
                </el-button>
              </el-form-item>
            </el-form>
            <tableForm :table-data='tableData'
                       :table-selection="tableSelection"
                       :table-label="mData[0].tableHeader"
                       :table-option="tableOption">
            </tableForm>
            <paging ref='pager0' :total="total" @pageChange="pageChange"></paging>
          </el-col>
        </el-tab-pane >
        <el-tab-pane label="视 频" name="1" v-if="uploadOpt.fileType === 'video'">
          <el-col :span='4'>
            <div class="groupDiv">视频分组</div>
            <el-tree
              class="group-tree"
              :data="groupData"
              node-key="id"
              default-expand-all
              highlight-current
              @node-click="onGroupClick"
              :expand-on-click-node="false">
              <span class="custom-tree-node" slot-scope="{ node, data }">
                <span class="myTreeNum">({{ data.num }})</span>
                <div  class="myTreeLabel">
                  <span>{{ data.name }}</span>
                </div>
              </span>
            </el-tree>
          </el-col>
          <el-col :span='20'>
            <el-form :inline="true" class='el-InputForm'>
              <el-form-item>
                <el-input
                  clearable
                  size="medium"
                  autocomplete="off"
                  v-model="lastSearch.name"
                  placeholder="请输入视频名称">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  size='medium'
                  icon="el-icon-search"
                  @click="search">搜索
                </el-button>
              </el-form-item>
            </el-form>
            <tableForm :table-data='tableData'
                       :table-selection="tableSelection"
                       :table-label="mData[1].tableHeader"
                       :table-option="tableOption">
            </tableForm>
            <paging ref='pager1' :total="total" @pageChange="pageChange"></paging>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="音 频" name="2" v-if="uploadOpt.fileType === 'audio'">
          <el-col :span='4'>
            <div class="groupDiv">音频分组</div>
            <el-tree
              class="group-tree"
              :data="groupData"
              node-key="id"
              default-expand-all
              highlight-current
              @node-click="onGroupClick"
              :expand-on-click-node="false">
              <span class="custom-tree-node" slot-scope="{ node, data }">
                <span class="myTreeNum">({{ data.num }})</span>
                <div  class="myTreeLabel">
                  <span>{{ data.name }}</span>
                </div>
              </span>
            </el-tree>
          </el-col>
          <el-col :span='20'>
            <el-form :inline="true" class='el-InputForm'>
              <el-form-item>
                <el-input
                  clearable
                  size="medium"
                  autocomplete="off"
                  v-model="lastSearch.name"
                  placeholder="请输入音频名称">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  size='medium'
                  icon="el-icon-search"
                  @click="search">搜索
                </el-button>
              </el-form-item>
            </el-form>
            <tableForm :table-data='tableData'
                       :table-selection="tableSelection"
                       :table-label="mData[2].tableHeader"
                       :table-option="tableOption">
            </tableForm>
            <paging ref='pager2' :total="total" @pageChange="pageChange"></paging>
          </el-col>
        </el-tab-pane>
      </el-tabs>
    </el-col>
    <div slot='footer' class='maskLoading dialog-footer'  v-loading='dialogLoading'>
      <div style="text-align: center">
        <el-button size='medium' style="margin-top: 20px" @click="closeDialog">取 消
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>
<script>
import tableForm from './tableList'
import paging from './pagination'

export default {
  components: {tableForm, paging},

  data () {
    return {
      dialogVisible: false, // 显隐开关
      tableData: [], // 列表数据
      groupData: [], // 分组数据
      SearchItem: {
        name: ''
      },
      lastSearch: {
        name: ''
      },
      tableSelection: {
        key: false,
        type: 'selection'
      },
      mData: [
        {
          tableHeader: [{
            id: false,
            type: 'imgTxt',
            label: '图片名称',
            width: 400,
            covor: 'pic',
            list: 'name'
          }, {
            id: false,
            type: '',
            label: '大小(kb)',
            list: 'size'
          }, {
            id: false,
            type: '',
            label: '上传时间',
            list: 'createTime'
          }]
        },
        {
          tableHeader: [{
            id: false,
            type: 'imgTxt',
            label: '视频名称',
            width: 400,
            covor: 'pic',
            list: 'name'
          }, {
            id: false,
            type: '',
            label: '大小(kb)',
            list: 'size'
          }, {
            id: false,
            type: '',
            label: '上传时间',
            list: 'createTime'
          }]
        },
        {
          tableHeader: [{
            id: false,
            type: 'imgTxt',
            label: '音频名称',
            width: 400,
            covor: 'pic',
            list: 'name'
          }, {
            id: false,
            type: '',
            label: '大小(kb)',
            list: 'size'
          }, {
            id: false,
            type: '',
            label: '上传时间',
            list: 'createTime'
          }]
        }
      ],
      tableOption: {
        label: '操作',
        value: 0,
        options: [{
          label: '选择',
          key: 0,
          type: 'text',
          icon: 'el-icon-edit',
          State: false,
          method: (row) => {
            this.onItemSelected(row)
          }
        }]
      },
      dialogLoading: false,
      total: 0,
      pageSize: 20,
      currentPage: 1,
      currentType: '0', // 当前类型
      materialGroupId: '0', // 当前分组Id
      QiniuToken: {
        token: ''
      },
      // BaoliToken: {
      //   Authorization: ''
      // },
      requestCode: '',
      uploadOpt: {
        url: '',
        autoUpload: true,
        showFileList: false,
        fileType: '' // 上传类型
      }
    }
  },

  methods: {
    // region 基础业务
    initAndShow (value) {
      this.requestCode = value
      this.dialogVisible = true
      this.switchMode()
      this.GetToken()
    },
    // 请求token
    async GetToken () {
      const that = this
      // if (that.requestCode === 'audioFile' || that.requestCode === 'videoFile') {
      //   that.initial(false, false, false)
      // } else {
      try {
        const url = that.Interface.QiniuToken.Storage
        const response = await this.request.dataGet(that, url, {})
        if (response.data.code === 1) {
          that.QiniuToken.token = response.data.data
          that.initial(false, false, false)
        } else {
          that.$message.error('请求token获取失败！')
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
      // }
    },

    // 初始化获取当前全部列表数据
    async initial (come2FirstPage, searchOnlyList, saveSearchCondition) {
      const that = this
      that.tableData = []
      that.selectedItems = []
      if (!saveSearchCondition) {
        that.lastSearch.name = ''
      }
      if (come2FirstPage) {
        that.currentPage = 1
        that.$refs['pager' + that.currentType].changePage()
        that.$refs['pager' + that.currentType].changePageSize(that.pageSize)
      }
      if (!searchOnlyList) {
        that.getGroupThenListData()
      } else {
        that.getListData()
      }
    },
    async getGroupThenListData () {
      const that = this
      try {
        var url = that.Interface.courseManage.materialGroup.findAll
        var param = {
          type: that.currentType
        }
        that.dialogLoading = true
        const response = await this.request.dataGet(that, url, param)
        that.dialogLoading = false
        that.groupData = response.data.data.content
        that.groupData.forEach(item => {
          item.nameShow = item.name
        })
        that.getListData()
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    async getListData () {
      const that = this
      try {
        var findUrl = that.Interface.courseManage.material.findAll
        let url = findUrl + '?page=' + that.currentPage +
          '&size=' + that.pageSize +
          '&type=' + that.currentType +
          '&materialGroupId=' + that.materialGroupId
        Object.entries(that.SearchItem).map((item, index) => {
          that.SearchItem[item[0]] = that.lastSearch[item[0]]
        })
        that.dialogLoading = true
        const response = await this.request.dataGet(that, url, this.SearchItem)
        that.dialogLoading = false
        if (response.data.code === 1) {
          that.tableData = response.data.data.content
          that.tableData.forEach(item => {
            item.size = item.size.toFixed(2)
          })
        } else {
          that.$message.error(response.data.data)
        }

        that.total = response.data.data.totalElements
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    // 模糊查询
    search: function () {
      const that = this
      that.currentPage = 1
      that.$refs['pager' + that.currentType].changePage()
      this.initial(false, true, true)
    },
    // 获取搜索当前分页 页码 条数
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.initial(false, true, true)
    },

    tabChanged (item, event) {
      var that = this
      that.materialGroupId = that.currentType
      that.initial(true, false, false)
    },

    onGroupClick (data) {
      this.materialGroupId = data.id
      this.initial(true, true, true)
    },
    // endregion

    switchMode () {
      const that = this
      if (that.requestCode === 'fuWenBen') {
        that.uploadOpt.fileType = 'fwb'
        that.uploadOpt.url = 'https://upload-z2.qiniup.com'
        that.uploadOpt.autoUpload = false
        that.uploadOpt.showFileList = false
      } else if (that.requestCode === 'audioFile') {
        that.uploadOpt.fileType = 'audio'
        that.uploadOpt.url = that.Interface.Baoliweishi.polyvUploadVideo
        that.uploadOpt.autoUpload = false
        that.uploadOpt.showFileList = false
        that.uploadOpt.fileFieldName = 'Filedata'
        that.currentType = '2'
        that.materialGroupId = that.currentType
      } else if (that.requestCode === 'videoFile') {
        that.uploadOpt.fileType = 'video'
        that.uploadOpt.url = that.Interface.Baoliweishi.polyvUploadVideo
        that.uploadOpt.autoUpload = false
        that.uploadOpt.showFileList = false
        that.uploadOpt.fileFieldName = 'Filedata'
        that.currentType = '1'
        that.materialGroupId = that.currentType
      } else {
        that.uploadOpt.fileType = 'pic'
        that.uploadOpt.url = 'https://upload-z2.qiniup.com'
        that.uploadOpt.autoUpload = false
        that.uploadOpt.showFileList = false
      }
    },

    /**
     * 上传图片素材库
     * @param file 文件
     * @param res 包含key和hash
     */
    async materialPicInsert (file, res) {
      const that = this
      let url = that.Interface.material.insert
      const param = {
        name: file.name,
        pic: this.Interface.QiNiuHttp + res.key,
        vid: '',
        size: file.size / 1024,
        materialGroupId: '0',
        type: '0'
      }
      that.dialogLoading = true
      const response = await that.request.dataPost(that, url, param)
      that.dialogLoading = false
      if (response.data.code === 1) {
        var result = {
          requestCode: that.requestCode,
          url: param.pic
        }
        that.$emit('itemSelected', result)
        that.closeDialog()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.data)
      }
    },
    /**
     * 上传其他素材库
     * @param file 文件
     * @param res 包含key和hash
     */
    async materialPolInsert (name, pic, size, type, vid) {
      const that = this
      let url = that.Interface.material.insert
      const param = {
        name: name,
        pic: pic,
        vid: vid,
        size: size / 1024,
        materialGroupId: type,
        type: type
      }
      that.dialogLoading = true
      const response = await that.request.dataPost(that, url, param)
      that.dialogLoading = false
      if (response.data.code === 1) {
        var resultFile = {
          requestCode: that.requestCode,
          url: vid
        }
        that.$emit('itemSelected', resultFile)
        that.closeDialog()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.data)
      }
    },

    beforeUpload (file) {
      const that = this
      if (that.uploadOpt.fileType === 'fwb') {
        if (file.type !== 'image/png' && file.type !== 'image/jpeg' && file.type !== 'image/jpg' && file.type !== 'image/gif' && file.type !== 'video/mp4' && file.type !== 'audio/mp3' && file.type !== 'audio/mpeg' && file.type !== 'video/rmvb' && file.type !== 'video/avi' && file.type !== 'video/mp4' && file.type !== 'video/MOV') {
          this.$message.warning('上传文件格式有误！')
          return false
        }
      } else if (that.uploadOpt.fileType === 'pic') {
        if (file.size / 1024 / 1024 < 5) {
          // 文件大小需在5MB
          if (file.type !== 'image/png' && file.type !== 'image/jpeg' && file.type !== 'image/jpg') {
            that.$message.warning('上传图片文件格式有误！')
            return false
          }
        } else {
          that.$message.warning('超出文件上传大小！')
          return false
        }
      } else if (that.uploadOpt.fileType === 'audio') {
        if (file.type !== 'audio/mp3' &&
          file.type !== 'audio/aac' &&
          file.type !== 'audio/mp4' &&
          file.type !== 'audio/mpeg' &&
          file.type !== 'audio/m3u8') {
          this.$message.warning('上传音频文件格式有误！')
          return false
        }
        that.QiniuToken.JSONRPC = JSON.stringify({'title': file.name, 'tag': '', 'desc': ''})
        that.QiniuToken.writetoken = that.Interface.Baoliweishi.writeToken
      } else if (that.uploadOpt.fileType === 'video') {
        if (file.type !== 'video/avi' &&
          file.type !== 'video/f4v' &&
          file.type !== 'video/mpg' &&
          file.type !== 'video/mp4' &&
          file.type !== 'video/flv' &&
          file.type !== 'video/wmv' &&
          file.type !== 'video/mov' &&
          file.type !== 'video/3gp' &&
          file.type !== 'video/rmvb' &&
          file.type !== 'video/mkv' &&
          file.type !== 'video/asf' &&
          file.type !== 'video/264' &&
          file.type !== 'video/ts' &&
          file.type !== 'video/mts' &&
          file.type !== 'video/dat' &&
          file.type !== 'video/vob' &&
          file.type !== 'video/mp3' &&
          file.type !== 'video/wav' &&
          file.type !== 'video/m4v' &&
          file.type !== 'video/webm' &&
          file.type !== 'video/quicktime') {
          this.$message.warning('上传视频文件格式有误！')
          return false
        }
        that.QiniuToken.JSONRPC = JSON.stringify({'title': file.name, 'tag': '', 'desc': ''})
        that.QiniuToken.writetoken = that.Interface.Baoliweishi.writeToken
      }
    },

    onUploadSuccess (res, file) {
      const that = this
      if (that.uploadOpt.fileType === 'fwb') {
        var resultFwb = {
          requestCode: that.requestCode,
          res: res,
          file: file
        }
        that.$emit('itemSelected', resultFwb)
        that.closeDialog()
      } else if (that.uploadOpt.fileType === 'pic') {
        that.materialPicInsert(file, res)
      } else if (that.uploadOpt.fileType === 'audio' || that.uploadOpt.fileType === 'video') {
        var type = that.uploadOpt.fileType === 'audio' ? '2' : '1'
        if (file.response.error === '0') {
          that.materialPolInsert(file.name, res.data[0].first_image, file.size, type, res.data[0].vid)
        } else {
          that.$message.error('上传失败')
          return false
        }
      }
    },

    onItemSelected (row) {
      const that = this
      that.$emit('itemSelected', that.formatResult(row))
      that.closeDialog()
    },

    formatResult (dto) {
      const that = this
      var result = {}
      if (that.uploadOpt.fileType === 'audio' || that.uploadOpt.fileType === 'video') {
        result.url = dto.vid
      } else {
        result.url = dto.pic
      }
      result.requestCode = that.requestCode
      return result
    },

    closeDialog () {
      const that = this
      that.requestCode = ''
      that.SearchItem.name = ''
      that.lastSearch.name = ''
      that.dialogVisible = false
      that.total = 0
      that.pageSize = 20
      that.currentPage = 1
      that.currentType = '0'
      that.materialGroupId = that.currentType
      that.uploadOpt.fileFieldName = undefined
      that.QiniuToken.JSONRPC = undefined
      that.QiniuToken.writetoken = undefined
      that.$refs.upload.clearFiles()
    }
  }
}
</script>
<style lang="scss">
  .groupDiv {
    text-align: left;
    font-size: 25px;
    font-weight: bold;
  }
  .groupAddDiv {
    text-align: left;
    font-size: 15px;
    color: #1e88e5;
    cursor:pointer;
    margin: 10px 0 0 0;
  }
  .custom-tree-node {
    max-width: 100%;
    flex: 1;
    display: flex;
    align-items: center;
    font-size: 14px;
  }

  .group-tree{
    margin-right: 10px;
    .el-tree-node__content{
      .el-tree-node__expand-icon{
        width: 0;
        padding: 0;
      }
    }
  }

  .myTreeLabel {
    width: 100%;
    text-align: left;
    line-height: 28px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap
  }
  .myTreeNum {
    text-align: left;
    line-height: 28px;
  }
  .maskLoading .el-loading-spinner .circular{
    visibility: hidden;
  }
  .scrollDialog {
    overflow: auto !important;
  }
</style>
