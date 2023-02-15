/*
 * @Author: your name
 * @Date: 2020-10-23 14:53:02
 * @LastEditTime: 2020-10-30 16:57:11
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
  let pageIndex = index + 1;
  let body = {
    "page": pageIndex,
    "size": "8",
    "startTime": '',
    "endTime": '',
    'type': type
  };
  request.method = 'GET';
  request.url = request.UrlPublic + request.collect;
  request.data = body;
  request.redata = function (data) {

    let total = data.data.totalElements;
    if (total == 0) {
      $('.pagination').hide();
    } else {
      $('.pagination').show();
    }
    $(".currentpage").html(pageIndex);
    $(".totalpage").html(Math.ceil(parseInt(total) / 8));
    if (type == 0) {
      const html = template("collect-cell", { content: data.data.content });
      $(".list").empty();
      $(".list").append(html);
    } else {
      const htmls = template("collectArc-cell", { content: data.data.content });
      $(".listArc").empty();
      $(".listArc").append(htmls);
    };
    //分页-只初始化一次
    if ($(".pagination").html().length == '') {
      $(".pagination").pagination(total, {
        'items_per_page': 8,
        'num_display_entries': 8,
        'num_edge_entries': 8,
        'prev_text': "上一页",
        'next_text': "下一页",
        'callback': pageselectCallback
      });
    }
  };
  request.run();
}

function pageselectCallback(page_index, jq) {
  getDataList(page_index, type);
}

function showList() {
  $('.list').show();
  $('.list-content').hide();
  type = 0;
  getDataList(0, type);
  $('.fw').addClass('active');
  $('.wz').removeClass('active');
}
function listContent() {
  $('.list').hide();
  $('.list-content').show();
  type = 1;
  getDataList(0, type);
  $('.wz').addClass('active');
  $('.fw').removeClass('active');
}
function calCollect(id) {
  $.ajax({
    type: "DELETE",
    url: request.UrlPublic + request.delCollect + '?cmsContentId=' + id,
    headers: {
      "Authorization": "token " + window.sessionStorage.getItem('token')
    },
    contentType: 'application/json; charset=utf-8',
    data: {},
    success: function (response) {
      if (response.code == 1) {
        getDataList(0, 0);
      }
    }
  });
}
