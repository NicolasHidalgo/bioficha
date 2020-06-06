package com.example.covid19;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import beans.BioFichaBean;
import beans.UsuarioBean;
import db.DatabaseManagerBioFicha;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerUsuario;
import helper.Session;

public class ViewPageRegisterEmpleado extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_register_empleado);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        final RegistroRegistradorActivity registroRegistradorActivity = new RegistroRegistradorActivity();
        final AsignarSedeEmpleadoActivity asignarSedeEmpleadoActivity = new AsignarSedeEmpleadoActivity();
        viewPagerAdapter.AddFragment(registroRegistradorActivity, "Empleado");
        viewPagerAdapter.AddFragment(asignarSedeEmpleadoActivity,"Sedes");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_user_tap);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sede_tap);
    }
}
