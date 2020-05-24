package com.example.covid19;


import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class ViewPageRegister extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_register);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.AddFragment(new RegistroActivity(), "Ficha");
        viewPagerAdapter.AddFragment(new SintomasActivity(),"Síntomas");
        viewPagerAdapter.AddFragment(new EnfermedadesActivity(),"Enfermedades");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_insert_tap);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sintomas_tab);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_enfermedad_tab);



    }
}
