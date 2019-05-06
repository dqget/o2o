$(function () {
    let productId = getQueryString('productId');
    let addEvaUrl = '/o2o/evaluation/addevaluationByUser';
    const productUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId;
    let getEvaUrl = '/o2o/evaluation/getevaluation';
    let product;
    let pageIndex = 1;
    let pageSize = 999;
    let imgList = [];

    function getProductEva() {
        let url = getEvaUrl + "?productId=" + product.productId + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize;
        $.getJSON(url, function (data) {
            let evaluationList = data.data.evaluationList;
            let evaHtml = '';

            evaluationList.map(function (item, index) {
                evaHtml += '<li class="card"><div class="card-content">' +
                    '<div class="card-content-inner">'
                    + item.fromName + (item.toName != null ? '@' + item.toName : '') + ':' + item.content
                    + '<span class="pull-right">'
                    + new Date(item.createTime).Format("yyyy-MM-dd hh:mm")
                    + '</span></div></div><div class="card-footer" style="display:block;">' +
                    '<a href="#" fromName="' + item.fromName + '" fromUid="' + item.fromUid + '"' +
                    ' class="prompt-title-ok" style="float:right;">回复</a></div></li>';
            });
            $("#eva-list").html(evaHtml);
        });
    }

    $(document).on('click', '.prompt-title-ok', function (e) {
        let fromName = this.getAttribute("fromName");
        let fromUid = this.getAttribute("fromUid");
        console.log(this.value);
        $.prompt('请输入回复内容', '回复评论', function (value) {
            $.ajax({
                url: addEvaUrl,
                type: "POST",
                contentType: 'application/json',
                dataType: "json",
                data: JSON.stringify({product: product, toName: fromName, toUid: fromUid, content: value}),
                success: function (data) {
                    if (data.success) {
                        $.toast(data.msg);
                        location.reload();
                    } else {
                        $.toast(data.msg);

                    }
                },
                error: function (data) {

                }
            });
        });
    });

    // function send(from_name, from_uid) {
    //     let el = document.querySelector("#mubu")
    //     el.style.display = "block";
    //     setTimeout(function () {
    //         el.style.top = "0px";
    //     }, 0);
    //     el.style.transition = ".5s";
    //     el.addEventListener("transitionend", function () {
    //         let ele = document.querySelector("#mubu_el");
    //         ele.style.display = "block";
    //     });
    //
    //     // fromUid = from_uid;
    //     // fromName = from_name;
    // }

    $.getJSON(productUrl, function (data) {
        if (data.success) {
            product = data.product;
            getProductEva();
            $('#product-img').attr('src', getContextPath() + product.imgAddr);
            $('#product-time').text(
                new Date(product.lastEditTime)
                    .Format("yyyy-MM-dd"));
            if (product.point !== undefined) {
                $('#product-point').text('购买可获得' + product.point + '积分');
            }
            $('#product-name').text(product.productName);
            $('#product-desc').text(product.productDesc);
            if (product.normalPrice !== undefined && product.promotionPrice !== undefined) {
                $('#price').show();
                $('#normalPrice').html('<del>' + '￥' + product.normalPrice + '</del>');
                $('#promotionPrice').text('￥' + product.promotionPrice);
            }

            // var imgListHtml = '';
            product.productImgList.map(function (item, index) {
                // imgListHtml += '<img src="' + getContextPath() + item.imgAddr + '"/>';
                imgList.unshift(getContextPath() + item.imgAddr);
            });
            imgInit();
            // 生成购买商品的二维码供商家扫描
            //imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4product?productId='
            //    + product.productId + '"/></div>';
            // $('#imgList').html(imgListHtml);
        }
    });
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    function imgInit() {
        /*=== Popup ===*/
        let myPhotoBrowserPopup = $.photoBrowser({
            photos: imgList,
            type: 'popup'
        });
        $(document).on('click', '.pb-popup', function () {
            myPhotoBrowserPopup.open();
        });
    }

    $('#additem').click(function () {
        var url = '/o2o/frontend/updateitemtobuyercart';
        var buyerCartItem = {
            product: product,
            amount: 1
        };
        console.log(buyerCartItem);

        $.ajax({
            url: url,
            type: "post",
            contentType: 'application/json',
            data: JSON.stringify([buyerCartItem]),
            success: function (data) {
                if (data.data && data.success) {
                    $.toast("添加成功");
                } else {
                    $.toast(data.msg);
                }
            }
        });
    });

    $.init();

});


// function calce() {
//     let el = document.querySelector("#mubu")
//     let ele = document.querySelector("#mubu_el")
//     let ele_msg = document.querySelector(".mubu_msg")
//     ele_msg.value = "";
//     ele.style.display = "none";
//     el.style.top = "-667px";
//     el.style.display = "none";
//
// }
//
//
// function save() {
//     let el = document.querySelector(".btn .btn-save");
//     let content = document.querySelector(".mubu_msg").value;
//     // $.ajax({
//     //     url: addEvaUrl,
//     //     type: "POST",
//     //     contentType: 'application/json',
//     //     data: {content: content, product: product, toName: s, toUid:}
//     // });
//     console.log(fromUid + "," + fromName);
//     console.log(el)
//     // console.log(ele_msg)
//     console.log(product)
// }
