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

public class DatabaseManagerBioFichaSintoma extends DatabaseManager {

    public static final String NOMBRE_TABLA = "BIO_FICHA_SINTOMA";
    public static final String CN_ID_FICHA = "ID_FICHA";
    public static final String CN_ID_SINTOMA = "ID_SINTOMA";


    public static final String CREATE_TABLE =  "create table " + NOMBRE_TABLA + " ("
            + CN_ID_FICHA + " integer,"
            + CN_ID_SINTOMA + " integer"
            + ");";

    public DatabaseManagerBioFichaSintoma(Context ctx) {
        super(ctx);
    }
    @Override
    public void cerrar(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(BioFichaSintomaBean obj){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID_FICHA,obj.getID_FICHA());
        valores.put(CN_ID_SINTOMA,obj.getID_SINTOMA());
        return valores;
    }

    public void insertar(BioFichaSintomaBean obj) {
        Log.d(NOMBRE_TABLA + "_in",super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(obj))+"");
    }

    public void actualizar(BioFichaSintomaBean obj) {
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
                {CN_ID_FICHA,CN_ID_SINTOMA};
        return super.getDb().query(NOMBRE_TABLA, columnas,null,null,null,null,null);
    }

    @Override
    public Cursor cargarById(String id) {
        String [] columnas = new String[]
                {CN_ID_FICHA,CN_ID_SINTOMA};
        return super.getDb().query(NOMBRE_TABLA, columnas,CN_ID_FICHA + "=?", new String[]{id},null,null,null);
    }


    public Cursor cargarPorTipo(String tipo) {
        String [] columnas = new String[]
                {CN_ID_FICHA,CN_ID_SINTOMA};
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

    public List<BioFichaSintomaBean> ListarPorFicha(String Id) {
        List<BioFichaSintomaBean> list = new ArrayList<>();
        String SQL = "Select s._ID, s.DESCRIPCION FROM BIO_FICHA bf INNER JOIN BIO_FICHA_SINTOMA bfs ON bfs.ID_FICHA = bf._ID INNER JOIN SINTOMA s ON s._ID = bfs.ID_SINTOMA WHERE bf._ID = " + Id;
        Cursor c = super.getDb().rawQuery(SQL, null);

        BioFichaSintomaBean bean = null;
        while (c.moveToNext()){
            bean = new BioFichaSintomaBean();
            bean.setID_SINTOMA(c.getString(0));
            bean.setNOM_SINTOMA(c.getString(1));

            list.add(bean);
        }
        return list;
    }

    public List<BioFichaSintomaBean> getList(String tipo){
        List<BioFichaSintomaBean> list = new ArrayList<>();
        Cursor c = null;

        if (tipo == "")
            c = cargar();
        else
            c = cargarPorTipo(tipo);

        BioFichaSintomaBean bean = null;
        while (c.moveToNext()){
            bean = new BioFichaSintomaBean();
            bean.setID_FICHA(c.getString(0));
            bean.setID_SINTOMA(c.getString(1));

            list.add(bean);
        }
        return list;
    }

    @Override
    public BioFichaSintomaBean get(String id){
        BioFichaSintomaBean bean = null;
        Cursor c = cargarById(id);

        while (c.moveToNext()){
            bean = new BioFichaSintomaBean();
            bean.setID_FICHA(c.getString(0));
            bean.setID_SINTOMA(c.getString(1));;
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
