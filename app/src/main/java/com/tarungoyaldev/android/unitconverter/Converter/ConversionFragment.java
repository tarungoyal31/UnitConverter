package com.tarungoyaldev.android.unitconverter.Converter;

import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.tarungoyaldev.android.unitconverter.ConverterHelper;
import com.tarungoyaldev.android.unitconverter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversionFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_TITLE = "section_title";

    private View rootView;
    private EditText firstEditText;
    private EditText secondEditText;
    private Spinner firstUnitSpinner;
    private Spinner secondUnitSpinner;
    private ConverterContentHandler converterContentHandler;
    private Bundle savedInstanceState;

    public ConversionFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ConversionFragment newInstance(int sectionNumber, String title) {
        ConversionFragment fragment = new ConversionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SECTION_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.savedInstanceState = savedInstanceState;
        rootView = inflater.inflate(R.layout.fragment_unit_converter, container, false);
        final GridLayout gridLayout =
                (GridLayout) rootView.findViewById(R.id.conversion_gridLayout);
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        for (int i = 0; i < gridLayout.getChildCount(); i++) {
                            View view = gridLayout.getChildAt(i);
                            GridLayout.LayoutParams params = (GridLayout.LayoutParams) view.getLayoutParams();
                            params.width = (gridLayout.getWidth()/gridLayout.getColumnCount()) -params.rightMargin - params.leftMargin;
                            params.height = (gridLayout.getHeight()/gridLayout.getRowCount()) -params.topMargin - params.bottomMargin;
                            view.setLayoutParams(params);
                        }
                    }
                });
        converterContentHandler = ((ConverterFragmentActivity) getActivity()).
                getConverterContentHandler(getArguments().getInt(ARG_SECTION_NUMBER), this);
        firstUnitSpinner = (Spinner) rootView.findViewById(R.id.firstUnit_spinner);
        secondUnitSpinner = (Spinner) rootView.findViewById(R.id.secondUnit_spinner);
        updateSpinners();

        firstEditText = (EditText) rootView.findViewById(R.id.firstValue_editText);
        secondEditText = (EditText) rootView.findViewById(R.id.secondValue_editText);
        firstEditText.setSaveEnabled(false);
        secondEditText.setSaveEnabled(false);
        initializeEditText(firstEditText);
        initializeEditText(secondEditText);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (firstEditText != null && firstUnitSpinner != null && secondUnitSpinner != null) {
            outState.putString("firstTextViewValue", firstEditText.getText().toString());
            outState.putInt("firstSpinnerSelectedPosition", firstUnitSpinner.getSelectedItemPosition());
            outState.putInt("secondSpinnerSelectedPosition", secondUnitSpinner.getSelectedItemPosition());
        }
        super.onSaveInstanceState(outState);
    }

    private void initializeEditText(EditText editText) {
        if (editText == firstEditText) {
            editText.addTextChangedListener(new ConversionTextWatcher(true));
        } else {
            editText.addTextChangedListener(new ConversionTextWatcher(false));
        }
        if (savedInstanceState != null && editText == firstEditText) {
            editText.setText(savedInstanceState.getString("firstTextViewValue"));
        }
    }

    private void initializeSpinner(Spinner spinner, int spinnerPosition) {
        ArrayAdapter<CharSequence> adapter;
        adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item,
                        converterContentHandler.getNamesArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinner.invalidate();
        spinner.setOnItemSelectedListener(this);
        if (spinnerPosition < spinner.getCount()) {
            spinner.setSelection(spinnerPosition, false);
        }
    }

    public void updateSpinners() {
        System.out.println("First editText, second update spinner called!!!");
        int firstSpinnerPosition = savedInstanceState == null ? 0
                : savedInstanceState.getInt("firstSpinnerSelectedPosition");
        int secondSpinnerPosition = savedInstanceState == null ? 0
                : savedInstanceState.getInt("secondSpinnerSelectedPosition");
        firstSpinnerPosition =
                firstSpinnerPosition < converterContentHandler.getNamesArray().length
                        ? firstSpinnerPosition : 0;
        secondSpinnerPosition =
                secondSpinnerPosition < converterContentHandler.getNamesArray().length
                        ? secondSpinnerPosition : 0;
        initializeSpinner(firstUnitSpinner, firstSpinnerPosition);
        initializeSpinner(secondUnitSpinner, secondSpinnerPosition);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            double firstValue =
                    ConverterHelper.convertStringToDouble(firstEditText.getText().toString());
            updateEditText(secondEditText,
                    ConverterHelper.convertDoubleToString(converterContentHandler.conversionResult(
                            firstValue, firstUnitSpinner.getSelectedItemPosition(),
                            secondUnitSpinner.getSelectedItemPosition())
            ), false);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid number for conversion!!!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private synchronized void updateEditText(EditText editText, String text, Boolean checkFocus) {
        if (!checkFocus || !editText.hasFocus() && !editText.isSelected()) {
            if (!editText.getText().toString().equals(text)) {
                editText.setText(text);
            }
        } else {
            System.out.println("!!!!!!!!!!!!!!!!! Focus !!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    private class ConversionTextWatcher implements TextWatcher {

        private Boolean isFirstEditText;

        public ConversionTextWatcher(Boolean isFirstEditText) {
            this.isFirstEditText = isFirstEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                if (s.toString().equals("")) {
                    return;
                }
                double value = ConverterHelper.convertStringToDouble(s.toString().replaceAll(",", ""));
                if (isFirstEditText) {
                    updateEditText(secondEditText, ConverterHelper.convertDoubleToString(
                            converterContentHandler.conversionResult(value,
                                    firstUnitSpinner.getSelectedItemPosition(),
                                    secondUnitSpinner.getSelectedItemPosition())), true);
                } else if (!isFirstEditText) {
                    updateEditText(firstEditText, ConverterHelper.convertDoubleToString(
                            converterContentHandler.conversionResult(value,
                                    secondUnitSpinner.getSelectedItemPosition(),
                                    firstUnitSpinner.getSelectedItemPosition())), true);
                }
            } catch (NumberFormatException e) {
                Log.e(getActivity().getLocalClassName(), "String not formatted correctly!!");
            }
        }
    }
}

