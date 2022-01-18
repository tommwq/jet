package com.tommwq.jet.util;

/**
 * 比较结果。
 */
public enum CompareResult {
    BELOW, EQUAL, ABOVE;

    /**
     * 高于或等于
     */
    public boolean isAboveOrEqual() {
        return isAbove() || isEqual();
    }

    /**
     * 低于或等于
     */
    public boolean isBelowOrEqual() {
        return isBelow() || isEqual();
    }

    /**
     * 低于
     */
    public boolean isBelow() {
        return this == BELOW;
    }

    /**
     * 等于
     */
    public boolean isEqual() {
        return this == EQUAL;
    }

    /**
     * 高于
     */
    public boolean isAbove() {
        return this == ABOVE;
    }
}
