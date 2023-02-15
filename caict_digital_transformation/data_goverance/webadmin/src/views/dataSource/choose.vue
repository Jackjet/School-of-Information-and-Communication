<template>
  <el-main class="main">
    <el-row v-for="(item, index) in datas" :key="index">
      <el-row>
        <p class="head">{{item.name}}</p>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col :class="'item-' + indexx" class="item" v-for="(value,indexx) in item.types" :key="indexx" :span="5">
          <div @click="add(value)">
            <!-- <img :src="value.src" class="image"></el-image> -->
            <img :src="value.src" class="image">
            <p class="title">{{value.value}}</p>
          </div>
        </el-col>
      </el-row>
    </el-row>
    <el-row type="flex" justify="center" style="margin-top: 80px;">
      <el-button type="primary" size='medium' @click="reTu" icon="el-icon-add">返回</el-button>
    </el-row>
    <drawer :modal-objj='modalObjj' ref="callDetail" @Reload="submitLoading"></drawer>
  </el-main>
</template>

<script>
  import drawer from './add'

  export default {
    components: {drawer},
    data() {
      return {
        drawer: false,
        modalObjj: '',
        datas: [{
          name: '关系数据源',
          types: [{
            value: 'MySQL',
            src: require("@/assets/svg/MySQL.png")
          }, {
            value: 'Oracle',
            src: require("@/assets/svg/Oracle.png"),
          }, {
            value: 'SqlServer',
            src: require("@/assets/svg/SQLServer.png"),
          }, {
            value: 'PostgreSQL',
            src: require("@/assets/svg/PostgreSQL.png"),
          }]
        }]
      }
    },
    created() {

    },
    methods: {
      add: function (value) {
        this.modalObjj = '添加'
        this.$refs.callDetail.initial(false, value)
      },
      reTu: function (value) {
        this.$router.push({name: "index"})
      },
    }
  }
</script>
<style lang="scss" scoped>
  .main {
    .head {
      border-bottom: 1px dashed #8e9a92;
      padding: 0 0 5px 3px;
      margin: 0 0 20px 0;
    }
    .image {
      display: block;
      width: 80px;
      height: 80px;
      margin: 0 auto;
      border-radius: 4px;
      background-size: 100% 100%;
    }
    .item-0 {
      margin: 0 40px 0 20px !important;
    }
    .item {
      margin: 0 40px;
      padding-top: 15px;
      width: 140px;
      height: 140px;
      cursor: pointer;
      .title {
        text-align: center;
        // padding: 0;
        // margin: 0;
      }
    }
  }


</style>
