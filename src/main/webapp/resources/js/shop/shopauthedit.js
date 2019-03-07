$(function () {
    var shopAuthId = getQueryString('shopAuthId');
    //获取授权信息
    var infoUrl = '/o2o/shopadmin/getshopauthmapbyid?shopAuthId=' + shopAuthId;
    //修改信息Url
    var shopAuthPostUrl = '/o2o/shopadmin/modifyshopauthmap';


    if (shopAuthId) {
        getInfo(shopAuthId);
    } else {
        $.toast('用户不存在！');
        window.location.href = '/o2o/shopadmin/shopmanage';
    }

    function getInfo(shopAuthId) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                var shopAuthMap = data.data;
                $('#shopauth-name').val(shopAuthMap.employee.name);
                $('#title').val(shopAuthMap.title);
            }
        })
    }

    $('#submit').click(function () {
        var shopAuth = {};
        var employee = {};
        shopAuth.employee = employee;
        shopAuth.employee.name = $('#shopauth-name').val();
        shopAuth.title = $('#title').val();
        shopAuth.shopAuthId = shopAuthId;
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码');
            return;
        }
        $.ajax({
            url: shopAuthPostUrl,
            type: 'POST',
            conditionText: "application/x-www-form-urlencoded;charset=utf-8",
            data: {
                shopAuthMapStr: JSON.stringify(shopAuth),
                verifyCodeActual: verifyCodeActual,
            },
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功');
                    $('#j_captcha').click();
                } else {
                    $.toast('提交失败,' + data.msg);
                    $('#j_captcha').click();
                }
            }
        })
    });
});
