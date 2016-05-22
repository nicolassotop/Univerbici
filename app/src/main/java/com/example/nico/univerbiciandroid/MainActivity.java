package com.example.nico.univerbiciandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button botonMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obteniendo una instancia del boton show_pet_button
        botonMapa = (Button)findViewById(R.id.botonMapa);

        //Registrando la escucha sobre la actividad Main
        botonMapa.setOnClickListener(this);
    }



    @Override
    public void onClick(View v){

        //Iniciando la actividad Visor
        Intent intent = new Intent(this,mapa.class);
        startActivity(intent);

    }


}
