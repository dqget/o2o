package com.lovesickness.o2o.web.shopadmin;

import com.lovesickness.o2o.dto.ScheduleExecution;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Schedule;
import com.lovesickness.o2o.entity.ScheduleDistribution;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.enums.ScheduleStateEnum;
import com.lovesickness.o2o.service.ScheduleService;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shopadmin")
@Api(tags = "ScheduleManagementController|后台管理系统预定记录操作控制器")
public class ScheduleManagementController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/modifyscheduledistributionbyshop")
    @ApiOperation(value = "店家修改预定配送记录", notes = "店家修改预定配送记录")
    public ResultBean<ScheduleExecution> modifyScheduleByUser(@RequestBody ScheduleDistribution scheduleDistribution,
                                                              HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (currentShop == null || user == null || currentShop.getShopId() == null || user.getUserId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        scheduleDistribution.setOperator(user);
        ScheduleExecution se = scheduleService.modifyScheduleDistributionByShop(scheduleDistribution);
        if (se.getState() == ScheduleStateEnum.SUCCESS.getState()) {
            return new ResultBean<>(true, ResultBean.SUCCESS, se.getStateInfo());
        } else {
            return new ResultBean<>(false, ResultBean.FAIL, se.getStateInfo());
        }
    }

    @GetMapping("/getschedulelistbyshop")
    @ApiOperation(value = "店家可以根据用户名称查询在即", notes = "店家可以根据购买用户名称查询预定记录")
    public ResultBean<ScheduleExecution> getScheduleList(@RequestParam(value = "userName") String userName,
                                                         @RequestParam(value = "pageIndex") Integer pageIndex,
                                                         @RequestParam(value = "pageSize") Integer pageSize,
                                                         HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        Schedule scheduleCondition = new Schedule();
        scheduleCondition.setShop(currentShop);
        if (userName != null && !"".equals(userName)) {
            PersonInfo user = new PersonInfo();
            user.setName(userName);
            scheduleCondition.setUser(user);
        }
        return new ResultBean<>(scheduleService.getScheduleList(scheduleCondition, pageIndex, pageSize));
    }

    @GetMapping("/getschedulebyshop")
    @ApiOperation(value = "用户查询单个预定记录，查询预定记录详情", notes = "根据预定记录id查询详情")
    public ResultBean<Schedule> getSchedule(@RequestParam(value = "scheduleId") Long scheduleId,
                                            HttpServletRequest request) {
        return new ResultBean<>(scheduleService.getScheduleById(scheduleId));
    }


    @GetMapping("/getscheduledistributionbyshop")
    @ApiOperation(value = "店家根据Id查询预定配送记录", notes = "根据预Id查询预定配送记录")
    public ResultBean<ScheduleDistribution> getScheduleDistribution(
            @RequestParam(value = "scheduleDistributionId") Long scheduleDistributionId,
            HttpServletRequest request) {
        return new ResultBean<>(scheduleService.getScheduleDistributionById(scheduleDistributionId));
    }
}
