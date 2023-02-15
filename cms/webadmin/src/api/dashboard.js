import request from '@/utils/request'

let rootUrl = '/webadmin/homePage/'

export function getNavigation(params) {
  return request({
    url: rootUrl+'getNavigation',
    method: 'get',
    params:params
  })
}

export function getUserMenus(params) {
  return request({
    url: rootUrl+'getUserMenus',
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

export function getOsInfo(params) {
  return request({
    url: rootUrl+'osInfo',
    method: 'get',
    params:params
  })
}

