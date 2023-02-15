/*
 * @Author: your name
 * @Date: 2020-08-12 16:07:56
 * @LastEditTime: 2020-10-12 00:38:16
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\permission.js
 */

import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import { isEmpty, getPermissonJsonTree } from '@/utils/tool'
import Layout from '@/layout'
import { menuTreeFindall } from '@/api/menu'
import { sessionStorageGet, sessionStorageSet } from '@/utils/storage'
NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()
  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()
  if (!isEmpty(hasToken)) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      if(!isEmpty(sessionStorageGet('state'))){
        next()
      }
      else{
        next()
        // sessionStorageSet('state',1)
        // await store.dispatch('permission/findCurrentUserButton')
        // menuTreeFindall().then(response => {
        //   console.log('routers', router)
        //   console.log('response', response.data.data)
        //   let accessRoutes = getPermissonJsonTree(response.data.data, '', '')
        //   console.log('response', accessRoutes)
        //   router.addRoutes(accessRoutes)
        //   // router.options.routes = accessRoutes
        //   console.log('routers', router)
        //   next({ ...to, replace: true }) 
        //   console.log('routers', router)
        // })

        // console.log('accessRoutess', accessRoutess)
        // router.addRoutes(accessRoutess)
        // console.log('routers', router)
    
        // next()
      }
      // if(store.getters.avatar === 1){
      //   next()
      // }else{
      // let accessRoutes =  [{
      //   path: '/eventmanage',          
      //   component: Layout,
      //   redirect: '/eventmanage/conductView',
      //   name: '',
      //   meta: { title: '办理事项管理', icon: 'icon-shuzhuangtu'},
      //   children: [
      //     {
      //       path: 'conductView',
      //       name: 'conductView',
      //       component: () => import('@/views/eventmanage/conduct/conductView'),
      //       meta: { title: '办理事项管理', icon: 'icon-shuzhuangtu' }
      //     },
            
      //   ]
      // },
      // { path: '*', redirect: '/404', hidden: true },
      // ]
      // const data  = await store.dispatch('user/getInfo')
      // // router.options.routes = accessRoutes;
      // router.addRoutes(accessRoutes)
      // console.log(router)
      // next({ ...to, replace: true }) 
      //   next()
      // }
   
      // const hasGetUserInfo = store.getters.name
      // if (hasGetUserInfo) {
      //   next()
      // } else {
      //   try {
      //     // get user info
      //     let data = await store.dispatch('user/getInfo')
      //      if(data.data.status === '0'){
      //       ElementUI.Message({message:'该组织机构已被禁用',type: 'error'})
      //       await this.$store.dispatch("user/logout")
      //       next(`/login?redirect=${to.path}`)
      //       NProgress.done()
      //      } else {
      //       next()
      //      }
      //   } catch (error) {
      //     // remove token and go to login page to re-login
      //     await store.dispatch('user/resetToken')
      //     // ElementUI.Message({message: error ,type: 'error'})
      //     next(`/login?redirect=${to.path}`)
      //     NProgress.done()
      //   }
      // }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
