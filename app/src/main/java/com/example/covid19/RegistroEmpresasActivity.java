package com.example.covid19;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import beans.ProvinciaBean;
import beans.SpinnerBean;
import db.DatabaseManagerDepartamento;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerProvincia;
import db.DatabaseManagerDistrito;
import helper.ConnectivityReceiver;


public class RegistroEmpresasActivity extends AppCompatActivity {

    Button btnBuscarRUC, btnGrabar;
    EditText txtRUC;
    EditText txtRazonSocial;
    EditText txtDireccionFiscal;
    EditText txtRubro;
    Spinner spDepartamento, spProvincia, spDistrito;
    Context context;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    final String URL = "https://bioficha.electocandidato.com/insert_id.php";
    DatabaseManagerDepartamento dbDepartamento;
    DatabaseManagerProvincia dbProvincia;
    DatabaseManagerDistrito dbDistrito;
    DatabaseManagerEmpresa dbEmpresa;
    public int id_departamento = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_empresas);
        context = this;
        btnBuscarRUC = findViewById(R.id.btnBuscarRUC);
        btnGrabar = findViewById(R.id.btnGrabar);
        txtRUC = findViewById(R.id.txtRUC);
        txtRazonSocial = findViewById(R.id.txtRazonSocial);
        txtDireccionFiscal = findViewById(R.id.txtDireccionFiscal);
        txtRubro = findViewById(R.id.txtRubro);
        spDepartamento = findViewById(R.id.spDepartamento);
        spProvincia = findViewById(R.id.spProvincia);
        spDistrito = findViewById(R.id.spDistrito);
        spProvincia.setEnabled(false);
        spDistrito.setEnabled(false);
        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);
        dbDepartamento = new DatabaseManagerDepartamento(context);
        dbProvincia = new DatabaseManagerProvincia(context);
        dbDistrito = new DatabaseManagerDistrito(context);
        dbEmpresa = new DatabaseManagerEmpresa(context);
        final List<SpinnerBean> listaDepartamento = dbDepartamento.getSpinner();
        ArrayAdapter<SpinnerBean> adapterDepartamento = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaDepartamento);
        adapterDepartamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartamento.setAdapter(adapterDepartamento);
        String[] provincia = {"Seleccione"};
        ArrayAdapter adapterProvincia = new ArrayAdapter(context,R.layout.custom_spinner, provincia);
        adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvincia.setAdapter(adapterProvincia);
        String[] distrito = {"Seleccione"};
        ArrayAdapter adapterDistrito = new ArrayAdapter(context,R.layout.custom_spinner, distrito);
        adapterDistrito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistrito.setAdapter(adapterDistrito);
        spDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_departamento = listaDepartamento.get(position).getID();
                if(id_departamento==-1){
                    String[] provincia = {"Seleccione"};
                    ArrayAdapter adapterProvincia = new ArrayAdapter(context,R.layout.custom_spinner, provincia);
                    adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProvincia.setAdapter(adapterProvincia);
                    spProvincia.setEnabled(false);
                    return;
                }
                List<SpinnerBean> listaProvincia = dbProvincia.getListSpinner(Integer.toString(id_departamento));
                ArrayAdapter adapterProvincia = new ArrayAdapter(context,R.layout.custom_spinner, listaProvincia);
                adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spProvincia.setAdapter(adapterProvincia);
                spProvincia.setEnabled(true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<SpinnerBean> listaProvincia = dbProvincia.getListSpinner(Integer.toString(id_departamento));
                int id_provincia = listaProvincia.get(position).getID();
                if(id_provincia==-1){
                    String[] distrito = {"Seleccione"};
                    ArrayAdapter adapterDistrito = new ArrayAdapter(context,R.layout.custom_spinner, distrito);
                    adapterDistrito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDistrito.setAdapter(adapterDistrito);
                    spDistrito.setEnabled(false);
                    return;
                }

                List<SpinnerBean> listaDistrito = dbDistrito.getListSpinner(Integer.toString(id_provincia));
                ArrayAdapter adapterDistrito = new ArrayAdapter(context,R.layout.custom_spinner, listaDistrito);
                adapterDistrito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDistrito.setAdapter(adapterDistrito);
                spDistrito.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnBuscarRUC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Consulta RUC Sunat
                String RUC = txtRUC.getText().toString();
                if (RUC.isEmpty()){
                    Toast.makeText(context,"Debe ingresar un ruc válido", Toast.LENGTH_LONG).show();
                    return;
                }
                String URL = "https://dniruc.apisperu.com/api/v1/ruc/" + RUC + "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5pY29sYXNoaWRhbGdvY29ycmVhQGhvdG1haWwuY29tIn0.vRpQYdBvxUFwsXFehU1KpQzNJhl08IBR69hHBcefGno";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")) {
                            Toast.makeText(context, "No se encontraron datos", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                txtRazonSocial.setText(jsonObject.getString("razonSocial"));
                                txtDireccionFiscal.setText(jsonObject.getString("direccion"));
                                JSONArray c = jsonObject.getJSONArray("actEconomicas");
                                txtRubro.setText(c.getString(0));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse (VolleyError error) {

                    }
                });
                requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(ConnectivityReceiver.isConnected(context))){
                    Toast.makeText(context, "Necesitas contectarte a internet para continuar", Toast.LENGTH_LONG).show();
                    return;
                }
                final String QUERY = "call SP_FICHA_ENFERMEDAD('INSERT'," + ID_FICHAK + ",'" + ENFERMEDADES + "');";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new HashMap<>();
                        parametros.put("consulta", QUERY);
                        return parametros;
                    }
                };
                requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });

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
    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


}

