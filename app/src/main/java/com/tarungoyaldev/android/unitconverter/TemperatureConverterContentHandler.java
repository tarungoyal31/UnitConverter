package com.tarungoyaldev.android.unitconverter;

import com.tarungoyaldev.android.unitconverter.Converter.ConverterContentHandler;

/**
 * Handles temperature conversion content. This supports conversion between Celsius, FarhenHeit and
 * Kelvin.
 */
public class TemperatureConverterContentHandler implements ConverterContentHandler {

    private final String title;
    private final String[] namesArray;

    public enum TemperatureUnits {
        CELSIUS,
        FARHENHEIT,
        KELVIN
    };

    public TemperatureConverterContentHandler(String title) {
        this.title = title;
        namesArray = new String[TemperatureUnits.values().length];
        for (int i=0; i < TemperatureUnits.values().length; i++) {
            namesArray[i] = TemperatureUnits.values()[i].name();
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String[] getNamesArray() {
        return namesArray;
    }

    @Override
    public double conversionResult(double value, int firstPos, int secondPos) {
        TemperatureUnits firstUnit = TemperatureUnits.valueOf(namesArray[firstPos]);
        TemperatureUnits secondUnit = TemperatureUnits.valueOf(namesArray[secondPos]);
        switch (firstUnit) {
            case CELSIUS:
                switch (secondUnit) {
                    case CELSIUS:
                        return value;
                    case KELVIN:
                        return value + 273.15;
                    case FARHENHEIT:
                        return (value * 9 / 5) + 32;
                }
            case KELVIN:
                switch (secondUnit) {
                    case CELSIUS:
                        return value - 273.15;
                    case KELVIN:
                        return value;
                    case FARHENHEIT:
                        return ((value - 273.15) * 9 / 5) + 32;
                }
            case FARHENHEIT:
                switch (secondUnit) {
                    case CELSIUS:
                        return (value - 32) * 5 / 9;
                    case KELVIN:
                        return (value - 32) * 5 / 9 + 273.15;
                    case FARHENHEIT:
                        return value;
                }
        }
        return -1;
    }
}
