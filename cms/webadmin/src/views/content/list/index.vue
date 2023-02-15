<template>
  <el-container>
    <SideBar :data="data" @nodeClick="getNodeClick"></SideBar>
    <LContent :columnId="columnId" :templateId="templateId" :isList="isList" :pcListUrl="pcListUrl" :pcDocumentUrl="pcDocumentUrl" :appListUrl="appListUrl" :appDocumentUrl="appDocumentUrl"></LContent>
  </el-container>
</template>

<script>
import {treeFindAll} from '@/api/content'
import SideBar from '@/views/content/component/sideBar'
import LContent from '@/views/content/list/content'
export default {
  components: {LContent, SideBar},
  data() {
    return{
      data: [],
      loading:false,
      columnId:'',
      templateId:'',
      isPcList:'',
      isPcDocument:'',
      isAppList:'',
      isAppDocument:'',
      pcListUrl:'',
      pcDocumentUrl:'',
      appListUrl:'',
      appDocumentUrl:'',
      isList:false
    }
  },
  created() {
    this.initial()
  },
  methods: {
    async initial() {
      const that = this
      that.loading = true
      const response = await treeFindAll()
      that.loading = false

      if (response.data.code === 1) {
        // 查询成功后 回调效果
        let data = response.data.data
        that.data = that.cleanData(data)
        that.columnId = that.data[0].columnId
        that.templateId = that.data[0].templateId
        that.isPcList = that.data[0].isPcList
        that.isPcDocument = that.data[0].isPcDocument
        that.isAppList = that.data[0].isAppList
        that.isAppDocument = that.data[0].isAppDocument
        that.pcListUrl = that.data[0].pcListUrl
        that.pcDocumentUrl = that.data[0].pcDocumentUrl
        that.appListUrl = that.data[0].appListUrl
        that.appDocumentUrl = that.data[0].appDocumentUrl

        that.isList = false
        if(that.isPcList === '1' || that.isAppList === '1') {
          that.isList = true
        }
      } else {
        // 查询失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    cleanData(data) {
      const that = this
      let result = []
      data.forEach(function (value, i) {
        let children = []
        if (value.hasChildren) {
          children = that.cleanData(value.children)
        }
        let item = {
          id: value.id,
          label: value.columnName,
          columnId:value.columnId,
          templateId:value.templateId,
          isPcList: value.isPcList,
          isPcDocument: value.isPcDocument,
          isAppList:value.isAppList,
          isAppDocument:value.isAppDocument,
          pcListUrl:value.pcListUrl,
          pcDocumentUrl:value.pcDocumentUrl,
          appListUrl:value.appListUrl,
          appDocumentUrl:value.appDocumentUrl,
          children: children
        }
        result.push(item)
      })

      return result
    },
    getNodeClick(columnId,templateId,isList,pcListUrl,pcDocumentUrl,appListUrl,appDocumentUrl){
      this.columnId = columnId
      this.templateId = templateId
      this.isList = isList
      this.pcListUrl = pcListUrl
      this.pcDocumentUrl = pcDocumentUrl
      this.appListUrl = appListUrl
      this.appDocumentUrl = appDocumentUrl
    }
  }
}
</script>
