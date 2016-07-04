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



        //Se realiza un get para obtener todas las rutas
        try {
            rest = new HttpGet(contextCalifico, calificaRutaActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/rutas/").get();
            //JSONObject json = new JSONObject(rest);
            JSONArray jArrayRutas = new JSONArray(rest);

            //Para iterar en el jsonarray
            for (int i=0; i<jArrayRutas.length();i++) {
                try {
                    jObjrutas = jArrayRutas.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jObjrutas != null) {

                    //Se agregan los nombres a un arreglo
                    listaRutas.add(jObjrutas.getString("nombreRuta"));
                    //Se agregan los id de las rutas a un arreglo, en el mismo orden
                    listIDRutas.add(jObjrutas.getString("idRuta"));

                }
            }
            //Con el arreglo de los nombres se llena el spinner
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
        //Si acepto la calificacion
        if (v.getId() == R.id.aceptaCalifica) {

            //Obtengo el nombre de la ruta seleccionada en el spinner
            nombreRutaElegida= rutasACalificar.getSelectedItem().toString();

            //Se debe encontrar el id de la ruta, se crea un ciclo para recorrer las listas
            for (int i=0; i<listaRutas.size();i++) {
                //Si encontre el nombre
                if (nombreRutaElegida.equals(listaRutas.get(i))) {
                    String rest = null;
                    //Obtengo el id
                    idRutaElegida = listIDRutas.get(i);
                    try {
                        //Con el id puedo hacer un get de esa ruta
                        rest = new HttpGet(contextCalifico, calificaRutaActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/rutas/" + idRutaElegida).get();

                        //Se guarda el resultado del get en un JSONObject
                        jObjrutas = new JSONObject(rest);

                        //Si el JSON OBject no es nulo
                        if (jObjrutas != null) {

                            //La califIngresada son las estrellas ingresadas por el usuario
                            califIngresada = estrellas.getRating();

                            //La calificacion final es el promedio entre la calif de la BD y la calif ingresada con estrellas
                            promedioCalif = (califIngresada + jObjrutas.getDouble("calificacion"))/2;

                            //Edito el campo calificacion del JSONObject
                            jObjrutas.put("calificacion",promedioCalif);

                            //realizo el put al servicio REST
                            new HttpPut(contextCalifico, jObjrutas,calificaRutaActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/rutas/"+idRutaElegida);

                            //Muestro toast para confirmar
                            Toast.makeText(this, "Calificación ingresada exitosamente", Toast.LENGTH_LONG).show();

                            //Vuelvo a la pantalla anterior
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
        //Si cancelo
        if (v.getId() == R.id.cancelaCalifica) {
            //vuelvo a la pantalla anterior
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }

    }
    //Si presiono atras
    public void onBackPressed() {
        //vuelvo a la pantalla anterior
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
