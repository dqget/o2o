package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dto.ScheduleExecution;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.Schedule;
import com.lovesickness.o2o.entity.ScheduleDistribution;
import com.lovesickness.o2o.enums.ScheduleStateEnum;
import com.lovesickness.o2o.service.ScheduleService;
import com.lovesickness.o2o.util.ALiPayUtil;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/frontend")
@Api(tags = "ScheduleController|前台展示系统预定商品操作的控制器")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/addscheduleandopenpay")
    @ApiOperation(value = "添加预定记录，并支付", notes = "添加预定记录,并唤醒支付界面")
    public void addSchedule(@RequestBody Schedule schedule,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null) {
            return;
        }
        schedule.setUser(user);
        //1.添加预定记录，和 预定配送记录
        ScheduleExecution se = scheduleService.addSchedule(schedule);
        if (ScheduleStateEnum.SUCCESS.getState() != se.getState()) {
            return;
        }

        //唤起支付
        ALiPayUtil.aliPay4Schedule(schedule, request, response);
        //支付回调 增加购买记录 增加用户积分
    }

    @GetMapping("/getschedulelist")
    @ApiOperation(value = "用户查询预定列表", notes = "用户查询预定列表")
    public ResultBean<ScheduleExecution> getScheduleList(@RequestParam(value = "productNum") String productNum,
                                                         @RequestParam(value = "pageIndex") Integer pageIndex,
                                                         @RequestParam(value = "pageSize") Integer pageSize,
                                                         HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null || user.getUserId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        Schedule scheduleCondition = new Schedule();
        scheduleCondition.setUser(user);
        if (productNum != null && !"".equals(productNum)) {
            Product product = new Product();
            product.setProductName(productNum);
            scheduleCondition.setProduct(product);
        }
        return new ResultBean<>(scheduleService.getScheduleList(scheduleCondition, pageIndex, pageSize));
    }

    @GetMapping("/getschedule")
    @ApiOperation(value = "用户查询单个预定记录，查询预定记录详情", notes = "根据预定记录id查询详情")
    public ResultBean<Schedule> getSchedule(@RequestParam(value = "scheduleId") Long scheduleId,
                                            HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null || user.getUserId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        return new ResultBean<>(scheduleService.getScheduleById(scheduleId));
    }

    @PostMapping("/modifyscheduledistributionbyuser")
    @ApiOperation(value = "用户修改预定配送记录", notes = "用户可以修改未配送的预定配送记录，包括配送人、手机、配送地址、配送时间")
    public ResultBean<ScheduleExecution> modifyScheduleByUser(@RequestBody ScheduleDistribution scheduleDistribution,
                                                              HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null || user.getUserId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        ScheduleExecution se = scheduleService.modifyScheduleDistributionByUser(scheduleDistribution);
        if (se.getState() == ScheduleStateEnum.SUCCESS.getState()) {
            return new ResultBean<>(true, ResultBean.SUCCESS, se.getStateInfo());
        } else {
            return new ResultBean<>(false, ResultBean.FAIL, se.getStateInfo());
        }
    }

}
