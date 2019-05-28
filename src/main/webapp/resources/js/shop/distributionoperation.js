$(function () {
    const getScheduleDistributionUrl = '/o2o/shopadmin/getscheduledistributionbyshop';
    const modifyScheduleDistributionByShopUrl = '/o2o/shopadmin/modifyscheduledistributionbyshop';
    const scheduleDistributionId = getQueryString('scheduleDistributionId');
    getScheduleDistribution();
    let scheduleDistribution;

    function getScheduleDistribution() {
        let url = getScheduleDistributionUrl + '?scheduleDistributionId=' + scheduleDistributionId;
        $.getJSON(url, function (data) {
            if (data.success) {
                console.log(data);
                scheduleDistribution = data.data;

                $("#schedule-distribution-id").val(scheduleDistribution.scheduleDistributionId);
                $("#receipt-time").val(new Date(scheduleDistribution.receiptTime).Format("yyyy-MM-dd"));
                $("#update-time").val(scheduleDistribution.updateTime != null ? new Date(scheduleDistribution.updateTime).Format("yyyy-MM-dd") : "未配送");
                $("#operator").val(scheduleDistribution.operator != null ? scheduleDistribution.operator.name : "未配送");
                $("#receive-name").val(scheduleDistribution.receiveName);
                $("#receive-addr").val(scheduleDistribution.receiveAddr);
                $("#receive-phone").val(scheduleDistribution.receivePhone);


                if (scheduleDistribution.isReceipt == 1) {
                    $("#distribution-operation").html("已发货");
                    $("#order-operation").css("pointer-events", "none");
                } else if (scheduleDistribution.isReceipt == 0) {
                    $("#distribution-operation").html("确认发货");
                    $("#distribution-operation").on('click', function (e) {
                        $.confirm('确认发货吗？', function () {
                            if (new Date() < scheduleDistribution.receiptTime) {
                                $.confirm('还未到配送时间，确认发货吗?', function () {
                                    modifyScheduleDistributionByShop(scheduleDistributionId);
                                });
                            } else {
                                modifyScheduleDistributionByShop(scheduleDistributionId);
                            }

                        });
                    });
                }
            }
        })
    }

    function modifyScheduleDistributionByShop(scheduleDistributionId) {
        $.ajax({
            url: modifyScheduleDistributionByShopUrl,
            type: "POST",
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify({
                scheduleDistributionId: scheduleDistributionId,
                isReceipt: 1,
            }),
            success: function (data) {
                if (data.success) {
                    console.log(data);
                    location.reload();
                } else {
                    $.toast(data.msg);
                }
            },
        });
    }
});