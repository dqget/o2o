$(function () {
    var changePwdUrl = '/o2o/local/changelocalpwd';
    var usertype = getQueryString("usertype");

    $('#submit').click(function () {
        var userName = $('#username').val();
        var password = $('#pwd').val();
        var newPassword = $('#newPwd').val();
        var confirmPassword = $('#confirmpwd').val();
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        if (newPassword !== confirmPassword) {
            $.toast('两次输入的新密码不一致！');
            return;
        }

        $.ajax({
            url: changePwdUrl,
            async: false,
            cache: false,
            type: "post",
            dataType: 'json',
            data: {
                userName: userName,
                password: password,
                newPassword: newPassword,
                verifyCodeActual: verifyCodeActual
            },
            success: function (data) {
                if (data.success) {
                    $.toast('修改密码成功！');
                    // if (usertype == 1) {
                    //     window.location.href = '/o2o/frontend/index';
                    // } else {
                    //     window.location.href = '/o2o/shopadmin/shoplist';
                    // }
                    history.back(-1);
                } else {
                    $.toast('修改密码失败！' + data.errMsg);
                    $('#j_captcha').click();
                }
            }
        });
    });
    // $('#back').click(function () {
    //     window.location.href = '/o2o/shopadmin/shoplist';
    // });
});