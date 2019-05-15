$(function () {
    const getScheduleDistributionUrl = '/o2o/frontend/getscheduledistribution';
    const modifyScheduleDistributionUrl = '/o2o/frontend/modifyscheduledistributionbyuser';
    const scheduleDistributionId = getQueryString('scheduleDistributionId');
    getScheduleDistribution();
    let scheduleDistribution;

    function getScheduleDistribution() {
        let url = getScheduleDistributionUrl + '?scheduleDistributionId=' + scheduleDistributionId;
        $.getJSON(url, function (data) {
            console.log(data);

            if (data.success) {
                scheduleDistribution = data.data;

                $("#schedule-distribution-id").val(scheduleDistribution.scheduleDistributionId);
                $("#receipt-time").val(new Date(scheduleDistribution.receiptTime).Format("yyyy-MM-dd"));
                $("#update-time").val(scheduleDistribution.updateTime != null ? new Date(scheduleDistribution.updateTime).Format("yyyy-MM-dd") : "未配送");
                $("#operator").val(scheduleDistribution.operator != null ? scheduleDistribution.operator.name : "未配送");
                $("#receive-name").val(scheduleDistribution.receiveName);
                $("#receive-addr").val(scheduleDistribution.receiveAddr);
                $("#receive-phone").val(scheduleDistribution.receivePhone);


                if (scheduleDistribution.isReceipt == 1) {
                    $("#distribution-operation").html("花已经帮您送到");
                    $("#order-operation").css("pointer-events", "none");
                } else if (scheduleDistribution.isReceipt == 0) {
                    $("#distribution-operation").html("确认修改");

                    $("#receive-name").removeAttr('readonly');
                    $("#receive-addr").removeAttr('readonly');
                    $("#receive-phone").removeAttr('readonly');
                    $("#receipt-time").removeAttr('readonly');


                    $("#distribution-operation").on('click', function (e) {
                        $.confirm('确认修改吗？', function () {
                            $.ajax({
                                url: modifyScheduleDistributionUrl,
                                type: "POST",
                                contentType: 'application/json',
                                dataType: "json",
                                data: JSON.stringify({
                                    scheduleDistributionId: scheduleDistributionId,
                                    receiveName: $("#receive-name").val(),
                                    receiveAddr: $("#receive-addr").val(),
                                    receivePhone: $("#receive-phone").val(),
                                    receiptTime: $("#receive-time").val()
                                }),
                                success: function (data) {
                                    if (data.success) {
                                        console.log(data);
                                        location.reload();
                                    } else {
                                        $.toast(data.msg);
                                    }
                                }
                                ,
                            })
                            ;
                        });
                    });
                }
            }
        })
    }

});