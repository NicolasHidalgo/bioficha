package com.example.covid19;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        context = this.getActivity();

        dbSintoma = new DatabaseManagerSintoma(context);
        List<SpinnerBean> listaSintoma = null;
        listaSintoma = dbSintoma.getSpinner();

        lvSintoma = (ListView)view.findViewById(R.id.lvSintoma);
        ArrayAdapter<SpinnerBean> adapterSintoma = new ArrayAdapter<SpinnerBean>(context,android.R.layout.simple_list_item_multiple_choice,listaSintoma);
        adapterSintoma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lvSintoma.setAdapter(adapterSintoma);

        return view;
    }

}
