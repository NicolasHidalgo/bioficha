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
import beans.BioFichaEnfermedadBean;
import beans.BioFichaSintomaBean;
import beans.SpinnerBean;
import db.DatabaseManagerBioFichaEnfermedad;
import db.DatabaseManagerEnfermedad;
import helper.Session;


public class EnfermedadesActivity extends Fragment {

    View view;
    ListView lvEnfermedad;
    DatabaseManagerEnfermedad dbEnfermedad;
    DatabaseManagerBioFichaEnfermedad dbFichaEnfermedad;
    Context context;
    SparseBooleanArray checked = null;
    Session session;

    String[] arrayEnfermedades = {"Hipertensión arterial","Asma","Enfermedad cardiovascular","Enfermedad respiratoria crónica","Cáncer","Insuficiencia renal","Diabetes","Tratamiento Inmunosupresor"};
    public EnfermedadesActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_enfermedades,container, false);
        lvEnfermedad = (ListView)view.findViewById(R.id.lvEnfermedad);
        context = this.getActivity();
        session = new Session(context);

        dbEnfermedad = new DatabaseManagerEnfermedad(context);
        dbFichaEnfermedad = new DatabaseManagerBioFichaEnfermedad(context);
        List<SpinnerBean> listaEnfermedad = null;
        listaEnfermedad = dbEnfermedad.getSpinner();

        ArrayAdapter<SpinnerBean> adapterEnfermedad = new ArrayAdapter<SpinnerBean>(context,android.R.layout.simple_list_item_multiple_choice,listaEnfermedad);
        adapterEnfermedad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lvEnfermedad.setAdapter(adapterEnfermedad);

        String ide = session.getIdEmpleado();
        if (!(ide.isEmpty())){
            TraerEnfermedades(ide);
        }else{
            lvEnfermedad.setItemChecked(0,true);
        }

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

    public void TraerEnfermedades(String ide){
        List<BioFichaEnfermedadBean> lista = dbFichaEnfermedad.ListarPorFicha(ide);
        for (BioFichaEnfermedadBean item : lista){
            for (int i = 0; i < lvEnfermedad.getAdapter().getCount(); i++) {
                String valor = lvEnfermedad.getItemAtPosition(i).toString();
                if (valor.equalsIgnoreCase(item.getNOM_ENFERMEDAD())){
                    lvEnfermedad.setItemChecked(i,true);
                    break;
                }
            }

        }
    }
}
