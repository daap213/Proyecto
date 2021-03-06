package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.proyecto.entidades.AdminSQLiteOpenHelper;
import com.example.proyecto.entidades.Horario;
import com.example.proyecto.entidades.Usuario;
import com.example.proyecto.entidades.Utils;
import com.example.proyecto.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.*;
//Ventana que permite manejar a que hora se desea las notificaciones
public class HorariodeComida extends AppCompatActivity {
    private TextView notificationsTime;
    private int alarmID=1;
    private SharedPreferences settings;
    private EditText descripcion;
    ListView listaH;
    ArrayList<String> listaInformacion;
    ArrayList<Horario> listahora;
    AdminSQLiteOpenHelper admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Se declaran las variables
        setContentView(R.layout.activity_horariode_comida);
        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        listaH = (ListView) findViewById(R.id.ListaHora);

        notificationsTime = (TextView) findViewById(R.id.notifications_time);

         admin = new  AdminSQLiteOpenHelper(this,"PST_G6",null,1);

        descripcion = (EditText) findViewById(R.id.edit);
        String hour, minute;
        hour = settings.getString("hour", "");
        minute = settings.getString("minute", "");
        //Se revisa si ya existen alarmas para poder mostrarlas en la misma ventana
        consultarListahoras();
        //Crea un arreglo que mostrara informacion de cada alarma
        ArrayAdapter adaptador=new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaInformacion);
        listaH.setAdapter(adaptador);
        listaH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String informacion= listahora.get(pos).getHora();
                notificationsTime.setText(informacion);
            }
        });
    }

    private void consultarListahoras() {
//Se lee la base de datos y se consulta que horas hay existentes
        SQLiteDatabase db=admin.getReadableDatabase();
        Horario hora =null;
        listahora =new ArrayList<Horario>();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_HORARIO,null);

        while (cursor.moveToNext()){
            hora=new Horario();
            hora.setId(cursor.getInt(0));
            hora.setHora(cursor.getString(1));
            hora.setDescripcion(cursor.getString(2));


            listahora.add(hora);
        }
        obtenerLista();
    }
    public void obtenerLista() {
        //Se crea una lista con informacion sobre la alarma
        listaInformacion = new ArrayList<String>();

        for (int i = 0; i < listahora.size(); i++) {
            listaInformacion.add(listahora.get(i).getHora()+" - "+listahora.get(i).getDescripcion());
        }
    }
    public void eliminarUsuario(View view) {
        //Se elimina la alarma que el usuario haya escogido , y se vuelve a cargar el activity para mostrar el cambio
        SQLiteDatabase db=admin.getWritableDatabase();
        String[] parametros={notificationsTime.getText().toString()};

        db.delete(Utilidades.TABLA_HORARIO,Utilidades.KEY_HORARIO_HORA+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Se eliminó el recordatorio ",Toast.LENGTH_LONG).show();
        db.close();
        cargar();
    }
    public void agregarUsuario(View view) {
        //Utiliza la clase calendar para poder mostrar y obterner hora y minutos que se desea la alarma
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(HorariodeComida.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String finalHour, finalMinute="0";
                //Se obtienen las variables del tiempo y se procede a mostrarla
                finalHour = "" + selectedHour;
                finalMinute = "" + selectedMinute;
                if (selectedHour < 10) finalHour = "0" + selectedHour;
                if (selectedMinute < 10) finalMinute = "0" + selectedMinute;
                notificationsTime.setText(finalHour + ":" + finalMinute);

                Calendar today = Calendar.getInstance();

                today.set(Calendar.HOUR_OF_DAY, selectedHour);
                today.set(Calendar.MINUTE, selectedMinute);
                today.set(Calendar.SECOND, 0);

                SharedPreferences.Editor edit = settings.edit();
                edit.putString("hour", finalHour);
                edit.putString("minute", finalMinute);

                //Se guarda la hora seleccionada para que se cree la alarma si se reinicia el sistema
                edit.putInt("alarmID", alarmID);
                edit.putLong("alarmTime", today.getTimeInMillis());

                edit.commit();
                //Se guarda la alarma en la base de datos
                SQLiteDatabase db= admin.getWritableDatabase();

                ContentValues values=new ContentValues();
                values.put(Utilidades.KEY_HORARIO_HORA,finalHour + ":" + finalMinute);
                values.put(Utilidades.KEY_HORARIO_DESCRIPCION,descripcion.getText().toString());


                Long idResultante=db.insert(Utilidades.TABLA_HORARIO,Utilidades.KEY_HORARIO_ID,values);

                Toast.makeText(getApplicationContext(),"Alarma registrada "+idResultante+"\nHora: "+finalHour + ":" + finalMinute,Toast.LENGTH_SHORT).show();
                db.close();
                //Se programa la notificacion acorde la alarma
                Utils.setAlarm(alarmID, today.getTimeInMillis(), HorariodeComida.this);
                if (finalHour != "0" && finalMinute != "0"){
                    cargar();
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();

    }
    private void cargar() {
        //Vuelve a cargar el activity para que se muestre los cambios
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}