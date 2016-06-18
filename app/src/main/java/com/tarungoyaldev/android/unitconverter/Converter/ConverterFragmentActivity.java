package com.tarungoyaldev.android.unitconverter.Converter;

/**
 * Interface to be implemented for using ConversionFragment
 */
public interface ConverterFragmentActivity {
    ConverterContentHandler getConverterContentHandler(int position, ConversionFragment fragment);
}
