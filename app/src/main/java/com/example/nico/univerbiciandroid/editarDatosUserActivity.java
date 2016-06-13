package com.example.nico.univerbiciandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class editarDatosUserActivity extends AppCompatActivity implements View.OnClickListener {
    private Button botonAceptar;
    private Button botonVolver;

    private EditText correo;
    private EditText dire;
    private EditText fono;
    private EditText pass;

    private String correoIngresado;
    private String direccionIngresada;
    private String fonoIngresado;
    private String passIngresada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_datos_user);

        correo = (EditText) findViewById(R.id.editCorreo);
        dire = (EditText) findViewById(R.id.editDire);
        fono = (EditText) findViewById(R.id.editTelefono);
        pass = (EditText) findViewById(R.id.editPass);

        botonAceptar = (Button) findViewById(R.id.buttonAceptoEditar);
        botonAceptar.setOnClickListener(this);

        botonVolver = (Button) findViewById(R.id.buttonCancelarEditar);
        botonVolver.setOnClickListener(this);


    }

    public void onClick(View v) {
        if (v.getId() == R.id.buttonAceptoEditar) {
            //REST PUT


        }
        if (v.getId() == R.id.buttonCancelarEditar) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
    }
    public void onBackPressed() {
        //onNavigateUp();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
