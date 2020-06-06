package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.EnfermedadBean;
import beans.RolBean;
import beans.SedeBean;
import beans.SpinnerBean;

public class DatabaseManagerRol extends DatabaseManager {
    public static final String NOMBRE_TABLA = "ROL";
    public static final String CN_ID = "_ID";
    public static final String CN_NOM_ROL = "NOM_ROL";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY,"
            + CN_NOM_ROL + " text NULL"
            + ");";

    public DatabaseManagerRol(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(RolBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID,obj.getID());
        valores.put(CN_NOM_ROL,obj.getNOM_ROL());
        return valores;
    }

    public void insertar(RolBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(RolBean obj) {
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
                {CN_ID,CN_NOM_ROL};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_ROL};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }

    public Cursor cargarNotSuperAdmin() {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_ROL};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "!= ?", new String[]{"1"},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_ROL};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID+ " = ?",new String[] { tipo },null,null,null);
    }

    public Cursor cargarPorNombre(String Nombre) {
        String [] columnas = new String[]
                {CN_ID,CN_NOM_ROL};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_NOM_ROL+ " = ? ",new String[] { "'"+ Nombre + "'" },null,null,null);
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

    public List<RolBean> getList(String tipo){
        List<RolBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        RolBean bean = null;
        while (c.moveToNext()){
            bean = new RolBean();
            bean.setID(c.getString(0));
            bean.setNOM_ROL(c.getString(1));

            list.add(bean);
        }
        return list;
    }

    @Override
    public RolBean get(String id){
        RolBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new RolBean();
            bean.setID(c.getString(0));
            bean.setNOM_ROL(c.getString(1));
        }
        return bean;
    }
    public RolBean getByNombre(String nombre){
        RolBean bean = null;
        String SQL = "Select " + CN_ID + ", " + CN_NOM_ROL + " from " + NOMBRE_TABLA + " WHERE " + CN_NOM_ROL + " LIKE '%" + nombre + "%'";
        Cursor c = super.getDb().rawQuery(SQL, null);

        while (c.moveToNext()){
            bean = new RolBean();
            bean.setID(c.getString(0));
            bean.setNOM_ROL(c.getString(1));
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

    public List<SpinnerBean> getSpinnerNotSuperAdmin(){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = cargarNotSuperAdmin();

        while (c.moveToNext()){
            SpinnerBean bean = new SpinnerBean(c.getInt(0),c.getString(1));
            list.add(bean);
        }
        return list;
    }

    public List<SpinnerBean> getSpinnerById(String Id){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = cargarById(Id);

        while (c.moveToNext()){
            SpinnerBean bean = new SpinnerBean(c.getInt(0),c.getString(1));
            list.add(bean);
        }
        return list;
    }
}
