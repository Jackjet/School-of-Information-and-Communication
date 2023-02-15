import request from '@/utils/request'

export function findAll(params) {
  return request({
    url: '/webadmin/repository/findAll',
    method: 'get',
    params:params
  })
}

export function findById(params) {
  return request({
    url: '/webadmin/repository/findById',
    method: 'get',
    params:params
  })
}

export function insert(params) {
  return request({
    url: '/webadmin/repository/insert',
    method: 'post',
    data:params
  })
}

export function update(params) {
  return request({
    url: '/webadmin/repository/update',
    method: 'put',
    data:params
  })
}

export function deleteById(params) {
  return request({
    url: '/webadmin/repository/deleteById',
    method: 'delete',
    params:params
  })
}

export function fileFindAll(params) {
  return request({
    url: '/webadmin/repository/fileFindAll',
    method: 'get',
    params:params
  })
}

export function fileInsert(params) {
  return request({
    url: '/webadmin/repository/fileInsert',
    method: 'post',
    data:params
  })
}

export function fileDelete(params) {
  return request({
    url: '/webadmin/repository/fileDelete',
    method: 'delete',
    params:params
  })
}
