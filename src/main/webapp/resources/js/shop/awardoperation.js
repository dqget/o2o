$(function () {
    let awardId = getQueryString('awardId');
    //通过awardId查询奖品信息
    let infoUrl = '/o2o/shopadmin/getawardbyid?awardId=' + awardId;
    //
    let awardUrl = '/o2o/shopadmin/addaward';

    if (awardId) {
        getInfo();
        awardUrl = '/o2o/shopadmin/modifyaward';
    }

    //获取需要的奖品信息，并赋值给表单的
    function getInfo() {
        $.getJSON(infoUrl, function (data) {
            console.log(data);
            if (data.success) {
                let award = data.data;
                $('#award-name').val(award.awardName);
                $('#award-desc').val(award.awardDesc);
                $('#award-priority').val(award.priority);
                $('#point').val(award.point);
            }
        });
    }


    $('#submit').click(function () {
        let award = {};
        award.awardName = $('#award-name').val();
        award.awardDesc = $('#award-desc').val();
        award.priority = $('#award-priority').val();
        award.point = $('#point').val();
        award.awardId = awardId;
        let thumbnail = $('#small-img')[0].files[0];
        let formData = new FormData();
        formData.append('thumbnail', thumbnail);
        $('.detail-img').map(function (index, item) {
            if ($('.detail-img')[index].files.length > 0) {
                formData.append('awardImg' + index, $('.detail-img')[index].files[0]);
            }
        });
        formData.append('awardStr', JSON.stringify(award));
        let verifyCodeActual = $('#j_kaptcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码!');
            return;
        }
        formData.append('verifyCodeActual', verifyCodeActual);

        $.ajax({
            url: awardUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                console.log(data);
                changeVerifyCode(document.querySelector("#kaptcha_img"));
                if (data.success) {
                    $.toast('提交成功！');
                    // history.back(-1);
                    //返回
                    location.href = document.referrer;
                } else {
                    $.toast('提交失败！');
                }
            },
            error: function () {
                changeVerifyCode(document.querySelector("#kaptcha_img"));
            }
        });
    });
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });
});