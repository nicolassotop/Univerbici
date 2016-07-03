package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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

import java.util.concurrent.ExecutionException;

public class mapsRuta extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap2;
    private Handler handler2;
    Context mcontext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_ruta);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap2 = googleMap;
        Thread t3;



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
        //mMap2.setMyLocationEnabled(true);

        //Ubicacion del usuario segun BD
        LatLng ubiUser = new LatLng(Login.getLatUserLogged(),Login.getLngUserLogged());


        CameraPosition camPos = new CameraPosition.Builder()
                .target(ubiUser)   //Centramos el mapa en la ubicacion del user
                .zoom(15)         //Establecemos el zoom en 15
                .build();

        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);

        mMap2.animateCamera(camUpd3);

        final JSONObject[] jsonObj1 = {null};
        handler2 = new Handler();

        t3 = new Thread(new Runnable() {
            public void run() {
                handler2.post(new Runnable() { // This thread runs in the UI
                    @Override
                    public void run() {
                        try {
                            //Directions API
                            String rest3 = new HttpGet(mcontext, mapsRuta.this).execute("https://maps.googleapis.com/maps/api/directions/json?origin="+Login.getLatUserLogged()+","+Login.getLngUserLogged()+"&destination="+destinoRutaActivity.getLatEntrada()+","+destinoRutaActivity.getLngEntrada()+"&mode=driving&avoid=highways|tolls").get();



                            try {
                                jsonObj1[0] = new JSONObject(rest3);//jRest.getJSONObject(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (jsonObj1[0] != null) {


                                //El get devuelve muchos datos, se debe filtrar y luego iterar entre los puntos
                                JSONArray jArrayRoutes;
                                JSONObject jObjectRoute;

                                JSONArray jArrayLegs;
                                JSONObject jObjectLegs;

                                JSONArray jArraySteps;
                                JSONObject jObjectStep;

                                JSONArray jArrayEndLocation;
                                JSONObject jObjectEndLocation;
                                JSONArray jArrayStartLocation;
                                JSONObject jObjectStartLocation;
                                JSONObject jObjGet;

                                LatLng puntoOrigen;
                                LatLng puntoDestino;

                                PolylineOptions rectOptions = null;



                                //SOL 3

                                jArrayRoutes = (JSONArray) jsonObj1[0].get("routes");
                                jObjGet = (JSONObject)jArrayRoutes.get(0);
                                jArrayLegs = (JSONArray)jObjGet.get("legs");
                                jObjectLegs = (JSONObject) jArrayLegs.get(0);
                                jArraySteps = (JSONArray)jObjectLegs.get("steps");

                                //For que itera entre los puntos
                                for (int i = 0; i < jArraySteps.length(); i++) {
                                    //jObjectStep = jArraySteps.getJSONObject(i);
                                    jObjectStep = (JSONObject)jArraySteps.get(i);

                                    if(i==0){

                                        //Cada vez que dobla se crea una linea con un nuevo start y end location
                                        //Si es la primera vez el start sera el punto de inicio
                                        jObjectStartLocation = jObjectStep.getJSONObject("start_location");
                                        puntoOrigen = new LatLng(jObjectStartLocation.getDouble("lat"),jObjectStartLocation.getDouble("lng"));

                                        mMap2.addMarker(new MarkerOptions()
                                                        .position(puntoOrigen));

                                                rectOptions = new PolylineOptions()
                                                .add(puntoOrigen).color(Color.BLUE); // Closes the polyline.

                                    }

                                    //Se consideran solo los end porque:
                                    //end_location[0] == start_location[1]
                                    jObjectEndLocation = jObjectStep.getJSONObject("end_location");

                                    puntoDestino = new LatLng(jObjectEndLocation.getDouble("lat"),jObjectEndLocation.getDouble("lng"));

                                    rectOptions.add(puntoDestino).color(Color.BLUE);

                                    //Si es el ultimo punto se pone un marcador
                                    if(i==jArraySteps.length() -1){
                                        mMap2.addMarker(new MarkerOptions()
                                                .position(puntoDestino));
                                    }
                                }

                                //Se crea la polilinea y se agrega al mapa
                                Polyline polyline = mMap2.addPolyline(rectOptions);
                            }


                        }catch (InterruptedException e) {
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
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t3.start();

    }

    @Override
    public void onBackPressed() {
        //onNavigateUp();
        Intent intent = new Intent(this,destinoRutaActivity.class);
        startActivity(intent);
    }



}



