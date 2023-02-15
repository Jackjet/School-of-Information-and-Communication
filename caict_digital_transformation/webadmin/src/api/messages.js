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
    url: '/webadmin/messages/findAll',
    method: 'get',
    params
  })
}

// 列表
export function findAllReceiver(params) {
  return request({
    url: '/webadmin/messages/findAllReceiver',
    method: 'get',
    params
  })
}

// 添加
export function add(data) {
  return request({
    url: '/webadmin/messages/insert',
    method: 'post',
    data
  })
}


// 详情
export function find(params) {
  return request({
    url: '/webadmin/messages/find',
    method: 'get',
    params
  })
}

// 删除
export function userMessageDeleteAll(ids) {
  return request({
    url: '/webadmin/message/messageUser/deleteAll',
    method: 'delete',
    params: {ids}
  })
}

// 已读
export function updateStatus(data) {
  return request({
    url: '/webadmin/message/messageUser/updateAllStatus',
    method: 'put',
    data
  })
}

// 已读全部
export function updateStatusAll(data) {
  return request({
    url: '/webadmin/message/messageUser/updateStatusAll',
    method: 'put',
    data
  })
}

// 删除
export function userMessageDelete(id) {
  return request({
    url: '/webadmin/message/messageUser/delete',
    method: 'delete',
    params: {id}
  })
}
