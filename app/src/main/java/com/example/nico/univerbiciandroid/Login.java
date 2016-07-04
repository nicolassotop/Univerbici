package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity implements View.OnClickListener{

    static JSONObject jsonUserLog;


    private Button botonRegistro;
    private Button botonLogea;
    private EditText nicknameIngresa;
    private EditText passIngresa;

    private String passIn;
    private String nickIn;
    private String nickDB;
    private String passDB;

    static String nickUserLogged;
    static String idUserLogged;
    static String estadoUserLogged;
    static String emailUserLogged;
    static double latUserLogged;
    static double lngUserLogged;

    //getter y setter

    public static JSONObject getJsonUserLog() {
        return jsonUserLog;
    }

    public static void setJsonUserLog(JSONObject jsonUserLog) {
        Login.jsonUserLog = jsonUserLog;
    }

    public static double getLatUserLogged() {
        return latUserLogged;
    }

    public static void setLatUserLogged(double latUserLogged) {
        Login.latUserLogged = latUserLogged;
    }

    public static double getLngUserLogged() {
        return lngUserLogged;
    }

    public static void setLngUserLogged(double lngUserLogged) {
        Login.lngUserLogged = lngUserLogged;
    }

    public static String getNickUserLogged() {
        return nickUserLogged;
    }

    public static void setNickUserLogged(String nickUserLogged) {
        Login.nickUserLogged = nickUserLogged;
    }

    public static String getIdUserLogged() {
        return idUserLogged;
    }

    public static void setIdUserLogged(String idUserLogged) {
        Login.idUserLogged = idUserLogged;
    }

    public static String getEstadoUserLogged() {
        return estadoUserLogged;
    }

    public static void setEstadoUserLogged(String estadoUserLogged) {
        Login.estadoUserLogged = estadoUserLogged;
    }

    public static String getEmailUserLogged() {
        return emailUserLogged;
    }

    public static void setEmailUserLogged(String emailUserLogged) {
        Login.emailUserLogged = emailUserLogged;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //botones
        botonRegistro = (Button)findViewById(R.id.buttonIngresa);
        botonRegistro.setOnClickListener(this);
        botonLogea = (Button)findViewById(R.id.registraButton);
        botonLogea.setOnClickListener(this);

        //Edittext para ingresar nickname y pass
        nicknameIngresa = (EditText)findViewById(R.id.usuarioIn);
        passIngresa = (EditText)findViewById(R.id.passIn);

    }

    @Override
    public void onClick(View v){

        //Si selecciona ingresar
        if(v.getId() == R.id.buttonIngresa) {

            //Se obtienen los datos desde los edittext
            nickIn = nicknameIngresa.getText().toString();
            passIn = passIngresa.getText().toString();

            //Si no ingresa alguno de los 2 campos
            if (nickIn.equals("") || passIn.equals("")) {
                Toast.makeText(this, "Debe rellenar todos los campos", Toast.LENGTH_LONG).show();
            }else{
                //Si ingresa ambos creo un JSONObject con ambos datos
                jsonUserLog = new JSONObject();
                try {
                    jsonUserLog.put("nickname", nickIn);
                    jsonUserLog.put("password", passIn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Se envia el POST con los datos al servicio REST
                new HttpPostLogin(this, jsonUserLog, Login.this).execute("http://192.168.0.15:9090/sakila-backend-master/usuarios/login");

                //Para saber cuando se tiene la respuesta del POST
                int i = 0;
                while (HttpPostLogin.getReady() != 1) {
                    i++;
                }

                //Se muestran Toast dependiendo del resultado del POST
                if (estadoUserLogged.equals("La password no corresponde")) {
                    Toast.makeText(this, "La contraseña no corresponde, vuelva a intentarlo", Toast.LENGTH_LONG).show();
                }

                if (estadoUserLogged.equals("No existe un usuario con ese username")) {
                    Toast.makeText(this, "No existe un usuario con ese nickname", Toast.LENGTH_LONG).show();
                }
                if (estadoUserLogged.equals("Loggeado")) {
                    Toast.makeText(this, "Ha ingresado exitosamente", Toast.LENGTH_LONG).show();

                    //Si coincide el nick y la pass se ingresa a la siguiente ventana: MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                }

            }

        }

        //Si selecciona registrar
        if (v.getId() == R.id.registraButton){
            Intent intent = new Intent(this,RegistrarseActivity.class);
            startActivity(intent);

        }

    }
    public void onBackPressed() {
        //FINALIZAR APP
        //onNavigateUp();
        //Intent intent = new Intent(this,destinoRutaActivity.class);
        //startActivity(intent);
        //android.os.Process.killProcess(android.os.Process.myPid());

    }
}
