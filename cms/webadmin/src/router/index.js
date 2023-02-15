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
      path: '/dashboard',
      name: '首页',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },
  {
    path: '/template',
    component: Layout,
    redirect: '/template',
    children: [{
      path: '/template/templateFile',
      name: '模板文件',
      component: () => import('@/views/template/templateFile'),
      meta: { title: '模板文件', icon: 'templateFile' }
    },
    {
      path: '/template/template',
      name: '模板管理',
      component: () => import('@/views/template/template'),
      meta: { title: '模板管理', icon: 'template' }
    },
    {
      path: '/template/dataField',
      name: '数据字段',
      component: () => import('@/views/template/dataField'),
      meta: { title: '数据字段', icon: 'template' }
    },
    {
      path: '/template/templateExchange',
      name: '模板添加与编辑',
      component: () => import('@/views/template/templateExchange'),
      meta: { title: '模板添加与编辑', icon: 'template' }
    }
    ]
  },
  {
    path: '/column',
    component: Layout,
    redirect: '/column/index',
    children: [{
      path: '/column/index',
      name: '栏目列表',
      component: () => import('@/views/column/index'),
      meta: { title: '栏目列表', icon: 'table' }
    }]
  },
  {
    path: '/website',
    component: Layout,
    redirect: '/website/index',
    children: [{
      path: '/website/index',
      name: '网站文件管理',
      component: () => import('@/views/website/index'),
      meta: {title: '网站文件管理', icon: 'table'}
    }]
  },{
    path: '/repository',
    component: Layout,
    redirect: '/repository/index',
    name: '',
    children: [{
      path: '/repository/index',
      name: '资源库管理',
      component: () => import('@/views/repository/index'),
      meta: { title: '资源库管理', icon: 'table' }
    },{
      path: '/repository/resourceFile',
      name: '资源库内容',
      component: () => import('@/views/repository/resourceFile'),
      meta: { title: '资源库内容', icon: 'table' }
    }]
  },
  {
    path: '/websiteManage',
    component: Layout,
    redirect: '/websiteManage',
    children: [{
      path: '/websiteManage/role/index',
      name: '角色列表',
      component: () => import('@/views/websiteManage/roleList'),
      meta: { title: '角色列表', icon: 'table' }
    },{
      path: '/websiteManage/role/permission',
      name: '权限',
      component: () => import('@/views/websiteManage/permission'),
      meta: { title: '权限', icon: 'table' }
    },{
      path: '/websiteManage/user/index',
      name: '用户列表',
      component: () => import('@/views/websiteManage/userList'),
      meta: { title: '用户列表', icon: 'table' }
    },{
      path: '/websiteManage/publicResource/index',
      name: '公共资源定义',
      component: () => import('@/views/websiteManage/publicResource'),
      meta: { title: '公共资源定义', icon: 'table' }
    }]
  },
  {
    path: '/content',
    component: Layout,
    redirect: '/content',
    children: [{
      path: '/content/list/index',
      name: '内容列表',
      component: () => import('@/views/content/list/index'),
      meta: { title: '内容列表', icon: 'content' }
    },{
      path: '/content/examineAndApprove/index',
      name: '内容审批',
      component: () => import('@/views/content/examineAndApprove/index'),
      meta: { title: '内容审批', icon: 'content' }
    },{
      path: '/content/list/repository/index',
      name: '资源列表',
      component: () => import('@/views/content/list/repository/index'),
      meta: { title: '资源列表', icon: 'content' }
    },{
      path: '/content/list/repository/resourceFile',
      name: '资源文件',
      component: () => import('@/views/content/list/repository/resourceFile'),
      meta: { title: '资源文件', icon: 'content' }
    }]
  },
  {
    path: '/updatePassword',
    component: Layout,
    redirect: '/updatePassword',
    children: [{
      path: '/updatePassword',
      name: '修改密码',
      component: () => import('@/views/login/updatePassword'),
      meta: { title: '修改密码', icon: 'content' }
    }]
  },
  {
    path: '/example',
    component: Layout,
    redirect: '/example/table',
    name: '',
    meta: { title: '表格', icon: 'example' },
    children: [
      {
        path: 'table',
        name: 'Table',
        component: () => import('@/views/table/index'),
        meta: { title: '表格', icon: 'table' }
      },
      {
        path: 'edit',
        name: 'edit',
        component: () => import('@/views/table/edit'),
        meta: { title: '可编辑表格', icon: 'edit' }
      },
      {
        path: 'echart',
        name: 'echart',
        component: () => import('@/views/echart/index'),
        meta: { title: '图表', icon: 'echart' }
      },
      {
        path: 'tree',
        name: 'tree',
        component: () => import('@/views/tree/tree'),
        meta: { title: '树', icon: 'echart' }
      },
      {
        path: 'editor',
        name: 'editor',
        component: () => import('@/views/editor/editor'),
        meta: { title: '富文本', icon: 'editor' }
      },
      {
        path: 'iframe',
        name: 'iframe',
        component: () => import('@/views/iframe/index'),
        meta: { title: 'iframe', icon: 'iframe' }
      },
      {
        path: 'iframe',
        name: 'iframe',
        component: () => import('@/views/column/index'),
        meta: { title: 'iframe', icon: 'iframe' }
      }
    ]
  },

  {
    path: '/form',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'index',
        component: () => import('@/views/form/index'),
        meta: { title: '表单', icon: 'form' }
      },
      {
        path: 'home',
        name: 'home',
        component: () => import('@/views/form/home'),
        meta: { title: '', icon: 'form' }
      },
      {
        path: 'indexUser',
        name: 'indexUser',
        component: () => import('@/views/form/indexUser'),
        meta: { title: '', icon: 'form' }
      },
      {
        path: 'roleList',
        name: 'roleList',
        component: () => import('@/views/form/roleList'),
        meta: { title: '', icon: 'form' }
      },
      {
        path: 'indexx',
        name: 'indexx',
        component: () => import('@/views/form/indexx'),
        meta: { title: '', icon: 'form' }
      },
      {
        path: 'repositoryView',
        name: 'repositoryView',
        component: () => import('@/views/form/repositoryView'),
        meta: { title: '', icon: 'form' }
      }
    ]
  },

  {
    path: '/nested',
    component: Layout,
    redirect: '/nested/menu1',
    name: 'Nested',
    meta: {
      title: 'Nested',
      icon: 'nested'
    },
    children: [
      {
        path: 'menu1',
        component: () => import('@/views/nested/menu1/index'), // Parent router-view
        name: 'Menu1',
        meta: { title: 'Menu1' },
        children: [
          {
            path: 'menu1-1',
            component: () => import('@/views/nested/menu1/menu1-1'),
            name: 'Menu1-1',
            meta: { title: 'Menu1-1' }
          },
          {
            path: 'menu1-2',
            component: () => import('@/views/nested/menu1/menu1-2'),
            name: 'Menu1-2',
            meta: { title: 'Menu1-2' },
            children: [
              {
                path: 'menu1-2-1',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-1'),
                name: 'Menu1-2-1',
                meta: { title: 'Menu1-2-1' }
              },
              {
                path: 'menu1-2-2',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-2'),
                name: 'Menu1-2-2',
                meta: { title: 'Menu1-2-2' }
              }
            ]
          },
          {
            path: 'menu1-3',
            component: () => import('@/views/nested/menu1/menu1-3'),
            name: 'Menu1-3',
            meta: { title: 'Menu1-3' }
          }
        ]
      },
      {
        path: 'menu2',
        component: () => import('@/views/nested/menu2/index'),
        meta: { title: 'menu2' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

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
