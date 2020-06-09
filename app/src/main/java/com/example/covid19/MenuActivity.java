package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import beans.EmpresaBean;
import beans.RolBean;
import beans.SedeBean;
import beans.SpinnerBean;
import beans.UsuarioBean;
import beans.UsuarioSedeBean;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerRol;
import db.DatabaseManagerSede;
import db.DatabaseManagerUsuario;
import db.DatabaseManagerUsuarioSede;
import helper.ConnectivityReceiver;
import helper.Session;
import util.Util;
import ws.WebService;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class MenuActivity extends AppCompatActivity {
    LinearLayout btnEmpresa, btnRegistrador, btnSede;
    Button btnVerFichas, btnSincronizar;
    TextView lblNomUsuario, lblRol;
    Spinner spSede;
    Context context;
    DatabaseManagerSede dbSede;
    DatabaseManagerEmpresa dbEmpresa;
    LinearLayout linearLayoutSeleccione;

    DatabaseManagerUsuario dbUsuario;
    DatabaseManagerUsuarioSede dbUsuarioSede;
    DatabaseManagerRol dbRol;
    private Session session;

    public static final String SERVER = "https://bioficha.electocandidato.com/";
    public String URL = SERVER + "select.php";
    public String ACCION = "SELECT";
    public String QUERY = "";
    RequestQueue requestQueue;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //spSede = (Spinner) findViewById(R.id.spSede);
        btnVerFichas = findViewById(R.id.btnVerFichas);
        btnSincronizar = findViewById(R.id.btnSincronizar);

        btnEmpresa = findViewById(R.id.btnEmpresa);
        btnRegistrador = findViewById(R.id.btnRegistrador);
        btnSede = findViewById(R.id.btnSede);
        linearLayoutSeleccione = findViewById(R.id.LinearLayoutSeleccione);

        btnEmpresa.setVisibility(LinearLayout.GONE);
        btnRegistrador.setVisibility(LinearLayout.GONE);
        btnSede.setVisibility(LinearLayout.GONE);

        btnEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(MenuActivity.this, EmpresasActivity.class);
                startActivity(dsp);
            }
        });
        btnRegistrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(MenuActivity.this, RegistradorActivity.class);
                startActivity(dsp);
            }
        });
        btnSede.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent dsp = new Intent(MenuActivity.this, SedeActivity.class);
                        startActivity(dsp);
                    }
                });
        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(ConnectivityReceiver.isConnected(context))){
                    Toast.makeText(context, "Necesita contectarte a internet para continuar", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(context, "Sincronizando...", Toast.LENGTH_SHORT).show();
                WebService web = new WebService(context);
                web.WebServiceRol();
                web.WebServiceEnfermedad();
                web.WebServiceSintoma();
                web.WebServiceTipoDocumento();
                web.WebServicePais();
            }
        });
        btnVerFichas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ItemSede = "";
                if(spSede != null && spSede.getSelectedItem() !=null ) {
                    ItemSede = spSede.getSelectedItem().toString();
                } else  {
                    Toast.makeText(context, "Usted no tiene sedes asignadas", Toast.LENGTH_SHORT).show();
                    return;
                }

                SedeBean sedeBean = dbSede.getByName(ItemSede);
                EmpresaBean empresaBean = dbEmpresa.get(session.getIdEmpresa());

                session.setIdSede(sedeBean.getID());
                session.setNomSede(sedeBean.getNOMBRE_SEDE());
                session.setIdEmpresa(empresaBean.getID());
                session.setNomEmpresa(empresaBean.getNOM_RAZON_SOCIAL());

                Intent dsp = new Intent(MenuActivity.this, FichasActivity.class);
                startActivity(dsp);
            }
        });

        context = this;
        session = new Session(context);

        dbUsuario = new DatabaseManagerUsuario(context);
        dbRol = new DatabaseManagerRol(context);
        dbSede = new DatabaseManagerSede(context);
        dbEmpresa = new DatabaseManagerEmpresa(context);
        dbUsuarioSede = new DatabaseManagerUsuarioSede(context);


        UsuarioBean usuarioBean = dbUsuario.get(session.getIdUsuario());
        RolBean rolBean = dbRol.get(usuarioBean.getID_ROL());
        session.setNomRol(rolBean.getNOM_ROL());

        lblNomUsuario = (TextView) findViewById(R.id.lblNomUsuario);
        lblNomUsuario.setText(usuarioBean.getNOMBRES() + " " + usuarioBean.getAPELLIDO_PATERNO());


        spSede = (Spinner) findViewById(R.id.spSede);
        List<SpinnerBean> listaSede = null;
        if (rolBean.getNOM_ROL().equals("SUPER-ADMIN")){
            btnEmpresa.setVisibility(LinearLayout.VISIBLE);
            btnRegistrador.setVisibility(LinearLayout.VISIBLE);
            btnSede.setVisibility(LinearLayout.VISIBLE);
            spSede.setVisibility(View.INVISIBLE);
            btnVerFichas.setVisibility(View.INVISIBLE);
            linearLayoutSeleccione.setVisibility(View.INVISIBLE);

            WebServiceEmpresa();
            WebServiceUsuarioADMIN();

        }else if (rolBean.getNOM_ROL().equals("ADMIN")) {
            btnEmpresa.setVisibility(LinearLayout.VISIBLE);
            btnRegistrador.setVisibility(LinearLayout.VISIBLE);
            btnSede.setVisibility(LinearLayout.VISIBLE);
            spSede.setVisibility(View.INVISIBLE);
            btnVerFichas.setVisibility(View.INVISIBLE);
            linearLayoutSeleccione.setVisibility(View.INVISIBLE);

            WebServiceEmpleado();
            WebServiceSede();
            WebServiceUsuarioSede();

        }else if (rolBean.getNOM_ROL().equals("REGISTRADOR")) {
            btnEmpresa.setVisibility(LinearLayout.INVISIBLE);
            btnRegistrador.setVisibility(LinearLayout.INVISIBLE);
            btnSede.setVisibility(LinearLayout.INVISIBLE);
            spSede.setVisibility(View.VISIBLE);
            btnVerFichas.setVisibility(View.VISIBLE);
            linearLayoutSeleccione.setVisibility(View.VISIBLE);
            btnRegistrador.getLayoutParams().height = 0;
            btnEmpresa.getLayoutParams().height = 0;
            btnSede.getLayoutParams().height = 0;

            listaSede =  dbSede.getSpinnerAll();
            List<UsuarioSedeBean> listaUsuarioSede = dbUsuarioSede.getList(usuarioBean.getID());
            String sedes = "";
            int i = 1;
            for (UsuarioSedeBean us: listaUsuarioSede) {
                sedes = sedes + us.getID_SEDE();
                if (i != listaUsuarioSede.size())
                    sedes = sedes + ",";
                i++;
            }

            listaSede =  dbSede.getSpinner(sedes);

            ArrayAdapter<SpinnerBean> adapterTurno = new ArrayAdapter<SpinnerBean>(context, R.layout.custom_spinner, listaSede);
            adapterTurno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spSede.setAdapter(adapterTurno);
        }



    }

    public void WebServiceEmpresa(){
        if(!(ConnectivityReceiver.isConnected(context))){
            Toast.makeText(this, "Necesitas contectarte a internet para sincronizar empresas", Toast.LENGTH_LONG).show();
            return;
        }

        dbEmpresa = new DatabaseManagerEmpresa(context);
        String descripcion = "XXX";
        ACCION = "SELECT";
        QUERY = "call SP_EMPRESA('" + ACCION + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dbEmpresa.eliminarTodo();
                if (response.equals("[]") || response.equals("")){
                    //Toast.makeText(context, "No se encontraron datos SP_EMPRESA", Toast.LENGTH_LONG).show();
                }else{
                    try {
                        EmpresaBean bean = null;
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            bean = new EmpresaBean();
                            bean.setID(jsonObject.getString("ID"));
                            bean.setRUC(jsonObject.getString("RUC"));
                            bean.setNOM_RAZON_SOCIAL(jsonObject.getString("NOM_RAZON_SOCIAL"));
                            bean.setACT_ECONOMICAS(jsonObject.getString("ACT_ECONOMICAS"));
                            bean.setDIRECCION(jsonObject.getString("DIRECCION"));
                            bean.setID_DISTRITO(jsonObject.getString("ID_DISTRITO"));
                            bean.setLATITUD(jsonObject.getString("LATITUD"));
                            bean.setLONGITUD(jsonObject.getString("LONGITUD"));
                            bean.setTELEFONO(jsonObject.getString("TELEFONO"));
                            bean.setCORREO(jsonObject.getString("CORREO"));
                            bean.setCONTACTO(jsonObject.getString("CONTACTO"));
                            bean.setFEC_CREACION(jsonObject.getString("FEC_CREACION"));
                            bean.setFEC_ACTUALIZACION(jsonObject.getString("FEC_ACTUALIZACION"));
                            bean.setFEC_ELIMINACION(jsonObject.getString("FEC_ELIMINACION"));
                            dbEmpresa.insertar(bean);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Error en el registro json EMPRESA: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error servicio EMPRESA: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void WebServiceUsuarioADMIN(){
        dbUsuario = new DatabaseManagerUsuario(context);
        String descripcion = "XXX";
        String ACCION = "SELECT_ADMIN";
        String Usuario = "";
        String Password = "";
        String IdEmpresa = session.getIdEmpresa();
        final String QUERY = "call SP_USUARIO('" + ACCION  + "',"+ IdEmpresa +",'" + Usuario + "','" + Password + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dbUsuario.eliminarTodo();
                    if (response.equals("[]") || response.equals("")){
                        //Toast.makeText(context, "No se encontraron datos SP_EMPLEADO", Toast.LENGTH_LONG).show();
                    }else{
                        UsuarioBean bean  = null;
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            bean = new UsuarioBean();
                            bean.setID(jsonObject.getString("ID"));
                            bean.setID_TIPO_DOCUMENTO(jsonObject.getString("ID_TIPO_DOCUMENTO"));
                            bean.setNUM_DOCUMENTO(jsonObject.getString("NUM_DOCUMENTO"));
                            bean.setCOD_PAIS(jsonObject.getString("COD_PAIS"));
                            bean.setNOMBRES(jsonObject.getString("NOMBRES"));
                            bean.setAPELLIDO_PATERNO(jsonObject.getString("APELLIDO_PATERNO"));
                            bean.setAPELLIDO_MATERNO(jsonObject.getString("APELLIDO_MATERNO"));
                            bean.setID_EMPRESA(jsonObject.getString("ID_EMPRESA"));
                            bean.setGENERO(jsonObject.getString("GENERO"));
                            bean.setCORREO(jsonObject.getString("CORREO"));
                            bean.setFECHA_NACIMIENTO(jsonObject.getString("FECHA_NACIMIENTO"));
                            bean.setNOMBRES_CONTACTO(jsonObject.getString("NOMBRES_CONTACTO"));
                            bean.setDIRECCION_CONTACTO(jsonObject.getString("DIRECCION_CONTACTO"));
                            bean.setTELEFONO_CONTACTO(jsonObject.getString("TELEFONO_CONTACTO"));
                            bean.setCORREO_CONTACTO(jsonObject.getString("CORREO_CONTACTO"));
                            bean.setUSUARIO(jsonObject.getString("USUARIO"));
                            bean.setCONTRASENA(jsonObject.getString("CONTRASENA"));
                            bean.setID_ROL(jsonObject.getString("ID_ROL"));
                            bean.setFEC_CREACION(jsonObject.getString("FEC_CREACION"));
                            bean.setFEC_ACTUALIZACION(jsonObject.getString("FEC_ACTUALIZACION"));
                            bean.setFEC_ELIMINACION(jsonObject.getString("FEC_ELIMINACION"));
                            dbUsuario.insertar(bean);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error en el registro json EMPLEADO: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fallo el servicio de EMPLEADO: " + error.getMessage().toString() , Toast.LENGTH_LONG).show();
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

    public void WebServiceEmpleado(){
        dbUsuario = new DatabaseManagerUsuario(context);
        String descripcion = "XXX";
        String ACCION = "SELECT_POR_EMPRESA";
        String Usuario = "";
        String Password = "";
        String IdEmpresa = session.getIdEmpresa();
        final String QUERY = "call SP_USUARIO('" + ACCION  + "',"+ IdEmpresa +",'" + Usuario + "','" + Password + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dbUsuario.eliminarTodo();
                try {
                    if (response.equals("[]") || response.equals("")){
                        //Toast.makeText(context, "No se encontraron datos SP_EMPLEADO", Toast.LENGTH_LONG).show();
                    }else{
                        UsuarioBean bean  = null;
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            bean = new UsuarioBean();
                            bean.setID(jsonObject.getString("ID"));
                            bean.setID_TIPO_DOCUMENTO(jsonObject.getString("ID_TIPO_DOCUMENTO"));
                            bean.setNUM_DOCUMENTO(jsonObject.getString("NUM_DOCUMENTO"));
                            bean.setCOD_PAIS(jsonObject.getString("COD_PAIS"));
                            bean.setNOMBRES(jsonObject.getString("NOMBRES"));
                            bean.setAPELLIDO_PATERNO(jsonObject.getString("APELLIDO_PATERNO"));
                            bean.setAPELLIDO_MATERNO(jsonObject.getString("APELLIDO_MATERNO"));
                            bean.setID_EMPRESA(jsonObject.getString("ID_EMPRESA"));
                            bean.setGENERO(jsonObject.getString("GENERO"));
                            bean.setCORREO(jsonObject.getString("CORREO"));
                            bean.setFECHA_NACIMIENTO(jsonObject.getString("FECHA_NACIMIENTO"));
                            bean.setNOMBRES_CONTACTO(jsonObject.getString("NOMBRES_CONTACTO"));
                            bean.setDIRECCION_CONTACTO(jsonObject.getString("DIRECCION_CONTACTO"));
                            bean.setTELEFONO_CONTACTO(jsonObject.getString("TELEFONO_CONTACTO"));
                            bean.setCORREO_CONTACTO(jsonObject.getString("CORREO_CONTACTO"));
                            bean.setUSUARIO(jsonObject.getString("USUARIO"));
                            bean.setCONTRASENA(jsonObject.getString("CONTRASENA"));
                            bean.setID_ROL(jsonObject.getString("ID_ROL"));
                            bean.setFEC_CREACION(jsonObject.getString("FEC_CREACION"));
                            bean.setFEC_ACTUALIZACION(jsonObject.getString("FEC_ACTUALIZACION"));
                            bean.setFEC_ELIMINACION(jsonObject.getString("FEC_ELIMINACION"));
                            dbUsuario.insertar(bean);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error en el registro json EMPLEADO: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fallo el servicio de EMPLEADO: " + error.getMessage().toString() , Toast.LENGTH_LONG).show();
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

    public void WebServiceSede(){
        String ACCION = "SELECT_POR_EMPRESA";
        String IdEmpresa = session.getIdEmpresa();
        dbSede = new DatabaseManagerSede(context);
        final String QUERY = "call SP_SEDE('" + ACCION + "', " + IdEmpresa + ");";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dbSede.eliminarTodo();
                if (response.equals("[]") || response.equals("")){
                    //Toast.makeText(context, "No se encontraron datos SP_SEDE", Toast.LENGTH_LONG).show();
                }else{
                    try {
                        SedeBean bean = null;
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            bean = new SedeBean();
                            bean.setID(jsonObject.getString("ID"));
                            bean.setNOMBRE_SEDE(jsonObject.getString("NOMBRE_SEDE"));
                            bean.setID_EMPRESA(jsonObject.getString("ID_EMPRESA"));
                            bean.setDIRECCION(jsonObject.getString("DIRECCION"));
                            bean.setID_DISTRITO(jsonObject.getString("ID_DISTRITO"));
                            bean.setLATITUD(jsonObject.getString("LATITUD"));
                            bean.setLONGITUD(jsonObject.getString("LONGITUD"));
                            bean.setFEC_CREACION(jsonObject.getString("FEC_CREACION"));
                            bean.setFEC_ACTUALIZACION(jsonObject.getString("FEC_ACTUALIZACION"));
                            bean.setFEC_ELIMINACION(jsonObject.getString("FEC_ELIMINACION"));
                            dbSede.insertar(bean);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Error en el registro json SEDE: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fallo el servicio de SEDE: " + error.getMessage().toString() , Toast.LENGTH_LONG).show();
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


    public void WebServiceUsuarioSede(){
        String IdEmpresa = session.getIdEmpresa();
        dbUsuarioSede = new DatabaseManagerUsuarioSede(context);
        final String QUERY = "call SP_USUARIO_SEDE('" + ACCION + "_POR_EMPRESA',0," + IdEmpresa + ");";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dbUsuarioSede.eliminarTodo();
                if (response.equals("[]") || response.equals("")){
                    //Toast.makeText(context, "No se encontraron datos SP_USUARIO_SEDE", Toast.LENGTH_LONG).show();
                }else {
                    try {
                        UsuarioSedeBean bean = null;
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            bean = new UsuarioSedeBean();
                            bean.setID_USUARIO(jsonObject.getString("ID_USUARIO"));
                            bean.setID_SEDE(jsonObject.getString("ID_SEDE"));
                            bean.setID_ROL(jsonObject.getString("ID_ROL"));
                            dbUsuarioSede.insertar(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error servicio SP_USUARIO_SEDE: " + error.getMessage(), Toast.LENGTH_LONG).show();
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





}
