<template>
  <div class="dashboard-container">
    <div class="item">
      <h4 style="font-size: 25px"><img height="20px" width="25px" :src="require('@/icons/image/nested.png')"> 快捷导航</h4>
      <data-tabs :option="DataOption" style="margin-left: 50px"></data-tabs>
    </div>
    <div>
      <h4 style="font-size: 25px"><img height="20px" width="25px" :src="require('@/icons/image/form.png')"> 服务器信息</h4>
      <table class="table-osInfo">
        <tr class="tr-01">
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">cpu空闲率</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">{{osInfo.cpuInfo.cupFreeIdle}}</td>
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">cpu使用率</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;">{{osInfo.cpuInfo.combined}}</td>
        </tr>
        <tr class="tr-01">
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">硬盘可用空间</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">{{osInfo.hardDiskInfo.avail}}</td>
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">硬盘总计空间</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;">{{ osInfo.hardDiskInfo.total}}</td>
        </tr>
        <tr class="tr-01">
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">硬盘已使用空间</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">{{osInfo.hardDiskInfo.used}}</td>
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">硬盘空闲空间</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;">{{osInfo.hardDiskInfo.free}}</td>
        </tr>
        <tr class="tr-01">
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">硬盘空闲率</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">{{ osInfo.hardDiskInfo.freeIdleRate }}</td>
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">IP信息</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;">{{osInfo.ipInfo}}</td>
        </tr>
        <tr class="tr-01">
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">操作系统</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">{{osInfo.osBasicInfo.osName}}</td>
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">操作系统版本</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;">{{osInfo.osBasicInfo.osVersion }}</td>
        </tr>
        <tr class="tr-01">
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">总内存大小</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">{{osInfo.ramInfo.memTotal}}</td>
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">可用内存大小</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;">{{osInfo.ramInfo.memRam}}</td>
        </tr>
        <tr class="tr-01">
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">已用内存大小</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">{{ osInfo.ramInfo.memUsed }}</td>
          <td class="td-title" style="border-bottom: 1px solid #c0c4cc;border-right: 1px solid #c0c4cc;">空闲内存大小</td>
          <td class="td-osInfo" style="border-bottom: 1px solid #c0c4cc">{{osInfo.ramInfo.memFree}}</td>
        </tr>
        <tr>
          <td class="td-title"  style="border-right: 1px solid #c0c4cc;">内存使用率</td>
          <td class="td-osInfo" style="border-right: 1px solid #c0c4cc;">{{osInfo.ramInfo.memoryUsage}}</td>
          <td class="td-title" style="border-right: 1px solid #c0c4cc;"></td>
          <td class="td-osInfo"></td>
        </tr>
      </table>
    </div>
  </div>

</template>
<script>
import DataTabs from '@/components/DataTab/DataTabs1'
import TableList from '@/components/table/TableList.vue'
import {getOsInfo} from '@/api/dashboard';

export default {
  mounted() {
    this.getOsInfo()
  },
  name: 'Dashboard',
  components: {
    DataTabs,
    TableList
  },
  data() {
    return {
      DataOption: {
        span: 6,
        data: []
      },
      osInfo: {
        cpuInfo:{
          cupFreeIdle: '',
          combined: ''
        } ,
        hardDiskInfo: {
          avail: '',
          total: '',
          used: '',
          free: '',
          freeIdleRate: '',
        },
        ipInfo: '',
        osBasicInfo: {
          osName: '',
          osVersion: ''
        },
        ramInfo: {
          memTotal: '',
          memRam: '',
          memUsed: '',
          memFree: '',
          memoryUsage: ''
        }
      },
    }
  },
  methods: {
    async getOsInfo() {
      try {
        let res = await getOsInfo()
        let content = res.data.data
        this.osInfo = content.osInfo
        this.osInfo.ipInfo = content.ipInfo
        this.load = false
      } catch (even) {
        this.load = false
        this.$message.error(even.msg)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../../components/styles/data-tabs1.scss';
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

</style>

<style>
.table-osInfo{
  position: relative;
  left: 50px;
  display: block;
  width: 1000px;
  border-spacing: 0;
  border: 1px solid #c0c4cc;
}
.td-title{
  width: 200px;
  height: 32px;
  padding-left: 8px;
  background: #f8f8f8;
  text-align: left;
  font-size: 15px;
  font-weight: bold;
  line-height: 30px;
}

.td-osInfo{
  padding-left: 8px;
  width: 300px;
  height: 32px;
}



</style>
