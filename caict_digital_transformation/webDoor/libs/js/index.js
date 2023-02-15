/*
 * @Author: your name
 * @Date: 2020-10-27 16:52:33
 * @LastEditTime: 2020-11-01 14:17:53
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webDoor\libs\js\index.js
 */
// 全局接口调用

function Ajax(url, data) {
    return $.ajax({
        type: "get",
        url: 'http://47.93.42.226/cmsV2' + url,
        dataType: "json",
        data: data,
        success: function (res) {
            return res;
        }
    })
}

