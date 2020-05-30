package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.security.IdentityScope;
import java.util.ArrayList;
import java.util.List;

import beans.DistritoBean;
import beans.ProvinciaBean;
import beans.SpinnerBean;

public class DatabaseManagerDistrito extends DatabaseManager {

    public static final String NOMBRE_TABLA = "DISTRITO";
    public static final String CN_ID = "_ID";
    public static final String CN_ID_PROVINCIA = "ID_PROVINCIA";
    public static final String CN_NOM_DISTRITO = "NOM_DISTRITO";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY,"
            + CN_ID_PROVINCIA + " integer NULL,"
            + CN_NOM_DISTRITO + " text NULL"
            + ");";

    public DatabaseManagerDistrito(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(DistritoBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID,obj.getID());
        valores.put(CN_ID_PROVINCIA,obj.getID_PROVINCIA());
        valores.put(CN_NOM_DISTRITO,obj.getNOM_DISTRITO());
        return valores;
    }

    public void insertar(DistritoBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(DistritoBean obj) {
        ContentValues valores = generarContentValues(obj);
        String [] args = new String[]{obj.getID()};
        Log.d(NOMBRE_TABLA + "_actualizar",super.getDb().update(NOMBRE_TABLA,valores, CN_ID+ "=?", args)+"");
    }

    @Override
    public void eliminar(String id) {
        super.getDb().delete(NOMBRE_TABLA,CN_ID+ "=?", new String[]{id});
    }

    @Override
    public void eliminarTodo() {
        super.getDb().execSQL("DELETE FROM " + NOMBRE_TABLA + ";");
        Log.d(NOMBRE_TABLA + "_eliminados","Datos borrados");
    }

    @Override
    public Cursor cargar() {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_DISTRITO};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_DISTRITO};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_DISTRITO};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID_PROVINCIA+ " = ?",new String[] { tipo },null,null,null);
    }


    @Override
    public Boolean compruebaRegistro(String id) {
        boolean existe = true;
        Cursor resultSet = super.getDb().rawQuery("Select * from " + NOMBRE_TABLA + " WHERE " + CN_ID + " =" + id, null);

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

    public List<DistritoBean> getList(String tipo){
        List<DistritoBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        DistritoBean bean = null;
        while (c.moveToNext()){
            bean = new DistritoBean();
            bean.setID(c.getString(0));
            bean.setID_PROVINCIA(c.getString(1));
            bean.setNOM_DISTRITO(c.getString(2));

            list.add(bean);
        }
        return list;
    }
    public List<SpinnerBean> getListSpinner(String tipo){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        SpinnerBean bean = null;
        while (c.moveToNext()){
            bean = new SpinnerBean();
            bean.setID(c.getInt(0));
            bean.setVALUE(c.getString(1));
            list.add(bean);
        }
        return list;
    }

    @Override
    public DistritoBean get(String id){
        DistritoBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new DistritoBean();
            bean.setID(c.getString(0));
            bean.setID_PROVINCIA(c.getString(1));
            bean.setNOM_DISTRITO(c.getString(2));
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
