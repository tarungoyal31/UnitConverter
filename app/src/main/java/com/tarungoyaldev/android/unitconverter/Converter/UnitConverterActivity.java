package com.tarungoyaldev.android.unitconverter.Converter;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.tarungoyaldev.android.unitconverter.DrawerNavigationActivity;
import com.tarungoyaldev.android.unitconverter.R;

public class UnitConverterActivity extends DrawerNavigationActivity implements  ConverterFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_converter);

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


        // The {@link android.support.v4.view.PagerAdapter} that will provide
        // fragments for each of the sections. We use a
        // {@link FragmentPagerAdapter} derivative, which will keep every
        // loaded fragment in memory. If this becomes too memory intensive, it
        // may be best to switch to a
        // {@link android.support.v4.app.FragmentStatePagerAdapter}.
        SectionsPagerAdapter sectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());

        // The {@link ViewPager} that will host the section contents.
        ViewPager viewPager = Preconditions.checkNotNull((ViewPager) findViewById(R.id.container));
        TabLayout tabLayout = Preconditions.checkNotNull((TabLayout) findViewById(R.id.tabs));
        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navigationView =
                Preconditions.checkNotNull((NavigationView) findViewById(R.id.nav_view));
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_unit_converter);
    }

    @Override
    public ConverterContentHandler getConverterContentHandler(int position, ConversionFragment fragment) {
        Resources resources = getResources();
        try {
            switch (position) {
                case 1:
                    return new UnitConverterContentHandler(
                            resources.getStringArray(R.array.distances_names),
                            resources.getStringArray(R.array.distances_values),
                            resources.getString(R.string.distance));
                case 2:
                    return new UnitConverterContentHandler(
                            resources.getStringArray(R.array.mass_names),
                            resources.getStringArray(R.array.mass_values),
                            resources.getString(R.string.mass));
                case 3:
                    return new CurrencyConverterContentHandler(
                            resources.getString(R.string.currency), this, fragment);
            }
        } catch (InvalidDataException e) {
            Log.e(getLocalClassName(), e.toString());
        }
        return null;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ConversionFragment conversionFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            conversionFragment = ConversionFragment.newInstance(
                    position + 1, (String) getPageTitle(position));
            return conversionFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.distance);
                case 1:
                    return getString(R.string.mass);
                case 2:
                    return getString(R.string.currency);
            }
            return null;
        }
    }
}
