package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.SpinnerBean;
import beans.UsuarioBean;
import beans.UsuarioSedeBean;

public class DatabaseManagerUsuario extends DatabaseManager {

    public static final String NOMBRE_TABLA = "USUARIO";
    public static final String CN_ID = "_ID";
    public static final String CN_ID_TIPO_DOCUMENTO = "ID_TIPO_DOCUMENTO";
    public static final String CN_NUM_DOCUMENTO = "NUM_DOCUMENTO";
    public static final String CN_COD_PAIS = "COD_PAIS";
    public static final String CN_NOMBRES = "NOMBRES";
    public static final String CN_APELLIDO_PATERNO = "APELLIDO_PATERNO";
    public static final String CN_APELLIDO_MATERNO = "APELLIDO_MATERNO";
    public static final String CN_ID_EMPRESA = "ID_EMPRESA";
    public static final String CN_GENERO = "GENERO";
    public static final String CN_CORREO = "CORREO";
    public static final String CN_FECHA_NACIMIENTO = "FECHA_NACIMIENTO";
    public static final String CN_NOMBRES_CONTACTO = "NOMBRES_CONTACTO";
    public static final String CN_DIRECCION_CONTACTO = "DIRECCION_CONTACTO";
    public static final String CN_TELEFONO_CONTACTO = "TELEFONO_CONTACTO";
    public static final String CN_CORREO_CONTACTO = "CORREO_CONTACTO";
    public static final String CN_USUARIO = "USUARIO";
    public static final String CN_CONTRASENA = "CONTRASENA";
    public static final String CN_ID_ROL = "ID_ROL";
    public static final String CN_FEC_CREACION = "FEC_CREACION";
    public static final String CN_FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
    public static final String CN_FEC_ELIMINACION = "FEC_ELIMINACION";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY,"
            + CN_ID_TIPO_DOCUMENTO + " integer NULL,"
            + CN_NUM_DOCUMENTO + " text NULL,"
            + CN_COD_PAIS + " text NULL,"
            + CN_NOMBRES + " text NULL,"
            + CN_APELLIDO_PATERNO + " text NULL,"
            + CN_APELLIDO_MATERNO + " text NULL,"
            + CN_ID_EMPRESA + " int NULL,"
            + CN_GENERO + " text NULL,"
            + CN_CORREO + " text NULL,"
            + CN_FECHA_NACIMIENTO + " datetime NULL,"
            + CN_NOMBRES_CONTACTO + " text NULL,"
            + CN_DIRECCION_CONTACTO + " text NULL,"
            + CN_TELEFONO_CONTACTO + " text NULL,"
            + CN_CORREO_CONTACTO + " text NULL,"
            + CN_USUARIO + " text NULL,"
            + CN_CONTRASENA + " text NULL,"
            + CN_ID_ROL + " int NULL,"
            + CN_FEC_CREACION + " datetime NULL,"
            + CN_FEC_ACTUALIZACION + " datetime NULL,"
            + CN_FEC_ELIMINACION + " datetime NULL"
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
        valores.put(CN_ID_TIPO_DOCUMENTO,obj.getID_TIPO_DOCUMENTO());
        valores.put(CN_NUM_DOCUMENTO,obj.getNUM_DOCUMENTO());
        valores.put(CN_COD_PAIS,obj.getCOD_PAIS());
        valores.put(CN_NOMBRES,obj.getNOMBRES());
        valores.put(CN_APELLIDO_PATERNO,obj.getAPELLIDO_PATERNO());
        valores.put(CN_APELLIDO_MATERNO,obj.getAPELLIDO_MATERNO());
        valores.put(CN_ID_EMPRESA,obj.getID_EMPRESA());
        valores.put(CN_GENERO,obj.getGENERO());
        valores.put(CN_CORREO,obj.getCORREO());
        valores.put(CN_FECHA_NACIMIENTO,obj.getFECHA_NACIMIENTO());
        valores.put(CN_NOMBRES_CONTACTO,obj.getNOMBRES_CONTACTO());
        valores.put(CN_DIRECCION_CONTACTO,obj.getDIRECCION_CONTACTO());
        valores.put(CN_TELEFONO_CONTACTO,obj.getTELEFONO_CONTACTO());
        valores.put(CN_CORREO_CONTACTO,obj.getCORREO_CONTACTO());
        valores.put(CN_USUARIO,obj.getUSUARIO());
        valores.put(CN_CONTRASENA,obj.getCONTRASENA());
        valores.put(CN_ID_ROL,obj.getID_ROL());
        valores.put(CN_FEC_CREACION,obj.getFEC_CREACION());
        valores.put(CN_FEC_ACTUALIZACION,obj.getFEC_ACTUALIZACION());
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
                {CN_ID,CN_ID_TIPO_DOCUMENTO,CN_NUM_DOCUMENTO,CN_COD_PAIS,CN_NOMBRES,CN_APELLIDO_PATERNO,CN_APELLIDO_MATERNO,
                        CN_ID_EMPRESA,CN_GENERO,CN_CORREO,CN_FECHA_NACIMIENTO,CN_NOMBRES_CONTACTO,CN_DIRECCION_CONTACTO,CN_TELEFONO_CONTACTO,
                CN_CORREO_CONTACTO,CN_USUARIO,CN_CONTRASENA,CN_ID_ROL,CN_FEC_CREACION,CN_FEC_ACTUALIZACION,CN_FEC_ELIMINACION};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    public Cursor cargarPorTipoDocumentoNumDocumento(String TipoDocumento, String NumDocumento) {
        String [] columnas = new String[]
                {CN_ID,CN_ID_TIPO_DOCUMENTO,CN_NUM_DOCUMENTO,CN_COD_PAIS,CN_NOMBRES,CN_APELLIDO_PATERNO,CN_APELLIDO_MATERNO,
                        CN_ID_EMPRESA,CN_GENERO,CN_CORREO,CN_FECHA_NACIMIENTO,CN_NOMBRES_CONTACTO,CN_DIRECCION_CONTACTO,CN_TELEFONO_CONTACTO,
                        CN_CORREO_CONTACTO,CN_USUARIO,CN_CONTRASENA,CN_ID_ROL,CN_FEC_CREACION,CN_FEC_ACTUALIZACION,CN_FEC_ELIMINACION};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID_TIPO_DOCUMENTO + " =? AND " + CN_NUM_DOCUMENTO + " =?", new String[]{TipoDocumento,NumDocumento},null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_ID_TIPO_DOCUMENTO,CN_NUM_DOCUMENTO,CN_COD_PAIS,CN_NOMBRES,CN_APELLIDO_PATERNO,CN_APELLIDO_MATERNO,
                        CN_ID_EMPRESA,CN_GENERO,CN_CORREO,CN_FECHA_NACIMIENTO,CN_NOMBRES_CONTACTO,CN_DIRECCION_CONTACTO,CN_TELEFONO_CONTACTO,
                        CN_CORREO_CONTACTO,CN_USUARIO,CN_CONTRASENA,CN_ID_ROL,CN_FEC_CREACION,CN_FEC_ACTUALIZACION,CN_FEC_ELIMINACION};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_ID_TIPO_DOCUMENTO,CN_NUM_DOCUMENTO,CN_COD_PAIS,CN_NOMBRES,CN_APELLIDO_PATERNO,CN_APELLIDO_MATERNO,
                        CN_ID_EMPRESA,CN_GENERO,CN_CORREO,CN_FECHA_NACIMIENTO,CN_NOMBRES_CONTACTO,CN_DIRECCION_CONTACTO,CN_TELEFONO_CONTACTO,
                        CN_CORREO_CONTACTO,CN_USUARIO,CN_CONTRASENA,CN_ID_ROL,CN_FEC_CREACION,CN_FEC_ACTUALIZACION,CN_FEC_ELIMINACION};
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

    public Boolean verificarPorUsuarioPassword(String usuario, String password) {
        boolean existe = true;
        Cursor resultSet = super.getDb().rawQuery("Select * from " + NOMBRE_TABLA + " WHERE " + CN_USUARIO + " =" + usuario + " AND " + CN_CONTRASENA + " =" + password, null);

        if (resultSet.getCount() <= 0)
            existe = false;
        else
            existe = true;

        return existe;
    }

    public void eliminarPorUsuarioPassword(String usuario, String password) {
        super.getDb().delete(NOMBRE_TABLA,CN_USUARIO + "=? AND " + CN_CONTRASENA + "=?" , new String[]{usuario,password});
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


    public Boolean verificarRegistroPorID(String Id) {
        boolean existe = true;
        Cursor resultSet = super.getDb().rawQuery("Select * from " + NOMBRE_TABLA + " WHERE " + CN_ID + " = " + Id, null);

        if (resultSet.getCount() > 0)
            existe = true;
        else
            existe = false;

        return existe;
    }

    @Override
    public UsuarioBean get(String id){
        UsuarioBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new UsuarioBean();
            bean.setID(c.getString(0));
            bean.setID_TIPO_DOCUMENTO(c.getString(1));
            bean.setNUM_DOCUMENTO(c.getString(2));
            bean.setCOD_PAIS(c.getString(3));
            bean.setNOMBRES(c.getString(4));
            bean.setAPELLIDO_PATERNO(c.getString(5));
            bean.setAPELLIDO_MATERNO(c.getString(6));
            bean.setID_EMPRESA(c.getString(7));
            bean.setGENERO(c.getString(8));
            bean.setCORREO(c.getString(9));
            bean.setFECHA_NACIMIENTO(c.getString(10));
            bean.setNOMBRES_CONTACTO(c.getString(11));
            bean.setDIRECCION_CONTACTO(c.getString(12));
            bean.setTELEFONO_CONTACTO(c.getString(13));
            bean.setCORREO_CONTACTO(c.getString(14));
            bean.setUSUARIO(c.getString(15));
            bean.setCONTRASENA(c.getString(16));
            bean.setID_ROL(c.getString(17));
            bean.setFEC_CREACION(c.getString(18));
            bean.setFEC_ACTUALIZACION(c.getString(19));
            bean.setFEC_ELIMINACION(c.getString(20));
        }
        return bean;
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
            bean.setID_TIPO_DOCUMENTO(c.getString(1));
            bean.setNUM_DOCUMENTO(c.getString(2));
            bean.setCOD_PAIS(c.getString(3));
            bean.setNOMBRES(c.getString(4));
            bean.setAPELLIDO_PATERNO(c.getString(5));
            bean.setAPELLIDO_MATERNO(c.getString(6));
            bean.setID_EMPRESA(c.getString(7));
            bean.setGENERO(c.getString(8));
            bean.setCORREO(c.getString(9));
            bean.setFECHA_NACIMIENTO(c.getString(10));
            bean.setNOMBRES_CONTACTO(c.getString(11));
            bean.setDIRECCION_CONTACTO(c.getString(12));
            bean.setTELEFONO_CONTACTO(c.getString(13));
            bean.setCORREO_CONTACTO(c.getString(14));
            bean.setUSUARIO(c.getString(15));
            bean.setCONTRASENA(c.getString(16));
            bean.setID_ROL(c.getString(17));
            bean.setFEC_CREACION(c.getString(18));
            bean.setFEC_ACTUALIZACION(c.getString(19));
            bean.setFEC_ELIMINACION(c.getString(20));

            list.add(bean);
        }
        return list;
    }

    public UsuarioBean getPorTipoDocumentoNumDocumento(String TipoDocumento, String NumDocumento){
        UsuarioBean bean = null;
        Cursor c = cargarPorTipoDocumentoNumDocumento(TipoDocumento, NumDocumento);

        while (c.moveToNext()){
            bean = new UsuarioBean();
            bean.setID(c.getString(0));
            bean.setID_TIPO_DOCUMENTO(c.getString(1));
            bean.setNUM_DOCUMENTO(c.getString(2));
            bean.setCOD_PAIS(c.getString(3));
            bean.setNOMBRES(c.getString(4));
            bean.setAPELLIDO_PATERNO(c.getString(5));
            bean.setAPELLIDO_MATERNO(c.getString(6));
            bean.setID_EMPRESA(c.getString(7));
            bean.setGENERO(c.getString(8));
            bean.setCORREO(c.getString(9));
            bean.setFECHA_NACIMIENTO(c.getString(10));
            bean.setNOMBRES_CONTACTO(c.getString(11));
            bean.setDIRECCION_CONTACTO(c.getString(12));
            bean.setTELEFONO_CONTACTO(c.getString(13));
            bean.setCORREO_CONTACTO(c.getString(14));
            bean.setUSUARIO(c.getString(15));
            bean.setCONTRASENA(c.getString(16));
            bean.setID_ROL(c.getString(17));
            bean.setFEC_CREACION(c.getString(18));
            bean.setFEC_ACTUALIZACION(c.getString(19));
            bean.setFEC_ELIMINACION(c.getString(20));
        }
        return bean;
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
