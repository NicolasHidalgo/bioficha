package com.example.covid19;


import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import beans.SpinnerBean;
import db.DatabaseManagerTipoDocumento;

public class ViewPageRegister extends AppCompatActivity {

    Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  ViewPagerAdapter viewPagerAdapter;
    Spinner spTipoDocumento;

    DatabaseManagerTipoDocumento dbTipoDocumento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_register);
        context = this;

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

        //dbTipoDocumento = new DatabaseManagerTipoDocumento(context);
        //spTipoDocumento = (Spinner) findViewById(R.id.spTipoDocumento);
        //List<SpinnerBean> listaTipoDocumento = dbTipoDocumento.getSpinner();

        //ArrayAdapter<SpinnerBean> adapterTipoDocumento = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaTipoDocumento);
        //adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spTipoDocumento.setAdapter(adapterTipoDocumento);
        //Toast.makeText(this,"xxxx",Toast.LENGTH_LONG).show();

    }
}
