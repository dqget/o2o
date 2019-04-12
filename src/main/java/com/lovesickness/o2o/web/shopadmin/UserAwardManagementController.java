package com.lovesickness.o2o.web.shopadmin;

import com.lovesickness.o2o.dto.UserAwardMapExecution;
import com.lovesickness.o2o.entity.Award;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserAwardMap;
import com.lovesickness.o2o.service.UserAwardMapService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shopadmin")
@Api(tags = "UserAwardManagementController|用户奖品映射控制器")
public class UserAwardManagementController {
    @Autowired
    private UserAwardMapService userAwardMapService;

    /**
     * 获取该店铺的用户兑换奖品记录列表
     * 可根据奖品名称进行模糊查询
     */
    @GetMapping("/getuserawardmaplistbyshop")
    @ApiOperation(value = "获取该店铺的用户兑换奖品记录列表",notes = "可根据奖品名称进行模糊查询")
    public ResultBean<UserAwardMapExecution> getUserAwardMapListByShop(HttpServletRequest request) {
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        //分页信息
        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //
        if (shop != null && shop.getShopId() != null) {
            UserAwardMap userAwardMap = new UserAwardMap();
            userAwardMap.setShop(shop);
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }
            return new ResultBean<>(userAwardMapService.queryUserAwardMapList(userAwardMap, pageIndex, pageSize));
        } else {
            return new ResultBean<>(false, 0, "empty shopId");
        }
    }


}
