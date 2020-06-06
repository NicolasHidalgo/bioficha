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
import beans.UsuarioBean;
import db.DatabaseManagerUsuario;
import helper.Session;

public class RegistradorActivity extends AppCompatActivity {

    Context context;
    Session session;
    DatabaseManagerUsuario dbUsuario;
    List<UsuarioBean> listaUsuario;
    ListView lvEmpleado;

    String nInfoID[];
    String nInfo1[];
    String nInfo2[];
    String nInfo3[];

    FloatingActionButton btnAgregarRegistrador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrador);

        context = this;
        session = new Session(context);
        dbUsuario = new DatabaseManagerUsuario(context);

        if (session.getNomRol().equals("SUPER-ADMIN")){
            // Solo traer a usuarios ADMIN
            listaUsuario = dbUsuario.getListADMIN();
        }else{
            // Traer todos los usuarios
            listaUsuario = dbUsuario.getList(session.getIdEmpresa());
        }

        UsuarioBean user = null;
        int len = listaUsuario.size();
        nInfoID = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaUsuario.size(); i++) {
            user = listaUsuario.get(i);
            nInfoID[i] = user.getID();
            nInfo1[i] = user.getNOMBRES();
            nInfo2[i] = user.getNUM_DOCUMENTO();
            nInfo3[i] = user.getCOD_PAIS();
        }

        btnAgregarRegistrador = findViewById(R.id.btnAgregarRegistrador);
        lvEmpleado = findViewById(R.id.lvEmpleado);
        RegistradorActivity.MyAdapter adapter = new RegistradorActivity.MyAdapter(this, nInfoID, nInfo1,nInfo2,nInfo3);
        lvEmpleado.setAdapter(adapter);

        btnAgregarRegistrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(RegistradorActivity.this, ViewPageRegisterEmpleado.class);
                session.setIdEmpleado("");
                startActivity(dsp);
            }
        });

        lvEmpleado.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ide =(String) ((TextView) view.findViewById(R.id.txtInfoEmpId)).getText();
                session.setIdEmpleado(ide);
                Intent intent = new Intent(context,ViewPageRegisterEmpleado.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        if (session.getNomRol().equals("SUPER-ADMIN")){
            // Solo traer a usuarios ADMIN
            listaUsuario = dbUsuario.getListADMIN();
        }else{
            // Traer todos los usuarios
            listaUsuario = dbUsuario.getList(session.getIdEmpresa());
        }

        UsuarioBean user = null;
        int len = listaUsuario.size();
        nInfoID = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaUsuario.size(); i++) {
            user = listaUsuario.get(i);
            nInfoID[i] = user.getID();
            nInfo1[i] = user.getNOMBRES();
            nInfo2[i] = user.getNUM_DOCUMENTO();
            nInfo3[i] = user.getCOD_PAIS();
        }
        btnAgregarRegistrador = findViewById(R.id.btnAgregarRegistrador);
        lvEmpleado = findViewById(R.id.lvEmpleado);
        RegistradorActivity.MyAdapter adapter = new RegistradorActivity.MyAdapter(this, nInfoID, nInfo1, nInfo2, nInfo3);
        lvEmpleado.setAdapter(adapter);

    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;

        String nInfoID[];
        String nInfo1[];
        String nInfo2[];
        String nInfo3[];

        MyAdapter(Context c, String nInfoId[], String nInfo1[], String nInfo2[], String nInfo3[]){
            super(c,R.layout.row_empleado, R.id.txtInfoEmpId, nInfoId);
            this.context = c;
            this.nInfoID = nInfoId;
            this.nInfo1 = nInfo1;
            this.nInfo2 = nInfo2;
            this.nInfo3 = nInfo3;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_empleado, parent, false);
            TextView id = row.findViewById(R.id.txtInfoEmpId);
            TextView tit = row.findViewById(R.id.txtInfoEmp1);
            TextView sub = row.findViewById(R.id.txtInfoEmp2);
            TextView inf = row.findViewById(R.id.txtInfoEmp3);

            id.setText(nInfoID[position]);
            tit.setText(nInfo1[position]);
            sub.setText(nInfo2[position]);
            inf.setText(nInfo3[position]);

            return row;
        }
    }
}
