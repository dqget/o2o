package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.ScheduleExecution;
import com.lovesickness.o2o.entity.Schedule;
import com.lovesickness.o2o.exception.ScheduleOperationException;

public interface ScheduleService {
    ScheduleExecution addSchedule(Schedule schedule) throws ScheduleOperationException;

    ScheduleExecution getScheduleList(Schedule scheduleCondition, Integer pageIndex, Integer pageSize);

    Schedule getScheduleById(Integer scheduleId);
}
