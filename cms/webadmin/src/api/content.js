import request from '@/utils/request'

export function treeFindAll() {
  return request({
    url: '/webadmin/content/treeFindAll',
    method: 'get'
  })
}

export function findAll(params) {
  return request({
    url: '/webadmin/content/findAll',
    method: 'get',
    params:params
  })
}

export function updatePower(params) {
  return request({
    url: '/webadmin/content/updatePower',
    method: 'put',
    data:params
  })
}

export function fieldFindAll(params) {
  return request({
    url: '/webadmin/content/fieldFindAll',
    method: 'get',
    params:params
  })
}

export function insert(params) {
  return request({
    url: '/webadmin/content/insert',
    method: 'post',
    data:params
  })
}

export function update(params) {
  return request({
    url: '/webadmin/content/update',
    method: 'put',
    data:params
  })
}

export function findById(params) {
  return request({
    url: '/webadmin/content/findById',
    method: 'get',
    params:params
  })
}

export function findAllColumn() {
  return request({
    url: '/webadmin/content/findAllColumn',
    method: 'get'
  })
}

export function copy(params) {
  return request({
    url: '/webadmin/content/copy',
    method: 'post',
    data:params
  })
}

export function deleteById(params) {
  return request({
    url: '/webadmin/content/deleteById',
    method: 'delete',
    params:params
  })
}


export function findAllRepository(params) {
  return request({
    url: '/webadmin/content/findAllRepository',
    method: 'get',
    params:params
  })
}

export function fileFindAll(params) {
  return request({
    url: '/webadmin/content/fileFindAll',
    method: 'get',
    params:params
  })
}
