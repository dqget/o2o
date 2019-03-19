package com.lovesickness.o2o.web.shopadmin;

import com.lovesickness.o2o.dto.UserShopMapExecution;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserShopMap;
import com.lovesickness.o2o.service.UserShopMapService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shopadmin")
public class UserShopManagenmentController {
    @Autowired
    private UserShopMapService userShopMapService;

    @GetMapping("/getusershopmaplistbyshop")
    public ResultBean<UserShopMapExecution> getUserShopMapListByShop(HttpServletRequest request) {
        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        UserShopMap userShopMap = null;
        if (currentShop != null && currentShop.getShopId() != null) {
            userShopMap = new UserShopMap();
            userShopMap.setShop(currentShop);

            String userName = HttpServletRequestUtil.getString(request, "userName");
            if (userName != null) {
                PersonInfo buyer = new PersonInfo();
                buyer.setName(userName);
                userShopMap.setUser(buyer);
            }
        }
        return new ResultBean<>(userShopMapService.queryUserShopMapList(userShopMap, pageIndex, pageSize));
    }
}