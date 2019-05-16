$(function () {
    const getProductAveStarUrl = '/o2o/shopadmin/getproductavestar';
    getProductAveStar();

    function getProductAveStar() {
        $.ajax({
            url: getProductAveStarUrl,
            success: function (data) {
                let html = '';
                let productList = data.data;
                productList.map(function (item, index) {
                    html += '<div class="row row-product-star-list">'
                        + '<div class="col-20">' + parseInt(index + 1) + '</div>'
                        + '<div class="col-50">' + item.productName + '</div>' +
                        '<div class="col-30">' + item.aveStar + '</div></div>';
                });
                $('.list-div').html(html);

            }
        })
    }


});