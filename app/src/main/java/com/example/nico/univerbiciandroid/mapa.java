package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class mapa extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private Handler handler;
    Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        String name;
        int est;
        int estLibres;
        float lat;
        float lon;


        Thread t;

        // Add a marker in Informatica and move the camera
        LatLng info = new LatLng(-33.449833, -70.687145);

        /*Marker informatica = mMap.addMarker(new MarkerOptions()
                .position(info)
                .title("Ingeniería en Informática")
                .snippet("Estacionamientos: 10"));
*/


        CameraPosition camPos = new CameraPosition.Builder()
                .target(info)   //Centramos el mapa en Madrid
                .zoom(17)         //Establecemos el zoom en 19
                //.bearing(45)      //Establecemos la orientación con el noreste arriba
                //.tilt(70)         //Bajamos el punto de vista de la cámara 70 grados
                .build();

        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);

        mMap.animateCamera(camUpd3);

        handler = new Handler();

        t = new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() { // This thread runs in the UI
                    @Override
                    public void run() {
                        final ArrayList<Estacionamiento> estacionamientos = new ArrayList<Estacionamiento>();//creamos el objeto lista;
                        String rest = null;

                        try {
                            rest = new getEstacionamientos(mcontext, mapa.this).execute("http://192.168.0.15:8080/sakila-backend-master/estacionamientos/").get();
                            //JSONObject json = new JSONObject(rest);
                            JSONArray jRest = new JSONArray(rest);

                            for (int i = 0; i < jRest.length(); i++) {

                                JSONObject jObject = null;
                                try {
                                    jObject = jRest.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (jObject != null) {

                                    Estacionamiento est = new Estacionamiento(jObject);
                                    LatLng ubi = new LatLng(jObject.getDouble("ubi_x"),jObject.getDouble("ubi_y"));
                                    est.setUbicacion(ubi);

                                    int idEst = i +1;
                                    est.setIdEstacionamiento(idEst);

                                    estacionamientos.add(est);//almacenamos el estacionamiento en la lista
                                    int estDisp = est.getCantidadEstacionamiento() - est.getOcupados();

                                    Marker m2 = mMap.addMarker(new MarkerOptions()
                                            .position(ubi)
                                            .title(est.getNombreEstacionamiento())
                                            .snippet("Cantidad de Estacionamientos: "+est.getCantidadEstacionamiento()+"\n"+
                                                    "Estacionamientos Disponibles: "+estDisp)
                                    );

                                    /////////////
                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                        @Override
                                        public View getInfoWindow(Marker marker) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker marker) {
                                            Context mContext = getApplicationContext();
                                            LinearLayout info = new LinearLayout(mContext);
                                            info.setOrientation(LinearLayout.VERTICAL);

                                            TextView title = new TextView(mContext);
                                            title.setTextColor(Color.BLACK);
                                            title.setGravity(Gravity.CENTER);
                                            title.setTypeface(null, Typeface.BOLD);
                                            title.setText(marker.getTitle());

                                            TextView snippet = new TextView(mContext);
                                            snippet.setTextColor(Color.GRAY);
                                            snippet.setText(marker.getSnippet());

                                            info.addView(title);
                                            info.addView(snippet);
                                            return info;
                                        }
                                    });


                                    ///
                                }


                            } // End Loop



                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }


        });
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.start();

/*
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public void onMarkerClick(Marker marker) {
                String nombreMarcador = marker.getTitle();

                Iterator iterador = estacionamientos.listIterator(); //Le solicito a la lista que me devuelva un iterador con todos los el elementos contenidos en ella

                //Mientras que el iterador tenga un proximo elemento
                while( iterador.hasNext() ) {
                    Estacionamiento estPulsado = (Estacionamiento) iterador.next(); //Obtengo el elemento contenido
                    //Log.e("Iterador","");
                    if(nombreMarcador == estPulsado.getNombreEstacionamiento()){
                        int estLibres = estPulsado.getCantidadEstacionamiento() - estPulsado.getOcupados();
                        Toast.makeText(mapa.this,
                                "Cantidad de estacionamientos: "+estPulsado.getCantidadEstacionamiento()+"\nEstacionamientos libres: "+estLibres,
                                Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });*/


    }
}


