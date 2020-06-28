package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NOMBRE = "bioficha.sqlite";
    private static int DB_SCHEME_VERSION = 47;

    public DbHelper(Context context){ //, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NOMBRE, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DatabaseManagerEnfermedad.CREATE_TABLE);
            db.execSQL(DatabaseManagerSintoma.CREATE_TABLE);
            db.execSQL(DatabaseManagerUsuario.CREATE_TABLE);
            db.execSQL(DatabaseManagerEmpresa.CREATE_TABLE);
            db.execSQL(DatabaseManagerSede.CREATE_TABLE);
            db.execSQL(DatabaseManagerBioFicha.CREATE_TABLE);
            db.execSQL(DatabaseManagerRol.CREATE_TABLE);
            db.execSQL(DatabaseManagerTipoDocumento.CREATE_TABLE);
            db.execSQL(DatabaseManagerDepartamento.CREATE_TABLE);
            db.execSQL(DatabaseManagerProvincia.CREATE_TABLE);
            db.execSQL(DatabaseManagerDistrito.CREATE_TABLE);
            db.execSQL(DatabaseManagerUsuarioSede.CREATE_TABLE);
            db.execSQL(DatabaseManagerSedePoligono.CREATE_TABLE);
            db.execSQL(DatabaseManagerBioFichaEnfermedad.CREATE_TABLE);
            db.execSQL(DatabaseManagerBioFichaSintoma.CREATE_TABLE);
            db.execSQL(DatabaseManagerPais.CREATE_TABLE);

        }catch(Exception e){
            e.getMessage();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*Cursor cursor = db.rawQuery("SELECT * FROM "+ DataBaseManagerVehiculo.NOMBRE_TABLA, null); // grab cursor for all data
        int deleteStateColumnIndex = cursor.getColumnIndex(DataBaseManagerVehiculo.CN_IMEI);  // see if the column is there
        if (deleteStateColumnIndex < 0) {
            db.execSQL("ALTER TABLE " + DataBaseManagerVehiculo.NOMBRE_TABLA + " ADD COLUMN " + DataBaseManagerVehiculo.CN_IMEI + " text;");
        }

        cursor = db.rawQuery("SELECT * FROM "+ DataBaseManagerVehiculo.NOMBRE_TABLA, null); // grab cursor for all data
        deleteStateColumnIndex = cursor.getColumnIndex(DataBaseManagerVehiculo.CN_ESTADO_SYNC);  // see if the column is there
        if (deleteStateColumnIndex < 0) {
            db.execSQL("ALTER TABLE " + DataBaseManagerVehiculo.NOMBRE_TABLA + " ADD COLUMN " + DataBaseManagerVehiculo.CN_ESTADO_SYNC + " int;");
        }*/

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerEnfermedad.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerSintoma.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerUsuario.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerEmpresa.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerSede.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerBioFicha.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerRol.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerTipoDocumento.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerDepartamento.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerProvincia.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerDistrito.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerUsuarioSede.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerSedePoligono.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerBioFichaEnfermedad.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerBioFichaSintoma.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerPais.NOMBRE_TABLA);
        onCreate(db);
    }
}
