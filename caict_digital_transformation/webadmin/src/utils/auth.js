/*
 * @Author: your name
 * @Date: 2020-09-28 12:29:11
 * @LastEditTime: 2020-09-28 21:22:40
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\utils\auth.js
 */
import Cookies from 'js-cookie'


const TokenKey = 'token'
const NameKey = 'name'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function getName() {
  return Cookies.get(NameKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function setName(name) {
  return Cookies.set(NameKey, name)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function removeName() {
  return Cookies.remove(NameKey)
}


export function getCookies (key) {
  return Cookies.get(key)
}

export function setCookies (key, value, expiresTime) {
  let day = expiresTime
  let expires = new Date(new Date() * 1 +  24 * 60 * 60 * 1000 * day)
  return Cookies.set(key, value, { expires: expires })
}

export function removeCookies (key) {
  return Cookies.remove(key)
}
