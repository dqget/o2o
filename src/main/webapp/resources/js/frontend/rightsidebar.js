$(function () {
    var storage = window.localStorage;
    var user = storage.getItem('user');
    if (user != null && user !== undefined) {
        var rightHtml = '';
        rightHtml += '<p></p><a class="in external" href=/o2o/local/accountbind?usertype=1>绑定账号</a></p>';
        rightHtml += '<p></p><a class="in external" href=/o2o/local/personoperation>个人信息</a></p>';
        rightHtml += '<p></p><a class="in external" href=/o2o/frontend/orderlist>订单列表</a></p>';
        rightHtml += '<p></p><a class="in external" href=/o2o/local/changepwd?usertype=1>修改密码</a></p>';
        rightHtml += '<p></p><a class="in external" id="log-out" href=# usertype="1">退出登录</a></p>';
        $('#right').html(rightHtml);
    } else {
        $('#right').html('<a href=/o2o/local/login?usertype=1 >登录</a>');
    }
});