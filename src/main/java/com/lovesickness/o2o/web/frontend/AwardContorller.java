package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dto.UserAwardMapExecution;
import com.lovesickness.o2o.entity.Award;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserAwardMap;
import com.lovesickness.o2o.service.AwardService;
import com.lovesickness.o2o.service.UserAwardMapService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/frontend")
@Api(tags = "AwardContorller|奖品操作的控制器")
public class AwardContorller {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private AwardService awardService;

    /**
     * 用户使用积分兑换奖品
     */
    @PostMapping("/adduserawardmap")
    @ApiOperation(value = "添加用户兑换奖品记录", notes = "用户使用积分兑换奖品")
    public ResultBean<?> addUserAwardMap(@RequestParam(value = "awardId") long awardId,
                                         HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //构建用户兑换积分记录对象
        UserAwardMap userAwardMap = compactUserAwardMap4Add(user, awardId);
        return new ResultBean<>(userAwardMapService.addUserAwardMap(userAwardMap));
    }

    /**
     * 获取用户兑换奖品的记录列表
     * 可根据店铺id进行查询该店铺下的记录列表
     * 可根据奖品名称进行模糊查询
     */
    @GetMapping("/getuserawardmaplistbyuser")
    @ApiOperation(value = "根据用户查询用户兑换奖品的记录列表",
            notes = "可根据店铺id进行查询该店铺下的记录列表，可根据奖品名称进行模糊查询")
    public ResultBean<UserAwardMapExecution> getUserAwardMapListByUser(HttpServletRequest request) {
        //分页信息
        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //从Session中获取当前登录用户
        PersonInfo currentUser = (PersonInfo) request.getSession().getAttribute("user");
        UserAwardMap userAwardMap = null;
        if (currentUser != null && currentUser.getUserId() != null) {
            userAwardMap = new UserAwardMap();
            userAwardMap.setUser(currentUser);
            Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            if (shopId != null) {
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userAwardMap.setShop(shop);
            }
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }

        }
        return new ResultBean<>(userAwardMapService.queryUserAwardMapList(userAwardMap, pageIndex, pageSize));
    }

    private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
        UserAwardMap userAwardMap = new UserAwardMap();
        userAwardMap.setUser(user);
        Award award;
        if (awardId != null) {
            try {
                award = awardService.getAwardById(awardId);
                userAwardMap.setAward(award);
                userAwardMap.setPoint(award.getPoint());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
        Shop shop = new Shop();
        shop.setShopId(award.getShopId());
        userAwardMap.setShop(shop);

        return userAwardMap;
    }
}
