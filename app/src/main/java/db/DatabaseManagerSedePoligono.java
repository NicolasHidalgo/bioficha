package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.SedePoligonoBean;
import beans.SpinnerBean;
import beans.UsuarioSedeBean;

public class DatabaseManagerSedePoligono extends DatabaseManager {

    public static final String NOMBRE_TABLA = "SEDE_POLIGONO";
    public static final String CN_ID = "_ID";
    public static final String CN_LATITUD = "LATITUD";
    public static final String CN_LONGITUD = "LONGITUD";
    public static final String CN_ID_SEDE = "ID_SEDE";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer,"
            + CN_LATITUD + " double NULL,"
            + CN_LONGITUD + " double NULL,"
            + CN_ID_SEDE + " integer NULL"
            + ");";

    public DatabaseManagerSedePoligono(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(SedePoligonoBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID,obj.getID());
        valores.put(CN_LATITUD,obj.getLATITUD());
        valores.put(CN_LONGITUD,obj.getLONGITUD());
        valores.put(CN_ID_SEDE,obj.getID_SEDE());
        return valores;
    }

    public void insertar(SedePoligonoBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(SedePoligonoBean obj) {
        ContentValues valores = generarContentValues(obj);
        String [] args = new String[]{obj.getID()};
        Log.d(NOMBRE_TABLA + "_actualiza",super.getDb().update(NOMBRE_TABLA,valores, CN_ID+ "=?", args)+"");
    }

    @Override
    public void eliminar(String id) {
        super.getDb().delete(NOMBRE_TABLA,CN_ID+ "=?", new String[]{id});
    }

    public void eliminarPorSede(String IdSede) {
        super.getDb().delete(NOMBRE_TABLA,CN_ID_SEDE+ "=?", new String[]{IdSede});
    }

    @Override
    public void eliminarTodo() {
        super.getDb().execSQL("DELETE FROM " + NOMBRE_TABLA + ";");
        Log.d(NOMBRE_TABLA + "_eliminado","Datos borrados");
    }

    @Override
    public Cursor cargar() {
        String [] columnas = new String[]
                {CN_ID,CN_LATITUD,CN_LONGITUD,CN_ID_SEDE};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_LATITUD,CN_LONGITUD,CN_ID_SEDE};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_LATITUD,CN_LONGITUD,CN_ID_SEDE};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID_SEDE+ " = ?",new String[] { tipo },null,null,null);
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

    public List<SedePoligonoBean> getList(String tipo){
        List<SedePoligonoBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        SedePoligonoBean bean = null;
        while (c.moveToNext()){
            bean = new SedePoligonoBean();
            bean.setID(c.getString(0));
            bean.setLATITUD(c.getDouble(1));
            bean.setLONGITUD(c.getDouble(2));
            bean.setID_SEDE(c.getString(3));
            list.add(bean);
        }
        return list;
    }

    @Override
    public SedePoligonoBean get(String id){
        SedePoligonoBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new SedePoligonoBean();
            bean.setID(c.getString(0));
            bean.setLATITUD(c.getDouble(1));
            bean.setLONGITUD(c.getDouble(2));
            bean.setID_SEDE(c.getString(3));
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
