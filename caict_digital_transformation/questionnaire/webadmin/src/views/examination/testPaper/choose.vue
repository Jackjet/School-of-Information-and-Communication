<template>
  <el-dialog
    title="选择题目"
    width='80%'
    class="chooseTi"
    :close-on-click-modal='false'
    :show-close='true'
    :before-close="CloseModal"
    :visible.sync="isShow">
    <el-menu
      background-color="#8fb9ef52"
      text-color="#fff"
      active-text-color="#f0f"
      class="el-menu-vertical-demo group-box">
      <div class="menu-title" @click="menuClick('')">所有题目</div>
      <el-form :inline="true" class='menuSeach'>
        <el-form-item style="margin: 0; width: 120px;">
          <el-input
            clearable
            style="margin: 0;"
            size="mini"
            autocomplete="off"
            v-model="SearchMenu.name"
            placeholder="请输入分组名称">
          </el-input>
        </el-form-item>
        <el-form-item style="margin: 0;">
          <el-button
            style="margin: 0;"
            type="primary"
            size='mini'
            @click="findGroup">搜索
          </el-button>
        </el-form-item>
      </el-form>
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
              placeholder="请输入题目名称">
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
        </el-form>
      </el-col>
      <!-- 调用表格 -->
      <el-col :span="24">
        <tableForm ref="tableList" :table-data='tableData'
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
    <div slot='footer' class='dialog-footer'>
      <el-button size='medium'
        @click="CloseModal">取 消
      </el-button>
      <el-button size='medium'
         type='primary'
         @click="Save">确 定
      </el-button>
    </div>
  </el-dialog>
</template>
<script>
import tableForm from '../../integration/tableListNew'
import paging from '../../integration/pagination'

export default {
  components: { tableForm, paging },
  data () {
    return {
      theId: '',
      SearchMenu: {
        name: ''
      },
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
        label: '题目名称',
        list: 'topic'
      },
      {
        id: false,
        type: '',
        label: '题目类型',
        list: 'type'
      }],
      tableOption: {
        label: '操作',
        value: 12313,
        options: []
      },
      Loading: false,
      total: 0,
      pageSize: 20,
      currentPage: 1,
      isShow: false,
      loading: false
    }
  },
  methods: {
    init () {
      this.chushihua()
      this.findGroup()
      this.search()
      this.isShow = true
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
      setTimeout(function () {
        this.$refs.pager.changePage()
        this.$refs.pager.changePageSize(20)
      }, 300)
    },
    Save () {
      const that = this
      if (that.chooseArr.length < 1) {
        that.$message.warning('请选择需要添加的试题')
        return false
      } else {
        let data = []
        that.chooseArr.forEach(item => {
          data.push(item.id)
        })
        that.CloseModal()
        // 重新触发父组件加载loading
        that.$emit('Reload', data)
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
    // 模糊查询
    searchList: function () {
      const that = this
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastSearch[item[0]] = that.SearchItem[item[0]]
      })
      console.log(that.lastSearch)
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
    handleSelectionChange (ele) {
      this.chooseArr.splice(0)
      this.chooseArr = ele
    },
    async findGroup () {
      const that = this
      try {
        const url = that.Interface.courseManage.examination.findGroup
        that.Loading = true
        const response = await this.request.dataGet(that, url, that.SearchMenu)
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
    CloseModal () {
      this.SearchMenu.name = ''
      this.SearchItem.examinationGroupId = ''
      this.SearchItem.topic = ''
      this.SearchItem.type = ''
      this.total = 0
      this.pageSize = 20
      this.currentPage = 1
      this.chooseArr.splice(0)
      this.navMenus.splice(0)
      this.tableData.splice(0)
      this.$refs.tableList.initRadio()
      this.$refs.pager.changePage()
      this.$refs.pager.changePageSize(20)
      this.isShow = false
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
  .chooseTi {
    .menuSeach {
      width: 300px;
      .el-input__suffix-inner {
        height: 100%;
      }
    }
    .el-dialog {
      max-height: 80%;
      overflow-y: auto;

      .ti-box {
        margin: 5px 0;
        display: inline-block;
        width: calc(100% - 300px);
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
  }
</style>
