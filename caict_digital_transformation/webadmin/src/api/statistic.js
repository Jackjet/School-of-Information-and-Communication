import request from '@/utils/request'

export function statisticActiveUser(params) {
  return request({
    url: '/webadmin/statistic/findActiveUser',
    method: 'get',
    params
  })
}

export function statisticVisitUser(params) {
  return request({
    url: '/webadmin/statistic/findVisitUser',
    method: 'get',
    params
  })
}

export function statisticServiceVisitCount(params) {
  return request({
    url: '/webadmin/statistic/findServiceVisitCount',
    method: 'get',
    params
  })
}

export function statisticNewUser(params) {
  return request({
    url: '/webadmin/statistic/findNewUser',
    method: 'get',
    params
  })
}

export function statisticNewNews(params) {
  return request({
    url: '/webadmin/statistic/findNewNews',
    method: 'get',
    params
  })
}

export function statisticUserSource(params) {
  return request({
    url: '/webadmin/statistic/findUserSource',
    method: 'get',
    params
  })
}

export function statisticUserAddress(params) {
  return request({
    url: '/webadmin/statistic/findUserAddress',
    method: 'get',
    params
  })
}

export function statisticOsInfo(params) {
  return request({
    url: '/webadmin/system/base/findOsInfo',
    method: 'get',
    params
  })
}
