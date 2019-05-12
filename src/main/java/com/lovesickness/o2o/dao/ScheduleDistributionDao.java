package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.ScheduleDistribution;

import java.util.List;

public interface ScheduleDistributionDao {
    int batchInsertScheduleDistribution(List<ScheduleDistribution> scheduleDistributionList);

    List<ScheduleDistribution> queryScheduleDistribution(long scheduleId);

    int updateScheduleDistribution(ScheduleDistribution scheduleDistribution);
}
