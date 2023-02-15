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
    url: '/webadmin/industries/findAll',
    method: 'get',
    params
  })
}

/**
 * 解决 客户列表 显示所属行业 id 的问题
 * 通过一次性查询所有的行业信息
 */
export function findAllNoPage() {
  return request({
    url: '/webadmin/industries/findAllNoPage',
    method: 'get'
  })
}

// 详情
export function find(params) {
  return request({
    url: '/webadmin/industries/find',
    method: 'get',
    params
  })
}

// 添加
export function add(data) {
  return request({
    url: '/webadmin/industries/insert',
    method: 'post',
    data
  })
}

// 修改
export function update(data) {
  return request({
    url: '/webadmin/industries/update',
    method: 'put',
    data
  })
}

// 删除
export function deleteIndustry(id) {
  return request({
    url: '/webadmin/industries/delete',
    method: 'delete',
    params: {id}
  })
}

// 删除
export function deleteAll(ids) {
  return request({
    url: '/webadmin/industries/deleteAll',
    method: 'delete',
    params: {ids}
  })
}

