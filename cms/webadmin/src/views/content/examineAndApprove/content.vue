<template>
  <el-container>
    <MultiPage ref="multiPage" :columnId="columnId" :templateId="templateId" :isList="isList" :pcListUrl="pcListUrl" :pcDocumentUrl="pcDocumentUrl" :appListUrl="appListUrl" :appDocumentUrl="appDocumentUrl" ></MultiPage>
  </el-container>
</template>

<script>
import MultiPage from '@/views/content/examineAndApprove/multiPage.vue'
import Template from '../../template/template';
export default {
  components: { MultiPage},
  data() {
    return {
      columnId:'',
      templateId:'',
      isList:false,
      pcListUrl:'',
      pcDocumentUrl:'',
      appListUrl:'',
      appDocumentUrl:''
    }
  },
  watch:{
    async columnId (newValue ,oldValue){
      oldValue = newValue
    },
    async templateId (newValue ,oldValue){
      oldValue = newValue
    },
    async isList (newValue ,oldValue){
      oldValue = newValue
    },
    async pcListUrl (newValue ,oldValue){
      oldValue = newValue
    },
    async pcDocumentUrl (newValue ,oldValue){
      oldValue = newValue
    },
    async appListUrl (newValue ,oldValue){
      oldValue = newValue
    },
    async appDocumentUrl (newValue ,oldValue){
      oldValue = newValue
    }
  },
  props:{
    columnId: String,
    templateId: String,
    isList: Boolean,
    pcListUrl:String,
    pcDocumentUrl:String,
    appListUrl:String,
    appDocumentUrl:String
  },
  methods: {
  }
}
</script>
