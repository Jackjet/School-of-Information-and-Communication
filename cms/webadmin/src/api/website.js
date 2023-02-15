
import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/webadmin/website/getAllPathByDir',
    method: 'get',
    params
  })
}
export function createDir(data) {
  return request({
    url: '/webadmin/website/createDir',
    method: 'post',
    data
  })
}
export function deleteFile(params) {
  return request({
    url: '/webadmin/website/deleteFile',
    method: 'get',
    params
  })
}
export function getOS(params) {
  return request({
    url: '/webadmin/website/getOS',
    method: 'get',
    params
  })
}