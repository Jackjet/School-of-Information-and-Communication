/*
 * @Author: your lyy

 * @LastEditTime: 2020-07-05 12:19:50
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */ 
import request from '@/utils/request'

export function getDataQuality(params) {
  return request({
    url: '/webadmin/configure/dataQuality/findAll',
    method: 'get',
    params
  })
}

export function insertDataQuality(data) {
  return request({
    url: '/webadmin/configure/dataQuality/insert',
    method: 'post',
    data
  })
}

export function updateDataQuality(data) {
  return request({
    url: '/webadmin/configure/dataQuality/update',
    method: 'put',
    data
  })
}

// 模型删除
export function deleteDataQuality(id) {
    return request({
      url: '/webadmin/configure/dataQuality/delete',
      method: 'delete',
      params: { id }
    })
  }