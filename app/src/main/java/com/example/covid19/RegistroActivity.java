package com.example.covid19;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import beans.BioFichaBean;
import beans.PaisBean;
import beans.SpinnerBean;
import beans.TipoDocumentoBean;
import beans.UsuarioBean;
import beans.UsuarioSedeBean;
import db.DatabaseManagerBioFicha;
import db.DatabaseManagerPais;
import db.DatabaseManagerTipoDocumento;
import db.DatabaseManagerUsuario;
import helper.Session;
import util.Util;

public class RegistroActivity extends Fragment {

    View view;
    Spinner spTipoDocumento, spGenero, spPais;
    DatabaseManagerTipoDocumento dbTipoDocumento;
    DatabaseManagerPais dbPais;
    DatabaseManagerUsuario dbUsuario;
    DatabaseManagerBioFicha dbFicha;
    Context context;
    EditText txtNumDocumento, txtNombres, txtApePaterno, txtApeMaterno, txtCorreo;
    EditText txtFechaNacimiento, txtEstatura, txtPeso, txtGrados;
    TextView lblAlerta1, lblAlerta2, lblAlerta3;
    RequestQueue requestQueue;
    TextView lblNomEmpresa, lblNomSede, lblIMC;
    private Session session;
    Button btnBuscarEmpleado;

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    ProgressBar progressBar;

    int check = 0;
    public static final String SERVER = "https://bioficha.electocandidato.com/";
    public String URL = SERVER + "select.php";

    public RegistroActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_registro, container, false);
        context = this.getActivity();
        session = new Session(context);

        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);

        dbTipoDocumento = new DatabaseManagerTipoDocumento(context);
        dbPais = new DatabaseManagerPais(context);
        dbUsuario = new DatabaseManagerUsuario(context);
        dbFicha = new DatabaseManagerBioFicha(context);

        spTipoDocumento = (Spinner) view.findViewById(R.id.spTipoDocumento);
        spGenero = (Spinner) view.findViewById(R.id.spGenero);
        txtNumDocumento = (EditText) view.findViewById(R.id.txtNumDocumento);
        spPais = (Spinner) view.findViewById(R.id.spPais);
        txtNombres = (EditText) view.findViewById(R.id.txtNombres);
        txtApePaterno = (EditText) view.findViewById(R.id.txtApellidoPaterno);
        txtApeMaterno = (EditText) view.findViewById(R.id.txtApellidoMaterno);
        spGenero = (Spinner) view.findViewById(R.id.spGenero);
        txtCorreo = (EditText) view.findViewById(R.id.txtCorreo);
        txtFechaNacimiento = (EditText) view.findViewById(R.id.txtFechaNacimiento);
        txtEstatura = (EditText) view.findViewById(R.id.txtEstatura);
        txtPeso = (EditText) view.findViewById(R.id.txtPeso);
        txtGrados = (EditText) view.findViewById(R.id.txtGrados);
        lblIMC = (TextView) view.findViewById(R.id.lblIMC);
        lblAlerta1 = (TextView) view.findViewById(R.id.lblAlerta1);
        lblAlerta2 = (TextView) view.findViewById(R.id.lblAlerta2);
        lblAlerta3 = (TextView) view.findViewById(R.id.lblAlerta3);

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateFecha();
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

        txtFechaNacimiento.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                Alerta3();
            }
        });

        txtFechaNacimiento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new DatePickerDialog(context, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        txtEstatura.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                String estatura = txtEstatura.getText().toString();
                String peso = txtPeso.getText().toString();

                if (estatura.isEmpty()){
                    return;
                }
                if (peso.isEmpty()){
                    return;
                }
                if(estatura.equals("0")){
                    return;
                }
                
                float fest = Float.parseFloat(txtEstatura.getText().toString());
                float fpes = Float.parseFloat(txtPeso.getText().toString());

                float resultado = (fpes/(fest*fest));
                lblIMC.setText(String.format("%.2f",resultado));

                Alerta1();
            }
        });

        txtGrados.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                Alerta2();
            }
        });

        txtPeso.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                String estatura = txtEstatura.getText().toString();
                String peso = txtPeso.getText().toString();

                if (estatura.isEmpty()){
                    return;
                }
                if (peso.isEmpty()){
                    return;
                }
                if(estatura.equals("0")){
                    return;
                }

                float fest = Float.parseFloat(txtEstatura.getText().toString());
                float fpes = Float.parseFloat(txtPeso.getText().toString());

                float resultado = (fpes/(fest*fest));
                lblIMC.setText(String.format("%.2f",resultado));

                Alerta1();
            }
        });

        lblNomEmpresa = (TextView) view.findViewById(R.id.lblNomEmpresa);
        lblNomSede = (TextView) view.findViewById(R.id.lblNomSede);

        lblNomSede.setText(session.getNomSede());
        lblNomEmpresa.setText(session.getNomEmpresa());
        btnBuscarEmpleado = (Button) view.findViewById(R.id.btnBuscarEmpleado);

        List<SpinnerBean> listaTipoDocumento = dbTipoDocumento.getSpinner();
        ArrayAdapter<SpinnerBean> adapterTipoDocumento = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaTipoDocumento);
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoDocumento.setAdapter(adapterTipoDocumento);

        List<SpinnerBean> listaPais = dbPais.getSpinner();
        final ArrayAdapter<SpinnerBean> adapterPais = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaPais);
        adapterPais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPais.setAdapter(adapterPais);

        String[] value = {"MASCULINO","FEMENINO"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, arrayList);
        spGenero.setAdapter(arrayAdapter);

        String ide = session.getIdFicha();
        if (!(ide.isEmpty())){
            BloquearCampos(false);
            TraerFicha(ide);
        }

        spTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(++check > 1) {
                    LimpiarCampos();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
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

                final String NumDocumento = txtNumDocumento.getText().toString();
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
                    txtEstatura.setText(usuarioBean.getESTATURA());
                    txtPeso.setText(usuarioBean.getPESO());

                    BloquearCampos(false);
                    spTipoDocumento.setEnabled(true);
                    txtNumDocumento.setEnabled(true);
                    btnBuscarEmpleado.setEnabled(true);
                    return;
                }

                // Si no esta en el sqlite, buscamos al mysql (ws)
                OpenProgressBar();

                String ACCION = "CONSULTA";
                final String QUERY = "call SP_USUARIO('" + ACCION  + "'," + session.getIdEmpresa() + ",'',''," + tipoDocumentoBean.getID() +",'"+NumDocumento+"');";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("[]") || response.isEmpty()){
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
                                    Toast.makeText(context,"Error: no se encontraron datos", Toast.LENGTH_LONG).show();
                                }
                            });
                            requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);

                        }else{
                            try {
                                UsuarioBean bean  = null;
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    bean = new UsuarioBean();
                                    txtNombres.setText(jsonObject.getString("NOMBRES"));
                                    txtApePaterno.setText(jsonObject.getString("APELLIDO_PATERNO"));
                                    txtApeMaterno.setText(jsonObject.getString("APELLIDO_MATERNO"));

                                    String estatura = jsonObject.getString("ESTATURA");
                                    if (!(estatura.equals("null"))){
                                        txtEstatura.setText(estatura);
                                    }

                                    String peso = jsonObject.getString("PESO");
                                    if (!(peso.equals("null"))){
                                        txtPeso.setText(peso);
                                    }

                                    String fecNac = "";
                                    try {
                                        fecNac = Util.formatDate(jsonObject.getString("FECHA_NACIMIENTO"),"yyyy-MM-dd","dd/MM/yyyy");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    txtFechaNacimiento.setText(fecNac);


                                    PaisBean paisBean = dbPais.get(jsonObject.getString("COD_PAIS"));
                                    int pos = 0;
                                    for (int j=0;j<spPais.getCount();j++){
                                        if (spPais.getItemAtPosition(j).toString().equalsIgnoreCase(paisBean.getNOMBRE())){
                                            pos = j;
                                            break;
                                        }
                                    }
                                    spPais.setSelection(pos);

                                    pos = 0;
                                    for (int j=0;j<spGenero.getCount();j++){
                                        if (spGenero.getItemAtPosition(j).toString().equalsIgnoreCase(jsonObject.getString("GENERO"))){
                                            pos = j;
                                            break;
                                        }
                                    }
                                    spGenero.setSelection(pos);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            CloseProgressBar();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CloseProgressBar();
                        Toast.makeText(context,"Error: no se encontraron datos", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parametros = new HashMap<>();
                        parametros.put("consulta",QUERY);
                        return parametros;
                    }
                };
                requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }
        });
        return view;

    }

    private void updateFecha() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtFechaNacimiento.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onResume() {
        super.onResume();
        CloseProgressBar();
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

    public void BloquearCampos(boolean x){
        spTipoDocumento.setEnabled(x);
        txtNumDocumento.setEnabled(x);
        btnBuscarEmpleado.setEnabled(x);
        spPais.setEnabled(x);
        txtNombres.setEnabled(x);
        txtApePaterno.setEnabled(x);
        txtApeMaterno.setEnabled(x);
        spGenero.setEnabled(x);
        txtFechaNacimiento.setEnabled(x);
    }
    public void LimpiarCampos(){
        txtNombres.setText("");
        txtApePaterno.setText("");
        txtApeMaterno.setText("");
        txtFechaNacimiento.setText("");
    }


    public void TraerFicha(String ide){
        BioFichaBean bioFichaBean = null;
        bioFichaBean = dbFicha.getObject(ide);

        TipoDocumentoBean tipoDocumentoBean = dbTipoDocumento.get(bioFichaBean.getID_TIPO_DOCUMENTO());
        int pos = 0;
        for (int i=0;i<spTipoDocumento.getCount();i++){
            if (spTipoDocumento.getItemAtPosition(i).toString().equalsIgnoreCase(tipoDocumentoBean.getNOM_DOCUMENTO())){
                pos = i;
                break;
            }
        }
        spPais.setSelection(pos);
        txtNumDocumento.setText(bioFichaBean.getNUM_DOCUMENTO());

        PaisBean paisBean = dbPais.get(bioFichaBean.getCOD_PAIS());
        pos = 0;
        for (int i=0;i<spPais.getCount();i++){
            if (spPais.getItemAtPosition(i).toString().equalsIgnoreCase(paisBean.getNOMBRE())){
                pos = i;
                break;
            }
        }
        spPais.setSelection(pos);

        txtNombres.setText(bioFichaBean.getNOMBRES());
        txtApePaterno.setText(bioFichaBean.getAPELLIDO_PATERNO());
        txtApeMaterno.setText(bioFichaBean.getAPELLIDO_MATERNO());

        pos = 0;
        for (int i=0;i<spGenero.getCount();i++){
            if (spGenero.getItemAtPosition(i).toString().equalsIgnoreCase(bioFichaBean.getGENERO())){
                pos = i;
                break;
            }
        }
        spGenero.setSelection(pos);
        String fecNac = "";
        try {
            fecNac = Util.formatDate(bioFichaBean.getFECHA_NACIMIENTO(),"yyyy-MM-dd","dd/MM/yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtFechaNacimiento.setText(fecNac);
        txtEstatura.setText(bioFichaBean.getESTATURA());
        txtPeso.setText(bioFichaBean.getPESO());
        txtGrados.setText(bioFichaBean.getGRADO_CELSIUS());
    }

    public void Alerta1(){
        String imc =  lblIMC.getText().toString().replace(",",".");
        lblAlerta1.setText("IMC OK");
        lblAlerta1.setTextColor(Color.GREEN);
        if (!(imc.isEmpty())){
            float resultado = Float.parseFloat(imc);
            if (resultado > 40){
                lblAlerta1.setText("IMC: SEGUN D.S. 083-2020-PCM, NO ESTA AUTORIZADO A LABORAR POR RERPESENTAR RIESGO INMINENTE A SU SALUD");
                lblAlerta1.setTextColor(Color.RED);

            }
        }

    }

    public void Alerta2(){
        String valor = txtGrados.getText().toString();
        String mensaje = "TEMPERATURA OK";
        lblAlerta2.setTextColor(Color.GREEN);
        if (!(valor.isEmpty())){
            double grados = Double.parseDouble(valor);
            if(grados >= 38.0){
                mensaje = "TEMPERATURA: SEGUN D.S. 083-2020-PCM, NO ESTA AUTORIZADO INGRESAR A LABORAR POR REPRESENTAR UN RIESGO AL CENTRO DE TRABAJO";
                lblAlerta2.setTextColor(Color.RED);
            }
        }
        lblAlerta2.setText(mensaje);
    }

    public void Alerta3(){
        String mensaje = "EDAD OK";
        lblAlerta3.setTextColor(Color.GREEN);
        String fechaNacimiento = txtFechaNacimiento.getText().toString();
        if (!(fechaNacimiento.isEmpty())){
            String [] dateParts = fechaNacimiento.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            int edad = Util.getAge(year,month,day);
            if (edad >= 65){
                mensaje = "EDAD: SEGUN D.S. 083-2020-PCM, PRESENTAR DECLARACION JURADA, DONDE SE HACE RESPONSABLE POR SU ESTADO DE SALUD";
                lblAlerta3.setTextColor(Color.RED);
            }
        }
        lblAlerta3.setText(mensaje);
    }

}
