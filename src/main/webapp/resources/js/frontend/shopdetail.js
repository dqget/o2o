$(function () {
    let loading = false;
    let maxItems = 20;
    let pageSize = 2;

    let listUrl = '/o2o/frontend/listproductbyshop';

    let pageNum = 1;
    let shopId = getQueryString('shopId');
    let productCategoryId = '';
    let productName = '';

    let searchDivUrl = '/o2o/frontend/listshopdetailpageinfo?shopId=' + shopId;
    $('#exchangelist').attr('href', '/o2o/frontend/awardlist?shopId=' + shopId);

    getSearchDivData();
    //加载10条商品信息
    addItems(pageSize, pageNum);


    function getSearchDivData() {
        // let url = searchDivUrl;
        $.getJSON(searchDivUrl, function (data) {

            if (data.success) {
                let shop = data.shop;
                $('#shop-cover-pic').attr('src', getContextPath() + shop.shopImg);
                $('#shop-update-time').html(new Date(shop.lastEditTime).Format("yyyy-MM-dd"));
                $('#shop-name').html(shop.shopName);
                $('#shop-desc').html(shop.shopDesc);
                $('#shop-addr').html(shop.shopAddr);
                $('#shop-phone').html(shop.phone);

                let productCategoryList = data.productCategoryList;
                let html = '';
                productCategoryList
                    .map(function (item, index) {
                        html += '<a class="button" data-product-search-id='
                            + item.productCategoryId+ '>'+ item.productCategoryName+ '</a>';
                    });
                $('#shopdetail-button-div').html(html);
            }
        });
    }


    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        let url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize +
            '&productCategoryId=' + productCategoryId + '&productName=' + productName + '&shopId=' + shopId;
        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                // console.log(data);
                maxItems = data.count;
                let html = '';
                data.data.productList.map(function (item, index) {
                    html += '' + '<div class="card" data-product-id='
                        + item.productId + '>'
                        + '<div class="card-header">' + item.productName
                        + '</div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + getContextPath() + item.imgAddr + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.productDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + '更新</p>' + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                $('.list-div').append(html);
                let total = $('.list-div .card').length;
                maxItems = data.data.count;
                if (total >= maxItems) {

                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    // 隐藏加载提示符
                    // console.log("111111111")
                    $('.infinite-scroll-preloader').remove();
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
                $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
            }
            $('.list-div').empty();
            pageNum = 1;
            addItems(pageSize, pageNum);
        }
    });

    $('.list-div').on('click', '.card', function (e) {
        let productId = e.currentTarget.dataset.productId;
        window.location.href = '/o2o/frontend/productdetail?productId=' + productId;
    });

    // $('#search').on('change', function (e) {
    //     productName = e.target.value;
    //     $('.list-div').empty();
    //     pageNum = 1;
    //     addItems(pageSize, pageNum);
    // });
    //失去焦点时执行
    $('#search').blur(function (e) {
        productName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    $.init();
});
