<template>
  <div class="dashboard-container">
		<div class="main"   v-loading.lock="fullscreenLoading">
				<div class="flex contain">
						<div class="child" v-for="(item, index) in datas" :key='index' >
							<p class='center'>{{item.name}}</p>
							<div class="flex"> 
								<div class="childd">
									<board :value='item.accuracy'></board>
								</div>
								<div class="childdd">
									{{item.date}} 数据执行情况 {{item.count}}条准确率 {{item.accuracy}}%
								</div>
							</div>
							<p class='center'>检查评分{{item.date}}</p>				
						</div>
				</div>
				<div>
					<el-tabs class="bgColor" v-model="activeName" @tab-click="handleClick">
						<el-tab-pane value='0' label="最新提交工单" name="最新提交工单">
							<list :option="data"></list>
						</el-tab-pane>
						<el-tab-pane label="待办的工单" name="待办的工单"><list :option="data"></list></el-tab-pane>
						<el-tab-pane label="我订阅的检查规则" name="我订阅的检查规则"><list :option="data"></list></el-tab-pane>
					</el-tabs>
				</div>	
		 </div>
  </div>
</template>
<script>

import board from '@/components/echarts/board'
import list from '@/components/list/list'
export default {
	 components: { board, list },
   data () {
    return {
			List: [
				
			],
			datas: [],
			activeName: '最新提交工单',
			data:{
				title: '最新提交工单',
				type: '',
				total: '',
				dataLeft:[],
				dataRight: []
			},
			fullscreenLoading: false,
    }
	},
	created() {
		this.getList()
		this.getListWorkOrder()
	},
	 methods: {
    async getList() {
			const data = {} 
			const that = this
			this.fullscreenLoading = true
      try {
        
				this.datas = [{"date":"2020-08-12","name":"有效性内容规范约束","count":0,"accuracy":0},{"date":"2020-08-12","name":"时效性约束","count":0,"accuracy":0},{"date":"2020-08-12","name":"实体唯一性约束","count":0,"accuracy":0},{"date":"2020-08-12","name":"取值准确性约束","count":0,"accuracy":0}]
				this.fullscreenLoading = false
      } catch (even) {
				this.$message.error(even.msg)
				this.fullscreenLoading = false
      }
		},
		 async getListWorkOrder(status) {
		
			let data = {
				page:1,
				size: 10,
				currentItcode: '',
				status: status
			} 
			if(this.data.title === '待办的工单'){
				data.currentItcode = sessionStorageGet('id')
			}
			const that = this
      try {
				// const res = await workOrder(data)
				let datas = []
				that.data.type = 0
				that.data.dataRight = []
				that.data.dataLeft = []
				datas = [{"id":"366fbb85c9494b1ba3efc6e7760a6a95","createById":"admin","createTime":"2020-08-06 16:41:04","updateById":"admin","updateTime":"2020-08-06 16:42:23","ruleId":"589ee608df354e099f689f5e9f79fadb","ruleName":"mysql判user表非空","taskId":"ae1d31ed906d40d49caad6659c0ec9ae","taskName":"mysqltomysql","startTime":"2020-08-04 20:18:57","title":"民用建筑节能专项验收备案","itcode":"admin","userName":"admin","level":"一般","eventDesc":"[{\"context\":\"民用建筑节能专项验收备案 \",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"},{\"updateTime\":\"2020-08-06 16:42:23\",\"userName\":\"admin\",\"operation\":\"通过\"}]","status":"已通过","duration":1,"remark":null,"currentItcode":"admin","currentUserName":"admin","createByName":"admin","updateByName":"admin"},{"id":"11dc871c4cb5432ba471604b8aae11ae","createById":"admin","createTime":"2020-08-06 16:40:47","updateById":"admin","updateTime":"2020-08-06 16:42:58","ruleId":"589ee608df354e099f689f5e9f79fadb","ruleName":"mysql判user表非空","taskId":"ae1d31ed906d40d49caad6659c0ec9ae","taskName":"mysqltomysql","startTime":"2020-08-04 20:18:57","title":"房屋建筑和市政基础设施工程竣工验收备案","itcode":"admin","userName":"admin","level":"一般","eventDesc":"[{\"context\":\"房屋建筑和市政基础设施工程竣工验收备案 \",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"},{\"updateTime\":\"2020-08-06 16:42:58\",\"userName\":\"admin\",\"operation\":\"通过\"}]","status":"已通过","duration":2,"remark":null,"currentItcode":"admin","currentUserName":"admin","createByName":"admin","updateByName":"admin"},{"id":"0e15053da6d541a39ea3f9a6e12da923","createById":"admin","createTime":"2020-08-06 16:30:15","updateById":"admin","updateTime":"2020-08-06 16:30:37","ruleId":"589ee608df354e099f689f5e9f79fadb","ruleName":"mysql判user表非空","taskId":"ae1d31ed906d40d49caad6659c0ec9ae","taskName":"mysqltomysql","startTime":"2020-08-04 20:18:57","title":"dasd","itcode":"admin","userName":"admin","level":"严重警告","eventDesc":"[{\"context\":\"asdasd\",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"},{\"updateTime\":\"2020-08-06 16:30:37\",\"userName\":\"admin\",\"operation\":\"通过\"}]","status":"已通过","duration":0,"remark":null,"currentItcode":"admin","currentUserName":"admin","createByName":"admin","updateByName":"admin"},{"id":"d9ee21445fe74f38a1884f67c0242c68","createById":"admin","createTime":"2020-08-06 16:16:09","updateById":"admin","updateTime":"2020-08-06 16:16:27","ruleId":"589ee608df354e099f689f5e9f79fadb","ruleName":"mysql判user表非空","taskId":"ae1d31ed906d40d49caad6659c0ec9ae","taskName":"mysqltomysql","startTime":"2020-08-04 20:18:57","title":"哈哈哈","itcode":"admin","userName":"admin","level":"严重警告","eventDesc":"[{\"context\":\"阿斯顿\",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"},{\"updateTime\":\"2020-08-06 16:16:27\",\"userName\":\"admin\",\"operation\":\"通过\"}]","status":"已通过","duration":0,"remark":null,"currentItcode":"admin","currentUserName":"admin","createByName":"admin","updateByName":"admin"},{"id":"7d3f8c8f34ef4561b4ebd87a05a97d86","createById":"admin","createTime":"2020-08-04 19:51:13","updateById":null,"updateTime":null,"ruleId":"589ee608df354e099f689f5e9f79fadb","ruleName":"mysql判user表非空","taskId":"ae1d31ed906d40d49caad6659c0ec9ae","taskName":"mysqltomysql","startTime":"2020-08-04 19:49:17","title":"差率","itcode":"admin","userName":"admin","level":"严重警告","eventDesc":"[{\"context\":\"111\",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"}]","status":"处理中","duration":null,"remark":null,"currentItcode":"admin","currentUserName":"admin","createByName":"admin","updateByName":null},{"id":"f2bdcf74105e409da7cc999551c22c54","createById":"admin","createTime":"2020-08-04 19:07:50","updateById":"admin","updateTime":"2020-08-04 19:11:55","ruleId":"589ee608df354e099f689f5e9f79fadb","ruleName":"mysql判user表非空","taskId":"ae1d31ed906d40d49caad6659c0ec9ae","taskName":"mysqltomysql","startTime":"2020-08-04 19:04:35","title":"王小贺提交的出差差旅请及时处理","itcode":"admin","userName":"admin","level":"严重警告","eventDesc":"[{\"context\":\"王小贺提交的出差差旅请及时处理\",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"},{\"updateTime\":\"2020-08-04 19:11:55\",\"userName\":\"admin\",\"operation\":\"通过\"}]","status":"已通过","duration":4,"remark":"1212","currentItcode":"admin","currentUserName":"admin","createByName":"admin","updateByName":"admin"},{"id":"e812aafc9bd040689a379d8979498086","createById":"admin","createTime":"2020-08-04 18:52:37","updateById":"admin","updateTime":"2020-08-04 19:05:07","ruleId":"589ee608df354e099f689f5e9f79fadb","ruleName":"mysql判user表非空","taskId":"ae1d31ed906d40d49caad6659c0ec9ae","taskName":"mysqltomysql","startTime":"2020-08-04 18:50:45","title":"出差差率请及时处理","itcode":"admin","userName":"admin","level":"严重警告","eventDesc":"[{\"context\":\"请及时处理\",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"},{\"updateTime\":\"2020-08-04 19:05:07\",\"userName\":\"admin\",\"operation\":\"通过\"}]","status":"已通过","duration":12,"remark":null,"currentItcode":"admin","currentUserName":"admin","createByName":"admin","updateByName":"admin"},{"id":"21b6deb0af224143b439769b062e5b71","createById":"admin","createTime":"2020-08-04 18:40:14","updateById":null,"updateTime":null,"ruleId":"589ee608df354e099f689f5e9f79fadb","ruleName":"mysql判user表非空","taskId":"ae1d31ed906d40d49caad6659c0ec9ae","taskName":"mysqltomysql","startTime":"2020-08-04 18:35:51","title":"您有一条差旅计划需要及时通过","itcode":"3c6a55b7-69b1-40b2-8c6f-a67faed3ba0e","userName":"test1","level":"严重警告","eventDesc":"[{\"context\":\"请及时处理！\",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"}]","status":"处理中","duration":null,"remark":null,"currentItcode":"3c6a55b7-69b1-40b2-8c6f-a67faed3ba0e","currentUserName":"test1","createByName":"admin","updateByName":null},{"id":"b4093a23334a408590c979c541c0cef3","createById":"admin","createTime":"2020-08-03 22:17:21","updateById":"admin","updateTime":"2020-08-03 22:18:02","ruleId":"1ee346ca2fc14aceacda5d23062fc19a","ruleName":"初次时","taskId":"60d06b1d9e1f45f88023e01ee0330a14","taskName":"mysqltopostgre","startTime":"2020-08-03 22:02:59","title":"日常判空规则检查","itcode":"cd0c438e-5877-4a71-a3cf-13136bfdf385","userName":"张晓希","level":"正常","eventDesc":"[{\"context\":\"日常判空规则检查\",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"},{\"updateTime\":\"2020-08-03 22:18:02\",\"userName\":\"admin\",\"operation\":\"转发\",\"desc\":\"admin\"}]","status":"处理中","duration":null,"remark":null,"currentItcode":"admin","currentUserName":"admin","createByName":"admin","updateByName":"admin"},{"id":"38167be1d088475fa788af4e5eb24496","createById":"admin","createTime":"2020-08-03 22:08:51","updateById":"admin","updateTime":"2020-08-03 22:23:51","ruleId":"df7630e227794342abbc58eb54cde491","ruleName":"检查空","taskId":"b5ea1dc20f5f46c79e9a5774c789fae9","taskName":"postgretopostgre","startTime":"2020-08-03 21:54:45","title":"检查空数据规则复查","itcode":"ab4bf237-ef8d-4944-91fd-b75c7daf2e01","userName":"张晓萌","level":"正常","eventDesc":"[{\"context\":\"检查空数据规则复查\",\"updateTime\":\"\",\"userName\":\"\",\"operation\":\"\"},{\"updateTime\":\"2020-08-03 22:23:51\",\"userName\":\"admin\",\"operation\":\"转发\",\"desc\":\"admin\"}]","status":"处理中","duration":null,"remark":null,"currentItcode":"admin","currentUserName":"admin","createByName":"admin","updateByName":"admin"}]
				// if (res.data.code === 1 && res.data.data.content.length > 0) {
					datas.map(function(v, k){
						if(k%2 === 0){
							that.data.dataLeft.push({id: v.id, name: v.title, status: v.status, content: v.createByName, contentTIme: v.createTime})
						}else{
							that.data.dataRight.push({id: v.id, name: v.title, status: v.status, content: v.createByName, contentTIme: v.createTime})
						}
					})
					// this.data.dataRight = res.data.data.content
				// }
				//  that.data.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
		},
		async findByCurrent(status) {
			const data = {
				page:1,
				size: 10,
			} 
			const that = this
      try {
				const res = await findByCurrentUser(data)
				that.data.type = 1
				that.data.dataRight = []
				that.data.dataLeft = []
				if (res.data.code === 1 && res.data.data.content.length > 0) {
					res.data.data.content.map(function(v, k){
						// if(k%2 === 0){
							that.data.dataLeft.push({id: v.ruleId,name: v.ruleName, contentTIme: v.createTime})
						// }else{
						// 	that.data.dataRight.push({name: v.ruleName, contentTIme: v.createTime})
						// }
					})
					that.data.total = res.data.data.totalElements
					// this.data.dataRight = res.data.data.content
				}
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
		handleClick(tab, event){
			this.data.title = tab.name
			if(tab.name === '待办的工单'){
				this.getListWorkOrder('处理中')
			}else if(tab.name === '最新提交工单'){
				this.getListWorkOrder()
			}else if(tab.name === '我订阅的检查规则'){
				this.findByCurrent()
			}
		},
    add: function() {
      this.$refs.add.initial('system')
		},
		go(){
			this.$router.push({ path: '/' })
		}
  }
}
</script>
<style lang="scss" scoped>

.dashboard {
  &-container {
    // margin: 2px;
    // padding: 0 10px;
		padding: 15px 0 30px 0;
	  height: 100%;
		min-height: 100vh;
    background-color: #f6f6f6;
    // background-image: url(../../assets/image/index.png);
    // height: 100vh;
		// width: 90%;
		// margin: 0 auto;
    position: relative;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
	
}
img{
	display: block;
	width: 100%;
	// height: 40%;
	max-height: 100%;
	max-width: 100%;
}
.flex{
	display: flex;
	display: -webkit-flex;
	flex-direction: row;
	flex-wrap: wrap;
	align-content: flex-start;
}
.h100{
	min-height: 100vh;
}
.child {
	box-sizing: border-box;
	// background-color: white;
	flex: 0 0 24%;
	background: white;
	margin: 0.2vw;
	box-shadow: 0 6px 15px rgba(36, 37, 38, 0.1);
	border-radius: 5px;
	overflow: hidden;
	transition: box-shadow 0.25s ease, transform 0.25s ease;

	// height: 50px;
	// border: 1px solid red;
}
.childd {
	box-sizing: border-box;
	// background-color: white;
	// flex: 0 0 49%;
	// flex-grow: 1;
	display: flex;
	background: #fff;
	// flex-shrink: 0;
	// flex-grow: 2;
	flex:1.2;
	// height: 50px;
	// border: 1px solid red;
}
.childdd {
	box-sizing: border-box;
	// background-color: white;
	// flex: 0 0 49.5%;
	flex:1;
	padding-right: 8px;
	line-height: 1.5;
	// flex-grow: 1;
	// flex-shrink: 0;
	background: #fff;
	display: flex;
	// margin: 4px;
	// height: 50px;
	// border: 1px solid red;
}
.center{
	text-align: center;
}

.dashboard-container{
	  .main{
			width: 95%;
			margin: 0 auto;
			// background: #fff;
		}
	  .pdl10{
			margin: 0 0 10px 3px;
			display: inline-block;
			padding: 5px 10px 5px 10px;
			// line-height: 30px;
			// background: white;
			// box-shadow: 0 6px 15px rgba(36, 37, 38, 0.1);
			// border-radius: 5px;
			// overflow: hidden;
			// transition: box-shadow 0.25s ease, transform 0.25s ease;
		}
		.mb20{
			margin-bottom: 40px;
		}
		.contain{
			// background: #fff;
		}
		.bgColor{
			padding: 5px;
		  background: #fff;
			margin-top: 15px;
		}
		.containn{
			// background: #fff;
			p {
				margin: 0;
			}
		}
    .box {
			position: absolute;
			z-index: 5;
			background: #fff;
			width: 80%;
			height: 64%;
			left:50%;    
			top:50%;
			transform: translate(-50%,-50%); /*自己的50% */
			border-radius: 5px;
			padding: 20px;
			.item {
				box-sizing: border-box;
				// flex: 0 0 33%;
				flex: 0 0 1;
				// width: 30%;
				height: 30%;
				// background-image: url(../../assets/image/zhishi.png);
				
			}
    }
		.top{
			width: 100%;
			height: 50%;
			// background: url(../../assets/image/bank.png);
			background-size: 100% 100%;
		}
		.item{
			cursor: pointer;
		}
}

</style>
