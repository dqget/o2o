$(function () {
    var loading = false;
    var maxItems = 20;
    var pageSize = 99;

    var listUrl = '/o2o/frontend/getorderlistbyuser';

    var pageNum = 1;
    var keyWord = '';
    var status = 0;
    //加载10条商品信息
    addItems(pageSize, pageNum);

    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize  + '&keyWord=' + keyWord + '&status=' + status;
        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                console.log(data);


                var total = $('.list-div .card').length;
                maxItems = data.data.count;
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    // $.detachInfiniteScroll($('.infinite-scroll'));
                    // 隐藏加载提示符
                    $('.infinite-scroll-preloader').hide();
                } else {
                    $('.infinite-scroll-preloader').show();
                }
                pageNum += 1;
                loading = false;
                $.refreshScroller();
            }
        });
    }


    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    $('#shopdetail-button-div').on('click', '.button', function (e) {
        productCategoryId = e.target.dataset.productSearchId;
        if (productCategoryId) {
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                productCategoryId = '';
            } else {
                $(e.target).addClass('button-fill').siblings()
                    .removeClass('button-fill');
            }
            $('.list-div').empty();
            pageNum = 1;
            addItems(pageSize, pageNum);
        }
    });

    $('.list-div')
        .on(
            'click',
            '.card',
            function (e) {
                var productId = e.currentTarget.dataset.productId;
                window.location.href = '/o2o/frontend/productdetail?productId='
                    + productId;
            });

    // $('#search').on('change', function (e) {
    //     productName = e.target.value;
    //     $('.list-div').empty();
    //     pageNum = 1;
    //     addItems(pageSize, pageNum);
    // });
    //失去焦点时执行
    $('#search').blur(function (e) {
        keyWord = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });
    $('#me').click(function () {
        $.openPanel('#panel-js-demo');
    });
    $.init();
});
