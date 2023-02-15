/*
 * @Author: your name
 * @Date: 2020-09-15 22:04:05
 * @LastEditTime: 2020-10-11 21:18:45
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\store\modules\permission.js
 */
import { findCurrentUserButton } from '@/api/role'

const state = {
  permissions: [],
  states: ''
}

const mutations = {
  SET_PREMISSION: (state, permissions) => {
    state.permissions = permissions
  },
  SET_STATE: (state, val) => {
    state.states =  val
  },
  
}

const actions = {
  findCurrentUserButton({ commit }, userInfo) {
    const pararms = {}
    return new Promise((resolve, reject) => {
      findCurrentUserButton(pararms).then(response => {
        const { data } = response.data
        commit('SET_PREMISSION', data)
        commit('SET_STATE', 1)
        resolve(response.data)
      }).catch(error => {
        reject(error)
      })
    })
  },
    
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

