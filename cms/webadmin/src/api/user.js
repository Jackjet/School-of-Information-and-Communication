import request from '@/utils/request'

export function login(params) {
  console.log(params)
  return request({
    url: '/webadmin/user/signIn',
    method: 'get',
    params
  })
}

export function getInfo() {
  return request({
    url: '/webadmin/user/findById',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/webadmin/user/signOut',
    method: 'get'
  })
}

export function PermissionFindAll(params) {
  return request({
    url: '/webadmin/permission/my/menuTree',
    method: 'get',
    params
  })
}

export function updatePassword(params) {
  return request({
    url: '/webadmin/user/updatePassword',
    method: 'put',
    data:params
  })
}
