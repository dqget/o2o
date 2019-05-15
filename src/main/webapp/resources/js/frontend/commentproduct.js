$(function () {
    let orderProductMapId = getQueryString("orderProductMapId");
    let orderProductMap;
    let product;
    let starLevel = 3;
    const getProductByIdUrl = '/o2o/frontend/listproductdetailpageinfo?productId=';
    const getOrderProductMapByIdUrl = '/o2o/frontend/getorderproductmapbyid?orderProductMapId=' + orderProductMapId;
    const addEvaAndModifyOrderProductMapUrl = '/o2o/frontend/addevaandmodifyorderproductmap';

    //初始化评分
    init();
    //获取订单项信息和商品信息
    getOrderProductMapById();

    $("#comment-product").click(function () {
        console.log(starLevel);
        let content = $("#content").val().trim();
        $.ajax({
            url: addEvaAndModifyOrderProductMapUrl + "?orderProductMapId=" + orderProductMapId + "&starLevel=" + starLevel,
            type: "post",
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify({
                product: {
                    productId: product.productId
                },
                content: content
            }),
            success: function (data) {
                if (data.success) {
                    $.toast("评论成功");
                    location.href = "/o2o/frontend/productdetail?productId=" + product.productId;
                } else {
                    $.toast(data.msg);
                }
            }
        });
    });

    function init() {
        $('.ratyli').ratyli({
                onRated: function (value, init) {
                    if (!init) {
                        starLevel = value;
                        $("#star-level").text(starLevel + "星");
                    }
                }
            }
        );
    }

    function getOrderProductMapById() {
        console.log(orderProductMapId);
        $.getJSON(getOrderProductMapByIdUrl, function (data) {
            console.log(data);
            if (data.success) {
                orderProductMap = data.data;
                getProductById();
            }
        })
    }

    function getProductById() {
        $.getJSON(getProductByIdUrl + orderProductMap.product.productId, function (data) {
            console.log(data);
            product = data.product;
            let productHtml = '';

            $("#product-name").html(orderProductMap.product.productName);
            productHtml += '<li class="item-content">' + '<div class="item-media">'
                + '<img src="' + getContextPath() + product.imgAddr + '" width="44">' +
                '</div><div class="item-inner"><div class="item-title-row">'
                + '<div class="item-subtitle">' + product.productName + '</div>' +
                '</div><div class="item-title-row">'
                + '<div class="item-text">' + product.productDesc + '</div>'
                + '</div></div></li>';
            $("#product-info").html(productHtml);
        });
    }

});