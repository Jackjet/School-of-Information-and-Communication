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
    url: '/webadmin/activities/findAll',
    method: 'get',
    params
  })
}

// 详情
export function find(params) {
  return request({
    url: '/webadmin/activities/find',
    method: 'get',
    params
  })
}

// 根据活动id查找所有参与的客户
export function findUsersByActivityId(params) {
  return request({
    url: '/webadmin/activities/findUsersByActivityId',
    method: 'get',
    params
  })
}

// 添加
export function add(data) {
  return request({
    url: '/webadmin/activities/insert',
    method: 'post',
    data
  })
}

// 修改
export function update(data) {
  return request({
    url: '/webadmin/activities/update',
    method: 'put',
    data
  })
}

// 删除
export function deleteActivity(id) {
  return request({
    url: '/webadmin/activities/delete',
    method: 'delete',
    params: {id}
  })
}

// 删除
export function deleteAll(ids) {
  return request({
    url: '/webadmin/activities/deleteAll',
    method: 'delete',
    params: {ids}
  })
}

