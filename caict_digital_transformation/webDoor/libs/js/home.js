let request = new Call_Request();
$(function () {
    // getAllColumn()
    getContentByColumnId('e376660b9db940909f0cd813ebf52c5e')
    getContentByColumnIdt('624a0f7e786c445da9e151ca4af483b8')
    // getContentByColumnIdtb('6478d53406b54beabd957b8ef92e3b5b')
    var $scroll = 0;
    $('body').on('scroll', function () {
        $scroll = $(this).scrollTop();
        if ($scroll < 710) {
            $('.louti-box li').siblings().removeClass('nowFlow');
            $('.louti-box li').eq(0).addClass('nowFlow');
        } else if ($scroll > 710 && $scroll < 1420) {
            $('.louti-box li').siblings().removeClass('nowFlow');
            $('.louti-box li').eq(1).addClass('nowFlow');
        } else if ($scroll > 1420) {
            $('.louti-box li').siblings().removeClass('nowFlow');
            $('.louti-box li').eq(2).addClass('nowFlow');
        }
    });
    $('.louti-box').hover(function () {
        $('body').unbind();
    }, function () {
        $('body').on('scroll', function () {
            $scroll = $(this).scrollTop();
            if ($scroll < 100) {
                $('.louti-box li').siblings().removeClass('nowFlow');
                $('.louti-box li').eq(0).addClass('nowFlow');
            } else if ($scroll >= 710 && $scroll < 1420) {
                $('.louti-box li').siblings().removeClass('nowFlow');
                $('.louti-box li').eq(1).addClass('nowFlow');
            } else if ($scroll > 1420) {
                $('.louti-box li').siblings().removeClass('nowFlow');
                $('.louti-box li').eq(2).addClass('nowFlow');
            }
        });
    })
    $('.louti-box li').on('click', function () {
        $scroll = $(this).index() * 710;
        $(this).siblings().removeClass('nowFlow');
        $(this).addClass('nowFlow');
        $('body').animate({
            scrollTop: $scroll
        });
    });
})
function getAllColumn() {
    request.url = request.Url + request.getAllColumn;
    request.method = "get";
    request.data = {};
    request.redata = function (data) {
        const html = template("nav-cell", { content: data.data });
        $(".nav").html(html);
    };
    request.run();
}
function getContentByColumnId(id) {
    request.url = request.Url + request.getContentByColumnId + '?columnId=' + id;
    request.method = "get";
    request.data = {};
    request.redata = function (data) {
        let datas = data.data.content;
        $('.hexin').html(JSON.parse(datas[0].listContent).values.cumlTitle);
        $('.hexin_contant').html(JSON.parse(datas[0].listContent).values.content);
        $('.qiye').html(JSON.parse(datas[1].listContent).values.cumlTitle);
        $('.qiye_contant').html(JSON.parse(datas[1].listContent).values.content);
        $('.pince').html(JSON.parse(datas[2].listContent).values.cumlTitle);
        $('.pince_contant').html(JSON.parse(datas[2].listContent).values.content);
        $('.chanye').html(JSON.parse(datas[3].listContent).values.cumlTitle);
        $('.chanye_contant').html(JSON.parse(datas[3].listContent).values.content);
        $('.chuangxin').html(JSON.parse(datas[4].listContent).values.cumlTitle);
        $('.chuangxin_contant').html(JSON.parse(datas[4].listContent).values.content);

    };
    request.run();
}
function getContentByColumnIdt(id) {
    request.url = request.Url + request.getContentByColumnId + '?columnId=' + id;
    request.method = "get";
    request.data = {};
    request.redata = function (data) {
        let content = [];
        data.data.content.map(function (v, k) {
            let list = JSON.parse(v.listContent).values;
            content.push({ 'image': list.img[0].fileDownloadUri, 'titleOne': list.titileOne, 'titleTwo': list.titleTwo, 'titleThree': list.titleThree });
        });
        const html = template("swiper-cell", { content: content });
        $("#top_swiper").html(html);
        let swiper = new Swiper('.swiper-container', {
            loop: true,
            autoplay: true,
            pagination: {
                el: '.swiper-pagination',
            },
        });

    };
    request.run();
}
function getContentByColumnIdtb(id) {
    request.url = request.Url + request.getContentByColumnId + '?columnId=' + id;
    request.method = "get";
    request.data = {};
    request.redata = function (data) {
        let content = [];
        data.data.content.map(function (v, k) {
            let list = JSON.parse(v.listContent).values;
            content.push({ 'image': list.img[0].fileDownloadUri });
        });

        $("#certify_img_0").attr("src", content[0].image);
        $("#certify_img_1").attr("src", content[1].image);
        $("#certify_img_2").attr("src", content[2].image);
        $("#certify_img_3").attr("src", content[3].image);
        // $("#certify_img_five").attr("src",content[4].image);
    };
    request.run();
}

function go(url) {
    window.location.href = request.httpUrl + url;
}
function goMy() {
    if (window.sessionStorage.getItem('token')) {
        window.location.href = '../../admin/index.html'
    } else {
        window.location.href = '../../login.html';
    }
}