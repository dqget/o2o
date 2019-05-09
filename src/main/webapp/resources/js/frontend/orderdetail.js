$(function () {
    const getOrderDetailByNoUrl = '/o2o/frontend/getorderdetailbyno';
    const oldOrderOpenPayUrl = '/o2o/frontend/oldorderopenpay';
    const modifyOrderByUserUrl = '/o2o/frontend/modifyorderbyuser';
    const orderNo = getQueryString('orderNo');
    getOrderDetailByNo();
    let order;

    function getOrderDetailByNo() {
        let url = getOrderDetailByNoUrl + '?orderNo=' + orderNo;
        $.getJSON(url, function (data) {
            if (data.success) {
                order = data.data;
                let shop = order.shop;
                let orderProductMapList = order.orderProductMapList;
                $("#shop-name").html(shop.shopName);
                let html = '';
                let productPriceCount = 0;
                let productPointCount = 0;
                //评论地址
                const evaUrl = '/o2o/frontend/commentproduct?orderProductMapId='
                orderProductMapList.map(function (item, index) {
                    productPriceCount += parseInt(item.product.normalPrice);
                    productPointCount += item.product.point;
                    html += '<li class="item-content">' + '<div class="item-media">'
                        + '<img src="' + getContextPath() + item.product.imgAddr + '" width="44">' +
                        '</div><div class="item-inner"><div class="item-title-row">'
                        + '<div class="item-title">' + item.product.productName + '</div>' +
                        '<div class="item-after"><del>￥' + item.product.normalPrice + '</del> ￥'
                        + item.product.promotionPrice
                        + '</div></div><div class="item-title-row">'
                        + '<div class="item-subtitle ">' + item.product.productDesc + '</div>'
                        + '<div class="item-after">×' + item.productNum + '</div>'
                        + '</div></div></li>';
                    if (order.isPay == 1 && order.isShip == 1 && order.isReceipt == 1 && item.isEvaluation != 1) {
                        html += '<li class="item-content">' +
                            '<div class="item-after">' +
                            '<a href="' + evaUrl + item.orderProductId + '" class="external">去评价</a>' +
                            '</div></li>';
                    }
                });
                $("#product-list").html(html);
                $("#product-price").html('商品总价:￥' + productPriceCount);
                $("#order-price").html('订单总价:￥' + order.payPrice);
                $("#pay").html('实付款:￥' + order.payPrice);
                $("#name-and-phone").html(order.receiveName + ' ' + order.receivePhone);
                $("#addr").html(order.receiveAddr);
                $("#orderNo").val(orderNo);
                $("#order-create-time").val(new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss"));
                $("#pay-time").val(order.payTime != null ? new Date(order.payTime).Format("yyyy-MM-dd hh:mm:ss") : "待支付");
                $("#ship-time").val(order.shipTime != null ? new Date(order.shipTime).Format("yyyy-MM-dd hh:mm:ss") : "待发货");
                $("#receipt-time").val(order.receiptTime != null ? new Date(order.receiptTime).Format("yyyy-MM-dd hh:mm:ss") : "未成功");
                $("#point").val(productPointCount);
                $("#track-number").val(order.trackNumber);

                if (order.isPay == 0) {
                    $("#order-operation").html("支付");
                    $("#order-operation").on('click', function (e) {
                        $.ajax({
                            url: oldOrderOpenPayUrl,
                            type: "POST",
                            contentType: 'application/json;charset=utf-8',
                            data: JSON.stringify({orderNo: orderNo}),
                            success: function (data) {
                                sessionStorage.setItem("orderNo", orderNo);
                                $('#returnAli').append(data);
                                $("#returnAli script").remove();
                                var queryParam = '';
                                Array.prototype.slice.call(document.querySelectorAll("input[type=hidden]")).forEach(function (ele) {
                                    queryParam += ele.name + "=" + encodeURIComponent(ele.value) + '&';
                                });
                                let url = document.getElementsByName("punchout_form")[0].action + '&' + queryParam;
                                _AP.pay(url);
                            }
                        });
                    });
                } else if (order.isShip == 0) {
                    $("#order-operation").html("等待发货");
                    $("#order-operation").css("pointer-events", "none");
                } else if (order.isReceipt == 0) {
                    $("#order-operation").html("确认收货");
                    $("#order-operation").on('click', function (e) {
                        alert("确认收货？");
                        $.ajax({
                            url: modifyOrderByUserUrl,
                            type: "POST",
                            contentType: 'application/json',
                            dataType: "json",
                            data: JSON.stringify({orderId: order.orderId, isReceipt: 1, receiptTime: new Date()}),
                            success: function (data) {
                                location.reload();
                            }
                        })
                    });
                } else {
                    $("#order-operation").html("交易成功");
                    $("#order-operation").css("pointer-events", "none");
                }
                // console.log(data);
            }
        })
    }

});