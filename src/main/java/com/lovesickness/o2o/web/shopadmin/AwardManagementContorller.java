package com.lovesickness.o2o.web.shopadmin;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.dto.AwardExecution;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.entity.Award;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.service.AwardService;
import com.lovesickness.o2o.util.CodeUtil;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/shopadmin")
@Api(tags = "AwardManagementContorller|奖品操作控制器")
public class AwardManagementContorller {
    @Autowired
    private AwardService awardService;

    @GetMapping("/getawardbyid")
    @ApiOperation(value = "根据奖品Id查询奖品信息", notes = "查询奖品信息")
    public ResultBean<Award> getAwardById(@RequestParam(value = "awardId") Long awardId) {
        return new ResultBean<>(awardService.getAwardById(awardId));
    }


    @GetMapping("/getawardlistbyshop")
    @ApiOperation(value = "查询店铺下的奖品列表", notes = "查询该店铺下的奖品列表")
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
    @ApiOperation(value = "添加奖品", notes = "向该店铺下查询一个奖品信息")
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
    @ApiOperation(value = "修改奖品", notes = "修改奖品信息")
    public ResultBean<AwardExecution> modifyAward(
            @RequestParam(value = "thumbnail", required = false) MultipartFile file,
            HttpServletRequest request) {
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            return new ResultBean<>(false, 0, "输入了错误的验证码");
        }
        ImageHolder thumbnail = null;
        if (file != null) {
            try {
                thumbnail = new ImageHolder(file.getInputStream(), file.getOriginalFilename());
            } catch (IOException e) {
                return new ResultBean<>(e);
            }
        }

        String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
        Award award;
        award = JSON.parseObject(awardStr, Award.class);
        if (award != null) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            award.setShopId(currentShop.getShopId());
            return new ResultBean<>(awardService.modifyAward(award, thumbnail));
        } else {
            return new ResultBean<>(false, 0, "请输入奖品信息");
        }

    }
}
