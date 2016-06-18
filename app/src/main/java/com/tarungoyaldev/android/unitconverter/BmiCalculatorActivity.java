package com.tarungoyaldev.android.unitconverter;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.base.Preconditions;

public class BmiCalculatorActivity extends DrawerNavigationActivity implements AdapterView.OnItemSelectedListener {

    private EditText weightEditText;
    private EditText heightEditText;
    private EditText heightEditTextSecondary;
    private EditText ageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bmi_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initializeEditTexts();
        initializeSpinners();
        initializeBmiCategoriesGridLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navigationView =
                Preconditions.checkNotNull((NavigationView) findViewById(R.id.nav_view));
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_bmi_calculator);
    }

    private void initializeBmiCategoriesGridLayout() {
        GridLayout bmiCategoriesGrid = (GridLayout) findViewById(R.id.bmiCategories_gridLayout);
        String[] bmiCategoriesNames = getResources().getStringArray(R.array.bmi_categories_names);
        String[] bmiCategoriesValues = getResources().getStringArray(R.array.bmi_categories_values);

        for (int i=0; i<bmiCategoriesNames.length; i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(18);
            textView.setPadding(5,2,5,2);
            textView.setText(bmiCategoriesNames[i]);
            textView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            GridLayout.LayoutParams param =
                    new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(0));
            bmiCategoriesGrid.addView(textView, param);
        }

        for (int i=0; i<bmiCategoriesValues.length; i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(18);
            textView.setPadding(5,2,5,2);
            textView.setText(bmiCategoriesValues[i]);
            textView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            GridLayout.LayoutParams param =
                    new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(1));
            bmiCategoriesGrid.addView(textView, param);
        }
    }

    private void initializeSpinners() {
        initializeSpinner((Spinner) findViewById(R.id.genderSpinner), R.array.gender_array, 0);
        initializeSpinner((Spinner) findViewById(R.id.weightSpinner), R.array.bmi_weight_array, 0);
        initializeSpinner((Spinner) findViewById(R.id.heightSpinner), R.array.bmi_height_array, 0);
    }

    private void initializeEditTexts() {
        TextWatcher textWatcher  = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateViews();
            }
        };
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        heightEditText = (EditText) findViewById(R.id.heightEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        heightEditTextSecondary = (EditText) findViewById(R.id.heightEditText_secondary);
        ageEditText.addTextChangedListener(textWatcher);
        weightEditText.addTextChangedListener(textWatcher);
        heightEditText.addTextChangedListener(textWatcher);
        heightEditTextSecondary.addTextChangedListener(textWatcher);
    }

    private void initializeSpinner(Spinner spinner, int resId, int spinnerPosition) {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, resId, android.R.layout.simple_spinner_dropdown_item);
        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinner.invalidate();
        spinner.setOnItemSelectedListener(this);
        if (spinnerPosition < spinner.getCount()) {
            spinner.setSelection(spinnerPosition, false);
        }
    }

    private void updateViews() {
        GridLayout resultGridLayout = (GridLayout) findViewById(R.id.result_gridLayout);
        int resultGridChildCount = resultGridLayout.getChildCount();
        for (int i=0; i < resultGridChildCount; i++) {
            updateTextView(resultGridLayout.getChildAt(i).getId());
        }
    }

    private void updateTextView(int resId) {
        TextView textView = (TextView) findViewById(resId);
        if (textView == null) {
            return;
        }
        switch (resId) {
            case R.id.bmiHeading:
                if (getHeightInMeters() == 0 || getWeightInKgs() == 0) {
                    textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                } else {
                    textView.setTextColor(
                            ContextCompat.getColor(this, android.R.color.holo_green_dark));
                }
                break;
            case R.id.idealWeightHeading:
                if (getHeightInMeters() < 0.1) {
                    textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                } else {
                    textView.setTextColor(
                            ContextCompat.getColor(this, android.R.color.holo_green_dark));
                }
                break;
            case R.id.fatHeading:
                if (getHeightInMeters() == 0 || getWeightInKgs() == 0 || getAge() == 0) {
                    textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                } else {
                    textView.setTextColor(
                            ContextCompat.getColor(this, android.R.color.holo_green_dark));
                }
                break;
            case R.id.bmiValue:
                if (getHeightInMeters() == 0 || getWeightInKgs() == 0) {
                    textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                    textView.setText("??");
                } else {
                    textView.setTextColor(
                            ContextCompat.getColor(this, android.R.color.holo_green_dark));
                    textView.setText(ConverterHelper.convertDoubleToSmallString(
                            getWeightInKgs() / (getHeightInMeters() * getHeightInMeters())));
                }
                break;
            case R.id.idealWeightValue:
                double heightInInch = getHeightInMeters() / 0.0254;
                if (getHeightInMeters() < 0.1) {
                    textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                    textView.setText("??");
                } else {
                    textView.setTextColor(
                            ContextCompat.getColor(this, android.R.color.holo_green_dark));
                    double idealBodyWeight = 45.5;
                    idealBodyWeight += heightInInch > 60 ? 2.3 * (heightInInch - 60) : 0;
                    idealBodyWeight += isMale() ? 4.5 : 0;
                    textView.setText(ConverterHelper.convertDoubleToSmallString(idealBodyWeight));
                }
                break;
            case R.id.fatValue:
                if (getHeightInMeters() == 0 || getWeightInKgs() == 0 || getAge() == 0) {
                    textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                    textView.setText("??");
                } else {
                    double bmi = getWeightInKgs() / (getHeightInMeters() * getHeightInMeters());
                    double fat;
                    if (getAge() < 15.0) {
                        fat = 1.51 * bmi - 0.7 * getAge() + 1.4;
                        if (isMale()) {
                            fat -= 3.6;
                        }
                    } else {
                        fat = 1.2 * bmi + 0.23 * getAge() - 5.4;
                        if (isMale()) {
                            fat -= 10.8;
                        }
                    }
                    textView.setTextColor(
                            ContextCompat.getColor(this, android.R.color.holo_green_dark));
                    textView.setText(ConverterHelper.convertDoubleToSmallString(fat));
                }
                break;
        }
    }

    private double getHeightInMeters() {
        Spinner heightSpinner = (Spinner) findViewById(R.id.heightSpinner);
        if (heightSpinner == null) {
            return 0;
        }
        double heightInMeters;
        if (heightSpinner.getSelectedItemPosition() == 0) {
            heightInMeters =
                    ConverterHelper.convertStringToDouble(heightEditText.getText().toString());
        } else {
            double ft = ConverterHelper.convertStringToDouble(heightEditText.getText().toString());
            double in = ConverterHelper.convertStringToDouble(
                    heightEditTextSecondary.getText().toString());
            heightInMeters = ft * 0.3048 + in * 0.0254;
        }
        return heightInMeters;
    }

    private double getWeightInKgs() {
        Spinner weightSpinner = (Spinner) findViewById(R.id.weightSpinner);
        if (weightSpinner == null) {
            return 0;
        }
        double weightInKg = ConverterHelper.convertStringToDouble(weightEditText.getText().toString());
        if (weightSpinner.getSelectedItemPosition() == 1) {
            weightInKg *= 0.453592;
        }

        return weightInKg;
    }

    private double getAge() {
        return ConverterHelper.convertStringToDouble(ageEditText.getText().toString());
    }

    private boolean isMale() {
        Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        return genderSpinner != null && genderSpinner.getSelectedItemPosition() == 0;
    }

    /* ---------------- Spinner interface listeners ---------------------- */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.heightSpinner) {
            Spinner heightSpinner = (Spinner) parent;
            if (heightSpinner.getSelectedItemPosition() == 0) {
                heightEditTextSecondary.setVisibility(View.INVISIBLE);
                GridLayout.LayoutParams params =
                        (GridLayout.LayoutParams) heightSpinner.getLayoutParams();
                params.columnSpec = GridLayout.spec(2);
                heightSpinner.setLayoutParams(params);
            } else {
                heightEditTextSecondary.setVisibility(View.VISIBLE);
                GridLayout.LayoutParams params =
                        (GridLayout.LayoutParams) heightSpinner.getLayoutParams();
                params.columnSpec = GridLayout.spec(3);
                heightSpinner.setLayoutParams(params);
            }
        }
        updateViews();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
