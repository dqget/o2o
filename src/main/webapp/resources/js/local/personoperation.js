$(function () {
    let storage = window.sessionStorage;
    let user = storage.getItem('user');
    let getUserInfoUrl = '/o2o/local/islogin';
    if (user == null) {
        getUserInfo()
        //跳转到登录界面
    } else {
        init();

    }

    function getUserInfo() {
        $.getJSON(getUserInfoUrl, function (data) {
            console.log(data);
            if (data.success) {
                // let userJSON = JSON.parse(user);
                storage.setItem("user", JSON.parse(data.data));
                init();

            } else {
                // $('#right').html(notLoginHtml);
                $.toast("请重新登录");
            }
        })
    }


    function init() {
        var userJSON = JSON.parse(user);
        //用户昵称
        $('#name').val(userJSON.name);
        //用户邮箱
        $('#email').val(userJSON.email);
        var userType = '';
        if (userJSON.userType === 1) {
            userType = '顾客';
        } else if (userJSON.userType === 2) {
            userType = '店家';
        } else if (userJSON.userType === 3) {
            userType = '管理员';
        }
        //用户身份
        $('#userType').val(userType);
        //用户性别
        document.getElementById("gender")[userJSON.gender - 1].selected = true;
        $('#profile_Img').attr("src", userJSON.profileImg)
    }
});