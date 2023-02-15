import request from '@/utils/request';

export function findAll(params) {
  return request({
    url: '/webadmin/permission/role/findAll',
    method: 'get',
    params:params
  })
}

export function findById(params) {
  return request({
    url: '/webadmin/permission/role/findById',
    method: 'get',
    params:params
  })
}

export function insert(params) {
  return request({
    url: '/webadmin/permission/role/insert',
    method: 'post',
    data:params
  })
}

export function update(params) {
  return request({
    url: '/webadmin/permission/role/update',
    method: 'put',
    data:params
  })
}

export function deleteById(params) {
  return request({
    url: '/webadmin/permission/role/deleteRole',
    method: 'delete',
    params:params
  })
}

export function rolePermissionFindAll(params) {
  return request({
    url: '/webadmin/permission/rolePermission/findAll',
    method: 'get',
    params
  })
}

export function rolePermissionInsert(data) {
  return request({
    url: '/webadmin/permission/rolePermission/insert',
    method: 'post',
    data
  })
}

export function findAllUser(data) {
  return request({
    url: '/webadmin/user/findAll',
    method: 'get',
    params:data
  })
}

export function findUserById(data) {
  return request({
    url: '/webadmin/user/findUserById',
    method: 'get',
    params:data
  })
}

export function insertUser(data) {
  return request({
    url: '/webadmin/user/insert',
    method: 'post',
    data:data
  })
}

export function updateUser(data) {
  return request({
    url: '/webadmin/user/update',
    method: 'put',
    data:data
  })
}

export function getRoles(params) {
  return request({
    url: '/webadmin/permission/role/findAllList',
    method: 'get',
    params:params
  })
}

export function resetPassword(params) {
  return request({
    url: '/webadmin/user/resetPassword',
    method: 'put',
    data:params
  })
}

export function getAllPublicResource(params) {
  return request({
    url: '/webadmin/publicResource/getAllPublicResource',
    method: 'get',
    params:params
  })
}

export function deleteData(params) {
  return request({
    url: '/webadmin/publicResource/deleteData',
    method: 'get',
    params:params
  })
}
export function saveData(data) {
  return request({
    url: '/webadmin/publicResource/saveData',
    method: 'post',
    data:data
  })
}

export function deleteUser(params) {
  return request({
    url: '/webadmin/user/deleteById',
    method: 'delete',
    params:params
  })
}
