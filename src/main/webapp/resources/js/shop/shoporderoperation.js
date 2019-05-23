$(function () {
    const getOrderDetailByNoUrl = '/o2o/shopadmin/getorderdetailbyno';
    const modifyOrderBySHopUrl = '/o2o/shopadmin/modifyorderbyshop';
    const orderNo = getQueryString('orderNo');
    getOrderDetailByNo();
    let order;

    function getOrderDetailByNo() {
        let url = getOrderDetailByNoUrl + '?orderNo=' + orderNo;
        $.getJSON(url, function (data) {
            if (data.success) {
                order = data.data;
                let orderProductMapList = order.orderProductMapList;
                let html = '';
                let productPriceCount = 0;
                let productPointCount = 0;
                orderProductMapList.map(function (item, index) {
                    productPriceCount += parseInt(item.product.normalPrice);
                    productPointCount += item.product.point;
                    html += '<li class="item-content">' + '<div class="item-media">'
                        + '<img src="' + getContextPath() + item.product.imgAddr + '" width="44">' +
                        '</div><div class="item-inner"><div class="item-title-row">'
                        + '<div class="item-title">' + item.product.productName + '</div>' +
                        '<div class="item-after"><del>￥' + item.product.normalPrice + '</del> ￥'
                        + item.product.promotionPrice
                        + '</div></div><div class="item-title-row">' +
                        '<div class="item-subtitle ">' + item.product.productDesc + '</div>' +
                        '<div class="item-after">×' + item.productNum + '</div>' +
                        '</div></div></li>';
                });
                $("#product-list").html(html);
                $("#product-price").html('商品总价:￥' + productPriceCount);
                $("#order-price").html('订单总价:￥' + order.payPrice);
                $("#pay").html('实付款:￥' + order.payPrice);
                // $("#name-and-phone").html(order.receiveName + ' ' + order.receivePhone);
                // $("#addr").html(order.receiveAddr);

                $("#orderNo").val(orderNo);
                $("#order-create-time").val(new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss"));
                $("#pay-time").val(order.payTime != null ? new Date(order.payTime).Format("yyyy-MM-dd hh:mm:ss") : "待支付");
                $("#ship-time").val(order.shipTime != null ? new Date(order.shipTime).Format("yyyy-MM-dd hh:mm:ss") : "待发货");
                $("#receipt-time").val(order.receiptTime != null ? new Date(order.receiptTime).Format("yyyy-MM-dd hh:mm:ss") : "未成功");
                $("#point").val(productPriceCount);
                $("#user-name").val(order.user.name);
                $("#receive-name").val(order.receiveName);
                $("#receive-addr").val(order.receiveAddr);
                $("#track-number").val(order.trackNumber);
                $("#receive-phone").val(order.receivePhone);

                // console.log(order.user.name);

                if (order.isPay == 0) {
                    $("#order-operation").html("未支付");
                    $("#order-operation").css("pointer-events", "none");
                } else if (order.isShip == 0) {
                    $("#order-operation").html("确认发货");
                    $("#track-number").removeAttr('readonly');
                    $("#order-operation").on('click', function (e) {
                        let trackNumber = $("#track-number").val().trim();
                        if (trackNumber == null || trackNumber == "") {
                            $.toast("请填写快递单号才能确认发货");
                        } else {
                            $.ajax({
                                url: modifyOrderBySHopUrl,
                                type: "POST",
                                contentType: 'application/json',
                                dataType: "json",
                                data: JSON.stringify({
                                    orderId: order.orderId,
                                    trackNumber: trackNumber,
                                    isShip: 1,
                                    shipTime: new Date()
                                }),
                                success: function (data) {
                                    if (data.success) {
                                        console.log(data);
                                        location.reload();
                                    } else {
                                        $.toast(data.msg);
                                    }
                                },
                            });
                        }
                    });
                } else if (order.isReceipt == 0) {
                    $("#order-operation").html("等待收货");
                    $("#order-operation").css("pointer-events", "none");
                } else {
                    $("#order-operation").html("交易成功");
                    $("#order-operation").css("pointer-events", "none");
                }
            }
        })
    }

});