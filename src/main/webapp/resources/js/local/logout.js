$(function () {
    $('#log-out').click(function () {
        $.ajax({
            url: "/o2o/local/logout",
            type: "post",
            contentType: false,
            processData: false,
            success: function (data) {
                if (data.success) {
                    var userType = $('#log-out').attr("usertype");
                    window.location.href = '/o2o/local/login?usertype=' + userType;
                }
            },
            error: function (data, error) {
                alert(error);
            }
        });
    });
});