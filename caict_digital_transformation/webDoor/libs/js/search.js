
let requests = new Call_Request();
$(function () {
    let flag = false;
    $('#input_btn').on('click', function () {
        flag = !flag;
        if (flag) {
            $('.input-text').css({ display: 'block' });
            $('.search-input-style').css({ border: '1px solid #eee', background: 'rgba(0,0,0,1)' });
            $('.input-text').animate({ width: '300px', height: '30px' });
            $('.search-input').animate({ padding: '0 5px' })
        } else {
            $('.input-text').css({ display: 'none' });
            $('.search-input-style').css({ border: 'none', background: 'none' });
            $('.search-input').animate({ padding: '0' })
        }
    })
    $('.input-text').on('keydown', function (e) {
        if (e.keyCode == 13) {
            window.location.href = request.httpUrl + 'searchList.html?keyword=' + window.btoa(escape($(this).val()).replace(/%/g, "\\").toLowerCase());
            // window.location.href = 'http://10.125.22.243:8085/caict_digital_transformation/webDoor/view/seachList.html?keyword=' + window.btoa(escape($(this).val()).replace(/%/g, "\\").toLowerCase());
        }
    })
    // 系统埋点统计
    if (sessionStorage.getItem('id')) {
        addStatistics({ flag: sessionStorage.getItem('id') });
    } else {
        let strID = JSON.stringify(Date.parse(new Date()));
        sessionStorage.setItem('strId', strID);
        addStatistics({ flag: sessionStorage.getItem('strId') });
    }
    function addStatistics(body) {
        request.method = 'POST';
        request.url = request.UrlPublic + 'web/statistic/insertSystemVisitUser';
        request.data = JSON.stringify(body);
        request.redata = function (res) {
            if (res.code == 1) {
                // 统计访问量添加数
            }
        };
        request.run();
    }
})
