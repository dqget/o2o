$(function () {
    getlist();

    function getlist(e) {
        $.ajax({
            url: "/o2o/shopadmin/getshoplist",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    handleList(data.shopList);
                }
            },
            error: function () {
                handleUser();
            }
        });

    }

    function handleUser() {
        $.alert('请回到首页', '您的身份或许不是店主', function () {
            window.location.href = "/o2o/frontend/index";
        });
    }

    function handleList(data) {
        var html = '';
        data.map(function (item, index) {
            html += '<div class="row row-shop"><div class="col-40">'
                + item.shopName + '</div><div class="col-40">'
                + shopStatus(item.enableStatus)
                + '</div><div class="col-20">'
                + goShop(item.enableStatus, item.shopId) + '</div></div>';

        });
        $('.shop-wrap').html(html);
    }

    function goShop(status, id) {
        if (status !== 0 && status !== -1) {
            return '<a class="in external" '
                + 'href="/o2o/shopadmin/shopmanage?shopId=' + id + '">进入</a>';
        } else {
            return '';
        }
    }


    function shopStatus(status) {
        if (status === 0) {
            return '审核中';
        } else if (status === -1) {
            return '店铺非法';
        } else {
            return '审核通过';
        }
    }


});
