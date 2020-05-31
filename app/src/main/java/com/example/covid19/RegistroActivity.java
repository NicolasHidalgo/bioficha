package com.example.covid19;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import beans.SpinnerBean;
import beans.UsuarioSedeBean;
import db.DatabaseManagerPais;
import db.DatabaseManagerTipoDocumento;
import helper.Session;

public class RegistroActivity extends Fragment {

    View view;
    Spinner spTipoDocumento, spGenero, spPais;
    DatabaseManagerTipoDocumento dbTipoDocumento;
    DatabaseManagerPais dbPais;
    Context context;
    EditText txtNumDocumento, txtNombres, txtApePaterno, txtApeMaterno, txtCorreo;
    EditText txtFechaNacimiento, txtEstatura, txtPeso, txtGrados;
    RequestQueue requestQueue;
    TextView lblNomEmpresa, lblNomSede, lblIMC;
    private Session session;

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    ProgressBar progressBar;

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
            }
        });

        lblNomEmpresa = (TextView) view.findViewById(R.id.lblNomEmpresa);
        lblNomSede = (TextView) view.findViewById(R.id.lblNomSede);

        lblNomSede.setText(session.getNomSede());
        lblNomEmpresa.setText(session.getNomEmpresa());

        List<SpinnerBean> listaTipoDocumento = dbTipoDocumento.getSpinner();
        ArrayAdapter<SpinnerBean> adapterTipoDocumento = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaTipoDocumento);
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoDocumento.setAdapter(adapterTipoDocumento);

        List<SpinnerBean> listaPais = dbPais.getSpinner();
        ArrayAdapter<SpinnerBean> adapterPais = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaPais);
        adapterPais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPais.setAdapter(adapterPais);

        String[] value = {"MASCULINO","FEMENINO"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, arrayList);
        spGenero.setAdapter(arrayAdapter);

        Button btnBuscarEmpleado = (Button) view.findViewById(R.id.btnBuscarEmpleado);
        btnBuscarEmpleado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Consulta DNI webservice

                String tipoDocumento = spTipoDocumento.getSelectedItem().toString();
                if(!(tipoDocumento.equals("DNI"))){
                    Toast.makeText(context,"Solo puede buscar DNI", Toast.LENGTH_LONG).show();
                    return;
                }

                OpenProgressBar();
                String NumDocumento = txtNumDocumento.getText().toString();
                if (NumDocumento.isEmpty()){
                    CloseProgressBar();
                    Toast.makeText(context,"Debe ingresar un numero de documento", Toast.LENGTH_LONG).show();
                    return;
                }

                String URL = "https://dniruc.apisperu.com/api/v1/dni/" + NumDocumento + "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5pY29sYXNoaWRhbGdvY29ycmVhQGhvdG1haWwuY29tIn0.vRpQYdBvxUFwsXFehU1KpQzNJhl08IBR69hHBcefGno";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")) {
                            CloseProgressBar();
                            Toast.makeText(context, "No se encontraron datos", Toast.LENGTH_LONG).show();
                        }else if (response.equals("")){
                            CloseProgressBar();
                            Toast.makeText(context,"No se encontraron datos", Toast.LENGTH_LONG).show();
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


}
