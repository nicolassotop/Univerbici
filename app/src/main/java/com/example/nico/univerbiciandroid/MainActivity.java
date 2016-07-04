package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Handler handler;
    private Thread t;
    private Button botonMapa;
    private Button botonRuta;
    private Button botonAmigos;
    private Button botonEditar;
    private Button botonCalifico;
    private TextView bienvenido;
    private JSONObject jObjUserLogged;

    private Context mcontextLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bienvenido = (TextView)findViewById(R.id.bienvenidoTextV);
        bienvenido.setText("Bienvenido "+Login.getNickUserLogged());

        //Obteniendo una instancia del boton
        botonMapa = (Button)findViewById(R.id.botonMapa);

        //Registrando la escucha sobre la actividad Main
        botonMapa.setOnClickListener(this);


        botonRuta = (Button)findViewById(R.id.buttonRuta);
        botonRuta.setOnClickListener(this);

        botonAmigos = (Button)findViewById(R.id.buttonAmigos);
        botonAmigos.setOnClickListener(this);

        botonEditar = (Button)findViewById(R.id.buttonEditar);
        botonEditar.setOnClickListener(this);

        botonCalifico = (Button)findViewById(R.id.botonCalificarRutaMain);
        botonCalifico.setOnClickListener(this);



                String rest = null;

                try {
                    //realizo un get para obtener todos los datos del usuario que ingresa a partir del id
                    rest = new HttpGet(mcontextLog, MainActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/usuarios/"+Login.getIdUserLogged()).get();


                    //Se guarda el resultado en un jsonObject
                    jObjUserLogged = new JSONObject(rest);


                    if (jObjUserLogged != null) {
                        Login.setLatUserLogged(jObjUserLogged.getDouble("ubi_xUsuario"));
                        Login.setLngUserLogged(jObjUserLogged.getDouble("ubi_yUsuario"));

                        //Se pasa al login, variable estatica para accederla desde cualquier parte
                        Login.setJsonUserLog(jObjUserLogged);


                    }

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
        if (v.getId() == R.id.botonMapa) {
            //mapa -> ver estacionamientos
            Intent intent = new Intent(this, mapa.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonRuta) {
            //Ver mejor ruta
            Intent intent = new Intent(this, destinoRutaActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonAmigos) {
            //BUSCAR AMIGOS CERCA
            Intent intent = new Intent(this, amigosCercaActivity.class);
            startActivity(intent);

        }
        if (v.getId() == R.id.buttonEditar) {
            //EDITAR DATOS
            //SOLO EMAIL; PASS; DIR Y TEL
            Intent intent = new Intent(this, editarDatosUserActivity.class);
            startActivity(intent);

        }
        if (v.getId() == R.id.botonCalificarRutaMain) {
            //calificar rutas
            Intent intent = new Intent(this, calificaRutaActivity.class);
            startActivity(intent);

        }
    }
    public void onBackPressed() {
        //vuelvo al login
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

}
