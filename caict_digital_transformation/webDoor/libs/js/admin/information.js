/*
 * @Author: your name
 * @Date: 2020-10-23 15:21:56
 * @LastEditTime: 2020-11-02 23:28:34
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webDoor\libs\js\admin\information.js
 */
let request = new Call_Request()
layui.use('layer', function () {
    var layer = layui.layer;

});
function openBrowse() {
    var ie = navigator.appName == "Microsoft Internet Explorer" ? true : false;
    if (ie) {
        document.getElementById("fileElem").click();
    } else {
        var a = document.createEvent("MouseEvents");//FF的处理 
        a.initEvent("click", true, true);
        document.getElementById("fileElem").dispatchEvent(a);
    }
}

function handleFiles(e) {
    $('#fileName').html(e[0].name)
}

$(function () {
    getDataList(0);
    // findUsers();
});
function getDataList(index) {
    let pageIndex = index + 1;
    let body = {
        "page": pageIndex,
        "size": "1000",
        "startTime": '',
        "endTime": ''
    };
    request.method = 'GET',
        request.url = request.UrlPublic + request.industries,
        request.data = body,
        request.redata = function (data) {
            const html = template("infor-cell", { content: data.data.content });
            $(".infor").html(html);
            findUsers();
        }
    request.run()

}
function findUsers() {
    let body = {
        id: window.sessionStorage.getItem('id'),
    };
    request.method = 'GET',
        request.url = request.UrlPublic + request.findUsers,
        request.data = body,
        request.redata = function (data) {
            if (data.code == 1) {
                $('#name').val(data.data.name);
                $('#linkmanName').val(data.data.linkmanName);
                $('#linkmanPhone').val(data.data.linkmanPhone);
                $('#address').val(data.data.address);
                if (data.data.industries) {
                    var hangye = data.data.industries.split(',');
                    hangye.forEach(function (item) {
                        for (var i = 0; i < $('.check_box').length; i++) {
                            if ($('.check_box')[i].value === item) {
                                $($('.check_box')[i]).prop('checked', true)
                            }
                        }
                    });
                }
                addProvinces();
                initCity(data.data.province);
                initArea(data.data.province, data.data.city);
                $(".province ").val(data.data.province);
                $(".city ").val(data.data.city);
                $(".area").val(data.data.county);
                $('#yl').attr('src', data.data.logo)
                if (data.data.logo) {
                    $('#yl').css({ display: 'inline-block' })
                }
            }

        }
    request.run()
}
function pageselectCallback(page_index, jq) {
    getDataList(page_index);
}
function addProvinces() {
    area.map(function (v, k) {
        $('.province').append("<option value=" + v.label + ">" + v.label + "</option>")
    })
}
function initCity(data) {
    $('.city').html('<option value="">请选择</option>')
    area.map(function (v, k) {
        if (v.label == data) {
            v.children.map(function (v, k) {
                $('.city').append("<option value=" + v.label + ">" + v.label + "</option>")
            })
        }

    })
}
function initArea(data1, data2) {
    $('.area').html('<option value="">请选择</option>')
    area.map(function (v, k) {
        if (v.label == data1) {
            v.children.map(function (v1, k) {
                if (v1.label == data2) {
                    v1.children.map(function (v, k) {
                        $('.area').append("<option value=" + v.label + ">" + v.label + "</option>")
                    })
                }
            })
        }
    })
}
function selectCity(data) {
    $('.city').html('<option value="">请选择</option>')
    area.map(function (v, k) {
        if (v.label == $(data).val()) {
            v.children.map(function (v, k) {
                $('.city').append("<option value=" + v.label + ">" + v.label + "</option>")
            })
        }
    })
}
function selectarea(data) {
    $('.area').html('<option value="">请选择</option>')
    area.map(function (v, k) {
        if (v.label == $('.province').val()) {
            v.children.map(function (v1, k) {
                if (v1.label == $('.city').val()) {
                    v1.children.map(function (v, k) {
                        $('.area').append("<option value=" + v.label + ">" + v.label + "</option>")
                    })
                }
            })
        }
    })
}

function updateInfo() {
    let industries = ''
    $('input:checkbox').each(function () {
        if ($(this).prop('checked') == true) {
            if (industries == '') {
                industries = $(this).val()
            } else {
                industries = industries + ',' + $(this).val()
            }
        }
    });
    let body = JSON.stringify({
        "id": window.sessionStorage.getItem('id'),
        "name": $('#name').val(),
        "linkmanName": $('#linkmanName').val(),
        "linkmanPhone": $('#linkmanPhone').val(),
        "province": $('.province').val(),
        "city": $('.city').val(),
        "county": $('.area').val(),
        "address": $('#address').val(),
        "industries": industries,
        "logo": $('#yl').attr('src'),
    });
    request.method = 'PUT',
        request.url = request.UrlPublic + request.userUpdate,
        request.data = body,
        request.redata = function (data) {
            if (data.code == 1) {
                layer.msg("修改成功!");
                findUsers()
            } else {
                layer.msg("修改失败!");
            }

        }
    request.run()

}
