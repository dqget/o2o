$(function () {
    var storage = window.localStorage;
    var user = storage.getItem('user');
    if (user == null) {
        //跳转到登录界面
        window.location.href = '';
    }
    init();

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