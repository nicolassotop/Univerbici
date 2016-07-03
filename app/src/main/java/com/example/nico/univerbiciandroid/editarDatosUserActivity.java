package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

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

    private JSONObject jsonEdit;
    Context mContext;


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


            correoIngresado = correo.getText().toString();
            direccionIngresada = dire.getText().toString();
            fonoIngresado = fono.getText().toString();
            passIngresada = pass.getText().toString();

            jsonEdit = new JSONObject();
            jsonEdit= Login.getJsonUserLog();

            try {
                jsonEdit.put("email",correoIngresado);
                jsonEdit.put("direccion",direccionIngresada);
                jsonEdit.put("telefono",fonoIngresado);
                jsonEdit.put("password",passIngresada);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            new HttpPut(mContext, jsonEdit,editarDatosUserActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/usuarios/"+Login.getIdUserLogged());

            Toast.makeText(this, "Datos editados exitosamente", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

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
