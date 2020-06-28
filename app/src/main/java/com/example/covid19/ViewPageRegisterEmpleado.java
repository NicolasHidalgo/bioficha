package com.example.covid19;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import beans.BioFichaBean;
import beans.BioFichaEnfermedadBean;
import beans.EmpresaBean;
import beans.PaisBean;
import beans.RolBean;
import beans.SedeBean;
import beans.SintomaBean;
import beans.TipoDocumentoBean;
import beans.UsuarioBean;
import beans.UsuarioSedeBean;
import db.DatabaseManagerBioFicha;
import db.DatabaseManagerBioFichaEnfermedad;
import db.DatabaseManagerBioFichaSintoma;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerEnfermedad;
import db.DatabaseManagerPais;
import db.DatabaseManagerRol;
import db.DatabaseManagerSede;
import db.DatabaseManagerSedePoligono;
import db.DatabaseManagerSintoma;
import db.DatabaseManagerTipoDocumento;
import db.DatabaseManagerUsuario;
import db.DatabaseManagerUsuarioSede;
import helper.ConnectivityReceiver;
import helper.Session;
import util.Util;

public class ViewPageRegisterEmpleado extends AppCompatActivity {

    Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Session session;

    RequestQueue requestQueue;
    final String URL = "https://bioficha.electocandidato.com/insert_id.php";
    SedeBean sedeBean = null;

    DatabaseManagerTipoDocumento dbTipoDocumento;
    DatabaseManagerPais dbPais;
    DatabaseManagerUsuario dbUsuario;
    DatabaseManagerEmpresa dbEmpresa;
    DatabaseManagerRol dbRol;
    DatabaseManagerSede dbSede;
    DatabaseManagerUsuarioSede dbUsuarioSede;

    ProgressBar progressBar;
    Button btnGrabar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_register_empleado);
        context = this;
        session = new Session(context);

        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        final RegistroRegistradorActivity registroRegistradorActivity = new RegistroRegistradorActivity();
        viewPagerAdapter.AddFragment(registroRegistradorActivity, "Datos Generales");

        final AsignarSedeEmpleadoActivity asignarSedeEmpleadoActivity = new AsignarSedeEmpleadoActivity();
        if (!(session.getNomRol().equals("SUPER-ADMIN"))){
            viewPagerAdapter.AddFragment(asignarSedeEmpleadoActivity,"Sedes");
        }

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (!(session.getNomRol().equals("SUPER-ADMIN"))){
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_user_tap);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_sede_tap);
        }

        dbTipoDocumento = new DatabaseManagerTipoDocumento(context);
        dbPais = new DatabaseManagerPais(context);
        dbUsuario = new DatabaseManagerUsuario(context);
        dbEmpresa = new DatabaseManagerEmpresa(context);
        dbRol = new DatabaseManagerRol(context);
        dbSede = new DatabaseManagerSede(context);
        dbUsuarioSede = new DatabaseManagerUsuarioSede(context);

        btnGrabar = (Button) findViewById(R.id.btnGrabar);

        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(ConnectivityReceiver.isConnected(context))){
                    Toast.makeText(context, "Necesita contectarte a internet para continuar", Toast.LENGTH_LONG).show();
                    return;
                }

                String pAccion = "INSERT";
                String pId = "0";
                final String ide = session.getIdEmpleado();
                if(!(ide.isEmpty())){
                    pAccion = "UPDATE";
                    pId = ide;
                }

                final String pIdSede = session.getIdSede();

                final String pTipoDocumento = registroRegistradorActivity.spTipoDocumento.getSelectedItem().toString();
                if (pTipoDocumento.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar un tipo de documento", Toast.LENGTH_LONG).show();
                    return;
                }
                TipoDocumentoBean tipoDocumentoBean = dbTipoDocumento.getByName(pTipoDocumento);
                final String pIdTipoDocumento = tipoDocumentoBean.getID();

                final String pNumDocumento = registroRegistradorActivity.txtNumDocumento.getText().toString();
                if (pNumDocumento.isEmpty()){
                    Toast.makeText(context, "Debe ingresar un numero de documento", Toast.LENGTH_LONG).show();
                    return;
                }

                if(pTipoDocumento.equals("DNI")){
                    if(pNumDocumento.length() != 8){
                        Toast.makeText(context,"El numero de documento debe contener 8 digitos", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                final String pPais = registroRegistradorActivity.spPais.getSelectedItem().toString();
                if (pPais.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar un pais", Toast.LENGTH_LONG).show();
                    return;
                }
                PaisBean paisBean = dbPais.getByName(pPais);
                final String pCodPais = paisBean.getCOD();

                final String pNombres = registroRegistradorActivity.txtNombres.getText().toString();
                if (pNombres.isEmpty()){
                    Toast.makeText(context, "Debe ingresar el campo Nombres", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pApePaterno = registroRegistradorActivity.txtApePaterno.getText().toString();
                if (pApePaterno.isEmpty()){
                    Toast.makeText(context, "Debe ingresar el campo Apellido Paterno", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pApeMaterno = registroRegistradorActivity.txtApeMaterno.getText().toString();
                if (pApeMaterno.isEmpty()){
                    Toast.makeText(context, "Debe ingresar el campo Apellido Materno", Toast.LENGTH_LONG).show();
                    return;
                }

                String pRazonSocial = "";
                if(registroRegistradorActivity.spEmpresa != null && registroRegistradorActivity.spEmpresa.getSelectedItem() !=null ) {
                    pRazonSocial = registroRegistradorActivity.spEmpresa.getSelectedItem().toString();
                } else  {
                    Toast.makeText(context, "Debe crear una empresa antes de registrar un usuario.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (pTipoDocumento.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar un tipo de documento", Toast.LENGTH_LONG).show();
                    return;
                }
                EmpresaBean empresaBean = dbEmpresa.getByRazonSocial(pRazonSocial);
                final String pIdEmpresa = empresaBean.getID();

                final String pGenero = registroRegistradorActivity.spGenero.getSelectedItem().toString();
                if (pGenero.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar Genero", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pCorreo = registroRegistradorActivity.txtCorreo.getText().toString();
                if (pCorreo.isEmpty()){
                    Toast.makeText(context, "Debe ingresar un correo", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!(Util.isValidEmail(pCorreo))){
                    Toast.makeText(context, "Debe ingresar un correo valido", Toast.LENGTH_LONG).show();
                    return;
                }

                final String fechaNacimiento = registroRegistradorActivity.txtFechaNacimiento.getText().toString();
                if (fechaNacimiento.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar la fecha de nacimiento", Toast.LENGTH_LONG).show();
                    return;
                }
                String regex = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$";
                if(!fechaNacimiento.matches(regex)){
                    Toast.makeText(context, "Ingrese una fecha válida dd/mm/YYYY", Toast.LENGTH_LONG).show();
                    return;
                }

                String [] dateParts = fechaNacimiento.split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);

                int edad = Util.getAge(year,month,day);
                if (edad < 18){
                    Toast.makeText(context, "La persona debe ser mayor de edad (18).", Toast.LENGTH_LONG).show();
                    return;
                }

                String fecNac = "";
                try {
                    fecNac = Util.formatDate(fechaNacimiento,"dd/MM/yyyy","yyyy-MM-dd");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final String pFechaNacimiento = fecNac;

                Date currentTime = Calendar.getInstance().getTime();
                final String pFecha = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());

                String pNomRol = registroRegistradorActivity.spRol.getSelectedItem().toString();
                if (pNomRol.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar un tipo de documento", Toast.LENGTH_LONG).show();
                    return;
                }

                String username = "";
                String clave = "";
                if (pNomRol.equals("ADMIN") || pNomRol.equals("REGISTRADOR")){
                    username = registroRegistradorActivity.txtUsuario.getText().toString();
                    if (username.isEmpty()){
                        Toast.makeText(context, "Debe ingresar un nombre de usuario", Toast.LENGTH_LONG).show();
                        return;
                    }
                    clave = registroRegistradorActivity.txtClave.getText().toString();
                    if (clave.isEmpty()){
                        Toast.makeText(context, "Debe ingresar una clave", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                String estatura = registroRegistradorActivity.txtEstatura.getText().toString();
                if (estatura.isEmpty())
                    estatura = "0";

                final String pEstatura = estatura;

                String peso = registroRegistradorActivity.txtPeso.getText().toString();
                if (peso.isEmpty())
                    peso = "0";

                final String pPeso = peso;

                RolBean rolBean = dbRol.getByNombre(pNomRol);
                final String pIdRol = rolBean.getID();

                final String pNombresContacto = "";
                final String pDireccionContacto = "";
                final String pTelefonoContacto = "";
                final String pCorreoContacto = "";

                final String pUsuario = username;
                final String pContrasena = clave;

                OpenProgressBar();
                String Params = "";
                Params = Params + "'" + pAccion + "',";
                Params = Params + pId + ",";
                Params = Params + pIdTipoDocumento + ",";
                Params = Params + "'" + pNumDocumento + "',";
                Params = Params + "'" + pCodPais + "',";
                Params = Params + "'" + pNombres + "',";
                Params = Params + "'" + pApePaterno + "',";
                Params = Params + "'" + pApeMaterno + "',";
                Params = Params + pIdEmpresa + ",";
                Params = Params + "'" + pGenero + "',";
                Params = Params + "'" + pCorreo + "',";
                Params = Params + "'" + pFechaNacimiento + "',";
                Params = Params + pEstatura + ",";
                Params = Params + pPeso + ",";
                Params = Params + "'" + pNombresContacto + "',";
                Params = Params + "'" + pDireccionContacto + "',";
                Params = Params + "'" + pTelefonoContacto + "',";
                Params = Params + "'" + pCorreoContacto + "',";
                Params = Params + "'" + pUsuario + "',";
                Params = Params + "'" + pContrasena + "',";
                Params = Params + pIdRol;


                final String QUERY = "CALL SP_USUARIO_UPDATE(" + Params  + ");";
                final String finalAccion = pAccion;
                final String finalId = pId;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")){
                            CloseProgressBar();
                            Toast.makeText(context,"No se encontraron datos", Toast.LENGTH_LONG).show();
                        }else if (response.equals("")){
                            CloseProgressBar();
                            Toast.makeText(context,"Error en el servicio", Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = null;
                                String ID_FICHA = "";
                                String ID_USUARIO = "";
                                String MENSAJE = "";
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    ID_USUARIO = jsonObject.getString("ID_USUARIO");
                                    MENSAJE = jsonObject.getString("MENSAJE");

                                    if(ID_USUARIO.equals("0") || ID_USUARIO.isEmpty()){
                                        CloseProgressBar();
                                        Toast.makeText(context,MENSAJE,Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }

                                // INSERTAMOS LA FICHA EN EL SQLITE
                                UsuarioBean usuarioBean = new UsuarioBean();
                                usuarioBean.setID(ID_USUARIO);
                                usuarioBean.setID_TIPO_DOCUMENTO(pIdTipoDocumento);
                                usuarioBean.setNUM_DOCUMENTO(pNumDocumento);
                                usuarioBean.setCOD_PAIS(pCodPais);
                                usuarioBean.setNOMBRES(pNombres);
                                usuarioBean.setAPELLIDO_PATERNO(pApePaterno);
                                usuarioBean.setAPELLIDO_MATERNO(pApeMaterno);
                                usuarioBean.setID_EMPRESA(pIdEmpresa);
                                usuarioBean.setGENERO(pGenero);
                                usuarioBean.setESTATURA(pEstatura);
                                usuarioBean.setPESO(pPeso);
                                usuarioBean.setCORREO(pCorreo);
                                usuarioBean.setFECHA_NACIMIENTO(pFechaNacimiento);
                                usuarioBean.setNOMBRES_CONTACTO(pNombresContacto);
                                usuarioBean.setDIRECCION_CONTACTO(pDireccionContacto);
                                usuarioBean.setTELEFONO_CONTACTO(pTelefonoContacto);
                                usuarioBean.setCORREO_CONTACTO(pCorreoContacto);
                                usuarioBean.setUSUARIO(pUsuario);
                                usuarioBean.setCONTRASENA(pContrasena);
                                usuarioBean.setID_ROL(pIdRol);
                                usuarioBean.setFEC_CREACION(pFecha);
                                if(finalAccion.equals("UPDATE")){
                                    //usuarioBean = dbUsuario.getObject(finalId);
                                    usuarioBean.setFEC_ACTUALIZACION(pFecha);
                                    dbUsuario.actualizar(usuarioBean);
                                }else{
                                    dbUsuario.insertar(usuarioBean);
                                }

                                // REGISTRO DE SEDES, SOLO CUANDO SEA ADMIN
                                if(session.getNomRol().equals("ADMIN")){
                                    SparseBooleanArray checked = null;
                                    String SEDES = "";//"1"; // SET 1 "NINGUNO"
                                    checked = asignarSedeEmpleadoActivity.lvSedes.getCheckedItemPositions();
                                    if (checked != null){
                                        SedeBean sedeBean = null;
                                        String name = "";
                                        String ID_SEDE = "";
                                        for (int i = 0; i < checked.size() ; ++i) {
                                            if (checked.valueAt(i)) {
                                                int pos = checked.keyAt(i);
                                                name = asignarSedeEmpleadoActivity.lvSedes.getAdapter().getItem(pos).toString();
                                                sedeBean = dbSede.getByName(name);
                                                ID_SEDE = sedeBean.getID();
                                                if (SEDES.isEmpty())
                                                    SEDES = ID_SEDE;
                                                else
                                                    SEDES = SEDES + "," + ID_SEDE;
                                            }
                                        }
                                    }

                                    final String QUERY = "call SP_USUARIO_SEDE_UPDATE('INSERT'," + ID_USUARIO + "," + pIdRol + ",'" + SEDES + "');";
                                    final String SEDESK = SEDES;
                                    final String ID_USUARIOK = ID_USUARIO;
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                //JSONArray jsonArray = new JSONArray(response);
                                                // INSERTARMOS FICHA_SINTOMA EN EL SQLITE
                                                dbUsuarioSede.eliminar(ID_USUARIOK);
                                                List<String> lista = Arrays.asList(SEDESK.split(","));
                                                UsuarioSedeBean bean = null;
                                                for (int i = 0; i < lista.size() ; ++i) {
                                                    String ID = lista.get(i);
                                                    bean = new UsuarioSedeBean();
                                                    bean.setID_USUARIO(ID_USUARIOK);
                                                    bean.setID_SEDE(ID);
                                                    bean.setID_ROL(pIdRol);
                                                    dbUsuarioSede.insertar(bean);
                                                }
                                            } catch (Exception e) {
                                                CloseProgressBar();
                                                Toast.makeText(context,"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                            CloseProgressBar();
                                            if(finalAccion.equals("INSERT"))
                                                Toast.makeText(context,"Registro exitoso",Toast.LENGTH_LONG).show();
                                            else
                                                Toast.makeText(context,"Actualizacion exitosa",Toast.LENGTH_LONG).show();
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
                                    requestQueue.add(stringRequest);
                                }

                            } catch (Exception e) {
                                CloseProgressBar();
                                Toast.makeText(context,"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }

                        CloseProgressBar();
                        if(finalAccion.equals("INSERT"))
                            Toast.makeText(context,"Registro exitoso",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context,"Actualizacion exitosa",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CloseProgressBar();
                        Toast.makeText(context,"Error:" + error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
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
}
