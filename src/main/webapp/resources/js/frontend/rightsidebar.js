$(function () {
    var is = isLogin();
    if (is) {
        var rightHtml = '';
        rightHtml += '<p></p><a href=/o2o/local/accountbind?usertype=1>绑定账号</a></p>';
        rightHtml += '<p></p><a href=/o2o/local/changepwd?usertype=1>修改密码</a></p>';
        rightHtml += '<p></p><a id="log-out" href=# usertype="1">退出登录</a></p>';
        $('#right').html(rightHtml);
    } else {

        $('#right').html('<a href=/o2o/local/login?usertype=1 >登录</a>');
    }
});