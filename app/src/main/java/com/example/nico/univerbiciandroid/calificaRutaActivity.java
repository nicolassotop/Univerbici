package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class calificaRutaActivity extends AppCompatActivity implements View.OnClickListener{

    private RatingBar estrellas;
    private Spinner rutasACalificar;
    private Button acepto;
    private Button cancelo;

    private String rest;
    private double califIngresada;
    private double promedioCalif;

    private JSONObject jObjrutas;

    private Context contextCalifico;

    List<String> listaRutas = new ArrayList<String>();
    List<String> listIDRutas = new ArrayList<String>();

    String nombreRutaElegida;
    String idRutaElegida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_califica_ruta);

        rutasACalificar = (Spinner)findViewById(R.id.spinnerCalifico);
        estrellas = (RatingBar)findViewById(R.id.calificaRatingBar);

        acepto = (Button)findViewById(R.id.aceptaCalifica);
        acepto.setOnClickListener(this);

        cancelo = (Button)findViewById(R.id.cancelaCalifica);
        cancelo.setOnClickListener(this);




        try {
            rest = new HttpGet(contextCalifico, calificaRutaActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/rutas/").get();
            //JSONObject json = new JSONObject(rest);
            JSONArray jArrayRutas = new JSONArray(rest);

            for (int i=0; i<jArrayRutas.length();i++) {
                try {
                    jObjrutas = jArrayRutas.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jObjrutas != null) {


                    //estacionamientos.add(est);
                    //names[i]=jObjEst.getString("nombreEntrada");
                    listaRutas.add(jObjrutas.getString("nombreRuta"));
                    listIDRutas.add(jObjrutas.getString("idRuta"));

                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, listaRutas);


            rutasACalificar.setAdapter(adapter);



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
        if (v.getId() == R.id.aceptaCalifica) {

            nombreRutaElegida= rutasACalificar.getSelectedItem().toString();

            for (int i=0; i<listaRutas.size();i++) {
                if (nombreRutaElegida.equals(listaRutas.get(i))) {
                    String rest = null;
                    idRutaElegida = listIDRutas.get(i);
                    try {
                        rest = new HttpGet(contextCalifico, calificaRutaActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/rutas/" + idRutaElegida).get();

                        jObjrutas = new JSONObject(rest);


                        if (jObjrutas != null) {



                            califIngresada = estrellas.getRating();


                            promedioCalif = (califIngresada + jObjrutas.getDouble("calificacion"))/2;


                            jObjrutas.put("calificacion",promedioCalif);

                            new HttpPut(contextCalifico, jObjrutas,calificaRutaActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/rutas/"+idRutaElegida);

                            Toast.makeText(this, "Calificación ingresada exitosamente", Toast.LENGTH_LONG).show();


                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);

                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        if (v.getId() == R.id.cancelaCalifica) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }

    }
    public void onBackPressed() {
        //onNavigateUp();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
