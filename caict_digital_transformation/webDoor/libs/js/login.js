/*
 * @Author: your name
 * @Date: 2020-10-21 09:26:33
 * @LastEditTime: 2020-11-04 22:19:27
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webDoor\libs\js\login.js
 */
let request = new Call_Request();
layui.use('layer', function () {
    var layer = layui.layer;
});
function login() {
    if ($('#username').val() === '') {
        return layer.msg("手机号码或用户名不能为空!");
    }
    if ($('#passwd').val() === '') {
        return layer.msg("密码不能为空!");
    }
    request.url = request.UrlPublic + request.password;
    request.method = "post";
    request.data = JSON.stringify({
        phoneOrUsername: $('#username').val(),
        password: hex_md5($('#passwd').val())
    });
    request.redata = function (data) {
        if (data.code === 1) {
            window.sessionStorage.setItem('name', data.data.name);
            window.sessionStorage.setItem('id', data.data.id);
            window.sessionStorage.setItem('token', data.data.token);
            let urlData = location.search.substr(1).split("&");
            let obj = {};
            urlData.forEach(function (item) {
                obj[item.split("=")[0]] = item.split("=")[1];
            });
            let returnUrl = '';
            for (var k in obj) {
                if (k !== 'url') {
                    returnUrl += k + '=' + obj[k] + '&';
                }
            }
            if (obj.url) {
                window.location.href = request.Url + 'web/view' + obj.url + '?' + returnUrl.slice(0, -1);
            } else {
                window.location.href = '../../web/view/e376660b9db940909f0cd813ebf52c5e/8bb9e33fd29a43a2b60b44436d03d7d9/home.html'
            }
            // if (getQueryString('url') != '' && getQueryString('url') != null && getQueryString('url') != undefined) {
            //     if (location.href.split('?id')[1] != '' && location.href.split('?id')[1] != null && location.href.split('?id')[1] != undefined) {
            //         window.location.href = request.Url + 'web/view' + getQueryString('url') + '?id=' + getQueryString('id') + '&typeIndex=' + getQueryString('typeIndex')
            //     } else {
            //         window.location.href = request.Url + 'web/view' + getQueryString('url')
            //     }
            // } else {
            //     window.location.href = '../../web/view/e376660b9db940909f0cd813ebf52c5e/8bb9e33fd29a43a2b60b44436d03d7d9/home.html'
            // }
        } else {
            return layer.msg(data.msg);
        }
    };
    request.run();
}
// 发送验证码
function send(seconds) {
    request.url = request.UrlPublic + request.code;
    request.method = "post";
    request.data = JSON.stringify({
        phone: $('#userphone').val(),
        type: 0,
    });
    request.redata = function (data) {
        if (data.code === 1) {
            $('#sendbtn').html(seconds + "s后获取");
            let countdown = setInterval(function () {
                if (seconds > 0) {
                    --seconds;
                    $('#sendbtn').html(seconds + "s后获取");
                } else {
                    $('#sendbtn').html("获取验证码");
                    $('#sendbtn').attr("disabled", false);
                    clearInterval(countdown);
                }
            }, 1000);
            layer.msg(data.msg);
        }
        else {
            return layer.msg(data.msg);
        }
    };
    request.run();
}

function btnsend() {
    if ($('#userphone').val() === '') {
        return layer.msg("手机号码不能为空!");
    }
    if (!/^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/g.test($('#userphone').val())) {
        return layer.msg("请输入正确的手机号码!");
    }
    $('#sendbtn').html("请稍候...");
    $('#sendbtn').attr("disabled", true);
    setTimeout(function () {
        this.send(60);
    }, 500);
}
function loginPhone() {
    if ($('#userphone').val() === '') {
        return layer.msg("手机号码不能为空!");
    }
    if (!/^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/g.test($('#userphone').val())) {
        return layer.msg("请输入正确的手机号码!");
    }
    // if ($('#code').val() === '') {
    //     return layer.msg("验证码不能为空!");
    // }
    if ($('#code').val() === '') {
        return layer.msg("密码不能为空!");
    }

    if ($('#okcode').val() === '') {
        return layer.msg("请输入确认密码!");
    }

    if ($('#okcode').val() !== $('#code').val()) {
        return layer.msg("两次密码输入不一致!");
    }

    request.url = request.UrlPublic + request.loginPhone;
    request.method = "post";
    request.data = JSON.stringify({
        phone: $('#userphone').val(),
        code: $('#code').val(),
    });
    request.redata = function (data) {
        if (data.code === 1) {
            window.sessionStorage.setItem('name', data.data.name);
            window.sessionStorage.setItem('id', data.data.id);
            window.sessionStorage.setItem('token', data.data.token);
            let urlData = location.search.substr(1).split("&");
            let obj = {};
            urlData.forEach(function (item) {
                obj[item.split("=")[0]] = item.split("=")[1];
            });
            let returnUrl = '';
            for (var k in obj) {
                if (k !== 'url') {
                    returnUrl += k + '=' + obj[k] + '&';
                }
            }
            if (obj.url) {
                window.location.href = request.Url + 'web/view' + obj.url + '?' + returnUrl.slice(0, -1);
            } else {
                window.location.href = '../../web/view/e376660b9db940909f0cd813ebf52c5e/8bb9e33fd29a43a2b60b44436d03d7d9/home.html'
            }

        } else {
            return layer.msg(data.msg);
        };
    };
    request.run();
}

function dxLogin() {
    $('.box_left_span').addClass('active')
    $('.box_right_span').removeClass('active')
    $("#one").show();
    $("#two").hide();
}

function pwLogin() {
    $('.box_right_span').addClass('active');
    $('.box_left_span').removeClass('active');
    $("#two").show();
    $("#one").hide();
}


function getQueryString(name) {
    let reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    let r = window.location.search.substr(1).match(reg);

    if (r != null) {
        return unescape(r[2]);
    };
    return null;
}
