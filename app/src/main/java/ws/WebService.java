package ws;

import android.content.Context;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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

public class WebService  {

    public static final String SERVER = "https://bioficha.electocandidato.com/";
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
    DatabaseManagerPais dbPais;

    RequestQueue requestQueue;
    JSONArray jsonArray;

    Context context;
    public WebService(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void WebServicePais(){
        dbPais = new DatabaseManagerPais(context);
        final String QUERY = "call SP_PAIS('" + ACCION + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]") || response.equals("") || response.isEmpty()){
                    Toast.makeText(context, "No se encontraron datos WebServicePais", Toast.LENGTH_LONG).show();
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
                            String encodedWithISO88591 = jsonObject.getString("NOMBRE");
                            String decodedToUTF8 = "";

                            try{
                                decodedToUTF8 = new String(encodedWithISO88591.getBytes("ISO-8859-1"), "UTF-8");
                            }
                            catch(UnsupportedEncodingException e){
                                e.printStackTrace();
                            }
                            bean.setNOMBRE(decodedToUTF8);
                            bean.setORDEN(jsonObject.getString("ORDEN"));
                            dbPais.insertar(bean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error servicio WebServicePais: " + error.getMessage(), Toast.LENGTH_LONG).show();
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

    public void WebServiceEnfermedad(){
        dbEnfermedad = new DatabaseManagerEnfermedad(context);
        String descripcion = "XXX";
        final String QUERY = "call SP_ENFERMEDAD('" + ACCION + "','" + descripcion + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]") || response.equals("")){
                    Toast.makeText(context, "No se encontraron datos WebServiceEnfermedad", Toast.LENGTH_LONG).show();
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
                            String encodedWithISO88591 = jsonObject.getString("DESCRIPCION");
                            String decodedToUTF8 = "";

                            try{
                                decodedToUTF8 = new String(encodedWithISO88591.getBytes("ISO-8859-1"), "UTF-8");
                            }
                            catch(UnsupportedEncodingException e){
                                e.printStackTrace();
                            }
                            bean.setDESCRIPCION(decodedToUTF8);
                            dbEnfermedad.insertar(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERROR WebServiceEnfermedad " + error.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void WebServiceRol(){
        dbRol = new DatabaseManagerRol(context);
        String descripcion = "XXX";
        final String QUERY = "call SP_ROL('" + ACCION + "','" + descripcion + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]") || response.equals("")){
                    Toast.makeText(context, "No se encontraron datos WebServiceRol", Toast.LENGTH_LONG).show();
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERROR WebServiceRol " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void WebServiceSintoma(){
        dbSintoma = new DatabaseManagerSintoma(context);
        String descripcion = "XXX";
        final String QUERY = "call SP_SINTOMA('" + ACCION + "','" + descripcion + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]") || response.equals("")){
                    Toast.makeText(context, "No se encontraron datos WebServiceSintoma", Toast.LENGTH_LONG).show();
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
                            String encodedWithISO88591 = jsonObject.getString("DESCRIPCION");
                            String decodedToUTF8 = "";

                            try{
                                decodedToUTF8 = new String(encodedWithISO88591.getBytes("ISO-8859-1"), "UTF-8");
                            }
                            catch(UnsupportedEncodingException e){
                                e.printStackTrace();
                            }
                            bean.setDESCRIPCION(decodedToUTF8);
                            dbSintoma.insertar(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERROR WebServiceSintoma " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String encodedWithISO88591 = jsonObject.getString("nombre_departamento");
                            String decodedToUTF8 = "";

                            try{
                                decodedToUTF8 = new String(encodedWithISO88591.getBytes("ISO-8859-1"), "UTF-8");
                            }
                            catch(UnsupportedEncodingException e){
                                e.printStackTrace();
                            }
                            bean.setNOM_DEPARTAMENTO(decodedToUTF8);
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
                            String encodedWithISO88591 = jsonObject.getString("nombre_provincia");
                            String decodedToUTF8 = "";

                            try{
                                decodedToUTF8 = new String(encodedWithISO88591.getBytes("ISO-8859-1"), "UTF-8");
                            }
                            catch(UnsupportedEncodingException e){
                                e.printStackTrace();
                            }
                            bean.setNOM_PROVINCIA(decodedToUTF8);
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
                            String encodedWithISO88591 = jsonObject.getString("nombre_distrito");
                            String decodedToUTF8 = "";

                            try{
                                decodedToUTF8 = new String(encodedWithISO88591.getBytes("ISO-8859-1"), "UTF-8");
                            }
                            catch(UnsupportedEncodingException e){
                                e.printStackTrace();
                            }
                            bean.setNOM_DISTRITO(decodedToUTF8);
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
        final String QUERY = "call SP_TIPO_DOCUMENTO('" + ACCION + "');";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]") || response.equals("")){
                    Toast.makeText(context, "No se encontraron datos WebServiceTipoDocumento", Toast.LENGTH_LONG).show();
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
                            String encodedWithISO88591 = jsonObject.getString("NOM_DOCUMENTO");
                            String decodedToUTF8 = "";

                            try{
                                decodedToUTF8 = new String(encodedWithISO88591.getBytes("ISO-8859-1"), "UTF-8");
                            }
                            catch(UnsupportedEncodingException e){
                                e.printStackTrace();
                            }
                            bean.setNOM_DOCUMENTO(decodedToUTF8);
                            dbTipoDocumento.insertar(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERROR WebServiceTipoDocumento " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        QUERY = "call SP_USUARIO('" + ACCION  + "',0,'','',0,'');";
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
                        bean.setCOD_PAIS(jsonObject.getString("COD_PAIS"));
                        bean.setNOMBRES(jsonObject.getString("NOMBRES"));
                        bean.setAPELLIDO_PATERNO(jsonObject.getString("APELLIDO_PATERNO"));
                        bean.setAPELLIDO_MATERNO(jsonObject.getString("APELLIDO_MATERNO"));
                        bean.setID_EMPRESA(jsonObject.getString("ID_EMPRESA"));
                        bean.setGENERO(jsonObject.getString("GENERO"));
                        bean.setCORREO(jsonObject.getString("CORREO"));
                        bean.setESTATURA(jsonObject.getString("ESTATURA"));
                        bean.setPESO(jsonObject.getString("PESO"));
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
        QUERY = "call SP_SEDE_POLIGONO('" + ACCION  + "',0);";
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
                        bean.setLATITUD(jsonObject.getDouble("LATITUD"));
                        bean.setLONGITUD(jsonObject.getDouble("LONGITUD"));
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
