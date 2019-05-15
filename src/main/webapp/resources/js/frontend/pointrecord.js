$(function () {
    let loading = false;
    let maxItems = 20;
    let pageSize = 5;
    let pageNum = 1;
    let awardName = '';
    let getUserAwardMapListUrl = '/o2o/frontend/getuserawardmaplistbyuser';
    //查询奖品列表
    addItems(pageSize, pageNum);

    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        let url = getUserAwardMapListUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&awardName=' + awardName;

        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                console.log(data);
                maxItems = data.count;
                let html = '';
                data.data.userAwardMapList.map(function (item, index) {
                    let status = '';
                    if (item.usedStatus == 1) {
                        status = '已领取';
                    } else if (item.usedStatus == 0) {
                        status = '未领取';
                    }
                    html += '<div class="card" data-user-award-id='
                        + item.userAwardId + '>'
                        + '<div class="card-header">' + item.shop.shopName
                        + '<span class="pull-right">' + status + '</span> '
                        + '</div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + getContextPath() + item.award.awardImg + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.award.awardName
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.createTime).Format("yyyy-MM-dd")
                        + '</p><span>消耗积分:' + item.point + '</span></div></div>';

                });
                $('.list-div').append(html);
                let total = $('.list-div .card').length;
                maxItems = data.data.count;
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    // $.detachInfiniteScroll($('.infinite-scroll'));
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

    $('.point-record-list').on('click', '.card', function (e) {
        let userAwardId = e.currentTarget.dataset.userAwardId;
        console.log(userAwardId);
        // window.location.href = '/o2o/frontend/myawarddeatil?userAwardId=' + userAwardId;


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