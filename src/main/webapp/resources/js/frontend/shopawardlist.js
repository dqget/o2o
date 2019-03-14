$(function () {
    var loading = false;
    var maxItems = 20;
    var pageSize = 2;
    var pageNum = 1;
    var awardName = '';

    var shopId = getQueryString("shopId");
    var getAwardListUrl = '/o2o/frontend/getawardlist';
    //查询奖品列表
    addItems(pageSize, pageNum);

    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        var url = getAwardListUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&shopId=' + shopId
            + '&awardName=' + awardName;

        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                console.log(data);
                maxItems = data.count;
                var html = '';
                data.data.awardList.map(function (item, index) {
                    html += '' + '<div class="card" data-product-id='
                        + item.productId + '>'
                        + '<div class="card-header">' + item.awardName
                        + '</div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + getContextPath() + item.awardImg + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.awardDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + '更新</p>' + '<span>' + item.point + '积分</span>'
                        + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                $('.list-div').append(html);
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
            }
        });
    }

    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });
    //失去焦点时执行
    $('#search').blur(function (e) {
        awardName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });
    $('#me').click(function () {
        $.openPanel('#panel-js-demo');
    });
    $.init();
});