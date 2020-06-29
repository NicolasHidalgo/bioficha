package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import beans.BioFichaBean;
import db.DatabaseManagerBioFicha;
import helper.Session;
import util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class FichasActivity extends AppCompatActivity {

    Context context;
    Session session;
    DatabaseManagerBioFicha dbFicha;
    List<BioFichaBean> listaFicha;

    FloatingActionButton btnAgregarFicha;
    ListView lvFicha;
    public final String URL = "https://bioficha.electocandidato.com/insert_id.php";
    RequestQueue requestQueue;
    ProgressBar progressBar;
    String nInfoId[];
    String nInfo1[];
    String nInfo2[];
    String nInfo3[];

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichas);
        context = this;
        session = new Session(context);
        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);
        dbFicha = new DatabaseManagerBioFicha(context);

        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        listaFicha = dbFicha.ListarPorSedeFechaV2(session.getIdSede(),fechaHoy);

        BioFichaBean bio = null;
        int len = listaFicha.size();
        nInfoId = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaFicha.size(); i++) {
            bio = listaFicha.get(i);
            nInfoId[i] = bio.getID();
            nInfo1[i] = bio.getNOMBRES();
            nInfo2[i] = bio.getNUM_DOCUMENTO();
            nInfo3[i] = bio.getESTADO();
        }

        btnAgregarFicha = findViewById(R.id.btnAgregarFicha);
        lvFicha = findViewById(R.id.lvFicha);
        MyAdapter adapter = new MyAdapter(this, nInfoId,nInfo1,nInfo2,nInfo3);
        lvFicha.setAdapter(adapter);

        btnAgregarFicha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo Location Already on  ... start
                final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                // Todo Location Already on  ... end
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Util.hasGPSDevice(context)) {
                    Toast.makeText(context, "Necesita habilitar el GPS", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent dsp = new Intent(FichasActivity.this, ViewPageRegister.class);
                session.setIdFicha("");
                startActivity(dsp);
            }
        });

        lvFicha.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Todo Location Already on  ... start
                final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                // Todo Location Already on  ... end
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Util.hasGPSDevice(context)) {
                    Toast.makeText(context, "Necesita habilitar el GPS", Toast.LENGTH_LONG).show();
                    return;
                }

                String ide =(String) ((TextView) view.findViewById(R.id.txtInfoFichaId)).getText();
                session.setIdFicha(ide);
                Intent intent = new Intent(context,ViewPageRegister.class);
                startActivity(intent);
            }
        });
        lvFicha.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final View v = view;
                AlertDialog.Builder builder = new AlertDialog.Builder(FichasActivity.this);
                // Set a title for alert dialog
                builder.setTitle("ALERTA");

                // Ask the final question
                builder.setMessage("¿Estás seguro de eliminar esta ficha?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                EliminarFicha(v);
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

        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        listaFicha = dbFicha.ListarPorSedeFechaV2(session.getIdSede(),fechaHoy);
        BioFichaBean bio = null;
        int len = listaFicha.size();
        nInfoId = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaFicha.size(); i++) {
            bio = listaFicha.get(i);
            nInfoId[i] = bio.getID();
            nInfo1[i] = bio.getNOMBRES();
            nInfo2[i] = bio.getNUM_DOCUMENTO();
            nInfo3[i] = bio.getESTADO();
        }
        btnAgregarFicha = findViewById(R.id.btnAgregarFicha);
        lvFicha = findViewById(R.id.lvFicha);
        MyAdapter adapter = new MyAdapter(this, nInfoId, nInfo1, nInfo2, nInfo3);
        lvFicha.setAdapter(adapter);

    }
    public void EliminarFicha(View v){
        String pId = (String) ((TextView) v.findViewById(R.id.txtInfoFichaId)).getText();
        String pAccion = "DELETE";
        final String QUERY = "call SP_FICHA('" + pAccion + "', " + pId + ",NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);";
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
                        String ID_FICHA = "";
                        String MENSAJE = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            ID_FICHA = jsonObject.getString("ID");
                            MENSAJE = jsonObject.getString("MENSAJE");
                            if (ID_FICHA.equals("0")) {
                                CloseProgressBar();
                                Toast.makeText(context, MENSAJE, Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        dbFicha.EliminarRegistro(ID_FICHA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CloseProgressBar();
                Toast.makeText(context, "Se ha eliminado la ficha", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CloseProgressBar();
                Toast.makeText(context, "Ocurrió un error al eliminar la ficha", Toast.LENGTH_LONG).show();
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
    class MyAdapter extends ArrayAdapter<String>{
     Context context;

     String nInfoId[];
     String nInfo1[];
     String nInfo2[];
     String nInfo3[];

     MyAdapter(Context c, String nInfoId[], String nInfo1[], String nInfo2[], String nInfo3[]){
         super(c,R.layout.row, R.id.txtInfoFichaId, nInfoId);
         this.context = c;
         this.nInfoId = nInfoId;
         this.nInfo1 = nInfo1;
         this.nInfo2 = nInfo2;
         this.nInfo3 = nInfo3;
     }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView id = row.findViewById(R.id.txtInfoFichaId);
            TextView ficha = row.findViewById(R.id.txtInfoFicha1);
            TextView empleado = row.findViewById(R.id.txtInfoFicha2);
            TextView hora = row.findViewById(R.id.txtInfoFicha3);

            id.setText(nInfoId[position]);
            ficha.setText(nInfo1[position]);
            empleado.setText(nInfo2[position]);
            String estado = nInfo3[position];
            if(estado.equals("1")){
                hora.setTextColor(Color.GREEN);
                hora.setText("Activo");
            }else{
                hora.setTextColor(Color.RED);
                hora.setText("Inactivo");
            }

            return row;
        }
    }
}
