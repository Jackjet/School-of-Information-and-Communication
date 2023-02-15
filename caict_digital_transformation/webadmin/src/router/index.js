/*
 * @Author: your name
 * @Date: 2020-09-03 13:58:48
 * @LastEditTime: 2020-09-21 23:38:58
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \web\src\router\index.js
 */
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
      component: () => import('@/views/home/index'),
      meta: { title: '首页', icon: 'icon-shouye' }
    }]
  },

  // 系统设置
  {
    path: '/sysSet',
    component: Layout,
    redirect: '/sysSet/system',
    meta: { title: '系统设置', icon: '' },
    children: [{
      path: 'dataBackup',
      name: '数据备份',
      component: () => import('@/views/systemSet/dataBackup'),
      meta: { title: '数据备份', icon: 'icon-shouye' }
    }, {
      path: 'system',
      name: '系统设置',
      component: () => import('@/views/systemSet/system'),
      meta: { title: '系统设置', icon: 'icon-shouye' }
    }, {
      path: 'service',
      name: '服务器信息',
      component: () => import('@/views/systemSet/service'),
      meta: { title: '服务器信息', icon: 'icon-shouye' }
    }]
  },
  // 日志
  {
    path: '/logManage',
    component: Layout,
    redirect: '/logManage/operationLog',
    meta: { title: '日志管理', icon: '' },
    children: [{
      path: 'userLog',
      name: 'userLog',
      component: () => import('@/views/logManage/index'),
      redirect: '/logManage/userLog/userLogs',
      meta: { title: '用户日志', icon: 'icon-shouye' },
      children: [{
        path: 'userLogs',
        name: '列表',
        component: () => import('@/views/logManage/userLog/index'),
        meta: { title: '列表', icon: 'icon-shouye' },
      }, {
        path: 'userLogAdd',
        name: '详情',
        component: () => import('@/views/logManage/userLog/add'),
        meta: { title: '详情', icon: 'icon-shouye' }
      }]
    }, {
      path: 'operationLog',
      name: 'operationLog',
      component: () => import('@/views/logManage/index'),
      meta: { title: '操作日志', icon: 'icon-shouye' },
      redirect: '/logManage/operationLog/operationLogs',
      children: [{
        path: 'operationLogs',
        name: '列表',
        component: () => import('@/views/logManage/operationLog/index'),
        meta: { title: '列表', icon: 'icon-shouye' }
      }, {
        path: 'operationLogAdd',
        name: '详情',
        component: () => import('@/views/logManage/operationLog/add'),
        meta: { title: '详情', icon: 'icon-shouye' }
      }]
    }]
  },
  // 系统管理
  {
    path: '/systemManage',
    component: Layout,
    redirect: '/systemManage/organization',
    meta: { title: '系统管理', icon: '' },
    children: [{
      path: 'organization',
      name: '组织机构',
      component: () => import('@/views/systemManage/index'),
      redirect: '/systemManage/organization/organizations',
      meta: { title: '组织机构', icon: 'icon-shouye' },
      children: [{
        path: 'organizations',
        name: '列表',
        component: () => import('@/views/systemManage/organization/index'),
        meta: { title: '列表', icon: 'icon-shouye' }
      }, {
        path: 'organizationAdd',
        name: '新增',
        component: () => import('@/views/systemManage/organization/add'),
        meta: { title: '新增', icon: 'icon-shouye' }
      }]
    }, {
      path: 'user',
      name: 'user',
      component: () => import('@/views/systemManage/index'),
      redirect: '/systemManage/user/users',
      meta: { title: '用户管理', icon: 'icon-shouye' },
      children: [{
        path: 'users',
        name: '列表',
        component: () => import('@/views/systemManage/user/index'),
        meta: { title: '列表', icon: 'icon-shouye' }
      }, {
        path: 'userAdd',
        name: '新增',
        component: () => import('@/views/systemManage/user/add'),
        meta: { title: '新增', icon: 'icon-shouye' }
      }]
    }, {
      path: 'menu',
      name: 'menu',
      component: () => import('@/views/systemManage/index'),
      redirect: '/systemManage/menu/menus',
      meta: { title: '菜单管理', icon: 'icon-shouye' },
      children: [{
        path: 'menus',
        name: '列表',
        component: () => import('@/views/systemManage/menu/index'),
        meta: { title: '列表', icon: 'icon-shouye' },
      }, {
        path: 'addChild',
        name: '子菜单',
        component: () => import('@/views/systemManage/menu/addChild'),
        meta: { title: '子菜单', icon: 'icon-shouye' }
      }, {
        path: 'addParent',
        name: '根菜单',
        component: () => import('@/views/systemManage/menu/addParent'),
        meta: { title: '根菜单', icon: 'icon-shouye' }
      }]
    }, {
      path: 'role',
      name: 'role',
      component: () => import('@/views/systemManage/role/index'),
      meta: { title: '角色权限', icon: 'icon-shouye' },
      redirect: '/systemManage/role/roles',
      children: [{
        path: 'roles',
        name: '列表',
        component: () => import('@/views/systemManage/role/roles/index'),
        meta: { title: '列表', icon: 'icon-shouye' }
      }, {
        path: 'users',
        name: '用户',
        component: () => import('@/views/systemManage/role/roles/users'),
        meta: { title: '用户列表', icon: 'icon-shouye' }
      }, {
        path: 'assignPremission',
        name: '权限分配',
        component: () => import('@/views/systemManage/role/roles/assignPremission'),
        meta: { title: '权限分配', icon: 'icon-shouye' }
      }, {
        path: 'roleAdd',
        name: '新增',
        component: () => import('@/views/systemManage/role/roles/add'),
        meta: { title: '新增', icon: 'icon-shouye' }
      }]
    }]
  },

  // 客户管理
  {
    path: '/customer',
    component: Layout,
    redirect: '/customer/list',
    meta: { title: '客户管理', icon: '' },
    children: [{
      path: 'list',
      name: '客户列表',
      component: () => import('@/views/customer/index'),
      meta: { title: '', icon: 'icon-shouye' }
    }, {
      path: 'info',
      name: '客户详情',
      component: () => import('@/views/customer/info'),
      meta: { title: '', icon: 'icon-shouye' }
    }]
  },

  // 活动管理
  {
    path: '/activities',
    component: Layout,
    redirect: '/activities/list',
    meta: { title: '活动管理', icon: '' },
    children: [{
      path: 'list',
      name: '活动列表',
      component: () => import('@/views/activities/index'),
      meta: { title: '', icon: 'icon-shouye' }
    }, {
      path: 'info',
      name: '活动详情',
      component: () => import('@/views/activities/info'),
      meta: { title: '', icon: 'icon-shouye' }
    }, {
      path: 'add',
      name: '添加活动',
      component: () => import('@/views/activities/add'),
      meta: { title: '', icon: 'icon-shouye' }
    }]
  },
  // 统计分析
  {
    path: '/statistics',
    component: Layout,
    redirect: '/statistics/echarts',
    children: [{
      path: 'echarts',
      name: '统计分析',
      component: () => import('@/views/statistics/index'),
      meta: { title: '统计分析', icon: 'icon-shouye' }
    }]
  },
  // 行业管理
  {
    path: '/industries',
    component: Layout,
    redirect: '/industries/list',
    meta: { title: '行业管理', icon: '' },
    children: [{
      path: 'list',
      name: '行业列表',
      component: () => import('@/views/industries/index'),
      meta: { title: '', icon: 'icon-shouye' }
    }, {
      path: 'add',
      name: '行业活动',
      component: () => import('@/views/industries/add'),
      meta: { title: '', icon: 'icon-shouye' }
    }]
  },
  // 消息
  {
    path: '/messages',
    component: Layout,
    redirect: '/messages/list',
    meta: { title: '消息管理', icon: '' },
    children: [{
      path: 'list',
      name: '消息列表',
      component: () => import('@/views/messages/index'),
      meta: { title: '', icon: 'icon-shouye' }
    }, {
      path: 'add',
      name: '消息添加',
      component: () => import('@/views/messages/add'),
      meta: { title: '', icon: 'icon-shouye' }
    }, {
      path: 'info',
      name: '消息详情',
      component: () => import('@/views/messages/info'),
      meta: { title: '', icon: 'icon-shouye' }
    }]
  },
];

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
