import {login, logout, getInfo} from '@/api/user'
import {getToken, setToken, removeToken} from '@/utils/auth'
import {sessionStorageSet} from '@/utils/storage'
import {resetRouter} from '@/router'
import md5 from 'js-md5'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    id: '',
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
  SET_ID: (state, id) => {
    state.id = id
  }
}

const actions = {
  login({commit}, userInfo) {
    const {username, password} = userInfo
    const pararms = {
      username: username.trim(),
      password: md5(password)
    }
    return new Promise((resolve, reject) => {
      login(pararms).then(response => {
        if (response.data.code === 1) {
          const {data} = response.data
          commit('SET_TOKEN', data.token)
          commit('SET_NAME', data.name)
          setToken(data.token)
          sessionStorageSet('id', data.id)
          sessionStorageSet('name', data.name)
          sessionStorageSet('roles', JSON.stringify(data.roles))
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
      getInfo(state.token).then(response => {
        const {data} = response
        // const { data } = {
        //   name: 'do',
        //   avatar: 'wo'
        // }
        if (!data) {
          reject('Verification failed, please Login again.')
        }
        // const { name, avatar } = data
        // commit('SET_NAME', name)
        // commit('SET_AVATAR', avatar)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({commit, state}) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
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
      commit('RESET_STATE')
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

