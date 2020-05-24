package ws;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import beans.DepartamentoBean;
import beans.DistritoBean;
import beans.EmpresaBean;
import beans.EnfermedadBean;
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
import db.DatabaseManagerProvincia;
import db.DatabaseManagerRol;
import db.DatabaseManagerSede;
import db.DatabaseManagerSedePoligono;
import db.DatabaseManagerSintoma;
import db.DatabaseManagerTipoDocumento;
import db.DatabaseManagerUsuario;
import db.DatabaseManagerUsuarioSede;

public class WebService  {

    public static final String SERVER = "http://bioficha.electocandidato.com/";
    public String URL = SERVER + "select.php";
    public String RESPUESTA = "NADA";
    public String ACCION = "SELECT";
    public String QUERY = "";
    public String TABLA = "";

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

    RequestQueue requestQueue;
    JSONArray jsonArray;

    Context context;
    public WebService(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void WebService(){
        dbEnfermedad = new DatabaseManagerEnfermedad(context);
        if (!(dbEnfermedad.verificarRegistros())) {
            String descripcion = "XXX";
            QUERY = "call SP_ENFERMEDAD('" + ACCION + "','" + descripcion + "');";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
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
                        WebServiceRol();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    WebServiceRol();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RESPUESTA = error.toString();
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
                    WebServiceSintoma();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RESPUESTA = error.toString();
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
                    dbSintoma.eliminarTodo();
                    try {
                        SintomaBean bean = null;
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            bean = new SintomaBean();
                            bean.setID(jsonObject.getString("ID"));
                            bean.setDESCRIPCION(jsonObject.getString("DESCRIPCION"));
                            dbSintoma.insertar(bean);
                        }
                        WebServiceDepartamento();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RESPUESTA = error.toString();
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
                        WebServiceProvincia();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RESPUESTA = error.toString();
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
                        WebServiceDistrito();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RESPUESTA = error.toString();
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
                        WebServiceTipoDocumento();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RESPUESTA = error.toString();
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
                        WebServiceEmpresa();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RESPUESTA = error.toString();
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
                        WebServiceSede();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RESPUESTA = error.toString();
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
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RESPUESTA = error.toString();
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
        }
    }

    public void WebServiceUsuario(){
        dbUsuario = new DatabaseManagerUsuario(context);
        String descripcion = "XXX";
        QUERY = "call SP_USUARIO('" + ACCION  + "','" + descripcion + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dbUsuario.eliminarTodo();
                try {
                    UsuarioBean bean  = null;
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        bean = new UsuarioBean();
                        bean.setID(jsonObject.getString("ID"));
                        bean.setID_TIPO_DOCUMENTO(jsonObject.getString("ID_TIPO_DOCUMENTO"));
                        bean.setNUM_DOCUMENTO(jsonObject.getString("NUM_DOCUMENTO"));
                        bean.setNACIONALIDAD(jsonObject.getString("NACIONALIDAD"));
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RESPUESTA = error.toString();
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

    public void WebServiceUsuarioSede(final Context context){
        dbUsuarioSede = new DatabaseManagerUsuarioSede(context);
        String descripcion = "XXX";
        QUERY = "call SP_USUARIO_SEDE('" + ACCION  + "','" + descripcion + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dbUsuarioSede.eliminarTodo();
                try {
                    UsuarioSedeBean bean  = null;
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        bean  = new UsuarioSedeBean();
                        bean.setID_USUARIO(jsonObject.getString("ID_USUARIO"));
                        bean.setID_SEDE(jsonObject.getString("ID_SEDE"));
                        bean.setID_ROL(jsonObject.getString("ID_ROL"));
                        dbUsuarioSede.insertar(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RESPUESTA = error.toString();
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

    public void WebServiceSedePoligono(final Context context){
        dbSedePoligono = new DatabaseManagerSedePoligono(context);
        QUERY = "call SP_SEDE_POLIGONO('" + ACCION  + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dbSedePoligono.eliminarTodo();
                try {
                    SedePoligonoBean bean  = null;
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        bean  = new SedePoligonoBean();
                        bean.setID(jsonObject.getString("ID"));
                        bean.setLATITUD(jsonObject.getString("LATITUD"));
                        bean.setLONGITUD(jsonObject.getString("LONGITUD"));
                        bean.setID_SEDE(jsonObject.getString("ID_SEDE"));
                        dbSedePoligono.insertar(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RESPUESTA = error.toString();
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

}
