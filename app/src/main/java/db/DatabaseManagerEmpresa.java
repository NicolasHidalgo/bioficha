package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.BioFichaBean;
import beans.EmpresaBean;
import beans.SpinnerBean;

public class DatabaseManagerEmpresa extends DatabaseManager {

    public static final String NOMBRE_TABLA = "EMPRESA";
    public static final String CN_ID = "_ID";
    public static final String CN_RUC = "RUC";
    public static final String CN_NOM_RAZON_SOCIAL = "NOM_RAZON_SOCIAL";
    public static final String CN_ACT_ECONOMICAS = "ACT_ECONOMICAS";
    public static final String CN_DIRECCION = "DIRECCION";
    public static final String CN_ID_DISTRITO = "ID_DISTRITO";
    public static final String CN_LATITUD = "LATITUD";
    public static final String CN_LONGITUD = "LONGITUD";
    public static final String CN_TELEFONO = "TELEFONO";
    public static final String CN_CORREO = "CORREO";
    public static final String CN_CONTACTO = "CONTACTO";

    public static final String CN_FEC_CREACION = "FEC_CREACION";
    public static final String CN_FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
    public static final String CN_FEC_ELIMINACION = "FEC_ELIMINACION";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY,"
            + CN_RUC + " text NULL,"
            + CN_NOM_RAZON_SOCIAL + " text NULL,"
            + CN_ACT_ECONOMICAS + " text NULL,"
            + CN_DIRECCION + " text NULL,"
            + CN_ID_DISTRITO + " int NULL,"
            + CN_LATITUD + " float NULL,"
            + CN_LONGITUD + " float NULL,"
            + CN_TELEFONO + " text NULL,"
            + CN_CORREO + " text NULL,"
            + CN_CONTACTO + " text NULL,"
            + CN_FEC_CREACION + " datetime NULL,"
            + CN_FEC_ACTUALIZACION + " datetime NULL,"
            + CN_FEC_ELIMINACION + " datetime NULL"
            + ");";

    public DatabaseManagerEmpresa(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(EmpresaBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID,obj.getID());
        valores.put(CN_RUC,obj.getRUC());
        valores.put(CN_NOM_RAZON_SOCIAL,obj.getNOM_RAZON_SOCIAL());
        valores.put(CN_ACT_ECONOMICAS,obj.getACT_ECONOMICAS());
        valores.put(CN_DIRECCION,obj.getDIRECCION());
        valores.put(CN_ID_DISTRITO,obj.getID_DISTRITO());
        valores.put(CN_LATITUD,obj.getLATITUD());
        valores.put(CN_LONGITUD,obj.getLONGITUD());
        valores.put(CN_TELEFONO,obj.getTELEFONO());
        valores.put(CN_CORREO,obj.getCORREO());
        valores.put(CN_CONTACTO,obj.getCONTACTO());
        valores.put(CN_FEC_CREACION,obj.getFEC_CREACION());
        valores.put(CN_FEC_ACTUALIZACION,obj.getFEC_ACTUALIZACION());
        valores.put(CN_FEC_ELIMINACION,obj.getFEC_ELIMINACION());
        return valores;
    }

    public void insertar(EmpresaBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(EmpresaBean obj) {
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
                {CN_ID,CN_RUC,CN_NOM_RAZON_SOCIAL};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_RUC,CN_NOM_RAZON_SOCIAL};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }

    public Cursor cargarByRazonSocial(String RazonSocial) {
        String [] columnas = new String[]
                {CN_ID,CN_RUC,CN_NOM_RAZON_SOCIAL};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_NOM_RAZON_SOCIAL + "=?", new String[]{RazonSocial},null,null,null);
    }

    public Cursor cargarByIdObject(String id) {
        String sql = "SELECT EMP._ID, EMP.RUC, EMP.NOM_RAZON_SOCIAL, EMP.ACT_ECONOMICAS, EMP.DIRECCION, EMP.ID_DISTRITO, EMP.LATITUD, EMP.LONGITUD, EMP.TELEFONO, EMP.CORREO, EMP.CONTACTO, EMP.FEC_CREACION, EMP.FEC_ACTUALIZACION, " +
                " EMP.FEC_ELIMINACION, PRO._ID AS ID_PROVINCIA, DEP._ID AS ID_DEPARTAMENTO FROM " + NOMBRE_TABLA + " EMP INNER JOIN DISTRITO DIS ON DIS._ID = EMP.ID_DISTRITO" +
                " INNER JOIN PROVINCIA PRO ON PRO._ID = DIS.ID_PROVINCIA INNER JOIN DEPARTAMENTO DEP ON DEP._ID = PRO.ID_DEPARTAMENTO WHERE EMP._ID  = " + id;
        Cursor resultSet = super.getDb().rawQuery(sql, null);
        return resultSet;
    }
    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_RUC,CN_NOM_RAZON_SOCIAL};
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

    public List<EmpresaBean> getList(String tipo){
        List<EmpresaBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        EmpresaBean bean = null;
        while (c.moveToNext()){
            bean = new EmpresaBean();
            bean.setID(c.getString(0));
            bean.setRUC(c.getString(1));
            bean.setNOM_RAZON_SOCIAL(c.getString(2));

            list.add(bean);
        }
        return list;
    }

    public EmpresaBean getObject(String id){
        EmpresaBean bean = null;
        Cursor c = cargarByIdObject(id);
        while (c.moveToNext()){
            bean = new EmpresaBean();
            bean.setID(c.getString(0));
            bean.setRUC(c.getString(1));
            bean.setNOM_RAZON_SOCIAL(c.getString(2));
            bean.setACT_ECONOMICAS(c.getString(3));
            bean.setDIRECCION(c.getString(4));
            bean.setID_DISTRITO(c.getString(5));
            bean.setLATITUD(c.getString(6));
            bean.setLONGITUD(c.getString(7));
            bean.setTELEFONO(c.getString(8));
            bean.setCORREO(c.getString(9));
            bean.setCONTACTO(c.getString(10));
            bean.setFEC_CREACION(c.getString(11));
            bean.setFEC_ACTUALIZACION(c.getString(12));
            bean.setFEC_ELIMINACION(c.getString(13));
            bean.setID_PROVINCIA(c.getString(14));
            bean.setID_DEPARTAMENTO(c.getString(15));
        }
        return bean;
    }

    @Override
    public EmpresaBean get(String id){
        EmpresaBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new EmpresaBean();
            bean.setID(c.getString(0));
            bean.setRUC(c.getString(1));
            bean.setNOM_RAZON_SOCIAL(c.getString(2));
        }
        return bean;
    }

    public EmpresaBean getByRazonSocial(String RazonSocial){
        EmpresaBean bean = null;
        Cursor c = cargarByRazonSocial(RazonSocial);

        while (c.moveToNext()){
            bean = new EmpresaBean();
            bean.setID(c.getString(0));
            bean.setRUC(c.getString(1));
            bean.setNOM_RAZON_SOCIAL(c.getString(2));
        }
        return bean;
    }

    public List<SpinnerBean> getSpinner(){
        List<SpinnerBean> list = new ArrayList<>();
        Cursor c = cargar();

        while (c.moveToNext()){
            SpinnerBean bean = new SpinnerBean(c.getInt(0),c.getString(2));
            list.add(bean);
        }
        return list;
    }

}
