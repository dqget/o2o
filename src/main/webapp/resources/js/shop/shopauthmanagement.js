$(function () {
    var listUrl = '/o2o/shopadmin/getshopauthmaplistbyshop?pageSize=999&pageIndex=1';
    var modifyUrl = '/o2o/shopadmin/modifyshopauthmap';
    getlist();

    function getlist() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var shopAuthList = data.data.shopAuthMapList;
                var tempHtml = '';
                console.log(data);
                shopAuthList.map(function (item, index) {
                    var textOr = '恢复';
                    var contraryStatus = 0;
                    if (item.enableStatus === 1) {
                        textOr = '删除';
                        contraryStatus = 0;
                    } else {
                        contraryStatus = 1;
                    }
                    tempHtml += '<div class="row row-shopauth">' +
                        '<div class="col-40">' + item.employee.name +
                        '</div>';
                    if (item.titleFlag != 0) {
                        //若不是店家本人
                        tempHtml += '<div class="col-20">' + item.title + '</div>'
                            + '<div class="col-40">' +
                            '<a href="#" class="edit" data-employee-id="' + item.employee.userId +
                            '" data-auth-id ="' + item.shopAuthId + '">编辑</a>'
                            + '<a href="#" class="status" data-auth-id="' + item.shopAuthId + '"'
                            + 'data-status="' + contraryStatus + '">' + textOr + '</a></div>';
                    } else {
                        //若为店家
                        tempHtml += '<div class="col-20">'
                            + item.title
                            + '</div><div class="col-40">'
                            + '<span>不可操作</span></div>';
                    }
                    tempHtml += '</div>';
                });
                $('.shopauth-wrap').html(tempHtml);
            }
        });
    }

    function changeState(authId, status) {
        var shopAuth = {};
        shopAuth.shopAuthId = authId;
        shopAuth.enableStatus = status;
        $.confirm('确定吗？', function () {
            $.ajax({
                url: modifyUrl,
                type: 'POST',
                data: {
                    shopAuthMapStr: JSON.stringify(shopAuth),
                    statusChange: true,
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('操作成功！');
                        getlist();
                    } else {
                        $.toast('操作失败！');
                    }
                }
            })
        })
    }

    $('.shopauth-wrap').on('click', 'a', function (e) {
        var target = $(e.currentTarget);
        if (target.hasClass('edit')) {
            window.location.href = '/o2o/shopadmin/shopauthedit?shopAuthId=' + e.currentTarget.dataset.authId;
        } else if (target.hasClass('status')) {
            changeState(e.currentTarget.dataset.authId, e.currentTarget.dataset.status);
        }
    });

});
