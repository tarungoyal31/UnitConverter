package com.tarungoyaldev.android.unitconverter;

import android.util.Log;

import java.text.DecimalFormat;

/**
 * Helper methods for Converter.
 */
public class ConverterHelper {

    public static String convertDoubleToString(double value) {
        if (value > 1E10 || value < 1E-10 && value > 1E-50) {
            return (new DecimalFormat("0.#####E0")).format(value);
        } else if (value > 10){
            return (new DecimalFormat("0.####")).format(value);
        } else {
            return (new DecimalFormat("0.#######")).format(value);
        }
    }

    public static String convertDoubleToSmallString(double value) {
        return (new DecimalFormat("0.##")).format(value);
    }

    public static double convertStringToDouble(String valueString) {
        try {
            if (valueString == null && valueString == "") {
                return 0;
            } else if (valueString.equals("∞")) {
                return Double.POSITIVE_INFINITY;
            } else if (valueString.equals("-∞")) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return Double.valueOf(valueString);
            }
        } catch (NumberFormatException e) {
            Log.e("calculatorUtilities", "Invalid Double: ", e);
            return 0;
        }
    }
}
