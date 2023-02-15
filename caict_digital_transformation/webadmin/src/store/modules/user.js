import {login, logout} from '@/api/user'
import {getName, getToken, removeToken, removeName, setName, setToken} from '@/utils/auth'
import {resetRouter} from '@/router'
import {sessionStorageRemove, sessionStorageSet} from '@/utils/storage'
import md5 from 'js-md5'
import da from "element-ui/src/locale/lang/da";

const getDefaultState = () => {
  return {
    token: getToken(),
    name: getName(),
    avatar: '',
    id: '',
    roles: []
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  login({commit}, userInfo) {
    const {username, password, x_index, chenckMoveid} = userInfo
    const pararms = {
      username: username.trim(),
      password: md5(password),
      x_index: x_index,
      chenckMoveid: chenckMoveid
    }
    return new Promise((resolve, reject) => {
      login(pararms).then(response => {
        if (response.data.code === 1) {
          const {data} = response.data
          commit('SET_TOKEN', data.token)
          commit('SET_NAME', data.name)
          setToken(data.token)
          setName(data.name)
          sessionStorageSet('id', data.id)
          sessionStorageSet('name', data.name)
          sessionStorageSet('roles', JSON.stringify(data.roles))

          console.log(data)
        }
        resolve(response.data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({commit, state}) {
    return new Promise((resolve, reject) => {
      let data = {vale: 1}
      commit('SET_AVATAR', 1)
      resolve(data)
    })
  },

  // user logout
  logout({commit, state}) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        sessionStorageRemove('id')
        removeToken() // must remove  token  first
        removeName()
        resetRouter()
        commit('RESET_STATE')
        commit('SET_TOKEN')
        commit('SET_NAME')
        commit('SET_ROLES')
        sessionStorageRemove('name')
        sessionStorageRemove('store')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({commit}) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      removeName()
      resetRouter()
      commit('RESET_STATE')
      commit('SET_TOKEN')
      commit('SET_NAME')
      commit('SET_ROLES')
      sessionStorageRemove('id')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

