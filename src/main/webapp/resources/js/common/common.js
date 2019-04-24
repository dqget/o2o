/**
 *
 */
function changeVerifyCode(img) {
    img.src = "../Kaptcha?" + Math.floor(Math.random() * 100);
}

function getContextPath() {
    return "/o2o/"
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return '';
}


Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds()
        // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k])
                : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
//订单对象
var order = {
    orderId: 0,
    orderNumber: '',
    userId: 0,
    payPrice: '',
    isPay: 0,
    payTime: null,
    isReceipt: 0,
    receiptTime: null,
    receivePhone: '',
    receiveAddr: '',
    receiveName: '',
    trackNumber: '',
    isShip: 0,
    shipTime: null,
    status: 1,
    createTime: null,
    updateTime: null,
    orderProductsMapList: []
};
var orderProductsMap = {
    orderProductId: 0,
    orderId: 0,
    productId: 0,
    productNum: 0,
    productPrice: '',
    isEvaluation: 0,
    starLevel: 0,
    evaluationId: 0,
    status: 1,
    createTime: null,
    updateTime: null


};