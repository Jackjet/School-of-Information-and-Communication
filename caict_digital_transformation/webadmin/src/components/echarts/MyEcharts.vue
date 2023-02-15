<template>
  <div ref="myEcharts" id="myEcharts"></div>
</template>
<script>
import echarts from "echarts";

export default {
  props: {
    option: String,
    required: true
  },
  data() {
    return {
      chart: undefined,
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.chart = echarts.init(this.$refs.myEcharts);
      this.chart.setOption(this.option);
    },
  },
  watch: {
    //观察option的变化
    option: {
      handler(newVal, oldVal) {
        if (this.chart) {
          if (newVal) {
            this.chart.setOption(newVal);
          } else {
            this.chart.setOption(oldVal);
          }
        } else {
          this.init();
        }
      },
      deep: true //对象内部属性的监听，关键。
    }
  }
};
</script>
<style lang="scss" scope>
#myEcharts {
  width: 100%;
  height: 100%;
}
</style>
