$(function () {

    const frontendInfoUrl = '/o2o/frontend/listmainpageinfo';
    $.getJSON(frontendInfoUrl, function (data) {
        console.log(data);
        if (data.success) {
            //获取头条列表信息
            let headLineList = data.headLineList;
            let swiperHtml = '';
            headLineList.map(function (item, index) {
                swiperHtml += '<div class="swiper-slide img-wrap">'
                    + '<a class="in external" href="' + item.lineLink
                    + '" ><img class="banner-img" src="' + getContextPath() + item.lineImg
                    + '" alt="' + item.lineName + '"></a></div>';
            });

            $('.swiper-wrapper').html(swiperHtml);
            $('.swiper-container').swiper({
                autoplay: 3000,
                //用户对轮播图进行操作时，是否自动停止autoplay
                autoplayDisableOnInteraction: false
            });
            let shopCategoryList = data.shopCategoryList;
            let categoryHtml = '';
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                    + '<div class="col-50 shop-classify" data-category=' + item.shopCategoryId + '>'
                    + '<div class="word">'
                    + '<p class="shop-title">' + item.shopCategoryName + '</p>'
                    + '<p class="shop-desc">' + item.shopCategoryDesc + '</p></div>'
                    + '<div class="shop-classify-img-warp">'
                    + '<img class="shop-img" src="' + getContextPath() + item.shopCategoryImg + '">'
                    + '</div></div>';
            });
            $('.row').html(categoryHtml);
        }
    });
    $.init();
    $('.row').on('click', '.shop-classify', function (e) {
        let shopCategoryId = e.currentTarget.dataset.category;
        window.location.href = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
    });

});