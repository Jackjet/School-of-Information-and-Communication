/*
 * @Author: your name
 * @Date: 2020-07-08 15:30:36
 * @LastEditTime: 2020-07-08 15:42:01
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\omManger\user.js
 */ 
import request from '@/utils/request'

// 获取任务列表
export function getList(params) {
  return request({
    url: '/webadmin/groupTaskLog/findAll',
    method: 'get',
    params
  })
}





export function groupTaskfa(params) {
  return request({
    url: '/webadmin/groupTask/findAll',
    method: 'get',
    params
  })
}
