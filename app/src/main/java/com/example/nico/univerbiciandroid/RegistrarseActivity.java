package com.example.nico.univerbiciandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Handler;

public class RegistrarseActivity extends AppCompatActivity implements View.OnClickListener{

    private Button botonGuardar;
    private Button botonCancelar;

    private EditText nombreIngresa;
    private EditText apellidoIngresa;

    private RadioButton sexoIngresaHombre;
    private RadioButton sexoIngresaMujer;
    private RadioGroup grupoSexo;

    private EditText nickIngresa;
    private EditText correoIngresa;
    private EditText passIngresa;
    private EditText dirIngresa;
    private EditText telIngresa;
    private EditText nacimientoIngresa;


    private String nombre;
    private String apellido;
    private String nick;
    private String pass;
    private int sexoInt;
    private String fecha;
    private String correo;
    private String telefono;
    private String dire;
    private int idEstacionam;

    private int sexo;

    private Usuario user;

    private Thread t;
    private Handler handler;
    private Context mcontext;


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

        grupoSexo = (RadioGroup)findViewById(R.id.grupoSex);
        grupoSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.hombre){
                    //SEXO HOMBRE
                    sexo=1;

                }
                else if (checkedId == R.id.mujer){
                    //SEXO MUJER
                    sexo=0;
                }
            }
        });


        nombreIngresa = (EditText)findViewById(R.id.nombreIn);
        apellidoIngresa = (EditText)findViewById(R.id.apellidoIn);
        sexoIngresaHombre = (RadioButton) findViewById(R.id.hombre);
        sexoIngresaMujer = (RadioButton) findViewById(R.id.mujer);
        nickIngresa = (EditText)findViewById(R.id.nickIn);
        correoIngresa = (EditText)findViewById(R.id.correoIn);
        passIngresa = (EditText)findViewById(R.id.passIn);
        dirIngresa = (EditText)findViewById(R.id.dirIn);
        telIngresa= (EditText)findViewById(R.id.telIn);
        nacimientoIngresa =(EditText)findViewById(R.id.nacimientoIn);
        idEstacionam=1;

    }


    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.aceptaIn) {

            nombre=nombreIngresa.getText().toString();
            apellido=apellidoIngresa.getText().toString();
            nick=nickIngresa.getText().toString();
            correo=correoIngresa.getText().toString();
            pass=passIngresa.getText().toString();
            dire=dirIngresa.getText().toString();
            telefono=telIngresa.getText().toString();
            fecha=nacimientoIngresa.getText().toString();
            sexoInt=sexo;

            JSONObject jsonUser = new JSONObject();

            try {
                jsonUser.put("nombre",nombre);
                jsonUser.put("apellido",apellido);
                jsonUser.put("nickname",nick);
                jsonUser.put("email",correo);
                jsonUser.put("password",pass);
                jsonUser.put("direccion",dire);
                jsonUser.put("telefono",telefono);
                jsonUser.put("fechaNacimiento",fecha);
                jsonUser.put("sexo",sexoInt);
                jsonUser.put("estacionamiento_idestacionamiento",1);


                //Log.e("CONTENIDO JSON",jsonUser.get("nombre"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            new HttpPost(mcontext, jsonUser,RegistrarseActivity.this).execute("http://192.168.0.15:8080/sakila-backend-master/usuarios");


            /*Intent intent = new Intent(this, mapa.class);
            startActivity(intent);
            Pantalla bienvenida, mostrar mensaje




            t = new Thread(new Runnable() {
                public void run() {
                    handler.post(new Runnable() { // This thread runs in the UI
                        @Override
                        public void run() {

                            JSONObject jsonParam = new JSONObject();

                            nombreIngresa = (EditText)findViewById(R.id.nombreIn);
                            apellidoIngresa = (EditText)findViewById(R.id.apellidoIn);
                            sexoIngresaHombre = (RadioButton) findViewById(R.id.hombre);
                            sexoIngresaMujer = (RadioButton) findViewById(R.id.mujer);
                            nickIngresa = (EditText)findViewById(R.id.nickIn);
                            correoIngresa = (EditText)findViewById(R.id.correoIn);
                            passIngresa = (EditText)findViewById(R.id.passIn);
                            dirIngresa = (EditText)findViewById(R.id.dirIn);
                            telIngresa= (EditText)findViewById(R.id.telIn);
                            nacimientoIngresa =(EditText)findViewById(R.id.nacimientoIn);


                            String nombre = nombreIngresa.getText().toString();
                            String apellido = apellidoIngresa.getText().toString();
                            String nick = nickIngresa.getText().toString();
                            String correo = correoIngresa.getText().toString();
                            String pass = passIngresa.getText().toString();
                            String dir = dirIngresa.getText().toString();
                            int tel = Integer.parseInt(telIngresa.getText().toString());
                            String nac= nacimientoIngresa.getText().toString();



                            Usuario nuevoUser = new Usuario(nombre, apellido, nick, pass, correo,
                                    dir, sexo, tel, nac);


                            try {
                                jsonParam.put("nombre", nombre);
                                jsonParam.put("apellido", apellido);
                                jsonParam.put("nickname", nick);
                                jsonParam.put("email", correo);
                                jsonParam.put("password", pass);
                                jsonParam.put("direccion", dir);
                                jsonParam.put("Telefono", tel);
                                jsonParam.put("fechaNacimiento", nac);
                                jsonParam.put("sexo",sexo);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new HttpPost(mcontext,jsonParam, RegistrarseActivity.this).execute("http://192.168.0.15:8080/sakila-backend-master/usuarios/");

                        }
                    });
                }
            });
            t.start();




*/
            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        }
        if(v.getId() == R.id.cancelaIn){

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        }

    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
