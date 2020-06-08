package com.example.covid19;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.RequestQueue;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import beans.DepartamentoBean;
import beans.DistritoBean;
import beans.EmpresaBean;
import beans.ProvinciaBean;
import beans.SedeBean;
import beans.SedePoligonoBean;
import beans.SpinnerBean;
import db.DatabaseManagerDepartamento;
import db.DatabaseManagerDistrito;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerProvincia;
import db.DatabaseManagerSede;
import db.DatabaseManagerSedePoligono;
import helper.Session;


public class RegistroSedesActivity extends Fragment {

    View view;
    Context context;
    Session session;
    Spinner spDepartamento, spProvincia, spDistrito, spEmpresa;
    EditText txtNombreSede, txtDireccionFiscal;
    Button btnGrabar;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    DatabaseManagerDepartamento dbDepartamento;
    DatabaseManagerProvincia dbProvincia;
    DatabaseManagerDistrito dbDistrito;
    DatabaseManagerEmpresa dbEmpresa;
    DatabaseManagerSede dbSede;
    public int id_departamento = 0;
    public int id_provincia = 0;
    public int id_distrito = 0;
    int checkDep = 0;
    int checkProv = 0;
    int checkDis = 0;
    public RegistroSedesActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_registro_sedes, container, false);
        context = this.getActivity();
        session = new Session(context);
        dbDepartamento = new DatabaseManagerDepartamento(context);
        dbProvincia = new DatabaseManagerProvincia(context);
        dbDistrito = new DatabaseManagerDistrito(context);
        dbSede = new DatabaseManagerSede(context);
        dbEmpresa = new DatabaseManagerEmpresa(context);
        spDepartamento = view.findViewById(R.id.spDepartamento);
        spProvincia = view.findViewById(R.id.spProvincia);
        spDistrito = view.findViewById(R.id.spDistrito);
        spEmpresa = view.findViewById(R.id.spEmpresa);
        txtNombreSede = view.findViewById(R.id.txtNombreSede);
        txtDireccionFiscal = view.findViewById(R.id.txtDireccionFiscal);
        spProvincia.setEnabled(false);
        spDistrito.setEnabled(false);
        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);
        final List<SpinnerBean> listaDepartamento = dbDepartamento.getSpinner();
        ArrayAdapter<SpinnerBean> adapterDepartamento = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaDepartamento);
        adapterDepartamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartamento.setAdapter(adapterDepartamento);
        String[] provincia = {"Seleccione"};
        ArrayAdapter adapterProvincia = new ArrayAdapter(context, R.layout.custom_spinner, provincia);
        adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvincia.setAdapter(adapterProvincia);
        String[] distrito = {"Seleccione"};
        ArrayAdapter adapterDistrito = new ArrayAdapter(context, R.layout.custom_spinner, distrito);
        adapterDistrito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistrito.setAdapter(adapterDistrito);
        spDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_departamento = listaDepartamento.get(position).getID();
                if (++checkDep > 1) {
                    if (id_departamento == -1) {
                        String[] provincia = {"Seleccione"};
                        ArrayAdapter adapterProvincia = new ArrayAdapter(context, R.layout.custom_spinner, provincia);
                        adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spProvincia.setAdapter(adapterProvincia);
                        spProvincia.setEnabled(false);
                        return;
                    }
                    List<SpinnerBean> listaProvincia = dbProvincia.getListSpinner(Integer.toString(id_departamento));
                    ArrayAdapter adapterProvincia = new ArrayAdapter(context, R.layout.custom_spinner, listaProvincia);
                    adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProvincia.setAdapter(adapterProvincia);
                    spProvincia.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++checkProv > 1) {
                    List<SpinnerBean> listaProvincia = dbProvincia.getListSpinner(Integer.toString(id_departamento));
                    id_provincia = listaProvincia.get(position).getID();
                    if (id_provincia == -1) {
                        String[] distrito = {"Seleccione"};
                        ArrayAdapter adapterDistrito = new ArrayAdapter(context, R.layout.custom_spinner, distrito);
                        adapterDistrito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spDistrito.setAdapter(adapterDistrito);
                        spDistrito.setEnabled(false);
                        return;
                    }

                    List<SpinnerBean> listaDistrito = dbDistrito.getListSpinner(Integer.toString(id_provincia));
                    ArrayAdapter adapterDistrito = new ArrayAdapter(context, R.layout.custom_spinner, listaDistrito);
                    adapterDistrito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDistrito.setAdapter(adapterDistrito);
                    spDistrito.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDistrito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++checkDis > 1) {
                    List<SpinnerBean> listaDistrito = dbDistrito.getListSpinner(Integer.toString(id_provincia));
                    id_distrito = listaDistrito.get(position).getID();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<SpinnerBean> listaEmpresa = null;
        if(session.getNomRol().equals("SUPER-ADMIN")){
            listaEmpresa = dbEmpresa.getSpinner();
        }else{
            listaEmpresa = dbEmpresa.getSpinnerPorEmpresa(session.getIdEmpresa());
        }

        ArrayAdapter<SpinnerBean> adapterEmpresa = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaEmpresa);
        adapterEmpresa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEmpresa.setAdapter(adapterEmpresa);

        String ide = session.getIdSede();
        if (!(ide.isEmpty())){
            TraerSede(ide);
        }

        return view;


    }
    public void ProgressBarHandler(Context context) {
        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout rl = new RelativeLayout(context);
        rl.setGravity(Gravity.CENTER);
        rl.addView(progressBar);
        layout.addView(rl, params);

    }

    public void CloseProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
    }
    public void OpenProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        CloseProgressBar();
    }
    public void TraerSede(String ide){
        SedeBean sedeBean = null;
        sedeBean = dbSede.getObject(ide);
        txtNombreSede.setText(sedeBean.getNOMBRE_SEDE());
        txtDireccionFiscal.setText(sedeBean.getDIRECCION());
        DepartamentoBean departamentoBean = dbDepartamento.get(sedeBean.getID_DEPARTAMENTO());
        int pos = 0;
        for (int i=0;i<spDepartamento.getCount();i++){
            if (spDepartamento.getItemAtPosition(i).toString().equalsIgnoreCase(departamentoBean.getNOM_DEPARTAMENTO())){
                pos = i;
                break;
            }
        }
        spDepartamento.setSelection(pos);
        List<SpinnerBean> listaProvincia = dbProvincia.getListSpinner(sedeBean.getID_DEPARTAMENTO());
        ArrayAdapter adapterProvincia = new ArrayAdapter(context, R.layout.custom_spinner, listaProvincia);
        adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvincia.setAdapter(adapterProvincia);
        id_departamento = Integer.parseInt(sedeBean.getID_DEPARTAMENTO());
        ProvinciaBean provinciaBean = dbProvincia.get(sedeBean.getID_PROVINCIA());
        int pos1 = 0;
        for (int j=0;j<spProvincia.getCount();j++){
            if (spProvincia.getItemAtPosition(j).toString().equalsIgnoreCase(provinciaBean.getNOM_PROVINCIA())){
                pos1 = j;
                break;
            }
        }
        spProvincia.setSelection(pos1);
        spProvincia.setEnabled(true);
        List<SpinnerBean> listaDistrito = dbDistrito.getListSpinner(sedeBean.getID_PROVINCIA());
        ArrayAdapter adapterDistrito = new ArrayAdapter(context, R.layout.custom_spinner, listaDistrito);
        adapterDistrito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistrito.setAdapter(adapterDistrito);
        id_provincia = Integer.parseInt(sedeBean.getID_PROVINCIA());
        DistritoBean distritoBean = dbDistrito.get(sedeBean.getID_DISTRITO());
        int pos2 = 0;
        for (int k=0;k<spDistrito.getCount();k++){
            if (spDistrito.getItemAtPosition(k).toString().equalsIgnoreCase(distritoBean.getNOM_DISTRITO())){
                pos2 = k;
                break;
            }
        }
        spDistrito.setSelection(pos2);
        spDistrito.setEnabled(true);
        List<SpinnerBean> listaEmpresa = null;
        if(session.getNomRol().equals("SUPER-ADMIN")){
            listaEmpresa = dbEmpresa.getSpinner();
        }else{
            listaEmpresa = dbEmpresa.getSpinnerPorEmpresa(sedeBean.getID_EMPRESA());
        }

        ArrayAdapter<SpinnerBean> adapterEmpresa = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaEmpresa);
        adapterEmpresa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEmpresa.setAdapter(adapterEmpresa);
    }


}


