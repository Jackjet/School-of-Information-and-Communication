/*
 * @Author: your lyy

 * @LastEditTime: 2020-07-15 10:55:46
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */ 
import request from '@/utils/request'

export function getGroupTask(params) {
  return request({
    url: '/webadmin/groupTask/findAll',
    method: 'get',
    params
  })
}

export function insertGroupTask(data) {
  return request({
    url: '/webadmin/groupTask/insert',
    method: 'post',
    data
  })
}




export function deletegroupTask(id) {
  return request({
    url: '/webadmin/groupTask/delete',
    method: 'delete',
    params: { id }
  })
}




export function groupTaskUpdate(data) {
  return request({
    url: '/webadmin/groupTask/update',
    method: 'put',
    data
  })
}

export function findByIdTaskUpdate(params) {
  return request({
    url: '/webadmin/groupTask/findById',
    method: 'get',
    params
  })
}

export function updateStatus(data) {
  return request({
    url: '/webadmin/groupTask/updateStatus',
    method: 'put',
    data
  })
}

export function manualOperation(data) {
  return request({
    url: '/webadmin/groupTask/execute',
    method: 'put',
    data
  })
}

export function metaDataTableFindAll(params) {
  return request({
    url: '/webadmin/metaDataTable/findAll',
    method: 'get',
    params
  })
}





