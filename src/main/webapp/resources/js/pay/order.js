$(function () {
    var getOrderNoNrl = '/o2o/pay/getorderno';
    var addOrderUrl = '/o2o/frontend/addorderbyuser';
    let orderNo = '';
    var localStorage = window.localStorage;
    var orderItems = JSON.parse(localStorage.getItem('orderItems'));
    //商品总价值
    var price = 0;

    init();

    function getOrderNo() {
        $.ajax({
            url: getOrderNoNrl,
            type: "get",
            success: function (data) {
                orderNo = data + '';
                console.log(orderNo)
            }
        });
    }

    function init() {

        //商品总数
        var productAmount = 0;
        var html = '';
        //获取订单号
        getOrderNo();

        orderItems.map(function (item, index) {
            var product = item.product;
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
                + '<p class="color-gray">'
                + product.normalPrice + '   ' + product.promotionPrice
                + '</p>' + '<span>' + item.amount + '</span>' + '</div>'
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
            '<h1 class="title">选择收货地址</h1></header>'
        });
    }

    function addOrder() {
        order.orderNumber = orderNo;
        order.payPrice = price;
        order.receivePhone = $('#receivePhone').val();
        order.receiveName = $('#receiveName').val();
        order.receiveAddr = $('#city-picker').val() +' '+ $('#addrAdditional').val();
        return order;
    }

    $('#pay').on('click', function (e) {
        const order = addOrder();
        $.ajax({
            url: addOrderUrl,
            type: 'POST',
            data: JSON.stringify({
                items: orderItems,
                order: order
            }),
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {
                console.log(data);
            }
        })
    });
});




