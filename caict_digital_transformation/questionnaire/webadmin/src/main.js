import Vue from 'vue'
import App from './App'
import router from './router'
import echarts from 'echarts'
import ElementUI from 'element-ui'
import axios from 'axios'
import cookies from 'js-cookie'
import '@babel/polyfill'

import Interface from './../static/interface/data'
import * as util from './assets/js/common'
import * as http from './assets/js/request'
import store from './store/store'

import './assets/scss/global.scss'
import './assets/iconFont/iconfont.css'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

/**
 * 钩子函数 判断路由是否通过登录页面.
 * @param route 即将进入的路由对象.
 * @param routeFrom 当前导航即将离开的路由.
 * @param routeNext 终止导航.
 */

router.beforeEach((route, routeFrom, routeNext) => {
  if (route.matched.some(record => record.meta.requireAuth)) {
    // 抓取登录状态如果未登录返回登录页面
    if (!util.default.session('currentUser')) {
      routeNext('/login')
    } else {
      routeNext()
    }
  } else {
    routeNext()
  }
})

Vue.prototype.$axios = axios
Vue.prototype.Interface = Interface.constApi
Vue.prototype.common = util.default
Vue.prototype.request = http
Vue.prototype.echarts = echarts
Vue.prototype.cookie = cookies
Vue.config.productionTip = false
Vue.use(ElementUI)

new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
