package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.ScheduleExecution;
import com.lovesickness.o2o.entity.Schedule;
import com.lovesickness.o2o.entity.ScheduleDistribution;
import com.lovesickness.o2o.exception.ScheduleOperationException;

public interface ScheduleService {
    ScheduleExecution addSchedule(Schedule schedule) throws ScheduleOperationException;

    ScheduleExecution getScheduleList(Schedule scheduleCondition, Integer pageIndex, Integer pageSize);

    Schedule getScheduleById(long scheduleId);

    ScheduleExecution modifySchedule(Schedule schedule) throws ScheduleOperationException;

    ScheduleExecution modifyScheduleDistributionByUser(ScheduleDistribution scheduleDistribution) throws ScheduleOperationException;

    ScheduleExecution modifyScheduleDistributionByShop(ScheduleDistribution scheduleDistribution) throws ScheduleOperationException;

    ScheduleDistribution getScheduleDistributionById(long scheduleDistributionId);
}
