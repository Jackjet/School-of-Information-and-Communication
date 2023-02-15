/*
 * @Author: your name
 * @Date: 2020-05-11 16:46:40
 * @LastEditTime: 2020-11-02 21:58:33
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \nankai_aged_care_service_portal\public\common\js\libs\request.js
 */
/**
 * @callback url 服务器接口
 */

function Call_Request() {
    // 测试服务器
    // this.Url = "http://47.105.140.86/tangshan/";
    // this.Url = "http://47.105.96.207/tangshan/"
    // this.Url = "http://47.105.140.86/nankai/service/";
    this.Url = "http://47.93.42.226/cmsV2/";
    // this.UrlPublic = 'http://47.105.140.86/caict/'
    this.UrlPublic = 'http://39.102.73.127/caict/'

    // 七牛云图片路径
    this.imagesUrl = 'http://47.93.42.226/cms/';
    // 服务器接口
    this.getAllColumn = "webadmin/column/getAllColumn";
    this.getAllPublicResource = "webadmin/publicResource/getAllPublicResource";
    this.getContentByColumnId = "webadmin/content/getContentByColumnId";
    this.getContentByColumnIdt = "webadmin/content/getContentByColumnId2"
    this.getContentById = '/webadmin/content/getContentById';
    this.code = 'web/users/code'
    this.loginPhone = 'web/users/login/phone'
    this.footprint = 'web/my/footprint/findAll'
    this.password = 'web/users/login/password'
    this.collect = 'web/my/collect/findAll'
    this.delCollect = 'web/my/collect/delete'
    this.subscribe = 'web/my/subscribe/findAll'
    this.industries = 'web/industries/findAll'
    this.uploadLogo = 'web/users/upload/users/logo'
    this.activities = 'web/activities/findAll'
    this.myactiv = 'web/my/activity/findAll'
    this.message = 'web/my/message/findAll'
    this.updateMessage = 'web/my/message/read'
    this.userUpdate = 'web/users/update'
    this.findUsers = 'web/users/find'
    this.updatePwd = 'web/users/update/password'
    // ajax请求配置参数
    this.url = "";
    this.method = "";
    this.data;
    this.token = "";
    this.dataType = "json";
    // this.headers = {
    //     "Authorization": "token " + window.sessionStorage.getItem('token'),
    //     // "Authorization": "token 1587cec2658b4b87989c2318ab0ab16b",
    // };
    this.reinfo = function () { };
    this.status = 0;
    this.redata = function () { };
    this.run = function () {
        if (this.url == "") {
            return -1;
        }
        var redata = this.redata;
        var reinfo = this.reinfo;
        $.ajax({
            type: this.method,
            url: this.url,
            // headers: this.headers,
            headers: { "Authorization": "token " + window.sessionStorage.getItem('token') },
            data: this.data,
            dataType: this.dataType,
            contentType: 'application/json; charset=utf-8',
            success: function (data) {
                if (data.status == 0) {
                    reinfo(data);
                    this.status = -1
                } else {
                    redata(data);
                    this.status = 0;
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                this.status = -1;
            }
        })
    }
}