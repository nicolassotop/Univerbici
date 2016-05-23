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

import org.json.JSONArray;
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
            rest = new HttpGetEstacionamientoxId(mcontext, editarEstacionamActivity.this).execute("http://192.168.0.15:8080/sakila-backend-master/estacionamientos/" + idEstacionamiento).get();

            jsonObj = new JSONObject(rest);
            nPicker.setMaxValue(jsonObj.getInt("capacidad"));
            //JSONArray ja = new JSONArray(rest);
/*
            capEst = jsonObj.getInt("capacidad");
            nPicker.setMaxValue(capEst);
            Log.e("GET", "capacidadEst " + capEst);

            estDispIngresa=nPicker.getValue();

            lat = jsonObj.getDouble("ubi_x");
            lon = jsonObj.getDouble("ubi_y");
//            estOcu = capEst - estDispIngresa;

  */



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

            estDispIngresa = nPicker.getValue();

            Log.e("ONCLICK","Ingresa user: "+estDispIngresa);

            try {
                estOcu = jsonObj.getInt("capacidad") - estDispIngresa;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("ONCLICK","Est ocupados: "+estOcu);
            try {
                jsonObj.put("ocupados",estOcu);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new HttpPutEstacionamientos(mcontext, jsonObj,editarEstacionamActivity.this).execute("http://192.168.0.15:8080/sakila-backend-master/estacionamientos/"+idEstacionamiento);
            Toast.makeText(this, "Capacidad editada exitosamente", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, mapa.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.botonCancela){

            Intent intent = new Intent(this,mapa.class);
            startActivity(intent);

        }
    }
}
