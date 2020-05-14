package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NOMBRE = "bioficha.sqlite";
    private static int DB_SCHEME_VERSION = 1;

    public DbHelper(Context context){ //, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NOMBRE, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(DatabaseManagerUsuario.CREATE_TABLE);
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

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManagerUsuario.NOMBRE_TABLA);
        onCreate(db);
    }
}
