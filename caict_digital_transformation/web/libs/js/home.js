let request = new Call_Request()
$(function () {
    // getAllColumn()
    getContentByColumnIdt('4dcffd48fe8e43f7920d6e0c0003b53f')
    getContentByColumnId('e62fe90299c545aab6c17a6463d06f54', 1)
    getContentByColumnId('b29653ccb407440282cbdcba2a8ffaf5', 2)
})

function getAllColumn() {
    request.url = request.Url + request.getAllColumn
    request.method = "get"
    request.data = {}
    request.redata = function (data) {
        let list = data.data
        // getContentByColumnId('e62fe90299c545aab6c17a6463d06f54', 1)
        // getContentByColumnId('edd4513343724f73b352aca5703b4fb2', 2)
        // let htmlc = template('btpc', { list: list })
        // let containerc = document.querySelector('.nav');
        // containerc.innerHTML = htmlc;
        let topNav = '';
        $.each(data.data, function (index, item) {
            topNav += '<li class="nav-item nav' + (index + 1) + '" id=' + item.id + '>' +
                '<a class="nav-link active" href="#">' + item.columnName + '</a>' +
                '<span class="hov"></span>' +
                '</li>';
        })
        $('.nav').html(topNav)
        // 点击头部导航
        $('.nav').on('click', 'li', function (e) {
            let index = $(".nav li").index(this);
            if (index === 0) {
                $('.page-layout').removeClass('displayNone');
                $('.experiment').addClass('displayNone');
            } else if (index === 1) {
                $('.page-layout').addClass('displayNone');
                $('.experiment').removeClass('displayNone');
                $('.experiment-banner img').attr('src', '../public/images/experiment/banner .png');
                $('.back-top-text h2').text('物联网安全创新实验室');
                $('.back-top-text p').text('由中国信息通信研究院、中国移动研究院、中国移动江苏公司、中移物联网有限公司组成，集合“产研用”各方力量，全面推进物联网安全相关工作，打造物联网全生命周期安全生产链，提升物联网体系安全防护能力。');
                leftNavListData(data.data[index].children)
            } else if (index === 2) {
                $('.page-layout').addClass('displayNone');
                $('.experiment').removeClass('displayNone');
                $('.experiment-banner img').attr('src', '../public/images/achievements/banner2x.png');
                $('.back-top-text h2').text('科技创新 | 引领未来');
                $('.back-top-text p').text('Technological Innovation Leads the Future');
                leftNavListData(data.data[index].children)
            } else if (index === 3) {
                $('.page-layout').addClass('displayNone');
                $('.experiment').removeClass('displayNone');
                $('.experiment-banner img').attr('src', '../public/images/achievements/banner3.png');
                $('.back-top-text h2').text('物联网安全创新实验室');
                $('.back-top-text p').text('由中国信息通信研究院、中国移动研究院、中国移动江苏公司、中移物联网有限公司组成，集合“产研用”各方力量，全面推进物联网安全相关工作，打造物联网全生命周期安全生产链，提升物联网体系安全防护能力。');
                leftNavListData(data.data[index].children)
            } else if (index === 4) {
                $('.page-layout').addClass('displayNone');
                $('.experiment').removeClass('displayNone');
                $('.experiment-banner img').attr('src', '../public/images/achievements/banner4.png');
                $('.back-top-text h2').text('物联网安全创新实验室');
                $('.back-top-text p').text('由中国信息通信研究院、中国移动研究院、中国移动江苏公司、中移物联网有限公司组成，集合“产研用”各方力量，全面推进物联网安全相关工作，打造物联网全生命周期安全生产链，提升物联网体系安全防护能力。');
                leftNavListData(data.data[index].children)
            } else if (index === 5) {
                $('.page-layout').addClass('displayNone');
                $('.experiment').removeClass('displayNone');
                $('.experiment-banner img').attr('src', '../public/images/achievements/banner5.png');
                $('.back-top-text h2').text('物联网安全创新实验室');
                $('.back-top-text p').text('由中国信息通信研究院、中国移动研究院、中国移动江苏公司、中移物联网有限公司组成，集合“产研用”各方力量，全面推进物联网安全相关工作，打造物联网全生命周期安全生产链，提升物联网体系安全防护能力。');
                leftNavListData(data.data[index].children)
            } else if (index === 6) {
                $('.page-layout').addClass('displayNone');
                $('.experiment').removeClass('displayNone');
                $('.experiment-banner img').attr('src', '../public/images/achievements/banner6.png');
                $('.back-top-text h2').text('');
                $('.back-top-text p').text('');
                leftNavListData(data.data[index].children)
            }
        })
    }
    request.run()
}

function getContentByColumnIdt(id) {
    request.url = request.Url + request.getContentByColumnId + '?columnId=' + id
    request.method = "get"
    request.data = {}
    request.redata = function (data) {
        let list = data.data.content
        list.map(function (v, k) {
            if (v.id === 'e440c0ae9dcc4515acde40efcce543cb') {
                $('.lantu_image').css("background-image", 'url(' + JSON.parse(v.content).values.lantu_pic[0].fileDownloadUri + ')')
                $('.lantu_top_title').html(JSON.parse(v.content).values.lantu_title)
                $('.lantu_top_content').html(JSON.parse(v.content).values.lantu_content)
            }
            else if (v.id === '6515dcb5b2d74d05a0406475297a5c6e') {
                $('.peixun_top_title').html(JSON.parse(v.content).values.typx_title)
                $('.peixun_top_content').html(JSON.parse(v.content).values.typx_content)
            }
        })


    }
    request.run()
}

function getContentByColumnId(id, val) {
    request.url = request.Url + request.getContentByColumnId + '?columnId=' + id
    request.method = "get"
    request.data = {}
    request.redata = function (data) {
        let list = data.data.content
        if (val === 1 && list.length > 0) {
            $("#xunshu_center_bottom_oneImg").attr("src", JSON.parse(list[0].listContent).values.icon[0].fileDownloadUri)
            $("#xunshu_center_bottom_twoImg").attr("src", JSON.parse(list[1].listContent).values.icon[0].fileDownloadUri)
            $("#xunshu_center_bottom_threeImg").attr("src", JSON.parse(list[2].listContent).values.icon[0].fileDownloadUri)
            $("#xunshu_center_bottom_fourImg").attr("src", JSON.parse(list[3].listContent).values.icon[0].fileDownloadUri)
            $("#xunshu_center_bottom_one_detail").attr("data-id", list[0].id)
            $("#xunshu_center_bottom_two_detail").attr("data-id", list[1].id)
            $("#xunshu_center_bottom_three_detail").attr("data-id", list[2].id)
            $("#xunshu_center_bottom_four_detail").attr("data-id", list[3].id)
            $(".xunshu_center_bottom_one_title").html(JSON.parse(list[0].listContent).values.title)
            $(".xunshu_center_bottom_two_title").html(JSON.parse(list[1].listContent).values.title)
            $(".xunshu_center_bottom_three_title").html(JSON.parse(list[2].listContent).values.title)
            $(".xunshu_center_bottom_four_title").html(JSON.parse(list[3].listContent).values.title)
            $(".xunshu_center_bottom_two_content").html(JSON.parse(list[0].listContent).values.content)
            $(".xunshu_center_bottom_two_content").html(JSON.parse(list[1].listContent).values.content)
            $(".xunshu_center_bottom_two_content").html(JSON.parse(list[2].listContent).values.content)
            $(".xunshu_center_bottom_two_content").html(JSON.parse(list[3].listContent).values.content)
        }
        else if (val === 2 && list.length > 0) {
            $("#peixun_centerr_one").attr("src", JSON.parse(list[0].listContent).values.imgUrl[0].fileDownloadUri)
            $("#peixun_centerr_two").attr("src", JSON.parse(list[1].listContent).values.imgUrl[0].fileDownloadUri)
            $("#peixun_centerr_three").attr("src", JSON.parse(list[2].listContent).values.imgUrl[0].fileDownloadUri)
            $("#peixun_centerr_ones").attr("data-id", list[0].id)
            $("#peixun_centerr_twos").attr("data-id", list[1].id)
            $("#peixun_centerr_threes").attr("data-id", list[2].id)
            $("#peixun_centerr_one_title").html(JSON.parse(list[0].listContent).values.title)
            $("#peixun_centerr_two_title").html(JSON.parse(list[1].listContent).values.title)
            $("#peixun_centerr_three_title").html(JSON.parse(list[2].listContent).values.title)
            $("#peixun_centerr_one_time").html(JSON.parse(list[0].listContent).values.time)
            $("#peixun_centerr_two_time").html(JSON.parse(list[1].listContent).values.time)
            $("#peixun_centerr_three_time").html(JSON.parse(list[2].listContent).values.time)
        }
    }
    request.run()
}

function menu(id, type) {
    request.url = request.Url + request.getAllColumn
    request.method = "get"
    request.data = {}
    request.redata = function (data) {
        let datas = data.data
        if (type === 1) {
            leftNavListData(datas[2].children, '学术交流');
            listDetailsFun(id, '学术交流', "e62fe90299c545aab6c17a6463d06f54")
            $(".nav3").find(".nav-link").addClass('corBg')
            $(".nav3").siblings().find("a").removeClass('corBg');
            $(".nav3").find(".hov").addClass('bgColor')
            $(".nav3").siblings().find(".hov").removeClass('bgColor');
        }
        else if (type === 2) {
            leftNavListData(datas[5].children, '体验培训')
            listDetailsFun(id, '体验培训', "b29653ccb407440282cbdcba2a8ffaf5")
            $(".nav6").find(".nav-link").addClass('corBg')
            $(".nav6").siblings().find("a").removeClass('corBg');
            $(".nav6").find(".hov").addClass('bgColor')
            $(".nav6").siblings().find(".hov").removeClass('bgColor');
        } else if (type === 3) {
            leftNavListData(datas[4].children, '开放课题')
            listDetailsFun(id, '开放课题', "3bdd46bf47974ac2bc293c7ecf345d3d")
            $(".nav5").find(".nav-link").addClass('corBg')
            $(".nav5").siblings().find("a").removeClass('corBg');
            $(".nav5").find(".hov").addClass('bgColor')
            $(".nav5").siblings().find(".hov").removeClass('bgColor');
        } else if (type === 4) {
            leftNavListData(datas[5].children)
            $(".nav6").find(".nav-link").addClass('corBg')
            $(".nav6").siblings().find("a").removeClass('corBg');
            $(".nav6").find(".hov").addClass('bgColor')
            $(".nav6").siblings().find(".hov").removeClass('bgColor');
        }
    }
    request.run()
}

