$(function () {

    const scheduleListUrl = "/o2o/shopadmin/getschedulelistbyshop?pageSize=999&&pageIndex=0&userName";
    //商品下架的URL
    // var statusUrl = '/o2o/shopadmin/modifyproduct';
    getList();

    function getList() {
        $.ajax({
            url: scheduleListUrl,
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    // console.log(data);
                    handleList(data.data.scheduleList);
                }
            }
        });
    }

    function handleList(scheduleList) {
        let html = '';
        scheduleList.map(function (item, index) {
            html += '<div class="row row-schedule">' +
                '<div class="col-30">' + new Date(item.createTime).Format("yy-MM-dd") + '</div>' +
                '<div class="col-20">' + item.product.productName + '</div>' +
                '<div class="col-15">' + item.user.name + '</div>' +
                '<div class="col-20">' + item.amountDay + '</div>' +
                '<div class="col-15">' + getScheduleDetailHtml(item) + '</div></div>';
        });
        $('.schedule-wrap').html(html);
    }

    function getScheduleDetailHtml(schedule) {
        return '<a class="schedule-detail" data-schedule-id="' + schedule.scheduleId + '" >查看</a>';
    }

    $('.schedule-wrap').on('click', 'a', function (e) {
        let scheduleId = e.currentTarget.dataset.scheduleId;
        // console.log(scheduleId);
        location.href = "/o2o/shopadmin/scheduledistributionlist?scheduleId=" + scheduleId;
    });
});
