$(function () {

    let url = '/o2o/frontend/getbuyercartbyuser';
    getBuyerCart();
    let buyerCart;

    $('.card').on('click','.labcheck',function(event) {
        if(!$(this).hasClass('checkBox')) {
            $(this).addClass('checkBox');
            event.preventDefault();
        } else {
            $(this).removeClass('checkBox');
            event.preventDefault();
        }
    });


    function getBuyerCart() {
        $.getJSON(url, function (data) {
            if (data.success) {
                buyerCart = data.data;
                //当前查询条件总数
                maxItems = data.count;
                let html = '';
                buyerCart.map(function (item, index) {
                    let product = item.product;
                    html += '' + '<div class="card" data-product-id="'
                        + product.productId + '">'
                        + '<div class="left">'
                        + '<label class="labcheck">'
                        + '<input type="checkbox" class="select">' + '</label>' + '</div>'
                        + '<div class="goods clearfix">' + '<img src="'
                        + getContextPath() + product.imgAddr + '">'
                        + '<div class="goods_details fr">'
                        + '<div class="name">' + product.productName + '</div>'
                        + '<div class="describe">' + product.productDesc + '</div>'
                        + '<div class="price clearfix">'
                        + '<div class="money fl">'
                        + '<div class="new">￥' + product.promotionPrice + '</div>'
                        + '<div class="old">' + product.normalPrice + '</div>' + '</div>'
                        + '<div class="kuang fr" id="changeCount">'
                        + '<span class="one" id="sub">-' + '</span>'
                        + '<span class="middle">' + item.amount + '</span>'
                        + '<span class="two" id="add">+' + '</span>'
                        + '</div></div></div></div></div>';
                        /*+ '<div class="card-content">'
                        + '<div class="list-block media-list" >' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + getContextPath() + product.imgAddr + '" width="70">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + product.productName + '</div>'
                        + '<div class="item-subtitle">' + product.productDesc + '</div>'
                        + '</div></li></ul></div></div>'
                        + '<div class="card-footer">' + '<p class="color-gray">'
                        + '<del>￥' + product.normalPrice + '</del> ￥' + product.promotionPrice + '</p>'
                        + '<span>' + item.amount + '</span>' + '</div>'
                        + '</div>';*/
                });
                $('.buyercart-list').append(html);
                let total = $('.list-div .card').length;
                maxItems = data.count;
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    $.init();
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    // 隐藏加载提示符
                    //$('.infinite-scroll-preloader').hide();
                } else {
                    $('.infinite-scroll-preloader').show();
                }
                $.refreshScroller();
            }
        });
    }



    $('#settleAccounts').click(function () {
        let sessionStorage = window.sessionStorage;
        sessionStorage.setItem('orderItems', JSON.stringify(buyerCart));
        window.location.href = '/o2o/pay/order';
    });



    /*$.ajax({
        url: '/o2o/frontend/updateitemtobuyercart',
        type: "post",
        contentType: 'application/json;charset=utf-8',
        data:JSON.stringify([
            {
                "amount": 1,
                "product": {
                    "productId": 8
                }
            }
        ]),
        dataType:'json',
        success: function (data) {
            console.log(data);
            if (data.success) {

            }
        },
        error:function (data) {

        }
    });*/
});