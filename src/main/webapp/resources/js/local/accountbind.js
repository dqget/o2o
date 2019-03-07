$(function () {
    var bindUrl = '/o2o/local/bindlocalauth';
    var userType = getQueryString("usertype");
    $('#submit').click(function () {
        var userName = $('#username').val();
        var password = $('#pwd').val();
        var verifyCodeActual = $('#j_captcha').val();
        var needVerify = false;
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        $.ajax({
            url: bindUrl,
            async: false,
            cache: false,
            type: "post",
            dataType: 'json',
            data: {
                userName: userName,
                password: password,
                verifyCodeActual: verifyCodeActual
            },
            success: function (data) {
                if (data.success) {
                    $.toast('绑定成功！');
                    if (userType == 1) {
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        window.location.href = '/o2o/shopadmin/shoplist';

                    }
                } else {
                    $.toast(data.errMsg);
                    $('#captcha_img').click();
                }
            }
        });
    });
});