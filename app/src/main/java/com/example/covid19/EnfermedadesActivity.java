package com.example.covid19;


import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    DatabaseManagerEnfermedad dbEnfermedad;
    Context context;
    SparseBooleanArray checked = null;

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

        ArrayAdapter<SpinnerBean> adapterEnfermedad = new ArrayAdapter<SpinnerBean>(context,android.R.layout.simple_list_item_multiple_choice,listaEnfermedad);
        adapterEnfermedad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lvEnfermedad.setAdapter(adapterEnfermedad);

        lvEnfermedad.setItemChecked(0,true);

        lvEnfermedad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    checked = lvEnfermedad.getCheckedItemPositions();
                    if (checked != null){
                        for (int i = 0; i < checked.size() ; ++i) {
                            if (checked.valueAt(i)) {
                                int pos = checked.keyAt(i);
                                lvEnfermedad.setItemChecked(pos,false);
                            }
                        }
                    }
                    lvEnfermedad.setItemChecked(0,true);
                }else{
                    lvEnfermedad.setItemChecked(0,false);
                }
            }
        });

        return view;
    }
}
