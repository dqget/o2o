$(function () {
    let userName = '';
    let getShopUserMapUrl = '/o2o/shopadmin/getusershopmaplistbyshop?pageIndex=1&pageSize=999&userName=';
    getShopUserMap();

    function getShopUserMap() {
        $.getJSON(getShopUserMapUrl + userName, function (data) {
            console.log(data);
            if (data.success) {
                let userShopMapList = data.data.userShopMapList;
                let html = '';
                userShopMapList.map(function (item, index) {
                    html += '<div class="row row-shop-user-point">' +
                        '<div class="col-50">' + item.user.name + '</div>' +
                        '<div class="col-50">' + item.point + '</div></div>';
                });
                $(".shop-user-point-list").html(html);
            } else {

            }
        })
    }

    $("#search").on("change", function (e) {
        userName = e.target.value;
        $(".shop-user-point-list").empty();
        getShopUserMap();
    });
});