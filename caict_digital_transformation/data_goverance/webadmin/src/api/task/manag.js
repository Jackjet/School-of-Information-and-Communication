/*
 * @Author: your name
 * @Date: 2020-07-08 15:30:36
 * @LastEditTime: 2020-07-15 15:37:29
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\omManger\user.js
 */ 
import request from '@/utils/request'

// 获取任务列表
export function getList(params) {
  return request({
    url: '/webadmin/tasks/findAll',
    method: 'get',
    params
  })
}

// 模型任务删除
export function deletes(id) {
  return request({
    url: '/webadmin/tasks/delete',
    method: 'delete',
    params: { id }
  })
}

// 任务新增
export function insert(data) {
  return request({
    url: '/webadmin/tasks/insert',
    method: 'post',
    data
  })
}


//更新任务信息
export function update(data) {
  return request({
    url: '/webadmin/tasks/update',
    method: 'put',
    data
  })
}

//更新任务信息
export function findById(params) {
  return request({
    url: '/webadmin/tasks/findById',
    method: 'get',
    params
  })
}

//查询任务组信息


export function groupTaskfa(params) {
  return request({
    url: '/webadmin/groupTask/findAll',
    method: 'get',
    params
  })
}

//查询数据源信息
export function SourcefindAll(params) {
  return request({
    url: '/webadmin/dataSource/findAll',
    method: 'get',
    params
  })
}
export function updateStatus(data) {
  return request({
    url: '/webadmin/tasks/updateStatus',
    method: 'put',
    data
  })
}
export function manualOperation(data) {
  return request({
    url: '/webadmin/tasks/execute',
    method: 'put',
    data
  })
}

