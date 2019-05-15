$(function () {
    let productId = getQueryString('productId');
    const productUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId;
    const addScheduleAndOpenPayUrl = '/o2o/frontend/addscheduleandopenpay';
    let product;
    getProduct();

    function getProduct() {
        $.getJSON(productUrl, function (data) {
            if (data.success) {
                // console.log("----------");
                product = data.product;
                $('#product-name').val(product.productName);
                $('#promotion-price').val(parseInt(product.promotionPrice));
            }
        });
        let dateTime = new Date().setDate(new Date().getDate() + 1);
        $('#receipt-time').val(new Date(dateTime).Format("yyyy-MM-dd"));
    }

    let isClickPay = true;

    $('#submit').click(function () {
        let schedule = {
            product: {
                productId: product.productId
            },
            shop: product.shop,
            dailyQuantity: $('#daily-quantity').val(),
            amountDay: $('#amount-day').val(),
            scheduleDistributionList: [{
                receiptTime: $('#receipt-time').val(),
                receiveAddr: $('#receive-addr').val(),
                receiveName: $('#receive-name').val(),
                receivePhone: $('#receive-phone').val()
            }]
        };
        if (isClickPay) {
            isClickPay = false;

            $.confirm("确认支付？", function () {
                $.ajax({
                    url: addScheduleAndOpenPayUrl,
                    type: "POST",
                    contentType: 'application/json',
                    data: JSON.stringify(schedule),
                    success: function (data) {
                        $('#returnAli').append(data);
                        $("#returnAli script").remove();
                        let queryParam = '';
                        Array.prototype
                            .slice
                            .call(document.querySelectorAll("input[type=hidden]"))
                            .forEach(function (ele) {
                                queryParam += ele.name + "=" + encodeURIComponent(ele.value) + '&';
                                sessionStorage.setItem("scheduleId", JSON.parse(ele.value).out_trade_no);
                            });
                        let url = document.getElementsByName("punchout_form")[0].action + '&' + queryParam;
                        _AP.paySchedule(url);
                    }
                })
            });
        }
    });
});