$(function () {
    let productId = getQueryString('productId');
    const productUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId;
    const addScheduleAndOpenPayUrl = '/o2o/frontend/addscheduleandopenpay';
    let product;
    getProduct();

    //sui地区选择器
    $("#city-picker").cityPicker({
        toolbarTemplate: '<header class="bar bar-nav">' +
        '<button class="button button-link pull-right close-picker">确定</button>' +
        '<h1 class="title">选择收货地址</h1></header>'
    });

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
        let receiveName = $('#receive-name').val().trim();
        let receiveAddr = $('#receive-addr').val().trim();
        let receivePhone = $('#receive-phone').val().trim();
        let checkMsg = true;
        if (receiveAddr == '' || receiveName == '' || receivePhone == '') {
            $.toast("请填写完整的配送信息");
            checkMsg = false;
        }
        if (checkMsg) {
            let schedule = {
                product: {
                    productId: product.productId
                },
                shop: product.shop,
                dailyQuantity: $('#daily-quantity').val(),
                amountDay: $('#amount-day').val(),
                scheduleDistributionList: [{
                    receiptTime: $('#receipt-time').val(),
                    receiveAddr: $('#city-picker').val() + $('#receive-addr').val(),
                    receiveName: $('#receive-name').val(),
                    receivePhone: $('#receive-phone').val()
                }]
            };
            if (isClickPay) {
                $.confirm("确认支付？", function () {
                    isClickPay = false;
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
        }

    });
});