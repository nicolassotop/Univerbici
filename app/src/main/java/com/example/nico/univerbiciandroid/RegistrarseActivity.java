package com.example.nico.univerbiciandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Date;

public class RegistrarseActivity extends AppCompatActivity implements View.OnClickListener{

    Button botonGuardar;
    Button botonCancelar;

    EditText nombreIngresa;
    EditText apellidoIngresa;
    RadioButton sexoIngresaHombre;
    RadioButton sexoIngresaMujer;
    EditText nickIngresa;
    EditText correoIngresa;
    EditText passIngresa;
    EditText dirIngresa;
    EditText telIngresa;
    EditText nacimientoIngresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        botonGuardar = (Button)findViewById(R.id.aceptaIn);
        //Registrando la escucha sobre la actividad Main
        botonGuardar.setOnClickListener(this);

        botonCancelar = (Button)findViewById(R.id.cancelaIn);
        //Registrando la escucha sobre la actividad Main
        botonCancelar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v){
        if(v.getId() == R.id.aceptaIn) {
            /*Intent intent = new Intent(this, mapa.class);
            startActivity(intent);
            Pantalla bienvenida

            */
        }
        if(v.getId() == R.id.cancelaIn){

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        }

    }
}
