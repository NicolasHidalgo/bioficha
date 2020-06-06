package com.example.covid19;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import beans.EmpresaBean;
import beans.UsuarioBean;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerUsuario;
import helper.Session;

public class EmpresasActivity extends AppCompatActivity {

    Context context;
    Session session;
    DatabaseManagerEmpresa dbEmpresa;
    List<EmpresaBean> listaEmpresa;
    ListView lvEmpresa;

    String nInfoID[];
    String nInfo1[];
    String nInfo2[];
    String nInfo3[];

    FloatingActionButton btnAgregarEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        context = this;
        session = new Session(context);
        dbEmpresa = new DatabaseManagerEmpresa(context);

        if(session.getNomRol().equals("SUPER-ADMIN")){
            //lISTAR TODAS LAS EMPRESAS
            listaEmpresa = dbEmpresa.getList("");
        }else if(session.getNomRol().equals("ADMIN")){
            // LISTAR SOLO SU EMPRESA
            listaEmpresa = dbEmpresa.getList(session.getIdEmpresa());
        }


        EmpresaBean user = null;
        int len = listaEmpresa.size();
        nInfoID = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaEmpresa.size(); i++) {
            user = listaEmpresa.get(i);
            nInfoID[i] = user.getID();
            nInfo1[i] = user.getNOM_RAZON_SOCIAL();
            nInfo2[i] = user.getRUC();
            nInfo3[i] = "x";
        }

        btnAgregarEmpresa = findViewById(R.id.btnAgregarEmpresa);

        if(session.getNomRol().equals("ADMIN")){
            btnAgregarEmpresa.hide();
        }

        lvEmpresa = findViewById(R.id.lvEmpresa);
        EmpresasActivity.MyAdapter adapter = new EmpresasActivity.MyAdapter(this, nInfoID, nInfo1,nInfo2,nInfo3);
        lvEmpresa.setAdapter(adapter);

        btnAgregarEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(EmpresasActivity.this, RegistroEmpresasActivity.class);
                session.setIdEmpresa("");
                startActivity(dsp);
            }
        });

        lvEmpresa.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ide =(String) ((TextView) view.findViewById(R.id.txtInfoEmpresaId)).getText();
                session.setIdEmpresa(ide);
                Intent intent = new Intent(context,RegistroEmpresasActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if(session.getNomRol().equals("SUPER-ADMIN")){
            //lISTAR TODAS LAS EMPRESAS
            listaEmpresa = dbEmpresa.getList("");
        }else if(session.getNomRol().equals("ADMIN")){
            // LISTAR SOLO SU EMPRESA
            listaEmpresa = dbEmpresa.getList(session.getIdEmpresa());
        }

        EmpresaBean user = null;
        int len = listaEmpresa.size();
        nInfoID = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaEmpresa.size(); i++) {
            user = listaEmpresa.get(i);
            nInfoID[i] = user.getID();
            nInfo1[i] = user.getNOM_RAZON_SOCIAL();
            nInfo2[i] = user.getRUC();
            nInfo3[i] = "x";
        }
        btnAgregarEmpresa = findViewById(R.id.btnAgregarEmpresa);
        lvEmpresa = findViewById(R.id.lvEmpresa);
        EmpresasActivity.MyAdapter adapter = new EmpresasActivity.MyAdapter(this, nInfoID, nInfo1, nInfo2, nInfo3);
        lvEmpresa.setAdapter(adapter);

    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;

        String nID[];
        String nInfo1[];
        String nInfo2[];
        String nInfo3[];

        MyAdapter(Context c, String id[], String nInfo1[], String nInfo2[], String nInfo3[]){
            super(c,R.layout.row_empresa, R.id.txtInfoEmpresaId, id);
            this.context = c;
            this.nID = id;
            this.nInfo1 = nInfo1;
            this.nInfo2 = nInfo2;
            this.nInfo3 = nInfo3;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_empresa, parent, false);
            TextView id = row.findViewById(R.id.txtInfoEmpresaId);
            TextView tit = row.findViewById(R.id.txtInfoEmpresa1);
            TextView sub = row.findViewById(R.id.txtInfoEmpresa2);
            TextView inf = row.findViewById(R.id.txtInfoEmpresa3);

            id.setText(nID[position]);
            tit.setText(nInfo1[position]);
            sub.setText(nInfo2[position]);
            inf.setText(nInfo3[position]);

            return row;
        }
    }

}
