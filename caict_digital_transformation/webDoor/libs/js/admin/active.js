/*
 * @Author: your name
 * @Date: 2020-10-26 14:10:15
 * @LastEditTime: 2020-10-30 17:20:15
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webDoor\libs\js\admin\active.js
 */
//获取列表数据
let request = new Call_Request()
$(function () {
  getDataList(0)
});
function getDataList(index) {
  var pageIndex = index + 1;
  var body = {
    "page": pageIndex,
    "size": "3",
  };
  request.method = 'GET',
    request.url = request.UrlPublic + request.myactiv,
    request.data = body,
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
      data.data.content.map(function (v, k) {
        v.pic = request.UrlPublic + v.activityPic
      })
      const html = template("active-cell", { content: data.data.content });
      $(".activeList").html(html);
      // //分页-只初始化一次
      if ($(".pagination").html().length == '') {
        $(".pagination").pagination(total, {
          'items_per_page': 3,
          'num_display_entries': 3,
          'num_edge_entries': 3,
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
