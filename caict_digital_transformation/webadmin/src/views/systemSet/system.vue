<template>
  <el-main class="main">
    <el-col class="main-center">
       <div style="padding-top: 10px; padding-left: 10px; font-size: 22px;">主题色</div>
       <div style="padding: 20px;"><theme-picker></theme-picker></div>
       <!-- <div class="color">
         <li v-for="item in lists" :key="item.id" :class="item.color" @click="change(item.id)"></li>
       </div> -->
    </el-col>
    <el-col class="main-center" style="margin-top: 20px;">
       <div style="padding-top: 10px; padding-left: 10px; font-size: 22px;">导航设置</div>
       <div class="color">
         <li class="horizontal" @click="changed('horizontal')">横</li>
         <li class="vertical" @click="changed('vertical')">纵</li>
       </div>
    </el-col>
    <el-col class="main-center" style="margin-top: 50px;">
       <div style="padding-top: 10px; padding-left: 10px; font-size: 22px;">多标签页</div>
       <div class="color">
         <span style="padding-top: 30px;" >多标签模式</span>
         <el-switch style="padding-left: 50px;" @change="tab" v-model="delivery"></el-switch>
       </div>
    </el-col>
  </el-main>
</template>

<script>
import { getToken } from '@/utils/auth'
import transmit from '@/utils/transmit'
import ThemePicker from '@/components/themepicker/ThemePicker.vue'
export default {
  components: {ThemePicker },
  data() {
    return {
      title: '新增',
      disabled: true,
      disabledo: true,
      props: { checkStrictly: true },
      ruleForm: {
      },
      delivery: false,
      lists: [
        {id: 1, color:'red'},
        {id: 2, color:'orange'},
        {id: 3, color:'yellow'},
        {id: 4, color:'green'},
        {id: 5, color:'lightBlue'},
        {id: 6, color:'blue'},
        {id: 7, color:'purple'},
        {id: 7, color:'Pink'}
      ],
      organizationOptions: [],
      options: [],
      formLabelWidth: "120px"
    }
  },
  created () {
  },
  methods: {
    changed(model) {
      if(model === 'horizontal') {
        this.$store.dispatch('app/toggleDevice', 'mobile')
        this.$store.dispatch('app/closeSideBar', { withoutAnimation: true })
      }else if(model === 'vertical') {
        this.$store.dispatch('app/toggleDevice', 'desktop')
      }
    },
    tab() {
      transmit.$emit('delivery', this.delivery)
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    change(id){

    },
    // 初始化表单
    CloseModal() {
      this.disabled = false;
      this.disabledo = false
    },
  }
}
</script>
<style lang="scss" scoped>
 /deep/ .el-input{
   width: 100%!important;
 }
 /deep/ .el-cascader{
    width: 100%!important;
 }
 /deep/ .el-select{
    width: 100%!important;
 }
 /deep/.el-form-item__content{
   line-height: 20px;
 }
 .main{
  display: flex;
  flex-direction: column;
  background: #fff;
  height: 100%;
  width: 95%;
  margin: 0 auto;
  min-height: 600px;
  margin-top: -240px;
  border-radius: 5px;
  padding: 10px 20px;
  .main-center{
    width: 35%;
    margin: 0 0 0 20px;
  }
 }
 .demo-drawer__footer{
   margin-top: 80px;
   text-align: center;
 }
 .color{
   display: block;
   background-color: #fff;
   margin-top: 20px;
   margin-left: 10px;
   li{
     width: 40px;
     height: 40px;
     float: left;
     border-radius: 5px;
     list-style-type:none;
     margin: 0 5px;
   }
 }
 .red{
   background-color: red;
 }
 .orange{
   background-color: orange;
 }
 .yellow{
   background-color: yellow;
 }
 .green{
   background-color: green;
 }
  .lightBlue{
   background-color: #6699FF;
 }
 .blue{
   background-color: blue;
 }
 .purple{
   background-color: purple;
 }
 .pink{
   background-color: pink;
 }

 .horizontal{
    color:#fff;
    cursor:pointer;
    display: flex;
    align-items:center;
    justify-content: center;
    background-color: #6699FF;
 }
 .vertical{
    color:#fff;
    cursor:pointer;
    display: flex;
    align-items:center;
    justify-content: center;
    background-color: #6699FF;
 }
 .horizontav{
    background-color: pink;
 }
</style>
