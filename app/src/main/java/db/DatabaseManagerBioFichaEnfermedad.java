package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import beans.BioFichaEnfermedadBean;
import beans.BioFichaSintomaBean;
import beans.SpinnerBean;
import beans.UsuarioSedeBean;

public class DatabaseManagerBioFichaEnfermedad extends DatabaseManager {

    public static final String NOMBRE_TABLA = "BIO_FICHA_ENFERMEDAD";
    public static final String CN_ID_FICHA = "ID_FICHA";
    public static final String CN_ID_ENFERMEDAD = "ID_ENFERMEDAD";


    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID_FICHA + " integer,"
            + CN_ID_ENFERMEDAD + " integer"
            + ");";

    public DatabaseManagerBioFichaEnfermedad(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(BioFichaEnfermedadBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID_FICHA,obj.getID_FICHA());
        valores.put(CN_ID_ENFERMEDAD,obj.getID_ENFERMEDAD());
        return valores;
    }

    public void insertar(BioFichaEnfermedadBean obj) {
        Log.d(NOMBRE_TABLA + "_in",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(BioFichaEnfermedadBean obj) {
        ContentValues valores = generarContentValues(obj);
        String [] args = new String[]{obj.getID_FICHA()};
        Log.d(NOMBRE_TABLA + "_ac",super.getDb().update(NOMBRE_TABLA,valores, CN_ID_FICHA+ "=?", args)+"");
    }

    @Override
    public void eliminar(String id) {
        super.getDb().delete(NOMBRE_TABLA,CN_ID_FICHA+ "=?", new String[]{id});
    }

    @Override
    public void eliminarTodo() {
        super.getDb().execSQL("DELETE FROM " + NOMBRE_TABLA + ";");
        Log.d(NOMBRE_TABLA + "_el","Datos borrados");
    }

    @Override
    public Cursor cargar() {
        String [] columnas = new String[]
                {CN_ID_FICHA,CN_ID_ENFERMEDAD};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID_FICHA,CN_ID_ENFERMEDAD};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID_FICHA + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID_FICHA,CN_ID_ENFERMEDAD};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID_FICHA+ " = ?",new String[] { tipo },null,null,null);
    }


    @Override
    public Boolean compruebaRegistro(String id) {
        boolean existe = true;
        Cursor resultSet = super.getDb().rawQuery("Select * from " + NOMBRE_TABLA + " WHERE " + CN_ID_FICHA + " =" + id, null);

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

    public List<BioFichaEnfermedadBean> ListarPorFicha(String Id) {
        List<BioFichaEnfermedadBean> list = new ArrayList<>();
        String SQL = "Select e._ID, e.DESCRIPCION FROM BIO_FICHA bf INNER JOIN BIO_FICHA_ENFERMEDAD bfe ON bfe.ID_FICHA = bf._ID INNER JOIN ENFERMEDAD e ON e._ID = bfe.ID_ENFERMEDAD WHERE bf._ID = " + Id;
        Cursor c = super.getDb().rawQuery(SQL, null);

        BioFichaEnfermedadBean bean = null;
        while (c.moveToNext()){
            bean = new BioFichaEnfermedadBean();
            bean.setID_ENFERMEDAD(c.getString(0));
            bean.setNOM_ENFERMEDAD(c.getString(1));

            list.add(bean);
        }
        return list;
    }

    public List<BioFichaEnfermedadBean> getList(String tipo){
        List<BioFichaEnfermedadBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        BioFichaEnfermedadBean bean = null;
        while (c.moveToNext()){
            bean = new BioFichaEnfermedadBean();
            bean.setID_FICHA(c.getString(0));
            bean.setID_ENFERMEDAD(c.getString(1));

            list.add(bean);
        }
        return list;
    }

    @Override
    public BioFichaEnfermedadBean get(String id){
        BioFichaEnfermedadBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new BioFichaEnfermedadBean();
            bean.setID_FICHA(c.getString(0));
            bean.setID_ENFERMEDAD(c.getString(1));;
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
