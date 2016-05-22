package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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
import java.util.concurrent.ExecutionException;

public class mapa extends FragmentActivity implements OnMapReadyCallback {

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
                                    Log.e("Marker", "asda" + est.getNombreEstacionamiento() +" Ubicacion: " + ubi);


                                    Marker m2 = mMap.addMarker(new MarkerOptions()
                                            .position(ubi)
                                            .title(est.getNombreEstacionamiento())
                                            .snippet("Ocupados: "+est.getOcupados())
                                    );

                                    



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
        }
}


