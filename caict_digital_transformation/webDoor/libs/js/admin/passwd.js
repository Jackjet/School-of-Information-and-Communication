/*
 * @Author: your name
 * @Date: 2020-10-29 16:06:34
 * @LastEditTime: 2020-11-04 21:36:46
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webDoor\libs\js\admin\passwd.js
 */
  //获取列表数据
  let request = new Call_Request();
  layui.use('layer', function(){
    var layer = layui.layer;
  });  
  let type = ''
  $(function () {
   
  });
 
  function sumitPwd(index) {
    if($('#phone').val() === ''){
        return layer.msg("手机号码不能为空!");
    }
    if(!/^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/g.test($('#phone').val())){
        return layer.msg("请输入正确的手机号码!");
    }
    if($('#code').val() === ''){
        return layer.msg("验证码不能为空!");
    }
    if($('#newPwd').val() === ''){
        return layer.msg("新密码不能为空!");
    }
 
    let body = JSON.stringify({
        "phone": $('#phone').val(),
        "password": hex_md5($('#newPwd').val()),
        "code": $('#code').val(),
    });
    request.method = 'put',
    request.url = request.UrlPublic + request.updatePwd,
    request.data = body,
    request.redata = function (data) {
      if(data.code == 1){
         layer.msg("修改成功!");
         tcLogin()
      }else{
        layer.msg("修改失败!");
      }
      
    }
    request.run()
        
  }

  // 发送验证码
function send(seconds){
    request.url = request.UrlPublic + request.code
    request.method = "post"
    request.data = JSON.stringify({
        phone: $('#phone').val(),
        type: 1,
    })
    request.redata = function (data) {
        if(data.code === 1){
            $('#sendbtn').html(seconds + "s后获取");
            let countdown = setInterval(function () {
                if (seconds > 0) {
                    --seconds;
                    $('#sendbtn').html(seconds + "s后获取")             
                } else {
                    $('#sendbtn').html("获取验证码");
                    $('#sendbtn').attr("disabled",false);
                    clearInterval(countdown);
                }
            }, 1000);
            layer.msg(data.msg);
        }
        else{
            return layer.msg(data.msg);
        }
       
    }
    request.run()
}

function btnsend(){
    if($('#phone').val() === ''){
        return layer.msg("手机号码不能为空!");
    }
    if(!/^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/g.test($('#phone').val())){
        return layer.msg("请输入正确的手机号码!");
    }
    
    $('#sendbtn').html("请稍候...")
    $('#sendbtn').attr("disabled",true);
    setTimeout(function () {
        this.send(60)
    }, 500)
}

function tcLogin(){
    window.sessionStorage.setItem('id', '')
    window.sessionStorage.setItem('token', '')
    window.top.location.href = '../../../view/login.html'
}