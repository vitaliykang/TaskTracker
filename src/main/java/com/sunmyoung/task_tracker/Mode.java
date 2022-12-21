package com.sunmyoung.task_tracker;

import lombok.Getter;
import lombok.Setter;

public enum Mode {
    MANAGEMENT,
    PRODUCTION {
        @Override
        public String getMainScreen() {
            return Dialogs.MAIN_SCREEN_P;
        }
    };

    @Getter @Setter
    private static Mode currentMode;

    public String getMainScreen() {
        return Dialogs.MAIN_SCREEN_M;
    }
}
