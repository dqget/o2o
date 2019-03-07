package com.lovesickness.o2o.web.shopadmin;

import com.lovesickness.o2o.dto.EchartSeries;
import com.lovesickness.o2o.dto.EchartXAxis;
import com.lovesickness.o2o.dto.UserProductMapExecution;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.ProductSellDaily;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserProductMap;
import com.lovesickness.o2o.service.ProductSellDailyService;
import com.lovesickness.o2o.service.UserProductMapService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 懿
 */
@Controller
@RequestMapping("/shopadmin")
public class UserProductMapController {
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;

    @GetMapping("/listuserproductmapbyshop")
    @ResponseBody
    public ResultBean<UserProductMapExecution> listUserProductMapByShop(HttpServletRequest request) {
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空置判断
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setShop(currentShop);
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                Product product = new Product();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }
            return new ResultBean<>(userProductMapService.listUserProductMap(userProductMapCondition, pageIndex, pageSize));
        } else {
            return new ResultBean<>(false, 0, "empty pageIndex or pageSize or shopId");
        }
    }

    @GetMapping(value = "/listproductselldailybyshop")
    @ResponseBody
    public Map<String, Object> listProductSellDailyByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(16);
        //获取当前的店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空置校验
        if (currentShop != null && currentShop.getShopId() != null) {
            //添加查询条件
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);
            Calendar calendar = Calendar.getInstance();
            //获取昨天的日期
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            //获取七天前的日期
            calendar.add(Calendar.DATE, -6);
            Date beginTime = calendar.getTime();
            //根据传入的查询条件获取该店铺的商品销售情况
            List<ProductSellDaily> productSellDailyList = productSellDailyService.listProductSellDaily(productSellDailyCondition, beginTime, endTime);
            //指定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //商品名列表，保证唯一性
            HashSet<String> legendData = new HashSet<>();
            //x周数组
            HashSet<String> xData = new HashSet<>();
            //定义series
            List<EchartSeries> series = new ArrayList<>();
            //日销量列表
            List<Integer> totalList = new ArrayList<>();
            //当前商品名默认为空
            String currentProductName = "";
            for (int i = 0; i < productSellDailyList.size(); i++) {
                ProductSellDaily productSellDaily = productSellDailyList.get(i);
                //自动去重
                legendData.add(productSellDaily.getProduct().getProductName());
                xData.add(sdf.format(productSellDaily.getCreateTime()));
                if (i != 0 && !currentProductName.equals(productSellDaily.getProduct().getProductName()) && !currentProductName.isEmpty()) {
                    //如果currentProductName不等于获取的商品名
                    //则是遍历到下一个商品的日销量信息了，将前一轮遍历的信息放入series当中，
                    //包括了商品名以及与商品对应的统计日期以及当日销量
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                    //重置totalList
                    totalList = new ArrayList<>();
                    //currentProductName 为当前的productName
                    currentProductName = productSellDaily.getProduct().getProductName();
                    //继续添加新的值
                    totalList.add(productSellDaily.getTotal());
                } else {
                    //如果还是当前的productName
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getProductName();
                }
                //队列之末，需要将最后一个
                if (i == productSellDailyList.size() - 1) {
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                }
            }
            modelMap.put("series", series);
            modelMap.put("legendData", legendData);
            //拼接出xAxis
            List<EchartXAxis> xAxis = new ArrayList<>();
            EchartXAxis exa = new EchartXAxis();
            exa.setData(xData);
            xAxis.add(exa);
            modelMap.put("xAxis", xAxis);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");

        }
        return modelMap;
    }
}
