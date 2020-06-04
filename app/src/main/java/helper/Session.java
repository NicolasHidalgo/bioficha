package helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setIdUsuario(String Id) {
        prefs.edit().putString("IdUsuario", Id).commit();
    }
    public void setIdEmpresa(String Id) {
        prefs.edit().putString("IdEmpresa", Id).commit();
    }
    public void setNomEmpresa(String Nom) {
        prefs.edit().putString("NomEmpresa", Nom).commit();
    }
    public void setIdSede(String Id) {
        prefs.edit().putString("IdSede", Id).commit();
    }
    public void setNomSede(String Nom) {
        prefs.edit().putString("NomSede", Nom).commit();
    }
    public void setIdEmpleado(String Id){ prefs.edit().putString("IdEmpleado", Id).commit();}
    public void setIdRol(String Id){ prefs.edit().putString("IdRol", Id).commit();}
    public void setNomRol(String NomRol){ prefs.edit().putString("NomRol", NomRol).commit();}

    public String getIdUsuario() {
        String IdUsuario = prefs.getString("IdUsuario","");
        return IdUsuario;
    }
    public String getIdEmpresa() {
        String IdEmpresa = prefs.getString("IdEmpresa","");
        return IdEmpresa;
    }
    public String getNomEmpresa() {
        String NomEmpresa = prefs.getString("NomEmpresa","");
        return NomEmpresa;
    }
    public String getIdSede() {
        String IdUsuario = prefs.getString("IdSede","");
        return IdUsuario;
    }
    public String getNomSede() {
        String NomSede = prefs.getString("NomSede","");
        return NomSede;
    }
    public String getIdEmpleado() {
        String IdEmpleado = prefs.getString("IdEmpleado","");
        return IdEmpleado;
    }
    public String getIdRol() {
        String IdRol = prefs.getString("IdRol","");
        return IdRol;
    }
    public String getNomRol() {
        String NomRol = prefs.getString("NomRol","");
        return NomRol;
    }
}
