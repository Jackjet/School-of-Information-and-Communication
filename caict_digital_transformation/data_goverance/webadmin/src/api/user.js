/*
 * @Author: your name
 * @Date: 2020-06-20 19:31:19
 * @LastEditTime: 2020-07-26 17:10:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

export function login(params) {
  return request({
    url: '/webadmin/user/signIn',
    method: 'get',
    params
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-admin-template/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/webadmin/user/signOut',
    method: 'get'
  })
}

export function mainMenuTree(params) {
  return request({
    url: '/webadmin/permission/my/mainMenuTree',
    method: 'get',
    params
  })
}

export function resetPassword(data) {
  return request({
    url: '/webadmin/user/updatePassword',
    method: 'put',
    data
  })
}

export function PermissionFindAll(params) {
  return request({
    url: 'webadmin/permission/my/menuTree',
    method: 'get',
    params
  })
}

export function dataQualityIndex(params) {
  return request({
    url: '/webadmin/template/dataQualityIndex',
    method: 'get',
    params
  })
}


export function findByCurrentUser(params) {
  return request({
    url: '/webadmin/rule/ruleSubscribe/findByCurrentUser',
    method: 'get',
    params
  })
}

export function verifyUserRole(data) {
  return request({
    url: '/webadmin/user/verifyUserRole',
    method: 'post',
    data
  })
}

