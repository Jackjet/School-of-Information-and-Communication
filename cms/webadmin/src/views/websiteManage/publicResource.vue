<template>
  <el-main class="contain">
     <el-row class="el-row1">
        <el-col :span="18">
          <span>公共资源列表</span>
          <el-popover
            placement="top-start"
            title="示例:"
            width="320"
            trigger="hover">
            <img
              slot="reference"
              src="@/icons/image/explain.png"
              height="15px"
              width="15px"
              style="position: relative;left:5px;top:0px;">
            <slot>
              单字段value格式:18522298765<br/>
              多字段value格式:[{'name':'实验室1','href':'www.baidu.com'}]<br/>
              图片value格式:https://upload-images.io/6.png<br/>
              链接value格式:www.baidu.com<br/>
            </slot>
          </el-popover>
        </el-col>
     </el-row>
    <el-row class="el-row" v-for="(domain, index) in tableData" :key="domain.theKey">
			  <el-col :span="1" :offset="1"><span>key</span></el-col>
			  <el-col :span="4">
					<el-input v-model="domain.theKey"></el-input>
        </el-col>
				<el-col :span="1" :offset="1"><span>value</span></el-col>
				<el-col :span="4">				
					<el-input v-model="domain.theValue"></el-input>
				</el-col>
				<el-col v-if="index>=0" class="titleR name"  offset="1" :span="1" :style="{ display: visible1 }">
          <el-button type="primary"  @click="deleteData(index)">删除</el-button>
        </el-col>
        <el-col v-if="index == tableData.length-1" class="titleR name"  offset="1" :span="1">
          <el-button type="primary"  @click="addData" >新增</el-button>
        </el-col>
		</el-row>
	 <el-row class="el-row">
         <el-col class="name" :span="13"><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </div></el-col>
         <el-button type="primary"  @click="submit" >提交</el-button>
     </el-row>       
  </el-main>
</template>

<script>

import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import { getToken } from '@/utils/auth'
import { getAllPublicResource, deleteData, saveData } from '@/api/websiteManage'

export default {
  components: { TableList, Pagination },

  data() {
      return {
      drawer: false,
      tableData: [],
      tableHeader: [{id: false, type: '', label: '文件名称', list: 'fileName'},
        {id: false, type: '', label: '文件大小(kb)', list: 'size'},
        {id: false, type: '', label: '更新时间', list: 'updateTime'}
      ],
      tableOpction: {},
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        name: '',
        type: ''
      },
      lastItem: {
        name: '',
        type: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
      dataCount :0,
      parentPath: 'static/web',
      currentDir: 'static/web',
      visible1: 'none',
    }
  },
  created () {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const res = await getAllPublicResource()
        this.tableData = res.data.data
        console.log("this.tableData.length",this.tableData.length)
        this.refresh()
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    addData: function () {
        const lastItem = this.tableData[this.tableData.length-1]
        if(lastItem.theKey.length == 0 && lastItem.theValue.length ==0 ){
            this.$message.error("请先把数据填写完整")
        }else{
          const item = { "theKey":"","theValue":""}
          this.tableData.push(item)
          this.refresh()
        }
    },
    refresh (){
        if(this.tableData.length == 1){
          this.visible1 = 'none'
        }else{
          this.visible1 = 'block'
        }
    },
    async deleteData(index) {
        try {
            if(this.tableData.length > 1){
              this.tableData.splice(index,1)
            }
            this.refresh()
        } catch (even) {
            this.$message.error(even.msg)
        }
    },
    async submit () {
        const that = this
        try {
            let keyFlag = false
            let valueFlag = false
            for (var index in that.tableData) {
                  console.log("item",index)
                  if (that.tableData[index].theKey.length === 0) {
                    keyFlag = true
                  } 
                  if (that.tableData[index].theValue.length === 0) {
                    valueFlag = true
                  } 
            }
            if(that.tableData.length == 1){
                if(keyFlag && valueFlag || !keyFlag && !valueFlag){
                  this.submitData()
                }else{
                  this.$message.warning("数据不能为空")
                }
            }else{
                if(keyFlag || valueFlag){
                  this.$message.warning("数据不能为空")
                }else{
                  this.submitData()
                }
            }
        } catch (even) {
            this.$message.error(even.msg)
        }
    },
    async submitData(){
        const that = this
        const data = {
            resourceData : that.tableData
        }
        const res = await saveData(data)
        if(res.data.code==1){
          this.$message.success('提交成功');
        }else{
           this.$message.error(res.data.msg);
        }
    },
    submitLoadingg(){
      this.fetchData()
    },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },
    SearchNoteList(){
      this.fetchData()
    },
  }
}
</script>
<style lang="scss" scoped>
/deep/ .el-form-item__label{
    color: #000;
}
.main{
  background: #00000000;
  height: 100%;
  width: 100%;
  padding: 0 30px;
  min-height: 600px;
  margin-top: 30px;
  .el-InputForm{
    //background:url('../../../assets/image/filter.png');
    background-size: 100% 100%;
    padding:15px 0 0 20px;

  }
}
.work-query {
  display: flex;
  align-items: center;
  padding: 20px;
}
.table-list {
  padding: 0 20px;
  box-sizing: border-box;
}
.table-pagination {
  display: flex;
  justify-content: flex-end;
  padding: 20px;
}
.el-row {
    margin-bottom: 5px;
    height: 40px;
    display: flex;
    flex-wrap: wrap
}
.el-row1 {
    margin-top: 20px;
    margin-bottom: 40px;
    margin-left: 40px;
    display: flex;
    flex-wrap: wrap
}
</style>