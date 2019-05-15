$(function () {
    const getScheduleListUrl = '/o2o/frontend/getschedulelist?pageIndex=1&pageSize=999&productNum=';
    let productName = '';
    getSchedule();

    function getSchedule() {
        $.getJSON(getScheduleListUrl + productName, function (data) {
            if (data.success) {
                let scheduleList = data.data.scheduleList;
                let html = '';
                scheduleList.map(function (item, index) {
                    html += '<div class="card" data-schedule-id="'
                        + item.scheduleId + '">'
                        + '<div class="card-header">'
                        + item.shop.shopName + new Date(item.createTime).Format("yyyy-MM-dd") + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">'
                        + '<ul>' + '<li class="item-content">'
                        + '<div class="item-media">'
                        + item.product.productName + '</div>'
                        + '<div class="item-inner">'
                        + '更新时间' + new Date(item.updateTime).Format("yyyy-MM-dd")
                        + '</div>' + '</li>' + '</ul>' + '</div>' + '</div>'
                        + '<div class="card-footer">'
                        + '<p class="color-gray">共计 ' + item.payPrice + '￥</p>'
                        + '<span>共' + item.amountDay + '次，一次' + item.dailyQuantity + '件</span>'
                        + '<span>查看配送情况</span>' + '</div>' + '</div>';
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

    $('.list-div').on('click', '.card', function (e) {
        let scheduleId = e.currentTarget.dataset.scheduleId;
        // console.log(scheduleId);
        window.location.href = '/o2o/frontend/scheduledistributionlist?scheduleId=' + scheduleId;
    });
});