package com.example.covid19;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import beans.DistritoBean;
import beans.SedeBean;
import beans.SedePoligonoBean;
import db.DatabaseManagerDepartamento;
import db.DatabaseManagerDistrito;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerProvincia;
import db.DatabaseManagerSede;
import db.DatabaseManagerSedePoligono;
import helper.ConnectivityReceiver;
import helper.Session;

public class ViewPageRegisterSede extends AppCompatActivity {

    Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Session session;
    RequestQueue requestQueue;
    final String URL = "https://bioficha.electocandidato.com/insert_id.php";
    SedeBean sedeBean = null;
    DatabaseManagerDepartamento dbDepartamento;
    DatabaseManagerProvincia dbProvincia;
    DatabaseManagerDistrito dbDistrito;
    DatabaseManagerEmpresa dbEmpresa;
    DatabaseManagerSede dbSede;
    DatabaseManagerSedePoligono dbSedePoligono;
    ProgressBar progressBar;
    Button btnGrabar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_register_sede);
        context = this;
        session = new Session(context);
        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        final RegistroSedesActivity registroSedesActivity = new RegistroSedesActivity();
        final RegistroGeolocalizacionActivity registroGeolocalizacionActivity = new RegistroGeolocalizacionActivity();

        viewPagerAdapter.AddFragment(registroSedesActivity, "Datos Generales");
        viewPagerAdapter.AddFragment(registroGeolocalizacionActivity, "Geolocalización");

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_sede_tap);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_location_tap);

        dbDepartamento = new DatabaseManagerDepartamento(context);
        dbProvincia = new DatabaseManagerProvincia(context);
        dbDistrito = new DatabaseManagerDistrito(context);
        dbSede = new DatabaseManagerSede(context);
        dbEmpresa = new DatabaseManagerEmpresa(context);
        dbSedePoligono = new DatabaseManagerSedePoligono(context);

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
                final String ide = session.getIdSede();
                if(!(ide.isEmpty())){
                    pAccion = "UPDATE";
                    pId = ide;
                }
                final String pIdEmpresa = session.getIdEmpresa();
                final String txtNombreSede = registroSedesActivity.txtNombreSede.getText().toString();
                final String txtDireccionFiscal = registroSedesActivity.txtDireccionFiscal.getText().toString();
                final String pID_DISTRITO = registroSedesActivity.spDistrito.getSelectedItem().toString();
                final String pLATITUD = "0";
                final String pLONGITUD = "0";
                final String CoordenadaX1 = registroGeolocalizacionActivity.txtCoordenadaX1.getText().toString();
                final String CoordenadaX2 = registroGeolocalizacionActivity.txtCoordenadaX2.getText().toString();
                final String CoordenadaX3 = registroGeolocalizacionActivity.txtCoordenadaX3.getText().toString();
                final String CoordenadaX4 = registroGeolocalizacionActivity.txtCoordenadaX4.getText().toString();
                final String CoordenadaY1 = registroGeolocalizacionActivity.txtCoordenadaY1.getText().toString();
                final String CoordenadaY2 = registroGeolocalizacionActivity.txtCoordenadaY2.getText().toString();
                final String CoordenadaY3 = registroGeolocalizacionActivity.txtCoordenadaY3.getText().toString();
                final String CoordenadaY4 = registroGeolocalizacionActivity.txtCoordenadaY4.getText().toString();
                DistritoBean distritoBean = dbDistrito.getByName(pID_DISTRITO);
                if(pIdEmpresa.equals("")){
                    Toast.makeText(context, "Sessión de Empresa vacía", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtNombreSede.equals("")){
                    Toast.makeText(context, "Complete el campo Nombre Sede", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtDireccionFiscal.equals("")){
                    Toast.makeText(context, "Complete el campo Dirección Fiscal", Toast.LENGTH_LONG).show();
                    return;
                }
                if(pID_DISTRITO.equals("Seleccione")){
                    Toast.makeText(context, "Seleccione un distrito", Toast.LENGTH_LONG).show();
                    return;
                }
                if(CoordenadaX1.equals("") || CoordenadaX2.equals("") || CoordenadaX3.equals("") || CoordenadaX4.equals("") ||
                        CoordenadaY1.equals("") || CoordenadaY2.equals("") || CoordenadaY3.equals("") || CoordenadaY4.equals("")){
                    Toast.makeText(context, "Dibuje las coordenadas en el mapa", Toast.LENGTH_LONG).show();
                    return;
                }
                final String formatoCoordenada = CoordenadaX1 + "," + CoordenadaY1 + "|" + CoordenadaX2 + "," +CoordenadaY2 + "|" + CoordenadaX3 + "," + CoordenadaY3 + "|" + CoordenadaX4 + "," + CoordenadaY4;
                final String id_distrito = distritoBean.getID();
                OpenProgressBar();
                String Params = "";
                Params = Params + "'" + pAccion + "',";
                Params = Params + pId + ",";
                Params = Params +  pIdEmpresa + ",";
                Params = Params + "'" + txtNombreSede + "',";
                Params = Params + "'" + txtDireccionFiscal + "',";
                Params = Params + "'" + id_distrito + "',";
                Params = Params + "'" + pLATITUD + "',";
                Params = Params + "'" + pLONGITUD + "'";
                final String QUERY = "CALL SP_SEDE_UPDATE(" + Params  + ");";
                final String finalAccion = pAccion;
                final String finalId = pId;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")){
                            CloseProgressBar();
                            Toast.makeText(context,"No se encontraron datos", Toast.LENGTH_LONG).show();
                        }else if (response.equals("")) {
                            CloseProgressBar();
                            Toast.makeText(context, "Error en el servicio", Toast.LENGTH_LONG).show();
                        }else{
                            try{
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = null;
                                String ID_SEDE = "";
                                String MENSAJE = "";
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    ID_SEDE = jsonObject.getString("ID");
                                    MENSAJE = jsonObject.getString("MENSAJE");

                                    if(ID_SEDE.equals("0") || ID_SEDE.isEmpty()){
                                        CloseProgressBar();
                                        Toast.makeText(context,MENSAJE,Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                                // INSERTAMOS LA FICHA EN EL SQLITE
                                SedeBean sedeBean = new SedeBean();
                                sedeBean.setID(ID_SEDE);
                                sedeBean.setNOMBRE_SEDE(txtNombreSede);
                                sedeBean.setID_EMPRESA(pIdEmpresa);
                                sedeBean.setDIRECCION(txtDireccionFiscal);
                                sedeBean.setID_DISTRITO(id_distrito);
                                Date currentTime = Calendar.getInstance().getTime();
                                String fecha = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
                                sedeBean.setFEC_CREACION(fecha);
                                if(finalAccion.equals("UPDATE")){
                                    //usuarioBean = dbUsuario.getObject(finalId);
                                    sedeBean.setFEC_ACTUALIZACION(fecha);
                                    dbSede.actualizar(sedeBean);
                                }else{
                                    dbSede.insertar(sedeBean);
                                }
                                if(session.getNomRol().equals("ADMIN")){
                                    final String QUERY = "call SP_GEOLOCALIZACION('INSERT'," + ID_SEDE + ", '" + formatoCoordenada+ "');";
                                    final String COORDENADASK = formatoCoordenada;
                                    final String ID_SEDEK = ID_SEDE;
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try{
                                                dbSedePoligono.eliminar(ID_SEDEK);
                                                List<String> lista = Arrays.asList(COORDENADASK.split("\\|"));
                                                SedePoligonoBean Poligonobean = null;
                                                for (int i = 0; i < lista.size() ; ++i) {
                                                    String[] array = lista.get(i).split(",");
                                                    Poligonobean = new SedePoligonoBean();
                                                    Poligonobean.setLATITUD(Double.parseDouble(array[0]));
                                                    Poligonobean.setLONGITUD(Double.parseDouble(array[1]));
                                                    Poligonobean.setID_SEDE(ID_SEDEK);
                                                    dbSedePoligono.insertar(Poligonobean);
                                                }
                                            }catch (Exception ex){
                                                CloseProgressBar();
                                                Toast.makeText(context,"Error:" + ex.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    },new Response.ErrorListener() {
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

                                }catch (Exception ex){
                                CloseProgressBar();
                                Toast.makeText(context,"Error:" + ex.getMessage(),Toast.LENGTH_LONG).show();
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
