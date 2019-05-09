package com.lovesickness.o2o.web.shopadmin;

import com.lovesickness.o2o.dto.UserAwardMapExecution;
import com.lovesickness.o2o.entity.Award;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserAwardMap;
import com.lovesickness.o2o.enums.UserAwardMapStateEnum;
import com.lovesickness.o2o.service.UserAwardMapService;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value = "获取该店铺的用户兑换奖品记录列表", notes = "可根据奖品名称进行模糊查询")
    public ResultBean<UserAwardMapExecution> getUserAwardMapListByShop(@RequestParam(value = "awardName") String awardName,
                                                                       @RequestParam("pageIndex") int pageIndex,
                                                                       @RequestParam("pageSize") int pageSize,
                                                                       HttpServletRequest request) {
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        //分页信息
        //
        if (shop != null && shop.getShopId() != null) {
            UserAwardMap userAwardMap = new UserAwardMap();
            userAwardMap.setShop(shop);
            if (awardName != null && !"".equals(awardName)) {
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }

            return new ResultBean<>(userAwardMapService.queryUserAwardMapList(userAwardMap, pageIndex, pageSize));
        } else {
            return new ResultBean<>(false, 0, "empty shopId");
        }
    }

    @PostMapping("/modifyuserawardmap")
    @ApiOperation(value = "店家修改用户兑换奖品记录", notes = "修改兑换记录")
    public ResultBean<String> modifyUserAwardMap(@RequestBody UserAwardMap userAwardMap,
                                                 HttpServletRequest request) {
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //分页信息
        if (shop != null && shop.getShopId() != null) {
            if (userAwardMap != null && userAwardMap.getUserAwardId() != null) {
                userAwardMap.setOperator(user);
                UserAwardMap oldUserAwardMap = userAwardMapService.queryUserAwardMapById(userAwardMap.getUserAwardId());
                assert oldUserAwardMap != null;
                if (shop.getShopId().equals(oldUserAwardMap.getShop().getShopId())) {
                    UserAwardMapExecution uame = userAwardMapService.modifyUserAwardMap(userAwardMap);
                    if (uame.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                        return new ResultBean<>(true, ResultBean.SUCCESS, UserAwardMapStateEnum.SUCCESS.getStateInfo());
                    } else {
                        return new ResultBean<>(false, ResultBean.FAIL, uame.getStateInfo());

                    }
                } else {
                    return new ResultBean<>(false, ResultBean.FAIL, UserAwardMapStateEnum.OFFLINE.getStateInfo());
                }

            } else {
                return new ResultBean<>(false, ResultBean.FAIL, UserAwardMapStateEnum.EMPTY.getStateInfo());
            }
        } else {
            return new ResultBean<>(false, 0, "empty shopId");
        }
    }
}
