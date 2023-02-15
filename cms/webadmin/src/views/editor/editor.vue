<template>
  <div class="dashboard-container">
    <div class="item">
        <el-col ><h2>富文本编辑器</h2></el-col>
        <el-col :offset="1" :span="22">
            <editor></editor>
        </el-col>
    </div>
  </div>
</template>

<script>

import editor from '@/components/editor/wangEditor'
export default {
  name: 'Dashboard',
  components: {
    editor,
  },
//   computed: {
    
//   },
//   mounted() {
   
//   },
//   data() {
//     return {
     
//     },
//   },
//   methods: {
   
//   }
}
</script>

<style lang="scss" scoped>
    .item{
        margin-top: 5%;
        text-align: center;
    }
</style>
