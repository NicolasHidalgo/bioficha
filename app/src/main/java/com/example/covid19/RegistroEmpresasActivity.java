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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;
import beans.DepartamentoBean;
import beans.DistritoBean;
import beans.EmpresaBean;
import beans.ProvinciaBean;
import beans.SpinnerBean;
import db.DatabaseManagerDepartamento;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerProvincia;
import db.DatabaseManagerDistrito;
import helper.ConnectivityReceiver;
import helper.Session;


public class RegistroEmpresasActivity extends AppCompatActivity {

    Button btnBuscarRUC, btnGrabar;
    EditText txtRUC;
    EditText txtRazonSocial;
    EditText txtDireccionFiscal;
    EditText txtRubro, txtTelefono, txtCorreo, txtContacto;
    Spinner spDepartamento, spProvincia, spDistrito;
    Context context;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    private Session session;
    final String URL = "https://bioficha.electocandidato.com/insert_id.php";
    DatabaseManagerDepartamento dbDepartamento;
    DatabaseManagerProvincia dbProvincia;
    DatabaseManagerDistrito dbDistrito;
    DatabaseManagerEmpresa dbEmpresa;
    public int id_departamento = 0;
    public int id_provincia = 0;
    public int id_distrito = 0;

    int checkDep = 0;
    int checkProv = 0;
    int checkDis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_empresas);
        context = this;
        session = new Session(context);
        btnBuscarRUC = findViewById(R.id.btnBuscarRUC);
        btnGrabar = findViewById(R.id.btnGrabar);
        txtRUC = findViewById(R.id.txtRUC);
        txtRazonSocial = findViewById(R.id.txtRazonSocial);
        txtDireccionFiscal = findViewById(R.id.txtDireccionFiscal);
        txtRubro = findViewById(R.id.txtRubro);
        spDepartamento = findViewById(R.id.spDepartamento);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtContacto = findViewById(R.id.txtContacto);
        txtCorreo = findViewById(R.id.txtCorreo);
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
                if (++checkDep > 1){
                    id_departamento = listaDepartamento.get(position).getID();
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
                if (++checkProv > 1){
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
                if (++checkDis > 1){
                    List<SpinnerBean> listaDistrito = dbDistrito.getListSpinner(Integer.toString(id_provincia));
                    id_distrito = listaDistrito.get(position).getID();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnBuscarRUC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Consulta RUC Sunat
                if (!(ConnectivityReceiver.isConnected(context))) {
                    Toast.makeText(context, "Necesitas contectarte a internet para continuar", Toast.LENGTH_LONG).show();
                    return;
                }
                String RUC = txtRUC.getText().toString();
                if (RUC.isEmpty()) {
                    Toast.makeText(context, "Debe ingresar un ruc válido", Toast.LENGTH_LONG).show();
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
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });

        String ide = session.getIdEmpresa();
        if (!(ide.isEmpty())){
            BloquearCampos(false);
            TraerEmpresa(ide);
        }

        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pRUC = txtRUC.getText().toString();
                final String pNOM_RAZON_SOCIAL = txtRazonSocial.getText().toString();
                final String pACT_ECONOMICAS = txtRubro.getText().toString();
                final String pDIRECCION = txtDireccionFiscal.getText().toString();
                final String pID_DISTRITO = spDistrito.getSelectedItem().toString();
                final String pTELEFONO = txtTelefono.getText().toString();
                final String pCORREO = txtCorreo.getText().toString();
                final String pCONTACTO = txtContacto.getText().toString();
                final String pLATITUD = "0";
                final String pLONGITUD = "0";
                String pAccion = "INSERT";
                String pId = "0";
                final String ide = session.getIdEmpresa();
                if(!(ide.isEmpty())){
                    BloquearCampos(true);
                    pAccion = "UPDATE";
                    pId = ide;
                }
                if (!(ConnectivityReceiver.isConnected(context))) {
                    Toast.makeText(context, "Necesitas contectarte a internet para continuar", Toast.LENGTH_LONG).show();
                    return;
                }
                OpenProgressBar();
                DistritoBean distritoBean = dbDistrito.getByName(pID_DISTRITO);
                final String id_distrito = distritoBean.getID();
                String Params = "";
                Params = Params + "'" + pAccion + "',";
                Params = Params + pId + ",";
                Params = Params + "'" + pRUC + "',";
                Params = Params + "'" + pNOM_RAZON_SOCIAL + "',";
                Params = Params + "'" + pACT_ECONOMICAS + "',";
                Params = Params + "'" + pDIRECCION + "',";
                Params = Params + "'" + id_distrito + "',";
                Params = Params + "'" + pLATITUD + "',";
                Params = Params + "'" + pLONGITUD + "',";
                Params = Params + "'" + pTELEFONO + "',";
                Params = Params + "'" + pCORREO + "',";
                Params = Params + "'" + pCONTACTO + "'";
                final String QUERY = "call SP_UPDATE_EMPRESA(" + Params + ");";
                final String finalAccion = pAccion;
                final String finalId = pId;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")) {
                            CloseProgressBar();
                            Toast.makeText(context, "No se encontraron datos", Toast.LENGTH_LONG).show();
                        } else if (response.equals("")) {
                            CloseProgressBar();
                            Toast.makeText(context, "Error en el servicio", Toast.LENGTH_LONG).show();
                        } else
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = null;
                                String ID_EMPRESA = "";
                                String MENSAJE = "";
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    ID_EMPRESA = jsonObject.getString("ID");
                                    MENSAJE = jsonObject.getString("MENSAJE");
                                    if (ID_EMPRESA.equals("0")) {
                                        CloseProgressBar();
                                        Toast.makeText(context, MENSAJE, Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                                // INSERTAMOS LA FICHA EN EL SQLITE
                                EmpresaBean bioFichaEmpresaBean = new EmpresaBean();
                                bioFichaEmpresaBean.setID(ID_EMPRESA);
                                bioFichaEmpresaBean.setRUC(pRUC);
                                bioFichaEmpresaBean.setNOM_RAZON_SOCIAL(pNOM_RAZON_SOCIAL);
                                bioFichaEmpresaBean.setACT_ECONOMICAS(pACT_ECONOMICAS);
                                bioFichaEmpresaBean.setDIRECCION(pDIRECCION);
                                bioFichaEmpresaBean.setID_DISTRITO(id_distrito);
                                bioFichaEmpresaBean.setTELEFONO(pTELEFONO);
                                bioFichaEmpresaBean.setCORREO(pCORREO);
                                bioFichaEmpresaBean.setCONTACTO(pCONTACTO);
                                Date currentTime = Calendar.getInstance().getTime();
                                String fecha = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
                                bioFichaEmpresaBean.setFEC_CREACION(fecha);
                                if(finalAccion.equals("UPDATE")){
                                    bioFichaEmpresaBean.setFEC_ACTUALIZACION(fecha);
                                    dbEmpresa.actualizar(bioFichaEmpresaBean);
                                }else{
                                    dbEmpresa.insertar(bioFichaEmpresaBean);
                                }

                            } catch (JSONException e) {
                                CloseProgressBar();
                                Toast.makeText(context,"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        CloseProgressBar();
                        if(finalAccion.equals("UPDATE")) {
                            Toast.makeText(context, "Los datos han sido actualizados con éxito", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CloseProgressBar();
                        Toast.makeText(context,"Error:" + error.getMessage(),Toast.LENGTH_LONG).show();
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

    public void CloseProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void OpenProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void BloquearCampos(boolean x){
        txtRUC.setEnabled(x);
    }

    public void TraerEmpresa(String ide){
        EmpresaBean empresaBean = null;
        empresaBean = dbEmpresa.getObject(ide);
        txtRUC.setText(empresaBean.getRUC());
        txtRazonSocial.setText(empresaBean.getNOM_RAZON_SOCIAL());
        txtDireccionFiscal.setText(empresaBean.getDIRECCION());
        txtRubro.setText(empresaBean.getACT_ECONOMICAS());
        txtContacto.setText(empresaBean.getCONTACTO());
        txtTelefono.setText(empresaBean.getTELEFONO());
        DepartamentoBean departamentoBean = dbDepartamento.get(empresaBean.getID_DEPARTAMENTO());
        int pos = 0;
        for (int i=0;i<spDepartamento.getCount();i++){
            if (spDepartamento.getItemAtPosition(i).toString().equalsIgnoreCase(departamentoBean.getNOM_DEPARTAMENTO())){
                pos = i;
                break;
            }
        }
        spDepartamento.setSelection(pos);

        List<SpinnerBean> listaProvincia = dbProvincia.getListSpinner(empresaBean.getID_DEPARTAMENTO());
        ArrayAdapter adapterProvincia = new ArrayAdapter(context, R.layout.custom_spinner, listaProvincia);
        adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvincia.setAdapter(adapterProvincia);

        ProvinciaBean provinciaBean = dbProvincia.get(empresaBean.getID_PROVINCIA());
        int pos1 = 0;
        for (int j=0;j<spProvincia.getCount();j++){
            if (spProvincia.getItemAtPosition(j).toString().equalsIgnoreCase(provinciaBean.getNOM_PROVINCIA())){
                pos1 = j;
                break;
            }
        }
        spProvincia.setSelection(pos1);
        spProvincia.setEnabled(true);
        List<SpinnerBean> listaDistrito = dbDistrito.getListSpinner(empresaBean.getID_PROVINCIA());
        ArrayAdapter adapterDistrito = new ArrayAdapter(context, R.layout.custom_spinner, listaDistrito);
        adapterDistrito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistrito.setAdapter(adapterDistrito);
        DistritoBean distritoBean = dbDistrito.get(empresaBean.getID_DISTRITO());
        int pos2 = 0;
        for (int k=0;k<spDistrito.getCount();k++){
            if (spDistrito.getItemAtPosition(k).toString().equalsIgnoreCase(distritoBean.getNOM_DISTRITO())){
                pos2 = k;
                break;
            }
        }

        spDistrito.setSelection(pos2);
        spDistrito.setEnabled(true);

    }

}

