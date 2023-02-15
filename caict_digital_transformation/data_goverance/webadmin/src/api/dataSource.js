/*
 * @Author: your lyy

 * @LastEditTime: 2020-07-16 11:38:34
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

export function getDataSource(params) {
  return request({
    url: '/webadmin/dataSource/findAll',
    method: 'get',
    params
  })
}

export function insertDataSource(data) {
  return request({
    url: '/webadmin/dataSource/insert',
    method: 'post',
    data
  })
}

export function updateUpdateStatus(data) {
  return request({
    url: '/webadmin/dataSource/updateStatus',
    method: 'put',
    data
  })
}


export function deleteDataSource(id) {
  return request({
    url: '/webadmin/dataSource/delete/',
    method: 'delete',
    params: {id}
  })
}


export function findByDataSourceId(params) {
  return request({
    url: '/webadmin/dataSource/findById',
    method: 'get',
    params
  })
}


export function DataSourceUpdate(data) {
  return request({
    url: '/webadmin/dataSource/update',
    method: 'put',
    data
  })
}


export function findByIdMetaDataTable(params) {
  return request({
    url: '/webadmin/metaDataTable/findById',
    method: 'get',
    params
  })
}

export function metaDataTableUpdate(data) {
  return request({
    url: '/webadmin/metaDataTable/update',
    method: 'post',
    data
  })
}

export function updateFields(data) {
  return request({
    url: '/webadmin/metaDataTable/updateFields',
    method: 'post',
    data
  })
}

export function metaDataTableFindById(params) {
  return request({
    url: '/webadmin/metaDataTable/findById',
    method: 'get',
    params
  })
}


export function findAllTableData(params) {
  return request({
    url: '/webadmin/metaDataTable/findAllTableData',
    method: 'get',
    params
  })
}

export function metaDataTablFindall(params) {
  return request({
    url: '/webadmin/metaDataTable/findAll',
    method: 'get',
    params
  })
}

export function connect(data) {
  return request({
    url: '/webadmin/dataSource/connect',
    method: 'put',
    data
  })
}

