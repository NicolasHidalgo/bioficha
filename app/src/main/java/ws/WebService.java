package ws;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import beans.DepartamentoBean;
import beans.DistritoBean;
import beans.EmpresaBean;
import beans.EnfermedadBean;
import beans.ProvinciaBean;
import beans.RolBean;
import beans.SedeBean;
import beans.SedePoligonoBean;
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
    public String URL = "";
    public String RESPUESTA = "NADA";
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
    JsonArrayRequest jsonArrayRequest;
    JSONArray jsonArray;

    public String WebService(final Context context, String urlAccion, String param, String tabla){
        URL = SERVER + urlAccion;
        QUERY = param;
        TABLA = tabla;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                RESPUESTA = response.toString();
                if (TABLA == "ENFERMEDAD")
                    SincronizarEnfermedad(context,response);
                if (TABLA == "SINTOMA")
                    SincronizarSintoma(context,response);
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
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return RESPUESTA;
    }

    public void SincronizarEnfermedad(Context context, String response){
        dbEnfermedad = new DatabaseManagerEnfermedad(context);
        dbEnfermedad.eliminarTodo();
        try {
            EnfermedadBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                bean  = new EnfermedadBean();
                bean.setID(jsonObject.getString("ID"));
                bean.setDESCRIPCION(jsonObject.getString("DESCRIPCION"));
                dbEnfermedad.insertar(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SincronizarSintoma(Context context, String response){
        dbSintoma = new DatabaseManagerSintoma(context);
        dbSintoma.eliminarTodo();
        try {
            EnfermedadBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                bean  = new EnfermedadBean();
                bean.setID(jsonObject.getString("ID"));
                bean.setDESCRIPCION(jsonObject.getString("DESCRIPCION"));
                dbSintoma.insertar(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SincronizarDepartamento(Context context, String response){
        dbDepartamento = new DatabaseManagerDepartamento(context);
        dbDepartamento.eliminarTodo();
        try {
            DepartamentoBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                bean  = new DepartamentoBean();
                bean.setID(jsonObject.getString("iddepartamento"));
                bean.setNOM_DEPARTAMENTO(jsonObject.getString("nombre_departamento"));
                dbDepartamento.insertar(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SincronizarProvincia(Context context, String response){
        dbProvincia = new DatabaseManagerProvincia(context);
        dbProvincia.eliminarTodo();
        try {
            ProvinciaBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                bean  = new ProvinciaBean();
                bean.setID(jsonObject.getString("idprovincia"));
                bean.setID_DEPARTAMENTO(jsonObject.getString("iddepartamento"));
                bean.setNOM_PROVINCIA(jsonObject.getString("nombre_provincia"));
                dbProvincia.insertar(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SincronizarDistrito(Context context, String response){
        dbDistrito = new DatabaseManagerDistrito(context);
        dbDistrito.eliminarTodo();
        try {
            DistritoBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
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

    public void SincronizarRol(Context context, String response){
        dbRol = new DatabaseManagerRol(context);
        dbRol.eliminarTodo();
        try {
            RolBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                bean  = new RolBean();
                bean.setID(jsonObject.getString("ID"));
                bean.setNOM_ROL(jsonObject.getString("NOM_ROL"));
                dbRol.insertar(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SincronizarTipoDocumento(Context context, String response){
        dbTipoDocumento = new DatabaseManagerTipoDocumento(context);
        dbTipoDocumento.eliminarTodo();
        try {
            TipoDocumentoBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                bean  = new TipoDocumentoBean();
                bean.setID(jsonObject.getString("ID"));
                bean.setNOM_DOCUMENTO(jsonObject.getString("NOM_DOCUMENTO"));
                dbTipoDocumento.insertar(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SincronizarEmpresa(Context context, String response){
        dbEmpresa = new DatabaseManagerEmpresa(context);
        dbEmpresa.eliminarTodo();
        try {
            EmpresaBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
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

    public void SincronizarSede(Context context, String response){
        dbSede = new DatabaseManagerSede(context);
        dbSede.eliminarTodo();
        try {
            SedeBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
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

    public void SincronizarUsuario(Context context, String response){
        dbUsuario = new DatabaseManagerUsuario(context);
        dbUsuario.eliminarTodo();
        try {
            UsuarioBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
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

    public void SincronizarUsuarioSede(Context context, String response){
        dbUsuarioSede = new DatabaseManagerUsuarioSede(context);
        dbUsuarioSede.eliminarTodo();
        try {
            UsuarioSedeBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
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

    public void SincronizarSedePoligono(Context context, String response){
        dbSedePoligono = new DatabaseManagerSedePoligono(context);
        dbSedePoligono.eliminarTodo();
        try {
            SedePoligonoBean bean  = null;
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
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

}
