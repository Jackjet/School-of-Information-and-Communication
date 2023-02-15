<template>
  <div class="app-container">
    <!-- 左侧树 -->
    <div class="left">
        <el-col :span="23">
          <TreeList
            :base-data.sync="treeData.baseData"
            :node-key="treeData.nodeKey"
            :lazy-data="treeData.lazyData"
            :tree-refresh="treeData.refresh"
            @handleEvent="handleEvent"
          ></TreeList>
        </el-col>
    </div>
    <div class="right">
      <el-col :span="23">
        <TableList :table-data='tableData' v-loading="isSubmitLoading"
          :table-label="tableHeader"
          :table-option="tableOpction">
        </TableList>
      </el-col>
      <el-col :span='23'>
        <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      </el-col>
    </div>
  </div>
</template>

<script>
import { getList } from '@/api/table'
import TreeList from '@/components/tree/TreeList'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'

export default {
  components: {
    TreeList,
    TableList,
    Pagination
  },
  data () {
    return {
  
      // 树相关信息
      treeData: {
        refresh: 1,
        nodeKey: 'key',
        lazy: true,
        type: 0, // 省市区类型
        baseData: [], // 树的基础数据，从组件中获取到
        lazyData: [
          {
            key: 'id', // 节点唯一标识字段
            label: 'name', // 节点显示字段
            type: 1, // 数据类型
            api: '', // 获取数据的接口
            params: { key: 'pid', value: 1, type: 'url' }, // 获取数据的参数
            resFieldList: ['content'] // 数据所在字段
          },
          {
            key: 'id',
            label: 'name',
            type: 2,
            api: '',
            params: { key: 'pid', value: '', type: 'url' },
            resFieldList: ['content'], // 数据所在字段
            leaf: false
          },
          {
            key: 'id',
            label: 'name',
            type: 3,
            api: '',
            params: { key: 'pid', value: '', type: 'url' },
            resFieldList: ['content'], // 数据所在字段
            leaf: true
          },
        ],
      },
      // 过滤相关配置
      filterInfo: {
        query: {
          pid: 1
        }
      },
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '访客', list: 'salesMan'},
        {id: false, type: '', label: '访客手机号', list: 'salesManLoginNme'},
        {id: false, type: '', label: '被访人', list: 'standDisc'},
        {id: false, type: '', label: '拜访日期', list: 'startDate'},
        {id: false, type: '', label: '申请时间', list: 'totalDisc'}
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [{
          label: '详情',
          key: 0,
          type: 'primary',
          State: false,
          method: (row) => {
            // this.detail(row)
             console.log(row)
          }
        },
        {
          label: '编辑',
          key: 0,
          type: 'success',
          State: false,
          method: (row) => {
            // this.detail(row)
             console.log(row)
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'danger',
          State: false,
          method: (row) => {
            // this.detail(row)
            console.log(row)
          }
        }
        ]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        name: '',
        phone: '',
        userName: '',
        Timeframe: ''
      },
      lastItem: {
        name: '',
        phone: '',
        userName: '',
        Timeframe: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: ''
    }
  },

  mounted () {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      let data = {
        page: 1,
        size: 10
      }
      let res = await getList(data)
      this.tableData = res.data.data.content
      this.total = res.data.data.totalElements
    },

    pageChange (item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.initial()
    },

    // 触发事件
    handleEvent (data) {
      this.fetchData()
    },

  }
}
</script>

<style scoped>
  .app-container{
    display: flex;
    flex-direction: row;
    height: 100%;

  }
  .left{
    border: 1px solid #f6f6ff;
    width: 350px;
    padding: 20px 10px 0 10px;
  }
  .right{
    /* float: left; */
    margin-left: 20px
  }
</style>
