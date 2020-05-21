package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FichasActivity extends AppCompatActivity {
    ListView listView;
    String nFicha[] = {"Ficha 01","Ficha 02","Ficha 03","Ficha 04","Ficha 05","Ficha 06","Ficha 07","Ficha 08","Ficha 09","Ficha 10"};
    String nEmpleado[] = {"Juan Perez","María Vásquez","Guillermo Villavicencio","Andres Garay","Nicolas Hidalgo","Milagros Linares","Hector Mendoza","Marina Rubio","Alejandro Rosales","Edwin Bedregal"};
    String nHora[] = {"10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm","10:35 pm"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichas);
        listView = findViewById(R.id.listview);
        MyAdapter adapter = new MyAdapter(this, nFicha,nEmpleado,nHora);
        listView.setAdapter(adapter);

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
