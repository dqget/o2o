$(function () {
    var productId = getQueryString('productId');
    var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId='
        + productId;

    $.getJSON(productUrl, function (data) {
        if (data.success) {
            var product = data.data;
            $('#product-img').attr('src', getContextPath() + product.imgAddr);
            $('#product-time').text(
                new Date(product.lastEditTime)
                    .Format("yyyy-MM-dd"));
            if (product.point != undefined) {
                $('#product-point').text('购买可获得' + product.point + '积分');
            }
            $('#product-name').text(product.productName);
            $('#product-desc').text(product.productDesc);
            if (product.normalPrice != undefined && product.promotionPrice != undefined) {
                $('#price').show();
                $('#normalPrice').html('<del>' + '￥' + product.normalPrice + '</del>');
                $('#promotionPrice').text('￥' + product.promotionPrice);
            }
            var imgListHtml = '';
            product.productImgList.map(function (item, index) {
                imgListHtml += '<img src="'
                    + getContextPath() + item.imgAddr + '"/>';
            });
            // 生成购买商品的二维码供商家扫描
            //imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4product?productId='
            //    + product.productId + '"/></div>';
            $('#imgList').html(imgListHtml);
        }
    });
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });
    $.init();
});
