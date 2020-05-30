package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.PaisBean;
import beans.RolBean;
import beans.SpinnerBean;
import beans.TipoDocumentoBean;

public class DatabaseManagerPais extends DatabaseManager {

    public static final String NOMBRE_TABLA = "PAIS";
    public static final String CN_COD = "_COD";
    public static final String CN_NOMBRE = "NOMBRE";
    public static final String CN_ORDEN = "ORDEN";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_COD + " text PRIMARY KEY,"
            + CN_NOMBRE + " text NULL,"
            + CN_ORDEN + " integer NULL"
            + ");";

    public DatabaseManagerPais(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(PaisBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_COD,obj.getCOD());
        valores.put(CN_NOMBRE,obj.getNOMBRE());
        valores.put(CN_ORDEN,obj.getORDEN());
        return valores;
    }

    public void insertar(PaisBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(PaisBean obj) {
        ContentValues valores = generarContentValues(obj);
        String [] args = new String[]{obj.getCOD()};
        Log.d(NOMBRE_TABLA + "_actualizar",super.getDb().update(NOMBRE_TABLA,valores, CN_COD+ "=?", args)+"");
    }

    @Override
    public void eliminar(String id) {
        super.getDb().delete(NOMBRE_TABLA,CN_COD+ "=?", new String[]{id});
    }

    @Override
    public void eliminarTodo() {
        super.getDb().execSQL("DELETE FROM " + NOMBRE_TABLA + ";");
        Log.d(NOMBRE_TABLA + "_eliminados","Datos borrados");
    }

    @Override
    public Cursor cargar() {
        String [] columnas = new String[]
                {CN_COD,CN_NOMBRE};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_COD,CN_NOMBRE};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_COD + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_COD,CN_NOMBRE};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_NOMBRE+ " = ?",new String[] { tipo },null,null,null);
    }


    @Override
    public Boolean compruebaRegistro(String id) {
        boolean existe = true;
        Cursor resultSet = super.getDb().rawQuery("Select * from " + NOMBRE_TABLA + " WHERE " + CN_COD + " =" + id, null);

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

    public List<PaisBean> getList(String tipo){
        List<PaisBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        PaisBean bean = null;
        while (c.moveToNext()){
            bean = new PaisBean();
            bean.setCOD(c.getString(0));
            bean.setNOMBRE(c.getString(1));
            list.add(bean);
        }
        return list;
    }

    @Override
    public PaisBean get(String id){
        PaisBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new PaisBean();
            bean.setCOD(c.getString(0));
            bean.setNOMBRE(c.getString(1));
        }
        return bean;
    }

    public List<SpinnerBean> getSpinner(){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = cargar();

        SpinnerBean bean = null;
        while (c.moveToNext()){
            bean = new SpinnerBean();
            bean.setCOD(c.getString(0));
            bean.setVALUE(c.getString(1));
            list.add(bean);
        }
        return list;
    }

    public PaisBean getByName(String name){
        PaisBean bean = null;
        Cursor c = cargarPorTipo(name);

        while (c.moveToNext()){
            bean = new PaisBean();
            bean.setCOD(c.getString(0));
            bean.setNOMBRE(c.getString(1));
        }
        return bean;
    }
}
