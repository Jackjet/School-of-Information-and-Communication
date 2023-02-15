import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '首页',
      component: () => import('@/views/dashboard/home'),
      meta: {title: '首页', icon: 'dashboard'}
    }]
  },
  {
    path: '/dataSource',
    component: Layout,
    redirect: '/dataSource/index',
    name: '',
    meta: {title: '数据源管理', icon: ''},
    children: [
      {
        path: 'index',
        name: 'index',
        component: () => import('@/views/dataSource/index'),
        meta: {title: '数据源管理', icon: ''}
      },
      {
        path: 'choose',
        name: 'choose',
        component: () => import('@/views/dataSource/choose'),
        meta: {title: '数据源分类', icon: ''}
      },
      {
        path: 'source',
        name: 'source',
        component: () => import('@/views/dataSource/source'),
        meta: {title: '元数据字段', icon: ''}
      },
      {
        path: 'sourceData',
        name: 'sourceData',
        component: () => import('@/views/dataSource/sourceData'),
        meta: {title: '元数据表', icon: ''}
      },
      {
        path: 'queryTableData',
        name: 'queryTableData',
        component: () => import('@/views/dataSource/queryTableData'),
        meta: {title: '表数据', icon: ''}
      },
    ]
  },
  {
    path: '/taskManage',
    component: Layout,
    redirect: '/taskManage/index',
    name: '',
    meta: {title: '接入任务管理', icon: ''},
    children: [

      {
        path: 'index',
        name: 'index',
        component: () => import('@/views/task/taskManage/taskManage'),
        meta: {title: '任务组管理', icon: ''}
      },
      {
        path: 'manag',
        name: 'manag',
        component: () => import('@/views/task/manag/manag'),
        meta: {title: '任务管理', icon: ''}
      },
      {
        path: 'executionlog',
        name: 'executionlog',
        component: () => import('@/views/task/executionlog/executionlog'),
        meta: {title: '任务执行日志', icon: ''}
      },
      {
        path: 'grouplog',
        name: 'grouplog',
        component: () => import('@/views/task/grouplog/grouplog'),
        meta: {title: '任务组执行日志', icon: ''}
      },


    ]
  },
  {
    path: '/rule',
    component: Layout,
    redirect: '/rule/manage',
    name: '',
    meta: {title: '质量检查规则', icon: 'example'},
    children: [
      {
        path: 'manage',
        name: 'manage',
        component: () => import('@/views/rule/manage/index.vue'),
        meta: {title: '规则管理', icon: 'dataModel'}
      },
      {
        path: 'update',
        name: 'update',
        component: () => import('@/views/rule/manage/update.vue'),
        meta: {title: '变更历史', icon: 'dataModel'}
      },
      {
        path: 'log',
        name: 'log',
        component: () => import('@/views/rule/log/log.vue'),
        meta: {title: '规则日志', icon: 'dataModel'}
      },
      {
        path: 'my',
        name: 'my',
        component: () => import('@/views/rule/my/my.vue'),
        meta: {title: '我订阅的检查规则', icon: ''}
      },
    ]
  },
  {
    path: '/dataBase',
    component: Layout,
    redirect: '/dataBase/examples',
    name: '',
    meta: {title: '数据模板', icon: 'example'},
    children: [
      {
        path: 'examples',
        name: 'examples',
        component: () => import('@/views/dataBase/examples/examples'),
        meta: {title: '模板管理', icon: 'dataModel'}
      },
    ]
  },
  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
        meta: {title: 'External Link', icon: 'link'}
      }
    ]
  },

  // 404 page must be placed at the end !!!
  // { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
