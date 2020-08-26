package com.example.proyecto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    //TODAS LAS VARIABLES ESTATICAS
    private static final int DATABASE_VERSION=1;

    //NOMBRE DE LA BASE DE DATOS
    private static final String DATABASE_NOMBRE= "PST_GRUPO6";

    //NOMBRES DE LAS TABLAS
    private static final String USUARIO= "usuario";
    private static final String MASCOTA= "mascota";

    //NOMBRE DE LA TABLA DE USUARIO
    private static final String KEY_USUARIO_ID= "id";
    private static final String KEY_USUARIO_NOMBRE= "nombre";
    private static final String KEY_GENERO= "genero";
    private static final String KEY_FN="Fecha de nacimiento";
    private static final String KEY_CONTRASEÑA= "contraseña";
    private static final String KEY_CORREO= "correo";
    private static final String KEY_UBICACION="ubicacion";

    //NOMBRE DE LA TABLA DE MASCOTA
    private static final String KEY_MASCOTA_ID= "id";
    private static final String KEY_MASCOTA_ID_FK= "usuarioId";//llave foranea
    private static final String KEY_MASCOTA_NOMBRE= "nombre";
    private static final String KEY_RAZA= "raza";
    private static final String KEY_EDAD= "edad";
    private static final String KEY_TAMAÑO="tamaño";





    public AdminSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NOMBRE, null,DATABASE_VERSION );
    }

    //Se llama cuando se está configurando la conexión de la base de datos.
    // Configure las configuraciones de la base de datos para cosas como
    // soporte de clave externa, registro de escritura anticipada, etc.
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MASCOTA_TABLE = "CREATE TABLE "+ MASCOTA+
                "(" +
                KEY_MASCOTA_ID + " INTEGER PRIMARY KEY," + // Define la primary key
                KEY_MASCOTA_ID_FK + " INTEGER REFERENCES " + USUARIO + "," +
                KEY_MASCOTA_NOMBRE + "VARCHAR REFERENCES " +
                KEY_RAZA+ "VARCHAR REFERENCES "+
                KEY_EDAD+ "INTEGER REFERENCES "+
                KEY_TAMAÑO+ "VARCHAR REFERENCES "+
                ")";

        String CREATE_USUARIO_TABLE = "CREATE TABLE " + USUARIO +
                "(" +
                KEY_USUARIO_ID + " INTEGER PRIMARY KEY,"+
                KEY_USUARIO_NOMBRE+ "VARCHAR REFERENCES"+
                KEY_GENERO+ "VARCHAR REFERENCES "+
                KEY_FN+ "DATE REFERENCES "+
                KEY_CONTRASEÑA+ "VARCHAR REFERENCES "+
                KEY_CORREO+ "VARCHAR REFERENCES "+
                KEY_UBICACION +"VARCHAR REFERENCES "+
                ")";



        db.execSQL(CREATE_MASCOTA_TABLE);
        db.execSQL(CREATE_USUARIO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int viejaVersion, int nuevaVersion) {
        if (viejaVersion != nuevaVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MASCOTA);
            db.execSQL("DROP TABLE IF EXISTS " + USUARIO);
            onCreate(db);
        }
    }

}
