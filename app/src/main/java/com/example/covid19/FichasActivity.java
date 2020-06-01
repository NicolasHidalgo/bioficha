package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import beans.BioFichaBean;
import db.DatabaseManagerBioFicha;
import helper.Session;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class FichasActivity extends AppCompatActivity {

    Context context;
    Session session;
    DatabaseManagerBioFicha dbFicha;
    List<BioFichaBean> listaFicha;

    FloatingActionButton btnAgregarFicha;
    ListView lvFicha;

    String nFicha[]; //= {"Ficha 01","Ficha 02","Ficha 03","Ficha 04","Ficha 05","Ficha 06","Ficha 07","Ficha 08","Ficha 09","Ficha 10"};
    String nEmpleado[]; //= {"Juan Perez","María Vásquez","Guillermo Villavicencio","Andres Garay","Nicolas Hidalgo","Milagros Linares","Hector Mendoza","Marina Rubio","Alejandro Rosales","Edwin Bedregal"};
    String nHora[]; //= {"10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm"};
    String nID[];

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichas);
        context = this;
        session = new Session(context);
        dbFicha = new DatabaseManagerBioFicha(context);

        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        listaFicha = dbFicha.ListarPorSedeFechaV2(session.getIdSede(),fechaHoy);

        BioFichaBean bio = null;
        int len = listaFicha.size();
        nID = new String[len];
        nFicha = new String[len];
        nEmpleado = new String[len];
        nHora = new String[len];

        for (int i=0; i<listaFicha.size(); i++) {
            bio = listaFicha.get(i);
            nID[i] = bio.getID();
            nFicha[i] = bio.getNOMBRES();
            nEmpleado[i] = bio.getNUM_DOCUMENTO();
            nHora[i] = bio.getFEC_CREACION();
        }
        btnAgregarFicha = findViewById(R.id.btnAgregarFicha);
        lvFicha = findViewById(R.id.lvFicha);
        MyAdapter adapter = new MyAdapter(this, nFicha,nEmpleado,nHora,nID);
        lvFicha.setAdapter(adapter);

        btnAgregarFicha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(FichasActivity.this, ViewPageRegister.class);
                session.setIdEmpleado("");
                startActivity(dsp);
            }
        });

        lvFicha.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ide =(String) ((TextView) view.findViewById(R.id.txtID)).getText();
                session.setIdEmpleado(ide);
                Intent intent = new Intent(context,ViewPageRegister.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        listaFicha = dbFicha.ListarPorSedeFechaV2(session.getIdSede(),fechaHoy);
        BioFichaBean bio = null;
        int len = listaFicha.size();
        nID = new String[len];
        nFicha = new String[len];
        nEmpleado = new String[len];
        nHora = new String[len];

        for (int i=0; i<listaFicha.size(); i++) {
            bio = listaFicha.get(i);
            nID[i] = bio.getID();
            nFicha[i] = bio.getNOMBRES();
            nEmpleado[i] = bio.getNUM_DOCUMENTO();
            nHora[i] = bio.getFEC_CREACION();
        }
        btnAgregarFicha = findViewById(R.id.btnAgregarFicha);
        lvFicha = findViewById(R.id.lvFicha);
        MyAdapter adapter = new MyAdapter(this, nFicha,nEmpleado,nHora,nID);
        lvFicha.setAdapter(adapter);

    }

    class MyAdapter extends ArrayAdapter<String>{
     Context context;

     String nID[];
     String nFicha[];
     String nEmpleado[];
     String nHora[];

     MyAdapter(Context c, String ficha[], String empleado[], String hora[], String id[]){
         super(c,R.layout.row, R.id.txtFicha, ficha);
         this.context = c;
         this.nID = id;
         this.nFicha = ficha;
         this.nEmpleado = empleado;
         this.nHora = hora;
     }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView id = row.findViewById(R.id.txtID);
            TextView ficha = row.findViewById(R.id.txtFicha);
            TextView empleado = row.findViewById(R.id.txtEmpleado);
            TextView hora = row.findViewById(R.id.txtHora);

            id.setText(nID[position]);
            ficha.setText(nFicha[position]);
            empleado.setText(nEmpleado[position]);
            hora.setText(nHora[position]);

            return row;
        }
    }
}
