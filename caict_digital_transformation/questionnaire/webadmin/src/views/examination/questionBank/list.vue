<template>
  <el-main class="ti-main" v-loading="Loading">
    <el-menu
      background-color="#8fb9ef52"
      text-color="#fff"
      active-text-color="#f0f"
      class="el-menu-vertical-demo group-box">
      <div class="menu-title" @click="menuClick('')">所有题目</div>
      <template v-for="navMenu in navMenus">
        <el-menu-item
        :key="navMenu.id"
        :index="navMenu.id"
        :class="isAll"
         @click.native="menuClick(navMenu.id)">
          <span :title="navMenu.name" slot="title">{{navMenu.name}}</span>
          <i>{{navMenu.questionNumber}}</i>
        </el-menu-item>
      </template>
    </el-menu>
    <div class="ti-box">
      <el-col :span="24">
        <el-form :inline="true" class='el-InputForm'>
          <el-form-item>
            <el-select
              clearable
              size="medium"
              placeholder="请选择题目类型"
              v-model="SearchItem.type">
              <el-option label="单选题" value="0"></el-option>
              <el-option label="多选题" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input
              clearable
              size="medium"
              autocomplete="off"
              v-model="SearchItem.topic"
              placeholder="请输入试题名称">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              size='medium'
              icon="el-icon-search"
              @click="searchList">搜索
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"
              size='medium'
              icon="el-icon-plus"
              @click="add">添加
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <!-- 批量删除 -->
      <el-col :span='24' class='delete'>
        <el-button type="primary" plain size='mini' @click="batchDelete">批量删除</el-button>
        <el-button type="primary" plain size='mini' @click="batchMove">批量移动</el-button>
      </el-col>
      <!-- 调用表格 -->
      <el-col :span="24">
        <tableForm :table-data='tableData'
                   @onHandleSelectionChange="handleSelectionChange"
                   :table-selection="tableSelection"
                   :table-label="tableHeader"
                   :table-option="tableOption">
        </tableForm>
      </el-col>
      <!-- 调用分页 -->
      <el-col :span='24'>
        <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
      </el-col>
    </div>
    <!-- 调用操作弹层 -->
    <theList
      ref="callList"
      :nav-menus='navMenus'
      @Reload='init'>
    </theList>
    <!-- 调用操作弹层 -->
    <theDetail ref="callDetail"></theDetail>
  </el-main>
</template>
<script>
import tableForm from '../../integration/tableListNew'
import paging from '../../integration/pagination'
import theList from './groupList'
import theDetail from './detail'
export default {
  components: { tableForm, paging, theList, theDetail },
  data () {
    return {
      SearchItem: {
        examinationGroupId: '',
        topic: '',
        type: ''
      },
      lastSearch: {
        examinationGroupId: '',
        topic: '',
        type: ''
      },
      isAll: '',
      chooseArr: [],
      navMenus: [],
      tableData: [],
      tableSelection: {
        key: true,
        type: 'selection'
      },
      tableHeader: [{
        id: false,
        type: '',
        label: '试题名称',
        list: 'topic'
      },
      {
        id: false,
        type: '',
        label: '题目类型',
        list: 'type'
      },
      {
        id: false,
        type: '',
        label: '最后更新',
        list: 'updateTime'
      }],
      tableOption: {
        label: '操作',
        value: 0,
        options: [{
          label: '移动',
          key: 0,
          type: 'text',
          icon: 'el-icon-sort',
          State: false,
          method: (row) => {
            this.doMove(row)
          }
        },
        {
          label: '编辑',
          key: 0,
          type: 'text',
          icon: 'el-icon-edit',
          State: false,
          method: (row) => {
            this.edit(row)
          }
        },
        {
          label: '预览',
          key: 0,
          type: 'text',
          icon: 'el-icon-view',
          State: false,
          method: (row) => {
            this.toSee(row)
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'text',
          icon: 'el-icon-delete',
          State: false,
          method: (row) => {
            this.doDetele(row)
          }
        }]
      },
      Loading: false,
      total: 0,
      pageSize: 20,
      currentPage: 1
    }
  },

  methods: {
    init () {
      this.chushihua()
      this.findGroup()
      this.searchList()
    },
    chushihua () {
      this.SearchItem.examinationGroupId = ''
      this.SearchItem.topic = ''
      this.SearchItem.type = ''
      this.lastSearch.examinationGroupId = ''
      this.lastSearch.topic = ''
      this.lastSearch.type = ''
      this.chooseArr.splice(0)
      this.navMenus.splice(0)
      this.tableData.splice(0)
      this.total = 0
      this.currentPage = 1
      this.pageSize = 20
      this.$refs.pager.changePage()
      this.$refs.pager.changePageSize(20)
    },
    // 模糊查询
    searchList: function () {
      const that = this
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastSearch[item[0]] = that.SearchItem[item[0]]
      })
      that.currentPage = 1
      this.$refs.pager.changePage()
      this.search()
    },
    async search () {
      const that = this
      try {
        let findUrl = that.Interface.courseManage.question.find
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        Object.entries(that.SearchItem).map((item, index) => {
          that.SearchItem[item[0]] = that.lastSearch[item[0]]
        })
        that.Loading = true
        const response = await this.request.dataGet(that, url, that.SearchItem)
        that.Loading = false
        if (response.data.code !== 0) {
          let Data = response.data.data
          Data.content.forEach(item => {
            if (item.type === '0') {
              item.type = '单选题'
            } else {
              item.type = '多选题'
            }
          })
          that.tableData = Data.content
          that.total = Data.totalElements
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    menuClick (id) {
      if (id === '') {
        this.isAll = 'bgc'
      } else {
        this.isAll = ''
      }
      this.SearchItem.examinationGroupId = id
      this.SearchItem.topic = ''
      this.SearchItem.type = ''
      this.searchList()
    },
    async findGroup () {
      const that = this
      try {
        const url = that.Interface.courseManage.questiongroup.find
        that.Loading = true
        const response = await this.request.dataGet(that, url, {})
        that.Loading = false
        if (response.data.code !== 0) {
          that.navMenus = response.data.data
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    add () {
      const that = this
      that.$router.push({
        path: 'questionAdd',
        query: {
          id: '',
          method: 'add'
        }
      })
    },
    edit (row) {
      const that = this
      that.$router.push({
        path: 'questionEdit',
        query: {
          id: row.id,
          method: 'edit'
        }
      })
    },
    toSee (row) {
      this.$refs.callDetail.init(row.id)
    },
    batchDelete () {
      const that = this
      if (that.chooseArr.length < 1) {
        that.$message.warning('请先选择需要删除的选项')
      } else {
        let data = []
        that.chooseArr.forEach(element => {
          data.push(element.id)
        })
        that.Detele(data)
      }
    },
    doMove (row) {
      this.Move([row.id])
    },
    batchMove () {
      if (this.chooseArr.length < 1) {
        this.$message.warning('请先选择需要移动的选项')
      } else {
        let data = []
        this.chooseArr.forEach(element => {
          data.push(element.id)
        })
        this.Move(data)
      }
    },
    Move (data) {
      this.$refs.callList.init(data)
    },
    handleSelectionChange (ele) {
      this.chooseArr.splice(0)
      this.chooseArr = ele
    },
    doDetele (row) {
      this.Detele([row.id])
    },
    Detele (data) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = that.Interface.courseManage.question.delete
        that.Loading = true
        const response = await this.request.dataPost(that, url, data)
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          that.search()
        } else {
          that.$message.error(response.data.data)
        }
      }).catch(() => {
        return false
      })
    },
    // 获取搜索当前分页 页码 条数
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.search()
    }
  }
}
</script>

<style lang="scss">
  .ti-main {
    display: table;

    .ti-box {
      margin: 5px 0;
      display: inline-block;
      width: calc(100% - 200px);
      margin-left: 20px;
      vertical-align: top;
    }

    .group-box {
      margin: 5px 0;
      display: inline-block;
      width: 180px;
      text-align: left;
      vertical-align: top;

      .menu-title {
        font-size: 20px;
        padding: 10px;
        background-color: #93b6e2;
        color: #5d5d5d;
        cursor: pointer;
      }

      .el-menu-item:hover {
        background-color: #8fb9efa1 !important;
      }
      .is-active {
        background-color: #8fb9efa1 !important;
      }

      .bgc {
        background-color: rgba(143, 185, 239, 0.32) !important;
      }

      span {
        display: inline-block;
        max-width: 100px;
        overflow: hidden;
      }
      i {
        float: right;
      }
    }
  }
</style>
