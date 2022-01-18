package com.tommwq.jet.text;

public class DecimalNumberFormat {
    public static final int DEFAULT_DECIMAL = 2;
    private int decimal = DEFAULT_DECIMAL;

    public DecimalNumberFormat(int decimal) {
        this.decimal = decimal;
    }

    public DecimalNumberFormat() {
    }

    public void setDecimal(int decimal) {
        if (decimal < 0) {
            return;
        }

        this.decimal = decimal;
    }

    public String format(double value) {
        String fmt = String.format("%%.%df", decimal);
        return String.format(fmt, value);
    }

    public String format(float value) {
        return format((double) value);
    }

    public String format(int value) {
        return format((double) value);
    }

    public String format(long value) {
        return format((double) value);
    }
}
