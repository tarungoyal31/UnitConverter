package com.tarungoyaldev.android.unitconverter.Converter;

/**
 * Handler class for handling the content of the Spinner in {@link ConversionFragment}
 */
public interface ConverterContentHandler {

    /**
     * @return Returns the title for current {@link ConversionFragment}
     */
    String getTitle();

    /**
     * @return name of the Units to be populated in conversion spinners
     */
    String[] getNamesArray();

    /**
     * Returns result value by converting value using the name of units.
     *
     * @param value Value to be converted.
     * @param firstPos position of the first unit in the unit arrays.
     * @param secondPos position of the second unit in the unit arrays.
     * @return Resultant value after applying the conversion.
     */
    double conversionResult(double value, int firstPos, int secondPos);
}
