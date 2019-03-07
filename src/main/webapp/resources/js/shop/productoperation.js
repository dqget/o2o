$(function () {
    var productId = getQueryString('productId');
    //通过productId查询商品信息
    var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
    //
    var categoryInfoUrl = '/o2o/shopadmin/getproductcategorylist';
    //
    var productUrl = '/o2o/shopadmin/addproduct';

    if (productId) {
        getInfo(productId);
        productUrl = '/o2o/shopadmin/modifyproduct';
    } else {
        getProductCategory();
    }

    //获取需要的商品信息，并赋值给表单的
    function getInfo(id) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#product-priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);
                $('#point').val(product.point);
                //获取原本商品类别以及该店铺的所有商品类别列表
                var optionHtml = '';
                var optionArr = data.productCategoryList;
                var optionSelected = product.productCategory.productCategoryId;
                //生成前端的Html商品类别列表
                optionArr.map(function (item, index) {
                    var isSelect = optionSelected === item.productCategoryId ? 'selected' : '';
                    optionHtml += '<option data-value="'
                        + item.productCategoryId
                        + '"'
                        + isSelect
                        + '>'
                        + item.productCategoryName
                        + '</option>';
                });
                $('#product-category').html(optionHtml);
            }
        });
    }

    function getProductCategory() {
        $.getJSON(categoryInfoUrl, function (data) {
            if (data.success) {
                var productCategoryList = data.data;
                var optionHtml = '';
                productCategoryList.map(function (item, index) {
                    optionHtml += '<option data-value="'
                        + item.productCategoryId
                        + '">' + item.productCategoryName + "</option>";
                });
                $('#product-category').html(optionHtml);
            }
        });
    }

    $('#submit').click(function () {
        var product = {};
        product.productName = $('#product-name').val();
        product.productDesc = $('#product-desc').val();
        product.priority = $('#product-priority').val();
        product.normalPrice = $('#normal-price').val();
        product.promotionPrice = $('#promotion-price').val();
        product.point = $('#point').val();
        product.productId = productId;
        product.productCategory = {
            productCategoryId: $('#product-category').find('option').not(
                function () {
                    return !this.selected;
                }).data('value')
        };
        var thumbnail = $('#small-img')[0].files[0];
        var formData = new FormData();
        formData.append('thumbnail', thumbnail);
        $('.detail-img').map(function (index, item) {
            if ($('.detail-img')[index].files.length > 0) {
                formData.append('productImg' + index, $('.detail-img')[index].files[0]);
            }
        });
        formData.append('productStr', JSON.stringify(product));
        var verifyCodeActual = $('#j_kaptcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码!');
            return;
        }
        formData.append('verifyCodeActual', verifyCodeActual);
        $.ajax({
            url: productUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                    history.back(-1);
                } else {
                    $.toast('提交失败！');
                }
                $('#kaptcha_img').click();
            }
        })
    });
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });
});