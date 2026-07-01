package com.finance.project.modules.scheduling.domain;

import org.springframework.stereotype.Repository;
import com.finance.project.modules.scheduling.domain.Scheduling;
import com.finance.project.domainLayer.domainEntities.vosShared.ScheduleID;

import java.util.List;


@Repository
public interface IScheduleRepository {

    boolean saveScheduling(Scheduling scheduling);

    Scheduling findSchedulingByScheduleID(ScheduleID scheduleID);

    int countSchedulings();

    boolean checkIfScheduleIDExists(ScheduleID scheduleID);

    List<Scheduling> getSchedulings();
}
