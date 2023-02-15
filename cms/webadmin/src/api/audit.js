import request from '@/utils/request'

export function treeFindAll() {
  return request({
    url: '/webadmin/audit/treeFindAll',
    method: 'get'
  })
}

export function findAll(params) {
  return request({
    url: '/webadmin/audit/findAll',
    method: 'get',
    params:params
  })
}

export function pass(params) {
  return request({
    url: '/webadmin/audit/pass',
    method: 'put',
    data:params
  })
}


export function rejected(params) {
  return request({
    url: '/webadmin/audit/rejected',
    method: 'put',
    data:params
  })
}

