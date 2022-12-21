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

        @Override
        public String getTaskBoard() {
            return Dialogs.TASK_BOARD_P;
        }

        @Override
        public String getCreateOrderDialog() {
            return Dialogs.CREATE_ORDER_P;
        }
    };

    @Getter @Setter
    private static Mode currentMode;

    public String getMainScreen() {
        return Dialogs.MAIN_SCREEN_M;
    }

    public String getTaskBoard() {
        return Dialogs.TASK_BOARD_M;
    }

    public String getCreateOrderDialog() {
        return Dialogs.CREATE_ORDER_M;
    }
}
