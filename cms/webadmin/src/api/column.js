import request from '@/utils/request'

export function findAll(params) {
  return request({
    url: '/webadmin/column/findAll',
    method: 'get',
    params:params
  })
}

export function findById(params) {
  return request({
    url: '/webadmin/column/findById',
    method: 'get',
    params:params
  })
}

export function insert(params) {
  return request({
    url: '/webadmin/column/insert',
    method: 'post',
    data:params
  })
}

export function update(params) {
  return request({
    url: '/webadmin/column/update',
    method: 'put',
    data:params
  })
}

export function deleteById(params) {
  return request({
    url: '/webadmin/column/deleteById',
    method: 'delete',
    params:params
  })
}

export function updatePower(params) {
  return request({
    url: '/webadmin/column/updatePower',
    method: 'put',
    data:params
  })
}

export function findAllRepository(params) {
  return request({
    url: '/webadmin/column/findAllRepository',
    method: 'get',
    params:params
  })
}

export function fileFindAll(params) {
  return request({
    url: '/webadmin/column/fileFindAll',
    method: 'get',
    params:params
  })
}

export function templateFindAll() {
  return request({
    url: '/webadmin/column/templateFindAll',
    method: 'get'
  })
}

