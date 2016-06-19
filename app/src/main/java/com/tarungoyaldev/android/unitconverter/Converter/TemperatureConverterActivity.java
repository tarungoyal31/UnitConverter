package com.tarungoyaldev.android.unitconverter.Converter;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.google.common.base.Preconditions;
import com.tarungoyaldev.android.unitconverter.DrawerNavigationActivity;
import com.tarungoyaldev.android.unitconverter.R;
import com.tarungoyaldev.android.unitconverter.TemperatureConverterContentHandler;

public class TemperatureConverterActivity extends DrawerNavigationActivity implements ConverterFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_converter);

        Toolbar toolbar = Preconditions.checkNotNull((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }

        DrawerLayout drawer =
                Preconditions.checkNotNull((DrawerLayout) findViewById(R.id.drawer_layout));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (findViewById(R.id.general_converter_container) != null) {
            ConversionFragment conversionFragment = ConversionFragment.newInstance(
                    0, getResources().getString(R.string.temperature_converter_name));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.general_converter_container, conversionFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navigationView =
                Preconditions.checkNotNull((NavigationView) findViewById(R.id.nav_view));
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_temperature_converter);
    }

    @Override
    public ConverterContentHandler getConverterContentHandler(int position, ConversionFragment fragment) {
        return new TemperatureConverterContentHandler((String) getTitle());
    }
}
