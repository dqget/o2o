package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Schedule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScheduleDao {

    List<Schedule> queryScheduleList(@Param("scheduleCondition") Schedule schedule,
                                     @Param("rowIndex") int rowIndex,
                                     @Param("pageSize") int pageSize);

    Integer queryScheduleCount(@Param("scheduleCondition") Schedule schedule);

    Schedule queryScheduleById(long scheduleId);

    int updateSchedule(Schedule schedule);

    int insertSchedule(Schedule schedule);


}
