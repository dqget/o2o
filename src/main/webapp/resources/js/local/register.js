$(function() {
	let registerUrl = '/o2o/local/ownerregister';
	$('#submit').click(function() {
		let localAuth = {};
		let personInfo = {};
		localAuth.userName = $('#userName').val();
		localAuth.password = $('#password').val();
		// personInfo.phone = $('#phone').val();
		personInfo.email = $('#email').val();
		personInfo.name = $('#name').val();
		personInfo.gender = $('#gender').val();
		localAuth.personInfo = personInfo;
		let thumbnail = $('#small-img')[0].files[0];
		// console.log(thumbnail);
		let formData = new FormData();
		formData.append('thumbnail', thumbnail);
		formData.append('localAuthStr', JSON.stringify(localAuth));
		let verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		// console.log(personInfo);

		$.ajax({
			url : registerUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					window.location.href = '/o2o/local/login';
				} else {
					$.toast('提交失败！');
					$('#captcha_img').click();
				}
			}
		});
	});

	$('#back').click(function() {
		window.location.href = '/o2o/local/login';
	});
});
