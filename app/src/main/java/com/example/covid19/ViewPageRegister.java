package com.example.covid19;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
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

import org.json.JSONArray;
import org.json.JSONException;
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
import beans.BioFichaBean;
import beans.BioFichaEnfermedadBean;
import beans.BioFichaSintomaBean;
import beans.EnfermedadBean;
import beans.PaisBean;
import beans.SintomaBean;
import beans.SpinnerBean;
import beans.TipoDocumentoBean;
import db.DatabaseManagerBioFicha;
import db.DatabaseManagerBioFichaEnfermedad;
import db.DatabaseManagerBioFichaSintoma;
import db.DatabaseManagerEnfermedad;
import db.DatabaseManagerPais;
import db.DatabaseManagerSintoma;
import db.DatabaseManagerTipoDocumento;
import helper.ConnectivityReceiver;
import helper.Session;

public class ViewPageRegister extends AppCompatActivity {

    Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  ViewPagerAdapter viewPagerAdapter;
    Button btnGrabar;
    private Session session;
    RequestQueue requestQueue;
    final String URL = "https://bioficha.electocandidato.com/insert_id.php";

    DatabaseManagerBioFicha dbFicha;
    DatabaseManagerTipoDocumento dbTipoDocumento;
    DatabaseManagerSintoma dbSintoma;
    DatabaseManagerEnfermedad dbEnfermedad;
    DatabaseManagerBioFichaSintoma dbFichaSintoma;
    DatabaseManagerBioFichaEnfermedad dbFichaEnfermedad;
    DatabaseManagerPais dbPais;

    ProgressBar progressBar;
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

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        final RegistroActivity registroActivity = new RegistroActivity();
        final SintomasActivity sintomasActivity = new SintomasActivity();
        final EnfermedadesActivity enfermedadesActivity = new EnfermedadesActivity();

        viewPagerAdapter.AddFragment(registroActivity, "Ficha");
        viewPagerAdapter.AddFragment(sintomasActivity,"Síntomas");
        viewPagerAdapter.AddFragment(enfermedadesActivity,"Enfermedades");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_insert_tap);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sintomas_tab);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_enfermedad_tab);

        btnGrabar = (Button) findViewById(R.id.btnGrabar);
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(ConnectivityReceiver.isConnected(context))){
                    Toast.makeText(context, "Necesitas contectarte a internet para continuar", Toast.LENGTH_LONG).show();
                    return;
                }

                OpenProgressBar();

                String pAccion = "INSERT";
                String pId = "0";
                final String pIdSede = session.getIdSede();

                final String pTipoDocumento = registroActivity.spTipoDocumento.getSelectedItem().toString();
                TipoDocumentoBean tipoDocumentoBean = dbTipoDocumento.getByName(pTipoDocumento);
                final String pIdTipoDocumento = tipoDocumentoBean.getID();

                final String pNumDocumento = registroActivity.txtNumDocumento.getText().toString();

                final String pais = registroActivity.spPais.getSelectedItem().toString();
                PaisBean paisBean = dbPais.getByName(pais);
                final String pPais = paisBean.getCOD();

                final String pNombres = registroActivity.txtNombres.getText().toString();
                final String pApePaterno = registroActivity.txtApePaterno.getText().toString();
                final String pApeMaterno = registroActivity.txtApeMaterno.getText().toString();
                final String pGenero = registroActivity.spGenero.getSelectedItem().toString();
                String pCorreo = registroActivity.txtCorreo.getText().toString();
                final String pFechaNacimiento = registroActivity.txtFechaNacimiento.getText().toString();
                final String pEstatura = registroActivity.txtEstatura.getText().toString();
                final String pPeso = registroActivity.txtPeso.getText().toString();
                final String pIMC = registroActivity.lblIMC.getText().toString();
                final String pGrados = registroActivity.txtGrados.getText().toString();

                final String QUERY = "CALL SP_FICHA('"+pAccion+"',null,"+pIdSede+","+pIdTipoDocumento+",'"+pNumDocumento+"','"+pPais+"','"+pNombres+"','"+pApePaterno+"','"+pApeMaterno+"','1996-12-20','"+pGenero+"',"+pEstatura+","+pPeso+","+pIMC+","+pGrados+",'message in a bottle',1234,5678,'');";
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
                                String MENSAJE = "";
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    ID_FICHA = jsonObject.getString("ID");
                                    MENSAJE = jsonObject.getString("MENSAJE");

                                    if(ID_FICHA.equals("0")){
                                        CloseProgressBar();
                                        Toast.makeText(context,MENSAJE,Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    // INSERTAMOS LA FICHA EN EL SQLITE
                                    BioFichaBean bioFichaBean = new BioFichaBean();
                                    bioFichaBean.setID(ID_FICHA);
                                    bioFichaBean.setID_SEDE(pIdSede);
                                    bioFichaBean.setID_TIPO_DOCUMENTO(pIdTipoDocumento);
                                    bioFichaBean.setNUM_DOCUMENTO(pNumDocumento);
                                    bioFichaBean.setCOD_PAIS(pPais);
                                    bioFichaBean.setNOMBRES(pNombres);
                                    bioFichaBean.setAPELLIDO_PATERNO(pApePaterno);
                                    bioFichaBean.setAPELLIDO_MATERNO(pApeMaterno);
                                    bioFichaBean.setFECHA_NACIMIENTO(pFechaNacimiento);
                                    bioFichaBean.setGENERO(pGenero);
                                    bioFichaBean.setESTATURA(pEstatura);
                                    bioFichaBean.setPESO(pPeso);
                                    bioFichaBean.setIMC(pIMC);
                                    bioFichaBean.setGRADO_CELSIUS(pGrados);
                                    bioFichaBean.setMENSAJE_ESTADO("");
                                    bioFichaBean.setLATITUD("123");
                                    bioFichaBean.setLONGITUD("456");
                                    bioFichaBean.setOTRO_SINTOMA("");
                                    Date currentTime = Calendar.getInstance().getTime();
                                    String fecha = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
                                    bioFichaBean.setFEC_CREACION(fecha);
                                    //bioFichaBean.setFEC_ACTUALIZACION(pIdTipoDocumento);
                                    //bioFichaBean.setFEC_ELIMINACION(pIdTipoDocumento);
                                    dbFicha.insertar(bioFichaBean);

                                }

                                SparseBooleanArray checked = null;
                                checked = sintomasActivity.lvSintoma.getCheckedItemPositions();
                                SintomaBean sintomaBean = null;
                                String name = "";
                                String ID_SINTOMA = "";
                                String SINTOMAS = "";
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

                                final String QUERY = "call SP_FICHA_SINTOMA('INSERT'," + ID_FICHA + ",'" + SINTOMAS + "');";
                                final String ID_FICHAK  = ID_FICHA;
                                final String SINTOMASK = SINTOMAS;
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);

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
                                            checked = enfermedadesActivity.lvEnfermedad.getCheckedItemPositions();
                                            EnfermedadBean enfermedadBean = null;
                                            String name = "";
                                            String ID_ENFERMEDAD = "";
                                            String ENFERMEDADES = "";
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

                                            final String QUERY = "call SP_FICHA_ENFERMEDAD('INSERT'," + ID_FICHAK + ",'" + ENFERMEDADES + "');";
                                            final String ENFERMEDADESK = ENFERMEDADES;
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONArray jsonArray = new JSONArray(response);

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
                                                    } catch (JSONException e) {
                                                        CloseProgressBar();
                                                        Toast.makeText(context,"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
                                                    }
                                                    CloseProgressBar();
                                                    Toast.makeText(context,"Registro exitoso",Toast.LENGTH_LONG).show();
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

                                        } catch (JSONException e) {
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

                            } catch (JSONException e) {
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
