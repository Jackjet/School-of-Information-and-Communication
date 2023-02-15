<template>
  <el-main>
    <el-col :span="24">
      <template>
        <UpLoadFile :folderId='folderId' :typeAccess='"0"'></UpLoadFile>
      </template>
      <el-row class='bottom'>
        <el-col :span="24">
          <el-button @click="close">返回</el-button>
        </el-col>
      </el-row>
    </el-col>
  </el-main>
</template>

<script>
import UpLoadFile from '@/components/UpLoadFile/UpLoadFile'
export default {
  components: {
    UpLoadFile
  },

  data () {
    return {
      folderId: ''
    }
  },

  mounted () {
  },

  methods: {
  
  }
}
</script>
