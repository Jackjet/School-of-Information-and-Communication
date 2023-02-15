<template>
  <!-- 主列表 -->
  <el-table
    :max-height="maxHeight"
    :data='tableData'
    @expand-change="expandSelect"
    :expand-row-keys="expands"
    empty-text='暂无数据'
    class='el_tab_alage'
    :row-key='getRowKeys'
    @selection-change="handleSelectionChange">
    <!-- 单选框 -->
    <el-table-column align="center" width="50" label="" v-if="tableSelection.key === true && tableSelection.type === 'radio'">
      <template slot-scope="scope">
        <el-radio :label="scope.$index" v-model="radio" @change="handleTemplateRow(scope.$index, scope.row)">&nbsp;</el-radio>
      </template>
    </el-table-column>
    <!-- index索引 -->
    <el-table-column label="序号" type="index" width="50" align="center" v-if="tableSelection.key === true && tableSelection.type === 'index'"></el-table-column>
    <!-- 多选框 -->
    <el-table-column type="selection" width="50" align="center" v-if="tableSelection.key === true && tableSelection.type === 'selection'"></el-table-column>
    <!-- 列表表头-->
    <el-table-column type="expand" v-if="tableSelection.detaile === true">
      <template slot-scope="scope">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item :label="index.label"  v-for="(index, item) in tableLabel" :key='item' v-show="index.type === 'expand'">
            <span>{{scope.row[index.list]}}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>
    <template v-for="(index, item) in tableLabel">
      <el-table-column fit
        :align="index.align?index.align:'center'"
        :key='item'
        :sortable='index.sort'
        v-if="index.type !== 'expand'"
        :label="index.label"
        :width="index.width"
        :show-overflow-tooltip="true"
        :prop="index.list">
         <template slot-scope="scope">
          <!-- 图文 -->
          <template v-if="index.type === 'imgTxt'">
            <div  style="display: flex;align-items: center;width: 100%">
              <el-image
                v-if="scope.row[index.covor]"
                style="width: 100px; height: 50px;"
                :src="scope.row[index.covor]">
                <div slot="error" class="image-slot">
                  <el-image  :lazy="true"  fit="fill"  :src="require('@/assets/image/errorPic.jpg')">
                  </el-image>
                </div>
              </el-image>
              <p style="width:100%;margin-left:10px;text-align:left;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                {{scope.row[index.list]}}
              </p>
            </div>
          </template>
           <!-- 图片 -->
           <template v-else-if="index.type === 'image'">
             <el-image
               v-if="scope.row[index.list] !== ''"
               style="width: 100px; height: 50px;"
               :src="scope.row[index.list]">
               <div slot="error" class="image-slot">
                 <el-image  :lazy="true"  fit="fill"  :src="require('@/assets/image/errorPic.jpg')">
                 </el-image>
               </div>
             </el-image>
             <div v-else></div>
           </template>
          <!-- 头像 -->
          <template v-else-if="index.type === 'head'">
            <el-image
              v-if="!(scope.row[index.list] === '' || scope.row[index.list] === null)"
              style="width: 50px; height: 50px;"
              :src="scope.row[index.list]">
              <div slot="error" class="image-slot">
                <el-image  :lazy="true"  fit="fill"  :src="require('@/assets/image/errorPic.jpg')">
                </el-image>
              </div>
            </el-image>
            <div v-else></div>
          </template>
          <!-- 按钮 -->
          <template v-else-if="index.type === 'btn'">
            <el-button type="text"
              @click.native.prevent="index.method(scope.row, scope)">
              <u>{{scope.row[index.list]}}</u>
            </el-button>
          </template>
          <!-- 下拉 -->
          <template v-else-if="index.type === 'select'">
            <el-select v-model="scope.row[index.list]"
             @change="changeType($event, scope.row, item)"
             size="medium">
              <el-option
                v-for="item in index.options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </template>
          <!-- 正常显示 -->
          <template v-else>
            <div class="html-box" v-html="scope.row[index.list]"></div>
          </template>
        </template>
      </el-table-column>
    </template>
    <!-- 正常按钮操作 -->
    <el-table-column fit align='center'
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === 0">
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <template v-if="value.link">
            <el-button
              v-if="value.label[scope.row[value.link]]"
              :key='item'
              :type="value.type"
              :plain='value.State'
              :icon="value.icon"
              @click.native.prevent="value.method(scope.row, scope)">{{value.label[scope.row[value.link]]}}
            </el-button>
          </template>
          <template v-else>
            <el-button
              :key='item'
              :type="value.type"
              :plain='value.State'
              :icon="value.icon"
              @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
            </el-button>
          </template>
        </template>
      </template>
    </el-table-column>
    <!-- 通过状态显示按钮操作 分开设置 -->
    <!-- 轮播模块列表 -->
    <el-table-column fit align='center'
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === '轮播列表'">
      <template slot-scope="scope">
        <el-button
          v-for="(value, item) in tableOption.options"
          :type="value.type"
          :plain='value.State'
          :icon="value.icon"
          :key='item'
          v-if="!(item === 3 && scope.row.serialNumber === 1)"
          @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
        </el-button>
      </template>
    </el-table-column>
    <!-- 每日阅读 发布中 加精操作 -->
    <el-table-column fit align='center'
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === '每日阅读发布'">
      <template slot-scope="scope">
        <el-button
          v-for="(value, item) in tableOption.options"
          :type="value.type"
          :plain='value.State'
          :icon="value.icon"
          :key='item'
          v-if="!((item === 3 && scope.row.isGreat === '0') || (item === 2 && scope.row.isGreat === '1'))"
          @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
        </el-button>
      </template>
    </el-table-column>
    <!-- 通知管理 管理页面 推送操作 -->
    <el-table-column fit align='center'
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === '通知管理'">
      <template slot-scope="scope">
        <el-button
          v-for="(value, item) in tableOption.options"
          :type="value.type"
          :plain='value.State'
          :icon="value.icon"
          :key='item'
          v-if="!(item === 2 && scope.row.isPush === '1')"
          @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
        </el-button>
      </template>
    </el-table-column>
    <!-- 每日阅读 评论管理 下架 -->
    <el-table-column fit align='center'
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === '评论管理'">
      <template slot-scope="scope">
        <el-button
          v-for="(value, item) in tableOption.options"
          :type="value.type"
          :plain='value.State'
          :icon="value.icon"
          :key='item'
          v-if="scope.row.obtained === '1'"
          @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  data () {
    return {
      radio: '',
      qiNiuHttp: this.Interface.QiNiuHttp,
      expands: [],
      getRowKeys (row) {
        return row.id
      }
    }
  },

  props: {
    tableData: {
      type: Array,
      default: () => {}
    },
    tableSelection: {
      type: Object,
      default: () => {}
    },
    tableLabel: {
      type: Array,
      default: () => {}
    },
    tableOption: {
      type: Object,
      default: () => {}
    },
    maxHeight: {
      type: String,
      default: () => {}
    }
  },

  methods: {
    handleSelectionChange (val) {
      this.$emit('onHandleSelectionChange', val)
    },

    handleTemplateRow (index, row) {
      this.$emit('onHandleTemplateRow', row)
    },
    initRadio () {
      this.radio = ''
    },
    changeType (event, row) {
      this.$emit('onChangeType', event, row)
    },

    expandSelect (row, expandedRows) {
      const that = this
      if (expandedRows.length) {
        that.expands = []
        if (row) {
          that.expands.push(row.id)
        }
      } else {
        that.expands = []
      }
    }
  }
}
</script>

<style lang="scss">
  .html-box {
    position: relative;
    max-height: 45px;
    img {
      display: none;
    }
  }
  .demo-table-expand {
    font-size: 0;
  }

  .el-table thead th {
    background-color: #f9f9f9;
  }

  .demo-table-expand label {
    width: 90px !important;
    color: #99a9bf !important;
  }

  .demo-table-expand .el-form-item {
    margin-right: 0 !important;
    margin-bottom: 0 !important;
    width: 50%;
  }

  .el-table--scrollable-x .el-table__body-wrapper::-webkit-scrollbar {
    overflow-x: hidden;
  }

  .el-table .el-table__body-wrapper::-webkit-scrollbar-thumb {
    background-color: rgba(169, 178, 196, 0.3) !important;
  }

  .el-table__fixed-right {
    height: calc(100% - 27px) !important;
  }

  .el_tab_alage {
    width: 99.5% !important;
    border:1px solid #f0f0f0 !important;
    border-bottom: none !important;
    margin: {
      left: 2px;
      bottom: 20px !important;
      top: 10px !important;
    }

    .el-table__row {
      .cell {
        -webkit-line-clamp: 3 !important;
        -webkit-box-orient: vertical !important;
      }
      .el-button {
          margin: {
          top: 0 !important;
          right: 10px !important;
          bottom:0 !important;
          left: 0 !important;
        }
      }
    }
  }
</style>
