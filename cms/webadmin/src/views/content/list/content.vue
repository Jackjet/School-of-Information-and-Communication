<template>
  <el-container>
    <template v-if="isList">
      <MultiPage ref="multiPage" :columnId="columnId" :templateId="templateId" :isList="isList" :pcListUrl="pcListUrl" :pcDocumentUrl="pcDocumentUrl" :appListUrl="appListUrl" :appDocumentUrl="appDocumentUrl" ></MultiPage>
    </template>
    <template v-else>
      <SinglePage ref="singlePage" :columnId="columnId" :templateId="templateId" :isList="isList" :pcListUrl="pcListUrl" :pcDocumentUrl="pcDocumentUrl" :appListUrl="appListUrl" :appDocumentUrl="appDocumentUrl" ></SinglePage>
    </template>
  </el-container>
</template>

<script>
import MultiPage from '@/views/content/list/multiPage.vue'
import SinglePage from '@/views/content/list/singlePage.vue'
import Template from '../../template/template';
export default {
  components: { MultiPage ,SinglePage },
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
