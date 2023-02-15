/*
 * @Author: your name
 * @Date: 2020-10-23 14:53:02
 * @LastEditTime: 2020-10-30 17:08:39
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webDoor\libs\js\admin\collect.js
 */
/*
* @Author: your name
* @Date: 2020-10-23 14:15:26
* @LastEditTime: 2020-10-27 18:19:44
* @LastEditors: Please set LastEditors
* @Description: In User Settings Edit
* @FilePath: \webDoor\libs\js\admin\footer.js
*/
//获取列表数据
let request = new Call_Request();
let type = '';
$(function () {
    type = 0;
    getDataList(0, type);
});
function getDataList(index, type) {
    var pageIndex = index + 1;
    var body = {
        "page": pageIndex,
        "size": "5",
        "startTime": '',
        "endTime": '',
        'type': type
    };
    request.method = 'GET';
    request.url = request.UrlPublic + request.subscribe;
    request.data = body;
    request.redata = function (data) {
        let total = data.data.totalElements;
        $('.totals').html(total);
        $("#data").empty();
        if (total == 0) {
            $('.pagination').hide();
        } else {
            $('.pagination').show();
        }
        $(".currentpage").html(pageIndex);
        $(".totalpage").html(Math.ceil(parseInt(total) / 8));
        const html = template("order-cell", { content: data.data.content });
        $("#order_list").html(html);
        // //分页-只初始化一次
        if ($(".pagination").html().length == '') {
            $(".pagination").pagination(total, {
                'items_per_page': 2,
                'num_display_entries': 2,
                'num_edge_entries': 2,
                'prev_text': "上一页",
                'next_text': "下一页",
                'callback': pageselectCallback
            });
        }
    };
    request.run();
}

function pageselectCallback(page_index, jq) {
    // getDataList(page_index, type);
}

function cydt() {
    type = 0;
    getDataList(0, type);
    $('.cydtTitle').addClass('active');
    $('.hyzxTitle').removeClass('active');
    $('.zcjdTitle').removeClass('active');
    $('.hdTitle').removeClass('active');
}
function hyzx() {
    type = 1;
    getDataList(0, type);
    $('.cydtTitle').removeClass('active');
    $('.hyzxTitle').addClass('active');
    $('.zcjdTitle').removeClass('active');
    $('.hdTitle').removeClass('active');
}
function zcjd() {
    type = 2;
    getDataList(0, type);
    $('.cydtTitle').removeClass('active');
    $('.hyzxTitle').removeClass('active');
    $('.zcjdTitle').addClass('active');
    $('.hdTitle').removeClass('active');
}
function hd() {
    type = 3;
    getDataList(0, type);
    $('.cydtTitle').removeClass('active');
    $('.hyzxTitle').removeClass('active');
    $('.zcjdTitle').removeClass('active');
    $('.hdTitle').addClass('active');
}