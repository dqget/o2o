$(function () {

    let awardListUrl = "/o2o/shopadmin/getawardlistbyshop?pageSize=999&&pageIndex=1";
    //商品下架的URL
    const statusUrl = '/o2o/shopadmin/modifyaward';
    getList();

    function getList() {

        $.ajax({
            url: awardListUrl,
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    console.log(data);
                    handleList(data.data.awardList);
                }
            }
        });
    }

    function handleList(data) {
        let html = '';
        data.map(function (item, index) {
            html += '<div class="row row-product">' +
                '<div class="col-40">' + item.awardName + '</div>' +
                '<div class="col-20">' + item.point + '</div>' +
                '<div class="col-40">' + editOrChangeOrPreviewProduct(item.awardId, item.enableStatus)
                + '</div></div>';

        });
        $('.award-wrap').html(html);
    }

    function editOrChangeOrPreviewProduct(productId, enableStatus) {
        let textOp = "下架";
        let contraryStatus = 0;
        if (enableStatus === 0) {
            textOp = "上架";
            contraryStatus = 1;
        } else {
            contraryStatus = 0;
        }
        let html = "";
        html =
            '<a href="#" class="edit" data-id="' + productId + '" data-status="' + enableStatus + '">编辑</a>' +
            '<a href="#" class="change" data-id="' + productId + '" data-status="' + contraryStatus + '">' + textOp + '</a>'
            + '<a href="#" class="preview" data-id="' + productId + '" data-status="' + enableStatus + '">预览</a>';
        return html;
    }

    $('.award-wrap').on('click', 'a', function (e) {
        let target = $(e.currentTarget);

        if (target.hasClass('edit')) {
            window.location.href = '/o2o/shopadmin/awardoperation?awardId=' + e.currentTarget.dataset.id;
        } else if (target.hasClass('change')) {
            //TODO
            changeItemStatus(e.currentTarget.dataset.id, e.currentTarget.dataset.status, e.currentTarget.innerText);
        } else if (target.hasClass('preview')) {
            // window.location.href = '/o2o/frontend/productdetail?productId='
            //     + e.currentTarget.dataset.id;
        }
    });

    function changeItemStatus(id, enableStatus, textOp) {
        let award = {};
        award.awardId = id;
        award.enableStatus = enableStatus;
        $.confirm('确定' + textOp + '商品吗?', function () {
            $.ajax({
                url: statusUrl,
                type: 'POST',
                data: {
                    awardStr: JSON.stringify(award),
                    statusChange: true
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('操作成功');
                        getList();
                    } else {
                        $.toast(data.msg);
                    }
                }
            });
        });
    }

    $('#new').click(function () {
        location.href = '/o2o/shopadmin/awardoperation';
    });
});
