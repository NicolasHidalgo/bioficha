package com.example.covid19;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import beans.UsuarioBean;
import db.DatabaseManagerUsuario;
import helper.Session;

public class RegistradorActivity extends AppCompatActivity {

    Context context;
    Session session;
    DatabaseManagerUsuario dbUsuario;
    List<UsuarioBean> listaUsuario;
    ListView lvEmpleado;
    RegistradorActivity.MyAdapter adapter;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    public final String URL = "https://bioficha.electocandidato.com/insert_id.php";
    String nInfoID[];
    String nInfo1[];
    String nInfo2[];
    String nInfo3[];

    FloatingActionButton btnAgregarRegistrador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrador);

        context = this;
        session = new Session(context);
        dbUsuario = new DatabaseManagerUsuario(context);
        ProgressBarHandler(context);
        progressBar.setVisibility(View.INVISIBLE);
        lvEmpleado = findViewById(R.id.lvEmpleado);

        Listar();

        btnAgregarRegistrador = findViewById(R.id.btnAgregarRegistrador);
        btnAgregarRegistrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(RegistradorActivity.this, ViewPageRegisterEmpleado.class);
                session.setIdEmpleado("");
                startActivity(dsp);
            }
        });

        lvEmpleado.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ide =(String) ((TextView) view.findViewById(R.id.txtInfoEmpId)).getText();
                session.setIdEmpleado(ide);
                Intent intent = new Intent(context,ViewPageRegisterEmpleado.class);
                startActivity(intent);
            }
        });
        lvEmpleado.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final View v = view;
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistradorActivity.this);
                // Set a title for alert dialog
                builder.setTitle("ALERTA");

                // Ask the final question
                builder.setMessage("¿Estás seguro de eliminar este registrador?");

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
    public void EliminarSede(View v){
        String pId = (String) ((TextView) v.findViewById(R.id.txtInfoEmpId)).getText();
        String pAccion = "DELETE";
        final String QUERY = "call SP_USUARIO_UPDATE('" + pAccion + "', " + pId + ",NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);";
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
                        String ID_USUARIO = "";
                        String MENSAJE = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            ID_USUARIO = jsonObject.getString("ID");
                            MENSAJE = jsonObject.getString("MENSAJE");
                            if (ID_USUARIO.equals("0")) {
                                CloseProgressBar();
                                Toast.makeText(context, MENSAJE, Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        dbUsuario.EliminarRegistro(ID_USUARIO);
                        Listar();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CloseProgressBar();
                Toast.makeText(context, "Se ha eliminado el usuario", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CloseProgressBar();
                Toast.makeText(context, "Ocurrió un error al eliminar el usuario", Toast.LENGTH_LONG).show();
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
    class MyAdapter extends ArrayAdapter<String> {
        Context context;

        String nInfoID[];
        String nInfo1[];
        String nInfo2[];
        String nInfo3[];

        MyAdapter(Context c, String nInfoId[], String nInfo1[], String nInfo2[], String nInfo3[]){
            super(c,R.layout.row_empleado, R.id.txtInfoEmpId, nInfoId);
            this.context = c;
            this.nInfoID = nInfoId;
            this.nInfo1 = nInfo1;
            this.nInfo2 = nInfo2;
            this.nInfo3 = nInfo3;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_empleado, parent, false);
            TextView id = row.findViewById(R.id.txtInfoEmpId);
            TextView tit = row.findViewById(R.id.txtInfoEmp1);
            TextView sub = row.findViewById(R.id.txtInfoEmp2);
            TextView inf = row.findViewById(R.id.txtInfoEmp3);

            id.setText(nInfoID[position]);
            tit.setText(nInfo1[position]);
            sub.setText(nInfo2[position]);
            String estado = nInfo3[position];
            if(estado.equals("1")){
                inf.setTextColor(Color.GREEN);
                inf.setText("Activo");
            }else{
                inf.setTextColor(Color.RED);
                inf.setText("Inactivo");
            }

            return row;
        }
    }

    private void Listar(){
        if (session.getNomRol().equals("SUPER-ADMIN")){
            // Solo traer a usuarios ADMIN
            listaUsuario = dbUsuario.getListADMIN();
        }else{
            // Traer todos los usuarios
            listaUsuario = dbUsuario.getList(session.getIdEmpresa());
        }

        UsuarioBean user = null;
        int len = listaUsuario.size();
        nInfoID = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaUsuario.size(); i++) {
            user = listaUsuario.get(i);
            nInfoID[i] = user.getID();
            nInfo1[i] = user.getNOMBRES() +" " + user.getAPELLIDO_PATERNO();
            nInfo2[i] = user.getNUM_DOCUMENTO();
            nInfo3[i] = user.getESTADO();
        }
        adapter = new RegistradorActivity.MyAdapter(context, nInfoID, nInfo1,nInfo2,nInfo3);
        lvEmpleado.setAdapter(adapter);
    }
}
