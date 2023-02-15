<!--
 * @Author: your name
 * @Date: 2020-07-03 14:24:23
 * @LastEditTime: 2020-07-23 21:53:05
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\components\echarts\PieChart.vue
--> 
<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'

export default {
  props: {
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
      default: '350px'
    },
    datas: {
      type: Object,
      default: ''
    },
    title: {
      type: String,
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
      // this.datas.forEach((item, index) => {
      //   if(item.data.length > 0) {
      //     this.initChart(item)
      //   } 
      // })
      this.initChart(this.datas)
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
    initChart(item) {
      let data = []
       if(this.title === '工单级别饼图'){
           data = ['严重警告','警告','正常','一般','关注']
       }else{
          item.map(x => {
            data.push(x.name) 
          })
       }
    
      this.chart = echarts.init(this.$el)
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ({c}条)({d}%)'
        },
        title: {
            text: this.title,
            x:'center',
            // y: 'bottom',
            bottom: '60',
            textStyle:{
              fontSize: '14'
            }   
        },
        legend: {
          left: 'center',
          bottom: '10',   
          data: data
        },
        color: ['#6dd8da', '#b6a2de', '#58afed' , '#58affd' , '#58eeed'], 
        series: [
          {
            name: '',
            type: 'pie',
            radius: [25, 75],
            center: ['50%', '38%'],
            data: item,
//              label: {
//              normal: {
//                    position: 'inner',
//                    show : false
//                 }
//             },
          },
        ]
      })
    }
  }
}
</script>