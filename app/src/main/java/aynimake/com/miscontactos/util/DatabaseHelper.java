package aynimake.com.miscontactos.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import aynimake.com.miscontactos.R;
import aynimake.com.miscontactos.entity.Contacto;

/**
 * Esta clase es utilizada para administrar la CREACION y ACTUALIZACION de tu Base de Datos.
 * Esta clase usualmente proporciona las clases DAO (Patron de Diseño Data Access Object)
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "agenda.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * Metodo invocado cuando la Base de Datos es creada.
     * Usualmente se hacen llamadas a los metodos "createTable"
     * para crear las tablas que almacenaran los datos
     * @param db
     * @param source
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {
        try {
            Log.i(DatabaseHelper.class.getSimpleName(),"onCreate()");
            TableUtils.createTable(source, Contacto.class);
        } catch (SQLException ex) {
            Log.e(DatabaseHelper.class.getSimpleName(), "Imposible crear la Base de Datos", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Este método es invocado cuando la aplicacion es actualizada y tiene un
     * numero de version superior. Permite el ajuste a los datos para alinearse a la nueva version.
     * @param db
     * @param source
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getSimpleName(),"onUpgrade()");
            TableUtils.dropTable(source, Contacto.class, true);
            onCreate(db, source);
        } catch (SQLException ex) {
            Log.e(DatabaseHelper.class.getSimpleName(), "Imposible eliminar la Base de Datos", ex);
            throw new RuntimeException(ex);
        }
    }


}
