$(function () {
    let awardName = '';
    let getUserAwardMapListByShopUrl = '/o2o/shopadmin/getuserawardmaplistbyshop?pageIndex=1&pageSize=999&awardName=';
    let modifyUserAwardMapUrl = '/o2o/shopadmin/modifyuserawardmap';
    getUserAwardMapListByShop();

    function getUserAwardMapListByShop() {
        $.getJSON(getUserAwardMapListByShopUrl + awardName, function (data) {
            console.log(data);
            if (data.success) {
                let userAwardMapList = data.data.userAwardMapList;
                let html = '';
                userAwardMapList.map(function (item, index) {
                    html += '<div class="row row-shop-user-award">' +
                        '<div class="col-30">' + new Date(item.createTime).Format("yy-MM-dd") + '</div>' +
                        '<div class="col-20">' + item.award.awardName + '</div>' +
                        '<div class="col-15">' + item.user.name + '</div>' +
                        '<div class="col-15">' + item.point + '</div>' +
                        '<div class="col-20">' + usedStatusHtml(item) + '</div></div>';
                });
                $(".shop-user-award-list").html(html);
            } else {

            }
        })
    }

    function usedStatusHtml(userAwardMap) {
        if (userAwardMap.usedStatus == 1) {
            return '已兑换'
        } else if (userAwardMap.usedStatus == 0) {
            return '<a class="modifyStatus" data-user-award-map-id="' + userAwardMap.userAwardId + '" >兑换</a>';
        } else {
            return "";
        }
    }

    $('.shop-user-award-list').on('click', 'a', function (e) {
        console.log(e.currentTarget.dataset.userAwardMapId);
        $.confirm('确认兑换', function () {
            let userAwardId = parseInt(e.currentTarget.dataset.userAwardMapId);
            $.ajax({
                url: modifyUserAwardMapUrl,
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({userAwardId: userAwardId, usedStatus: 1}),
                success: function (data) {
                    if (data.success) {
                        location.reload();
                    }
                    $.toast(data.msg);
                },
                error: function (data) {

                }
            })
        });


    });
    $("#search").on("change", function (e) {
        awardName = e.target.value;
        $(".shop-user-award-list").empty();
        getUserAwardMapListByShop();
    });
});