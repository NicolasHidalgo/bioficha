package com.example.covid19;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import beans.SpinnerBean;
import db.DatabaseManagerSintoma;


public class SintomasActivity extends Fragment {

    View view;
    ListView lvSintoma;
    DatabaseManagerSintoma dbSintoma;
    Context context;
    SparseBooleanArray checked = null;

    String[] arraySintomas = {"Fiebre/escalofrío","Malestar general","Tos","Dolor de garganta","Congestión nasal","Irritación de ojo(s)","Dificultad respiratoria","Diarrea",
            "Náuseas/vómitos","Cefalea","Irritabilidad/confusión", "Dolor muscular", "Dolor de pecho", "Dolor abdominal", "Dolor articular", "Dolor de cabeza", "Dolor de oído","Otro, especificar"};
    List<String> listaSeleccionados;
    ArrayAdapter<String> adapterDolor;
    public SintomasActivity(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sintomas, container, false);
        lvSintoma = (ListView)view.findViewById(R.id.lvSintoma);
        context = this.getActivity();

        dbSintoma = new DatabaseManagerSintoma(context);
        List<SpinnerBean> listaSintoma = null;
        listaSintoma = dbSintoma.getSpinner();

        ArrayAdapter<SpinnerBean> adapterSintoma = new ArrayAdapter<SpinnerBean>(context,android.R.layout.simple_list_item_multiple_choice,listaSintoma);
        adapterSintoma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lvSintoma.setAdapter(adapterSintoma);

        lvSintoma.setItemChecked(0,true);

        lvSintoma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    checked = lvSintoma.getCheckedItemPositions();
                    if (checked != null){
                        for (int i = 0; i < checked.size() ; ++i) {
                            if (checked.valueAt(i)) {
                                int pos = checked.keyAt(i);
                                lvSintoma.setItemChecked(pos,false);
                            }
                        }
                    }
                    lvSintoma.setItemChecked(0,true);
                }else{
                    lvSintoma.setItemChecked(0,false);
                }
            }
        });

        return view;
    }

}
