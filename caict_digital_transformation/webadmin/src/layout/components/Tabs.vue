<!--
 * @Author: your name
 * @Date: 2020-09-21 15:50:04
 * @LastEditTime: 2020-09-27 17:54:58
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\layout\components\TagsView\index.vue
-->
<template>
  <div class="tabs scrollbar">
    <!--closable这里说明home是不能关闭的-->
    <el-tag
      :key="tag.name"
      size="small"
      v-for="(tag, index) in tags"
      :closable="tag.name !== 'home'"
      :disable-transitions="false"
      @close="handleClose(tag, index)"
      @click="changeMenu(tag, index)"
      :effect="$route.name === tag.name ? 'dark' : 'plain'"
      :class="index === _index || _index === 0 && tags.length === 1? 'active': ''"
    >
    {{tag.label}}
    </el-tag>
  </div>
</template>

<script>
import transmit from '@/utils/transmit'
import { mapState, mapMutations } from 'vuex'
export default {
  computed: {
    ...mapState({
      tags: state => state.tab.tabsList
    })
  },
  created () {
    this._index = 0
    transmit.$on('tab', msg => {
      this._index = msg === -1 ? this.tags.length - 1 : msg
    })
  },
  data() {
    return {
      _index: 0,
    }
  },
 
  methods: {
    ...mapMutations({
      close: 'closeTab'
    }),
    //关闭标签
    handleClose(tag, index) {
      let length = this.tags.length - 1
      //vuex调方法的另一种写法
      this.close(tag)
      // 如果关闭的标签不是当前路由的话，就不跳转
      if (tag.name !== this.$route.name) {
        return
      }
      // 关闭的标签是最右边的话，往左边跳转一个
      if (index === length) {
        this.$router.push({ path: this.tags[index - 1].path })
      } else {
        // 否则往右边跳转
        this.$router.push({ path: this.tags[index].path })
      }
    },
    //选择标签跳转路由
    changeMenu(item, index) {
      this._index = index
      this.$router.push({ path: item.path })
      this.$store.commit('selectMenu', item)
    }
  }
}
</script>

<style lang="scss" scoped>
.mobileTab{
  margin-top: 50px;
}
.tabs {
  display: flex;
  align-items: center;
  height: 30px;
  width: 100%;
  overflow-x: scroll;
  position: relative;
  padding-left: 2.5%;
  padding-right: 2.5%;
  // border-bottom: 1px solid #7493FF;
  background: url('../../assets/image/middle.jpg');
  // background-image: linear-gradient(to right, #2575fc 0%, #2575fc 100%);
  background-size: 100% 100%;
  // box-shadow: 0 1px 2px #ffffff50;
  /deep/ .el-tag--small{
    display: flex;
    align-items: center;
    justify-content: center;
    border: none;
    color: #606266;
    cursor: pointer;
  }
  /deep/ .el-tag {
    margin-right: 8px;
    margin-top: 3px;
    cursor: pointer;
    background-color: #fff;
    background-size: 100% 100%;
    box-shadow: 0 1px 4px #dfe9f3;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 30px; 
    line-height: 30px;
    border-radius: 0px;
    font-size: 14px;
    color: #606266;
  }
  .active{
    background-image: linear-gradient(to top, #dfe9f3 0%, white 100%);
    color: #606266;
  }
}
/* IE 浏览器 */
  .scrollbar{
    /*三角箭头的颜色*/
    scrollbar-arrow-color: #7493FF;
    /*滚动条滑块按钮的颜色*/
    scrollbar-face-color: #7493FF;
    /*滚动条整体颜色*/
    scrollbar-highlight-color: #7493FF;
    /*滚动条阴影*/
    scrollbar-shadow-color: #7493FF;
    /*滚动条轨道颜色*/
    scrollbar-track-color: #7493FF;
    /*滚动条3d亮色阴影边框的外观颜色——左边和上边的阴影色*/
    scrollbar-3dlight-color:#7493FF;
    /*滚动条3d暗色阴影边框的外观颜色——右边和下边的阴影色*/
    scrollbar-darkshadow-color: #7493FF;
    /*滚动条基准颜色*/
    scrollbar-base-color: #7493FF;
  }

  /* chrome & safari 浏览器 */
  /*滚动条整体部分,必须要设置*/
  .scrollbar::-webkit-scrollbar{
    width: 0px;
    height: 0px;
    background-color: #7493FF;
  }
  /*滚动条的轨道*/
  .scrollbar::-webkit-scrollbar-track{
    background-color: #7493FF;
  }
  /*滚动条的滑块按钮*/
  .scrollbar::-webkit-scrollbar-thumb{
    border-radius: 5px;
    background-color: #E1E8FF;
    box-shadow: inset 0 0 0px #E1E8FF;
  }
  /*滚动条的上下两端的按钮*/
  .scrollbar::-webkit-scrollbar-button{
    height: 0;
    background-color: #7493FF;
  }

</style>
