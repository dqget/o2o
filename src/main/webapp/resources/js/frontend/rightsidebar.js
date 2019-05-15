$(function () {

    let checkUserLoginUrl = '/o2o/local/islogin';
    let storage = window.sessionStorage;
    const loginHtml =
        '<p></p><a class="in external" href=/o2o/local/accountbind?usertype=1>绑定账号</a></p>'
        + '<p></p><a class="in external" href=/o2o/local/personoperation>个人信息</a></p>'
        + '<p></p><a class="in external" href=/o2o/frontend/orderlist>订单列表</a></p>'
        + '<p></p><a class="in external" href=/o2o/frontend/schedulelist>预定记录</a></p>'
        + '<p></p><a class="in external" href=/o2o/frontend/pointrecord>兑换记录</a></p>'
        + '<p></p><a class="in external" href=/o2o/local/changepwd?usertype=1>修改密码</a></p>'
        + '<p></p><a class="in external" id="log-out" href=# usertype="1">退出登录</a></p>';
    const notLoginHtml = '<a href=/o2o/local/login?usertype=1 >登录</a>';
    checkUserLogin();

    $('#me').click(function () {
        $.openPanel('#panel-js-demo');
    });
    $.init();

    function checkUserLogin() {
        let user = storage.getItem('user');

        if (user != null && user !== undefined) {
            $('#right').html(loginHtml);
        } else {
            $.getJSON(checkUserLoginUrl, function (data) {
                console.log(data);
                if (data.success) {
                    $('#right').html(loginHtml);
                    storage.setItem("user", JSON.stringify(data.data))
                } else {
                    $('#right').html(notLoginHtml);
                }
            })
        }

    }


});