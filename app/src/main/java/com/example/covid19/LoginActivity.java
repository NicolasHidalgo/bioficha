package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import ws.WebService;

import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
