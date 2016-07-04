package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class destinoRutaActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner sp;
    private String nombreEntradaElegida;
    private Context contextDestiny;

    private Button botonVerRuta;
    private Button botonVolveraMain;

    private JSONObject jObjEst;

    static double latEntrada;
    static double lngEntrada;


    private String[] names;
    List<String> list = new ArrayList<String>();
    List<String> listID = new ArrayList<String>();

    private Context mcontextDestinoRuta;
    private JSONObject jObjEntrada;

    public static double getLatEntrada() {
        return latEntrada;
    }

    public static double getLngEntrada() {
        return lngEntrada;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destino_ruta);

        sp = (Spinner)findViewById(R.id.spinnerEntrada);

        botonVerRuta = (Button)findViewById(R.id.buttonVerRuta);
        botonVerRuta.setOnClickListener(this);

        botonVolveraMain = (Button)findViewById(R.id.buttonVolverAMain);
        botonVolveraMain.setOnClickListener(this);



        String rest = null;

        try {
            //Hago un get para obtener todas las entradas
            rest = new HttpGet(contextDestiny, destinoRutaActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/entradas/").get();

            //Se guarda el resultado en un array
            JSONArray jRest = new JSONArray(rest);

            //Se itera para cada elemento del array. Cada entrada distinta
            for (int i=0; i<jRest.length();i++) {
                try {
                    //Se crea el JSONObject para cada elemento
                    jObjEst = jRest.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jObjEst != null) {

                    //Se guardan los nombres y los id en listas diferentes, pero en el mismo orden
                    list.add(jObjEst.getString("nombreEntrada"));
                    listID.add(jObjEst.getString("identradaU"));

                }
            }
            //Se utiliza la lista de nombres para llenar el spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, list);


            sp.setAdapter(adapter);



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }





    public void onClick(View v) {
        if (v.getId() == R.id.buttonVerRuta) {

            //Se obtiene el nombre elegido en el spinner
            nombreEntradaElegida = sp.getSelectedItem().toString();

            //Se debe buscar el id en la otra lista
            for (int i=0; i<list.size();i++) {
                if (nombreEntradaElegida.equals(list.get(i))) {
                    String rest = null;
                    String idEntradaSelect;
                    //Encuentro el id
                    idEntradaSelect = listID.get(i);
                    try {
                        //Con el id hago un get de la ruta
                        rest = new HttpGet(mcontextDestinoRuta, destinoRutaActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/entradas/" + idEntradaSelect).get();

                        //se crea el JsonObject con los datos de la ruta
                        jObjEntrada = new JSONObject(rest);

                        if (jObjEntrada != null) {
                            //Se guarda el punto de inicio
                            latEntrada = jObjEntrada.getDouble("ubi_xEntrada");
                            lngEntrada = jObjEntrada.getDouble("ubi_yEntrada");

                            //Voy al mapa
                            Intent intent = new Intent(this, mapsRuta.class);
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


            Intent intent = new Intent(this, mapsRuta.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonVolverAMain){
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
