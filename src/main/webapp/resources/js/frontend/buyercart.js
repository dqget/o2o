$(function () {

    let url = '/o2o/frontend/getbuyercartbyuser';
    getBuyerCart();
    let buyerCart;

    function getBuyerCart() {
        $.getJSON(url, function (data) {
            if (data.success) {
                buyerCart = data.data;
                //当前查询条件总数
                maxItems = data.count;
                let html = '';
                buyerCart.map(function (item, index) {
                    let product = item.product;
                    html += '' + '<div class="card" data-shop-id="'
                        + product.productId + '">'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + getContextPath() + product.imgAddr + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + product.productName
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + product.normalPrice + '   ' + product.promotionPrice
                        + '</p>' + '<span>' + item.amount + '</span>' + '</div>'
                        + '</div>';
                });
                $('.buyercart-list').append(html);
                let total = $('.list-div .card').length;
                maxItems = data.count;
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    // $.detachInfiniteScroll($('.infinite-scroll'));
                    // 隐藏加载提示符
                    $('.infinite-scroll-preloader').hide();
                } else {
                    $('.infinite-scroll-preloader').show();
                }
                $.refreshScroller();
            }
        });
    }

    $('#settleAccounts').click(function () {
        var localStorage = window.localStorage;
        localStorage.setItem('orderItems', JSON.stringify(buyerCart));
        window.location.href = '/o2o/pay/order';
    });
});