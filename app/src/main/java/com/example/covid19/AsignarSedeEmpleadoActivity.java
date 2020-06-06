package com.example.covid19;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import beans.SpinnerBean;
import db.DatabaseManagerBioFichaEnfermedad;
import db.DatabaseManagerEnfermedad;
import db.DatabaseManagerSede;
import db.DatabaseManagerUsuarioSede;
import helper.Session;

public class AsignarSedeEmpleadoActivity extends Fragment {

    View view;
    ListView lvSedes;
    DatabaseManagerSede dbSede;
    DatabaseManagerUsuarioSede dbUsuarioSede;
    Context context;
    SparseBooleanArray checked = null;
    Session session;

    public AsignarSedeEmpleadoActivity(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_asignar_sede_empleado, container, false);

        lvSedes = (ListView)view.findViewById(R.id.lvSedes);
        context = this.getActivity();
        session = new Session(context);

        dbSede = new DatabaseManagerSede(context);
        dbUsuarioSede = new DatabaseManagerUsuarioSede(context);
        List<SpinnerBean> listaSede = null;
        listaSede = dbSede.getSpinnerPorEmpresa(session.getIdEmpresa());

        ArrayAdapter<SpinnerBean> adapterEnfermedad = new ArrayAdapter<SpinnerBean>(context,android.R.layout.simple_list_item_multiple_choice,listaSede);
        adapterEnfermedad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lvSedes.setAdapter(adapterEnfermedad);

        return view;
    }


}
