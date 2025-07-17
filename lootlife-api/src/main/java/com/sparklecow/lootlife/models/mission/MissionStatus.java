package com.sparklecow.lootlife.models.mission;

public enum MissionStatus {
    PENDING,        // Mission assigned but not started
    ACTIVE,         // Mission in progress
    COMPLETED,      // Mission completed successfully
    FAILED,         // Mission failed (expired or not completed)
    EXPIRED,        // Mission expired without completion
    CANCELLED       // Mission cancelled by system or user
}
