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
import db.DatabaseManagerEnfermedad;


public class EnfermedadesActivity extends Fragment {
    View view;
    ListView lvEnfermedad;
    Context context;
    DatabaseManagerEnfermedad dbEnfermedad;

    String[] arrayEnfermedades = {"Hipertensión arterial","Asma","Enfermedad cardiovascular","Enfermedad respiratoria crónica","Cáncer","Insuficiencia renal","Diabetes","Tratamiento Inmunosupresor"};
    public EnfermedadesActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_enfermedades,container, false);
        lvEnfermedad = (ListView)view.findViewById(R.id.lvEnfermedad);
        context = this.getActivity();

        dbEnfermedad = new DatabaseManagerEnfermedad(context);
        List<SpinnerBean> listaEnfermedad = null;
        listaEnfermedad = dbEnfermedad.getSpinner();

        lvEnfermedad = (ListView)view.findViewById(R.id.lvEnfermedad);
        ArrayAdapter<SpinnerBean> adapterEnfermedad = new ArrayAdapter<SpinnerBean>(context,android.R.layout.simple_list_item_multiple_choice,listaEnfermedad);
        adapterEnfermedad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lvEnfermedad.setAdapter(adapterEnfermedad);

        lvEnfermedad.setItemChecked(0,true);

        return view;
    }
}
