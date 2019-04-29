$(function () {
    var productName = '';
    getList();
    getProductSellDailyList();

    function getList() {
        //获取用户购买信息的URL
        var listUrl = '/o2o/shopadmin/listuserproductmapbyshop?pageIndex=1&pageSize=999&productName=' + productName;

        //访问后台，获取该店铺的购买信息列表
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var userProductMapList = data.data.userProductMapList;
                console.log(userProductMapList);

                var tempHtml = '';
                //遍历购买信息，拼接处列信息
                userProductMapList.map(function (item, index) {
                    tempHtml += '<div class="row row-productbuycheck">'
                        + '<div class="col-10">' + item.product.productName + '</div>'
                        + '<div class="col-40 productbuycheck-time">'
                        + new Date(item.createTime).Format("yyyy-MM-dd hh:mm:ss") + '</div>'
                        + '<div class="col-20">' + item.user.name + '</div>'
                        + '<div class="col-10">' + item.point + '</div>'
                        + '<div class="col-20">' + (item.operator != null ? item.operator.name : '线上支付') + '</div></div>'
                });
                $(".productbuycheck-wrap").html(tempHtml);
            }
        })
    }

    $('#search').on('change', function (e) {
        productName = e.target.value;
        $(".productbuycheck-wrap").empty();
        getList();
    });


    function getProductSellDailyList() {
        //获取该店铺商品7天销售的URL
        var listProductSellDailyListUrl = '/o2o/shopadmin/listproductselldailybyshop';
        //访问后台，该店铺商品7天销售的URL
        $.getJSON(listProductSellDailyListUrl, function (data) {
            if (data.success) {
                var myChart = echarts.init(document.getElementById('chart'));
                //生成静态的Echart信息
                var option = generateStaticEchartPart();
                //遍历销量统计列表，动态设定echarts的值
                option.legend.data = data.legendData;
                option.xAxis = data.xAxis;
                option.series = data.series;
                myChart.setOption(option)
            }
        });
    }

    /**
     *
     * 生成静态的Echart信息
     */
    function generateStaticEchartPart() {
        /**
         * echarts
         */
        var option = {
            //提示框，鼠标悬浮交互时的信息提示
            tooltip: {
                trigger: 'axis',
                axisPointer: {//坐标轴提示器，坐标轴触发有效
                    type: 'shadow'//鼠标移动到轴的时候，显示阴影
                }
            },
            //图例，每个图表最多仅有一个图例
            legend: {
                data: []
            },
            //直角坐标系内绘制网格
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            //直角坐标系中横轴数组，数组中的每一项代表一条横轴坐标轴
            xAxis: [{
                //类目型：需要指定类目列表，坐标轴内有且仅有这些指定类目坐标
            }],
            //直角坐标系中纵轴数组，数组中的每一项代表一条纵轴坐标轴
            yAxis: [{
                //类目型：需要指定类目列表，坐标轴内有且仅有这些指定类目坐标
                type: 'value'
            }],
            //驱动图标生成的数据内容数组，数组中每一项为一个系列的选项及数据
            series: []
        };
        return option;
    }
});