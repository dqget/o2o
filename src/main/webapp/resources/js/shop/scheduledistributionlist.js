$(function () {
    // $.init();
    let scheduleId = getQueryString("scheduleId");
    const scheduleDistributionListUrl = "/o2o/shopadmin/getschedulebyshop?scheduleId=" + scheduleId;
    //商品下架的URL
    // var statusUrl = '/o2o/shopadmin/modifyproduct';
    getList();

    function getList() {
        $.ajax({
            url: scheduleDistributionListUrl,
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    // console.log(data);
                    handleList(data.data.scheduleDistributionList);
                }
            }
        });
    }

    function handleList(scheduleDistributionList) {
        let html = '<div class="row">';
        scheduleDistributionList.map(function (item, index) {
            html +=
                '<div class="col-33">' +
                '<a href="#" class="button button-big button-fill external" '
                + (item.isReceipt == 1 ? 'style="opacity: 0.2"' : '')
                + 'data-schedule-distribution-id="' + item.scheduleDistributionId + '" >'
                + new Date(item.receiptTime).Format("yyyy-MM-dd") + '</a> </div>';
        });
        html += '</div>';
        $('.schedule-distribution-wrap').html(html);
    }

    $('.schedule-distribution-wrap').on('click', 'a', function (e) {
        let scheduleDistributionId = e.currentTarget.dataset.scheduleDistributionId;
        // console.log(scheduleDistributionId);
        location.href = '/o2o/shopadmin/distributionoperation?scheduleDistributionId=' + scheduleDistributionId;
    });
});
