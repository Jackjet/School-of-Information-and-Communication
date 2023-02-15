/*
 * @Author: your lyy

 * @LastEditTime: 2020-07-09 14:30:16
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */ 
import request from '@/utils/request'

export function getQualityReport(params) {
  return request({
    url: '/webadmin/qualityReport/findAll',
    method: 'get',
    params
  })
}


export function updateQualityReport(data) {
  return request({
    url: '/webadmin/qualityReport/update',
    method: 'put',
    data
  })
}


export function deleteQualityReport(id) {
    return request({
      url: '/webadmin/qualityReport/delete',
      method: 'delete',
      params: { id }
    })
}


export function findByQualityReport(params) {
    return request({
      url: '/webadmin/qualityReport/findById',
      method: 'get',
      params
    })
}


export function insertQualityReport(data) {
  return request({
    url: '/webadmin/qualityReport/insert',
    method: 'post',
    data
  })
}

export function contentOfReport(params) {
    return request({
      url: '/webadmin/qualityReport/contentOfReport',
      method: 'get',
      params
    })
  }