package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import beans.BioFichaBean;
import db.DatabaseManagerBioFicha;
import helper.Session;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichas);
        context = this;
        dbFicha = new DatabaseManagerBioFicha(context);

        listaFicha = dbFicha.get("");

        BioFichaBean bio = null;
        nFicha = new String[listaFicha.size()];
        nEmpleado = new String[listaFicha.size()];
        nHora = new String[listaFicha.size()];

        for (int i=0; i<listaFicha.size(); i++) {
            bio = listaFicha.get(i);
            nFicha[i] = bio.getNOMBRES();
            nEmpleado[i] = bio.getNUM_DOCUMENTO();
            nHora[i] = bio.getFEC_CREACION();
        }

        btnAgregarFicha = findViewById(R.id.btnAgregarFicha);
        lvFicha = findViewById(R.id.lvFicha);
        MyAdapter adapter = new MyAdapter(this, nFicha,nEmpleado,nHora);
        lvFicha.setAdapter(adapter);
        btnAgregarFicha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(FichasActivity.this, ViewPageRegister.class);
                startActivity(dsp);
            }
        });

    }

    class MyAdapter extends ArrayAdapter<String>{
     Context context;

     String nFicha[];
     String nEmpleado[];
     String nHora[];

     MyAdapter(Context c, String ficha[], String empleado[], String hora[]){
         super(c,R.layout.row, R.id.txtFicha, ficha);
         this.context = c;
         this.nFicha = ficha;
         this.nEmpleado = empleado;
         this.nHora = hora;
     }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView ficha = row.findViewById(R.id.txtFicha);
            TextView empleado = row.findViewById(R.id.txtEmpleado);
            TextView hora = row.findViewById(R.id.txtHora);

            ficha.setText(nFicha[position]);
            empleado.setText(nEmpleado[position]);
            hora.setText(nHora[position]);

            return row;
        }
    }
}
