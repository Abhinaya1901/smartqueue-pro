package com.smartqueue.model.enums;

import java.time.Duration;

public enum Priority {
    LOW(Duration.ofHours(24)),
    MEDIUM(Duration.ofHours(8)),
    HIGH(Duration.ofHours(4)),
    CRITICAL(Duration.ofHours(1));

    private final Duration slaDuration;

    Priority(Duration slaDuration) {
        this.slaDuration = slaDuration;
    }

    public Duration getSlaDuration() {
        return slaDuration;
    }
}