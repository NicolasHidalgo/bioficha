package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import beans.RolBean;
import beans.SedeBean;
import beans.SpinnerBean;
import beans.UsuarioBean;
import beans.UsuarioSedeBean;
import db.DatabaseManagerRol;
import db.DatabaseManagerSede;
import db.DatabaseManagerUsuario;
import db.DatabaseManagerUsuarioSede;
import helper.Session;
import util.Util;
import ws.WebService;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    LinearLayout btnEmpresa, btnRegistrador, btnSede;
    Button btnVerFichas;
    TextView lblNomUsuario, lblRol;
    Spinner spSede;
    Context context;
    DatabaseManagerSede dbSede;

    DatabaseManagerUsuario dbUsuario;
    DatabaseManagerUsuarioSede dbUsuarioSede;
    DatabaseManagerRol dbRol;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //spSede = (Spinner) findViewById(R.id.spSede);
        btnVerFichas = findViewById(R.id.btnVerFichas);

        btnEmpresa = findViewById(R.id.btnEmpresa);
        btnRegistrador = findViewById(R.id.btnRegistrador);
        btnSede = findViewById(R.id.btnSede);

        btnEmpresa.setVisibility(LinearLayout.GONE);
        btnRegistrador.setVisibility(LinearLayout.GONE);
        btnSede.setVisibility(LinearLayout.GONE);

        btnEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, "Empresa", Toast.LENGTH_SHORT).show();
            }
        });
        btnVerFichas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ItemSede = spSede.getSelectedItem().toString();
                SedeBean sedeBean = dbSede.getByName(ItemSede);
                session.setIdSede(sedeBean.getID());
                session.setNomSede(sedeBean.getNOMBRE_SEDE());
                Intent dsp = new Intent(MenuActivity.this, FichasActivity.class);
                startActivity(dsp);
            }
        });

        context = this;
        session = new Session(context);

        dbUsuario = new DatabaseManagerUsuario(context);
        dbRol = new DatabaseManagerRol(context);
        dbSede = new DatabaseManagerSede(context);
        dbUsuarioSede = new DatabaseManagerUsuarioSede(context);


        UsuarioBean usuarioBean = dbUsuario.get(session.getIdUsuario());
        RolBean rolBean = dbRol.get(usuarioBean.getID_ROL());

        lblNomUsuario = (TextView) findViewById(R.id.lblNomUsuario);
        lblRol = (TextView) findViewById(R.id.lblRol);
        lblNomUsuario.setText(usuarioBean.getNOMBRES() + " " + usuarioBean.getAPELLIDO_PATERNO());
        lblRol.setText("USUARIO " + rolBean.getNOM_ROL() + ":");

        spSede = (Spinner) findViewById(R.id.spSede);
        List<SpinnerBean> listaSede = null;
        if (rolBean.getID().equals("1") || rolBean.getNOM_ROL().equals("SUPER-ADMIN")){
            listaSede =  dbSede.getSpinnerAll();
            btnEmpresa.setVisibility(LinearLayout.VISIBLE);
            btnRegistrador.setVisibility(LinearLayout.VISIBLE);
            btnSede.setVisibility(LinearLayout.VISIBLE);
        }else {
            List<UsuarioSedeBean> listaUsuarioSede = dbUsuarioSede.getList(usuarioBean.getID());
            String sedes = "";
            int i = 1;
            for (UsuarioSedeBean us: listaUsuarioSede) {
                sedes = sedes + us.getID_SEDE();
                if (i != listaUsuarioSede.size())
                    sedes = sedes + ",";
                i++;
            }

            listaSede =  dbSede.getSpinner(sedes);
        }

        ArrayAdapter<SpinnerBean> adapterTurno = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaSede);
        adapterTurno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSede.setAdapter(adapterTurno);

    }



}
