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


public class SintomasActivity extends Fragment {

    View v;
    ListView listView;

    String[] arraySintomas = {"Fiebre/escalofrío","Malestar general","Tos","Dolor de garganta","Congestión nasal","Irritación de ojo(s)","Dificultad respiratoria","Diarrea",
            "Náuseas/vómitos","Cefalea","Irritabilidad/confusión", "Dolor muscular", "Dolor de pecho", "Dolor abdominal", "Dolor articular", "Dolor de cabeza", "Dolor de oído","Otro, especificar"};
    List<String> listaSeleccionados;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterDolor;
    public SintomasActivity(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_sintomas, container, false);
        listView = (ListView)v.findViewById(R.id.listViewSintomas);

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,arraySintomas);

        listView.setAdapter(adapter);

        return v;
    }

}
