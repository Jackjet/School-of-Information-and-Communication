<template>
  <el-table
    :data="tableData"
    empty-text="暂无数据"
    class="el_tab_alage"
    min-height="400"
    ref="multipleTable"
    @selection-change="handleSelectionChange"
    @select-all="handleSelectAll"
    @select="handleSelect"
  >
    <template v-for="(index, item) in tableLabel">
      <el-table-column
        fit
        :key="item"
        :prop="index.list"
        :label="index.label"
        :type="index.type"
        :width="index.width"
        show-overflow-tooltip
        align="center"
      >
        <template slot-scope="scope">
          <template v-if="scope.row[index.list] === '生效'">
            <span class="blue">{{ scope.row[index.list] }}</span>
          </template>
          <template v-else-if="index.type === 'wj'">
            <a class="blue" @click="down(scope.row)">{{
              scope.row[index.list]
            }}</a>
          </template>
          <template v-else-if="index.type === 'sjmb'">
            <a class="blue" @click="go(scope.row)">{{
              scope.row[index.list]
            }}</a>
          </template>
          <template v-else-if="index.type === 'operation'">
            <el-button
              v-for="(item, index) in scope.row[index.list]"
              size="mini"
              :type="item.type"
              :plain="item.State"
              :key="index"
              @click.native.prevent="
                operationClick(scope.row, scope, item.click)
              "
              >{{ item.label }}
            </el-button>
          </template>
          <template v-else-if="scope.row[index.list] === '失效'">
            <span class="redd">{{ scope.row[index.list] }}</span>
          </template>
          <template v-else-if="scope.row[index.list] === '已上线'">
            <span class="blue">{{ scope.row[index.list] }}</span>
          </template>
          <template v-else-if="scope.row[index.list] === '已下线'">
            <span class="redd">{{ scope.row[index.list] }}</span>
          </template>
          <template v-else>
            <span class="resAndSug">{{ scope.row[index.list] }}</span>
          </template>
        </template>
      </el-table-column>
    </template>
    <el-table-column
      fit
      align="center"
      :width="tableOption.width"
      :label="tableOption.label"
      v-if="tableOption.value === 0"
    >
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <template v-if="scope.row.name !== '超级管理员'">
            <el-button
              size="mini"
              :key="item"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              @click.native.prevent="value.method(scope.row, scope)"
              >{{ value.label }}
            </el-button>
          </template>
        </template>
      </template>
    </el-table-column>
    <el-table-column
      fit
      align="center"
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === 1"
    >
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <template v-if="item === 3 && scope.row.status === '失效'">
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
              >启用
            </el-button>
          </template>
          <template v-else-if="item === 3 && scope.row.status === '生效'">
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
              >禁用
            </el-button>
          </template>
          <template
            v-else-if="
              item === 0 ||
              item === 1 ||
              item === 2 ||
              item === 3 ||
              (item === 4 && scope.row.catalog !== '文件数据源') ||
              (item === 5 && scope.row.catalog !== '文件数据源')
            "
          >
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
              >{{ value.label }}
            </el-button>
          </template>
        </template>
      </template>
    </el-table-column>
    <el-table-column
      fit
      align="center"
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === 2"
    >
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <!-- <template v-if="item === 0 && scope.row.id !== 'admin' ">
              <el-button  size="mini"
            :type="value.type"
            :plain='value.State'
            :icon="value.icon"
            :key='item'
            @click.native.prevent="value.method(scope.row, scope)">{{scope.row.status}}
          </el-button>
          </template> -->
          <template v-if="item === 0 || scope.row.id !== 'admin'">
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
              >{{ value.label }}
            </el-button>
          </template>
        </template>
      </template>
    </el-table-column>
    <el-table-column
      fit
      align="center"
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === 3"
    >
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <template v-if="item === 2">
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
              >{{ scope.row.status === "已上线" ? "禁用" : "启用" }}
            </el-button>
          </template>
          <template
            v-if="
              item === 0 ||
              item === 1 ||
              item === 3 ||
              (item === 4 &&
                scope.row.operationMode === '手动模式' &&
                scope.row.status === '已上线')
            "
          >
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
              >{{ value.label }}
            </el-button>
          </template>
        </template>
      </template>
    </el-table-column>
    <el-table-column
      fit
      align="center"
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === 4"
    >
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <template v-if="item === 2">
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
              >{{ scope.row.status === "生效" ? "禁用" : "启用" }}
            </el-button>
          </template>
          <template
            v-if="
              item === 0 ||
              item === 1 ||
              item === 3 ||
              (item === 4 && scope.row.status !== '无效')
            "
          >
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
              >{{ value.label }}
            </el-button>
          </template>
        </template>
      </template>
    </el-table-column>
    <el-table-column
      fit
      align="center"
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === 5"
    >
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <template
            v-if="item === 0 || (item === 1 && scope.row.status === '处理中')"
          >
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
              >{{ value.label }}
            </el-button>
          </template>
        </template>
      </template>
    </el-table-column>
  </el-table>
</template>
<script>
import { updateDownload } from "@/api/laws";
export default {
  data() {
    return {
      Select: "",
    };
  },

  props: {
    tableData: {
      type: Array,
      default: () => {},
    },
    tableLabel: {
      type: Array,
      default: () => {},
    },
    tableOption: {
      type: Object,
      default: () => {},
    },
    tableStyle: {
      type: Array,
      default: () => {},
    },
  },

  methods: {
    operationClick(event, row, type) {
      this.$emit(type, event);
    },
    nitialization_check(Select) {
      let that = this;
      that.Select = Select;
      that.$refs.multipleTable.toggleRowSelection(Select, true);
    },

    handleSelectionChange(val) {
      this.$emit("onHandleSelectionChange", val);
    },

    handleSelectAll(val, row) {
      this.$emit("onHandleSelectAll", val, row);
    },

    handleSelect(val, row) {
      this.$emit("onHandleSelect", row);
    },
    down(row) {
      let that = this;
      let a_link = document.createElement("a");
      a_link.href =
        process.env.VUE_APP_BASE_API +
        "/webadmin/service/file/downloadRegulationKnowledgeFile/" +
        row.fileName +
        "?type=" +
        row.type;
      a_link.download = row.fileName;
      document.body.appendChild(a_link);
      a_link.click();
      document.body.removeChild(a_link);
      that.putt(row.id);
    },
    go(row) {
      this.$router.push({
        name: "examples",
        query: {
          code: row.templateId,
          type: "rule",
        },
      });
    },
    async putt(id) {
      const data = {
        id: id,
      };
      const that = this;
      try {
        const res = await updateDownload(data);
        if (res.data.code === 1) {
          this.$emit("loadd");
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
  },
};
</script>

<style scoped lang="scss">
.redd {
  color: red;
}
.blue {
  color: blue;
  cursor: pointer;
}
.el_tab_alage {
  width: 98%;
  border: 1px solid #f0f0f0;
  margin: {
    bottom: 20px;
    top: 10px;
  }
  .el-table__row {
    @media screen and (max-width: 1020px) {
      .el-button {
        margin: {
          top: 0px !important;
          left: 10px !important;
          bottom: 0px !important;
          right: 0px !important;
        }
      }
    }
    @media screen and (max-width: 1360px) {
      .el-button + .el-button {
        margin: {
          top: 0px !important;
          bottom: 0px !important;
          right: 0px !important;
        }
      }
    }
  }
}
</style>
<style>
.resAndSug {
  text-align: left;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
}
</style>