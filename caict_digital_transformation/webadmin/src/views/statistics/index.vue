<template>
  <div class="statistics-page">
    <div class="page-content">
      <div class="utilization-rate">
        <div class="utilization-rate-item" style="margin-left: 0">
          <div class="rate-item-title">CPU利用率</div>
          <div class="rate-item-time">{{ time | formatDate }}</div>
          <div class="rate-item-echarts">
            <UtilizationRate :option="cupUseOption" />
          </div>
        </div>
        <div class="utilization-rate-item">
          <div class="rate-item-title">内存利用率</div>
          <div class="rate-item-time">{{ time | formatDate }}</div>
          <div class="rate-item-echarts">
            <UtilizationRate :option="memoryUseOption" />
          </div>
        </div>
        <div class="utilization-rate-item">
          <div class="rate-item-title">磁盘利用率</div>
          <div class="rate-item-time">{{ time | formatDate }}</div>
          <div class="rate-item-echarts">
            <UtilizationRate :option="diskUseOption" />
          </div>
        </div>
      </div>

      <div class="statistics-column">
        <div class="active-num">
          <div class="active-num-item" style="margin-left: 0">
            <div class="active-item-title">今日 | 累计活跃用户数</div>
            <div class="active-item-time">
              {{ currentDay }}-{{ currentDay }}
            </div>
            <div class="active-item-text">
              合计
              <br />
              {{ todayUserCount }}人
            </div>
          </div>
          <div class="active-num-item">
            <div class="active-item-title">7日 | 累计活跃用户数</div>
            <div class="active-item-time">
              {{ getBeforeDate(7) }}-{{ currentDay }}
            </div>
            <div class="active-item-text">
              合计
              <br />
              {{ afterSevenUserCount }}人
            </div>
          </div>
          <div class="active-num-item">
            <div class="active-item-title">30日 | 累计活跃用户数</div>
            <div class="active-item-time">
              {{ getBeforeDate(30) }}-{{ currentDay }}
            </div>
            <div class="active-item-text">
              合计
              <br />
              {{ afterAMonthCount }}人
            </div>
          </div>
        </div>

        <div class="division-line">
          <div class="division-line-title" style="display: flex">
            <span>访问分析</span>
            <div style="flex: 1; text-align: right">
              <el-date-picker
                style="width: 280px; height: 32px; margin-bottom: 10px"
                v-model="selectDate"
                type="daterange"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                @change="dateChange"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              >
              </el-date-picker>
            </div>
          </div>
        </div>
        <div class="visit-data" style="margin-top: 10px">
          <div class="visit-echarts">
            <my-echarts :option="visitUserOption" />
          </div>
          <div class="visit-echarts">
            <my-echarts :option="serviceVisitCountOption" />
          </div>
        </div>
        <div class="visit-data">
          <div class="visit-echarts">
            <my-echarts :option="newUserOption" />
          </div>
          <div class="visit-echarts">
            <my-echarts :option="newNewsOption" />
          </div>
        </div>

        <div class="division-line">
          <div class="division-line-title">统计分析</div>
        </div>
        <div class="visit-data" style="margin-top: 10px">
          <div class="visit-echarts">
            <my-echarts :option="userSourceOption" />
          </div>
          <div class="visit-echarts">
            <my-echarts :option="userAddressOption" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import UtilizationRate from "@/components/echarts/UtilizationRate";
import MyEcharts from "@/components/echarts/MyEcharts";
import {
  statisticActiveUser,
  statisticNewNews,
  statisticNewUser,
  statisticOsInfo,
  statisticServiceVisitCount,
  statisticUserAddress,
  statisticUserSource,
  statisticVisitUser,
} from "@/api/statistic";
import moment from "moment";

export default {
  components: {
    UtilizationRate,
    MyEcharts,
  },
  data() {
    return {
      time: Date.parse(new Date()),
      currentDay: this.getDate(),
      todayUserCount: 0,
      afterSevenUserCount: 0,
      afterAMonthCount: 0,
      visitUserOption: {},
      serviceVisitCountOption: {},
      newUserOption: {},
      newNewsOption: {},
      userSourceOption: {},
      userAddressOption: {},
      cupUseOption: {},
      memoryUseOption: {},
      diskUseOption: {},

      selectDate: [this.getBeforeDate(7, undefined, "-"), this.getDate("-")],
      serviceType: [
        "企业服务",
        "产业监测",
        "评测验证",
        "创新孵化",
        "资讯",
        "关于我们",
        "政府支撑",
      ],
      newsType: ["产业动态", "行业资讯", "政策解读", "最新活动"],
    };
  },
  created() {
    let today = this.getDate("-");
    let afterSevenDay = this.getBeforeDate(7, undefined, "-");
    let afterAMonth = this.getBeforeDate(30, undefined, "-");
    this.statisticActiveUser(0, today, today);
    this.statisticActiveUser(1, afterSevenDay, today);
    this.statisticActiveUser(2, afterAMonth, today);

    this.statisticVisitUser();
    this.statisticServiceVisitCount();
    this.statisticNewUser();
    this.statisticNewNews();

    this.statisticUserSource();
    this.statisticUserAddress();

    this.statisticOsInfo();
  },

  filters: {
    formatDate: function (value) {
      let date = new Date(value);
      let y = date.getFullYear();
      let MM = date.getMonth() + 1;
      MM = MM < 10 ? "0" + MM : MM;
      let d = date.getDate();
      d = d < 10 ? "0" + d : d;
      let h = date.getHours();
      h = h < 10 ? "0" + h : h;
      let m = date.getMinutes();
      m = m < 10 ? "0" + m : m;
      let s = date.getSeconds();
      s = s < 10 ? "0" + s : s;
      return y + "." + MM + "." + d + " " + h + ":" + m + ":" + s;
    },
  },

  methods: {
    dateChange() {
      this.statisticVisitUser();
      this.statisticServiceVisitCount();
      this.statisticNewUser();
      this.statisticNewNews();
    },

    async statisticActiveUser(type, startTime, endTime) {
      const that = this;
      let data = {};
      data.startTime = startTime;
      data.endTime = endTime;

      try {
        that.isSubmitLoading = true;
        const res = await statisticActiveUser(data);
        if (res.data.code === 1) {
          let activeUserData = res.data.data;
          switch (type) {
            case 0: //当天
              this.todayUserCount = that.sumActiveUser(activeUserData);
              break;
            case 1: //七日
              this.afterSevenUserCount = that.sumActiveUser(activeUserData);
              break;
            case 2: //三十日
              this.afterAMonthCount = that.sumActiveUser(activeUserData);
              break;
          }
        } else {
          that.$message.error(res.data.msg);
        }
      } catch (even) {
        that.$message.error(even.msg);
      }
    },

    async statisticOsInfo() {
      const that = this;
      try {
        that.isSubmitLoading = true;
        const res = await statisticOsInfo();
        if (res.data.code === 1) {
          that.fullCpuUse(res.data.data.cpuInfo);
          that.fullMemoryUse(res.data.data.ramInfo);
          that.fullDiskUse(res.data.data.hardDiskInfo);
        } else {
          that.$message.error(res.data.msg);
        }
      } catch (even) {
        console.log(even);
      }
    },
    fullCpuUse(data) {
      let combined = data.combined;
      if (combined && combined.length > 0) {
        combined = combined.replaceAll("%", "");
      }

      let primaryColor = "#5AAFF4";
      let fillData = combined / 100;

      let option = {
        title: {
          text: (fillData * 100).toFixed(0) + "%",
          left: "center",
          top: "47%",
          textStyle: {
            fontSize: 34,
            fontWeight: "normal",
            color: primaryColor,
          },
        },
        series: [
          {
            type: "liquidFill",
            data: [
              {
                value: fillData,
              },
            ],
            backgroundStyle: {
              color: "#FFFFFF",
            },
            color: [primaryColor],
            radius: "86%",
            center: ["50%", "50%"],
            label: {
              formatter: fillData,
              fontSize: 20,
              color: primaryColor,
              fontStyle: "normal",
              fontWeight: "normal",
            },
            outline: {
              // 轮廓设置
              show: true,
              borderDistance: 3, // 轮廓间距
              itemStyle: {
                borderColor: primaryColor, // 轮廓颜色
                borderWidth: 2, // 轮廓大小
                shadowBlur: "none", // 轮廓阴影
              },
            },
          },
        ],
      };

      this.cupUseOption = option;
    },
    fullMemoryUse(data) {
      let combined = data.memoryUsage;
      if (combined && combined.length > 0) {
        combined = combined.replaceAll("%", "");
      }

      let primaryColor = "#62D9AD";
      let fillData = combined / 100;

      let option = {
        title: {
          text: (fillData * 100).toFixed(0) + "%",
          left: "center",
          top: "47%",
          textStyle: {
            fontSize: 34,
            fontWeight: "normal",
            color: primaryColor,
          },
        },
        series: [
          {
            type: "liquidFill",
            data: [
              {
                value: fillData,
              },
            ],
            backgroundStyle: {
              color: "#FFFFFF",
            },
            color: [primaryColor],
            radius: "86%",
            center: ["50%", "50%"],
            label: {
              formatter: fillData,
              fontSize: 20,
              color: primaryColor,
              fontStyle: "normal",
              fontWeight: "normal",
            },
            outline: {
              // 轮廓设置
              show: true,
              borderDistance: 3, // 轮廓间距
              itemStyle: {
                borderColor: primaryColor, // 轮廓颜色
                borderWidth: 2, // 轮廓大小
                shadowBlur: "none", // 轮廓阴影
              },
            },
          },
        ],
      };

      this.memoryUseOption = option;
    },
    fullDiskUse(data) {
      let combined = data.freeIdleRate;
      if (combined && combined.length > 0) {
        combined = combined.replaceAll("%", "");
      }

      let primaryColor = "#6E62E4";
      let fillData = (100 - combined) / 100;

      let option = {
        title: {
          text: (fillData * 100).toFixed(0) + "%",
          left: "center",
          top: "47%",
          textStyle: {
            fontSize: 34,
            fontWeight: "normal",
            color: primaryColor,
          },
        },
        series: [
          {
            type: "liquidFill",
            data: [
              {
                value: fillData,
              },
            ],
            backgroundStyle: {
              color: "#FFFFFF",
            },
            color: [primaryColor],
            radius: "86%",
            center: ["50%", "50%"],
            label: {
              formatter: fillData,
              fontSize: 20,
              color: primaryColor,
              fontStyle: "normal",
              fontWeight: "normal",
            },
            outline: {
              // 轮廓设置
              show: true,
              borderDistance: 3, // 轮廓间距
              itemStyle: {
                borderColor: primaryColor, // 轮廓颜色
                borderWidth: 2, // 轮廓大小
                shadowBlur: "none", // 轮廓阴影
              },
            },
          },
        ],
      };

      this.diskUseOption = option;
    },

    async statisticVisitUser() {
      const that = this;
      let data = {};
      data.startTime = this.selectDate[0];
      data.endTime = this.selectDate[1];

      try {
        that.isSubmitLoading = true;
        const res = await statisticVisitUser(data);
        if (res.data.code === 1) {
          that.fullVisitUser(data.startTime, data.endTime, res.data.data);
        } else {
          that.$message.error(res.data.msg);
        }
      } catch (even) {
        // that.$message.error(even.msg)
        console.log(even);
      }
    },
    fullVisitUser(startTime, endTime, data) {
      console.log(data);
      let json = {};
      for (let i = 0; i < data.length; i++) {
        json[data[i].day] = data[i].count;
      }

      let result = this.getConvertData(startTime, endTime, json);
      let days = result.days;
      let counts = result.counts;

      let option = {
        title: {
          text: "系统访问人数",
          subtext: this.getSubtext(startTime, endTime),
          x: "20px",
          y: "top",
          textStyle: {
            fontFamily: "Arial, Verdana, sans...",
            fontSize: 12,
            fontStyle: "normal",
            fontWeight: "normal",
          },
        },
        legend: {
          itemHeight: 10,
          itemWidth: 10,
          left: "center",
          bottom: 10,
          data: [
            {
              name: "在线人数",
              icon: "circle",
            },
          ],
        },
        xAxis: {
          type: "category",
          data: days,
        },
        yAxis: {
          type: "value",
          minInterval: 5, //只显示整数
          splitLine: {
            lineStyle: {
              type: "dotted",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
        },
        series: [
          {
            name: "在线人数",
            type: "line",
            itemStyle: {
              normal: {
                color: "#294D99",
              },
            },
            label: {
              normal: {
                show: true,
                position: "top",
              },
            },
            data: counts,
          },
        ],
      };

      this.visitUserOption = option;
    },

    async statisticServiceVisitCount() {
      const that = this;
      let data = {};
      data.startTime = this.selectDate[0];
      data.endTime = this.selectDate[1];

      try {
        that.isSubmitLoading = true;
        const res = await statisticServiceVisitCount(data);
        if (res.data.code === 1) {
          that.fullServiceVisitCount(
            data.startTime,
            data.endTime,
            res.data.data
          );
        } else {
          that.$message.error(res.data.msg);
        }
      } catch (even) {
        that.$message.error(even.msg);
      }
    },
    fullServiceVisitCount(startTime, endTime, data) {
      let json = {};
      for (let i = 0; i < data.length; i++) {
        json[data[i].type] = data[i].sum;
      }

      let counts = [];
      for (let i = 0; i < this.serviceType.length; i++) {
        let type = this.serviceType[i];
        if (json.hasOwnProperty(type)) {
          counts.push(json[type]);
        } else {
          counts.push(0);
        }
      }

      let option = {
        title: {
          text: "服务访问量",
          subtext: this.getSubtext(startTime, endTime),
          x: "20px",
          y: "top",
          textStyle: {
            fontFamily: "Arial, Verdana, sans...",
            fontSize: 12,
            fontStyle: "normal",
            fontWeight: "normal",
          },
        },
        legend: {
          itemHeight: 10,
          itemWidth: 10,
          left: "center",
          bottom: 10,
          data: [
            {
              name: "访问量",
              icon: "circle",
            },
          ],
        },
        xAxis: {
          type: "category",
          data: this.serviceType,
        },
        yAxis: {
          type: "value",
          minInterval: 5, //只显示整数
          splitLine: {
            lineStyle: {
              type: "dotted",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
        },
        series: [
          {
            name: "访问量",
            type: "line",
            itemStyle: {
              normal: {
                color: "#294D99",
              },
            },
            label: {
              normal: {
                show: true,
                position: "top",
              },
            },
            data: counts,
          },
        ],
      };

      this.serviceVisitCountOption = option;
    },

    async statisticNewUser() {
      const that = this;
      let data = {};
      data.startTime = this.selectDate[0];
      data.endTime = this.selectDate[1];

      try {
        that.isSubmitLoading = true;
        const res = await statisticNewUser(data);
        if (res.data.code === 1) {
          that.fullNewUser(data.startTime, data.endTime, res.data.data);
        } else {
          that.$message.error(res.data.msg);
        }
      } catch (even) {
        that.$message.error(even.msg);
      }
    },
    fullNewUser(startTime, endTime, data) {
      let json = {};
      for (let i = 0; i < data.length; i++) {
        json[data[i].regist_day] = data[i].count;
      }

      let result = this.getConvertData(startTime, endTime, json);
      let days = result.days;
      let counts = result.counts;

      let option = {
        title: {
          text: "新增客户数",
          subtext: this.getSubtext(startTime, endTime),
          x: "20px",
          y: "top",
          textStyle: {
            fontFamily: "Arial, Verdana, sans...",
            fontSize: 12,
            fontStyle: "normal",
            fontWeight: "normal",
          },
        },
        legend: {
          itemHeight: 10,
          itemWidth: 10,
          left: "center",
          bottom: 10,
          data: [
            {
              name: "人数",
              icon: "circle",
            },
          ],
        },
        xAxis: {
          type: "category",
          data: days,
        },
        yAxis: {
          type: "value",
          minInterval: 5, //只显示整数
          splitLine: {
            lineStyle: {
              type: "dotted",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
        },
        series: [
          {
            name: "人数",
            type: "line",
            itemStyle: {
              normal: {
                color: "#294D99",
              },
            },
            label: {
              normal: {
                show: true,
                position: "top",
              },
            },
            data: counts,
          },
        ],
      };

      this.newUserOption = option;
    },

    async statisticNewNews() {
      const that = this;
      let data = {};
      data.startTime = this.selectDate[0];
      data.endTime = this.selectDate[1];

      try {
        that.isSubmitLoading = true;
        const res = await statisticNewNews(data);
        if (res.data.code === 1) {
          that.fullNewNews(data.startTime, data.endTime, res.data.data);
        } else {
          that.$message.error(res.data.msg);
        }
      } catch (even) {
        console.log(even);
      }
    },
    fullNewNews(startTime, endTime, data) {
      let json = {};
      let cydtData = data[this.newsType[0]];
      let hyzxData = data[this.newsType[1]];
      let zxjdData = data[this.newsType[2]];
      let zxhdData = data[this.newsType[3]];

      let result = this.getConvertData(startTime, endTime, cydtData);
      let days = result.days;
      let cydtCounts = result.counts;
      let hyzxCounts = this.getConvertData(startTime, endTime, hyzxData).counts;
      let zxjdCounts = this.getConvertData(startTime, endTime, zxjdData).counts;
      let zxhdCounts = this.getConvertData(startTime, endTime, zxhdData).counts;

      let option = {
        title: {
          text: "新增资讯数",
          subtext: this.getSubtext(startTime, endTime),
          x: "20px",
          y: "top",
          textStyle: {
            fontFamily: "Arial, Verdana, sans...",
            fontSize: 12,
            fontStyle: "normal",
            fontWeight: "normal",
          },
        },
        legend: {
          itemHeight: 10,
          itemWidth: 10,
          left: "center",
          bottom: 10,
          data: [
            {
              name: this.newsType[0],
              icon: "circle",
            },
            {
              name: this.newsType[1],
              icon: "circle",
            },
            {
              name: this.newsType[2],
              icon: "circle",
            },
            {
              name: this.newsType[3],
              icon: "circle",
            },
          ],
        },
        xAxis: {
          type: "category",
          data: days,
        },
        yAxis: {
          type: "value",
          splitLine: {
            lineStyle: {
              type: "dotted",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
        },
        series: [
          {
            name: this.newsType[0],
            type: "bar",
            itemStyle: {
              normal: {
                color: "#00BEF3",
              },
            },
            data: cydtCounts,
          },
          {
            name: this.newsType[1],
            type: "bar",
            itemStyle: {
              normal: {
                color: "#C759A1",
              },
            },
            data: hyzxCounts,
          },
          {
            name: this.newsType[2],
            type: "bar",
            itemStyle: {
              normal: {
                color: "#5653A3",
              },
            },
            data: zxjdCounts,
          },
          {
            name: this.newsType[3],
            type: "bar",
            itemStyle: {
              normal: {
                color: "#294D99",
              },
            },
            data: zxhdCounts,
          },
        ],
      };

      this.newNewsOption = option;
    },

    async statisticUserSource() {
      const that = this;
      try {
        that.isSubmitLoading = true;
        const res = await statisticUserSource();
        if (res.data.code === 1) {
          that.fullUserSource(res.data.data);
        } else {
          that.$message.error(res.data.msg);
        }
      } catch (even) {
        console.log(even);
      }
    },
    fullUserSource(data) {
      let legendData = [];
      let seriesData = [];
      Object.keys(data.content).forEach((key) => {
        let legendRow = {};
        legendRow.name = key;
        legendRow.icon = "circle";
        legendData.push(legendRow);

        let seriesRow = {};
        seriesRow.name = key;
        seriesRow.value = data.content[key]["count"];
        seriesData.push(seriesRow);
      });

      let option = {
        legend: {
          itemHeight: 10,
          itemWidth: 10,
          left: "center",
          bottom: 5,
          data: legendData,
        },
        series: [
          {
            type: "pie",
            radius: "65%",
            center: ["50%", "50%"],
            label: {
              show: true,
              formatter: "{b}\n{d}%\n{c}",
            },
            selectedMode: "single",
            data: seriesData,
          },
        ],
      };

      this.userSourceOption = option;
    },

    async statisticUserAddress() {
      const that = this;
      try {
        that.isSubmitLoading = true;
        const res = await statisticUserAddress();
        if (res.data.code === 1) {
          that.fullUserAddress(res.data.data);
        } else {
          that.$message.error(res.data.msg);
        }
      } catch (even) {
        console.log(even);
      }
    },
    fullUserAddress(data) {
      let legendData = [];
      let seriesData = [];
      Object.keys(data.content).forEach((key) => {
        let legendRow = {};
        legendRow.name = key;
        legendRow.icon = "circle";
        legendData.push(legendRow);

        let seriesRow = {};
        seriesRow.name = key;
        seriesRow.value = data.content[key]["count"];
        seriesData.push(seriesRow);
      });

      let option = {
        legend: {
          itemHeight: 10,
          itemWidth: 10,
          left: "center",
          top: "bottom",
          data: legendData,
        },
        series: [
          {
            type: "pie",
            radius: [0, 100],
            center: ["50%", "50%"],
            label: {
              show: true,
              formatter: "{b}\n{d}%\n{c}",
            },
            roseType: "area",
            data: seriesData,
          },
        ],
      };

      this.userAddressOption = option;
    },

    getConvertData(startTime, endTime, json) {
      let days = [];
      let counts = [];
      let beginDate = moment(startTime);
      let endDate = moment(endTime);
      for (beginDate; beginDate <= endDate; ) {
        days.push(beginDate.format("MM-DD"));

        let tempDay = beginDate.format("YYYY-MM-DD");
        if (json != undefined && json.hasOwnProperty(tempDay)) {
          counts.push(json[tempDay]);
        } else {
          counts.push(0);
        }
        beginDate.add(1, "days");
      }

      let result = {};
      result["days"] = days;
      result["counts"] = counts;
      return result;
    },

    getSubtext(startTime, endTime) {
      return (
        startTime.replaceAll("-", ".") + "-" + endTime.replaceAll("-", ".")
      );
    },

    sumActiveUser(activeUserData) {
      let sum = 0;
      for (let i = 0; i < activeUserData.length; i++) {
        sum += activeUserData[i].count;
      }
      return sum;
    },

    getDate(separator) {
      let separatorStr;
      if (separator) {
        separatorStr = separator;
      } else {
        separatorStr = ".";
      }

      let date = new Date();
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let strDate = date.getDate();
      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
      }
      return year + separatorStr + month + separatorStr + strDate;
    },

    getBeforeDate(num, time, separator) {
      let n = num;
      let d = "";
      let separatorStr;
      if (separator) {
        separatorStr = separator;
      } else {
        separatorStr = ".";
      }

      if (time) {
        d = new Date(time);
      } else {
        d = new Date();
      }
      let year = d.getFullYear();
      let mon = d.getMonth() + 1;
      let day = d.getDate();
      if (day <= n) {
        if (mon > 1) {
          mon = mon - 1;
        } else {
          year = year - 1;
          mon = 12;
        }
      }
      d.setDate(d.getDate() - n);
      year = d.getFullYear();
      mon = d.getMonth() + 1;
      day = d.getDate();
      return (
        year +
        separatorStr +
        (mon < 10 ? "0" + mon : mon) +
        separatorStr +
        (day < 10 ? "0" + day : day)
      );
    },
  },
};
</script>
<style lang="scss" scoped>
.statistics-page {
  margin-top: -240px;
  padding: 0 30px;
  box-sizing: border-box;

  .page-content {
    width: 100%;
    height: auto;
    background: #fff;
    padding: 20px;
    box-sizing: border-box;
    border-radius: 5px;

    .utilization-rate {
      display: flex;
      justify-content: space-between;

      .utilization-rate-item {
        height: 200px;
        flex: 1;
        border: 1px solid #000;
        margin-left: 20px;
        padding: 15px;
        box-sizing: border-box;

        .rate-item-echarts {
          display: flex;
          justify-content: center;
          //   margin-top: 10px;
        }

        .rate-item-time {
          color: #999;
          font-size: 14px;
          line-height: 26px;
        }
      }
    }
  }

  .statistics-column {
    border-radius: 5px;
    margin-top: 20px;
    background: #fff;
    box-sizing: border-box;

    .active-num {
      display: flex;
      justify-content: space-between;

      .active-num-item {
        flex: 1;
        height: 120px;
        border: 1px solid #000;
        margin-left: 20px;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        padding: 0 15px;
        margin-bottom: 20px;
        box-sizing: border-box;

        .active-item-time {
          color: #999;
          font-size: 14px;
        }

        .active-item-text {
          color: #999;
        }
      }
    }

    .division-line {
      color: #3680e6;
      border-bottom: 2px solid #3680e6;
      line-height: 40px;
    }

    .visit-data {
      display: flex;

      .visit-echarts {
        height: 300px;
        flex: 1;
      }
    }
  }

  /deep/ .el-range__icon {
    line-height: 24px;
  }

  /deep/ .el-range-separator {
    line-height: 24px;
  }

  /deep/ .el-range__close-icon {
    line-height: 24px;
  }
}
</style>
