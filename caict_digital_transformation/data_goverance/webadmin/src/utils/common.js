export default {
  isInteger: (rule, value, callback) => {
    const reg = /^[0-9]+$/
    if (!value) {
      callback(new Error(rule.msg))
    } if (!reg.test(value)) {
      callback(new Error('请输入正整数'))
    } else {
      callback()
    }
  },
  isUrl: (rule, value, callback) => {
    // let reg=/^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\*\+,;=.]+$/
    // if (!value) {
    //   callback(new Error('请输入页面路径'))
    // } if (!reg.test(value)) {
    //   callback(new Error('请输入以http://，https://开头的网址'))
    // } else {
    //   callback()
    // }
    const reg0 = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    const reg1 = /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\*\+,;=.]+$/
    if (!value) {
      callback(new Error(rule.msg))
    } if (!reg0.test(value) && !reg1.test(value)) {
      callback(new Error('格式有误'))
    } else {
      callback()
    }
  }
}
