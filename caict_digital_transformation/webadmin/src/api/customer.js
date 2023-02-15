/*
 * @Author: your name
 * @Date: 2020-09-14 16:36:54
 * @LastEditTime: 2020-09-16 16:50:08
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\message.js
 */
import request from '@/utils/request'

// 列表
export function findAll(params) {
  return request({
    url: '/web/users/findAll',
    method: 'get',
    params
  })
}

// 详情
export function find(params) {
  return request({
    url: '/web/users/find',
    method: 'get',
    params
  })
}

// 重置密码
export function resetPassword(data) {
  return request({
    url: '/web/users/resetPwd',
    method: 'put',
    data
  })
}

// 删除
export function deleteUser(id) {
  return request({
    url: '/web/users/delete',
    method: 'delete',
    params: {id}
  })
}

// 删除
export function deleteAll(ids) {
  return request({
    url: '/web/users/deleteAll',
    method: 'delete',
    params: {ids}
  })
}

