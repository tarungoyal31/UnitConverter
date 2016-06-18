package com.tarungoyaldev.android.unitconverter.Converter;

import com.tarungoyaldev.android.unitconverter.ConverterHelper;

import java.util.HashMap;

/**
 * Handles unit conversion content. Names and values of Units are defined in {@link android.R.array}
 */
public class UnitConverterContentHandler implements ConverterContentHandler {

    private final String title;
    private final String[] namesArray;
    private final HashMap<String, Double> nameValueMap = new HashMap<>(0);

    public UnitConverterContentHandler(String[] namesArray, String[] valuesArray, String title)
            throws InvalidDataException{
        this.namesArray = namesArray;
        this.title = title;
        if (namesArray.length != valuesArray.length) {
            throw new InvalidDataException(
                    String.format("Length of names and values array for %s does not match", title));
        }
        int arrayLength = namesArray.length;
        for (int i = 0; i < arrayLength; i++) {
            nameValueMap.put(namesArray[i], ConverterHelper.convertStringToDouble(valuesArray[i]));
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

    public double getUnitValue(String name) {
        return nameValueMap.get(name);
    }

    public double getUnitValueAt(int pos) {
        return nameValueMap.get(namesArray[pos]);
    }

    @Override
    public double conversionResult(double value, int firstPos, int secondPos) {
        return value * getUnitValueAt(secondPos) / getUnitValueAt(firstPos);
    }
}
