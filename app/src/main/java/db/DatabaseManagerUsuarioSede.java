package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.RolBean;
import beans.SpinnerBean;
import beans.UsuarioSedeBean;

public class DatabaseManagerUsuarioSede extends DatabaseManager {

    public static final String NOMBRE_TABLA = "USUARIO_SEDE";
    public static final String CN_ID_USUARIO = "ID_USUARIO";
    public static final String CN_ID_SEDE = "ID_SEDE";
    public static final String CN_ID_ROL = "ID_ROL";


    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID_USUARIO + " integer,"
            + CN_ID_SEDE + " integer NULL,"
            + CN_ID_ROL + " integer NULL"
            + ");";

    public DatabaseManagerUsuarioSede(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(UsuarioSedeBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID_USUARIO,obj.getID_USUARIO());
        valores.put(CN_ID_SEDE,obj.getID_SEDE());
        valores.put(CN_ID_ROL,obj.getID_ROL());
        return valores;
    }

    public void insertar(UsuarioSedeBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(UsuarioSedeBean obj) {
        ContentValues valores = generarContentValues(obj);
        String [] args = new String[]{obj.getID_USUARIO()};
        Log.d(NOMBRE_TABLA + "_actualizar",super.getDb().update(NOMBRE_TABLA,valores, CN_ID_USUARIO+ "=?", args)+"");
    }

    @Override
    public void eliminar(String id) {
        super.getDb().delete(NOMBRE_TABLA,CN_ID_USUARIO+ "=?", new String[]{id});
    }

    @Override
    public void eliminarTodo() {
        super.getDb().execSQL("DELETE FROM " + NOMBRE_TABLA + ";");
        Log.d(NOMBRE_TABLA + "_eliminados","Datos borrados");
    }

    @Override
    public Cursor cargar() {
        String [] columnas = new String[]
                {CN_ID_USUARIO,CN_ID_SEDE,CN_ID_ROL};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID_USUARIO,CN_ID_SEDE,CN_ID_ROL};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID_USUARIO + "=?", new String[]{id},null,null,null);
    }


    @Override
    public Boolean compruebaRegistro(String id) {
        boolean existe = true;
        Cursor resultSet = super.getDb().rawQuery("Select * from " + NOMBRE_TABLA + " WHERE " + CN_ID_USUARIO + " =" + id, null);

        if (resultSet.getCount() <= 0)
            existe = false;
        else
            existe = true;

        return existe;
    }

    @Override
    public Boolean verificarRegistros() {
        boolean existe = true;
        Cursor resultSet = super.getDb().rawQuery("Select * from " + NOMBRE_TABLA, null);

        if (resultSet.getCount() <= 0)
            existe = false;
        else
            existe = true;

        return existe;
    }

    public List<UsuarioSedeBean> getList(String Id){
        List<UsuarioSedeBean> list = new ArrayList<>();
        Cursor c = null;

        if (Id == "")
            c = cargar();
        else
            c = cargarById(Id);

        UsuarioSedeBean bean = null;
        while (c.moveToNext()){
            bean = new UsuarioSedeBean();
            bean.setID_USUARIO(c.getString(0));
            bean.setID_SEDE(c.getString(1));
            bean.setID_ROL(c.getString(2));

            list.add(bean);
        }
        return list;
    }

    @Override
    public UsuarioSedeBean get(String id){
        UsuarioSedeBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new UsuarioSedeBean();
            bean.setID_USUARIO(c.getString(0));
            bean.setID_SEDE(c.getString(1));
            bean.setID_ROL(c.getString(2));
        }
        return bean;
    }

    public List<SpinnerBean> getSpinner(){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = cargar();

        while (c.moveToNext()){
            SpinnerBean bean = new SpinnerBean(c.getInt(0),c.getString(1));
            list.add(bean);
        }
        return list;
    }
}
