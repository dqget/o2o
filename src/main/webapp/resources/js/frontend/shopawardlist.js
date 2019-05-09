$(function () {
    let loading = false;
    let maxItems = 20;
    let pageSize = 5;
    let pageNum = 1;
    let awardName = '';
    let totalPoint;
    let shopId = getQueryString("shopId");
    let getAwardListUrl = '/o2o/frontend/getawardlistbyshopid';
    let exchangeUrl = '/o2o/frontend/adduserawardmap';
    //查询奖品列表
    addItems(pageSize, pageNum);

    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        let url = getAwardListUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&shopId=' + shopId
            + '&awardName=' + awardName;

        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                console.log(data);
                maxItems = data.count;
                let html = '';
                data.data.awardList.map(function (item, index) {
                    html += '' + '<div class="card" data-award-id='
                        + item.awardId + ' data-point=' + item.point + '>'
                        + '<div class="card-header">' + item.awardName
                        + '<span class="pull-right">' + item.point + '积分</span> '
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
                        + '更新</p>';
                    if (data.data.totalPoint >= item.point) {
                        html += '<span>点击领取</span></div></div>';
                    } else {
                        html += '</div></div>';
                    }
                });
                $('.list-div').append(html);
                if (data.data.totalPoint != null) {
                    totalPoint = data.data.totalPoint;
                    $("#title").text('当前积分' + totalPoint);
                }
                let total = $('.list-div .card').length;
                maxItems = data.data.count;
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    // 隐藏加载提示符
                    $('.infinite-scroll-preloader').remove();
                    return;
                }
                pageNum += 1;
                loading = false;
                $.refreshScroller();
            }
        });
    }

    $('.award-list').on('click', '.card', function (e) {
        console.log(totalPoint);
        console.log(e.currentTarget.dataset.point);

        if (totalPoint != null && totalPoint >= e.currentTarget.dataset.point) {
            //则弹出操作框
            $.confirm('需要消耗' + e.currentTarget.dataset.point + '积分，确定操作吗', function () {
                $.ajax({
                    url: exchangeUrl,
                    type: "post",
                    dataType: "json",
                    data: {awardId: e.currentTarget.dataset.awardId},
                    success: function (data) {
                        console.log(data);
                        if (data.success) {
                            $.toast("兑换成功");
                            $("#title").text('当前积分' + (totalPoint - e.currentTarget.dataset.point));

                        } else {
                            $.toast("操作失败");

                        }
                    }

                })

            });
        } else {
            $.toast("积分不足");
        }
    });
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

    $.init();
});