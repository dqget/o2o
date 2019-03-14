package com.lovesickness.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.dto.AwardExecution;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.entity.Award;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.service.AwardService;
import com.lovesickness.o2o.util.CodeUtil;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/shopadmin")
public class AwardManagementContorller {
    @Autowired
    private AwardService awardService;

    @GetMapping("/getawardbyid")
    public ResultBean<Award> getAwardById(@RequestParam(value = "awardId") Long awardId) {
        return new ResultBean<>(awardService.getAwardById(awardId));
    }


    @GetMapping("/getawardlistbyshop")
    public ResultBean<AwardExecution> getAwardListByShop(HttpServletRequest request) {
        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        Award award = null;
        if (currentShop != null && currentShop.getShopId() != null) {
            award = new Award();
            award.setShopId(currentShop.getShopId());
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            award.setAwardName(awardName);
        }
        return new ResultBean<>(awardService.queryAwardList(award, pageIndex, pageSize));
    }

    @PostMapping("/addaward")
    public ResultBean<AwardExecution> addAward(@RequestParam(value = "thumbnail") MultipartFile file, HttpServletRequest request) {
        if (!CodeUtil.checkVerifyCode(request)) {
            return new ResultBean<>(false, 0, "输入了错误的验证码");
        }
        ImageHolder thumbnail;
        try {
            thumbnail = new ImageHolder(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            return new ResultBean<>(e);
        }
        String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
        ObjectMapper mapper = new ObjectMapper();
        Award award;
        try {
            award = mapper.readValue(awardStr, Award.class);
        } catch (IOException e) {
            return new ResultBean<>(e);
        }
        if (award != null && thumbnail != null) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            award.setShopId(currentShop.getShopId());
            return new ResultBean<>(awardService.addAward(award, thumbnail));
        } else {
            return new ResultBean<>(false, 0, "请输入奖品信息");
        }
    }

    @PostMapping("/modifyaward")
    public ResultBean<AwardExecution> modifyAward(@RequestParam(value = "thumbnail") MultipartFile file, HttpServletRequest request) {
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            return new ResultBean<>(false, 0, "输入了错误的验证码");
        }
        ImageHolder thumbnail;
        try {
            thumbnail = new ImageHolder(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            return new ResultBean<>(e);
        }
        String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
        ObjectMapper mapper = new ObjectMapper();
        Award award;
        try {
            award = mapper.readValue(awardStr, Award.class);
        } catch (IOException e) {
            return new ResultBean<>(e);
        }
        if (award != null) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            award.setShopId(currentShop.getShopId());
            return new ResultBean<>(awardService.modifyAward(award, thumbnail));
        } else {
            return new ResultBean<>(false, 0, "请输入奖品信息");
        }
    }
}
