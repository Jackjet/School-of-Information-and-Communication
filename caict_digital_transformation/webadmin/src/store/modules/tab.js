/*
 * @Author: your name
 * @Date: 2020-09-21 18:10:02
 * @LastEditTime: 2020-09-26 23:25:32
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\store\modules\tab.js
 */
import transmit from '@/utils/transmit'
export default {

  //存储数据
  state: {
    isCollapse: false,
    currentMenu: null,
    tabsList: [
      {
        path: '/',
        name: 'home',
        label: '首页',
        title: '首页',
        icon: 'home'
      }
    ]
  },
  //调用方法
  mutations: {
    collapseMenu(state) {
      state.isCollapse = !state.isCollapse
    },
    //选择标签 选择面包屑
    selectMenu(state, val) {
      if (val.name === 'home') {
        state.currentMenu = null
      } else {
        let object = {}
        object.icon =
        object.path = val.index
        object.label = val.title
        object.title = val.title
        object.name = val.title
        state.currentMenu = object
        //如果等于-1说明tabsList不存在那么插入，否则什么都不做
        let result = state.tabsList.findIndex(item => item.title === val.title)
        result === -1 ? state.tabsList.push(object) : ''
        transmit.$emit('tab', result)
      }
      // val.name === 'home' ? (state.currentMenu = null) : (state.currentMenu = val)
    },
    //关闭标签
    closeTab(state, val) {
      let result = state.tabsList.findIndex(item => item.name === val.name)
      state.tabsList.splice(result, 1)
    },
  },
  actions: {}
}
