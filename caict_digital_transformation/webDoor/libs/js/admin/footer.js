/*
 * @Author: your name
 * @Date: 2020-10-23 14:15:26
 * @LastEditTime: 2020-10-29 11:25:13
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webDoor\libs\js\admin\footer.js
 */
  //获取列表数据
  let request = new Call_Request()
  $(function () {
    getDataList(0)
    $('.main_center').on('click', '.more-btn', function () {
        $(this).parent().find('.more').show();
        $(this).hide();
    });
  });
  function getDataList(index) {
    var pageIndex = index + 1;
    var body = {
        "page": pageIndex,
        "size": "5",
        "startTime": '',
        "endTime": ''
    };
    request.method = 'GET',
    request.url = request.UrlPublic + request.footprint,
    request.data = body,
    request.redata = function (data) {
        let total = data.data.totalElements;
        if (total == 0) {
            $('.pagination').hide();
        } else {
            $('.pagination').show();
        }
        $(".currentpage").html(pageIndex);
        $(".totalpage").html(Math.ceil(parseInt(total) / 8));
        const html = template("footer-cell", {content: data.data.content});
        $(".main_center").html(html);
        // //分页-只初始化一次
        if ($(".pagination").html().length == '') {
            $(".pagination").pagination(total, {
                'items_per_page': 5,
                'num_display_entries': 5,
                'num_edge_entries': 5,
                'prev_text': "上一页",
                'next_text': "下一页",
                'callback': pageselectCallback
            });
        }
    }
    request.run()      
}

function pageselectCallback(page_index, jq) {
    getDataList(page_index);
}