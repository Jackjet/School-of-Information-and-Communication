/*
 * @Author: your lyy

 * @LastEditTime: 2020-07-13 23:57:45
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */ 
import request from '@/utils/request'

export function getDataScoreRule(params) {
  return request({
    url: '/webadmin/configure/scoreRule/score',
    method: 'get',
    params
  })
}

export function insertScoreRule(data) {
  return request({
    url: '/webadmin/configure/scoreRule/insert',
    method: 'post',
    data
  })
}

export function findScoreRule(data) {
  return request({
    url: '/webadmin/configure/scoreRule/findScoreRule',
    method: 'get',
    data
  })
}

