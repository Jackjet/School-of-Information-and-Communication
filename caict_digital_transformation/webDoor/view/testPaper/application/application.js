var data = {
  question: [
  {
    title: '数据产生',
    type: 1,
    option: [{
      order: 'A',
      contant: '手动录入',
      score: 1
    }, {
      order: 'B',
      contant: '外部导入',
      score: 2
    }, {
      order: 'C',
      contant: '系统生成',
      score: 3
    }, {
      order: 'D',
      contant: '无',
      score: 0
    }],
  }, {
    title: '数据描述',
    type: 1,
    option: [{
      order: 'A',
      contant: '非结构化',
      score: 1
    }, {
      order: 'B',
      contant: '结构化',
      score: 2
    }, {
      order: 'C',
      contant: '无',
      score: 0
    }],
  }, {
    title: '业务范围',
    type: 2,
    option: [{
      order: 'A',
      contant: '研发域',
      score: 1
    }, {
      order: 'B',
      contant: '生产域',
      score: 1
    }, {
      order: 'C',
      contant: '物流域',
      score: 1
    }, {
      order: 'D',
      contant: '服务域',
      score: 1
    }, {
      order: 'E',
      contant: '市场与销售域',
      score: 1
    }],
  }, {
    title: '系统范围',
    type: 2,
    option: [{
      order: 'A',
      contant: 'ERP',
      score: null
    }, {
      order: 'B',
      contant: 'MES',
      score: null
    }, {
      order: 'C',
      contant: 'CRM',
      score: null
    }, {
      order: 'D',
      contant: 'WMS',
      score: null
    }, {
      order: 'E',
      contant: 'SCADA',
      score: null
    }, {
      order: 'F',
      contant: '售后服务',
      score: null
    }, {
      order: 'G',
      contant: 'PLM/PDM',
      score: null
    }, {
      order: 'H',
      contant: '设计工具',
      score: null
    }, {
      order: 'I',
      contant: '供应链管理',
      score: null
    }, {
      order: 'J',
      contant: 'excel',
      score: null
    }],
  }, {
    title: '集成方式',
    type: 1,
    option: [{
      order: 'A',
      contant: '人工迁移',
      score: 1
    }, {
      order: 'B',
      contant: '系统集成',
      score: 2
    }, {
      order: 'C',
      contant: '无',
      score: 0
    }],
  }, {
    title: '应用深度',
    type: 1,
    option: [{
      order: 'A',
      contant: '显示',
      score: 1
    }, {
      order: 'B',
      contant: '分析',
      score: 2
    }, {
      order: 'C',
      contant: '无',
      score: 0
    }],
  }, {
    title: '更新方式',
    type: 1,
    option: [{
      order: 'A',
      contant: '手动更新',
      score: 1
    }, {
      order: 'B',
      contant: '系统更新',
      score: 2
    }, {
      order: 'C',
      contant: '无',
      score: 0
    }],
  }
  ],
  project: [{
    name: '研发域',
    childs: [{
      name: '业务数据',
      childs: ['产品编码', 'BOM数据', 'BOM版本', '设计图纸', '工艺路线', '程序指令', '工序参数', '产品需求', '作业指导书', '产品变更记录']
    }, {
      name: '指标数据',
      childs: ['新产品研发周期', '研发人数比例', '研发周期占生产周期比例', '新产品客户满意度']
    }]
  }, {
    name: '生产域',
    childs: [{
      name: '业务数据',
      childs: ['生产设备信息', '工人信息', '物料需求计划', '生产计划', '生产计划进度', '工单', '工单进度', '在制品数据', '设备运行数据', '质检数据']
    }, {
      name: '指标数据',
      childs: ['交期达成率', '成品合格率', 'SPC', '实际产能/理论产能', '设备OEE', '按计划执行率']
    }]
  }, {
    name: '物流域',
    childs: [{
      name: '业务数据',
      childs: ['仓库配置数据', '运输工具信息', '出入库计划', '出入库记录', '配送工单', '配送地点信息', '物料库存数据', '采购计划', '采购订单', '供应商数据']
    }, {
      name: '指标数据',
      childs: ['库存周转率', '配送效率', '采购周期', '供应商评价']
    }]
  }, {
    name: '服务域',
    childs: [{
      name: '业务数据',
      childs: ['产品运行数据', '产品故障数据', '客户的业务数据', '巡检记录', '退换货数据', '备品备件数据', '故障处理日志记录', '客户投诉数据']
    }, {
      name: '指标数据',
      childs: ['客户满意度', '客户投诉率']
    }]
  }, {
    name: '市场与销售域',
    childs: [{
      name: '业务数据',
      childs: ['客户信息', '销售订单数据', '客户需求数据', '商机挖掘数据', '商机跟踪数据', '销售指标', '报价', '合同执行进度', '客户互动数据', '销售预测']
    }, {
      name: '指标数据',
      childs: ['成单率', '回款率']
    }]
  }]
}