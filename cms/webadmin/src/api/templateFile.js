import request from '@/utils/request'

let rootUrl = '/webadmin/templateFile/'

export function find(params) {
  return request({
    url: rootUrl+'find',
    method: 'get',
    params:params
  })
}

export function content(params) {
  return request({
    url: rootUrl+'content',
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

export function uploadFile(params) {
  return request({
    url: rootUrl+'uploadFile',
    method: 'post',
    params:params
  })
}
