<template>
  <div ref="ripple" id="ripple"></div>
</template>
<script>
import echarts from "echarts/lib/echarts";
import "echarts-liquidfill";

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
      this.chart = echarts.init(this.$refs.ripple);
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
#ripple {
  width: 130px;
  height: 130px;
}
</style>
