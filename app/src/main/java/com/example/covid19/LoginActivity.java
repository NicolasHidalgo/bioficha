package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import beans.DepartamentoBean;
import beans.DistritoBean;
import beans.EmpresaBean;
import beans.EnfermedadBean;
import beans.PaisBean;
import beans.ProvinciaBean;
import beans.RolBean;
import beans.SedeBean;
import beans.SedePoligonoBean;
import beans.SintomaBean;
import beans.TipoDocumentoBean;
import beans.UsuarioBean;
import beans.UsuarioSedeBean;
import db.DatabaseManagerDepartamento;
import db.DatabaseManagerDistrito;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerEnfermedad;
import db.DatabaseManagerPais;
import db.DatabaseManagerProvincia;
import db.DatabaseManagerRol;
import db.DatabaseManagerSede;
import db.DatabaseManagerSedePoligono;
import db.DatabaseManagerSintoma;
import db.DatabaseManagerTipoDocumento;
import db.DatabaseManagerUsuario;
import db.DatabaseManagerUsuarioSede;
import helper.ConnectivityReceiver;
import helper.Session;
import ws.WebService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnIngresar;
    EditText txtUsuario, txtPassword;
    private Session session;

    public static final String SERVER = "https://bioficha.electocandidato.com/";
    public String URL = SERVER + "select.php";
    public String RESPUESTA = "NADA";
    public String ACCION = "SELECT";
    public String QUERY = "";
    public String TABLA = "";
    Context context;

    DatabaseManagerEnfermedad dbEnfermedad;
    DatabaseManagerSintoma dbSintoma;
    DatabaseManagerDepartamento dbDepartamento;
    DatabaseManagerProvincia dbProvincia;
    DatabaseManagerDistrito dbDistrito;
    DatabaseManagerRol dbRol;
    DatabaseManagerTipoDocumento dbTipoDocumento;
    DatabaseManagerEmpresa dbEmpresa;
    DatabaseManagerSede dbSede;
    DatabaseManagerUsuario dbUsuario;
    DatabaseManagerUsuarioSede dbUsuarioSede;
    DatabaseManagerSedePoligono dbSedePoligono;
    DatabaseManagerPais dbPais;

    RequestQueue requestQueue;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new Session(this);
        context = this;
        requestQueue = Volley.newRequestQueue(this);

        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void CloseProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    public void OpenProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

    public void btnIngresar(View view){

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        final String Usuario = txtUsuario.getText().toString();
        final String Password = txtPassword.getText().toString();

        if (Usuario.isEmpty()){
            Toast.makeText(this, "Debe ingresar Usuario y Clave", Toast.LENGTH_LONG).show();
            return;
        }

        if (Password.isEmpty()){
            Toast.makeText(this, "Debe ingresar Usuario y Clave", Toast.LENGTH_LONG).show();
            return;
        }

        if(!(ConnectivityReceiver.isConnected(context))){
            Toast.makeText(this, "Necesitas contectarte a internet para continuar", Toast.LENGTH_LONG).show();
            return;
        }


        OpenProgressBar();
        dbUsuario = new DatabaseManagerUsuario(context);
        String descripcion = "XXX";
        String ACCION = "LOGIN";
        final String QUERY = "call SP_USUARIO('" + ACCION  + "','" + Usuario + "','" + Password + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("[]")){
                        CloseProgressBar();
                        Toast.makeText(context, "Usuario y Password invalidos", Toast.LENGTH_LONG).show();
                    }else if (response.equals("")){
                        CloseProgressBar();
                        Toast.makeText(context, "No se encontraron datos SP_USUARIO", Toast.LENGTH_LONG).show();
                    }else{
                        dbUsuario.eliminarPorUsuarioPassword(Usuario,Password);
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
                            bean.setCONTRASENA(jsonObject.getString("CONTRASENA"));
                            bean.setID_ROL(jsonObject.getString("ID_ROL"));
                            bean.setFEC_ACTUALIZACION(jsonObject.getString("FEC_ACTUALIZACION"));
                            bean.setFEC_ELIMINACION(jsonObject.getString("FEC_ELIMINACION"));
                            dbUsuario.insertar(bean);
                        }
                        session.setIdUsuario(bean.getID());
                        session.setIdEmpresa("1"); // EN DURO, DEBE SER DINAMICO
                        WebService(bean.getID());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CloseProgressBar();
                Toast.makeText(getApplicationContext(), "Fallo el servicio de Login SP_USUARIO: " + error.getMessage().toString() , Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("consulta",QUERY);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);

    }

    // UsuarioSede
    public void WebService(String IdUsuario){
        dbUsuarioSede = new DatabaseManagerUsuarioSede(context);
        if (!(dbUsuarioSede.verificarRegistros())) {
            QUERY = "call SP_USUARIO_SEDE('" + ACCION + "_BY_USUARIO'," + IdUsuario + ");";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]")){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_USUARIO_SEDE", Toast.LENGTH_LONG).show();
                    }else if (response.equals("")){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_USUARIO_SEDE", Toast.LENGTH_LONG).show();
                    }else {
                        dbUsuarioSede.eliminarTodo();
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
                    WebServiceEnfermedad();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
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
            requestQueue.add(stringRequest);
        }else{
            WebServiceEnfermedad();
        }
    }


    public void WebServiceEnfermedad(){
        dbEnfermedad = new DatabaseManagerEnfermedad(context);
        if (!(dbEnfermedad.verificarRegistros())) {
            String descripcion = "XXX";
            QUERY = "call SP_ENFERMEDAD('" + ACCION + "','" + descripcion + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_USUARIO_SEDE", Toast.LENGTH_LONG).show();
                    }else{
                        dbEnfermedad.eliminarTodo();
                        try {
                            EnfermedadBean bean = null;
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                bean = new EnfermedadBean();
                                bean.setID(jsonObject.getString("ID"));
                                bean.setDESCRIPCION(jsonObject.getString("DESCRIPCION"));
                                dbEnfermedad.insertar(bean);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WebServiceRol();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_ENFERMEDAD: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else {
            WebServiceRol();
        }
    }


    public void WebServiceRol(){
        dbRol = new DatabaseManagerRol(context);
        if (!(dbRol.verificarRegistros())) {
            String descripcion = "XXX";
            QUERY = "call SP_ROL('" + ACCION + "','" + descripcion + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_ROL", Toast.LENGTH_LONG).show();
                    }else{
                        dbRol.eliminarTodo();
                        try {
                            RolBean bean = null;
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                bean = new RolBean();
                                bean.setID(jsonObject.getString("ID"));
                                bean.setNOM_ROL(jsonObject.getString("NOM_ROL"));
                                dbRol.insertar(bean);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WebServiceSintoma();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_ROL: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            WebServiceSintoma();
        }
    }

    public void WebServiceSintoma(){
        dbSintoma = new DatabaseManagerSintoma(context);
        if (!(dbSintoma.verificarRegistros())) {
            String descripcion = "XXX";
            QUERY = "call SP_SINTOMA('" + ACCION + "','" + descripcion + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_SINTOMA", Toast.LENGTH_LONG).show();
                    }else{
                        dbSintoma.eliminarTodo();
                        try {
                            SintomaBean bean = null;
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                bean = new SintomaBean();
                                bean.setID(jsonObject.getString("ID"));
                                String descr = jsonObject.getString("DESCRIPCION");
                                bean.setDESCRIPCION(descr);
                                dbSintoma.insertar(bean);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WebServiceDepartamento();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_SINTOMA: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            WebServiceDepartamento();
        }
    }

    public void WebServiceDepartamento(){
        dbDepartamento = new DatabaseManagerDepartamento(context);
        if (!(dbDepartamento.verificarRegistros())) {
            QUERY = "call SP_UBIGEO('" + ACCION + "_DEPARTAMENTO" + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_UBIGEO DEPARTAMENTO", Toast.LENGTH_LONG).show();
                    }else{
                        dbDepartamento.eliminarTodo();
                        try {
                            DepartamentoBean bean = null;
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                bean = new DepartamentoBean();
                                bean.setID(jsonObject.getString("iddepartamento"));
                                bean.setNOM_DEPARTAMENTO(jsonObject.getString("nombre_departamento"));
                                dbDepartamento.insertar(bean);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WebServiceProvincia();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_UBIGEO DEPARTAMENTO: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            WebServiceProvincia();
        }
    }

    public void WebServiceProvincia(){
        dbProvincia = new DatabaseManagerProvincia(context);
        if (!(dbProvincia.verificarRegistros())) {
            QUERY = "call SP_UBIGEO('" + ACCION + "_PROVINCIA" + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_UBIGEO PROVINCIA", Toast.LENGTH_LONG).show();
                    }else{
                        dbProvincia.eliminarTodo();
                        try {
                            ProvinciaBean bean = null;
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                bean = new ProvinciaBean();
                                bean.setID(jsonObject.getString("idprovincia"));
                                bean.setID_DEPARTAMENTO(jsonObject.getString("iddepartamento"));
                                bean.setNOM_PROVINCIA(jsonObject.getString("nombre_provincia"));
                                dbProvincia.insertar(bean);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WebServiceDistrito();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_UBIGEO PROVINCIA: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            WebServiceDistrito();
        }
    }

    public void WebServiceDistrito(){
        dbDistrito = new DatabaseManagerDistrito(context);
        if (!(dbDistrito.verificarRegistros())){
            QUERY = "call SP_UBIGEO('" + ACCION + "_DISTRITO" + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_UBIGEO DISTRITO", Toast.LENGTH_LONG).show();
                    }else{
                        dbDistrito.eliminarTodo();
                        try {
                            DistritoBean bean  = null;
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                bean  = new DistritoBean();
                                bean.setID(jsonObject.getString("iddistrito"));
                                bean.setID_PROVINCIA(jsonObject.getString("idprovincia"));
                                bean.setNOM_DISTRITO(jsonObject.getString("nombre_distrito"));
                                dbDistrito.insertar(bean);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WebServiceTipoDocumento();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_UBIGEO DISTRITO: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("consulta",QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            WebServiceTipoDocumento();
        }

    }


    public void WebServiceTipoDocumento(){
        dbTipoDocumento = new DatabaseManagerTipoDocumento(context);
        if (!(dbTipoDocumento.verificarRegistros())) {
            QUERY = "call SP_TIPO_DOCUMENTO('" + ACCION + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_TIPO_DOCUMENTO", Toast.LENGTH_LONG).show();
                    }else{
                        dbTipoDocumento.eliminarTodo();
                        try {
                            TipoDocumentoBean bean = null;
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                bean = new TipoDocumentoBean();
                                bean.setID(jsonObject.getString("ID"));
                                bean.setNOM_DOCUMENTO(jsonObject.getString("NOM_DOCUMENTO"));
                                dbTipoDocumento.insertar(bean);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WebServiceEmpresa();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_TIPO_DOCUMENTO: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            WebServiceEmpresa();
        }
    }

    public void WebServiceEmpresa(){
        dbEmpresa = new DatabaseManagerEmpresa(context);
        if (!(dbEmpresa.verificarRegistros())) {
            String descripcion = "XXX";
            QUERY = "call SP_EMPRESA('" + ACCION + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_EMPRESA", Toast.LENGTH_LONG).show();
                    }else{
                        dbEmpresa.eliminarTodo();
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
                            e.printStackTrace();
                        }
                    }
                    WebServiceSede();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_EMPRESA: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else {
            WebServiceSede();
        }
    }

    public void WebServiceSede(){
        dbSede = new DatabaseManagerSede(context);
        if (!(dbSede.verificarRegistros())) {
            QUERY = "call SP_SEDE('" + ACCION + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_SEDE", Toast.LENGTH_LONG).show();
                    }else{
                        dbSede.eliminarTodo();
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
                            e.printStackTrace();
                        }
                    }
                    WebServicePais();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_SEDE: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            WebServicePais();
        }
    }

    public void WebServicePais(){
        dbPais = new DatabaseManagerPais(context);
        if (!(dbPais.verificarRegistros())) {
            QUERY = "call SP_PAIS('" + ACCION + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_PAIS", Toast.LENGTH_LONG).show();
                    }else{
                        dbPais.eliminarTodo();
                        try {
                            PaisBean bean = null;
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                bean = new PaisBean();
                                bean.setCOD(jsonObject.getString("COD"));
                                bean.setNOMBRE(jsonObject.getString("NOMBRE"));
                                bean.setORDEN(jsonObject.getString("ORDEN"));
                                dbPais.insertar(bean);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WebServiceSedePoligono();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_PAIS: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            WebServiceSedePoligono();
        }
    }

    public void WebServiceSedePoligono(){
        dbSedePoligono = new DatabaseManagerSedePoligono(context);
        if (!(dbSedePoligono.verificarRegistros())) {
            QUERY = "call SP_SEDE_POLIGONO('" + ACCION + "',0);";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("[]") || response.equals("") || response.isEmpty()){
                        //CloseProgressBar();
                        //Toast.makeText(context, "No se encontraron datos SP_SEDE_POLIGONO", Toast.LENGTH_LONG).show();
                    }else{
                        dbSedePoligono.eliminarTodo();
                        try {
                            SedePoligonoBean bean = null;
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                bean = new SedePoligonoBean();
                                bean.setID(jsonObject.getString("ID"));
                                bean.setLATITUD(jsonObject.getDouble("LATITUD"));
                                bean.setLONGITUD(jsonObject.getDouble("LONGITUD"));
                                bean.setID_SEDE(jsonObject.getString("ID_SEDE"));
                                dbSedePoligono.insertar(bean);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    CloseProgressBar();
                    Intent k = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(k);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CloseProgressBar();
                    Toast.makeText(context, "Error servicio SP_SEDE_POLIGONO: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("consulta", QUERY);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            CloseProgressBar();
            Intent k = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(k);
        }
    }

}
