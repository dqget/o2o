$(function () {

    let url = '/o2o/frontend/getbuyercartbyuser';
    let buyerCart;
    let payPrice = 0;
    getBuyerCart();
    edit_count();

    //购物车商品选择
    $('.buyercart-list').on('click', '.labcheck', function (event) {
        if (!$(this).hasClass('checkBox')) {
            $(this).addClass('checkBox');
            event.preventDefault();
        } else {
            $(this).removeClass('checkBox');
            event.preventDefault();
        }
    });
    //全选
    $('.footer-g').on('click', '.labcheck', function (event) {
        if (!$(this).hasClass('checkBox')) {
            $(this).addClass('checkBox');
            event.preventDefault();
        } else {
            $(this).removeClass('checkBox');
            event.preventDefault();
        }
    });
    $("#check_all").click(function () {
        if ($(this).hasClass("checkBox")) {
            $(".buyercart-list").find(".labcheck").removeClass("checkBox");
        } else {
            $(".buyercart-list").find(".labcheck").addClass("checkBox");
        }
    });
    //删除商品
    $("#manage").click(function () {
        $("#manage").removeClass("show").addClass("hide");
        $("#finish").removeClass("hide").addClass("show");
        $(".footer-r,.sum-money").removeClass("show").addClass("hide");
        $(".del_goods").removeClass("hide").addClass("show");


    });

    $(document).on('click', '.del_goods', function () {
        if ($('.buyercart-list').find(".labcheck").hasClass("checkBox")) {
            $.confirm('确定要删除该商品吗？', function () {
                // $(".labcheck[class*=checkBox]").parents(".card").remove();
                // console.log($(".labcheck[class*=checkBox]").parents(".card").attr("data-product-id"));
                // console.log($(".labcheck[class*=checkBox]").parents(".card").attr("data-product-id"));


            });
        } else {
            $.toast("您还没有选择宝贝哦！");
        }

    });
    //删除完成
    $("#finish").click(function () {
        $("#finish").removeClass("show").addClass("hide");
        $("#manage").removeClass("hide").addClass("show");
        $(".del_goods").removeClass("show").addClass("hide");
        $(".footer-r,.sum-money").removeClass("hide").addClass("show");

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
                    html += '' + '<div class="card"  data-product-id="' + product.productId + '">'
                        + '<div class="left">'
                        // + '<label class="labcheck">'
                        // + '<input type="checkbox" class="select">' + '</label>'
                        + '</div>'
                        + '<div class="goods clearfix">' + '<img src="'
                        + getContextPath() + product.imgAddr + '">'
                        + '<div class="goods_details fr">'
                        + '<div class="name">' + product.productName + '</div>'
                        + '<div class="describe">' + product.productDesc + '</div>'
                        + '<div class="price clearfix">'
                        + '<div class="money fl">'
                        + '<div class="new">￥' + product.promotionPrice + '</div>'
                        + '<div class="old">￥' + product.normalPrice + '</div>' + '</div>'
                        + '<div class="kuang fr" id="changeCount" >'
                        + '<span class="one sub" ' +
                        'data-product-id="' + product.productId + '" data-amount-id ="productId' + index + '">-'
                        + '</span>'
                        + '<span class="middle" id="productId' + index + '">' + item.amount + '</span>'
                        + '<span class="two add" ' +
                        'data-product-id="' + product.productId + '"  data-amount-id ="productId' + index + '">+'
                        + '</span>'
                        + '</div></div></div></div></div>';
                    payPrice += parseInt(product.promotionPrice) * parseInt(item.amount);
                });
                $('.buyercart-list').append(html);
                $('#pay-price').text('￥' + payPrice);
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

    function updateBuyerCart(amount, productId) {
        $.ajax({
            url: '/o2o/frontend/updateitemtobuyercart',
            type: "post",
            contentType: 'application/json;charset=utf-8',
            data: JSON.stringify([
                {
                    "amount": amount,
                    "product": {
                        "productId": productId
                    }
                }
            ]),
            dataType: 'json',
            success: function (data) {
                // console.log(data);
                if (data.success) {
                    $.getJSON(url, function (data) {
                        if (data.success) {
                            buyerCart = data.data;
                            payPrice = 0;
                            buyerCart.map(function (item, index) {
                                let product = item.product;
                                payPrice += parseInt(product.promotionPrice) * parseInt(item.amount);
                            });
                            $('#pay-price').text('￥' + payPrice);

                        }
                    });
                    // location.reload();
                } else {
                    $.toast(data.msg);
                    location.reload();
                }
            },
            error: function (data) {
                $.toast("修改购物车失败");
                location.reload();

            }
        });
    }


    function edit_count() {
        let amount;
        let product;
        let productAmount;
        let productAmountSpan;
        let afterAmount;
        $('.buyercart-list').on('click', '.sub', function (e) {
            amount = -1;
            product = e.currentTarget.dataset.productId;
            productAmountSpan = $('#' + e.currentTarget.dataset.amountId);
            productAmount = parseInt(productAmountSpan.text());
            afterAmount = parseInt(productAmount + amount);

            if (afterAmount == 0) {
                location.reload();
            } else {
                productAmountSpan.text(afterAmount);

            }
            updateBuyerCart(amount, product);
            // console.log(buyerCart);

            // console.log(productAmount);
        });

        $('.buyercart-list').on('click', '.add', function (e) {
            amount = 1;
            product = e.currentTarget.dataset.productId;
            productAmountSpan = $('#' + e.currentTarget.dataset.amountId);
            productAmount = parseInt(productAmountSpan.text());

            productAmountSpan.text(parseInt(productAmount + amount));
            // console.log(productAmount);
            updateBuyerCart(amount, product);
            // console.log(buyerCart);

        });

    }

});