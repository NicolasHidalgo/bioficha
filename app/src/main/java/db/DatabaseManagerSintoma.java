package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.EnfermedadBean;
import beans.SintomaBean;
import beans.SpinnerBean;

public class DatabaseManagerSintoma extends DatabaseManager {

    public static final String NOMBRE_TABLA = "SINTOMA";
    public static final String CN_ID = "_ID";
    public static final String CN_DESCRIPCION = "DESCRIPCION";
    public static final String CN_FEC_CREACION = "FEC_CREACION";
    public static final String CN_FEC_ACTUALIZCION = "FEC_ACTUALIZCION";
    public static final String CN_FEC_ELIMINACION = "FEC_ELIMINACION";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY,"
            + CN_DESCRIPCION + " text NOT NULL,"
            + CN_FEC_CREACION + " datetime NULL,"
            + CN_FEC_ACTUALIZCION + " datetime NULL,"
            + CN_FEC_ELIMINACION + " datetime NULL"
            + ");";

    public DatabaseManagerSintoma(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(SintomaBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID,obj.getID());
        valores.put(CN_DESCRIPCION,obj.getDESCRIPCION());
        valores.put(CN_FEC_CREACION,obj.getFEC_CREACION());
        valores.put(CN_FEC_ACTUALIZCION,obj.getFEC_ACTUALIZACION());
        valores.put(CN_FEC_ELIMINACION,obj.getFEC_ELIMINACION());
        return valores;
    }

    public void insertar(SintomaBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(SintomaBean obj) {
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
                {CN_ID,CN_DESCRIPCION};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_DESCRIPCION};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_DESCRIPCION};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID+ " = ?",new String[] { tipo },null,null,null);
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

    public List<SintomaBean> getList(String tipo){
        List<SintomaBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        SintomaBean bean = null;
        while (c.moveToNext()){
            bean = new SintomaBean();
            bean.setID(c.getString(0));
            bean.setDESCRIPCION(c.getString(1));

            list.add(bean);
        }
        return list;
    }

    @Override
    public SintomaBean get(String id){
        SintomaBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new SintomaBean();
            bean.setID(c.getString(0));
            bean.setDESCRIPCION(c.getString(1));
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
