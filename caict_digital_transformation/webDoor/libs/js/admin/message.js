/*
 * @Author: your name
 * @Date: 2020-10-26 14:10:15
 * @LastEditTime: 2020-10-30 18:00:43
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webDoor\libs\js\admin\active.js
 */
 //获取列表数据
 var readNum = 0; 
 var unreadNum = 0; 
 let request = new Call_Request()
 let type = ''
 $(function () {
   getNum(0);
   getNum(1);
   setTimeout(function () {
     type = 0;
     getDataList(0, type);
    $('.list-content').on('click', '.list-item-active', function () {
      if ($(this).find('.neirong').is(':hidden')) {
        $(this).find('.neirong').show();
      } else {
        $(this).find('.neirong').hide();
      }
      if ($('.noread').hasClass('active') && !($(this).find('.list-item-content').hasClass('doRead'))) {
        $(this).find('.list-item-content').addClass('doRead');
        readNum++;
        unreadNum--;
        $('.yread').html('已读消息('+ readNum +')');
        $('.noread').html('未读消息('+ unreadNum +')');
        changeRead($(this).data('id'));
      }
    });
   }, 500);
 });
function changeRead(id) {
  $.ajax({
    type:"PUT",
    url: request.UrlPublic + request.updateMessage,
    headers: {
        "Authorization": "token " + window.sessionStorage.getItem('token')
    },
    contentType: 'application/json; charset=utf-8',
    data: JSON.stringify({id: id}),
    success: function (response) {
      if (response.code == 1) {
        console.log(1);
      }
    }
  });
}
function getNum(type) {
  var body = {
    "page": 1,
    "size": "1",
    'status': type
  };
  request.method = 'GET';
  request.url = request.UrlPublic + request.message;
  request.data = body;
  request.redata = function (data) {
    let total = data.data.totalElements;
    if (type == 1) {
      readNum = total;
      $('.yread').html('已读消息('+ readNum +')');
    } else {
      unreadNum = total;
      $('.noread').html('未读消息('+ unreadNum +')');
    }
  };
  request.run();
}
 function getDataList(index, type) {
   var pageIndex = index + 1;
   var body = {
       "page": pageIndex,
       "size": "5",
       'status': type
   };
   request.method = 'GET';
   request.url = request.UrlPublic + request.message;
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
    const html = template("message-cell", {content: data.data.content});
    $(".message").html(html);
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
   };
   request.run();
}
function pageselectCallback(page_index, jq) {
  getDataList(page_index, type);
}
function noRead(){
  type = 0;
  getDataList(0, type);
  $('.noread').addClass('active');
  $('.yread').removeClass('active');
}
function read(){
  type = 1;
  getDataList(0, type);
  $('.yread').addClass('active');
  $('.noread ').removeClass('active');
}