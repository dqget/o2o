$(function () {
    let getOrderNoNrl = '/o2o/pay/getorderno';
    let addOrderAndOpenPayUrl = '/o2o/frontend/addorderandopenpay';
    let orderNo = '';
    let sessionStorage = window.sessionStorage;
    let orderItems;
    //商品总价值
    let price = 0;

    init();


    //获取订单编号
    function getOrderNo() {
        $.ajax({
            url: getOrderNoNrl,
            type: "get",
            async: false,
            success: function (data) {
                orderNo = data + '';
            }
        });
    }

    function init() {
        if (sessionStorage.getItem('orderItems') == null) {
            window.location.href = '/o2o/frontend/orderlist';
        }
        orderItems = JSON.parse(sessionStorage.getItem('orderItems'));
        //商品总数
        let productAmount = 0;
        let html = '';
        //获取订单号


        orderItems.map(function (item, index) {
            let product = item.product;
            price += item.amount * parseInt(product.promotionPrice);
            productAmount += item.amount;
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
                + '<p class="color-gray"><del>￥'
                + product.normalPrice + '</del><span style="color: red; font-size: medium; ">￥'
                + product.promotionPrice
                + '</span></p>' + '<span>' + item.amount + '件</span>' + '</div>'
                + '</div>';
        });
        //选购的商品列表
        $('.order-list').append(html);
        //左下角总价值
        $('#price').text('共' + productAmount + '件商品合计:￥' + price);
        //sui地区选择器
        $("#city-picker").cityPicker({
            toolbarTemplate: '<header class="bar bar-nav">' +
                '<button class="button button-link pull-right close-picker">确定</button>' +
                '<h1 class="title">选择收件地址</h1></header>'
        });
    }

    function addOrder() {
        getOrderNo();
        order.orderNumber = orderNo;
        order.payPrice = price;
        order.receivePhone = $('#receivePhone').val();
        order.receiveName = $('#receiveName').val();
        order.receiveAddr = $('#city-picker').val() + ' ' + $('#addrAdditional').val();
        return order;
    }

    let isClickPay = true;
    $('#pay').on('click', function () {
        if (isClickPay) {
            isClickPay = false;
            const order = addOrder();
            $.ajax({
                url: addOrderAndOpenPayUrl,
                type: 'POST',
                contentType: 'application/json;charset=utf-8',
                data: JSON.stringify({items: orderItems, order: order}),
                success: function (data) {
                    sessionStorage.setItem("orderNo", orderNo);
                    $('#returnAli').append(data);
                    $("#returnAli script").remove();
                    let queryParam = '';
                    Array.prototype.slice.call(document.querySelectorAll("input[type=hidden]")).forEach(function (ele) {
                        queryParam += ele.name + "=" + encodeURIComponent(ele.value) + '&';
                    });
                    let url = document.getElementsByName("punchout_form")[0].action + '&' + queryParam;
                    _AP.pay(url);
                }
            });
            sessionStorage.removeItem('orderItems');
        }

    });
});





