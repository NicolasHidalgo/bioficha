package com.example.covid19;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import beans.BioFichaBean;
import beans.SedeBean;
import db.DatabaseManagerSede;
import helper.Session;

public class SedeActivity extends AppCompatActivity {
    Context context;
    DatabaseManagerSede dbSede;
    List<SedeBean> listaSede;
    Session session;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    public final String URL = "https://bioficha.electocandidato.com/insert_id.php";
    String nID[];
    String nInfo1[];
    String nInfo2[];
    String nInfo3[];
    ListView lvSede;
    SedeActivity.MyAdapter adapter;
    FloatingActionButton btnAgregarSede;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sede);
        context = this;
        session = new Session(context);
        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);
        dbSede = new DatabaseManagerSede(context);
        lvSede = findViewById(R.id.lvSede);

        Listar();

        btnAgregarSede = findViewById(R.id.btnAgregarSede);
        btnAgregarSede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(SedeActivity.this, ViewPageRegisterSede.class);
                session.setIdSede("");
                startActivity(dsp);
            }
        });
        lvSede.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dsp = new Intent(SedeActivity.this, ViewPageRegisterSede.class);
                String ide =(String) ((TextView) view.findViewById(R.id.txtInfoSedeId)).getText();
                session.setIdSede(ide);
                startActivity(dsp);
            }
        });
        lvSede.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final View v = view;
                AlertDialog.Builder builder = new AlertDialog.Builder(SedeActivity.this);
                // Set a title for alert dialog
                builder.setTitle("ALERTA");

                // Ask the final question
                builder.setMessage("¿Estás seguro de eliminar esta sede?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                EliminarSede(v);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }

                    }
                };


                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Si", dialogClickListener);
                // Set the alert dialog no button click listener
                builder.setNegativeButton("No", dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
        Listar();

    }
    public void CloseProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void OpenProgressBar() {
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
    public void EliminarSede(View v){
        String pId = (String) ((TextView) v.findViewById(R.id.txtInfoSedeId)).getText();
        String pIdEmpresa = session.getIdEmpresa();
        String pAccion = "DELETE";
        final String QUERY = "call SP_SEDE_UPDATE('" + pAccion + "', " + pId + "," + pIdEmpresa + ",NULL,NULL,NULL,NULL,NULL);";
        final String finalAccion = pAccion;
        final String finalId = pId;
        OpenProgressBar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]") || response.equals("")) {
                    Toast.makeText(context, "No se encontraron datos", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = null;
                        String ID_SEDE = "";
                        String MENSAJE = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            ID_SEDE = jsonObject.getString("ID");
                            MENSAJE = jsonObject.getString("MENSAJE");
                            if (ID_SEDE.equals("0")) {
                                CloseProgressBar();
                                Toast.makeText(context, MENSAJE, Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        dbSede.EliminarRegistro(ID_SEDE);
                        Listar();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CloseProgressBar();
                Toast.makeText(context, "Se ha eliminado la sede", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CloseProgressBar();
                Toast.makeText(context, "Ocurrió un error al eliminar la sede", Toast.LENGTH_LONG).show();
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
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String nID[];
        String nInfo1[];
        String nInfo2[];
        String nInfo3[];

        MyAdapter(Context c, String ID[], String Info1[], String Info2[], String Info3[]) {
            super(c, R.layout.row_sede, R.id.txtInfoSedeId, ID);
            this.context = c;
            this.nID = ID;
            this.nInfo1 = Info1;
            this.nInfo2 = Info2;
            this.nInfo3 = Info3;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_sede, parent, false);
            TextView id = row.findViewById(R.id.txtInfoSedeId);
            TextView nombre_sede = row.findViewById(R.id.txtInfoSede1);
            TextView direccion = row.findViewById(R.id.txtInfoSede2);
            TextView hora = row.findViewById(R.id.txtInfoSede3);
            id.setText(nID[position]);
            nombre_sede.setText(nInfo1[position]);
            direccion.setText(nInfo2[position]);
            hora.setText(nInfo3[position]);
            return row;
        }
    }

    public void Listar(){
        listaSede = dbSede.ListarPorSedeXEmpresa(session.getIdEmpresa());
        SedeBean sede = null;
        int len = listaSede.size();
        nID = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];
        for (int i = 0; i < listaSede.size(); i++) {
            sede = listaSede.get(i);
            nID[i] = sede.getID();
            nInfo1[i] = sede.getNOMBRE_SEDE();
            nInfo2[i] = sede.getDIRECCION();
            nInfo3[i] = sede.getFEC_CREACION();
        }

        adapter = new SedeActivity.MyAdapter(this, nID, nInfo1, nInfo2, nInfo3);
        lvSede.setAdapter(adapter);
    }
}

