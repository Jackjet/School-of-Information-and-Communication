/*
 * @Author: your name
 * @Date: 2020-05-11 16:46:40
 * @LastEditTime: 2020-10-12 21:32:15
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
    this.Url = "http://10.200.7.153/cms/";

    // 七牛云图片路径
    this.imagesUrl = 'http://10.200.7.153/cms/';
    // 服务器接口
    this.getAllColumn = "webadmin/column/getAllColumn";
    this.getAllPublicResource = "webadmin/publicResource/getAllPublicResource";
    this.getContentByColumnId = "webadmin/content/getContentByColumnId";
    this.getContentByColumnIdt = "webadmin/content/getContentByColumnId2"
    this.getContentById = '/webadmin/content/getContentById';
    this.activity = 'base/activity/findAll';
    this.activityfindById = 'base/activity/findById';
    // ajax请求配置参数
    this.url = "";
    this.method = "";
    this.data;
    this.token = "";
    this.dataType = "json";

    this.reinfo = function () { };
    this.status = 0;
    this.redata = function () { };

    this.run = function () {
        if (this.url == "") {
            console.log("url未配置");
            return -1;
        }

        var redata = this.redata;
        var reinfo = this.reinfo;

        $.ajax({
            type: this.method,
            url: this.url,
            headers: this.token,
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