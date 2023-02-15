import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from '@/views/index/index'

Vue.use(VueRouter)

const originalPush = VueRouter.prototype.push

VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

export default new VueRouter({
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      meta: {
        title: '登录',
        hideInMenu: true,
        slots: ''
      },
      component: () => import('@/views/login/login')
    },
    {
      path: '/index',
      name: 'Home',
      meta: {
        title: '首页',
        hideInMenu: true,
        requireAuth: true,
        slots: ''
      },
      component: Index,
      redirect: '/index/examination',
      children: [
        {
          path: 'examination',
          meta: {
            title: '考试管理',
            name: '考试管理',
            slots: '',
            hideInMenu: true
          },
          component: () => import('@/views/examination/main')
        },
        {
          path: 'questionAdd',
          meta: {
            title: '考试管理',
            name: '考试管理',
            slots: '添加试题',
            hideInMenu: true
          },
          component: () => import('@/views/examination/questionBank/questionEdit')
        },
        {
          path: 'questionEdit',
          meta: {
            title: '考试管理',
            name: '考试管理',
            slots: '编辑试题',
            hideInMenu: true
          },
          component: () => import('@/views/examination/questionBank/questionEdit')
        },
        {
          path: 'examinationAdd',
          meta: {
            title: '考试管理',
            name: '考试管理',
            slots: '添加试卷',
            hideInMenu: true
          },
          component: () => import('@/views/examination/testPaper/edit')
        },
        {
          path: 'examinationEdit',
          meta: {
            title: '考试管理',
            name: '考试管理',
            slots: '编辑试卷',
            hideInMenu: true
          },
          component: () => import('@/views/examination/testPaper/edit')
        },
        {
          path: 'examinationDetail',
          meta: {
            title: '考试管理',
            name: '考试管理',
            slots: '预览试卷',
            hideInMenu: true
          },
          component: () => import('@/views/examination/testPaper/preview')
        }]
    }]
})
