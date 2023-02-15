
/*****************************************************************
 *
 * 直接上传图片
 *
 */
//$(window).load(function(){
var pictureName=null;
var a=null;
var s=null;
var x=0;
//选择图片
function selectImage(name) {

    pictureName = name;
    $("#imageUploadBtn").click();
}

//上传图片
function uploadImg(file)
{
    if (!/..jpg|..gif|..png|..bmp|..jpeg/i.test(file.value)) {
        z012.alert("文件类型应为[jpg,gif,png,bmp,jpeg]其中之一");
        return;
    }
    if( $("img").attr('src')===undefined ){
        a=file.files[0];
        Img(file);
    }else{
        x++;
        a=file.files[0];
        Img(file);
    }

}
function Img(file){
    var im = new Image();
    var _URL = window.URL || window.webkitURL;
    im.src = _URL.createObjectURL(file.files[0]);
    im.onload = function () {
        var width = this.width;
        var height = this.height;
        var image="";
        var reader = new FileReader();
        reader.onload = function (evt) {
            image  = evt.target.result;

        };
        reader.readAsDataURL(file.files[0]);
        $("#uploadImgForm").ajaxSubmit({
            url: $.url.CosFile_Angle,
            headers:{"Authorization":"Bearer " + $.cookie("accessToken")},
            //headers:{"Authorization":"Bearer 83bbc4e2f2844dedb2cae83e8821d21f"},
            type: "post",
            success: function (data) {
                if(data.Path){
                    $("#photo").show();
                    z012.alert("上传图片大小为:"+width+"*"+height+"");
                    //$("#sel_Photo").css({"margin-top":"30px"});
                    //$("#video_play").attr("src");
                    $("#photo").attr("src",image);
                    $("#"+pictureName).val(data.Path);
                    //console.log("///////////0////////"+$("#video_play").attr("src"));
                    //console.log("/////////////////////"+$("#video_play_html5_api").attr('src'));
                    //if( x!==0&&$("#video_play_html5_api").attr('src')!==undefined){
                    //    update1(a);
                    //}
                }
            },
            error: function (request, status, error) {
                if(!request.responseText) {
                    request.responseText="系统错误";
                }else{
                    request.responseText=JSON.parse(request.responseText).error;
                }
                z012.alert(request.responseText);
            }
        });
    }
}

//选择文件事件
function selectUploadFile(n,t,index) {
    $("#file").click();
}
//function update1(a){
//    var trans=null;
//    if(JSON.parse($("#Url").val()).fileId!==undefined){
//        trans=JSON.parse($("#Url").val()).fileId;
//    }
//    else{
//        trans=$("#Url").val()
//    }
//
//    qcVideo.ugcUploader.start({
//        fileId: trans,
//        coverFile: a,
//        getSignature: getSignature,
//        success: function(result){
//            $("#photo").show();
//
//        },
//        error: function(result){
//            z012.alert("图片更新失败");
//        },
//        progress: function(result){
//            result.curr = Math.ceil(result.curr*100);
//            $("#pro").html( result.curr +"%" ).css("width" , result.curr +"%");
//
//        },
//        finish: function(result){
//            //console.log('结果' + result.message);
//
//        }
//    })
//
//
//}
//上传
function update(a,callback){
    var trans=null;
    if(JSON.parse($("#Url").val()).fileId!==undefined){
        trans=JSON.parse($("#Url").val()).fileId;
    }
    else{
        trans=$("#Url").val()
    }

    qcVideo.ugcUploader.start({
        fileId: trans,
        coverFile: a,
        getSignature: getSignature,
        success: function(result){
            $("#photo").show();
            callback();
        },
        error: function(result){
            z012.alert("图片更新失败");
        },
        progress: function(result){
            result.curr = Math.ceil(result.curr*100);
            $("#pro").html( result.curr +"%" ).css("width" , result.curr +"%");

        },
        finish: function(result){
            //console.log('结果' + result.message);

        }
    })


}
function upload(file)
{
    //if (!/..wmv|..mv|..asf|..asx|..rm|..rmvb|..ra|..ram|..mpg|..mpeg|..mpe|..vob|..dat|..mov|..3gp|..mp4|..mp4v|..m4v|..mkv|..avi|..flv|..f4v/i.test(file.value)) {
    //    z012.alert("请上传视频格式文件");
    //    return;
    //}
    $("#sel_File2").hide(); //上传按钮隐藏

    $("#uploadForm").ajaxSubmit({
        url: $.url.Vodup,
        headers:{"Authorization":"Bearer " + $.cookie("accessToken")},
        //headers:{"Authorization":"Bearer 83bbc4e2f2844dedb2cae83e8821d21f"},
        type: "post",
        timeout:50000,
        success: function (data) {
            var j=0;
            var i=0;
            res=JSON.parse(data.FileId);
            //console.log(data.FileId);
            //console.log(res.url);
            var timer=setInterval(function(){
                i++;
                $("#pro").html(i +"%" ).css("width" , i+"%");
                if(i===101){

                    //$("#pro").html(i +"%" ).css("width" , i+"%");
                    clearInterval(timer);
                    $("#progress").hide();

                    $("#video_play").attr("src",res.url);
                    $("#Url").val(data.FileId);
                    alert("上传成功");
                    $("#video_play") .show();
                    $("#video_play_html5_api") .show();
                }

            },10);
            var player = TCPlayer('video_play', { // player-container-id 为播放器容器ID，必须与html中一致
                fileID: res.fileId, // 请传入需要播放的视频filID 必须
                appID: '1254182407' // 请传入点播账号的appID 必须
                //其他参数请在开发文档中查看
            });
        },
        error: function (request, status, error) {
            if(!request.responseText) {
                request.responseText="系统错误";
            }else{
                request.responseText=JSON.parse(request.responseText).error;
            }
            z012.alert(request.responseText);
        }
    });
}
let requestt = new Call_Request()
function upload1(file)
{
    //if (!/..wmv|..mv|..asf|..asx|..rm|..rmvb|..ra|..ram|..mpg|..mpeg|..mpe|..vob|..dat|..mov|..3gp|..mp4|..mp4v|..m4v|..mkv|..avi|..flv|..f4v/i.test(file.value)) {
    //    z012.alert("请上传视频格式文件");
    //    return;
    //}
    $('#yl').hide()
    $("#uploadForm1").ajaxSubmit({
        url: requestt.UrlPublic + requestt.uploadLogo,
        headers:{
            "Authorization": "token " + window.sessionStorage.getItem('token'),
        },
        type: "post",
        timeout:50000,
        success: function (data) {
            $('#yl').show()
            console.log(data.fileDownloadUri)
            $('#yl').attr('src',requestt.UrlPublic + data.fileDownloadUri)
        },
        error: function (request, status, error) {
            // $(".btn-primary").button("reset");
            // if(!request.responseText) {
            //     request.responseText="系统错误";
            // }else{
            //     request.responseText=JSON.parse(request.responseText).error;
            // }
            // z012.alert(request.responseText);
        }
    });
}
//获取签名
var getSignature = function(callback){
    $.ajax({
        url: $.url.VodSign,  //获取客户端上传签名的 URL
        type: 'POST',
        //dataType: 'json',
        success: function(result){//result 是派发签名服务器的回包
            //假设回包为 { "code": 0, "signature": "xxxx"  }
            //将签名传入 callback，SDK 则能获取这个上传签名，用于后续的上传视频步骤。
            callback(result);
        }
    });
}

//qcVideo.ugcUploader.start({
//    videoFile: file.files[0],//视频，类型为 File
//    coverFile: a,
//    getSignature: getSignature,//前文中所述的获取上传签名的函数
//    progress: function(result){
//        console.log('上传进度：' + result.curr);
//        result.curr = Math.ceil(result.curr*100);
//        $("#pro").html( result.curr +"%" ).css("width" , result.curr +"%");
//    },
//    error: function(result){//上传失败时的回调函数
//        //...
//        console.log('上传失败的原因：' + result.msg);
//    },
//    finish: function(result){//上传成功时的回调函数
//        //g= result.fileId;
//        console.log('上传结果的fileId：' + result.fileId);
//        console.log('上传结果的视频名称：' + result.videoName);
//        console.log('上传结果的视频地址：' + result.videoUrl);
//        $("#progress").hide();
//        $("#Url").val(result.fileId);
//        //播放视频
//        $("#video_play") .show();
//        //var player = new TcPlayer('video_play',{
//        //    //"m3u8": result.videoUrl,
//        //    fileID: result.fileId, // 请传入需要播放的视频filID 必须
//        //    appID: '1254182407', // 请传入点播账号的appID 必须
//        //    "width" :  '480',//视频的显示宽度，请尽量使用视频分辨率宽度
//        //    "height" : '320'//视频的显示高度，请尽量使用视频分辨率高度
//        //});
//        var player = TCPlayer('video_play', { // player-container-id 为播放器容器ID，必须与html中一致
//
//            fileID: result.fileId, // 请传入需要播放的视频filID 必须
//            appID: '1254182407' // 请传入点播账号的appID 必须
//            //其他参数请在开发文档中查看
//        });
//    }
//});




