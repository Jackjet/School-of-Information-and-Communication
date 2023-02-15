
<template>
  <div class="mainn">
		<h4 class="pal5">{{data.title}}</h4><span @click="more" v-if="data.total > 10" style="cursor:pointer;position:absolute; top: 10px; right: 20px;">更多</span>
    	<div class="flex h100 contain"  v-if="data.type === 0" >
				<div class="mainn_box">
					<div class="wid0" v-for="(item, index) in data.dataLeft" :key='index' >
						<div class="flexb" @click="detail(item.id)">
							<span style="width:85%; font-size: 13px; text-overflow: ellipsis;  overflow: hidden;white-space: nowrap;">{{item.name}}</span>
							<div :class="item.status === '已通过'? 'blu':'bor'">{{item.status}}</div>
						</div>
						<div class="wid0" style="display: flex;">
							<div style="width:45%; text-overflow: ellipsis; font-size: 14px; line-height: 35px; color:#919191; overflow: hidden;white-space: nowrap;">提交人:{{item.content}}</div>
							<div style="width:45%; text-overflow: ellipsis; font-size: 14px; line-height: 35px; color:#919191; overflow: hidden;white-space: nowrap;">提交日期:{{item.contentTIme}}</div>
						</div>
					</div>
				</div>
				<div class="mainn_box">
					<div v-for="(item, index) in data.dataRight" :key='index' >
						<div class="flexb" @click="detail(item.id)">
								<span style="font-size: 13px;width:85%; font-size: 13px; text-overflow: ellipsis;  overflow: hidden;white-space: nowrap;">{{item.name}}</span>
								<div :class="item.status === '已通过'? 'blu':'bor'">{{item.status}}</div>
						</div>
						<div class="wid0" style="display: flex;">
							<div style=" width:45%; text-overflow: ellipsis; font-size: 14px; line-height: 35px; color:#919191;  overflow: hidden;white-space: nowrap;">提交人:{{item.content}}</div>
							<div style=" width:45%; text-overflow: ellipsis; font-size: 14px; line-height: 35px; color:#919191; overflow: hidden;white-space: nowrap;">提交日期:{{item.contentTIme}}</div>
						</div>
					</div>
				</div>	
			</div>
			<div class="flex h100 contain"  v-if="data.type === 1" >
				<div class="mainn_box" v-for="(item, index) in data.dataLeft" :key='index'>
					<div class="wid0" >
						<!-- <div class="flexb">
								<span style="font-size: 13px;">{{item.name}}</span>
								<div :class="item.status === '已通过'? 'blu':'bor'">{{item.status}}</div>
						</div> -->
						<div class="wid0" style="display: flex;	cursor: pointer;" @click="detail(item.id)">
							<div style="width:40%; text-overflow: ellipsis; font-size: 14px; line-height: 35px; overflow: hidden;white-space: nowrap;">{{item.name}}</div>
							<div style="width:52%; text-overflow: ellipsis; font-size: 14px; line-height: 35px; color:#919191;  overflow: hidden;white-space: nowrap;"><span style="color:#000; font-weight:350;">订阅时间:</span>  {{item.contentTIme}}</div>
						</div>
					</div>
				</div>
				
			</div>
  </div>
</template>

<script>
import { sessionStorageGet } from '@/utils/storage'
export default {
  name: 'main',
  data() {
    return {
      data: this.option,
    }
  },
  props: {
    option: {
      type: Object,
      default: () => {}
    }
  },
  created() {
   
  },
  watch(){
  },
  methods: {
	more() {
		if( this.data.title === '最新提交工单'){
		  this.$router.push({ name: 'womanagement', query: {type: 'total'} })
		} else if(this.data.title === '待办的工单'){
		  this.$router.push({ name: 'womanagement', query: {type: 'countMyToDo'}})
		} else if(this.data.title === '我订阅的检查规则'){
		  this.$router.push({ name: 'my' })
		}
	},
	detail(id){
		if(this.data.title === '我订阅的检查规则'){
			 this.$router.push({ name: 'manage', query: {id: id} })
		}else{
				this.$router.push({ name: 'womanagement', query: {type: 'id',id: id} })
		}
	
	},

  }
}
</script>
<style lang="scss" scoped>
		.flex{
			display: flex;
			display: -webkit-flex;
			flex-direction: row;
			flex-wrap: wrap;
			align-content: flex-start;
			min-width: 0;
		}
		.flexb{
			display: flex;
			display: -webkit-flex;
			flex-direction: row;
			flex-wrap: wrap;
			justify-content: space-between;
			min-width: 0;
			cursor: pointer;
		}
		.wid0{
			min-width: 0;
			min-height: 30px;
		}
		.pal5{
			padding-left: 5px;
		}
    .mainn{
			min-height: 300px;
			// padding: 3px;
			background: white;
			box-shadow: 0 6px 15px rgba(36, 37, 38, 0.1);
			border-radius: 5px;
			overflow: hidden;
			transition: box-shadow 0.25s ease, transform 0.25s ease;
			p {
				text-overflow: -o-ellipsis-lastline;
				overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 1;
				line-clamp: 1;
				-webkit-box-orient: vertical;
				margin: 0;
				line-height: 30px;
				white-space: nowrap;
				width: 600px;
			}

			span{
				color:#000;
				font-weight: bold;
			}
			.bor{
				padding: 1px;
				border-radius: 3px;
				border: 1px solid  rgb(241, 91, 21);
				color: rgb(241, 91, 21);
				font-size: 12px;
				font-weight: bold;
				line-height:1;
			}
			.blu{
				padding: 1px;
				border-radius: 3px;
				border: 1px solid rgb(74, 226, 94);
				color: rgb(51, 209, 72);
				font-size: 12px;
				font-weight: bold;
				line-height:1;
			}
		}

		.mainn_box{
			box-sizing: border-box;
			flex:0 0 49.5%;
			// line-height: 1.5;
			background: #fff;
			// display: flex;
			padding: 5px 10px;
			min-width: 0;
		}
</style>
