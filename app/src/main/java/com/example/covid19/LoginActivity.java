package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import ws.WebService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button btnIngresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(dsp);
            }
        });
    }

    public void btnTestWS(View view){
        WebService ws = new WebService();
        String urlAccion = "select.php";
        String accion = "SELECT";
        String descripcion = "XXX";
        String query = "call sp_test('" + accion + "','" + descripcion + "');";
        String resultado = ws.WebService(getApplicationContext(),urlAccion,query,"ENFERMEDAD");
        //Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

        /*
        String urlAccion = "update.php";
        String accion = "INSERT";
        String descripcion = "JAIMES PARTECITO";
        String query = "call sp_test('" + accion + "','" + descripcion + "');";
        WebService ws = new WebService();
        String respuesta = ws.WebServiceUpdate(getApplicationContext(),urlAccion,query);
        Toast.makeText(getApplicationContext(), respuesta, Toast.LENGTH_LONG).show();
        */
    }


}
