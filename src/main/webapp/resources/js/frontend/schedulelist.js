$(function () {
    const getScheduleListUrl = '/o2o/frontend/getschedulelist?pageIndex=1&pageSize=999&productNum=';
    const oldScheduleOpenPayUrl = '/o2o/frontend/oldscheduleopenpay?scheduleId=';
    let productName = '';
    getSchedule();

    function getSchedule() {
        $.getJSON(getScheduleListUrl + productName, function (data) {
            if (data.success) {
                let scheduleList = data.data.scheduleList;
                let html = '';
                scheduleList.map(function (item, index) {
                    html += '<div class="card" data-schedule-id=" '
                        + item.scheduleId + '"data-schedule-is-pay="' + checkPay(item) + '">'
                        + '<div class="card-header">'
                        + item.shop.shopName + '<div class="item-after">'
                        + new Date(item.createTime).Format("yyyy-MM-dd") + '</div>' + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">'
                        + '<ul>' + '<li class="item-content row">'
                        + '<div class="item-media col-33">'
                        + item.product.productName + '</div>'
                        + '<div class="item-inner">'
                        + (checkPay(item) ? '已支付' : '未支付')
                        + '</div>' + '</li>' + '</ul>' + '</div>' + '</div>'
                        + '<div class="card-footer">'
                        + '<p class="color-gray">共计 ' + item.payPrice + '￥</p>'
                        + '<span>共' + item.amountDay + '次，一次' + item.dailyQuantity + '件</span>'
                        + '<span>' + (checkPay(item) ? '查看配送情况' : '点击支付') + '</span>' + '</div>' + '</div>';
                });
                $(".list-div").append(html);
            }
        })
    }

    //失去焦点时执行
    $('#search').blur(function (e) {
        productName = e.target.value;
        $('.list-div').empty();
        getSchedule();
    });

    function checkPay(schedule) {
        if (schedule.isPay == 1) {
            return true;
        } else {
            return false;
        }
    }

    $('.list-div').on('click', '.card', function (e) {
        let isPay = e.currentTarget.dataset.scheduleIsPay;
        let scheduleId = e.currentTarget.dataset.scheduleId;
        if (isPay == 'true') {
            window.location.href = '/o2o/frontend/scheduledistributionlist?scheduleId=' + scheduleId;
        } else {
            let url = oldScheduleOpenPayUrl + scheduleId;
            $.ajax({
                url: url,
                type: "POST",
                contentType: 'application/json;charset=utf-8',
                success: function (data) {
                    $('#returnAli').append(data);
                    $("#returnAli script").remove();
                    var queryParam = '';
                    Array.prototype.slice.call(document.querySelectorAll("input[type=hidden]")).forEach(function (ele) {
                        queryParam += ele.name + "=" + encodeURIComponent(ele.value) + '&';
                        sessionStorage.setItem("scheduleId", JSON.parse(ele.value).out_trade_no);
                    });
                    let url = document.getElementsByName("punchout_form")[0].action + '&' + queryParam;
                    _AP.paySchedule(url);
                }
            });
        }


        // console.log(scheduleId);
    });
});