/*
 * @Author: your lyy

 * @LastEditTime: 2020-07-05 12:19:50
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */ 
import request from '@/utils/request'

export function getTemplate(params) {
  return request({
    url: '/webadmin/template/findAll',
    method: 'get',
    params
  })
}

export function insertTemplate(data) {
  return request({
    url: '/webadmin/template/insert',
    method: 'post',
    data
  })
}

export function updateTemplate(data) {
  return request({
    url: '/webadmin/template/update',
    method: 'put',
    data
  })
}

// 模型删除
export function deleteTemplate(id) {
    return request({
      url: '/webadmin/template/delete',
      method: 'delete',
      params: { id }
    })
  }

// 模型删除
export function findByIdTemplate(params) {
    return request({
      url: '/webadmin/template/findById',
      method: 'get',
      params
    })
  }