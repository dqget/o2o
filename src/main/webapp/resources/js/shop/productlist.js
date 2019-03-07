$(function () {

    var productListUrl = "/o2o/shopadmin/getproductlistbyshop?pageSize=999&&pageIndex=0";
    //商品下架的URL
    var statusUrl = '/o2o/shopadmin/modifyproduct';
    getlist();

    function getlist(e) {
        $.ajax({
            url: productListUrl,
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    //console.log(data);
                    handleList(data.data.productList);
                }
            }
        });
    }

    function handleList(data) {
        var html = '';
        data.map(function (item, index) {
            html += '<div class="row row-product"><div class="col-40">'
                + item.productName + '</div><div class="col-20">'
                + item.point
                + '</div><div class="col-40">'
                + editOrChangeOrPreviewProduct(item.productId, item.enableStatus) + '</div></div>';

        });
        $('.product-wrap').html(html);
    }

    function editOrChangeOrPreviewProduct(productId, enableStatus) {
        var textOp = "下架";
        var contraryStatus = 0;
        if (enableStatus === 0) {
            textOp = "上架";
            contraryStatus = 1;
        } else {
            contraryStatus = 0;
        }
        var html = "";
        html = '<a href="#" class="edit" data-id="'
            + productId
            + '" data-status="'
            + enableStatus
            + '">编辑</a>'
            + '<a href="#" class="change" data-id="'
            + productId
            + '" data-status="'
            + contraryStatus
            + '">'
            + textOp
            + '</a>'
            + '<a href="#" class="preview" data-id="'
            + productId
            + '" data-status="'
            + enableStatus
            + '">预览</a>';
        return html;
    }

    $('.product-wrap').on('click', 'a', function (e) {
        var target = $(e.currentTarget);

        if (target.hasClass('edit')) {
            window.location.href = '/o2o/shopadmin/productoperation?productId=' + e.currentTarget.dataset.id;
        } else if (target.hasClass('change')) {
            //TODO
            changeItemStatus(e.currentTarget.dataset.id, e.currentTarget.dataset.status, e.currentTarget.innerText);
        } else if (target.hasClass('preview')) {
            // window.location.href = '/o2o/frontend/productdetail?productId='
            //     + e.currentTarget.dataset.id;
        }
    });

    function changeItemStatus(id, enableStatus, textOp) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('确定' + textOp + '商品吗?', function () {
            $.ajax({
                url: statusUrl,
                type: 'POST',
                data: {
                    productStr: JSON.stringify(product),
                    statusChange: true
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('操作成功');
                        getlist();
                    } else {
                        $.toast('操作失败');
                    }
                }
            });
        });
    }

    $('#new').click(function () {
        window.location.href = '/o2o/shopadmin/productoperation';
    });
});
