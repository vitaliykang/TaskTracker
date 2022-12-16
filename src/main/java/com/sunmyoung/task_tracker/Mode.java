package com.sunmyoung.task_tracker;

import lombok.Getter;
import lombok.Setter;

public enum Mode {
    MANAGEMENT {

    },

    PRODUCTION {

    };

    @Getter @Setter
    private static Mode currentMode;
}
