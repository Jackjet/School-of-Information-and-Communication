<template>
  <el-tree
    class="filter-tree"
    :data="data"
    :props="defaultProps"
    default-expand-all
    :filter-node-method="filterNode"
    ref="tree" @node-click="nodeClick"
    style="box-shadow: 4px 4px 3px #EEEEEE;width:400px;height: 100vh;">
  </el-tree>
</template>

<script>

export default {
  components: {},
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },

  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    nodeClick(node){
      let isList = false;

      if(node.isPcList === '1' || node.isAppList === '1') {
        isList = true
      }

      this.$emit('nodeClick',node.columnId,node.templateId,isList,node.pcListUrl,node.pcDocumentUrl,node.appListUrl,node.appDocumentUrl)
    }
  },

  props:{
    data: Array
  },

  data() {
    return {
      filterText: '',
      data: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    };
  }
};
</script>
