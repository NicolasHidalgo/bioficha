package com.example.covid19;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.android.material.tabs.TabLayout;
import com.snatik.polygon.Point;
import com.snatik.polygon.Polygon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import beans.BioFichaBean;
import beans.BioFichaEnfermedadBean;
import beans.BioFichaSintomaBean;
import beans.EnfermedadBean;
import beans.PaisBean;
import beans.SedeBean;
import beans.SedePoligonoBean;
import beans.SintomaBean;
import beans.TipoDocumentoBean;
import beans.UsuarioBean;
import db.DatabaseManagerBioFicha;
import db.DatabaseManagerBioFichaEnfermedad;
import db.DatabaseManagerBioFichaSintoma;
import db.DatabaseManagerEnfermedad;
import db.DatabaseManagerPais;
import db.DatabaseManagerSede;
import db.DatabaseManagerSedePoligono;
import db.DatabaseManagerSintoma;
import db.DatabaseManagerTipoDocumento;
import db.DatabaseManagerUsuario;
import helper.ConnectivityReceiver;
import helper.Session;
import util.Util;

public class ViewPageRegister extends AppCompatActivity {

    Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  ViewPagerAdapter viewPagerAdapter;
    Button btnGrabar;
    private Session session;
    RequestQueue requestQueue;
    final String URL = "https://bioficha.electocandidato.com/insert_id.php";
    SedeBean sedeBean = null;

    DatabaseManagerBioFicha dbFicha;
    DatabaseManagerTipoDocumento dbTipoDocumento;
    DatabaseManagerSintoma dbSintoma;
    DatabaseManagerEnfermedad dbEnfermedad;
    DatabaseManagerBioFichaSintoma dbFichaSintoma;
    DatabaseManagerBioFichaEnfermedad dbFichaEnfermedad;
    DatabaseManagerPais dbPais;
    DatabaseManagerSedePoligono dbSedePoligono;
    DatabaseManagerSede dbSede;
    DatabaseManagerUsuario dbUsuario;

    ProgressBar progressBar;

    private LocationManager locationManager; // Para capturar GPS
    private LocationListener locationListener; // Para capturar GPS
    Double latitud = 0.0, longitud = 0.0, verLat = 0.0, verLon = 0.0;
    Polygon polygon;
    Point point;
    Polygon.Builder builder;
    boolean contains = false;
    int flag = 0;
    List<SedePoligonoBean> listaVertice;
    int cIdSede = 0;
    int cIdSedeAux = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_register);
        context = this;
        session = new Session(context);

        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);

        dbFicha = new DatabaseManagerBioFicha(context);
        dbTipoDocumento = new DatabaseManagerTipoDocumento(context);
        dbSintoma = new DatabaseManagerSintoma(context);
        dbEnfermedad = new DatabaseManagerEnfermedad(context);
        dbFichaSintoma = new DatabaseManagerBioFichaSintoma(context);
        dbFichaEnfermedad = new DatabaseManagerBioFichaEnfermedad(context);
        dbPais = new DatabaseManagerPais(context);
        dbSedePoligono = new DatabaseManagerSedePoligono(context);
        dbSede = new DatabaseManagerSede(context);
        dbUsuario = new DatabaseManagerUsuario(context);

        final String pIdSede = session.getIdSede();
        listaVertice = dbSedePoligono.getList(pIdSede);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        final RegistroActivity registroActivity = new RegistroActivity();
        final SintomasActivity sintomasActivity = new SintomasActivity();
        final EnfermedadesActivity enfermedadesActivity = new EnfermedadesActivity();

        viewPagerAdapter.AddFragment(registroActivity, "Datos Generales");
        viewPagerAdapter.AddFragment(sintomasActivity,"Síntomas");
        viewPagerAdapter.AddFragment(enfermedadesActivity,"Enfermedades");

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_insert_tap);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sintomas_tab);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_enfermedad_tab);

        WebServiceSedePoligono();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitud = location.getLatitude();
                longitud = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                },10);
                return;
            }
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

        btnGrabar = (Button) findViewById(R.id.btnGrabar);
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(ConnectivityReceiver.isConnected(context))){
                    Toast.makeText(context, "Necesita contectarte a internet para continuar", Toast.LENGTH_LONG).show();
                    return;
                }

                // Todo Location Already on  ... start
                final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                // Todo Location Already on  ... end
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Util.hasGPSDevice(context)) {
                    Toast.makeText(context, "Necesita habilitar el GPS", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!(VerificarSedeGPS())){
                    Toast.makeText(context, "Para registrar una ficha usted debe encontrarse dentro del perimetro de la sede " + session.getNomSede(), Toast.LENGTH_LONG).show();
                    return;
                }

                String pAccion = "INSERT";
                String pId = "0";
                final String ide = session.getIdFicha();
                if(!(ide.isEmpty())){
                    pAccion = "UPDATE";
                    pId = ide;
                }

                final String pIdSede = session.getIdSede();

                final String pTipoDocumento = registroActivity.spTipoDocumento.getSelectedItem().toString();
                if (pTipoDocumento.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar un tipo de documento", Toast.LENGTH_LONG).show();
                    return;
                }
                TipoDocumentoBean tipoDocumentoBean = dbTipoDocumento.getByName(pTipoDocumento);
                final String pIdTipoDocumento = tipoDocumentoBean.getID();

                final String pNumDocumento = registroActivity.txtNumDocumento.getText().toString();
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

                final String pPais = registroActivity.spPais.getSelectedItem().toString();
                if (pPais.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar un pais", Toast.LENGTH_LONG).show();
                    return;
                }
                PaisBean paisBean = dbPais.getByName(pPais);
                final String pCodPais = paisBean.getCOD();

                final String pNombres = registroActivity.txtNombres.getText().toString();
                if (pNombres.isEmpty()){
                    Toast.makeText(context, "Debe ingresar el campo Nombres", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pApePaterno = registroActivity.txtApePaterno.getText().toString();
                if (pApePaterno.isEmpty()){
                    Toast.makeText(context, "Debe ingresar el campo Apellido Paterno", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pApeMaterno = registroActivity.txtApeMaterno.getText().toString();
                if (pApeMaterno.isEmpty()){
                    Toast.makeText(context, "Debe ingresar el campo Apellido Materno", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pGenero = registroActivity.spGenero.getSelectedItem().toString();
                if (pGenero.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar Genero", Toast.LENGTH_LONG).show();
                    return;
                }

                final String fechaNacimiento = registroActivity.txtFechaNacimiento.getText().toString();
                if (fechaNacimiento.isEmpty()){
                    Toast.makeText(context, "Debe seleccionar la fecha de nacimiento", Toast.LENGTH_LONG).show();
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

                final String pEstatura = registroActivity.txtEstatura.getText().toString();
                if (pEstatura.isEmpty()){
                    Toast.makeText(context, "Debe ingresar el campo Estatura", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pPeso = registroActivity.txtPeso.getText().toString();
                if (pPeso.isEmpty()){
                    Toast.makeText(context, "Debe ingresar el campo Peso", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pIMC = registroActivity.lblIMC.getText().toString();
                final String pGrados = registroActivity.txtGrados.getText().toString();
                if (pGrados.isEmpty()){
                    Toast.makeText(context, "Debe ingresar el campo Grados", Toast.LENGTH_LONG).show();
                    return;
                }

                /*
                SparseBooleanArray checked = null;
                checked = sintomasActivity.lvSintoma.getCheckedItemPositions();
                if (checked != null){
                    if (checked.size() == 0){
                        Toast.makeText(context, "Debe seleccionar un tipo de sintoma", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                checked = enfermedadesActivity.lvEnfermedad.getCheckedItemPositions();
                if (checked != null){
                    if (checked.size() == 0){
                        Toast.makeText(context, "Debe seleccionar un tipo de enfermedad", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                 */

                Date currentTime = Calendar.getInstance().getTime();
                final String pFecha = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());


                double grados = Double.parseDouble(pGrados);
                String mensaje = "Temperatura humana normal";
                if(grados >= 36.0 && grados <= 37.0 ){
                    mensaje = "Temperatura humana normal";
                }else if(grados >= 37.1 && grados <= 38.1 ){
                    mensaje = "Febricula";
                }else if(grados >= 38.2 && grados <= 38.5 ){
                    mensaje = "Fiebre leve";
                }else if(grados >= 38.6 && grados <= 39.0 ){
                    mensaje = "Fiebre moderada";
                }else if(grados > 39.0){
                    mensaje = "Fiebre alta";
                }
                final String pMensaje = mensaje;

                final String pOtroSintoma = "";

                // Validar si existe ficha localmente (por Tipo Document, Num Documento y Fecha Registro)
                if (pAccion.equals("INSERT")){
                    String fechaRegistro = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
                    if(dbFicha.VerificarRegistroPorDia(pIdTipoDocumento,pNumDocumento,fechaRegistro)){
                        Toast.makeText(context, "Este registro ya se encuentra registrado", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                OpenProgressBar();
                final String QUERY = "CALL SP_FICHA('"+pAccion+"',"+pId+","+pIdSede+","+pIdTipoDocumento+",'"+pNumDocumento+"','"+pCodPais+"','"+pNombres+"','"+pApePaterno+"','"+pApeMaterno+"','" + pFechaNacimiento +"','"+pGenero+"',"+pEstatura+","+pPeso+","+pIMC+","+pGrados+",'"+ pMensaje+ "'," + latitud + "," + longitud +",'"+ pOtroSintoma +"');";
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
                                    ID_FICHA = jsonObject.getString("ID");
                                    ID_USUARIO = jsonObject.getString("ID_USUARIO");
                                    MENSAJE = jsonObject.getString("MENSAJE");

                                    if(ID_FICHA.equals("0") || ID_FICHA.isEmpty()){
                                        CloseProgressBar();
                                        Toast.makeText(context,MENSAJE,Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }

                                // INSERTAMOS LA FICHA EN EL SQLITE
                                BioFichaBean bioFichaBean = new BioFichaBean();
                                bioFichaBean.setID(ID_FICHA);
                                bioFichaBean.setID_SEDE(pIdSede);
                                bioFichaBean.setID_TIPO_DOCUMENTO(pIdTipoDocumento);
                                bioFichaBean.setNUM_DOCUMENTO(pNumDocumento);
                                bioFichaBean.setCOD_PAIS(pCodPais);
                                bioFichaBean.setNOMBRES(pNombres);
                                bioFichaBean.setAPELLIDO_PATERNO(pApePaterno);
                                bioFichaBean.setAPELLIDO_MATERNO(pApeMaterno);
                                bioFichaBean.setFECHA_NACIMIENTO(pFechaNacimiento);
                                bioFichaBean.setGENERO(pGenero);
                                bioFichaBean.setESTATURA(pEstatura);
                                bioFichaBean.setPESO(pPeso);
                                bioFichaBean.setIMC(pIMC);
                                bioFichaBean.setGRADO_CELSIUS(pGrados);
                                bioFichaBean.setMENSAJE_ESTADO(pMensaje);
                                bioFichaBean.setLATITUD(latitud);
                                bioFichaBean.setLONGITUD(longitud);
                                bioFichaBean.setOTRO_SINTOMA(pOtroSintoma);
                                bioFichaBean.setFEC_CREACION(pFecha);
                                //bioFichaBean.setFEC_ACTUALIZACION(pIdTipoDocumento);
                                //bioFichaBean.setFEC_ELIMINACION(pIdTipoDocumento);

                                if(finalAccion.equals("UPDATE")){
                                    bioFichaBean = dbFicha.getObject(finalId);
                                    bioFichaBean.setESTATURA(pEstatura);
                                    bioFichaBean.setPESO(pPeso);
                                    bioFichaBean.setIMC(pIMC);
                                    bioFichaBean.setGRADO_CELSIUS(pGrados);
                                    bioFichaBean.setMENSAJE_ESTADO(pMensaje);
                                    bioFichaBean.setLATITUD(latitud);
                                    bioFichaBean.setLONGITUD(longitud);
                                    bioFichaBean.setOTRO_SINTOMA(pOtroSintoma);
                                    bioFichaBean.setFEC_CREACION(pFecha);
                                    bioFichaBean.setFEC_ACTUALIZACION(pFecha);
                                    dbFicha.actualizar(bioFichaBean);
                                }else{
                                    dbFicha.insertar(bioFichaBean);
                                }

                                // Insertamos Usuario Empleado
                                if (finalAccion.equals("INSERT")){
                                    if (!(ID_USUARIO.equals("0"))){
                                        if (!(dbUsuario.verificarRegistroPorID(ID_USUARIO))){
                                            UsuarioBean usuarioBean = new UsuarioBean();
                                            usuarioBean.setID(ID_USUARIO);
                                            usuarioBean.setID_TIPO_DOCUMENTO(pIdTipoDocumento);
                                            usuarioBean.setNUM_DOCUMENTO(pNumDocumento);
                                            usuarioBean.setCOD_PAIS(pCodPais);
                                            usuarioBean.setNOMBRES(pNombres);
                                            usuarioBean.setAPELLIDO_PATERNO(pApePaterno);
                                            usuarioBean.setAPELLIDO_MATERNO(pApeMaterno);
                                            usuarioBean.setID_EMPRESA(session.getIdEmpresa());
                                            usuarioBean.setGENERO(pGenero);
                                            usuarioBean.setFECHA_NACIMIENTO(pFechaNacimiento);
                                            bioFichaBean.setFEC_CREACION(pFecha);
                                            dbUsuario.insertar(usuarioBean);
                                        }
                                    }
                                }

                                SparseBooleanArray checked = null;
                                String SINTOMAS = "";//"1"; // SET 1 "NINGUNO"
                                checked = sintomasActivity.lvSintoma.getCheckedItemPositions();
                                if (checked != null){
                                    SintomaBean sintomaBean = null;
                                    String name = "";
                                    String ID_SINTOMA = "";
                                    for (int i = 0; i < checked.size() ; ++i) {
                                        if (checked.valueAt(i)) {
                                            int pos = checked.keyAt(i);
                                            name = sintomasActivity.lvSintoma.getAdapter().getItem(pos).toString();
                                            sintomaBean = dbSintoma.getByName(name);
                                            ID_SINTOMA = sintomaBean.getID();
                                            if (SINTOMAS.isEmpty())
                                                SINTOMAS = ID_SINTOMA;
                                            else
                                                SINTOMAS = SINTOMAS + "," + ID_SINTOMA;
                                        }
                                    }
                                }

                                final String QUERY = "call SP_FICHA_SINTOMA('INSERT'," + ID_FICHA + ",'" + SINTOMAS + "');";
                                final String ID_FICHAK  = ID_FICHA;
                                final String SINTOMASK = SINTOMAS;
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            //JSONArray jsonArray = new JSONArray(response);

                                            // INSERTARMOS FICHA_SINTOMA EN EL SQLITE
                                            dbFichaSintoma.eliminar(ID_FICHAK);
                                            List<String> lista = Arrays.asList(SINTOMASK.split(","));
                                            BioFichaSintomaBean bean = null;
                                            for (int i = 0; i < lista.size() ; ++i) {
                                                String ID = lista.get(i);
                                                bean = new BioFichaSintomaBean();
                                                bean.setID_FICHA(ID_FICHAK);
                                                bean.setID_SINTOMA(ID);
                                                dbFichaSintoma.insertar(bean);
                                            }

                                            SparseBooleanArray checked = null;
                                            String ENFERMEDADES = "";//"1"; // SET 1 "NINGUNO"
                                            checked = enfermedadesActivity.lvEnfermedad.getCheckedItemPositions();
                                            if (checked != null){
                                                EnfermedadBean enfermedadBean = null;
                                                String name = "";
                                                String ID_ENFERMEDAD = "";

                                                for (int i = 0; i < checked.size(); ++i) {
                                                    if (checked.valueAt(i)) {
                                                        int pos = checked.keyAt(i);
                                                        name = enfermedadesActivity.lvEnfermedad.getAdapter().getItem(pos).toString();
                                                        enfermedadBean = dbEnfermedad.getByName(name);
                                                        ID_ENFERMEDAD = enfermedadBean.getID();
                                                        if (ENFERMEDADES.isEmpty())
                                                            ENFERMEDADES = ID_ENFERMEDAD;
                                                        else
                                                            ENFERMEDADES = ENFERMEDADES + "," +ID_ENFERMEDAD;
                                                    }
                                                }
                                            }

                                            final String QUERY = "call SP_FICHA_ENFERMEDAD('INSERT'," + ID_FICHAK + ",'" + ENFERMEDADES + "');";
                                            final String ENFERMEDADESK = ENFERMEDADES;
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        //JSONArray jsonArray = new JSONArray(response);

                                                        // INSERTARMOS FICHA_SINTOMA EN EL SQLITE
                                                        dbFichaEnfermedad.eliminar(ID_FICHAK);
                                                        List<String> lista = Arrays.asList(ENFERMEDADESK.split(","));
                                                        BioFichaEnfermedadBean bean = null;
                                                        for (int i = 0; i < lista.size() ; ++i) {
                                                            String ID = lista.get(i);
                                                            bean = new BioFichaEnfermedadBean();
                                                            bean.setID_FICHA(ID_FICHAK);
                                                            bean.setID_ENFERMEDAD(ID);
                                                            dbFichaEnfermedad.insertar(bean);
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

                                        } catch (Exception e) {
                                            CloseProgressBar();
                                            Toast.makeText(context,"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
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
                                requestQueue.add(stringRequest);

                            } catch (Exception e) {
                                CloseProgressBar();
                                Toast.makeText(context,"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
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

    @Override
    public void onResume() {
        super.onResume();
        CloseProgressBar();
    }

    public boolean VerificarSedeGPS(){
        boolean resultado = true;
        builder = null;
        builder = new Polygon.Builder();
        flag = 0;
        sedeBean = dbSede.get(session.getIdSede());

        for (int i = 0; i < listaVertice.size(); i++){
            cIdSede = Integer.parseInt(listaVertice.get(i).getID_SEDE());
            verLat = listaVertice.get(i).getLATITUD();
            verLon = listaVertice.get(i).getLONGITUD();
            if (i > 0){
                cIdSedeAux = Integer.parseInt(listaVertice.get(i - 1).getID_SEDE());
                if (i == listaVertice.size() - 1){
                    cIdSedeAux = cIdSede;
                    flag = 1;
                    builder.addVertex(new Point(verLat,verLon));
                }
                if (cIdSede != cIdSedeAux){
                    flag = 1;
                }
            }
            if (flag == 0)
                builder.addVertex(new Point(verLat,verLon));

            if (flag == 1){
                flag = 0;
                polygon = builder.build();
                point = new Point(latitud, longitud);
                contains = polygon.contains(point);
                if(contains){
                    resultado = true;
                    //Toast.makeText(context, "Estas dentro de " + sedeBean.getNOMBRE_SEDE(),Toast.LENGTH_SHORT).show();
                }else {
                    builder = null;
                    resultado = false;
                    //Toast.makeText(context, "Estas fuera de " + sedeBean.getNOMBRE_SEDE(),Toast.LENGTH_SHORT).show();
                }
            }
        }
        return resultado;
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

    public void WebServiceSedePoligono(){
        if(!(ConnectivityReceiver.isConnected(context))){
            Toast.makeText(context, "Necesita contectarte a internet para continuar", Toast.LENGTH_LONG).show();
            return;
        }

        final String QUERY = "call SP_SEDE_POLIGONO('SELECT_BY_SEDE',"+ session.getIdSede() +");";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]") || response.equals("") || response.isEmpty()) {
                    Toast.makeText(context, "No se encontraron datos SP_SEDE_POLIGONO", Toast.LENGTH_LONG).show();
                }else{
                    dbSedePoligono.eliminarPorSede(session.getIdSede());
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro servicio poligono: " + error.getMessage(),Toast.LENGTH_SHORT).show();
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
}
