import * as session from '../../assets/js/common'

function getToken () {
  let currentUser = session.default.session('currentUser')
  return currentUser ? currentUser.token : ''
}

// 返回相应状态吗
function checkStatus (response) {
  if (response && (response.status === 200 || response.status === 304 || response.status === 400)) {
    return response
  }
  return {
    status: -404,
    msg: '服务器异常'
  }
}

/**
 * 登录请求 无 token
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const signGET = function (that, url, parameters) {
  let config = {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    params: parameters
  }
  return that.$axios.get(url, config).then(
    (response) => {
      return checkStatus(response)
    }
  )
}

/**
 * GET 请求
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataGet = function (that, url, parameters) {
  let config = {
    headers: {
      'Authorization': 'token ' + getToken()
    },
    params: parameters
  }
  return that.$axios.get(url, config).then(
    (response) => {
      return checkStatus(response)
    }
  )
}

/**
 * POST 请求
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataPost = function (that, url, parameters) {
  return that.$axios({
    method: 'post',
    url: url,
    data: parameters,
    headers: {
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      return checkStatus(response)
    }
  )
}

/**
 * PUT 请求
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataPut = function (that, url, parameters) {
  return that.$axios({
    method: 'put',
    url: url,
    data: parameters,
    headers: {
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      return checkStatus(response)
    }
  )
}

/**
 * DELETE 请求
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataDelete = function (that, url, parameters) {
  return that.$axios({
    method: 'delete',
    url: url,
    data: parameters,
    headers: {
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      return checkStatus(response)
    }
  )
}

/**
 * 表单下载文件请求（带token）
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 * @param {string} file 下载文件名称
 */

export const downloadFile = function (that, url, parameters, file) {
  return that.$axios({
    method: 'post',
    url: url,
    data: parameters,
    responseType: 'blob',
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      let result = window.URL.createObjectURL(new Blob([response]))
      let link = document.createElement('a')
      link.style.display = 'none'
      link.href = result
      link.setAttribute('download', file)

      document.body.appendChild(link)
      link.click()
    }
  )
}

export const downloadExcelGet = function (that, url, parameters, fileName) {
  let config = {
    headers: {
      'Authorization': 'token ' + getToken()
    },
    params: parameters,
    responseType: 'arraybuffer'
  }
  return that.$axios.get(url, config).then(
    (response) => {
      var res = response.data
      if (res instanceof ArrayBuffer) {
        const utf8decoder = new TextDecoder()
        const u8arr = new Uint8Array(res)
        // 将二进制数据转为字符串
        const temp = utf8decoder.decode(u8arr)
        if (temp.includes('数据为空不允许导出')) {
          return {
            data: {
              code: 0,
              data: '数据为空不允许导出',
              msg: '失败'
            }
          }
        } else {
          const downloadurl = window.URL.createObjectURL(new Blob([response.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}))
          const link = document.createElement('a')
          link.style.display = 'none'
          link.href = downloadurl
          link.setAttribute('download', fileName)
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
        }
      }
    }
  )
}
