package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class editarEstacionamActivity extends AppCompatActivity implements View.OnClickListener {

    String nombreEst;
    int idEstacionamiento;
    int estDispIngresa;
    double lat;
    double lon;
    int capEst;
    int estOcu;

    JSONObject jsonObj;

    Button botonAcepta;
    Button botonNoAcepta;
    NumberPicker nPicker;

    private Handler handler;
    Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_estacionam);

        Bundle bundle = getIntent().getExtras();
        nombreEst=bundle.getString("nombreEstacionamiento");
        idEstacionamiento=Integer.parseInt(bundle.getString("idEstacionamiento"));



        botonAcepta = (Button)findViewById(R.id.botonActualiz);
        //Registrando la escucha sobre la actividad Main
        botonAcepta.setOnClickListener(this);

        botonNoAcepta = (Button)findViewById(R.id.botonCancela);
        botonNoAcepta.setOnClickListener(this);

        nPicker = (NumberPicker)findViewById(R.id.numberPickerEst);
        nPicker.setMinValue(0);

        //nPicker.setWrapSelectorWheel(false);



        String rest=null;
        try {
            //Se realiza un get al estacionamiento seleccionado
            rest = new HttpGet(mcontext, editarEstacionamActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/estacionamientos/" + idEstacionamiento).get();

            //Se crea un jsonObj con los datos del estacionamiento
            jsonObj = new JSONObject(rest);
            //El valor maximo del npicker sera la cantidad maxima de espacios del estacioanmiento
            nPicker.setMaxValue(jsonObj.getInt("capacidad"));


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.botonActualiz) {
            //Se obtiene el valor ingresado
            estDispIngresa = nPicker.getValue();

            //estacionamientos ocupados = total - disponibles
            try {
                estOcu = jsonObj.getInt("capacidad") - estDispIngresa;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Se edita el valor resultante en el jsonObj
            try {
                jsonObj.put("ocupados",estOcu);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Hago el put al rest
            new HttpPut(mcontext, jsonObj,editarEstacionamActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/estacionamientos/"+idEstacionamiento);
            //Toast para confirmar
            Toast.makeText(this, "Capacidad editada exitosamente", Toast.LENGTH_LONG).show();
            //vuelvo al mapa
            Intent intent = new Intent(this, mapa.class);
            startActivity(intent);
        }
        //Si cancelo
        if(v.getId() == R.id.botonCancela){
            //vuelvo al mapa
            Intent intent = new Intent(this,mapa.class);
            startActivity(intent);

        }
    }
    public void onBackPressed() {
        //onNavigateUp();
        Intent intent = new Intent(this,mapa.class);
        startActivity(intent);
    }
}
