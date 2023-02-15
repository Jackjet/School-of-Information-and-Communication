/*
 * @Author: your name
 * @Date: 2020-09-15 09:28:48
 * @LastEditTime: 2020-09-27 01:01:40
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\directive\permission\permission.js
 */
import store from '@/store'
import {sessionStorageSet, sessionStorageGet} from '@/utils/storage'

function checkPermission(el, binding) {
  const {value} = binding
  const roles = JSON.parse(sessionStorageGet('roles'))
  // const roles = store.getters && JSON.parse(JSON.stringify(store.getters.roles))
  if (value && value instanceof Array) {
    if (value.length > 0) {
      const permissionRoles = JSON.parse(JSON.stringify(value))
      const hasPermission = roles.some(role => {
        return permissionRoles.includes(role)
      })
      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  } else {
    throw new Error(`need roles! Like v-permission="['admin','editor']"`)
  }
}

export default {
  inserted(el, binding) {
    checkPermission(el, binding)
  },
  update(el, binding) {
    checkPermission(el, binding)
  }
}
