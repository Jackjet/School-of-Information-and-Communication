<!--
 * @Author: your name
 * @Date: 2020-09-03 13:58:50
 * @LastEditTime: 2020-09-28 22:50:25
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \web\src\views\home\index.vue
-->
<template>
  <main class="main">
    <div class="main-img">
    </div>
  </main>
</template>

<script>

export default {
  name: "Dashboard",
  mounted() {
  },
  data() {
    return {}
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
 .main{
  display: flex;
  position: relative;
  flex-direction: column;
  justify-content: center; /* 水平居中 */
  align-items: center;     /* 垂直居中 */
  background: #fff;
  height: 100%;
  width: 95%;
  margin: 0 auto;
  min-height: 600px;
  margin-top: -240px;
  border-radius: 5px;
  padding: 10px 20px;
  .main-center{
    width: 65%;
    margin: 0 auto;
  }
  .main-img{
    // position: absolute;;
    background: url('../../assets/image/home.png') no-repeat;
    background-size: 678px 493px;
    padding: 30px;
    height: 493px;
    width: 678px;
  }
  // img{
  //   display: inline-block;
  //   max-width: 100%;
  //   max-height: 100%;
  //   width: 100%;
  //   height: 100%;
  // }
 }

</style>
