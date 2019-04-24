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
                    handleUser(data.user);
                }
            }
        });
    }

    function handleUser(data) {
        if (data.userType == null || data.userType == 1) {
            $.alert('您的身份不是店主', '警告!', function () {
                $.alert('您的身份不是店主！');
                window.location.href = "/o2o/frontend/index";
            });
        }
        $('#user-name').text(data.name);
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
