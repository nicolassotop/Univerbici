package com.example.nico.univerbiciandroid;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class mapa extends FragmentActivity implements OnMapReadyCallback, OnInfoWindowClickListener {

    private GoogleMap mMap;
    private Handler handler;
    Context mcontext;
    static ArrayList<Estacionamiento> estacionamientos;

    public static ArrayList<Estacionamiento> getEstacionamientos() {
        return estacionamientos;
    }

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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        String name;
        int est;
        int estLibres;
        float lat;
        float lon;


        Thread t;


        LatLng info = new LatLng(-33.449833, -70.687145);


        CameraPosition camPos = new CameraPosition.Builder()
                .target(info)   //Centramos el mapa en info
                .zoom(17)         //Establecemos el zoom en 17
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
                        estacionamientos = new ArrayList<Estacionamiento>();//creamos el objeto lista;
                        String rest = null;

                        try {
                            //obtengo todos los estacionamientos de la bd
                            rest = new HttpGet(mcontext, mapa.this).execute("http://192.168.0.15:9090/sakila-backend-master/estacionamientos/").get();

                            //guardo en un array
                            JSONArray jRest = new JSONArray(rest);

                            for (int i = 0; i < jRest.length(); i++) {
                                //se debe recorrer el array
                                JSONObject jObject = null;
                                try {
                                    //por cada elemento obtengo un jsonObj
                                    jObject = jRest.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Si no es nulo
                                if (jObject != null) {

                                    //creo un nuevo estacionamiento
                                    Estacionamiento est = new Estacionamiento(jObject);
                                    LatLng ubi = new LatLng(jObject.getDouble("ubi_x"), jObject.getDouble("ubi_y"));
                                    est.setUbicacion(ubi);

                                    final int idEst = i + 1;
                                    est.setIdEstacionamiento(idEst);

                                    estacionamientos.add(est);//almacenamos el estacionamiento en la lista
                                    int estDisp = est.getCantidadEstacionamiento() - est.getOcupados();

                                    //agrego un marcador
                                    //en el titulo guardo el nombre para mostrarlo y el id para buscarlo luego
                                    Marker m2 = mMap.addMarker(new MarkerOptions()
                                            .position(ubi)
                                            .title(est.getNombreEstacionamiento() + "," + est.getIdEstacionamiento())
                                            .snippet("Cantidad de Estacionamientos: " + est.getCantidadEstacionamiento() + "\n" +
                                                    "Estacionamientos Ocupados: " + est.getOcupados() + "\n" + "Estacionamientos Disponibles: " + estDisp)
                                    );

                                    ///////////// formato del infowindow
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

                                            String[] contTitulo = marker.getTitle().split(",");
                                            title.setText(contTitulo[0]);

                                            TextView snippet = new TextView(mContext);
                                            snippet.setTextColor(Color.GRAY);
                                            snippet.setText(marker.getSnippet());

                                            info.addView(title);
                                            info.addView(snippet);
                                            return info;
                                        }
                                    });


                                    //Si presiono el infowindow
                                    mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {

                                            //paso a la otra ventana
                                            Intent intent = new Intent(mapa.this, editarEstacionamActivity.class);

                                            //divido el titulo para separa nombre de id
                                            String[] nombreId = marker.getTitle().split(",");

                                            intent.putExtra("nombreEstacionamiento", nombreId[0]);
                                            intent.putExtra("idEstacionamiento", nombreId[1]);
                                            startActivity(intent);
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


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //Intent intent = new Intent(this,MainActivity.class);
        //startActivity(intent);
    }
    public void onBackPressed() {
        //vuelvo al menu principal
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}



