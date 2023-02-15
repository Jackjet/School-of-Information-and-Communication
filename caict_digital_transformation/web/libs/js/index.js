$(function () {
    let httpUrl = "http://10.200.7.153";
    // 头部导航
    $.ajax({
        type: "get",
        url: httpUrl + "/cms/webadmin/column/getAllColumn",
        dataType: "json",
        success: function (res) {
            let topNav = '';
            $.each(res.data, function (index, item) {
                topNav += '<li class="nav-item nav' + (index + 1) + '" id=' + item.id + '>' +
                    '<a class="nav-link active" href="#">' + item.columnName + '</a>' +
                    '<span class="hov"></span>' +
                    '</li>';
            })
            $('.nav').html(topNav)
            $(".nav1").find(".nav-link").addClass('corBg')
            $(".nav1").siblings().find("a").removeClass('corBg');
            $(".nav1").find(".hov").addClass('bgColor')
            $(".nav1").siblings().find(".hov").removeClass('bgColor');
            // 点击头部导航
            $('.nav').on('click', 'li', function (e) {
                let index = $(".nav li").index(this);
                $(this).find(".nav-link").addClass('corBg')
                $(this).siblings().find("a").removeClass('corBg');
                $(this).find(".hov").addClass('bgColor')
                $(this).siblings().find(".hov").removeClass('bgColor');
                if (index === 0) {
                    $('.page-layout').removeClass('displayNone');
                    $('.experiment').addClass('displayNone');
                } else if (index === 1) {
                    $('.page-layout').addClass('displayNone');
                    $('.experiment').removeClass('displayNone');
                    $('.experiment-banner img').attr('src', '../public/images/experiment/banner .png');
                    $('.back-top-text h2').text('工业互联网平台创新与测试验证重点实验室');
                    $('.back-top-text p').text('中国信息通信研究院负责设计搭建了工业互联网平台创新与测试验证重点实验室，并于2018年8月份被认定为工业和信息化部重点实验室。实验室致力于引领信息通信技术与工业融合创新，推动工业互联网平台应用普及，服务制造业高质量发展。');
                    leftNavListData(res.data[index].children)
                } else if (index === 2) {
                    $('.page-layout').addClass('displayNone');
                    $('.experiment').removeClass('displayNone');
                    $('.experiment-banner img').attr('src', '../public/images/achievements/banner2x.png');
                    $('.back-top-text h2').text('科 技 创 新 | 引 领 未 来');
                    $('.back-top-text p').text('Technological Innovation Leads the Future');
                    leftNavListData(res.data[index].children)
                } else if (index === 3) {
                    $('.page-layout').addClass('displayNone');
                    $('.experiment').removeClass('displayNone');
                    $('.experiment-banner img').attr('src', '../public/images/achievements/banner3.png');
                    $('.back-top-text h2').text('工业互联网平台创新与测试验证重点实验室');
                    $('.back-top-text p').text('中国信息通信研究院负责设计搭建了工业互联网平台创新与测试验证重点实验室，并于2018年8月份被认定为工业和信息化部重点实验室。实验室致力于引领信息通信技术与工业融合创新，推动工业互联网平台应用普及，服务制造业高质量发展。');
                    leftNavListData(res.data[index].children)
                } else if (index === 4) {
                    $('.page-layout').addClass('displayNone');
                    $('.experiment').removeClass('displayNone');
                    $('.experiment-banner img').attr('src', '../public/images/achievements/banner4.png');
                    $('.back-top-text h2').text('工业互联网平台创新与测试验证重点实验室');
                    $('.back-top-text p').text('中国信息通信研究院负责设计搭建了工业互联网平台创新与测试验证重点实验室，并于2018年8月份被认定为工业和信息化部重点实验室。实验室致力于引领信息通信技术与工业融合创新，推动工业互联网平台应用普及，服务制造业高质量发展。');
                    leftNavListData(res.data[index].children)
                } else if (index === 5) {
                    $('.page-layout').addClass('displayNone');
                    $('.experiment').removeClass('displayNone');
                    $('.experiment-banner img').attr('src', '../public/images/achievements/banner5.png');
                    $('.back-top-text h2').text('工业互联网平台创新与测试验证重点实验室');
                    $('.back-top-text p').text('中国信息通信研究院负责设计搭建了工业互联网平台创新与测试验证重点实验室，并于2018年8月份被认定为工业和信息化部重点实验室。实验室致力于引领信息通信技术与工业融合创新，推动工业互联网平台应用普及，服务制造业高质量发展。');
                    leftNavListData(res.data[index].children)
                } else if (index === 6) {
                    $('.page-layout').addClass('displayNone');
                    $('.experiment').removeClass('displayNone');
                    $('.experiment-banner img').attr('src', '../public/images/achievements/banner6.png');
                    $('.back-top-text h2').text('');
                    $('.back-top-text p').text('');
                    leftNavListData(res.data[index].children)
                }
            })
        }
    })

})
// 渲染左侧导航栏
function leftNavListData(list, isIf) {
    let leftNav = ''
    $.each(list, function (index, item) {
        if (index === 0) {
            leftNav += '<li class="experiment-list-item active-item" isList="' + item.isPcList + '"' + ' columnId=' + item.columnId + '>' +
                '<img src="../public/images/experiment/Icon-HBase.png" alt="">' +
                '<span>' + item.columnName + '</span>' +
                '</li>';
        } else {
            leftNav += '<li class="experiment-list-item" isList="' + item.isPcList + '"' + ' columnId=' + item.columnId + '>' +
                '<img src="../public/images/experiment/Icon-HBase.png" alt="">' +
                '<span>' + item.columnName + '</span>' +
                '</li>';;
        }
    })
    $('.experiment-list').html(leftNav);
    rightContentTem(isIf);
}
// 点击左侧导航
function rightContentTem(isIf) {
    let oneIsList = $($(".experiment-list li")[0]).attr('isList');
    let oneColumnId = $($(".experiment-list li")[0]).attr('columnId');
    let text = $($(".experiment-list li")[0].children[1]).text();
    if (!isIf) {
        clickLeftNav(oneColumnId, oneIsList, text);
    }
    if (isIf === '学术交流') {
        $($('.experiment-list-item')[1]).addClass("active-item").siblings().removeClass('active-item');
    }
    $('.experiment-list').on('click', 'li', function (e) {
        // tabs 高亮显示
        $(this).addClass("active-item").siblings().removeClass('active-item');
        let isList = $(this).attr('isList');
        let columnId = $(this).attr('columnId');
        let text = $($(this)[0].children[1]).text();
        clickLeftNav(columnId, isList, text);
        if (text === '产线全景展示') {
            window.open('video_map4.html');
        }
    })
}
// 右侧内容模板 isList判断是否为列表 columnId获取对应内容
function clickLeftNav(columnId, isList, text) {
    if (isList == 0) {
        $(document).ready(function () {
            $('.loading').removeClass('displayNone');
            setTimeout(function () {
                $.ajax({
                    type: "get",
                    url: httpUrl + "/cms/webadmin/content/getContentByColumnId2",
                    data: { columnId: columnId },
                    dataType: "json",
                    success: function (res) {
                        $('.loading').addClass('displayNone');
                        let htmlCon = JSON.parse(res.data.content).values.content;
                        $('.experiment-list-content').html(htmlCon);
                    }
                });
            }, 2000);
        })
    } else if (isList == 1) {
        listApiFun(text, columnId, page = 1, size = 6);
    }
}
function listApiFun(text, columnId, page, size) {
    $(document).ready(function () {
        $('.loading').removeClass('displayNone');
        $.ajax({
            type: "get",
            url: httpUrl + "/cms/webadmin/content/getContentByColumnId",
            data: { columnId: columnId, page: page, size: size },
            dataType: "json",
            success: function (res) {
                $('.loading').addClass('displayNone');
                if (text === '实验室成员') {
                    memberFun(res.data);
                } else if (text === '创新技术测试验证') {
                    verificationFun(res.data);
                } else if (text === '学术交流' || text === '开放课题') {
                    listTemFun(res.data, text, columnId);
                } else if (text === '联系我们') {
                    contactUsFun(res.data);
                } else if (text === '体验培训') {
                    experienceFun(text, columnId);
                }
            }
        })
    });
}
// 实验室介绍-实验室成员
function memberFun(list) {
    console.log(list)
    let htmlCon = '<h2>实验室成员</h2><ul class="member-list">';
    $.each(list.content, function (index, item) {
        // <div class="title-img"><img src="${JSON.parse(item.content).values.icon[0].fileDownloadUri}" alt=""></div>
        htmlCon += '<li>' +
            '<div class="member-information">' +
            '<span class="member-name">' + JSON.parse(item.content).values.name + '</span>' +
            '<span class="member-education">' + JSON.parse(item.content).values.education + '</span>' +
            '<span class="member-position">' + JSON.parse(item.content).values.department + '</span>' +
            '</div>' +
            '</li>';
    })
    htmlCon += '</ul>';
    $('.experiment-list-content').html(htmlCon);
}
// 科研成果-创新技术测试验证
function verificationFun(list) {
    let htmlCon = '<ul class="technical-verification">';
    $.each(list.content, function (index, item) {
        htmlCon += '<li>' +
            '<div class="technical-verification-lidiv">' +
            '<div class="technical-title-text">' +
            '<h4>' + JSON.parse(item.content).values.title + '</h4>' +
            '<p>' + JSON.parse(item.content).values.content + '</p>' +
            '</div>' +
            '<div class="technical-img"></div>' +
            '</div>' +
            '<div class="technical-verification-mouseenter verification-active">' +
            '<div class="verification-mouseenter">' +
            '<div class="title-back-img"></div>' +
            '<div class="hover-style">' +
            '<h4>' + JSON.parse(item.content).values.title + '</h4>' +
            '<p>' + JSON.parse(item.content).values.content2 + '</p>' +
            '</div>' +
            '</div>' +
            '<div class="hover-btn">' +
            '<span key="0" index="' + index + '"><img src="../public/images/achievements/xiangqing-2.png" alt=""> 查看详情</span>' +
            '<span key="1" index="' + index + '"><img src="../public/images/achievements/play-circle.png" alt=""> 查看视频</span>' +
            '</div>' +
            '</div>' +
            '</li>';
    })
    htmlCon += '</ul>';
    $('.experiment-list-content').html(htmlCon);
    // 鼠标移入事件
    $('.technical-verification').on('mouseenter', 'li', function (e) {
        $(this).children('.technical-verification-lidiv').addClass('verification-active');
        $(this).children('.technical-verification-mouseenter').removeClass('verification-active');
        $(this).addClass("technical-active").siblings().removeClass('technical-active');
    })
    // 创新技术测试验证-查看详情-查看视频
    $(".hover-btn").on('click', 'span', function () {
        let index = $(this).attr('index');
        let key = $(this).attr('key');
        $('.pop-up').removeClass('displayNone');
        if (key == 0) {
            let details = JSON.parse(list.content[index].content).values.details;
            $('.pop-up-title-text').text(JSON.parse(list.content[index].content).values.title);
            $('.pop-up-video-dateils').html(details);
        } else if (key == 1) {
            let videoUrl = JSON.parse(list.content[index].content).values.video[0].fileDownloadUri;
            $('.pop-up-title-text').text(JSON.parse(list.content[index].content).values.title);
            let htmlCon = '<video id="video" loop preload controls muted autoplay>' +
                '<source src="' + videoUrl + '" type="video/mp4; codecs="avc1.42E01E, mp4a.40.2"">' +
                '</video>';
            $('.pop-up-video-dateils').html(htmlCon);
        }
    })
    // 关闭弹窗
    $('.close-btn').on('click', function () {
        $('.pop-up').addClass('displayNone');
        $('.pop-up-video-dateils').children().remove('#video');
    })
    // 鼠标移出事件
    $('.technical-verification').on('mouseleave', 'li', function (e) {
        $(this).children('.technical-verification-mouseenter').addClass('verification-active');
        $(this).children('.technical-verification-lidiv').removeClass('verification-active');

        $(this).removeClass('technical-active');
    })
}
// 列表模板
function listTemFun(list, title, columnId) {
    let htmlCon = '<h2>' + title + '</h2><ul class="direction-list">';
    $.each(list.content, function (index, item) {
        let time = fromDate(item.createTime);
        htmlCon += '<li columnId="' + item.columnId + '" id="' + item.id + '">' +
            '<span>' + JSON.parse(item.content).values.title + '</span>' +
            '<span>' + time + '</span>' +
            '</li>';
    })
    htmlCon += '</ul>' + paginationFun(list);
    $('.experiment-list-content').html(htmlCon);
    // 点击列表某一项
    $('.direction-list').on('click', 'li', function (e) {
        let index = $('.direction-list li').index(this);
        let id = $(this).attr('id');
        listDetailsFun(id, title, list.content[index].columnId);
    })
    paginationClcikFun(list, title, columnId, size = 6);
}
function fromDate(ms) {
    var date = new Date(ms);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
}
// 分页模板
function paginationFun(list) {
    let html = '<div class="pagination-list">' +
        '<nav aria-label="...">' +
        '<ul class="pagination pagination-sm">' +
        '<li class="page-item page-left">' +
        '<span class="page-link">&lt;</span>' +
        '</li>';
    for (var i = 0; i < list.totalPages; i++) {
        if (i === 0) {
            html += '<li class="page-num page-item active">' +
                '<span class="page-link">' + (i + 1) + '</span>' +
                '</li>';
        } else {
            html += '<li class="page-num page-item">' +
                '<span class="page-link">' + (i + 1) + '</span>' +
                '</li>';
        }
    }
    html += '<li class="page-item">' +
        '<span class="page-link page-right">&gt;</span>' +
        '</li>' +
        '</ul>' +
        '<div class="input-num">' +
        '第 <input id="inputVal" type="text" value="1"> 页 / 共' + list.totalPages + '页' +
        '</div>' +
        '</nav>' +
        '</div>';
    return html
}
// 分页事件
function paginationClcikFun(list, title, columnId, size) {
    let pageNum = 1;
    $('.pagination').on('click', '.page-num', function () {
        $(this).addClass("active").siblings().removeClass('active');
        let index = $('.pagination .page-num').index(this);
        pageNum = index + 1;
        paginationListFun(title, columnId, pageNum, size);
    })
    $('.pagination').on('click', '.page-left', function () {
        pageNum -= 1;
        if (pageNum < 1) {
            pageNum = 1
        }
        paginationListFun(title, columnId, pageNum, size);
        $($('.pagination .page-num')[pageNum - 1]).addClass("active").siblings().removeClass('active');
    })
    $('.pagination').on('click', '.page-right', function () {
        pageNum += 1;
        if (pageNum > list.totalPages) {
            pageNum = list.totalPages
        }
        paginationListFun(title, columnId, pageNum, size);
        $($('.pagination .page-num')[pageNum - 1]).addClass("active").siblings().removeClass('active');
    })
    $('#inputVal').on('input', function () {
        pageNum = $(this).val() - 1;
        paginationListFun(title, columnId, pageNum, size);
        $($('.pagination .page-num')[pageNum - 1]).addClass("active").siblings().removeClass('active');
    })
}
function paginationListFun(title, columnId, page, size) {
    let url = $('.tabs-btn-active').text() == "不限" ? httpUrl + "/cms/webadmin/content/getAllContentByColumnId" : httpUrl + "/cms/webadmin/content/getContentByColumnId"
    $.ajax({
        type: "get",
        url: url,
        data: { columnId: columnId, page: page, size: size },
        dataType: "json",
        success: function (res) {
            if (title === '学术交流' || title === '开放课题') {
                let htmlCon = '';
                $.each(res.data.content, function (index, item) {
                    htmlCon += '<li columnId="' + item.columnId + '" id="' + item.id + '">' +
                        '<span>' + JSON.parse(item.content).values.content + '</span>' +
                        '<span>2019-02-22</span>' +
                        '</li>';
                })
                $('.direction-list').html(htmlCon)
            } else if (title === '体验培训') {
                let htmlCon = '';
                $.each(res.data.content, function (index, item) {
                    htmlCon += '<li id="' + item.id + '">' +
                        '<div class="experience-list-img">' +
                        '<img src="' + JSON.parse(item.content).values.imgUrl[0].fileDownloadUri + '" alt=""/>' +
                        '</div>' +
                        '<div class="experience-list-title">' + JSON.parse(item.content).values.title + '</div>' +
                        '<div class="experience-list-time">体验时间 ' + JSON.parse(item.content).values.time + '</div>' +
                        '</li>';
                })
                $('.experience-list').html(htmlCon)
            }
        }
    })
}
// 联系我们
function contactUsFun(list) {
    let htmlCon = '<h2>联系我们</h2><ul class="contact-us">';
    $.each(list.content, function (index, item) {
        htmlCon += '<li>' +
            '<img src="' + JSON.parse(item.content).values.icon[0].fileDownloadUri + '" alt=""/>' +
            '<span class="contact-us-title">' + JSON.parse(item.content).values.name + '</span>' +
            '<span class="contact-us-con">' + JSON.parse(item.content).values.content + '</span>' +
            '</li>';
    })
    htmlCon += '<div class="contact-us-back">' +
        '<iframe src="./baiduMap.html" frameBorder=0 height=100% width=100% scrolling=no></iframe>' +
        '</div></ul>';
    $('.experiment-list-content').html(htmlCon);
}
//列表详情
function listDetailsFun(id, text, columnId) {
    $(document).ready(function () {
        $('.loading').removeClass('displayNone');
        $.ajax({
            type: "get",
            url: httpUrl + "/cms/webadmin/content/getContentById",
            data: { id: id },
            dataType: "json",
            success: function (res) {
                $('.loading').addClass('displayNone');
                let htmlCon = '<span class="return-btn"><strong></strong>返回</span>' + JSON.parse(res.data.content).values.details;
                // if (text === '体验培训') {
                //     htmlCon += '<span class="participate-in">我要参与</span>';
                // }
                $('.experiment-list-content').html(htmlCon);
                // 返回
                $('.return-btn').on('click', function () {
                    $.ajax({
                        type: "get",
                        url: httpUrl + "/cms/webadmin/content/getContentByColumnId",
                        data: { columnId: columnId, page: 1, size: 6 },
                        dataType: "json",
                        success: function (res) {
                            if (text === '学术交流' || text === '开放课题') {

                                listTemFun(res.data, text, columnId);
                            } else if (text === '体验培训') {
                                experienceFun(text, columnId);
                            }
                        }
                    })
                })
                // $('.participate-in').on('click', function () {
                //     window.open(httpUrl+'/cmsV2/web/view/login.html');
                // })
            }
        })
    });
}
// 体验培训
function experienceFun(title, columnId) {
    $.ajax({
        type: "get",
        url: httpUrl + "/cms/webadmin/column/getAllColumn",
        dataType: "json",
        success: function (res) {
            let tabsBtn = res.data[5].children[0].children;
            let htmlCon = '<h2>体验培训</h2>' +
                '<div class="experience-tabs">' +
                '<div class="experience-tabs-btn">资源分类：' +
                '<span index=0 class="tabs-btn-active">不限</span>' +
                '<span columnId="' + tabsBtn[0].columnId + '">' + tabsBtn[0].columnName + '</span>' +
                '<span columnId="' + tabsBtn[1].columnId + '">' + tabsBtn[1].columnName + '</span>' +
                '</div>' +
                '</div>' +
                '<ul class="experience-list"></ul>';
            // <div class="tabs-time">
            //         <input id="start_time" type="text" value="">
            //         -
            //         <input id="end_time" type="text" value="">
            //     </div>
            $('.experiment-list-content').html(htmlCon);
            let time = cycleFun();
            $('#start_time').val(time[0].startDate)
            $('#end_time').val(time[1].endDate)
            let isFlag = 0;
            // $('.tabs-time').on('click', function () {
            //     isFlag += 1;
            //     if (isFlag > 2) {
            //         alert('时间搜索')
            //     }
            // })

            $('.experience-tabs-btn').on('click', 'span', function () {
                $(this).addClass("tabs-btn-active").siblings().removeClass('tabs-btn-active');
                let columnId = $(this).attr('columnId');
                if ($(this).attr('index') == 0) {
                    experienceTem('b29653ccb407440282cbdcba2a8ffaf5', httpUrl + '/cms/webadmin/content/getAllContentByColumnId');
                }
                experienceTem(columnId, httpUrl + '/cms/webadmin/content/getContentByColumnId');
            })
            experienceTem('b29653ccb407440282cbdcba2a8ffaf5', httpUrl + '/cms/webadmin/content/getAllContentByColumnId');
        }
    })
}
function experienceTem(columnId, url) {
    $.ajax({
        type: "get",
        url: url,
        data: { columnId: columnId, size: 6, page: 1 },
        dataType: "json",
        success: function (res) {
            let = htmlCon = '';
            $.each(res.data.content, function (index, item) {
                htmlCon += '<li id="' + item.id + '">' +
                    '<div class="experience-list-img">' +
                    '<img src="' + JSON.parse(item.content).values.imgUrl[0].fileDownloadUri + '" alt=""/>' +
                    '</div>' +
                    '<div class="experience-list-title">' + JSON.parse(item.content).values.title + '</div>' +
                    '<div class="experience-list-time">体验时间 ' + JSON.parse(item.content).values.time + '</div>' +
                    '</li>';
            })
            $('.experience-list').html(htmlCon);
            $('.experience-list').on('click', 'li', function () {
                let index = $('.experience-list li').index(this);
                let id = $(this).attr('id');
                listDetailsFun(id, title = "体验培训", columnId)
            })
            $('.experience-list').nextAll().remove();
            $('.experience-list').after(paginationFun(res.data))
            paginationClcikFun(res.data, title = "体验培训", columnId, size = 6);
        }
    })
}
// 默认一周时间
function cycleFun() {
    var condition = []; //传给后端的选中的值
    var date = new Date();
    var t = date.getTime() - 1000 * 60 * 60 * 144;
    var weekDay = new Date(t);
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var statrDate = weekDay.getFullYear() + seperator1 + (weekDay.getMonth() + 1) + seperator1 + weekDay.getDate(); //拼接开始时间
    var currentdate = year + seperator1 + month + seperator1 + strDate; //拼接结束时间
    var startDate = {
        "startDate": statrDate
    };
    condition.push(startDate);
    var endDate = {
        "endDate": currentdate
    };
    condition.push(endDate);
    return condition;
}