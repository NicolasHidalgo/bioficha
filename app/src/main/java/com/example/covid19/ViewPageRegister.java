package com.example.covid19;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import beans.SpinnerBean;
import beans.TipoDocumentoBean;
import db.DatabaseManagerTipoDocumento;
import helper.Session;

public class ViewPageRegister extends AppCompatActivity {

    Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  ViewPagerAdapter viewPagerAdapter;
    Button btnGrabar;
    private Session session;
    RequestQueue requestQueue;

    DatabaseManagerTipoDocumento dbTipoDocumento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_register);
        context = this;
        session = new Session(context);
        dbTipoDocumento = new DatabaseManagerTipoDocumento(context);

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
                String pAccion = "INSERT";
                String pId = "0";
                String pIdSede = session.getIdSede();
                String pTipoDocumento = registroActivity.spTipoDocumento.getSelectedItem().toString();
                TipoDocumentoBean tipoDocumentoBean = dbTipoDocumento.getByName(pTipoDocumento);
                String pIdTipoDocumento = tipoDocumentoBean.getID();
                String pNumDocumento = registroActivity.txtNumDocumento.getText().toString();
                String pNacionalidad = registroActivity.txtNacionalidad.getText().toString();
                String pNombres = registroActivity.txtNombres.getText().toString();
                String pApePaterno = registroActivity.txtApePaterno.getText().toString();
                String pApeMaterno = registroActivity.txtApeMaterno.getText().toString();
                String pGenero = registroActivity.spGenero.getSelectedItem().toString();
                String pCorreo = registroActivity.txtCorreo.getText().toString();
                String pFechaNacimiento = registroActivity.txtFechaNacimiento.getText().toString();
                String pEstatura = registroActivity.txtEstatura.getText().toString();
                String pPeso = registroActivity.txtPeso.getText().toString();
                String pIMC = registroActivity.lblIMC.getText().toString();
                String pGrados = registroActivity.txtGrados.getText().toString();

                String URL = "https://bioficha.electocandidato.com/insert_id.php";
                final String QUERY = "CALL SP_FICHA('"+pAccion+"',null,"+pIdSede+","+pIdTipoDocumento+",'"+pNumDocumento+"','"+pNacionalidad+"','"+pNombres+"','"+pApePaterno+"','"+pApeMaterno+"','1996-12-20','"+pGenero+"',"+pEstatura+","+pPeso+","+pIMC+","+pGrados+",'message in a bottle',1234,5678,'');";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")){
                            Toast.makeText(context,"No se encontraron datos", Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String ID = jsonObject.getString("ID");
                                    String MENSAJE = jsonObject.getString("MENSAJE");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new HashMap<>();
                        parametros.put("consulta", QUERY);
                        return parametros;
                    }
                };;
                requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }
        });

    }
}
