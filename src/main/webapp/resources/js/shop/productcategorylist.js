$(function () {
    var addUrl = '/o2o/shopadmin/addproductcategory';
    var listUrl = '/o2o/shopadmin/getproductcategorylist';
    var delUrl = '/o2o/shopadmin/removeproductcategory'
    getList();

    function getList(e) {
        $.ajax({
            url: listUrl,
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    handleList(data.data);
                }
            }
        });
    }

    function handleList(data) {
        var html = '';
        $('.product-category-wrap').html('');
        data.map(function (item, index) {
            html += '<div class="row row-product-category now"><div class="col-40">'
                + item.productCategoryName + '</div><div class="col-40">'
                + item.priority
                + '</div><div class="col-20"><a class="button delete" data-id="'
                + item.productCategoryId + '">删除</a></div></div>';

        });
        $('.product-category-wrap').append(html);
    }

    //新增按钮
    $('#new').click(function () {
        var tempHtml = '<div class="row row-product-category temp">'
            + '<div class="col-40"><input class="category-input category" type="text" placeholder="分类名"></div>'
            + '<div class="col-40"><input class="category-input priority" type="number" placeholder="优先级"></div>'
            + '<div class="col-20"><a href="#" class="button delete">删除</a></div>'
            + '</div>';
        $('.product-category-wrap').append(tempHtml);
    });
    //提交按钮
    $('#submit').click(function () {
        var tempArr = $('.temp');
        var productCategoryList = [];
        tempArr.map(function (index, item) {
            var tempObj = {};
            tempObj.productCategoryName = $(item).find('.category').val();
            tempObj.priority = $(item).find('.priority').val();
            if (tempObj.productCategoryName && tempObj.priority) {
                productCategoryList.push(tempObj);
            }
        });
        $.ajax({
            url: addUrl,
            type: 'POST',
            data: JSON.stringify(productCategoryList),
            contentType: 'application/json',
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                    getList();
                } else {
                    $.toast('提交失败！');
                }
            }
        });
    });
    //新增一行的删除按钮
    $('.product-category-wrap').on('click', '.row.row-product-category.temp .delete',
        function (e) {
            console.log($(this).parent().parent());
            $(this).parent().parent().remove();
        });
    $('.product-category-wrap').on('click', '.row.row-product-category.now .delete',
        function (e) {
            var target = e.currentTarget;
            $.confirm('确定删除？', function () {
                $.ajax({
                    url: delUrl,
                    type: 'POST',
                    data: {
                        productCategoryId: target.dataset.id
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            $.toast('删除成功');
                            //getList();
                            target.parentElement.parentElement.remove();
                        } else {
                            $.toast('删除失败');
                        }
                    }
                })
            })
        });

});