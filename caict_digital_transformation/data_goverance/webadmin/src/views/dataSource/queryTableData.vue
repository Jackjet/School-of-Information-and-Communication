<template>
  <el-main>
    <el-row>
      <el-button type="primary" size="medium" @click="reTu" icon="el-icon-add"
        >返回</el-button
      >
    </el-row>
    <TableList
      :table-data="tableData"
      v-loading="isSubmitLoading"
      :table-label="tableHeader"
      :table-option="tableOpction"
      @loadd="submitLoadingg"
    >
    </TableList>
    <el-col :span="24">
      <pagination
        ref="page"
        :total="total"
        @pageChange="pageChange"
      ></pagination>
    </el-col>
    <drawer :modal-objj="modalObjj" ref="callDetaill" @Reload="submitLoadingg">
    </drawer>
  </el-main>
</template>

<script>
import { metaDataTableFindById, findAllTableData } from "@/api/dataSource";
import TableList from "@/components/table/TableList.vue";
import Pagination from "@/components/table/Pagination.vue";
import drawer from "./add";
import { getToken } from "@/utils/auth";

export default {
  components: { TableList, Pagination, drawer },

  data() {
    return {
      drawer: false,
      tableData: [],
      tableHeader: [],
      tableOpction: {},
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        name: "",
        type: "",
      },
      lastItem: {
        name: "",
        type: "",
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
      RootUrl: "",
      modalObjj: "",
    };
  },
  created() {
    this.fetchHeaderData();
    this.fetchData();
  },
  methods: {
    async fetchHeaderData() {
      const data = {
        id: this.$route.query.id,
      };
      try {
        const res = await metaDataTableFindById(data);
        if (JSON.parse(res.data.data).length > 0) {
          let dataSource = JSON.parse(res.data.data);
          for (let i = 0; i < dataSource.length; i++) {
            let tableHeaderObj = {};
            tableHeaderObj.id = false;
            tableHeaderObj.type = "";
            tableHeaderObj.label = dataSource[i].name;
            tableHeaderObj.list = dataSource[i].name;
            this.tableHeader.push(tableHeaderObj);
          }
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    async fetchData() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        id: this.$route.query.id,
      };
      try {
        const res = await findAllTableData(data);
        let tableData = res.data.data.content;
        this.tableData = tableData;
        this.total = res.data.data.totalElements;
      } catch (even) {
        this.$message.error(even.msg);
      }
    },

    reTu: function (value) {
      this.$router.push({
        name: "sourceData",
        query: { id: this.$route.query.dataSourceId },
      });
    },
    submitLoadingg() {
      this.fetchData();
    },
    pageChange(item) {
      this.pageSize = item.limit;
      this.currentPage = item.page;
      this.fetchData();
    },
  },
};
</script>
