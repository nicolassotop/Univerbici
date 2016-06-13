package com.example.nico.univerbiciandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button botonMapa;
    private Button botonRuta;
    private Button botonAmigos;
    private Button botonEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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
    }



    @Override
    public void onClick(View v){
        if(v.getId() == R.id.botonMapa) {
            Intent intent = new Intent(this, mapa.class);
            startActivity(intent);
        }

        if(v.getId() == R.id.buttonRuta) {
            Intent intent = new Intent(this, mapsRuta.class);
            startActivity(intent);
        }

        if (v.getId()== R.id.buttonAmigos){
            //BUSCAR AMIGOS CERCA
            Intent intent = new Intent(this, amigosCercaActivity.class);
            startActivity(intent);

        }
        if (v.getId()== R.id.buttonEditar){
            //EDITAR DATOS
            //SOLO EMAIL; PASS; DIR Y TEL
        }

    }
}
