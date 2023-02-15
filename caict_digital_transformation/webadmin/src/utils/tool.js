/*
 * @Author: lyy
 * @Date: 2020-06-09 23:06:26
 * @LastEditTime: 2020-10-12 00:23:30
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 */
/* eslint-disable prefer-const */
// eslint-disable-next-line no-unused-vars
import {sessionStorageSet, sessionStorageGet} from '@/utils/storage'
import Layout from '@/layout'
import router from '../router'

export const isString = val => typeof val === 'string'

// eslint-disable-next-line no-unused-vars
export const isBoolean = val => typeof val === 'boolean'

// eslint-disable-next-line no-unused-vars
export const isFunction = val => val && typeof val === 'function'

// eslint-disable-next-line no-unused-vars
export const isArray = val => !!val && Array.isArray(val)

// eslint-disable-next-line no-unused-vars
export const isNumber = val => typeof val === 'number'

/**
 *  金钱格式化，三位加逗号
 *  @param { number } num
 */
export const formatMoney = num => num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')

/**
 *  截取字符串并加身略号
 */
export function subText(str, length) {
  if (str.length === 0) {
    return ''
  }
  if (str.length > length) {
    return str.substr(0, length) + '...'
  } else {
    return str
  }
}

/**
 是否为空
 * @param {*} val
 * @returns
 */
export function isEmpty(val) {
  // null or undefined
  if (val == null) return true
  if (val === undefined) return true

  if (val === 'undefined') return true

  if (typeof val === 'boolean') return false

  if (typeof val === 'number') return !val

  if (val instanceof Error) return val.message === ''
  switch (Object.prototype.toString.call(val)) {
    // String or Array
    case '[object String]':
    case '[object Array]':
      return !val.length

    // Map or Set or File
    case '[object File]':
    case '[object Map]':
    case '[object Set]': {
      return !val.size
    }
    // Plain Object
    case '[object Object]': {
      return !Object.keys(val).length
    }
  }

  return false
}

/**
 * 判断类型
 * @param {*} target 值
 */
export function type(target) {
  let ret = typeof (target)
  let template = {
    '[object Array]': 'array',
    '[object Object]': 'object',
    '[object Number]': 'number - object',
    '[object Boolean]': 'boolean - object',
    '[object String]': 'string-object'
  }

  if (target === null) {
    return 'null'
  } else if (ret === 'object') {
    let str = Object.prototype.toString.call(target)
    return template[str]
  } else {
    return ret
  }
}

/**
 * 递归树
 * @param {*} data 值
 * @parentId
 */
export function getJsonTree(data, parentId) {
  let itemArr = []
  for (let i = 0; i < data.length; i++) {
    let node = data[i];
    if (node.parentId === parentId) {
      let newNode = {};
      newNode.id = node.id
      newNode.component = node.component
      newNode.name = node.name
      newNode.parentId = node.parentId
      newNode.level = node.level
      newNode.levelMsg = node.levelMsg
      newNode.parentName = node.parentName
      newNode.redirect = node.redirect
      newNode.meta = node.meta
      newNode.path = node.path
      newNode.child = node.child
      newNode.typeIndex = node.type

      if (node.type === 0) {
        newNode.type = '菜单'
      } else if (node.type === 1) {
        newNode.type = '功能按钮'
      } else if (node.type === 2) {
        newNode.type = '链接新窗口'
      } else if (node.type === 3) {
        newNode.type = '链接内页'
      } else if (node.type === 4) {
        newNode.type = '内置功能页'
      }
      if (i !== 0 && i === data.length - 1) {
        newNode.seq = -1
      } else if (data.length === 1 && node.name !== "总部") {
        newNode.seq = -2
      } else {
        newNode.seq = node.seq
      }
      if (node.children.length > 0) {
        newNode.children = getJsonTree(node.children, node.id);
      }
      itemArr.push(newNode)
    }
  }
  return itemArr;
}

/**
 * 递归树
 * @param {*} data 值
 * @parentId
 */
export function getJsonTreeAssiagn(data, parentId) {
  let itemArr = []
  for (let i = 0; i < data.length; i++) {
    let node = data[i];
    if (node.parentId === parentId) {
      let newNode = {};
      newNode.id = node.id
      newNode.label = node.name
      if (node.type === 4) {
        newNode.disabled = true
      }
      if (node.children.length > 0) {
        newNode.children = getJsonTreeAssiagn(node.children, node.id);
      }
      if (node.type !== 4) {
        itemArr.push(newNode)
      }

    }
  }
  return itemArr;
}

/**
 * 递归树
 * @param {*} data 值
 * @parentId
 */
var choose = []

export function getJsonTreeChoose(data, parentId) {
  for (let i = 0; i < data.length; i++) {
    let node = data[i];
    if (node.parentId === parentId) {
      let newNode = {};
      newNode.id = node.id
      newNode.label = node.name
      if (node.type === 4) {
        choose.push(node.id)
      }
      if (node.children.length > 0) {
        newNode.children = getJsonTreeChoose(node.children, node.id);
      }

    }
  }
  return choose;
}


/**
 * 防抖
 *
 */
export function debounce(func, wait, immediately) {
  let timer
  let debounced = function (...args) {
    let result
    // 清除闹钟后，闹钟还是存在的
    if (timer) clearTimeout(timer)
    if (immediately) {
      let called = !timer
      timer = setTimeout(() => {
        timer = null
      }, wait)
      if (called) {
        result = func.apply(this, args)
      }
    } else {
      timer = setTimeout(() => {
        func.apply(this, args)
      }, wait)
    }
    return result
  }
  debounced.cancel = function () {
    clearTimeout(timer)
    timer = null
  }
  return debounced
}

/**
 * 节流
 *
 */
export function throttle(func, wait, options) {
  let time, context, args, result;
  let previous = 0;
  if (!options) options = {};

  let later = function () {
    previous = options.leading === false ? 0 : new Date().getTime();
    time = null;
    func.apply(context, args);
    if (!time) context = args = null;
  };

  let throttled = function () {
    let now = new Date().getTime();
    if (!previous && options.leading === false) previous = now;
    let remaining = wait - (now - previous);
    context = this;
    args = arguments;
    if (remaining <= 0 || remaining > wait) {
      if (time) {
        clearTimeout(time);
        time = null;
      }
      previous = now;
      func.apply(context, args);
      if (!time) context = args = null;
    } else if (!time && options.trailing !== false) {
      time = setTimeout(later, remaining);
    }
  };
  return throttled;
}

export function getPermissonJsonTree(data, parentId, path) {
  let itemArr = []
  for (let i = 0; i < data.length; i++) {
    let node = data[i];
    if (node.parentId === parentId) {
      let newNode = {};
      newNode.path = node.path
      if (node.levelMsg === '一级') {
        newNode.component = Layout
        if (node.children.length > 0) {
          newNode.redirect = node.path + node.children[0].path
        }
      } else if (node.levelMsg === '二级') {
        let num = node.component.indexOf("@") + 1
        let url = node.component.slice(num, node.component.length - 2)
        newNode.component = _import(url)
        if (node.children.length > 0) {
          newNode.redirect = path + node.path + node.children[0].path
        }
      } else {
        let num = node.component.indexOf("@") + 1
        let url = node.component.slice(num, node.component.length - 2)
        newNode.component = _import(url)
      }
      newNode.name = node.name
      newNode.meta = node.meta
      if (node.children.length > 0) {
        newNode.children = getPermissonJsonTree(node.children, node.id, node.path);
      }
      itemArr.push(newNode)
    }
  }
  return itemArr;
}

function _import(file) {
  return () => import('@/' + file + '.vue')
}
