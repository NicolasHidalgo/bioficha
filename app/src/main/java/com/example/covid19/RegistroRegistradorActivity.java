package com.example.covid19;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import beans.BioFichaBean;
import beans.EmpresaBean;
import beans.PaisBean;
import beans.RolBean;
import beans.SpinnerBean;
import beans.TipoDocumentoBean;
import beans.UsuarioBean;
import db.DatabaseManagerBioFicha;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerPais;
import db.DatabaseManagerRol;
import db.DatabaseManagerTipoDocumento;
import db.DatabaseManagerUsuario;
import helper.Session;
import util.Util;

public class RegistroRegistradorActivity extends Fragment {

    View view;
    Spinner spTipoDocumento, spGenero, spPais, spEmpresa, spRol;
    DatabaseManagerTipoDocumento dbTipoDocumento;
    DatabaseManagerPais dbPais;
    DatabaseManagerUsuario dbUsuario;
    DatabaseManagerRol dbRol;
    DatabaseManagerEmpresa dbEmpresa;

    Context context;
    EditText txtNumDocumento, txtNombres, txtApePaterno, txtApeMaterno, txtCorreo;
    EditText txtFechaNacimiento, txtUsuario, txtClave;
    LinearLayout linearLayoutEmpresa, linearLayoutUsuario, linearLayoutClave;
    RequestQueue requestQueue;
    Button btnBuscarEmpleado;

    Calendar calendar;
    private Session session;
    ProgressBar progressBar;

    public RegistroRegistradorActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_registro_registrador, container, false);
        context = this.getActivity();
        session = new Session(context);

        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);

        dbTipoDocumento = new DatabaseManagerTipoDocumento(context);
        dbPais = new DatabaseManagerPais(context);
        dbUsuario = new DatabaseManagerUsuario(context);
        dbRol = new DatabaseManagerRol(context);
        dbEmpresa = new DatabaseManagerEmpresa(context);

        btnBuscarEmpleado = (Button) view.findViewById(R.id.btnBuscarEmpleado);
        spTipoDocumento = (Spinner) view.findViewById(R.id.spTipoDocumento);
        txtNumDocumento = (EditText) view.findViewById(R.id.txtNumDocumento);
        spPais = (Spinner) view.findViewById(R.id.spPais);
        txtNombres = (EditText) view.findViewById(R.id.txtNombres);
        txtApePaterno = (EditText) view.findViewById(R.id.txtApellidoPaterno);
        txtApeMaterno = (EditText) view.findViewById(R.id.txtApellidoMaterno);
        spGenero = (Spinner) view.findViewById(R.id.spGenero);
        txtCorreo = (EditText) view.findViewById(R.id.txtCorreo);
        txtFechaNacimiento = (EditText) view.findViewById(R.id.txtFechaNacimiento);
        spEmpresa = (Spinner) view.findViewById(R.id.spEmpresa);
        txtUsuario = (EditText) view.findViewById(R.id.txtUsuario);
        txtClave = (EditText) view.findViewById(R.id.txtClave);
        spRol = (Spinner) view.findViewById(R.id.spRol);

        // Se ocultara cuando la empresa este en session
        linearLayoutEmpresa = (LinearLayout) view.findViewById(R.id.LinearLayoutEmpresa);
        linearLayoutUsuario = (LinearLayout) view.findViewById(R.id.LinearLayoutUsuario);
        linearLayoutClave = (LinearLayout) view.findViewById(R.id.LinearLayoutClave);
        linearLayoutEmpresa.setVisibility(View.VISIBLE);

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            UpdateFecha();
            }

        };

        txtFechaNacimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    new DatePickerDialog(context, date, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                } else {
                    // Hide your calender here
                }
            }
        });


        List<SpinnerBean> listaTipoDocumento = dbTipoDocumento.getSpinner();
        ArrayAdapter<SpinnerBean> adapterTipoDocumento = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaTipoDocumento);
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoDocumento.setAdapter(adapterTipoDocumento);

        List<SpinnerBean> listaPais = dbPais.getSpinner();
        ArrayAdapter<SpinnerBean> adapterPais = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaPais);
        adapterPais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPais.setAdapter(adapterPais);

        List<SpinnerBean> listaEmpresa = null;
        if(session.getNomRol().equals("SUPER-ADMIN")){
            listaEmpresa = dbEmpresa.getSpinner();
        }else{
            listaEmpresa = dbEmpresa.getSpinnerPorEmpresa(session.getIdEmpresa());
        }

        ArrayAdapter<SpinnerBean> adapterEmpresa = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaEmpresa);
        adapterEmpresa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEmpresa.setAdapter(adapterEmpresa);

        String[] value = {"MASCULINO","FEMENINO"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, arrayList);
        spGenero.setAdapter(arrayAdapter);

        List<SpinnerBean> listaRol = null;
        if (session.getNomRol().equals("SUPER-ADMIN")){
            listaRol = dbRol.getSpinnerById("2");
        }else{
            listaRol = dbRol.getSpinnerNotSuperAdmin();
        }

        ArrayAdapter<SpinnerBean> adapterRol = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaRol);
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRol.setAdapter(adapterRol);

        String ide = session.getIdEmpleado();
        if (!(ide.isEmpty())){
            BloquearCampos(false);
            TraerEmpleado(ide);
        }

        linearLayoutUsuario.setVisibility(View.INVISIBLE);
        linearLayoutClave.setVisibility(View.INVISIBLE);

        String NomRol = spRol.getSelectedItem().toString();
        if(NomRol.equals("ADMIN") || NomRol.equals("REGISTRADOR")){
            linearLayoutUsuario.setVisibility(View.VISIBLE);
            linearLayoutClave.setVisibility(View.VISIBLE);
        }

        spRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String NomRol = spRol.getItemAtPosition(position).toString();
                if(NomRol.equals("ADMIN") || NomRol.equals("REGISTRADOR")){
                    linearLayoutUsuario.setVisibility(View.VISIBLE);
                    linearLayoutClave.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutUsuario.setVisibility(View.INVISIBLE);
                    linearLayoutClave.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        btnBuscarEmpleado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BloquearCampos(true);
                String tipoDocumento = spTipoDocumento.getSelectedItem().toString();
                if(!(tipoDocumento.equals("DNI"))){
                    Toast.makeText(context,"Solo puede buscar DNI", Toast.LENGTH_LONG).show();
                    return;
                }

                String NumDocumento = txtNumDocumento.getText().toString();
                if (NumDocumento.isEmpty()){
                    Toast.makeText(context,"Debe ingresar un numero de documento", Toast.LENGTH_LONG).show();
                    return;
                }
                if(NumDocumento.length() != 8){
                    Toast.makeText(context,"El numero de documento debe contener 8 digitos", Toast.LENGTH_LONG).show();
                    return;
                }

                // Buscar Usuario Empleado en el sqlite
                TipoDocumentoBean tipoDocumentoBean = dbTipoDocumento.getByName(tipoDocumento);
                UsuarioBean usuarioBean = dbUsuario.getPorTipoDocumentoNumDocumento(tipoDocumentoBean.getID(),NumDocumento);
                if (usuarioBean != null){
                    PaisBean paisBean = dbPais.get(usuarioBean.getCOD_PAIS());
                    int pos = 0;
                    for (int i=0;i<spPais.getCount();i++){
                        if (spPais.getItemAtPosition(i).toString().equalsIgnoreCase(paisBean.getNOMBRE())){
                            pos = i;
                            break;
                        }
                    }
                    spPais.setSelection(pos);

                    txtNombres.setText(usuarioBean.getNOMBRES());
                    txtApePaterno.setText(usuarioBean.getAPELLIDO_PATERNO());
                    txtApeMaterno.setText(usuarioBean.getAPELLIDO_MATERNO());

                    EmpresaBean empresaBean = dbEmpresa.get(usuarioBean.getID_EMPRESA());
                    pos = 0;
                    for (int i=0;i<spEmpresa.getCount();i++){
                        if (spEmpresa.getItemAtPosition(i).toString().equalsIgnoreCase(empresaBean.getNOM_RAZON_SOCIAL())){
                            pos = i;
                            break;
                        }
                    }
                    spPais.setSelection(pos);

                    pos = 0;
                    for (int i=0;i<spGenero.getCount();i++){
                        if (spGenero.getItemAtPosition(i).toString().equalsIgnoreCase(usuarioBean.getGENERO())){
                            pos = i;
                            break;
                        }
                    }
                    spGenero.setSelection(pos);

                    String fecNac = "";
                    try {
                        fecNac = Util.formatDate(usuarioBean.getFECHA_NACIMIENTO(),"yyyy-MM-dd","dd/MM/yyyy");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    txtFechaNacimiento.setText(fecNac);
                    txtUsuario.setText(usuarioBean.getUSUARIO());
                    txtClave.setText(usuarioBean.getCONTRASENA());
                    txtCorreo.setText(usuarioBean.getCORREO());

                    RolBean rolBean = dbRol.get(usuarioBean.getID_ROL());
                    pos = 0;
                    for (int i=0;i<spRol.getCount();i++){
                        if (spRol.getItemAtPosition(i).toString().equalsIgnoreCase(rolBean.getNOM_ROL())){
                            pos = i;
                            break;
                        }
                    }
                    spRol.setSelection(pos);

                    BloquearCampos(false);
                    spTipoDocumento.setEnabled(true);
                    txtNumDocumento.setEnabled(true);
                    btnBuscarEmpleado.setEnabled(true);
                    return;
                }

                OpenProgressBar();
                // Consulta DNI webservice
                String URL = "https://dniruc.apisperu.com/api/v1/dni/" + NumDocumento + "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5pY29sYXNoaWRhbGdvY29ycmVhQGhvdG1haWwuY29tIn0.vRpQYdBvxUFwsXFehU1KpQzNJhl08IBR69hHBcefGno";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]") || response.equals("")) {
                            CloseProgressBar();
                            Toast.makeText(context, "No se encontraron datos", Toast.LENGTH_LONG).show();
                            LimpiarCampos();
                        }else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                txtNombres.setText(jsonObject.getString("nombres"));
                                txtApePaterno.setText(jsonObject.getString("apellidoPaterno"));
                                txtApeMaterno.setText(jsonObject.getString("apellidoMaterno"));
                            } catch (JSONException e) {
                                CloseProgressBar();
                                Toast.makeText(context,"Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            CloseProgressBar();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CloseProgressBar();
                        Toast.makeText(context,"Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });

        return view;
    }

    private void UpdateFecha() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtFechaNacimiento.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onResume() {
        super.onResume();
        CloseProgressBar();
    }

    public void BloquearCampos(boolean x){
        spTipoDocumento.setEnabled(x);
        txtNumDocumento.setEnabled(x);
        btnBuscarEmpleado.setEnabled(x);
        spPais.setEnabled(x);
        txtNombres.setEnabled(x);
        txtApePaterno.setEnabled(x);
        txtApeMaterno.setEnabled(x);
        //spGenero.setEnabled(x);
        //txtFechaNacimiento.setEnabled(x);
        spEmpresa.setEnabled(x);
        //txtUsuario.setEnabled(x);
        //txtClave.setEnabled(x);
    }
    public void LimpiarCampos(){
        txtNombres.setText("");
        txtApePaterno.setText("");
        txtApeMaterno.setText("");
        txtFechaNacimiento.setText("");
        txtUsuario.setText("");
        txtClave.setText("");
        //txtCorreo.setText("");
    }

    public void TraerEmpleado(String ide){
        UsuarioBean usuarioBean = null;
        usuarioBean = dbUsuario.get(ide);

        TipoDocumentoBean tipoDocumentoBean = dbTipoDocumento.get(usuarioBean.getID_TIPO_DOCUMENTO());
        int pos = 0;
        for (int i=0;i<spTipoDocumento.getCount();i++){
            if (spTipoDocumento.getItemAtPosition(i).toString().equalsIgnoreCase(tipoDocumentoBean.getNOM_DOCUMENTO())){
                pos = i;
                break;
            }
        }
        spPais.setSelection(pos);
        txtNumDocumento.setText(usuarioBean.getNUM_DOCUMENTO());

        PaisBean paisBean = dbPais.get(usuarioBean.getCOD_PAIS());
        pos = 0;
        for (int i=0;i<spPais.getCount();i++){
            if (spPais.getItemAtPosition(i).toString().equalsIgnoreCase(paisBean.getNOMBRE())){
                pos = i;
                break;
            }
        }
        spPais.setSelection(pos);

        txtNombres.setText(usuarioBean.getNOMBRES());
        txtApePaterno.setText(usuarioBean.getAPELLIDO_PATERNO());
        txtApeMaterno.setText(usuarioBean.getAPELLIDO_MATERNO());

        pos = 0;
        for (int i=0;i<spGenero.getCount();i++){
            if (spGenero.getItemAtPosition(i).toString().equalsIgnoreCase(usuarioBean.getGENERO())){
                pos = i;
                break;
            }
        }
        spGenero.setSelection(pos);
        String fecNac = "";
        try {
            fecNac = Util.formatDate(usuarioBean.getFECHA_NACIMIENTO(),"yyyy-MM-dd","dd/MM/yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtFechaNacimiento.setText(fecNac);

        EmpresaBean empresaBean = dbEmpresa.get(usuarioBean.getID_EMPRESA());
        pos = 0;
        for (int i=0;i<spEmpresa.getCount();i++){
            if (spEmpresa.getItemAtPosition(i).toString().equalsIgnoreCase(empresaBean.getNOM_RAZON_SOCIAL())){
                pos = i;
                break;
            }
        }
        spEmpresa.setSelection(pos);

        txtUsuario.setText(usuarioBean.getUSUARIO());
        txtClave.setText(usuarioBean.getCONTRASENA());
        txtCorreo.setText(usuarioBean.getCORREO());

        RolBean rolBean = dbRol.get(usuarioBean.getID_ROL());
        pos = 0;
        for (int i=0;i<spRol.getCount();i++){
            if (spRol.getItemAtPosition(i).toString().equalsIgnoreCase(rolBean.getNOM_ROL())){
                pos = i;
                break;
            }
        }
        spRol.setSelection(pos);

    }

    public void CloseProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
    }
    public void OpenProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
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
}
