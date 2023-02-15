<!--
 * @Author: your name
 * @Date: 2020-06-29 17:26:29
 * @LastEditTime: 2020-07-22 22:13:20
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\components\echarts\BarLine.vue
--> 
<template >
    <div :id='id' :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
export default {
  props: {
    id: {
      type: String,
      default: 'char'
    },
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '150px'
    },
    wid: {
      type: String,
      default: 24
    },
    value: {
      type: Number,
      default: ''
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
    //   this.optionss.forEach((item, index) => {
    //     if(item.data.length > 0) {
    //       this.initChart(item)
    //     } 
    //   })
     this.initChart(this.value)
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
	},
	
  methods: {
    initChart(value) {
			let datas = {
				value: value,
				title: "完成进度",
				type: 1,
				radiusType: 1
			};

			let fontColor = "#1e87f0";
			let seriesName = "";
			let noramlSize = 12;
			let state = "";
			// let center = ["50%", "70%"];
			let wqradius,
					nqradius,
					kdradius;
			wqradius = "85%";
			nqradius = "75%";
			kdradius = "75%";
			let wqColor = "rgba(80, 152, 237,0.9)";
			let nqColor = [
					[datas.value / 100, "#1e87f0"],
					[1, "#e6e6e6"]
			]
      // let echart = echarts.init(document.getElemen120tById(name))
      this.chart = echarts.init(this.$el)
      let option = {
        // title: {
        //   show: true,
        //   x: "center",
        //   bottom: "2%",
        //   text: datas.title,
        //   textStyle: {
				// 		fontWeight: "700",
				// 		fontSize: 16,
				// 		color: fontColor
        // 	}
      	// },
				tooltip: {
						show: false
				},
      	series: [{
						name: "刻度文字",
						type: "gauge",
						radius: kdradius,
						// center: center,
						startAngle: 90,
						endAngle: -269.9999,
						z: 7,
						// splitNumber: 20,
						min: 0,
						max: 100,
						axisTick: {
							show: true,
							lineStyle: {
									color: "#ffffff",
									width: 1
							},
							length: 8,
							splitNumber: 6
						},
						splitLine: {
							show: false
						},
						axisLine: {
							lineStyle: {
								width: 25,
								opacity: 0
							}
						},
						axisLabel: {
								fontSize: noramlSize,
								color: fontColor,
								formatter: function(v) {
									var str = '';
									// switch (v) {
									// 		case 0:
									// 				str = '0%';
									// 				break;
									// 		case 1:
									// 				str = '30%';
									// 				break;

									// 		case 2:
									// 				str = '60%';
									// 				break;

									// 		case 3:
									// 				str = '90%';
									// 				break;

									// 		case 4:
									// 				str = '100%';
									// 				break;

									// 		case 5:
									// 				str = '120%';
									// 				break;
									// }
									return str;
								},
						},
						pointer: {
								show: false
						},
						detail: {
								show: false
						}
          },
          {
						name: "指针",
						type: "gauge",
						z: 9,
						radius: "60%",
						startAngle: 90,
						endAngle: -269.9999,
						// center: center,
						axisLine: {
								lineStyle: {
										width: 0
								}
						},
						axisTick: {
								show: false
						},
						splitLine: {
								show: false
						},
						axisLabel: {
								show: false
						},
						min: 0,
						max: 100,
						pointer: {
							show: true,
							width: 5,
							length: "80%"
						},
						itemStyle: {
							normal: {
									color: wqColor
							}
						},
						detail: {
							show: true,
							offsetCenter: [0, "40%"],
							formatter: function(v) {
									var str = '';
									// switch (v) {
									// 		case 0:
									// 				str = '0%';
									// 				break;
									// 		case 1:
									// 				str = '30%';
									// 				break;

									// 		case 2:
									// 				str = '60%';
									// 				break;

									// 		case 3:
									// 				str = '90%';
									// 				break;

									// 		case 4:
									// 				str = '100%';
									// 				break;

									// 		case 5:
									// 				str = '120%';
									// 				break;
									// }
									return [
											"{value|" + (str) + "} ",
											"{company|" + state + "}"
									].join("\n");
								},
								rich: {
									value: {
										fontSize: 25,
										lineHeight: 10,
										color: "#1e87f0",
										fontWeight: "700"
									},
									company: {
										fontSize: 16,
										lineHeight: 20,
										color: "#1e87f0"
									}
								}
						},
						data: [datas.value]
          },
          {
						name: "内层盘",
						type: "gauge",
						z: 6,
						radius: nqradius,
						startAngle: 90,
						endAngle: -269.9999,
						// center: center,
						axisLine: {
							lineStyle: {
									color: nqColor,
									width: 15,
									opacity: 0.9
							}
						},
						// splitNumber: 20,
						min: 0,
						max: 100,
						axisTick: {
								show: false
						},
						axisLabel: {
								show: false
						},
						pointer: {
								show: false
						},
						detail: {
								show: 0
						}
          }
      	]
      }
      this.chart.setOption(option)
    }
  }
}
</script>
