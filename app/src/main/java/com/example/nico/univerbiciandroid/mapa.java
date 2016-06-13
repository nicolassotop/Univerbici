package com.example.nico.univerbiciandroid;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class mapa extends FragmentActivity implements OnMapReadyCallback, OnInfoWindowClickListener {

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
        Thread t2;

        LatLng info = new LatLng(-33.449833, -70.687145);


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
                                    LatLng ubi = new LatLng(jObject.getDouble("ubi_x"), jObject.getDouble("ubi_y"));
                                    est.setUbicacion(ubi);

                                    final int idEst = i + 1;
                                    est.setIdEstacionamiento(idEst);

                                    estacionamientos.add(est);//almacenamos el estacionamiento en la lista
                                    int estDisp = est.getCantidadEstacionamiento() - est.getOcupados();

                                    Marker m2 = mMap.addMarker(new MarkerOptions()
                                            .position(ubi)
                                            .title(est.getNombreEstacionamiento() + "," + est.getIdEstacionamiento())
                                            .snippet("Cantidad de Estacionamientos: " + est.getCantidadEstacionamiento() + "\n" +
                                                    "Estacionamientos Ocupados: " + est.getOcupados() + "\n" + "Estacionamientos Disponibles: " + estDisp)
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


                                    mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {

                                            Intent intent = new Intent(mapa.this, editarEstacionamActivity.class);

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


        final JSONObject[] jsonObj2 = {null};



        t2 = new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() { // This thread runs in the UI
                    @Override
                    public void run() {
        try {
            //String rest2 = new HttpGetRuta(mcontext, mapa.this).execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + latOrigen + "," + longOrigen + "&destination=" + latDestino + "," + longDestino + "&mode=driving&avoid=highways|tolls").get();
            String rest2 = new HttpGetRuta(mcontext, mapa.this).execute("https://maps.googleapis.com/maps/api/directions/json?origin=-33.448649,%20-70.725299&destination=-33.450526,-70.688042&mode=driving&avoid=highways|tolls").get();


            try {
                jsonObj2[0] = new JSONObject(rest2);//jRest.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonObj2[0] != null) {



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

                jArrayRoutes = (JSONArray) jsonObj2[0].get("routes");
                jObjGet = (JSONObject)jArrayRoutes.get(0);
                jArrayLegs = (JSONArray)jObjGet.get("legs");
                jObjectLegs = (JSONObject) jArrayLegs.get(0);
                jArraySteps = (JSONArray)jObjectLegs.get("steps");
                Log.e("JARRAYSTEP","contenido:"+jArraySteps.toString());
                Log.e("Luego del jArray","STEPS");
                /*/              SOLUCION 2
                jObjectRoute = jsonObj2[0].getJSONObject("routes");
                jObjectLegs = jObjectRoute.getJSONObject("legs");
                jArraySteps = jObjectRoute.getJSONArray("steps");
*/

                /*            SOLUCION 1
                jArrayRoutes = jsonObj2[0].getJSONArray("routes");
                jObjectRoute =  jArrayRoutes.getJSONObject(0);


                jArrayLegs = jObjectRoute.getJSONArray("legs");
                jObjectLegs = jArrayLegs.getJSONObject(0);

                jArraySteps = jObjectLegs.getJSONArray("steps");

*/

                Log.e("ANTES DEL FOR","FOR QUE AGREGA CADA PUNTO");

                for (int i = 0; i < jArraySteps.length(); i++) {
                    //jObjectStep = jArraySteps.getJSONObject(i);
                    jObjectStep = (JSONObject)jArraySteps.get(i);
                    Log.e("Luego del objStep","Objstem");
                    if(i==0){
                        //Log.e("Antes del array","anntes");
                        //jArrayStartLocation = (JSONArray)jObjectStep.get("start_location");

                        //AQUI SE CAEEEEEE OEEEEEEEEEE
                        //Log.e("Luego del object","start_location"+jObjectStep.getJSONArray("start_location"));

                        jObjectStartLocation = jObjectStep.getJSONObject("start_location");
                        Log.e("luego del objectStart","lat="+jObjectStartLocation.getDouble("lat"));
                        //jObjectStartLocation = jArrayStartLocation.getJSONObject(0);

                        Log.e("LUEGO DEL JOBJECT","i="+i);
                        puntoOrigen = new LatLng(jObjectStartLocation.getDouble("lat"),jObjectStartLocation.getDouble("lng"));

                        Log.e("PUNTO ORIGEN","luego del punto origen");

                        //latOrigen = jObjectStartLocation.getDouble("lat");
                        //longOrigen = jObjectStartLocation.getDouble("lng");

                        rectOptions = new PolylineOptions()
                                .add(puntoOrigen); // Closes the polyline.

                    }
                    Log.e("Luego del if","i!=0 /// i="+i);

                    //jArrayEndLocation = (JSONArray) jObjectStep.get("end_location");

                    jObjectEndLocation = jObjectStep.getJSONObject("end_location");

                    Log.e("Luego del objEnd","i="+i);

                    puntoDestino = new LatLng(jObjectEndLocation.getDouble("lat"),jObjectEndLocation.getDouble("lng"));
                    //latDestino = jObjectEndLocation.getDouble("lat");
                    //longDestino = jObjectEndLocation.getDouble("lng");

                    rectOptions.add(puntoDestino);


            }
                Polyline polyline = mMap.addPolyline(rectOptions);
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
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //Intent intent = new Intent(this,MainActivity.class);
        //startActivity(intent);
    }


}



