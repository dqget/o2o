$(function () {
    //console.log('shopId='+getQueryString('shopId'));
    const shopId = getQueryString('shopId');
    const shopInfoUrl = '/o2o/shopadmin/getshopmanagementinfo?shopId=' + shopId;
    $.getJSON(shopInfoUrl, function (data) {
        if (data.redirect) {
            window.location.href = data.url;
        } else {
            if (data.shopId !== undefined && data.shopId != null) {
                shopId = data.shopId;
            }
            const html = '/o2o/shopadmin/shopoperation?shopId=' + shopId;
            // $('#shopInfo').attr('href','/o2o/shopadmin/shopoperation?shopId='+shopId);
            $('#shopInfo').attr('onclick', 'window.location.href="' + html + '"');
            // $('#shopInfo').click(function () {
            //     window.location.href = '/o2o/shopadmin/shopoperation?shopId=' + shopId;
            // });
        }
    });

});