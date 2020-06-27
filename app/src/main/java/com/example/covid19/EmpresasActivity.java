package com.example.covid19;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import beans.EmpresaBean;
import beans.UsuarioBean;
import db.DatabaseManagerEmpresa;
import db.DatabaseManagerUsuario;
import helper.ConnectivityReceiver;
import helper.Session;

public class EmpresasActivity extends AppCompatActivity {

    Context context;
    Session session;
    DatabaseManagerEmpresa dbEmpresa;
    List<EmpresaBean> listaEmpresa;
    ListView lvEmpresa;
    RequestQueue requestQueue;
    Button btnExportarExcel;
    final String URL = "https://bioficha.electocandidato.com/insert_id.php";

    String nInfoID[];
    String nInfo1[];
    String nInfo2[];
    String nInfo3[];

    FloatingActionButton btnAgregarEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        context = this;
        session = new Session(context);
        dbEmpresa = new DatabaseManagerEmpresa(context);

        if (session.getNomRol().equals("SUPER-ADMIN")) {
            //lISTAR TODAS LAS EMPRESAS
            listaEmpresa = dbEmpresa.getList("");
        } else if (session.getNomRol().equals("ADMIN")) {
            // LISTAR SOLO SU EMPRESA
            listaEmpresa = dbEmpresa.getList(session.getIdEmpresa());
        }

        EmpresaBean user = null;
        int len = listaEmpresa.size();
        nInfoID = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i = 0; i < listaEmpresa.size(); i++) {
            user = listaEmpresa.get(i);
            nInfoID[i] = user.getID();
            nInfo1[i] = user.getNOM_RAZON_SOCIAL();
            nInfo2[i] = user.getRUC();
            nInfo3[i] = "x";
        }

        btnAgregarEmpresa = findViewById(R.id.btnAgregarEmpresa);
        btnExportarExcel = findViewById(R.id.btnExportarExcel);

        if (session.getNomRol().equals("ADMIN")) {
            btnAgregarEmpresa.hide();
        }

        lvEmpresa = findViewById(R.id.lvEmpresa);
        EmpresasActivity.MyAdapter adapter = new EmpresasActivity.MyAdapter(this, nInfoID, nInfo1, nInfo2, nInfo3);
        lvEmpresa.setAdapter(adapter);

        btnExportarExcel.setVisibility(View.INVISIBLE);
        if (session.getNomRol().equals("ADMIN")) {
            btnExportarExcel.setVisibility(View.VISIBLE);
        }

        btnExportarExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(ConnectivityReceiver.isConnected(context))) {
                    Toast.makeText(context, "Necesita contectarte a internet para continuar", Toast.LENGTH_LONG).show();
                    return;
                }
                String IdEmpresa = session.getIdEmpresa();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bioficha.electocandidato.com/exportar.php?ID_EMPRESA=" + IdEmpresa));
                startActivity(browserIntent);
            }
        });

        btnAgregarEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(EmpresasActivity.this, RegistroEmpresasActivity.class);
                session.setIdEmpresa("");
                startActivity(dsp);
            }
        });

        lvEmpresa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ide = (String) ((TextView) view.findViewById(R.id.txtInfoEmpresaId)).getText();
                session.setIdEmpresa(ide);
                Intent intent = new Intent(context, RegistroEmpresasActivity.class);
                startActivity(intent);
            }
        });
        lvEmpresa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String pId = (String) ((TextView) view.findViewById(R.id.txtInfoEmpresaId)).getText();
                String pAccion = "DELETE";
                final String QUERY = "call SP_UPDATE_EMPRESA('" + pAccion + "', " + pId + ",'','','','','','','','','','');";
                final String finalAccion = pAccion;
                final String finalId = pId;

                AlertDialog.Builder builder = new AlertDialog.Builder(EmpresasActivity.this);

                // Set a title for alert dialog
                builder.setTitle("ALERTA");

                // Ask the final question
                builder.setMessage("¿Estás seguro de eliminar esta empresa?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:


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

        if(session.getNomRol().equals("SUPER-ADMIN")){
            //lISTAR TODAS LAS EMPRESAS
            listaEmpresa = dbEmpresa.getList("");
        }else if(session.getNomRol().equals("ADMIN")){
            // LISTAR SOLO SU EMPRESA
            listaEmpresa = dbEmpresa.getList(session.getIdEmpresa());
        }

        EmpresaBean user = null;
        int len = listaEmpresa.size();
        nInfoID = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaEmpresa.size(); i++) {
            user = listaEmpresa.get(i);
            nInfoID[i] = user.getID();
            nInfo1[i] = user.getNOM_RAZON_SOCIAL();
            nInfo2[i] = user.getRUC();
            nInfo3[i] = user.getFEC_CREACION();
        }
        btnAgregarEmpresa = findViewById(R.id.btnAgregarEmpresa);
        lvEmpresa = findViewById(R.id.lvEmpresa);
        EmpresasActivity.MyAdapter adapter = new EmpresasActivity.MyAdapter(this, nInfoID, nInfo1, nInfo2, nInfo3);
        lvEmpresa.setAdapter(adapter);

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
    class MyAdapter extends ArrayAdapter<String> {
        Context context;

        String nID[];
        String nInfo1[];
        String nInfo2[];
        String nInfo3[];

        MyAdapter(Context c, String id[], String nInfo1[], String nInfo2[], String nInfo3[]){
            super(c,R.layout.row_empresa, R.id.txtInfoEmpresaId, id);
            this.context = c;
            this.nID = id;
            this.nInfo1 = nInfo1;
            this.nInfo2 = nInfo2;
            this.nInfo3 = nInfo3;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_empresa, parent, false);
            TextView id = row.findViewById(R.id.txtInfoEmpresaId);
            TextView tit = row.findViewById(R.id.txtInfoEmpresa1);
            TextView sub = row.findViewById(R.id.txtInfoEmpresa2);
            TextView inf = row.findViewById(R.id.txtInfoEmpresa3);

            id.setText(nID[position]);
            tit.setText(nInfo1[position]);
            sub.setText(nInfo2[position]);
            inf.setText(nInfo3[position]);

            return row;
        }
    }

}
