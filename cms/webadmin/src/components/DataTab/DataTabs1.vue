
<template>
  <div class="data-tabs">
    <el-row :span="24">
      <el-col :span="span" v-for="(item,index) in data" :key="index" style="width: 150px">
        <div class="item">
          <div class="item-body"  @click='exchangePage(item.apiUrl);'>
            <p>{{item.apiName}}</p>
          </div>
        </div>
      </el-col>
      <el-popover
        placement="right"
        width="400"
        trigger="click" v-if="listData" >
        <template>
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
          <div>
          <el-checkbox-group v-model="checkedUrls">
            <el-checkbox class="checkbox-listData" v-for="listUrl in listData" :label="listUrl.apiUrl+','+listUrl.apiName" :key="listUrl.id">{{listUrl.apiName}}</el-checkbox>
          </el-checkbox-group>
          <el-button class="button-checkbox" @click="insertUrl">确定</el-button>
          </div>
        </template>
        <el-button slot="reference" class="button-new-tag">＋</el-button>
      </el-popover>
    </el-row>
  </div>
</template>

<script>

import {getNavigation, getUserMenus, insert} from '@/api/dashboard';


export default {
  mounted() {
    this.init()
    this.getUserMenus()
  },
  name: 'DataTabs',
  data() {
    return {
      checkAll: false,
      span: this.option.span || 2,
      data: this.option.data || [],
      listData:[],
      checkedUrls: [],
      isIndeterminate: true,
    }
  },
  props: {
    option: {
      type: Object,
      default: () => {}
    }
  },
  methods: {
    exchangePage(url) {
      this.$router.replace(url)
    },
    async getUserMenus() {
      try {
        let res = await getUserMenus()
        this.listData = res.data.data
        this.load = false
      } catch (even) {
        this.load = false
        this.$message.error(even.msg)
      }
    },
    async init() {
      try {
        let res = await getNavigation()
        let nav = res.data.data
        this.data = nav
        for (let navElement of nav) {
          this.checkedUrls.push(navElement.apiUrl + ',' + navElement.apiName)
        }
        this.load = false
      } catch (even) {
        this.load = false
        this.$message.error(even.msg)
      }
    },
    async insertUrl() {
      try {
        await insert(this.checkedUrls)
        this.load = true
        this.$router.go('dashboard')
      } catch (even) {
        this.load = false
        this.$message.error(even.msg)
      }
    },
    handleCheckAllChange(val){
      let temp = []
      let listData = this.listData
      for (let listDatum of listData) {
        temp.push(listDatum.apiUrl + ',' + listDatum.apiName)
      }
      this.checkedUrls = val ? temp : [];
      this.isIndeterminate = false;
    }
  }
}
</script>

<style lang="scss" scoped>
    @import '../styles/data-tabs1.scss';
    .checkbox-listData{
      display: inline-block;
      width: 150px;
      margin-right: 5px;
      margin-bottom: 2px;
      border-radius: 6px;
    }
</style>
