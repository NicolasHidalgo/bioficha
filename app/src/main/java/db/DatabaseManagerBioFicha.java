package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.BioFichaBean;
import beans.EnfermedadBean;
import beans.SpinnerBean;

public class DatabaseManagerBioFicha extends DatabaseManager {

    public static final String NOMBRE_TABLA = "BIO_FICHA";
    public static final String CN_ID = "_ID";
    public static final String CN_ID_SEDE = "ID_SEDE";
    public static final String CN_ID_TIPO_DOCUMENTO = "ID_TIPO_DOCUMENTO";
    public static final String CN_NUM_DOCUMENTO = "NUM_DOCUMENTO";
    public static final String CN_COD_PAIS = "COD_PAIS";
    public static final String CN_NOMBRES = "NOMBRES";
    public static final String CN_APELLIDO_PATERNO = "APELLIDO_PATERNO";
    public static final String CN_APELLIDO_MATERNO = "APELLIDO_MATERNO";
    public static final String CN_FECHA_NACIMIENTO = "FECHA_NACIMIENTO";
    public static final String CN_GENERO = "GENERO";
    public static final String CN_ESTATURA = "ESTATURA";
    public static final String CN_PESO = "PESO";
    public static final String CN_IMC = "IMC";
    public static final String CN_GRADO_CELSIUS = "GRADO_CELSIUS";
    public static final String CN_MENSAJE_ESTADO = "MENSAJE_ESTADO";
    public static final String CN_LATITUD = "LATITUD";
    public static final String CN_LONGITUD = "LONGITUD";
    public static final String CN_OTRO_SINTOMA = "OTRO_SINTOMA";

    public static final String CN_FEC_CREACION = "FEC_CREACION";
    public static final String CN_FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
    public static final String CN_FEC_ELIMINACION = "FEC_ELIMINACION";

    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY,"
            + CN_ID_SEDE + " integer NULL,"
            + CN_ID_TIPO_DOCUMENTO + " integer NULL,"
            + CN_NUM_DOCUMENTO + " text NULL,"
            + CN_COD_PAIS + " text NULL,"
            + CN_NOMBRES + " text NULL,"
            + CN_APELLIDO_PATERNO + " text NULL,"
            + CN_APELLIDO_MATERNO + " text NULL,"
            + CN_FECHA_NACIMIENTO + " datetime NULL,"
            + CN_GENERO + " text NULL,"
            + CN_ESTATURA + " float NULL,"
            + CN_PESO + " float NULL,"
            + CN_IMC + " float NULL,"
            + CN_GRADO_CELSIUS + " integer NULL,"
            + CN_MENSAJE_ESTADO + " text NULL,"
            + CN_LATITUD + " double NULL,"
            + CN_LONGITUD + " double NULL,"
            + CN_OTRO_SINTOMA + " text NULL,"
            + CN_FEC_CREACION + " datetime NULL,"
            + CN_FEC_ACTUALIZACION + " datetime NULL,"
            + CN_FEC_ELIMINACION + " datetime NULL"
            + ");";

    public DatabaseManagerBioFicha(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(BioFichaBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID,obj.getID());
        valores.put(CN_ID_SEDE,obj.getID_SEDE());
        valores.put(CN_ID_TIPO_DOCUMENTO,obj.getID_TIPO_DOCUMENTO());
        valores.put(CN_NUM_DOCUMENTO,obj.getNUM_DOCUMENTO());
        valores.put(CN_COD_PAIS,obj.getCOD_PAIS());
        valores.put(CN_NOMBRES,obj.getNOMBRES());
        valores.put(CN_APELLIDO_PATERNO,obj.getAPELLIDO_PATERNO());
        valores.put(CN_APELLIDO_MATERNO,obj.getAPELLIDO_MATERNO());
        valores.put(CN_FECHA_NACIMIENTO,obj.getFECHA_NACIMIENTO());
        valores.put(CN_GENERO,obj.getGENERO());
        valores.put(CN_ESTATURA,obj.getESTATURA());
        valores.put(CN_PESO,obj.getPESO());
        valores.put(CN_IMC,obj.getIMC());
        valores.put(CN_GRADO_CELSIUS,obj.getGRADO_CELSIUS());
        valores.put(CN_MENSAJE_ESTADO,obj.getMENSAJE_ESTADO());
        valores.put(CN_LATITUD,obj.getLATITUD());
        valores.put(CN_LONGITUD,obj.getLONGITUD());
        valores.put(CN_OTRO_SINTOMA,obj.getOTRO_SINTOMA());
        valores.put(CN_FEC_CREACION,obj.getFEC_CREACION());
        valores.put(CN_FEC_ACTUALIZACION,obj.getFEC_ACTUALIZACION());
        valores.put(CN_FEC_ELIMINACION,obj.getFEC_ELIMINACION());
        return valores;
    }

    public void insertar(BioFichaBean obj) {
        Log.d(NOMBRE_TABLA + "_insertar",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(BioFichaBean obj) {
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
                {CN_ID,CN_ID_SEDE,CN_ID_TIPO_DOCUMENTO,CN_NUM_DOCUMENTO,CN_COD_PAIS,CN_NOMBRES,CN_APELLIDO_PATERNO,CN_APELLIDO_MATERNO
                        ,CN_FECHA_NACIMIENTO,CN_GENERO,CN_ESTATURA,CN_PESO,CN_IMC,CN_GRADO_CELSIUS,CN_MENSAJE_ESTADO,CN_LATITUD,CN_LONGITUD
                        ,CN_OTRO_SINTOMA,CN_FEC_CREACION,CN_FEC_ACTUALIZACION,CN_FEC_ELIMINACION};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID,CN_ID_SEDE,CN_ID_TIPO_DOCUMENTO,CN_NUM_DOCUMENTO,CN_COD_PAIS,CN_NOMBRES,CN_APELLIDO_PATERNO,CN_APELLIDO_MATERNO
                        ,CN_FECHA_NACIMIENTO,CN_GENERO,CN_ESTATURA,CN_PESO,CN_IMC,CN_GRADO_CELSIUS,CN_MENSAJE_ESTADO,CN_LATITUD,CN_LONGITUD
                        ,CN_OTRO_SINTOMA,CN_FEC_CREACION,CN_FEC_ACTUALIZACION,CN_FEC_ELIMINACION};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID,CN_ID_SEDE,CN_ID_TIPO_DOCUMENTO,CN_NUM_DOCUMENTO,CN_COD_PAIS,CN_NOMBRES,CN_APELLIDO_PATERNO,CN_APELLIDO_MATERNO
                        ,CN_FECHA_NACIMIENTO,CN_GENERO,CN_ESTATURA,CN_PESO,CN_IMC,CN_GRADO_CELSIUS,CN_MENSAJE_ESTADO,CN_LATITUD,CN_LONGITUD
                        ,CN_OTRO_SINTOMA,CN_FEC_CREACION,CN_FEC_ACTUALIZACION,CN_FEC_ELIMINACION};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID+ " = ?",new String[] { tipo },null,null,null);
    }
    public Cursor cargarPorSedeFecha(String IdSede, String Fecha) {
        String [] columnas = new String[]
                {CN_ID,CN_ID_SEDE,CN_ID_TIPO_DOCUMENTO,CN_NUM_DOCUMENTO,CN_COD_PAIS,CN_NOMBRES,CN_APELLIDO_PATERNO,CN_APELLIDO_MATERNO
                        ,CN_FECHA_NACIMIENTO,CN_GENERO,CN_ESTATURA,CN_PESO,CN_IMC,CN_GRADO_CELSIUS,CN_MENSAJE_ESTADO,CN_LATITUD,CN_LONGITUD
                        ,CN_OTRO_SINTOMA,CN_FEC_CREACION,CN_FEC_ACTUALIZACION,CN_FEC_ELIMINACION};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID_SEDE+ " = ? AND strftime('%Y-%m-%d'," + CN_FEC_CREACION + ") = ?",new String[] { IdSede, Fecha },null,null,null);
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

    public Boolean VerificarRegistroPorDia(String tipoDocumento, String numDocumento, String fechaRegistro) {
        boolean existe = true;
        String sql = "Select * from " + NOMBRE_TABLA + " WHERE " + CN_ID_TIPO_DOCUMENTO + " = '" + tipoDocumento + "' AND " + CN_NUM_DOCUMENTO + " = '" + numDocumento + "' AND strftime('%Y-%m-%d'," + CN_FEC_CREACION + ") = '" + fechaRegistro + "'";
        Cursor resultSet = super.getDb().rawQuery(sql, null);

        if (resultSet.getCount() > 0)
            existe = true;
        else
            existe = false;

        return existe;
    }

    @Override
    public List<BioFichaBean> get(String tipo){
        List<BioFichaBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        try{
            BioFichaBean bean = null;
            while (c.moveToNext()){
                bean = new BioFichaBean();
                bean.setID(c.getString(0));
                bean.setID_SEDE(c.getString(1));
                bean.setID_TIPO_DOCUMENTO(c.getString(2));
                bean.setNUM_DOCUMENTO(c.getString(3));
                bean.setCOD_PAIS(c.getString(4));
                bean.setNOMBRES(c.getString(5));
                bean.setAPELLIDO_PATERNO(c.getString(6));
                bean.setAPELLIDO_MATERNO(c.getString(7));
                bean.setFECHA_NACIMIENTO(c.getString(8));
                bean.setGENERO(c.getString(9));
                bean.setESTATURA(c.getString(10));
                bean.setPESO(c.getString(11));
                bean.setIMC(c.getString(12));
                bean.setGRADO_CELSIUS(c.getString(13));
                bean.setMENSAJE_ESTADO(c.getString(14));
                bean.setLATITUD(c.getDouble(15));
                bean.setLONGITUD(c.getDouble(16));
                bean.setOTRO_SINTOMA(c.getString(17));
                bean.setFEC_CREACION(c.getString(18));
                bean.setFEC_ACTUALIZACION(c.getString(19));
                bean.setFEC_ELIMINACION(c.getString(20));

                list.add(bean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public List<BioFichaBean> ListarPorFecha(String IdSede, String Fecha){
        List<BioFichaBean> list = new ArrayList<>();
        Cursor c = cargarPorSedeFecha(IdSede, Fecha);

        try{
            BioFichaBean bean = null;
            while (c.moveToNext()){
                bean = new BioFichaBean();
                bean.setID(c.getString(0));
                bean.setID_SEDE(c.getString(1));
                bean.setID_TIPO_DOCUMENTO(c.getString(2));
                bean.setNUM_DOCUMENTO(c.getString(3));
                bean.setCOD_PAIS(c.getString(4));
                bean.setNOMBRES(c.getString(5));
                bean.setAPELLIDO_PATERNO(c.getString(6));
                bean.setAPELLIDO_MATERNO(c.getString(7));
                bean.setFECHA_NACIMIENTO(c.getString(8));
                bean.setGENERO(c.getString(9));
                bean.setESTATURA(c.getString(10));
                bean.setPESO(c.getString(11));
                bean.setIMC(c.getString(12));
                bean.setGRADO_CELSIUS(c.getString(13));
                bean.setMENSAJE_ESTADO(c.getString(14));
                bean.setLATITUD(c.getDouble(15));
                bean.setLONGITUD(c.getDouble(16));
                bean.setOTRO_SINTOMA(c.getString(17));
                bean.setFEC_CREACION(c.getString(18));
                bean.setFEC_ACTUALIZACION(c.getString(19));
                bean.setFEC_ELIMINACION(c.getString(20));

                list.add(bean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public BioFichaBean getObject(String id){
        BioFichaBean bean = null;
        Cursor c = cargarById(id);

        try{
            while (c.moveToNext()){
                bean = new BioFichaBean();
                bean.setID(c.getString(0));
                bean.setID_SEDE(c.getString(1));
                bean.setID_TIPO_DOCUMENTO(c.getString(2));
                bean.setNUM_DOCUMENTO(c.getString(3));
                bean.setCOD_PAIS(c.getString(4));
                bean.setNOMBRES(c.getString(5));
                bean.setAPELLIDO_PATERNO(c.getString(6));
                bean.setAPELLIDO_MATERNO(c.getString(7));
                bean.setFECHA_NACIMIENTO(c.getString(8));
                bean.setGENERO(c.getString(9));
                bean.setESTATURA(c.getString(10));
                bean.setPESO(c.getString(11));
                bean.setIMC(c.getString(12));
                bean.setGRADO_CELSIUS(c.getString(13));
                bean.setMENSAJE_ESTADO(c.getString(14));
                bean.setLATITUD(c.getDouble(15));
                bean.setLONGITUD(c.getDouble(16));
                bean.setOTRO_SINTOMA(c.getString(17));
                bean.setFEC_CREACION(c.getString(18));
                bean.setFEC_ACTUALIZACION(c.getString(19));
                bean.setFEC_ELIMINACION(c.getString(20));
            }
        }catch (Exception e){
            e.printStackTrace();
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
