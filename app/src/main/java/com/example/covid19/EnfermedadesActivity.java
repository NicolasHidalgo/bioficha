package com.example.covid19;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class EnfermedadesActivity extends Fragment {
    View v;
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] arrayEnfermedades = {"Hipertensión arterial","Asma","Enfermedad cardiovascular","Enfermedad respiratoria crónica","Cáncer","Insuficiencia renal","Diabetes","Tratamiento Inmunosupresor"};
    public EnfermedadesActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_enfermedades,container, false);
        listView = (ListView)v.findViewById(R.id.listViewEnfermedades);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,arrayEnfermedades);
        listView.setAdapter(adapter);
        return v;
    }
}
