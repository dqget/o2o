$(function () {
    let loading = false;
    let maxItems = 20;
    let pageSize = 99;

    const getOrderListByShopUrl = '/o2o/order/shopadmin/getorderlistbyshop';

    let pageNum = 1;
    let keyWord = '';
    let activeTab;

    $("#all_a").click(function (e) {
        $("#all_a").css("pointer-events", "none");
        $("#not_pay_a").css("pointer-events", "auto");
        $("#not_ship_a").css("pointer-events", "auto");
        $("#not_receipt_a").css("pointer-events", "auto");
        $("#business-success_a").css("pointer-events", "auto");

        pageNum = 1;
        activeTab = e.target;
        $(activeTab.getAttribute("href") + " .list-div").html("");
        setTimeout(function () {
            getOrderList(activeTab, keyWord);
        }, 0)

    });
    $("#all_a").click();

    $("#not_pay_a").click(function (e) {
        $("#all_a").css("pointer-events", "auto");
        $("#not_pay_a").css("pointer-events", "none");
        $("#not_ship_a").css("pointer-events", "auto");
        $("#not_receipt_a").css("pointer-events", "auto");
        $("#business-success_a").css("pointer-events", "auto");
        pageNum = 1;
        activeTab = e.target;
        $(activeTab.getAttribute("href") + " .list-div").html("");
        setTimeout(function () {
            getOrderList(activeTab, keyWord);
        }, 0)
    });
    $("#not_ship_a").click(function (e) {
        $("#all_a").css("pointer-events", "auto");
        $("#not_pay_a").css("pointer-events", "auto");
        $("#not_ship_a").css("pointer-events", "none");
        $("#not_receipt_a").css("pointer-events", "auto");
        $("#business-success_a").css("pointer-events", "auto");
        pageNum = 1;
        activeTab = e.target;
        $(activeTab.getAttribute("href") + " .list-div").html("");
        setTimeout(function () {
            getOrderList(activeTab, keyWord);
        }, 0)
    });
    $("#not_receipt_a").click(function (e) {
        $("#all_a").css("pointer-events", "auto");
        $("#not_pay_a").css("pointer-events", "auto");
        $("#not_ship_a").css("pointer-events", "auto");
        $("#not_receipt_a").css("pointer-events", "none");
        $("#business-success_a").css("pointer-events", "auto");
        pageNum = 1;
        activeTab = e.target;
        $(activeTab.getAttribute("href") + " .list-div").html("");
        setTimeout(function () {
            getOrderList(activeTab, keyWord);
        }, 0)
    });
    $("#business-success_a").click(function (e) {
        $("#all_a").css("pointer-events", "auto");
        $("#not_pay_a").css("pointer-events", "auto");
        $("#not_ship_a").css("pointer-events", "auto");
        $("#not_receipt_a").css("pointer-events", "auto");
        $("#business-success_a").css("pointer-events", "none");
        pageNum = 1;
        activeTab = e.target;
        $(activeTab.getAttribute("href") + " .list-div").html("");
        setTimeout(function () {
            getOrderList(activeTab, keyWord);
        }, 0)
    });

    //获取全部订单信息
    function getOrderList(activeTab, keyWord) {
        var url = getOrderListByShopUrl + '?' + 'pageIndex=' + pageNum + '&pageSize='
            + pageSize + '&keyWord=' + keyWord + '&state=' + activeTab.getAttribute("state");
        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                let html = '';
                html = getOrderHtml(data);
                let activeTabId = activeTab.getAttribute("href");
                $(activeTabId + " .list-div").append(html);
                let total = $(activeTabId + ' .list-div .card').length;
                maxItems = data.data.count;
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    // 隐藏加载提示符
                    $(activeTabId + ' .infinite-scroll-preloader').hide();
                } else {
                    $(activeTabId + ' .infinite-scroll-preloader').show();
                }
                pageNum += 1;
                loading = false;
                $.refreshScroller();
            } else {
                $.toast(data.msg);
            }
        });
    }

    function getOrderHtml(data) {
        let html = '';
        data.data.orderList.map(function (item, index) {
            let productHtml = '';
            //商品列表
            item.orderProductMapList.map(function (item, index) {
                productHtml += '<div class="item-subtitle">' + item.product.productName
                    + ' ×' + item.productNum + '</div>';
            });
            //
            html += '' + '<div class="card" data-order-id="'
                + item.orderId + '" data-order-no="' + item.orderNumber + '">'
                + '<div class="card-header">'
                + item.user.name + new Date(item.updateTime).Format("yyyy-MM-dd") + '</div>'
                + '<div class="card-content">'
                + '<div class="list-block media-list">'
                + '<ul>' + '<li class="item-content">'
                + '<div class="item-media">'
                + '<img src="' + getContextPath() + item.shop.shopImg + '" width="44">' + '</div>'
                + '<div class="item-inner">'
                + productHtml
                + '</div>' + '</li>' + '</ul>' + '</div>' + '</div>'
                + '<div class="card-footer">'
                + '<p class="color-gray">共计 ' + item.payPrice + '￥</p>'
                + '<span>查看详情</span>' + '</div>' + '</div>';
        });
        return html;
    }

    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        getOrderList(activeTab, keyWord);
    });

    $('#shopdetail-button-div').on('click', '.button', function (e) {
        productCategoryId = e.target.dataset.productSearchId;
        if (productCategoryId) {
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                productCategoryId = '';
            } else {
                $(e.target).addClass('button-fill').siblings()
                    .removeClass('button-fill');
            }
            $('.list-div').empty();
            pageNum = 1;
            addItems(pageSize, pageNum);
        }
    });

    $('.list-div').on('click', '.card', function (e) {
        let orderNo = e.currentTarget.dataset.orderNo;
        window.location.href = '/o2o/shopadmin/shoporderoperation?orderNo=' + orderNo;
    });


    //失去焦点时执行
    $('#search').blur(function (e) {
        keyWord = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        getOrderList(activeTab, keyWord);
    });
    $('#me').click(function () {
        $.openPanel('#panel-js-demo');
    });
    $.init();
});
