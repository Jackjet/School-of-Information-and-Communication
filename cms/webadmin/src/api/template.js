import request from '@/utils/request'

let rootUrl = '/webadmin/template/'

export function find(params) {
  return request({
    url: rootUrl+'find',
    method: 'get',
    params:params
  })
}

export function findById(params) {
  return request({
    url: rootUrl+'findById',
    method: 'get',
    params:params
  })
}

export function insert(params) {
  return request({
    url: rootUrl+'insert',
    method: 'post',
    data:params
  })
}

export function update(params) {
  return request({
    url: rootUrl+'update',
    method: 'put',
    data:params
  })
}

export function deleteById(params) {
  return request({
    url: rootUrl+'/delete',
    method: 'delete',
    params:params
  })
}

export function findData(params) {
  return request({
    url: rootUrl+'findData',
    method: 'get',
    params:params
  })
}

export function findDataById(params) {
  return request({
    url: rootUrl+'findDataById',
    method: 'get',
    params:params
  })
}

export function insertData(params) {
  return request({
    url: rootUrl+'insertData',
    method: 'post',
    data:params
  })
}

export function updateData(params) {
  return request({
    url: rootUrl+'updateData',
    method: 'put',
    data:params
  })
}

export function deleteData(params) {
  return request({
    url: rootUrl+'deleteData',
    method: 'delete',
    params:params
  })
}

export function uploadJson(params) {
  return request({
    url: rootUrl+'uploadJson',
    method: 'post',
    params:params
  })
}

