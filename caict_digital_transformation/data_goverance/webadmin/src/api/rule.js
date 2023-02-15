/*
 * @Author: your lyy

 * @LastEditTime: 2020-07-11 11:27:27
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */ 
import request from '@/utils/request'

export function getRule(params) {
  return request({
    url: '/webadmin/rule/rule/findAll',
    method: 'get',
    params
  })
}

export function insertRule(data) {
  return request({
    url: '/webadmin/rule/rule/insert',
    method: 'post',
    data
  })
}

export function updateRule(data) {
  return request({
    url: '/webadmin/rule/rule/update',
    method: 'put',
    data
  })
}


export function deleteRule(id) {
    return request({
      url: 'webadmin/rule/rule/delete',
      method: 'delete',
      params: { id }
    })
}


export function findById(params) {
    return request({
      url: '/webadmin/rule/rule/findById',
      method: 'get',
      params
    })
}


export function findUpdate(params) {
  return request({
    url: '/webadmin/rule/ruleUpdateLog/findAll',
    method: 'get',
    params
  })
}

export function ruleSubscribe(data) {
  return request({
    url: '/webadmin/rule/ruleSubscribe/insert',
    method: 'post',
    data
  })
}


export function findByRuleId(params) {
  return request({
    url: '/webadmin/rule/ruleSubscribe/findByRuleId',
    method: 'get',
    params
  })
}


export function deleteRuleId(id) {
  return request({
    url: '/webadmin/rule/ruleSubscribe/delete',
    method: 'delete',
    params: { id }
  })
}

