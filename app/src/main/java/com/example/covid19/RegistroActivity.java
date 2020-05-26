package com.example.covid19;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import beans.SpinnerBean;
import beans.UsuarioSedeBean;
import db.DatabaseManagerTipoDocumento;
import helper.Session;

public class RegistroActivity extends Fragment {

    View view;
    Spinner spTipoDocumento, spGenero;
    DatabaseManagerTipoDocumento dbTipoDocumento;
    Context context;
    EditText txtNumDocumento, txtNombres, txtApePaterno, txtApeMaterno;
    RequestQueue requestQueue;
    TextView lblNomEmpresa, lblNomSede;
    private Session session;

    public RegistroActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_registro, container, false);
        context = this.getActivity();
        session = new Session(context);

        dbTipoDocumento = new DatabaseManagerTipoDocumento(context);
        spTipoDocumento = (Spinner) view.findViewById(R.id.spTipoDocumento);
        spGenero = (Spinner) view.findViewById(R.id.spGenero);
        txtNumDocumento = (EditText) view.findViewById(R.id.txtNumDocumento);
        txtNombres = (EditText) view.findViewById(R.id.txtNombres);
        txtApePaterno = (EditText) view.findViewById(R.id.txtApellidoPaterno);
        txtApeMaterno = (EditText) view.findViewById(R.id.txtApellidoMaterno);
        lblNomEmpresa = (TextView) view.findViewById(R.id.lblNomEmpresa);
        lblNomSede = (TextView) view.findViewById(R.id.lblNomSede);

        lblNomSede.setText(session.getNomSede());
        lblNomEmpresa.setText(session.getNomEmpresa());

        List<SpinnerBean> listaTipoDocumento = dbTipoDocumento.getSpinner();

        ArrayAdapter<SpinnerBean> adapterTipoDocumento = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaTipoDocumento);
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoDocumento.setAdapter(adapterTipoDocumento);

        String[] value = {"MASCULINO","FEMENINO"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, arrayList);
        spGenero.setAdapter(arrayAdapter);

        Button btnBuscarEmpleado = (Button) view.findViewById(R.id.btnBuscarEmpleado);
        btnBuscarEmpleado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Consulta DNI webservice
                String NumDocumento = txtNumDocumento.getText().toString();
                if (NumDocumento.isEmpty()){
                    Toast.makeText(context,"Debe ingresar un numero de documento", Toast.LENGTH_LONG).show();
                    return;
                }

                String URL = "https://dniruc.apisperu.com/api/v1/dni/" + NumDocumento + "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5pY29sYXNoaWRhbGdvY29ycmVhQGhvdG1haWwuY29tIn0.vRpQYdBvxUFwsXFehU1KpQzNJhl08IBR69hHBcefGno";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")){
                            Toast.makeText(context,"No se encontraron datos", Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                txtNombres.setText(jsonObject.getString("nombres"));
                                txtApePaterno.setText(jsonObject.getString("apellidoPaterno"));
                                txtApeMaterno.setText(jsonObject.getString("apellidoMaterno"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });

        return view;

    }


}
