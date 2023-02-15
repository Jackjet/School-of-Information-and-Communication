<template>
  <el-main>
    <template>
      <el-col :span="24" class="warp-main" v-loading="isSubmitLoading">
        <el-form label-width="120px">
          <el-row class='el-row-el ele-tree ml' v-if="data2.length">
            <div style="text-align: center;margin: 30px 0;">管理系统菜单权限</div>
            <div style="text-align: left;">
              <el-checkbox :indeterminate="isTreeAll" v-model="checkAllTree" @change="TreeAllChange">全选</el-checkbox>
            </div>
            <el-tree :data="data2" :render-content="renderContent" show-checkbox @check-change="TreeChange" :default-checked-keys="choose" default-expand-all node-key="id" ref="tree" highlight-current :props="defaultProps"></el-tree>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item class="el-button-el">
                <el-button type="primary" @click="onPageReturn">取消</el-button>
                <el-button :disabled="isAdmin" type="primary" @click="onSubmit">确定</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
    </template>
  </el-main>
</template>

<script>
// import { getRolePermissionList, insertRolePermission } from '@/api/omManger/comments'
export default {
  data () {
    return {
      id: '',
      data2: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      choose: [],
      chooseArr: [],
      checkAllTree: false,
      isTreeAll: true,
      isAdmin: false,
      isSubmitLoading: false,
      TreeLength: 0,
      createTimes: ''
    }
  },

  mounted () {
    let that = this
    that.isSubmitLoading = true
    that.id = that.$route.query.id
    that.isAdmin = that.$route.query.name === 'admin'
    that.getList()
  },

  methods: {
     renderContent(h, { node, data }) {
      let dom;
      // 懒加载图标设置
        dom = (
        <p class="custom-tree-node">
            <img src={require("@/assets/image/wj.png")}></img>
            <span style="margin-left:5px;" title={data.desc}>
            {node.label}
            </span>
        </p>
        );
      return dom;
    },  
    TreeAllChange (val) {
      let that = this
      if (that.checkAllTree) {
        // 全选
        that.$refs.tree.setCheckedNodes(that.data2)
      } else {
        // 取消选中
        that.$refs.tree.setCheckedKeys([])
      }
    },
    TreeChange (data, checked, indeterminate) {
      let that = this
      let nowLength = that.$refs.tree.getCheckedKeys().concat(that.$refs.tree.getHalfCheckedKeys()).length
      if (nowLength === 0) {
        that.checkAllTree = false
        that.isTreeAll = false
      } else if (nowLength === that.TreeLength) {
        that.checkAllTree = true
        that.isTreeAll = false
      } else {
        that.checkAllTree = false
        that.isTreeAll = true
      }
    },
    // 详情
    async getFindList () {
      const that = this
      try {
        // let url = this.api.role.getRoleList
        const response = await getRolePermissionList({ roleId : this.id })
        if (response.code === 1) {
          let arr = []
          response.data.forEach(item => {
            arr.push(item.permissionId)  
          })
         
          that.choose = arr
          return false
        }
        that.$message.error(response.msg)
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    // 列表
    async getList () {
      const that = this
      try {
        // const response = await getRolePermissionList({ roleId : this.id })
        // if (response.data.code === 1) {
          that.isSubmitLoading = false
          that.data2 = [{"id":"1","parentId":"","name":"首页","label":"首页","icon":null,"type":"menu","route":"/dashboard","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"2","parentId":"","name":"知识库","label":"知识库","icon":null,"type":"menu","route":"2","icon2":null,"orderNum":2,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":[{"id":"2-1","parentId":"2","name":"模型管理","label":"模型管理","icon":null,"type":"menu","route":"/index/model","icon2":null,"orderNum":0,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"2-2","parentId":"2","name":"数学模型建模","label":"数学模型建模","icon":null,"type":"menu","route":"/index/dataModel","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null}]},{"id":"3","parentId":"","name":"大数据处理服务","label":"大数据处理服务","icon":null,"type":"menu","route":"3","icon2":null,"orderNum":3,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":[{"id":"3-1","parentId":"3","name":"数学模型执行引擎","label":"数学模型执行引擎","icon":null,"type":"menu","route":"/dataProcessingService/digitalModel","icon2":null,"orderNum":0,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"3-2","parentId":"3","name":"数据服务接口管理","label":"数据服务接口管理","icon":null,"type":"menu","route":"/dataProcessingService/dataServiceApiView","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"3-3","parentId":"3","name":"数据服务接口计划任务","label":"数据服务接口计划任务","icon":null,"type":"menu","route":"/dataProcessingService/dataServicePlanView","icon2":null,"orderNum":2,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null}]},{"id":"4","parentId":"","name":"运维行为管理","label":"运维行为管理","icon":null,"type":"menu","route":"4","icon2":null,"orderNum":4,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":[{"id":"4-1","parentId":"4","name":"用户管理","label":"用户管理","icon":null,"type":"menu","route":"/omMager/user","icon2":null,"orderNum":0,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"4-2","parentId":"4","name":"组件管理","label":"组件管理","icon":null,"type":"menu","route":"/omMager/comments","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"4-3","parentId":"4","name":"日志管理","label":"日志管理","icon":null,"type":"menu","route":"/omMager/log","icon2":null,"orderNum":2,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"4-4","parentId":"4","name":"审批管理","label":"审批管理","icon":null,"type":"menu","route":"/omMager/approve","icon2":null,"orderNum":3,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null}]},{"id":"5","parentId":"","name":"基础环境监控","label":"基础环境监控","icon":null,"type":"menu","route":"5","icon2":null,"orderNum":5,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":[{"id":"5-1","parentId":"5","name":"基础环境监控","label":"基础环境监控","icon":null,"type":"menu","route":"/monitoringView/environmentView","icon2":null,"orderNum":0,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"5-2","parentId":"5","name":"系统监控","label":"系统监控","icon":null,"type":"menu","route":"/monitoringView/systemView","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"5-3","parentId":"5","name":"数据监控","label":"数据监控","icon":null,"type":"menu","route":"/monitoringView/dataView","icon2":null,"orderNum":2,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"5-4","parentId":"5","name":"资源监控","label":"资源监控","icon":null,"type":"menu","route":"/monitoringView/sourceView","icon2":null,"orderNum":3,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null}]},{"id":"6","parentId":"","name":"用户行为监控","label":"用户行为监控","icon":null,"type":"menu","route":"6","icon2":null,"orderNum":6,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":[{"id":"6-1","parentId":"6","name":"用户登录信息监控","label":"用户登录信息监控","icon":null,"type":"menu","route":"/behavior/userLogin","icon2":null,"orderNum":0,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"6-2","parentId":"6","name":"在线用户监控","label":"在线用户监控","icon":null,"type":"menu","route":"/behavior/online","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"6-3","parentId":"6","name":"用户活跃度","label":"用户活跃度","icon":null,"type":"menu","route":"/behavior/activity","icon2":null,"orderNum":2,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null}]},{"id":"7","parentId":"","name":"模块监控","label":"模块监控","icon":null,"type":"menu","route":"7","icon2":null,"orderNum":7,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":[{"id":"7-1","parentId":"7","name":"模块访问统计","label":"模块访问统计","icon":null,"type":"menu","route":"/moduleMonitorView/moduleStatisticsView","icon2":null,"orderNum":0,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"7-2","parentId":"7","name":"平台性能监控","label":"平台性能监控","icon":null,"type":"menu","route":"/moduleMonitorView/performanceView","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"7-3","parentId":"7","name":"数据监控","label":"数据监控","icon":null,"type":"menu","route":"/moduleMonitorView/dataMonitorView","icon2":null,"orderNum":2,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"7-4","parentId":"7","name":"资源监控","label":"资源监控","icon":null,"type":"menu","route":"/moduleMonitorView/resourceView","icon2":null,"orderNum":3,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null}]},{"id":"8","parentId":"","name":"数据库运维","label":"数据库运维","icon":null,"type":"menu","route":"8","icon2":null,"orderNum":8,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":[{"id":"8-1","parentId":"8","name":"参数配置","label":"参数配置","icon":null,"type":"menu","route":"/dataBase/parameter","icon2":null,"orderNum":0,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"8-2","parentId":"8","name":"实例管理","label":"实例管理","icon":null,"type":"menu","route":"/dataBase/examples","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"8-3","parentId":"8","name":"数据库监控","label":"数据库监控","icon":null,"type":"menu","route":"/dataBase/dataMonitoring","icon2":null,"orderNum":2,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"8-4","parentId":"8","name":"备份和恢复","label":"备份和恢复","icon":null,"type":"menu","route":"/dataBase/copy","icon2":null,"orderNum":3,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"8-5","parentId":"8","name":"远程SQL","label":"远程SQL","icon":null,"type":"menu","route":"/dataBase/sql","icon2":null,"orderNum":4,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null}]},{"id":"9","parentId":"","name":"远程控制","label":"远程控制","icon":null,"type":"menu","route":"9","icon2":null,"orderNum":9,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":[{"id":"9-1","parentId":"9","name":"运维指令分类","label":"运维指令分类","icon":null,"type":"menu","route":"/longRangeControlView/operationalClassView","icon2":null,"orderNum":0,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"9-2","parentId":"9","name":"日常运维指令","label":"日常运维指令","icon":null,"type":"menu","route":"/longRangeControlView/operationalDayView","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"9-3","parentId":"9","name":"远程控制台","label":"远程控制台","icon":null,"type":"menu","route":"/longRangeControlView/remoteConsoleView","icon2":null,"orderNum":2,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null}]},{"id":"10","parentId":"","name":"自动化部署","label":"自动化部署","icon":null,"type":"menu","route":"10","icon2":null,"orderNum":10,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":[{"id":"10-1","parentId":"10","name":"安装路径管理","label":"安装路径管理","icon":null,"type":"menu","route":"/deploymentView/installationPathView","icon2":null,"orderNum":0,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"10-2","parentId":"10","name":"自动化部署管理","label":"自动化部署管理","icon":null,"type":"menu","route":"/deploymentView/deploymentView","icon2":null,"orderNum":1,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null},{"id":"10-3","parentId":"10","name":"自动化部署日志","label":"自动化部署日志","icon":null,"type":"menu","route":"/deploymentView/deploymentLogView","icon2":null,"orderNum":2,"outOpen":null,"remark":null,"platform":null,"haveChild":null,"children":null}]}]
          // that.choose = ['1','2']
        //   that.getFindList()
          return false
        // }
        // that.$message.error(response.msg)
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },

    // 提交数据
    async onSubmit () {
      let that = this
      try {
        let obj = {
          roleId: that.id,
          menuTreeIds: this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys())
        }
        const response = await insertRolePermission(obj)
        if (response.data.code === 1) {
          that.isSubmitLoading = false
          that.$message.success(response.data.msg)
        //   that.$router.push({ path: 'roleList' })
          this.$router.push({ path: 'comments' })
          // that.getList()
          return false
        }
        that.$message.error(response.msg)
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
   onPageReturn() {
      this.$router.push({ path: 'indexUser' })
   }
   
  }
}
</script>
