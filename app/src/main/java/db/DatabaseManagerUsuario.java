package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.SpinnerBean;
import beans.UsuarioBean;

public class DatabaseManagerUsuario extends DatabaseManager {

    public static final String NOMBRE_TABLA = "USUARIO";
    public static final String CN_ID = "_ID";
    public static final String CN_NOMBRES = "NOMBRES";
    public static final String CN_APELLIDOS = "APELLIDOS";
    public static final String CN_ID_EMPRESA = "ID_EMPRESA";
    public static final String CN_CORREO = "CORREO";
    public static final String CN_USUARIO = "USUARIO";
    public static final String CN_CONTRASENA = "CONTRASENA";
    public static final String CN_ID_ROL = "ID_ROL";
    public static final String CN_FEC_CREACION = "FEC_CREACION";
    public static final String CN_FEC_ACTUALIZCION = "FEC_ACTUALIZCION";
    public static final String CN_FEC_ELIMINACION = "FEC_ELIMINACION";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY,"
            + CN_NOMBRES + " text NOT NULL,"
            + CN_APELLIDOS + " text NOT NULL,"
            + CN_ID_EMPRESA + " int NOT NULL,"
            + CN_CORREO + " text NOT NULL,"
            + CN_USUARIO + " text NOT NULL,"
            + CN_CONTRASENA + " text NOT NULL,"
            + CN_ID_ROL + " int NOT NULL,"
            + CN_FEC_CREACION + " datetime NOT NULL,"
            + CN_FEC_ACTUALIZCION + " datetime NOT NULL,"
            + CN_FEC_ELIMINACION + " datetime NOT NULL"
            + ");";

    public DatabaseManagerUsuario(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(UsuarioBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID,obj.getID());
        valores.put(CN_NOMBRES,obj.getNOMBRES());
        valores.put(CN_APELLIDOS,obj.getAPELLIDOS());
        valores.put(CN_ID_EMPRESA,obj.getID_EMPRESA());
        valores.put(CN_CORREO,obj.getCORREO());
        valores.put(CN_USUARIO,obj.getUSUARIO());
        valores.put(CN_CONTRASENA,obj.getCONTRASENA());
        valores.put(CN_ID_ROL,obj.getID_ROL());
        valores.put(CN_FEC_CREACION,obj.getFEC_CREACION());
        valores.put(CN_FEC_ACTUALIZCION,obj.getFEC_ACTUALIZCION());
        valores.put(CN_FEC_ELIMINACION,obj.getFEC_ELIMINACION());
        return valores;
    }

    public void insertar(UsuarioBean obj) {
        //super.getDb().execSQL("INSERT INTO ...");
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(UsuarioBean obj) {
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
                {CN_ID,CN_NOMBRES,CN_APELLIDOS};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_NOMBRES,CN_APELLIDOS};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_NOMBRES,CN_APELLIDOS};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID_EMPRESA+ " = ?",new String[] { tipo },null,null,null);
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

    public List<UsuarioBean> getList(String tipo){
        List<UsuarioBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        while (c.moveToNext()){
            UsuarioBean bean = new UsuarioBean();
            bean.setID(c.getString(0));
            bean.setNOMBRES(c.getString(1));
            bean.setAPELLIDOS(c.getString(2));

            list.add(bean);
        }
        return list;
    }

    public List<SpinnerBean> getSpinner(String tipo){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = cargarPorTipo(tipo);

        while (c.moveToNext()){
            SpinnerBean bean = new SpinnerBean(c.getInt(0),c.getString(2));
            list.add(bean);
        }
        return list;
    }
}
