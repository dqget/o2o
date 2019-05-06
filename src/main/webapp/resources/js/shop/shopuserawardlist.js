$(function () {
    let awardName = '';
    let getUserAwardMapListByShopUrl = '/o2o/shopadmin/getuserawardmaplistbyshop?pageIndex=1&pageSize=999&awardName=';
    getUserAwardMapListByShop();

    function getUserAwardMapListByShop() {
        $.getJSON(getUserAwardMapListByShopUrl + awardName, function (data) {
            console.log(data);
            if (data.success) {
                let userAwardMapList = data.data.userAwardMapList;
                let html = '';
                userAwardMapList.map(function (item, index) {
                    html += '<div class="row row-shop-user-award">' +
                        '<div class="col-30">' + item.award.awardName + '</div>' +
                        '<div class="col-40">' + new Date(item.createTime).Format("yyyy-MM-dd hh:mm:ss") + '</div>' +
                        '<div class="col-20">' + item.user.name + '</div>' +
                        '<div class="col-10">' + item.point + '</div>';
                });
                $(".shop-user-award-list").html(html);
            } else {

            }
        })
    }

    $("#search").on("change", function (e) {
        awardName = e.target.value;
        $(".shop-user-award-list").empty();
        getUserAwardMapListByShop();
    });
});