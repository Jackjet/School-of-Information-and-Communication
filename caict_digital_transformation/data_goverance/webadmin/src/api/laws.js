/*
 * @Author: your name
 * @Date: 2020-06-20 19:31:19
 * @LastEditTime: 2020-07-22 16:53:52
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */ 
import request from '@/utils/request'

export function getKnowledge(params) {
  return request({
    url: '/webadmin/regulationKnowledge/findAll',
    method: 'get',
    params
  })
}

export function insertKnowledge(data) {
  return request({
    url: '/webadmin/regulationKnowledge/insert',
    method: 'post',
    data
  })
}

export function updateKnowledge(data) {
  return request({
    url: '/webadmin/regulationKnowledge/update',
    method: 'put',
    data
  })
}

export function updateDownload(data) {
  return request({
    url: '/webadmin/regulationKnowledge/updateDownload',
    method: 'put',
    data
  })
}

// 模型删除
export function deleteKnowledge(id) {
    return request({
      url: '/webadmin/regulationKnowledge/delete/',
      method: 'delete',
      params: { id }
    })
  }

export function findById(params) {
  return request({
    url: '/webadmin/regulationKnowledge/findById',
    method: 'get',
    params
  })
}

export function deleteFile(name, type) {
  return request({
    url: '/webadmin/service/file/deleteRegulationKnowledgeFile/' + name,
    method: 'delete',
    params: { type }
  })
}
  