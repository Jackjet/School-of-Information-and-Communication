<template>
  <el-main class="lunbo-main">
    <el-tabs clearable v-model="activeName" type="border-card" @tab-click="handleClick">
      <el-tab-pane label="试卷管理" name="first">
        <testpaper ref="first"></testpaper>
      </el-tab-pane>
      <el-tab-pane label="题库" name="second">
        <bank ref="second"></bank>
      </el-tab-pane>
      <el-tab-pane label="题目分组" name="third">
        <group ref="third"></group>
      </el-tab-pane>
    </el-tabs>
  </el-main>
</template>

<script>
import testpaper from './testPaper/list.vue'
import bank from './questionBank/list'
import group from './questionGroup/list'
export default {
  components: { testpaper, bank, group },
  data () {
    return {
      activeName: 'first'
    }
  },
  mounted () {
    if (this.$route.query.numb) {
      this.activeName = this.$route.query.numb
      this.$refs[this.$route.query.numb].init()
    } else {
      this.activeName = 'first'
      this.$refs.first.init()
    }
  },
  methods: {
    handleClick (tab) {
      this.$refs[tab.name].init()
    }
  }
}
</script>

<style lang="scss">
  .lunbo-main {
    position: relative;

    .lunbo-btn {
      position: absolute;
      top: 0;
      left: 20px;
    }
  }
</style>
