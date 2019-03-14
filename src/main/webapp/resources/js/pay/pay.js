$(function () {
    $("#sbumitBtn").on('click', function () {
        $.ajax({
            type: "post",
            data: {
                WIDout_trade_no: $('#out_trade_no').val(),
                WIDsubject: $('#WIDsubject').val(),
                WIDtotal_amount: $('#WIDtotal_amount').val(),
                WIDbody: $('#WIDbody').val()
            },
            url: "/o2o/pay/open",
            success: function (data) {
                $('#returnAli').append(data);
            },
            error: function (da) {
            }
        });
    })
});