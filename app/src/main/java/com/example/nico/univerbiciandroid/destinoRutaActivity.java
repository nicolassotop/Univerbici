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
            rest = new HttpGet(contextDestiny, destinoRutaActivity.this).execute("http://192.168.0.15:8080/sakila-backend-master/entradas/").get();
            //JSONObject json = new JSONObject(rest);
            JSONArray jRest = new JSONArray(rest);

            for (int i=0; i<jRest.length();i++) {
                try {
                    jObjEst = jRest.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jObjEst != null) {


                    //estacionamientos.add(est);
                    //names[i]=jObjEst.getString("nombreEntrada");
                    list.add(jObjEst.getString("nombreEntrada"));
                    listID.add(jObjEst.getString("identradaU"));

                }
            }
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

            nombreEntradaElegida = sp.getSelectedItem().toString();
            Log.e("NOMBRE ENTRADA ELEGIDA","nombre: "+nombreEntradaElegida);

            for (int i=0; i<list.size();i++) {
                if (nombreEntradaElegida.equals(list.get(i))) {
                    String rest = null;
                    String idEntradaSelect;
                    idEntradaSelect = listID.get(i);
                    try {
                        rest = new HttpGet(mcontextDestinoRuta, destinoRutaActivity.this).execute("http://192.168.0.15:8080/sakila-backend-master/entradas/" + idEntradaSelect).get();

                        jObjEntrada = new JSONObject(rest);

                        if (jObjEntrada != null) {
                            latEntrada = jObjEntrada.getDouble("ubi_xEntrada");
                            lngEntrada = jObjEntrada.getDouble("ubi_yEntrada");

                            Log.e("LUEGO DE IF JOBJ", "lat: " + latEntrada);

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

}
