package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.RolBean;
import beans.SpinnerBean;
import beans.TipoDocumentoBean;

public class DatabaseManagerTipoDocumento extends DatabaseManager {

    public static final String NOMBRE_TABLA = "TIPO_DOCUMENTO";
    public static final String CN_ID = "_ID";
    public static final String CN_NOM_DOCUMENTO = "NOM_DOCUMENTO";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY,"
            + CN_NOM_DOCUMENTO + " text NULL"
            + ");";

    public DatabaseManagerTipoDocumento(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(TipoDocumentoBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID,obj.getID());
        valores.put(CN_NOM_DOCUMENTO,obj.getNOM_DOCUMENTO());
        return valores;
    }

    public void insertar(TipoDocumentoBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(TipoDocumentoBean obj) {
        ContentValues valores = generarContentValues(obj);
        String [] args = new String[]{obj.getID()};
        Log.d(NOMBRE_TABLA + "_actualiz",super.getDb().update(NOMBRE_TABLA,valores, CN_ID+ "=?", args)+"");
    }

    @Override
    public void eliminar(String id) {
        super.getDb().delete(NOMBRE_TABLA,CN_ID+ "=?", new String[]{id});
    }

    @Override
    public void eliminarTodo() {
        super.getDb().execSQL("DELETE FROM " + NOMBRE_TABLA + ";");
        Log.d(NOMBRE_TABLA + "_eliminad","Datos borrados");
    }

    @Override
    public Cursor cargar() {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_DOCUMENTO};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_DOCUMENTO};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_DOCUMENTO};
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

    public List<TipoDocumentoBean> getList(String tipo){
        List<TipoDocumentoBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        TipoDocumentoBean bean = null;
        while (c.moveToNext()){
            bean = new TipoDocumentoBean();
            bean.setID(c.getString(0));
            bean.setNOM_DOCUMENTO(c.getString(1));

            list.add(bean);
        }
        return list;
    }

    @Override
    public TipoDocumentoBean get(String id){
        TipoDocumentoBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new TipoDocumentoBean();
            bean.setID(c.getString(0));
            bean.setNOM_DOCUMENTO(c.getString(1));
        }
        return bean;
    }

    public List<SpinnerBean> getSpinner(){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = cargar();

        SpinnerBean bean = null;
        while (c.moveToNext()){
            bean = new SpinnerBean(c.getInt(0),c.getString(1));
            list.add(bean);
        }
        return list;
    }
}
