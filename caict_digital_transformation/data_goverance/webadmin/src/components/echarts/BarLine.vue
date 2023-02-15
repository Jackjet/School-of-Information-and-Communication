<!--
 * @Author: your name
 * @Date: 2020-06-29 17:26:29
 * @LastEditTime: 2020-07-24 15:25:54
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
      default: '350px'
    },
    wid: {
      type: String,
      default: 24
    },
    optionss: {
      type: Object,
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
      this.optionss.forEach((item, index) => {
        if(item.data.length > 0) {
          this.initChart(item)
        } 
      })
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
      let name = ''
      // let echart = echarts.init(document.getElementById(name))
      console.log(typeof item)
      if(item.name === '运行任务个数'){
        data.push('运行任务个数')
        name = '个数'
      }
      else{
        console.log(item.name)
        console.log(item.name.split('-')[0])
        console.log(item.name.split('-')[1])
        data.push(item.name.split('-')[0])
        data.push(item.name.split('-')[1])
        name = '条数'
      }
      
      
      this.chart = echarts.init(this.$el)
      let option = {
         title: {
            // text: item.name,
            x:'center',
            y: '',
            textStyle:{
              fontSize: '14'
            }   
        },
        tooltip : {
          trigger: 'axis',
          
          // formatter(params){   
          // }
        },
        legend: {
          left: 'center',
          top: '10',   
          data: data
        },
        xAxis: {
          type: 'category',
          data: item.month
        },

        yAxis: {
          name: name,
          minInterval:1,
          type: 'value'
        },
        series: [{
          name: data[0],
          data: item.data,
          type: 'line',
          showBackground: true,
          color: ['#58afed'],
          backgroundStyle: {
            color: 'rgba(220, 220, 220, 0.8)'
          }
        },
        { 
          name: data[1],
          data: item.data2,
          type: 'line',
          showBackground: true,
          color: ['#fff000'],
          backgroundStyle: {
            color: 'rgba(220, 220, 220, 0.8)'
          }
        }
        ]
      }
      this.chart.setOption(option)
    }
  }
}
</script>
