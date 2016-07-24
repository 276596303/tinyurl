/**
 * Created by YanYang on 2016/7/23.
 */
$(function () {

    $("input#name").blur(function () {
        $("div#nameForm label").html("用户名");
        var name = $("input#name").val();
        $.post('/user/exist', {username: name}, function (result) {
            if (result) {
                $("div#nameForm").addClass("has-error");
                $("div#nameForm label").html("用户名已存在");
                $("button#getTokenBtn").attr({"disabled": "disabled"});
            } else {
                $("div#nameForm label").html("用户名");
                $("div#nameForm").removeClass();
                $("div#nameForm").addClass("form-group");
                $("button#getTokenBtn").removeAttr("disabled");
            }
        });
    });


    $("button#getShort").click(function () {
        $("#urlShow").html("");

        var longUrl = $("input#url").val();

        $.post('/short', {username: "", password: "", longUrl: longUrl}, function (result) {
            var success = result.code;
            if (success == 1) {
                $("#urlShow").append("<div class='panel panel-success'>" +
                    "<div class='panel-heading'>" +
                    "<h5>" + "短网址" + " " +
                    "<a id = 'shortUrl' href='" + result.message + "'>" + result.message +
                    "</a>" +
                    "</h5>" +
                    "</div>");
            } else {
                $("#urlShow").append(result.message);
            }
        });
    });


});
