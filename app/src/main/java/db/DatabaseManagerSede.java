package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.EnfermedadBean;
import beans.SedeBean;
import beans.SpinnerBean;

public class DatabaseManagerSede extends DatabaseManager {

    public static final String NOMBRE_TABLA = "SEDE";
    public static final String CN_ID = "_ID";
    public static final String CN_NOMBRE_SEDE = "NOMBRE_SEDE";
    public static final String CN_ID_EMPRESA = "ID_EMPRESA";
    public static final String CN_DIRECCION = "DIRECCION";
    public static final String CN_ID_DISTRITO = "ID_DISTRITO";
    public static final String CN_LATITUD = "LATITUD";
    public static final String CN_LONGITUD = "LONGITUD";
    public static final String CN_FEC_CREACION = "FEC_CREACION";
    public static final String CN_FEC_ACTUALIZCION = "FEC_ACTUALIZCION";
    public static final String CN_FEC_ELIMINACION = "FEC_ELIMINACION";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY,"
            + CN_NOMBRE_SEDE + " text NULL,"
            + CN_ID_EMPRESA + " integer NULL,"
            + CN_DIRECCION + " text NULL,"
            + CN_ID_DISTRITO + " text NULL,"
            + CN_LATITUD + " float NULL,"
            + CN_LONGITUD + " float NULL,"
            + CN_FEC_CREACION + " datetime NULL,"
            + CN_FEC_ACTUALIZCION + " datetime NULL,"
            + CN_FEC_ELIMINACION + " datetime NULL"
            + ");";

    public DatabaseManagerSede(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(SedeBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID,obj.getID());
        valores.put(CN_NOMBRE_SEDE,obj.getNOMBRE_SEDE());
        valores.put(CN_ID_EMPRESA,obj.getID_EMPRESA());
        valores.put(CN_DIRECCION,obj.getDIRECCION());
        valores.put(CN_ID_DISTRITO,obj.getID_DISTRITO());
        valores.put(CN_LATITUD,obj.getLATITUD());
        valores.put(CN_LONGITUD,obj.getLONGITUD());
        valores.put(CN_FEC_CREACION,obj.getFEC_CREACION());
        valores.put(CN_FEC_ACTUALIZCION,obj.getFEC_ACTUALIZACION());
        valores.put(CN_FEC_ELIMINACION,obj.getFEC_ELIMINACION());
        return valores;
    }

    public void insertar(SedeBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(SedeBean obj) {
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
                {CN_ID,CN_NOMBRE_SEDE};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_NOMBRE_SEDE};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_NOMBRE_SEDE};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID+ " = ?",new String[] { tipo },null,null,null);
    }

    public Cursor cargarPorPermiso(String permiso) {
        String [] columnas = new String[]
                {CN_ID,CN_NOMBRE_SEDE};
        //return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID+ " IN ?",new String[] { "("+permiso+")" },null,null,null);
        String query = "SELECT "+CN_ID+", " + CN_NOMBRE_SEDE +" FROM " + NOMBRE_TABLA + " WHERE " + CN_ID +" IN (" + permiso + ")";
        return super.getDb().rawQuery(query, null);
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

    public List<SedeBean> getList(String tipo){
        List<SedeBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        SedeBean bean = null;
        while (c.moveToNext()){
            bean = new SedeBean();
            bean.setID(c.getString(0));
            bean.setNOMBRE_SEDE(c.getString(1));

            list.add(bean);
        }
        return list;
    }

    @Override
    public EnfermedadBean get(String id){
        EnfermedadBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new EnfermedadBean();
            bean.setID(c.getString(0));
            bean.setDESCRIPCION(c.getString(1));
        }
        return bean;
    }

    public List<SpinnerBean> getSpinner(String permiso){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = cargarPorPermiso(permiso);

        while (c.moveToNext()){
            SpinnerBean bean = new SpinnerBean(c.getInt(0),c.getString(1));
            list.add(bean);
        }
        return list;
    }

    public List<SpinnerBean> getSpinnerAll(){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = cargar();

        while (c.moveToNext()){
            SpinnerBean bean = new SpinnerBean(c.getInt(0),c.getString(1));
            list.add(bean);
        }
        return list;
    }
}
