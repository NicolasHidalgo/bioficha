package com.example.covid19;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import helper.Session;

public class ViewPageRegisterSede extends AppCompatActivity {

    Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_register_sede);
        context = this;
        session = new Session(context);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        final RegistroSedesActivity registroSedesActivity = new RegistroSedesActivity();
        final RegistroGeolocalizacionActivity registroGeolocalizacionActivity = new RegistroGeolocalizacionActivity();

        viewPagerAdapter.AddFragment(registroSedesActivity, "Datos Generales");
        viewPagerAdapter.AddFragment(registroGeolocalizacionActivity, "Geolocalización");

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_sede_tap);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_location_tap);


    }
}
