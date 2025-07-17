package com.sparklecow.lootlife.services.mission;

import com.sparklecow.lootlife.entities.Mission;

public interface MissionService {

    /*Utils*/
    boolean isCompleted(Mission mission);
    boolean isActive(Mission mission);
    boolean isExpired(Mission mission);
}
