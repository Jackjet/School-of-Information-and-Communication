/*
 * @Author: your name
 * @Date: 2020-10-22 14:54:41
 * @LastEditTime: 2020-11-04 21:54:02
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webDoor\libs\js\admin\index.js
 */
let request = new Call_Request();
$(function () {
    $('.navs').on('click', 'li', function (e) {
        $('.ifname_text').html($(this).find('a').html());
        if ($(this).hasClass('li_bottom')) {
            $(this).addClass('actives').siblings().removeClass('actives');
            $('.li_bottom_one').addClass('active').siblings().removeClass('active');
        } else {
            $(this).addClass('active').siblings().removeClass('active').removeClass('actives');
        }
        $('#iframe').attr('src', $(this).attr('data-url'));
    })
    if (window.sessionStorage.getItem('id') !== '' && window.sessionStorage.getItem('id') !== null && window.sessionStorage.getItem('id') !== undefined) {
        findUsers();
    } else {
        tcLogin();
    }
    getAllColumn();
});
function getAllColumn() {
    request.url = request.Url + request.getAllColumn;
    request.method = "get";
    request.data = {};
    request.redata = function (data) {
        const html = template("nav-cell", { content: data.data });
        $(".nav").html(html);
    }
    request.run();
}
function findUsers() {
    let body = {
        id: window.sessionStorage.getItem('id'),
    };
    request.method = 'GET';
    request.url = request.UrlPublic + request.findUsers;
    request.data = body;
    request.redata = function (data) {
        if (data.code === 1) {
            $('.tc').html(data.data.name);
        }
    };
    request.run();
}
function go(url) {
    window.location.href = request.httpUrl + url;
}

function tcLogin() {
    window.sessionStorage.setItem('id', '');
    window.sessionStorage.setItem('token', '');
    window.location.href = '../../../web/view/login.html';
}